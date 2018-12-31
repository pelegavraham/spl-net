package bgu.spl.net.Message;

public class PostMessage implements ClientToServerMessage {

    short opcode;
    String content;

    public PostMessage(String content){

        opcode=5;
        this.content=content;

    }

    public String send(){
        return opcode + content;
    }

    public String getContent() {
        return content;
    }

}
