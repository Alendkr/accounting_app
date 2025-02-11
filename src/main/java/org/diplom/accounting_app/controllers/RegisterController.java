package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.diplom.accounting_app.MainApplication;
import org.diplom.accounting_app.dao.UserDAO;

import java.io.IOException;

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

        if (UserDAO.registerUser(username, login, password, money)) {
            MainApplication.setRoot("login-view", "Авторизация");
        } else {
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

    public void onBackButtonClick() throws IOException {
        MainApplication.setRoot("login-view", "Авторизация");
    }
}
