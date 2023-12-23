package org.example.util;

import java.time.LocalDateTime;

/**
 * Класс для обертки сообщения
 */
public class Message {
    private final String body;
    private final String time;
    private final String userName;

    /**
     * Конструктор сообщения
     *
     * @param userName имя пользователя
     * @param body     тело сообщения
     */
    public Message(String userName, String body) {
        this.body = body;
        this.time = getMessageTime();
        this.userName = userName;
    }

    @Override
    public String toString() {
        return String.format("%s, %s: %s", userName, time, body);
    }

    /**
     * Метод для получения параметра времени в желаемом виде
     *
     * @return форматированное текущее время
     */
    private String getMessageTime() {
        return String.format("%s:%s:%s",
                LocalDateTime.now().getHour(),
                LocalDateTime.now().getMinute(),
                LocalDateTime.now().getSecond());
    }
}
