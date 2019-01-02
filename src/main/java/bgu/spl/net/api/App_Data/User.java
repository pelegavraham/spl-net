package bgu.spl.net.api.App_Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represent a user in the system
 * This class is thread-safe.
 */
public final class User
{
    /** The user name */
    private final String userName;


    /** the amput of post this user sent */
    private final AtomicInteger numOfPosts;

    /** the amount users that follow this user */
    private final AtomicInteger numOfFollowers;

    /** the number of the last message this user received */
    //private final AtomicInteger firstNotseenPost;

    /** the number of the last PM this user received */
    //private final AtomicInteger firstNotseenPM;



    /**
     * the users this user is following.
     *  the value of map is ignored.
     *  it's a map and not list, just because map have the method putIfAbsent, and a list doesn't
     */
    private final Map<User, String> follows;

    /** the public messages that might interest this user, and their sender */
    private final Map<String, User> posts;

    /** the private messages that sent to this user, and their sender */
    private final Map<String, User> mailBox;




    /**
     * Constructor
     * @param userName the user name
     */
    public User(String userName)
    {
        Objects.requireNonNull(userName);

        this.userName = userName;

        follows = new ConcurrentHashMap<>();
        posts= new ConcurrentHashMap<>();
        mailBox = new ConcurrentHashMap<>();

        numOfPosts = new AtomicInteger(0);
        numOfFollowers = new AtomicInteger(0);
        //firstNotseenPost = new AtomicInteger(0);
        //firstNotseenPM = new AtomicInteger(0);

    }

    /**
     * check if the user follows the other user
     * @param follwing the user to check of the this is following him
     * @return true if this follows follwing, false otherwise
     */
    public boolean doesfollow( User follwing)
    {
        checkInput(follwing);

        return follows.containsKey(follwing);
    }

    /**
     * follow all the users in the list
     * @param  toFollow the users he is want to follow
     * @return the list of the new users he followed, users he was already following will be ignored
     */
    public List<String> followALL(List<User> toFollow)
    {
        checkInput(toFollow);

        List<String> newUsers = new LinkedList<>();

        for(User user2follow : toFollow)
        {
            if (follows.putIfAbsent(user2follow, "IGNORE_THIS") == null)
            {
                newUsers.add(user2follow.getUserName()); // this was a new user tht that user followed
                user2follow.numOfFollowers.incrementAndGet(); // numOfFollowers++
            }
        }

        return new CopyOnWriteArrayList<>(newUsers);
    }

    /**
     *  unfollow all the users in the list
     * @param  toUnFollow the users he is want to unfollow
     * @return the list of the users he unfollowed, users he was  did not followed will be ignored
     */
    public List<String> unfollowALL( List<User> toUnFollow)
    {
        checkInput(toUnFollow);

        List<String> unfollowedUsers = new LinkedList<>();

        for(User user2Unfollow : toUnFollow)
        {
            if (follows.remove(user2Unfollow) != null)
            {
                unfollowedUsers.add(user2Unfollow.getUserName()); // this was a user that the user unfollowed
                user2Unfollow.numOfFollowers.decrementAndGet(); // numOfFollowers--.
            }
        }

        return new CopyOnWriteArrayList<>(unfollowedUsers);
    }

    /**
     * send a meesage
     * @param message the message
     * @param  sentTo the users to sent the message to
     */
    public void sentPublicMessage(String message, List<User> sentTo)
    {
        // input check
        checkInput(message);
        checkInput(sentTo);

        numOfPosts.incrementAndGet(); //numOfPosts++
        sentTo.forEach((user -> user.recivePost(message, this)));
    }

    /**
     * send a private message
     * @param reciver the reciver
     * @param message the message
     */
    public void sendPrivateMessage(String message, User reciver)
    {
        // input check
        checkInput(reciver);
        checkInput(message);

        reciver.recivePrivateMessage(message, this);
    }


    // ------------------------------------------------- Getters ---------------------------------------------------------


    /**
     * Getter to the userName
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter to the number of posts sent by this user
     * @return the number of posts sent by this user
     */
    public int getNumOfPosts() {
        return numOfPosts.get();
    }

    /**
     * Getter to the number of followers this user have
     * @return the number of followers this user have
     */
    public int getNumOfFolowers() {
        return numOfFollowers.get();
    }

    /**
     * Getter to the number of following this user is follow
     * @return the number of following this user is follow
     */
    public int getNumOfFolowing() {
        return follows.keySet().size();
    }


    // --------------------------------------------------- equals ---------------------------------------------------------

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;
        return getUserName().equals(user.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }


    // -------------------------------------------------- private -----------------------------------------------------------

    /**
     * recive a post
     * @param message the message
     * @param sender the sender of the message
     */
    private void recivePost(String message, User sender)
    {
        checkInput(message);
        checkInput(sender);

        posts.put(message, sender);
    }

    /**
     * recive a private message
     * @param message the message
     * @param sender the sender of the message
     */
    private void recivePrivateMessage(String message, User sender)
    {
        checkInput(message);
        checkInput(sender);

        mailBox.put(message, sender);
    }


    // ------------------------------------------------ input checks ----------------------------------------------------------


    /**
     * check the input of a a list of strings
     * @param list the list
     */
    private void checkInput(List<User> list)
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

    /**
     * check the input of a user
     * @param user the user
     */
    private void checkInput(User user)
    {
        Objects.requireNonNull(user, "this user must not be null!");
    }
}
