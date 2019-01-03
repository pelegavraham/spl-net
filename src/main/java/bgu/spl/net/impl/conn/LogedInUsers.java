package bgu.spl.net.impl.conn;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * This class hold foreahk LOGININ user it's conID
 * This class is Thread-safe
 * This class is singleton
 */
public class LogedInUsers
{
    // ------------------------------------------ Singleton -----------------------------------------

    /** The ONLY instance of this class */
    private static final LogedInUsers _instance = new LogedInUsers();

    /**
     * Constructor
     * it is private dou to the singleton design pattern
     */
    private LogedInUsers()
    {
        users = new ConcurrentHashMap<>();
    }

    /**
     * Grtter to the instance of the database
     * @return the database only instance
     */
    public static LogedInUsers getInstance()
    {
        return _instance;
    }


    // --------------------------------------------------------------------------------------------

    /** map a user name to it's conID */
    private Map<String, Integer> users;



    /**
     * log in a user to the server
     * @param user the user name
     * @param conId it's conID
     * @return true id sucseful, false other wise
     */
    public boolean logIn(String user, int conId)
    {
        Objects.requireNonNull(user);

        if(conId < 0)
            throw new IllegalArgumentException("Connection Id must ber gratewr then 0!");

        return users.putIfAbsent(user, conId) == null;
    }

    /**
     * log out a user to the server
     * @param user the user name
     * @return true id sucseful, false other wise
     */
    public boolean logout(String user)
    {
        Objects.requireNonNull(user);

        return  users.remove(user) != null;
    }

    /**
     * get the conection id of a user, or -1 if not logged in
     * @param user the user name
     * @return it's con id or -1 if not logged in
     */
    public int getConIdOf(String user)
    {
        Objects.requireNonNull(user);

        Integer con = users.get(user);

        if(con == null)
            return -1; // not logged in

        return con;
    }

    /**
     * check if a user is loged in
     * @param user the user name
     * @return true if loged in, false otherwise
     */
    public boolean isLoggedIn(String user)
    {
        return getConIdOf(user) != -1;
    }
}
