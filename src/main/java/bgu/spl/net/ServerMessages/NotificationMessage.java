package bgu.spl.net.ServerMessages;

/**
 * This class represent a message sent by the server to a client to inform him abot a message he have
 */
public class NotificationMessage implements ServerToClientMessage {

    /** true if it is a private message, false otherwise */
    private boolean isPrivate;

    /** the sender */
    private String postingUser;

    /** the content of the message */
    private String content;


    /**
     * Constructor
     * @param isPrivate true if it is a private message, false otherwise
     * @param postingUser the sender
     * @param content the content of the message
     */
    public NotificationMessage(boolean isPrivate, String postingUser, String content) {

        this.isPrivate = isPrivate;
        this.postingUser = postingUser;
        this.content = content;

    }

    /**
     * check if it is a private message
     * @return true if it is a private message, false otherwise
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Getter to the sender
     * @return the sender
     */
    public String getPostingUser() {
        return postingUser;
    }

    /**
     * Getter to the message
     * @return the message
     */
    public String getContent() {
        return content;
    }
}
