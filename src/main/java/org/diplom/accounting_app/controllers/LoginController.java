package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.diplom.accounting_app.MainApplication;
import org.diplom.accounting_app.dao.UserDAO;
import org.diplom.accounting_app.models.CurrentUser;
import org.diplom.accounting_app.models.User;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        String login = usernameField.getText();
        String password = passwordField.getText();

        User user = UserDAO.findUserByLoginAndPassword(login, password);

        if (user != null) {
            CurrentUser.setCurrentUser(user);  // Сохраняем пользователя
            System.out.println("Успешный вход! Пользователь: " + user.getName());

            MainApplication.setRoot("menu-view", "Главное меню");
        } else {
            showAlert("Ошибка авторизации", "Указаны неверный логин или пароль");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void onRegisterButtonClick() throws IOException {
        MainApplication.setRoot("register-view", "Регистрация");
    }
}
