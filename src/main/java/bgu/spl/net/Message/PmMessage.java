package bgu.spl.net.Message;

public class PmMessage implements ClientToServerMessage {

    short opcode;
    String userName;
    String content;

    public PmMessage(String userName, String content) {

        opcode=6;
        this.userName = userName;
        this.content = content;

    }

    public String send(){
        return opcode + userName + '0' + content + '0';
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }
}
