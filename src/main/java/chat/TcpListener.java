package chat;

public interface TcpListener {

    void onConnectionReady(TcpConnection tcpConnection);

    void onReceiveString(TcpConnection tcpConnection, String value);

    void onDisconnect(TcpConnection tcpConnection);

    void onException(TcpConnection tcpConnection, Exception e);
}
