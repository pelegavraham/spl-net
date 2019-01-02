package bgu.spl.net.ServerMessages;

import bgu.spl.net.api.App_Data.ServerDataBase;

/**
 * This class represent a log in message sent by a client to the server
 */
public class LoginMessage extends RegisterMessage implements ClientToServerMessage {

    //NOTE : This class extends RegisterMessage to avoid code duplication only.

    /**
     * constructor
     * @param userName the user name to log in with
     * @param password the password to login with
     */
    public LoginMessage(String userName, String password)
    {
        super(userName, password);
    }

}
