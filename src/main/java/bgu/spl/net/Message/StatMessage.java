package bgu.spl.net.Message;

public class StatMessage implements Message{

    short opcode;
    String userName;

    public StatMessage(String userName){

        opcode=8;
        this.userName=userName;

    }

    public String send(){
        return opcode + userName + '0';
    }
}
