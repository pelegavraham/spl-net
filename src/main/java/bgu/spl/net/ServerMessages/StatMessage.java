package bgu.spl.net.ServerMessages;

import java.util.Objects;

/**
 * This class represent a stat message
 */
public class StatMessage implements ClientToServerMessage {


    /** the user name to check */
    private String userName;

    /**
     * Constructor
     * @param userName the user name to check
     */
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
