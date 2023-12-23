package org.example.model.server.controller;

import org.example.dao.Logger;
import org.example.dao.impl.HistoryLogger;
import org.example.dao.impl.ServiceLogger;
import org.example.model.server.view.ServerUI;
import org.example.model.server.view.ViewServerUI;
import org.example.model.user.model.Observer;
import org.example.model.user.model.User;
import org.example.util.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс сервера, реализующий наблюдаемый объект
 */
public class Server implements Observable {
    private boolean status;
    private final Logger serverLogger;
    private final Logger historyLogger;
    private final List<Observer> observers;
    private final ViewServerUI viewServerUI;

    /**
     * Конструктор сервера
     */
    public Server() {
        this.viewServerUI = new ServerUI(this);
        status = false;
        serverLogger = new ServiceLogger("serverLogger.txt");
        historyLogger = new HistoryLogger("chatHistory.txt");
        observers = new ArrayList<>();
    }

    /**
     * Метод для логирования сообщений
     *
     * @param text сообщение
     */
    public void writeLogg(String text) {
        if (status) {
            serverLogger.write(text, true);
        }
    }

    /**
     * Метод для сохранения истории чата
     *
     * @param text сообщение
     */
    public void writeHistory(String text) {
        if (status) {
            historyLogger.write(text, true);
        }
    }

    /**
     * Метод для добавления наблюдателей в список
     *
     * @param observer объект наблюдателя
     */
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Метод для удаления наблюдателей из списка
     *
     * @param observer объект наблюдателя
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Метод для входа пользователя в чат
     *
     * @param inputUser новый пользователь
     * @param password  отдельно его пароль для проверки, т.к. после инкапсуляции в объект, доступа к нему не будет
     * @return true, если это новый пользователь и он не эквивалентен дефолтному значению полей, либо логин и пароль
     * соответствуют одному из аккаунтов в базе
     */
    public boolean logIn(User inputUser, String password) {
        //если пользователь ничего не вводил, но нажал логин
        if (inputUser.getLogin().equals(User.defaultLogin) && password.equals(User.defaultPassword)) {
            return false;
        }
        for (Observer observer : observers) {
            if (observer instanceof User user) {
                //если пользователь с таким логином уже существует, но пароль введен неверно
                if (user.getLogin().equals(inputUser.getLogin()) && !inputUser.comparePasswords(password)) {
                    return false;
                }
                //если пользователь полностью идентичен
                else if (user.getLogin().equals(inputUser.getLogin()) && inputUser.comparePasswords(password)) {
                    getMessage(String.format("%s подключился к беседе\n", inputUser.getLogin()));
                    return true;
                }
            }
        }
        //если такой пользователь не существует - создать его
        registerObserver(inputUser);
        getMessage(String.format("%s подключился к беседе\n", inputUser.getLogin()));
        return true;
    }

    /**
     * Метод для получения сообщения от пользователей, его рассылки и логирования
     *
     * @param message сообщение
     */
    public void getMessage(String message) {
        viewServerUI.handleMessage(message);
        notifyObservers(message);
        writeLogg(message);
        writeHistory(message);
    }

    /**
     * Перегруженный метод для получения сообщения в виде объекта от пользователей, его рассылки и логирования
     *
     * @param message сообщение
     */
    public void getMessage(Message message) {
        viewServerUI.handleMessage(message.toString());
        notifyObservers(message.toString());
        writeLogg(message.toString());
        writeHistory(message.toString());
    }

    /**
     * Метод для оповещения наблюдателей об изменении
     *
     * @param text сообщение
     */
    public void notifyObservers(String text) {
        for (Observer observer : observers) {
            if (observer instanceof User user) user.update(text);
        }
    }

    /**
     * Метод для восстановления истории чата
     *
     * @return список сообщений чата
     */
    public List<String> getHistory() {
        return historyLogger.getLogs();
    }

    /**
     * Метод для получения лога истории
     *
     * @return список логов
     */
    public List<String> getLogs() {
        return serverLogger.getLogs();
    }

    public void setStatus(boolean status) {
        if (!status) {
            for (Observer observer : observers) {
                if (observer instanceof User user) user.disconnectFromServer();
            }
        }
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
