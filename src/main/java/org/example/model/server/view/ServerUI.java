package org.example.model.server.view;

import org.example.model.server.controller.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс для создания окна сервера
 */
public class ServerUI extends JFrame implements ViewServerUI {
    private static final int WIDTH = 450;
    private static final int HEIGHT = 300;

    private JButton startServer;
    private JButton stopServer;
    private Server server; //привязываем к окну его сервер
    private JTextArea chat; //главное текстовое поле

    /**
     * Конструктор объекта окна сервера
     */
    public ServerUI(Server server) {
        this.server = server;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Chat server");

        Component serverStatusSwitcher = createStatusSwitchComponent();
        add(serverStatusSwitcher, BorderLayout.SOUTH);

        JScrollPane scrollChat = createCentralComponent();
        add(scrollChat);

        setResizable(false);
        setVisible(true);
    }

    /**
     * Метод, создающий центральный компонент окна с чатом
     *
     * @return не редактируемый чат с прокруткой
     */
    private JScrollPane createCentralComponent() {
        chat = new JTextArea();
        chat.setEditable(false);
        JScrollPane scrollChat = new JScrollPane(chat);
        return scrollChat;
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
                    server.writeLogg(activeServer);
                } else {
                    String alreadyActiveServer = "The server is already active!\n";
                    JOptionPane.showMessageDialog(startServer, alreadyActiveServer);
                    server.writeLogg(alreadyActiveServer);
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
                    server.writeLogg(disabledServer);
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

    /**
     * Метод для добавления в общий чат сообщений от пользователей
     *
     * @param message сообщение
     */
    public void handleMessage(String message) {
        chat.append(message);
    }
}
