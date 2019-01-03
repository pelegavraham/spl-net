package bgu.spl.net.impl.OLD;

public interface MessagingProtocol<T> {

    /**
     * execute the message
     * @param message the message
     */
    T process(T message);

    /**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
}
