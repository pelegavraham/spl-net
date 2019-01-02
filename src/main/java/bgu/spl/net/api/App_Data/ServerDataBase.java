package bgu.spl.net.api.App_Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        User u = new User(user);

         return users.putIfAbsent(u, password) == null;
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
        List<String> registered = new LinkedList<>();
        users.forEach( (user, pass) ->  registered.add(user.getUserName())); // get all users

        return new CopyOnWriteArrayList<>(registered);
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

        User u = getUser(user);
        Objects.requireNonNull(u, "An unregistered userd tried to perform operation IN THE DATABASE");

        return u.followALL(getUsers(toFollow));
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

        User u = getUser(user);
        Objects.requireNonNull(u, "An unregistered userd tried to perform operation IN THE DATABASE");

        return u.unfollowALL(getUsers(toUnFollow));
    }

    /**
     * send a meesage
     * @param sender the name of the sender user
     * @param message the message
     * @param  taged the users that were taged in this message
     */
    public void sentPublicMessage(String sender, String message, List<String> taged)
    {
        // input check
        checkInput(sender);
        checkInput(message);
        checkInput(taged);

        User u = getUserNotNull(sender);
        List<User> to = new LinkedList<>(getFollowersOf(u));

        getUsers(taged).forEach(user ->
        {
            if(!to.contains(user)) // only one thread have access to this, so this is thread safe.
                to.add(user);
        });


        u.sentPublicMessage(message, new CopyOnWriteArrayList<>(to)); // send messages
    }

    /**
     * send a private message
     * @param sender the sender user
     * @param reciver the reciver
     * @param message the message
     * @return true id successfully sent, false otherwise
     */
    public boolean sendPrivateMessage(String sender, String reciver, String message)
    {
        // input check
        checkInput(sender);
        checkInput(reciver);
        checkInput(message);

        User to = getUser(reciver);

        if(to == null)
            return false; // no such user exists

        getUserNotNull(sender).sendPrivateMessage(message, to);
        return true;
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

    /**
     * get the followers if a user
     * @param user the user
     * @return a list of it's followers
     */
    private List<User> getFollowersOf(User user)
    {
        List<User> followers = new LinkedList<>();

        users.keySet().forEach(u ->
        {
            if(u.doesfollow(user)) // he his following the user
                followers.add(u);
        });

        return new CopyOnWriteArrayList<>(followers);
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
