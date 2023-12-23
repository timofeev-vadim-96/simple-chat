package org.example.model.user.model;

import org.example.model.server.controller.Server;
import org.example.model.user.view.ViewUserUI;
import org.example.util.Message;

import java.util.List;

/**
 * Класс пользователя
 */
public class User implements Observer {
    private final String login;
    private final String password;
    private final ViewUserUI viewUserUI;
    private final Server server;
    private boolean connected;

    public static final String defaultLogin = "your_login";
    public static final String defaultPassword = "your_password";

    /**
     * Конструктор пользователя
     *
     * @param login      логин
     * @param password   пароль
     * @param server     сервер
     * @param viewUserUI объект графического интерфейса данного пользователя
     */
    public User(String login, String password, ViewUserUI viewUserUI, Server server) {
        this.login = login;
        this.password = password;
        this.viewUserUI = viewUserUI;
        this.server = server;
    }

    /**
     * Конструктор пользователя
     *
     * @param login      логин
     * @param server     сервер
     * @param viewUserUI объект графического интерфейса данного пользователя
     */
    public User(String login, ViewUserUI viewUserUI, Server server) {
        this.login = login;
        this.password = "";
        this.viewUserUI = viewUserUI;
        this.server = server;
    }

    /**
     * Метод подключения пользователя к серверу
     *
     * @return результат попытки подключения
     */
    public boolean connectToServer() {
        boolean connect = server.logIn(this, password);
        if (connect) {
            connected = true;
            viewUserUI.connectedToServer();
        }
        return connect;
    }

    /**
     * Метод отключения пользователя от сервера
     */
    public void disconnectFromServer() {
        if (connected) {
            connected = false;
            viewUserUI.disconnectedFromServer();
            viewUserUI.handleMessage("Вы были отключены от сервера!");
        }
    }

    /**
     * Метод получения новости от наблюдаемого объекта в рамках паттерна Наблюдателя
     *
     * @param message сообщение/новость
     */
    @Override
    public void update(String message) {
        viewUserUI.handleMessage(message);
    }

    /**
     * Метод отправки сообщения на сервер
     *
     * @param message сообщение
     */
    public void sendMessage(String message) {
        server.getMessage(new Message(login, message));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User user) {
            return user.getLogin().equals(this.login) && user.comparePasswords(this.password);
        } else return false;
    }

    /**
     * Метод для восстановления истории переписки при входе нового пользователя
     */
    public void restoreChatHistoryFromServer() {
        List<String> history = server.getHistory();
        for (String line : history) {
            update(line + "\n");
        }
    }

    public String getLogin() {
        return login;
    }

    /**
     * Метод для инкапсулированного сравнения паролей, с целью избегания геттера и сокрытия его от злоумышленников
     *
     * @param password введенный пользователем пароль
     * @return
     */
    public boolean comparePasswords(String password) {
        return this.password.equals(password);
    }

    public boolean isConnected() {
        return connected;
    }

    public Server getServer() {
        return server;
    }
}
