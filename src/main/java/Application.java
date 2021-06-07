import client.Window;
import server.ChatServer;

import javax.swing.*;

public class Application {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(Window::new);
        new ChatServer();
    }
}