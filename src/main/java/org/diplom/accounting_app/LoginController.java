package org.diplom.accounting_app;

import javafx.fxml.FXML;
import database.DatabaseConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidCredentials(username, password)) {
            System.out.println("Успешный вход!");
            //MainApplication.setRoot("menu-view", "Главное меню");
        } else {
            showAlert("Ошибка авторизации","Указаны неверный логин или пароль");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidCredentials(String name, String password) {
        String query = "SELECT COUNT(*) FROM Users WHERE Login = ? AND Password = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Если count > 0, то пользователь найден
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Если ошибка или нет таких данных
    }

    public void onRegisterButtonClick() throws IOException {
        MainApplication.setRoot("register-view", "Регистрация");
    }
}
