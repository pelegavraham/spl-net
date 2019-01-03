package bgu.spl.net.impl.conn;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;

import java.net.Socket;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsImpl<T> implements Connections<T> {

    /**
     * Constructor
     * it is private dou to the singleton design pattern
     */
    public ConnectionsImpl()
    {
        this.connectionHandlers = new ConcurrentHashMap<>();
        this.nextFree = new AtomicInteger(0);
    }

    /** The map for the connection handlers */
    private volatile Map<Integer, ConnectionHandler<T>> connectionHandlers;

    /** The next free connectionID */
    private volatile AtomicInteger nextFree;


    @Override
    public synchronized boolean send(int connectionId, T msg)
    {
        return send( connectionHandlers.get(connectionId), msg);
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

    /**
     * connect a user (not necery a user that loged in)
     * @param handler the hndler of the user
     * @return the conId of this user
     */
    public synchronized int connect(ConnectionHandler<T> handler)
    {
        int comId = nextFree.getAndIncrement();
        connectionHandlers.put(comId, handler);

        return comId;
    }

    /**
     * sent a message to a conection handler
     * @param handler the connection handler
     * @param msg the message
     * @return true if sucssfully sent (was online) false otherwise
     */
    private synchronized boolean send( ConnectionHandler<T> handler, T msg)
    {
        if(handler == null)
            return false; // could mot sent - was not online

        handler.send(msg);
        return true; // sent
    }
}
