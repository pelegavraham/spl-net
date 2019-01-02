package bgu.spl.net.impl.conn;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;

import java.net.Socket;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    /** The map for the connection handlers */
    private Map<Integer, ConnectionHandler<T>> connectionHandlers;

    /** Map the online users (performed log in) to their connID */
    //private Map<String, Integer> onlineUsers;

    /**
     * Constructor
     */
    public ConnectionsImpl()
    {
        this.connectionHandlers = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean send(int connectionId, T msg)
    {
        ConnectionHandler<T> handler = connectionHandlers.get(connectionId);

        if(handler == null)
            return false; // could mot sent - was not online

        handler.send(msg);
        return true; // sent
    }

    @Override
    public synchronized void broadcast(T msg)
    {
        connectionHandlers.forEach(((connectionId, connectionHandler) -> connectionHandler.send(msg))); // send to all
    }

    @Override
    public synchronized void disconnect(int connectionId)
    {
        connectionHandlers.remove(connectionId);
    }
}
