package org.example.model.user.view;

/**
 * Интерфейс UI пользователя
 */
public interface ViewUserUI {
    void handleMessage(String message);
    void connectedToServer();
    void disconnectedFromServer();
}
