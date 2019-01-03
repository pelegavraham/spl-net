package bgu.spl.net.ServerMessages;

/**
 * This class represent a stat message respond
 */
public class StatAckMessage extends AckMessage
{

    /** number of posts */
    private int numOfPosts;

    /** number of Followers */
    private int numOFollowers;

    /** number of Following */
    private int numOFollowing;


    /**
     * Consrtuctor
     * @param numOfPosts number of posts
     * @param numOFollowers number of Followers
     * @param numOFollowing number of Following
     */
    public StatAckMessage(int numOfPosts, int numOFollowers, int numOFollowing) {
        super((short) 8);

        this.numOfPosts = numOfPosts;
        this.numOFollowers = numOFollowers;
        this.numOFollowing = numOFollowing;
    }

    /**
     * Grtter to the number of posts
     * @return the number of posts
     */
    public int getNumOfPosts() {
        return numOfPosts;
    }

    /**
     * Grtter to the number of Followers
     * @return the number of Followers
     */
    public int getNumOFollowers() {
        return numOFollowers;
    }

    /**
     * Grtter to the number of Following
     * @return the number of Following
     */
    public int getNumOFollowing() {
        return numOFollowing;
    }
}
