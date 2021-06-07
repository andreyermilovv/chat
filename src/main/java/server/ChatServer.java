package server;

import chat.TcpConnection;
import chat.TcpListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer implements TcpListener {

    private final List<TcpConnection> connections = new ArrayList<>();

    public ChatServer() {
        System.out.println("Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(8189)){
            while (true){
                try {
                    new TcpConnection(serverSocket.accept(), this);
                }
                catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TcpConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAll("Client connected" + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TcpConnection tcpConnection, String value) {
        sendToAll(value);
    }

    @Override
    public synchronized void onDisconnect(TcpConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAll("Client disconnected" + tcpConnection);
    }

    @Override
    public synchronized void onException(TcpConnection tcpConnection, Exception e) {
        System.err.print(e.getMessage());
    }

    private void sendToAll(String message){
        for (TcpConnection connection : connections){
            connection.sendString(message);
        }
    }
}
