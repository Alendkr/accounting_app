package org.diplom.accounting_app.services;

import org.diplom.accounting_app.dao.UserDAO;
import org.diplom.accounting_app.models.CurrentUser;
import org.diplom.accounting_app.models.User;

public class UserService {

    public boolean registerUser(String username, String login, String password) {
        // Проверяем существование логина
        User existingUser = UserDAO.findUserByLogin(login);
        if (existingUser != null) {
            return false; // Пользователь с таким логином уже существует
        }

        // Если логин уникален, создаём нового пользователя
        int initialMoney = 0;  // Начальная сумма на счёте
        return UserDAO.registerUser(username, login, password, initialMoney);
    }

    public User loginUser(String login, String password) {
        User user = UserDAO.findUserByLoginAndPassword(login, password);
        if (user != null) {
            CurrentUser.setCurrentUser(user);
        }
        return user;
    }
}