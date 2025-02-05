package org.diplom.accounting_app.dao;

import io.ebean.DB;
import org.diplom.accounting_app.models.User;

public class UserDAO {

    public static User findUserByLoginAndPassword(String login, String password) {
        return DB.find(User.class)
                .where()
                .eq("login", login)
                .eq("password", password)
                .findOne();
    }

    public static boolean isValidCredentials(String login, String password) {
        return findUserByLoginAndPassword(login, password) != null;
    }

    public static boolean registerUser(String username, String login, String password, Integer money) {
        if (username == null || login == null || password == null) {
            return false;
        }

        // Check if user exists
        User existingUser = DB.find(User.class)
                .where()
                .eq("login", login)
                .findOne();

        if (existingUser != null) {
            return false;
        }

        // Create new user
        User user = new User();
        user.setName(username);
        user.setLogin(login);
        user.setPassword(password);
        user.setMoney(money);

        try {
            user.save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
