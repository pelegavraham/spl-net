package bgu.spl.net.Message;

public class ErrorMessage{

    short opcode ;
    short opcodeMessage ;

    public ErrorMessage(short opcodeMessage){

        opcode=11;
        this.opcodeMessage=opcodeMessage;

    }

    @Override
    public String toString() {
        return opcode + "" + opcodeMessage;
    }
}
