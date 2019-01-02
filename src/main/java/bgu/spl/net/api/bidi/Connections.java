package bgu.spl.net.api.bidi;

public interface Connections<T> {

    /**
     * sends a message T to client represented by the given connId
     * @param connectionId the connId of the client
     * @param msg the message to sent
     * @return true if succeeded, false otherwise
     */
    boolean send(int connectionId, T msg);

    /**
     * sends a message T to all active clients
     * This includes clients that has not yet completed log-in by the BGS protocol.
     * @param msg the message
     */
    void broadcast(T msg);

    /**
     * removes active client connId from map
     * @param connectionId the connId
     */
    void disconnect(int connectionId);
}