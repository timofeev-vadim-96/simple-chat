package org.example.model;

import org.example.frontend.UserWindow;

/**
 * Класс пользователя
 */
public class User {
    private String login;
    private String password;
    private UserWindow userWindow;
    public static final String defaultLogin = "your_login";
    public static final String defaultPassword = "your_password";

    /**
     * Конструктор пользователя
     *
     * @param login      логин
     * @param password   пароль
     * @param userWindow окно пользовательского чаата
     */
    public User(String login, String password, UserWindow userWindow) {
        this.login = login;
        this.password = password;
        this.userWindow = userWindow;
    }

    /**
     * Конструктор пользователя
     *
     * @param login      логин
     * @param userWindow окно пользовательского чата
     */
    public User(String login, UserWindow userWindow) {
        this.login = login;
        this.password = "";
        this.userWindow = userWindow;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return user.getLogin().equals(this.login) && user.getPassword().equals(this.password);
        } else return false;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UserWindow getUserWindow() {
        return userWindow;
    }
}
