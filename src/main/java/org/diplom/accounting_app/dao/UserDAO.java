package org.diplom.accounting_app.dao;

import org.diplom.accounting_app.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static boolean isValidCredentials(String login, String password) {
        String query = "SELECT COUNT(*) FROM Users WHERE Login = ? AND Password = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(String username, String login, String password, Integer money) {
        if (username == null || login == null || password == null) {
            return false; // Проверка на null
        }

        String checkLoginQuery = "SELECT COUNT(*) FROM Users WHERE Login = ?";
        String insertUserQuery = "INSERT INTO Users (Name, Login, Password, Money) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkLoginQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertUserQuery)) {

            checkStmt.setString(1, login);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Логин уже существует
            }

            insertStmt.setString(1, username);
            insertStmt.setString(2, login);
            insertStmt.setString(3, password);
            insertStmt.setInt(4, money);
            insertStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
