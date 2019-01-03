package bgu.spl.net.ServerMessages;

import java.util.List;
import java.util.Objects;

/**
 * This class represent a messagesent by a client to the server in order to follow some other users
 */
public class FollowMessage implements ClientToServerMessage {


    /** true if this is a follow request, false if this is unfollow request */
    private boolean follow;

    /** The list of users to follow or unfollow */
    private List<String> users;


    /**
     * constructor
     * @param follow true if this is a follow request, false if this is unfollow request
     * @param users The list of users to follow or unfollow
     */
    public FollowMessage(boolean follow,  List<String> users) {

        //input verification
        Objects.requireNonNull(users);
        users.forEach(Objects::requireNonNull);

        this.follow = follow;
        this.users = users;
    }

    /**
     * Getter to the follow or unfollow
     * @return true if this is a follow request, false if this is unfollow request
     */
    public boolean isFollow() {
        return follow;
    }

    /**
     * Getter to the user list
     * @return the user list
     */
    public List<String> getUsers() {
        return users;
    }
}
