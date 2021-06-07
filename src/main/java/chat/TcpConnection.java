package chat;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

public class TcpConnection {

    private final Socket socket;

    private final Thread thread;

    private final BufferedReader in;

    private final BufferedWriter out;

    private final TcpListener tcpListener;

    public TcpConnection(TcpListener tcpListener, String ip, int port) throws IOException {
        this(new Socket(ip, port), tcpListener);
    }

    public TcpConnection(Socket socket, TcpListener tcpListener) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.tcpListener = tcpListener;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                tcpListener.onConnectionReady(TcpConnection.this);
                try {
                    while (!thread.isInterrupted()) {
                        tcpListener.onReceiveString(TcpConnection.this, in.readLine());
                    }
                } catch (IOException e) {
                    tcpListener.onException(TcpConnection.this, e);
                }
                finally {
                    disconnect();
                }

            }
        });
        thread.start();
    }

    @SneakyThrows
    public synchronized void sendString(String value) {
        out.write(value + "\r\n");
        out.flush();
    }

    @SneakyThrows
    public synchronized void disconnect() {
        thread.interrupt();
        socket.close();
    }

    @Override
    public String toString() {
        return "TcpConnection{" +
                "socket=" + socket.getInetAddress() + ": " + socket.getPort();
    }
}