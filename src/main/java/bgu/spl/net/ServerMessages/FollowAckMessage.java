package bgu.spl.net.ServerMessages;

import java.util.List;

/**
 * This message represent a response to a follow command
 */
public class FollowAckMessage extends AckMessage
{
    /** the user list */
    private List<String> users;

    /**
     * Consrtuctor
     * @param users the user list
     */
    public FollowAckMessage(List<String> users){
        super((short) 4);
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
