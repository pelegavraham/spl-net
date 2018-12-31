package bgu.spl.net.Message;

public class UserListMessage implements Message{

    short opcode;

    public UserListMessage(){
        opcode=7;
    }

    public String send(){
        return opcode + "";
    }
}
