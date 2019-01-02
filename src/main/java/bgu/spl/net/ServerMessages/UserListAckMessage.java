package bgu.spl.net.ServerMessages;

import java.util.List;

/**
 * This message represent a response to user list command
 */
public class UserListAckMessage extends  AckMessage
{
    /** the user list */
    private List<String> users;

    /**
     * Consrtuctor
     * @param users the user list
     */
    public UserListAckMessage(List<String> users){
       super((short) 7);
       this.users = users;
    }

    /**
     * Getter tot the user list
     * @return user list
     */
    public List<String> getUsers() {
        return users;
    }
}
