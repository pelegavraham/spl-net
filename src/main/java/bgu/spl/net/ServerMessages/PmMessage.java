package bgu.spl.net.ServerMessages;

import java.util.Objects;

/**
 * This class represent a private post message sent by a client to the server
 */
public class PmMessage implements ClientToServerMessage {

    /** the sender of the message */
    private String reciver;

    /** The content if the message */
    private String content;

    public PmMessage(String reciver, String content)
    {
        Objects.requireNonNull(content);
        Objects.requireNonNull(reciver);

        this.reciver = reciver;
        this.content = content;

    }

    /**
     * Getter to the Reciver
     * @return the Reciver
     */
    public String getReciver() {
        return reciver;
    }

    /**
     * Getter to the content
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
