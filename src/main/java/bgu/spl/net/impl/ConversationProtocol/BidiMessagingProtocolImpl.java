package bgu.spl.net.impl.ConversationProtocol;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

/**
 * The protocol
 * @param <T> //todo : right here
 */
public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T>
{
    @Override
    public void start(int connectionId, Connections<T> connections) {

    }

    @Override
    public void process(T message) {

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
