package bgu.spl.net.Message;

public abstract class AckMessage implements Message{

    short opcode;
    short opcodeMessage;

    public AckMessage(short opcodeMessage){

        this.opcodeMessage=opcodeMessage;

    }

    @Override
    public String toString(){
        return opcode + "" + opcodeMessage;
    }
}
