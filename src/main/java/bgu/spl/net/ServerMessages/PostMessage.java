package bgu.spl.net.ServerMessages;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represent a post message sent by a client to the server
 */
public class PostMessage implements ClientToServerMessage {

    //NOTE : This class extends PmMessage to avoid code duplication

    /** a list of users that were tagged in thss message */
    private List<String> tagged;

    /** The content if the message */
    private String content;

    /**
     * Constructor
     * @param content the content of the message
     */
    public PostMessage( String content)
    {
        tagged = new LinkedList<>();
        this.content = content;

        String[] words = content.split(" "); // get al the word in this message
        String name;

        for(String word : words)
        {
            if (word.startsWith("@")) // this is a tagged user
            {
                name = word.substring(1);

                if (!tagged.contains(name))
                    tagged.add(name); // remove the "@" and save the user
            }
        }
    }


    /**
     * Getter to the tagged users int this message
     * @return the tagged users int this message
     */
    public List<String> getTaged() {
        return tagged;
    }

    /**
     * Getter to the content
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
