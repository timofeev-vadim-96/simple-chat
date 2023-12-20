package org.example;

import org.example.frontend.ServerWindow;
import org.example.frontend.UserWindow;

public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
        UserWindow user1 = new UserWindow(serverWindow);
        UserWindow user2 = new UserWindow(serverWindow);
        //Сдвигаем пользовательские окна относительно окна сервера для удобства
        user1.setLocation(serverWindow.getX() - 460, serverWindow.getY());
        user2.setLocation(serverWindow.getX() + 460, serverWindow.getY());
    }
}