package sever;

import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private boolean isConnected;

    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JTextField tfLogin;
    private final JPasswordField tfPassword = new JPasswordField("qwerty");
    private final JButton btnLogin = new JButton("Подключиться");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Отправить ->");

    ClientGUI(ServerWindow serverWindow, String clientName) {
        tfLogin = new JTextField(clientName);
        isConnected = false;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Клиент: " + clientName);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        add(panelTop, BorderLayout.NORTH);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);

        btnLogin.addActionListener(e -> {
            if (isConnected) {
                log.append("Подключение уже установлено\n");
                return;
            }

            if (serverWindow.isServerRunning()) {
                log.append("Подключение к чату\n");
                serverWindow.logMessage("Подключение к чату клиента " + clientName);
                serverWindow.addClient(this);
                isConnected = true;
            } else {
                log.append("Подключение не установлено\n");
            }
        });

        btnSend.addActionListener(e -> {
            if (isConnected && !serverWindow.isServerStopped()) {
                String message = tfLogin.getText() + ": " + tfMessage.getText();
                log.append(message + "\n");
                serverWindow.broadcastMessage(message, this);
                tfMessage.setText("");
            } else if (!isConnected) {
                log.append("Подключение отсутствует\n");
            } else {
                log.append("Сервер остановлен. Невозможно отправить сообщение\n");
            }
        });



        setVisible(true);
    }

    public void receiveMessage(String message) {
        log.append(message + "\n");
    }

    public void disconnect() {
        isConnected = false;
        log.append("Вы отключены от сервера\n");
    }

}
