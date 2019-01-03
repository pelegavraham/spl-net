package bgu.spl.net.api.App_Data;

import bgu.spl.net.ServerMessages.Message;
import bgu.spl.net.ServerMessages.NotificationMessage;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.conn.LogedInUsers;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
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




    /**
     * the users this user is following.
     *  the value of map is ignored.
     *  it's a map and not list, just because map have the method putIfAbsent, and a list doesn't
     */
    private final Map<User, String> follows;

    /** the public messages that might interest this user, and their sender */
    private final ConcurrentLinkedQueue<Pair<User, String>> posts;

    /** the private messages that sent to this user, and their sender */
    private final ConcurrentLinkedQueue<Pair<User, String>> mailBox;




    /**
     * Constructor
     * @param userName the user name
     */
    User(String userName)
    {
        Objects.requireNonNull(userName);

        this.userName = userName;

        follows = new ConcurrentHashMap<>();
        posts= new ConcurrentLinkedQueue<>();
        mailBox = new ConcurrentLinkedQueue<>();

        numOfPosts = new AtomicInteger(0);
        numOfFollowers = new AtomicInteger(0);
    }

    /**
     * check if the user follows the other user
     * @param follwing the user to check of the this is following him
     * @return true if this follows follwing, false otherwise
     */
    boolean doesfollow( User follwing)
    {
        checkInput(follwing);

        return follows.containsKey(follwing);
    }

    /**
     * check if the user follows the other user
     * @param follwing the user to check of the this is following him
     * @return true if this follows follwing, false otherwise
     */
    boolean doesfollow(String follwing)
    {
        checkInput(follwing);

        for(User u : follows.keySet())
            if(u.getUserName().equals(follwing))
                return true;

        return false;
    }

    /**
     * follow all the users in the list
     * @param  toFollow the users he is want to follow
     * @return the list of the new users he followed, users he was already following will be ignored
     */
    List<String> followALL(List<User> toFollow)
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
    List<String> unfollowALL( List<User> toUnFollow)
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
     * @param  sentTo the user to sent the message to
     */
    void sentPublicMessage(String message, User sentTo, Connections< ? super Message> connections)
    {
        // input check
        checkInput(message);
        checkInput(sentTo);

        numOfPosts.incrementAndGet(); //numOfPosts++
        sentTo.recivePost(message, this, connections);
    }

    /**
     * send a private message
     * @param reciver the reciver
     * @param message the message
     */
    void sendPrivateMessage(String message, User reciver, Connections< ? super Message> connections)
    {
        // input check
        checkInput(reciver);
        checkInput(message);

        reciver.recivePrivateMessage(message, this, connections);
    }


    // ------------------------------------------------- Getters ---------------------------------------------------------


    /**
     * Getter to the userName
     * @return the userName
     */
    String getUserName() {
        return userName;
    }

    /**
     * Getter to the number of posts sent by this user
     * @return the number of posts sent by this user
     */
    int getNumOfPosts() {
        return numOfPosts.get();
    }

    /**
     * Getter to the number of followers this user have
     * @return the number of followers this user have
     */
    int getNumOfFolowers() {
        return numOfFollowers.get();
    }

    /**
     * Getter to the number of following this user is follow
     * @return the number of following this user is follow
     */
    int getNumOfFolowing() {
        return follows.keySet().size();
    }

    /**
     * receive all unread posts
     * @return a mpa of the un read posts : key - the message, value - the sender
     */
    ConcurrentLinkedQueue<Pair<String, String>> getUreadPosts()
    {
        ConcurrentLinkedQueue<Pair<String, String>> messages = new ConcurrentLinkedQueue<>();

        synchronized (posts)
        {
            for(Pair<User, String> pair : posts)
                messages.add(new Pair<>( pair.getKey().getUserName(), pair.getValue()));

            posts.clear(); // those messages received by the user
            return messages;
        }
    }

    /**
     * receive all unread PM
     * @return a mpa of the un read posts : key - the message, value - the sender
     */
    ConcurrentLinkedQueue<Pair<String, String>> getUreadPM()
    {
        ConcurrentLinkedQueue<Pair<String, String>> messages = new ConcurrentLinkedQueue<>();

        synchronized (mailBox)
        {
            for(Pair<User, String> pair : mailBox)
                messages.add(new Pair<>( pair.getKey().getUserName(), pair.getValue()));

            mailBox.clear(); // those messages received by the user
            return messages;
        }
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
    private synchronized void recivePost(String message, User sender, Connections< ? super Message> connections)
    {
        checkInput(message);
        checkInput(sender);

        synchronized (posts)
        {
            int conId = LogedInUsers.getInstance().getConIdOf(getUserName());

            if(!connections.send(conId, new NotificationMessage(false, sender.getUserName(), message)))
                posts.add(new Pair<>(sender, message));
        }
    }

    /**
     * recive a private message
     * @param message the message
     * @param sender the sender of the message
     */
    private synchronized void recivePrivateMessage(String message, User sender, Connections< ? super Message> connections)
    {
        checkInput(message);
        checkInput(sender);
        Objects.requireNonNull(connections);

        synchronized (mailBox)
        {
            int conId = LogedInUsers.getInstance().getConIdOf(getUserName());

            if(!connections.send(conId, new NotificationMessage(true, sender.getUserName(), message)))
                mailBox.add(new Pair<>(sender, message));
        }
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
