package bgu.spl.net.Message;

public class ErrorMessage implements ServerToClientMessage {

    short opcode ;
    short opcodeMessage ;

    public ErrorMessage(short opcodeMessage){

        opcode=11;
        this.opcodeMessage=opcodeMessage;

    }

    public String sendAsFormat() {
        return opcode + "" + opcodeMessage;
    }

    public String sendAsOutput() {
        return "Error"+opcodeMessage;
    }
}
