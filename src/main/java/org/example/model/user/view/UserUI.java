package org.example.model.user.view;

import org.example.model.server.controller.Server;
import org.example.model.user.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Класс для создания пользовательского окна чата
 */
public class UserUI extends JFrame implements ViewUserUI {
    private static final int WIDTH = 450;
    private static final int HEIGHT = 300;
    private JTextArea chat;
    private User user;
    private final Component northComponent;
    private final Component southComponent;
    private final Component centralComponent;

    /**
     * Конструктор пользовательского окна чата
     */
    public UserUI(Server server) {
        user = new User("Unknown", this, server);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Chat user");

        add(northComponent = createNorthComponent(), BorderLayout.NORTH);
        add(southComponent = createSouthComponent(), BorderLayout.SOUTH);
        add(centralComponent = createCentralComponent());

        setResizable(false);
        setVisible(true);
    }

    /**
     * Метод создания центрального графического компонента окна - чата
     *
     * @return чат
     */
    private JScrollPane createCentralComponent() {
        chat = new JTextArea();
        chat.setEditable(false);
        JScrollPane scrollChat = new JScrollPane(chat);
        return scrollChat;
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
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    sendMessage(textField, textField, textFieldDefault);
                }
            }
        });
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(sendButton, textField, textFieldDefault);
            }
        });
        southComponent.add(textField);
        southComponent.add(sendButton);
        return southComponent;
    }

    /**
     * Вспомогательный метод для отправки сообщения (завязан в двух случаях на графике, поэтому не вынесен в пользователя)
     *
     * @param button           компонент, к которому привязываются всплывающие сообщения
     * @param textField        текстовое поле
     * @param textFieldDefault значение текстового поля по умолчанию
     */
    public void sendMessage(Component button, JTextField textField, String textFieldDefault) {
        if (!user.isConnected())
            JOptionPane.showMessageDialog(button, "Проверьте подключение к серверу!\n");
        else if (textField.getText().equals(textFieldDefault)) {
            JOptionPane.showMessageDialog(button, "Введите новое сообщение!\n");
        } else {
            user.sendMessage(textField.getText() + "\n");
        }
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
        JTextField socketNumberTextField = new JTextField("1234");
        socketNumberTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                socketNumberTextField.setText("");
            }
        });
        JButton invisible = new JButton();
        invisible.setVisible(false);
        component.add(ipAddress);
        component.add(socketNumberTextField);
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
        JTextField loginField = new JTextField(User.defaultLogin);
        loginField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loginField.setText("");
            }
        });
        JPasswordField passwordField = new JPasswordField(User.defaultPassword);
        passwordField.setEchoChar('*');
        passwordField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                passwordField.setText("");
            }
        });
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputLogin = loginField.getText();
                String inputPassword = new String(passwordField.getPassword());
                User newUser = new User(inputLogin, inputPassword, UserUI.this, user.getServer());
                if (!user.getServer().getStatus()) handleMessage("Подключение не удалось\n");
                else if (newUser.connectToServer()) {
                    user = newUser;
                    chat.setText("");
                    user.restoreChatHistoryFromServer();
                } else JOptionPane.showMessageDialog(loginButton, "Логин или пароль введены не верно.\n");
            }
        });
        component.add(loginField);
        component.add(passwordField);
        component.add(loginButton);
        return component;
    }

    /**
     * Метод для добавления текста в пользовательский чат
     *
     * @param text сообщение
     */
    @Override
    public void handleMessage(String text) {
        chat.append(text);
    }

    /**
     * Метод, определяющий действия после подключения к серверу
     */
    @Override
    public void connectedToServer() {
        northComponent.setVisible(false);
    }

    /**
     * Метод, определяющий действия после отключения от сервера
     */
    @Override
    public void disconnectedFromServer() {
        northComponent.setVisible(true);
    }
}
