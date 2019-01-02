package bgu.spl.net.ServerMessages;

import java.util.Objects;

public class StatMessage implements ClientToServerMessage {


    private String userName;

    public StatMessage(String userName){

        Objects.requireNonNull(userName);

        this.userName=userName;

    }

    /**
     * Getter to the user
     * @return the user
     */
    public String getUserName() {
        return userName;
    }
}
