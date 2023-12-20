package org.example.frontend;

import org.example.model.Server;
import org.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Класс для создания пользовательского окна чата
 */
public class UserWindow extends JFrame {
    private static final int WIDTH = 450;
    private static final int HEIGHT = 300;
    private Server server;
    private ServerWindow serverWindow;
    private JTextArea chat;
    private User currentUser;

    /**
     * Конструктор пользовательского окна чата
     *
     * @param serverWindow ссылка на объект окна сервера
     */
    public UserWindow(ServerWindow serverWindow) {
        this.serverWindow = serverWindow;
        server = serverWindow.getServer();
        currentUser = new User("Unknown", this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Chat user");

        add(createNorthComponent(), BorderLayout.NORTH);
        add(createSouthComponent(), BorderLayout.SOUTH);

        chat = new JTextArea();
        JScrollPane scrollChat = new JScrollPane(chat);
        add(scrollChat);

        setResizable(false);
        setVisible(true);
    }

    /**
     * Создание компонента с блоком из кнопок по вводу текстового сообщения и его отправки
     *
     * @return south component
     */
    private Component createSouthComponent() {
        JPanel southComponent = new JPanel(new FlowLayout());
        String textFieldDefault = "Введите Ваше сообщение:  ";
        JTextField textField = new JTextField(textFieldDefault, 30);
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                textField.setText("");
            }
        });
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //если текст соответствует дефолтному,то ничего не отправляем
                if (textField.getText().equals(textFieldDefault)) {
                } else if (currentUser.getLogin().equals("Unknown")) {
                } else if (!server.getStatus())
                    JOptionPane.showMessageDialog(sendButton, "Проверьте статус сервера!\n");
                else {
                    serverWindow.appendToChat(String.format("%s: %s\n", currentUser.getLogin(), textField.getText()));
                }
            }
        });
        southComponent.add(textField);
        southComponent.add(sendButton);
        return southComponent;
    }

    /**
     * Создания всего верхнего блока по вводу ip-адреса, сокета, логина, пароля и кнопки входа
     *
     * @return entire north component
     */
    private Component createNorthComponent() {
        JPanel northComponent = new JPanel(new GridLayout(2, 1));
        northComponent.add(createFirstNorthComponent());
        northComponent.add(createSecondNorthComponent());
        return northComponent;
    }

    /**
     * Метод для создания блока из полей для ввода ip-адреса и сокета, а также кнопки-заглушки для красивого отображения
     *
     * @return first north component
     */
    private Component createFirstNorthComponent() {
        JPanel component = new JPanel(new GridLayout(1, 3));
        JTextField ipAddress = new JTextField("127.0.0.1");
        ipAddress.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ipAddress.setText("");
            }
        });
        JTextField socket = new JTextField("1234");
        socket.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                socket.setText("");
            }
        });
        JButton invisible = new JButton();
        invisible.setVisible(false);
        component.add(ipAddress);
        component.add(socket);
        component.add(invisible);
        return component;
    }

    /**
     * Метод для создания второго верхнего блока полей для ввода логина, пароля и входа
     *
     * @return second north component
     */
    private Component createSecondNorthComponent() {
        JPanel component = new JPanel(new GridLayout(1, 3));
        JTextField login = new JTextField(User.defaultLogin);
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                login.setText("");
            }
        });
        JPasswordField password = new JPasswordField(User.defaultPassword);
        password.setEchoChar('*');
        password.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                password.setText("");
            }
        });
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = new User(login.getText(), new String(password.getPassword()), UserWindow.this);
                if (!server.getStatus()) chatAppend("Подключение не удалось\n");
                else if (server.loginUser(user)) {
                    currentUser = user;
                    serverWindow.appendToChat(String.format("%s подключился к беседе\n", user.getLogin()));
                    chat.setText(""); //очищаем переписку
                    restoreChatHistoryFromServer(server.getHistory());
                } else JOptionPane.showMessageDialog(loginButton, "Логин или пароль введены не верно.\n");
            }
        });
        component.add(login);
        component.add(password);
        component.add(loginButton);
        return component;
    }

    /**
     * Метод для добавления текста в пользовательский чат
     *
     * @param text сообщение
     */
    public void chatAppend(String text) {
        chat.append(text);
    }

    /**
     * Метод для восстановления истории переписки при входе нового пользователя
     *
     * @param history история переписки из файла на стороне сервера
     */
    private void restoreChatHistoryFromServer(List<String> history) {
        for (String line : history) {
            chatAppend(line + "\n");
        }
    }
}
