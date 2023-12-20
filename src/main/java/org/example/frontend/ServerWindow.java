package org.example.frontend;

import org.example.model.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс для создания окна сервера
 */
public class ServerWindow extends JFrame {
    private static final int WIDTH = 450;
    private static final int HEIGHT = 300;

    private JButton startServer;
    private JButton stopServer;
    private Server server; //привязываем к окну его сервер
    private JTextArea chat; //главное текстовое поле

    /**
     * Конструктор объекта окна сервера
     */
    public ServerWindow() {
        server = new Server();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Chat server");

        Component serverStatusSwitcher = createStatusSwitchComponent();
        add(serverStatusSwitcher, BorderLayout.SOUTH);

        chat = new JTextArea();
        JScrollPane scrollChat = new JScrollPane(chat);
        add(scrollChat);

        setResizable(false);
        setVisible(true);
    }

    /**
     * создание компонента Start/Stop server для нижней части окна Сервера
     *
     * @return south компонент
     */
    private Component createStatusSwitchComponent() {
        JPanel statusSwitcher = new JPanel(new GridLayout(1, 2));
        startServer = new JButton("Start server");
        startServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!server.getStatus()) {
                    server.setStatus(true);
                    String activeServer = "The server is active now!\n";
                    chat.append(activeServer);
                    server.writerLogg(activeServer);
                } else {
                    String alreadyActiveServer = "The server is already active!\n";
                    JOptionPane.showMessageDialog(startServer, alreadyActiveServer);
                    server.writerLogg(alreadyActiveServer);
                }
            }
        });
        stopServer = new JButton("Stop");
        stopServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.getStatus()) {
                    String disabledServer = "The server is disabled!\n";
                    chat.append(disabledServer);
                    server.writerLogg(disabledServer);
                    server.setStatus(false); //в конце алгоритма, т.к. логи пишутся только при рабочем сервере
                } else {
                    JOptionPane.showMessageDialog(startServer, "Server is already disabled!");
                }
            }
        });
        statusSwitcher.add(startServer);
        statusSwitcher.add(stopServer);
        return statusSwitcher;
    }

    public Server getServer() {
        return server;
    }

    /**
     * Метод для добавления в общий чат сообщений от пользователей, а также их сохранения в историю и их логирование
     *
     * @param userText сообщение
     */
    public void appendToChat(String userText) {
        chat.append(userText);
        server.sendUsers(userText);
        server.writerLogg(userText);
        server.writeHistory(userText);
    }
}
