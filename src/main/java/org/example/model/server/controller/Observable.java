package org.example.model.server.controller;

import org.example.model.user.model.Observer;

/**
 * Интерфейс для реализации паттерна Наблюдателя
 */
public interface Observable {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String message);
}
