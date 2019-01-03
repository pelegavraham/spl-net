package bgu.spl.net.ServerMessages;

import java.util.Objects;

/**
 * This class represent a log in message sent by a client to the server
 */
public class LoginMessage implements ClientToServerMessage {

    /** the user name to register with */
    private String userName;

    /** the paswword to register with */
    private String password;

    /**
     * constructor
     * @param userName the user name to log in with
     * @param password the password to login with
     */
    public LoginMessage(String userName, String password)
    {
        Objects.requireNonNull(userName);
        Objects.requireNonNull(password);

        this.userName = userName;
        this.password = password;
    }

    /**
     * Getter to the user name
     * @return the user name
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Getter to the password
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

}
