package bgu.spl.net.api.App_Data;

import bgu.spl.net.ServerMessages.Message;
import bgu.spl.net.api.bidi.Connections;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class represent the server's data base
 * This is a ThreadSafe class, since several clients can manipulate int at the same time
 * this class implements the singleton design pattern, since there could be only one database
 */
public class ServerDataBase
{

    // ------------------------------------------ Singleton ------------------------------------------

    /** The ONLY instance of this class */
    private static final ServerDataBase _instance = new ServerDataBase();

    /**
     * Constructor
     * it is private dou to the singleton design pattern
     */
    private ServerDataBase()
    {
        users = new ConcurrentHashMap<>();
        orderedUsers = new CopyOnWriteArrayList<>();
    }

    /**
     * Grtter to the instance of the database
     * @return the database only instance
     */
    public static ServerDataBase getInstance()
    {
        return _instance;
    }


    // --------------------------------------------------------------------------------------------

    /** this hols the users as the key. the value of map is it's password. */
    private final Map<User, String> users;

    private final List<String> orderedUsers;


    /**
     * register a user to the data base
     * @param user the user name
     * @param password the password of the user
     * @return true if successfully registered, false otherwise - the user name was already taken
     */
    public boolean register(String user, String password)
    {
        checkInput(user); // check input
        checkInput(password);

        if(users.putIfAbsent(new User(user), password) == null)
        {
            // registered successfully
            orderedUsers.add(user);
            return true;
        }

        return false;
    }

    /**
     * check i a user us registered
     * @param user the user name
     * @return true if registered, false otherwise
     */
    public boolean isRegistered(String user)
    {
        checkInput(user); // check input

        return getUser(user) != null;
    }

    /**
     * check if a user can login
     * user can successfully log in if and only if the given user exist in the system,
     * and the given password is it's correct password
     * @param user the user name
     * @param password the password of the user
     * @return true if can log in, false otherwise
     */
    public boolean canLogin(String user, String password)
    {
        checkInput(user); // check input
        checkInput(password);

        User u = getUser(user);

        if(u == null)
            return false; // this user does not exist user

        String passwordFromDB = users.get(u);

        if(passwordFromDB == null)
            return false; // Error?

        return passwordFromDB.equals(password);
    }

    /**
     * Get all the registred users in the system
     * @return a {List<String>} if all users in the system
     */
    public List<String> getRegisterdUseres()
    {
        return orderedUsers;
    }

    /**
     * get the stats of a user
     * @param user the user
     * @return amp with 3 keys coresponding to the number if the stat they are representing : posts, followers, following.
     */
    public Map<String, Integer> getStatOf(String user)
    {
        Objects.requireNonNull(user);

        Map<String, Integer> map = new ConcurrentHashMap<>();

        int posts = 0;
        int followers = 0;
        int following = 0;

        User u = getUser(user);

        if(u != null)
        {
            posts = u.getNumOfPosts();
            followers = u.getNumOfFolowers();
            following = u.getNumOfFolowing();
        }

        map.put("posts", posts);
        map.put("followers", followers);
        map.put("following", following);

        return map;
    }


    /**
     * make the given user follow all the users in the list
     * @param user the user that want to follow the other users
     * @param  toFollow the users he is want to follow
     * @return the list of the new users he followed, users he was already following will be ignored
     */
    public List<String> followALL(String user, List<String> toFollow)
    {
        checkInput(user); // check input
        checkInput(toFollow);

        return getUserNotNull(user).followALL(getUsers(toFollow));
    }

    /**
     * make the given user unfollow all the users in the list
     * @param user the user that want to unfollow the other users
     * @param  toUnFollow the users he is want to unfollow
     * @return the list of the users he unfollowed, users he was  did not  followed will be ignored
     */
    public List<String> unfollowALL(String user, List<String> toUnFollow)
    {
        checkInput(user); // check input
        checkInput(toUnFollow);

        return getUserNotNull(user).unfollowALL(getUsers(toUnFollow));
    }

    /**
     * send a meesage
     * @param sender the name of the sender user
     * @param message the message
     * @param reciver the user to send the message to
     */
    public void sendPublicMessage(String sender, String reciver, String message, Connections<Message> connections)
    {
        // input check
        checkInput(sender);
        checkInput(message);
        checkInput(reciver);

        getUserNotNull(sender).sentPublicMessage(message, getUser(reciver), connections); // send messages
    }

    /**
     * send a private message
     * @param sender the sender user
     * @param reciver the reciver
     * @param message the message
     * @return true id successfully sent, false otherwise
     */
    public boolean sendPrivateMessage(String sender, String reciver, String message, Connections< Message> connections)
    {
        // input check
        checkInput(sender);
        checkInput(reciver);
        checkInput(message);
        Objects.requireNonNull(connections);

        User to = getUser(reciver);

        if(to == null)
            return false; // no such user exists

        getUserNotNull(sender).sendPrivateMessage(message, to, connections);
        return true;
    }

    /**
     * Get all the posts and PM the given user did not received
     * @param user the user
     * @return a list pf pair that it's key is meta-data : is private + sender, and the value is the message
     */
    public Queue<Pair<Pair<Boolean, String>, String>> getUnreadMessagesOf(String user)
    {
        checkInput(user);

        synchronized (getUserNotNull(user))
        {
            return getUserNotNull(user).getUreadMessages();
        }
    }

    /**
     * get the followers if a user
     * @param user the user
     * @return a list of it's followers
     */
    public List<String> getFollowersOf(String user)
    {
        List<String> followers = new LinkedList<>();

        users.keySet().forEach(u ->
        {
            if(u.doesfollow(user)) // he his following the user
                followers.add(u.getUserName());
        });

        return new CopyOnWriteArrayList<>(followers);
    }



    // --------------------------------------------------- private  -----------------------------------------------------------


    /**
     * get the user object fron it's user name
     * @param user the user name
     * @return the user object corresponding to this user, or null uf does not exists
     */
    private User getUser(String user)
    {
        for(User u : users.keySet())
            if(u.getUserName().equals(user))
                return u; // found a mach

        return null;
    }

    /**
     * get the user object from it's user name, and throw exception id dies not exists
     * @param user the user name
     * @return the user object corresponding to this user
     */
    private User getUserNotNull(String user)
    {
        User u = getUser(user);
        Objects.requireNonNull(u, "An unregistered users tried to perform operation ON THE DATABASE");
        return u;
    }

    /**
     * get the user object list object from their user names
     * @param users the users name
     * @return the user object list corresponding to the users
     */
    private List<User> getUsers(List<String> users)
    {
        List<User> followList = new LinkedList<>();
        User temp;

        for(String name : users)
        {
            temp = getUser(name);

            if(temp != null)
                followList.add(temp);
        }

        return followList;
    }

    // ------------------------------------------------ input checks ----------------------------------------------------------


    /**
     * check the input of a a list of strings
     * @param list the list
     */
    private void checkInput(List<String> list)
    {
        Objects.requireNonNull(list, "the list must not be null!");
        list.forEach(Objects::requireNonNull);
    }

    /**
     * check the input of a string
     * @param string the string
     */
    private void checkInput(String string)
    {
        Objects.requireNonNull(string, "this string must not be null!");
    }

}
