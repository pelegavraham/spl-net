package bgu.spl.net.impl.echo;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

import java.time.LocalDateTime;

public class EchoProtocol implements BidiMessagingProtocol<String> {

    private boolean shouldTerminate = false;
    private Connections<String> connections;
    private int connectionId;

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void process(String msg) {
        shouldTerminate = "bye".equals(msg);
        System.out.println("[" + LocalDateTime.now() + "]: " + msg);
        String response = createEcho(msg);

        connections.send(connectionId, response);

        if(shouldTerminate)
            connections.disconnect(connectionId);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    private String createEcho(String message) {
        String echoPart = message.substring(Math.max(message.length() - 2, 0));
        return message + " .. " + echoPart + " .. " + echoPart + " ..";
    }
}
