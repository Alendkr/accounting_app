package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.diplom.accounting_app.MainApplication;
import org.diplom.accounting_app.services.UserService;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    private final UserService userService = new UserService();

    @FXML
    public void onRegisterButtonClick() throws IOException {
        String username = usernameField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();

        // Проверяем, что поля не пустые
        if (username.isEmpty() || login.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка регистрации", "Все поля должны быть заполнены.");
            return;
        }

        // Регистрируем пользователя через сервис
        boolean isRegistered = userService.registerUser(username, login, password);

        if (isRegistered) {
            // Если регистрация успешна, переходим на экран логина
            showAlert("Успех", "Регистрация прошла успешно!");
            MainApplication.setRoot("login-view", "Авторизация");
        } else {
            // В случае ошибки при регистрации
            showAlert("Ошибка регистрации", "Пользователь с таким логином уже существует или некорректные данные.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        MainApplication.setRoot("login-view", "Авторизация");
    }
}