package bgu.spl.net.Message;

public class LogoutMessage implements ClientToServerMessage {

    private short opcode;

    public LogoutMessage(){

        opcode=3;

    }

    public String send(){
        return opcode+"" ;
    }
}
