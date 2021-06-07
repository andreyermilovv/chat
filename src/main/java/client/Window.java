package client;

import chat.TcpConnection;
import chat.TcpListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Window extends JFrame implements ActionListener, TcpListener {

    private final static String IP = "0.0.0.0";

    private final static int PORT = 8189;

    private final static int WIDTH = 600;

    private final static int HEIGHT = 400;

    private final JTextArea log = new JTextArea();
    private final JTextField nickname = new JTextField("username");
    private final JTextField message = new JTextField();

    private TcpConnection tcpConnection;

    public Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        message.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(nickname, BorderLayout.NORTH);
        add(message, BorderLayout.SOUTH);
        setVisible(true);

        try {
            tcpConnection = new TcpConnection(this, IP, PORT);
        } catch (IOException e) {
            printMessage(e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = message.getText();
        if (msg.isBlank()) return;
        message.setText(null);
        tcpConnection.sendString(nickname.getText() + ": " + msg);
    }

    @Override
    public void onConnectionReady(TcpConnection tcpConnection) {
        printMessage("Connection Ready...");
    }

    @Override
    public void onReceiveString(TcpConnection tcpConnection, String value) {
        printMessage(value);
    }

    @Override
    public void onDisconnect(TcpConnection tcpConnection) {
        printMessage("Connection close");
    }

    @Override
    public void onException(TcpConnection tcpConnection, Exception e) {
        printMessage(e.getMessage());
    }

    private synchronized void printMessage(String msg){
        SwingUtilities.invokeLater(() -> log.append(msg + "\n"));
    }
}
