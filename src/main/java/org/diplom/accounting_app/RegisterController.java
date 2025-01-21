package org.diplom.accounting_app;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    public void onRegisterButtonClick() throws IOException {
        String username = usernameField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();
        Integer money = 0;

        if (registerUser(username, login, password, money)) {
            MainApplication.setRoot("login-view", "Авторизация");
        } else {
            showAlert("Ошибка регистрации","Пользователь с таким логином уже существует.");
            System.out.println("Ошибка регистрации: пользователь с таким логином уже существует.");
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private boolean registerUser(String username, String login, String password, Integer money) {
        String checkLoginQuery = "SELECT COUNT(*) FROM Users WHERE Login = ?";
        String insertUserQuery = "INSERT INTO Users (Name, Login, Password, Money) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkLoginQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertUserQuery)) {

            // Проверяем, существует ли уже пользователь с таким логином
            checkStmt.setString(1, login);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }

            insertStmt.setString(1, username);
            insertStmt.setString(2, login);
            insertStmt.setString(3, password);
            insertStmt.setInt(4, money);
            insertStmt.executeUpdate();

            return true; // Успешная регистрация
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Ошибка при регистрации
        }
    }

    public void onBackButtonClick() throws IOException {
        MainApplication.setRoot("login-view", "Авторизация");
    }
}
//Здесь где то потерялась проверка на NULL :)