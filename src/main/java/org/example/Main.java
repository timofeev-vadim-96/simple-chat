package org.example;

import org.example.model.server.controller.Server;
import org.example.model.user.view.UserUI;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        UserUI user1 = new UserUI(server);
        UserUI user2 = new UserUI(server);
        //Сдвигаем пользовательские окна относительно окна сервера для удобства
        user1.setLocation(user1.getX() - 460, user1.getY());
        user2.setLocation(user2.getX() + 460, user2.getY());
    }
}