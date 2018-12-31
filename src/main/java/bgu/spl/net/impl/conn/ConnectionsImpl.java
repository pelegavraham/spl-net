package bgu.spl.net.impl.conn;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;

import java.net.Socket;
import java.sql.Connection;
import java.util.List;

public class ConnectionsImpl<T> implements Connections<T> {

    List<ConnectionHandler<T>> connectionHandlers;

    Socket socket;

    @Override
    public boolean send(int connectionId, T msg) {
        return false;
    }

    @Override
    public void broadcast(T msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
