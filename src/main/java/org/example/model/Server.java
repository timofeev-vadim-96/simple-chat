package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс сервера
 */
public class Server {
    private boolean status;
    private ServerLogger serverLogger;
    private List<User> users;

    /**
     * Конструктор сервера
     */
    public Server() {
        status = false;
        serverLogger = new ServerLogger("serverLogger.txt", "chatHistory.txt");
        users = new ArrayList<>();
    }

    /**
     * Метод для логирования сообщений
     *
     * @param text сообщение
     */
    public void writerLogg(String text) {
        if (status) {
            serverLogger.writerLogg(text, true);
        }
    }

    /**
     * Метод для сохранения истории чата
     *
     * @param text сообщение
     */
    public void writeHistory(String text) {
        if (status) {
            serverLogger.writeHistory(text, true);
        }
    }

    /**
     * Метод для добавления пользователя в реестр для дальнейшей авторизации
     *
     * @param user объект пользователя
     */
    public void addNewUser(User user) {
        users.add(user);
    }

    /**
     * Метод для входа пользователя в чат
     *
     * @param user пользователь
     * @return true, если это новый пользователь и он не эквивалентен дефолтному значению полей, либо логин и пароль
     * соответствуют одному из аккаунтов в базе
     */
    public boolean loginUser(User user) {
        User defaultUser = new User(User.defaultLogin, User.defaultPassword, null);
        //если пользователь ничего не вводил, но нажал логин
        if (user.getLogin().equals(defaultUser.getLogin())) {
            return false;
        }
        for (User usr : users) {
            //если пользователь с таким логином уже существует, но пароль введен неверно
            if (user.getLogin().equals(usr.getLogin()) && !user.getPassword().equals(usr.getPassword())) {
                return false;
            }
            //если пользователь полностью идентичен
            else if (user.equals(usr)) return true;
        }
        //если такой пользователь не существует - создать его
        addNewUser(user);
        return true;
    }

    /**
     * Метод для синхронизации пользовательских чатов с сервером
     *
     * @param text сообщение
     */
    public void sendUsers(String text) {
        for (User user : users) {
            user.getUserWindow().chatAppend(text);
        }
    }

    /**
     * Метод для восстановления истории чата
     *
     * @return
     */
    public List<String> getHistory() {
        return serverLogger.getHistory();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
