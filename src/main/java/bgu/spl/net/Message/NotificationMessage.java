package bgu.spl.net.Message;

public class NotificationMessage implements ServerToClientMessage {

    short opcode;
    char notificationType;
    String postingUser;
    String content;

    public NotificationMessage(char notificationType, String postingUser, String content) {

        opcode=9;
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;

    }

    public char getNotificationType() {
        return notificationType;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String sendAsFormat() {
        return opcode + notificationType + postingUser + '0' + content + '0';
    }

    @Override
    public String sendAsOutput() {
        return "NOTIFICATION "+notificationType+ postingUser+ content;
    }
}
