package bgu.spl.net.impl.ConversationProtocol;

import bgu.spl.net.ServerMessages.*;
import bgu.spl.net.impl.App_Data.ServerDataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.conn.LogedInUsers;
import bgu.spl.net.Pair.Pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

/**
 * The protocol
 */
public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message>
{
    /** The connectionId of this client */
    private int connectionId;

    /** the connection Object */
    private Connections<Message> connections;

    /** should the conversation end */
    private boolean shouldTerminate;

    /** an instance to the DB */
    private ServerDataBase DB;

    /** the user name this user is loogied in woth, or null if not logged in */
    private String username;


    /**
     * Constructr
     */
    public BidiMessagingProtocolImpl()
    {
        shouldTerminate = false;

        connectionId = -1;
        connections = null;

        username = null;

        DB = ServerDataBase.getInstance();
    }

    @Override
    public void start(int connectionId, Connections<Message> connections)
    {
        Objects.requireNonNull(connections);

        if(connectionId < 0)
            throw new IllegalArgumentException("connection id must be grater or equal 0!");

        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Message message)
    {
        Objects.requireNonNull(message);
        Objects.requireNonNull(connections); // must initiate this firstly

        if(message instanceof RegisterMessage) {
            processRegister((RegisterMessage) message);
            return;
        }

        if(message instanceof LoginMessage) {
            processLogin((LoginMessage) message);
            return;
        }

        if(message instanceof LogoutMessage) {
            processLogout((LogoutMessage) message);
            return;
        }

        if(message instanceof FollowMessage) {
            processFollow((FollowMessage) message);
            return;
        }

        if(message instanceof PostMessage) {
            processPost((PostMessage) message);
            return;
        }

        if(message instanceof PmMessage) {
            processPM((PmMessage) message);
            return;
        }

        if(message instanceof UserListMessage) {
            processUserList((UserListMessage) message);
            return;
        }

        if(message instanceof StatMessage) {
            processStat((StatMessage) message);
            return;
        }

        throw new IllegalStateException("unknown message sent to the server : " + message.getClass().getName()); // mostly for debugging
    }

    @Override
    public boolean shouldTerminate()
    {
        return shouldTerminate;
    }

    /**
     * process a register message
     * @param message the message object
     */
    private void processRegister(RegisterMessage message)
    {
        Objects.requireNonNull(message);

        if(DB.register(message.getUserName(), message.getPassword()))
            send(new AckMessage((short)1)); // successful
        else
            send(new ErrorMessage((short) 1)); // failed
    }

    /**
     * process a login message
     * @param message the message object
     */
    private void processLogin(LoginMessage message)
    {
        Objects.requireNonNull(message);
        boolean isPrvate;
        String sender;
        String content;

        if(username != null)
        {
            send(new ErrorMessage((short) 2)); // already logged in
            return;
        }

        if(DB.canLogin(message.getUserName(), message.getPassword()))
        {
            username = message.getUserName(); // logged in - save for later

            if(!LogedInUsers.getInstance().logIn(username, connectionId)) // log in
            {
                send(new ErrorMessage((short) 2)); // this user was already logged in!
                username = null; // can't login
                return;
            }

            send(new AckMessage((short) 2)); // successful

            Queue<Pair<Pair<Boolean, String>, String>> messages =  DB.getUnreadMessagesOf(username);

            // sent the user all his unread messages
            for(Pair<Pair<Boolean, String>, String> pair : messages)
            {
                isPrvate = pair.getKey().getKey();
                sender = pair.getKey().getValue();
                content = pair.getValue();

                send(new NotificationMessage(isPrvate, sender, content ));
            }
        }
        else
        {
             send(new ErrorMessage((short) 2)); // failed
        }
    }

    /**
     * process a logout message
     * @param message the message object
     */
    private void processLogout(LogoutMessage message)
    {
        Objects.requireNonNull(message);

        if(username == null)
            send(new ErrorMessage((short)3)); // not logged in
        else
        {
            send(new AckMessage((short) 3)); // successful

            LogedInUsers.getInstance().logout(username); // log out
            connections.disconnect(connectionId); // disconnect

            username = null; // logged out
            shouldTerminate = true; // end of conversation
        }
    }

    /**
     * process a follow message
     * @param message the message object
     */
    private void processFollow(FollowMessage message)
    {
        Objects.requireNonNull(message);

        List<String> changed;

        if(username == null)
            send(new ErrorMessage((short)4)); // not logged in
        else
        {
            if(message.isFollow())
                changed = DB.followALL(username, message.getUsers());
            else // unfollow
                changed = DB.unfollowALL(username, message.getUsers());

            if(changed.isEmpty())
                send(new ErrorMessage((short)4)); // nothing changed
            else
                send(new FollowAckMessage(changed));
        }
    }

    /**
     * process a post message
     * @param message the message object
     */
    private void processPost(PostMessage message)
    {
        Objects.requireNonNull(message);

        List<String> interstedUsers;

        if(username == null)
            send(new ErrorMessage((short)5)); // not logged in
        else
        {
            interstedUsers = DB.getFollowersOf(username);

            for(String user : message.getTaged())
                if(!interstedUsers.contains(user))
                    interstedUsers.add(user);

             DB.sendPublicMessage(username, interstedUsers, message.getContent(), connections);

             send(new AckMessage((short)5)); // according to the specification - an answer must always be sent back
        }

    }

    /**
     * process a PM message
     * @param message the message object
     */
    private void processPM(PmMessage message)
    {
        Objects.requireNonNull(message);

        String reciver = message.getReciver();

        if(username == null)
            send(new ErrorMessage((short)6)); // not logged in
        else
        {
            if(DB.sendPrivateMessage(username, reciver, message.getContent(), connections))
                send(new AckMessage((short)6)); // according to the specification - an answer must always be sent back
               else
                   send(new ErrorMessage((short)6)); // reciver is not registered
        }
    }

    /**
     * process an user list message
     * @param message the message object
     */
    private void processUserList(UserListMessage message)
    {
        Objects.requireNonNull(message);

        if(username == null)
            send(new ErrorMessage((short)7)); // not logged in
        else
            send(new UserListAckMessage(DB.getRegisterdUseres()));
    }

    /**
     * process a stat message
     * @param message the message object
     */
    private void processStat(StatMessage message)
    {
        Objects.requireNonNull(message);

        Map<String, Integer> stats;
        int NumPosts;
        int NumFollowers;
        int NumFollowing;
        String user = message.getUserName();

        if(username == null)
            send(new ErrorMessage((short)8)); // not logged in
        else
        {
            if(!DB.isRegistered(user))
                send(new ErrorMessage((short)8)); // requested user does not exists
            else
            {
                stats = DB.getStatOf(user);

                NumPosts = stats.get("posts");
                NumFollowers = stats.get("followers");
                NumFollowing = stats.get("following");

                send(new StatAckMessage(NumPosts, NumFollowers, NumFollowing));
            }
        }
    }

    // ------------------------------------------------------------------

    /**
     * send a response to the client
     * @param msg the response
     */
    private void send(ServerToClientMessage msg)
    {
        Objects.requireNonNull(msg);

        connections.send(connectionId, msg);
    }
}
