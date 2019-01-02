package bgu.spl.net.ServerMessages;

import bgu.spl.net.api.App_Data.ServerDataBase;

/**
 * This class represent a register command in the server
 */
public class RegisterMessage implements ClientToServerMessage {

    /** the user name to register with */
    private String userName;

    /** the paswword to register with */
    private String password;

    /**
     * Constructor
     * @param userName the user name to register with
     * @param password the password to register with
     */
    public RegisterMessage(String userName, String password)
    {
        this.userName=userName;
        this.password=password;
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
