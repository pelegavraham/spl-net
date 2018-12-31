package bgu.spl.net.Message;

public abstract class AckMessage implements ServerToClientMessage {

    short opcode;
    short opcodeMessage;

    public AckMessage(short opcodeMessage){

        this.opcodeMessage=opcodeMessage;

    }

    @Override
    public String sendAsFormat(){
        return opcode + "" + opcodeMessage;
    }

    public String sendAsOutput(){         //send to the client
        return "ACK"+opcodeMessage;
    }
}
