package org.example.model.user.model;

/**
 * Класс наблюдателя для реализации соответствующего паттерна
 */
public interface Observer {
    void update (String news);
}