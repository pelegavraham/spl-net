package bgu.spl.net.ServerMessages;

public class ErrorMessage implements ServerToClientMessage {

    /** The message opcode the ERROR was sent for */
    private short opcodeMessage ;

    /**
     * Constructor
     * @param opcodeMessage The message opcode the ERROR was sent for
     */
    public ErrorMessage(short opcodeMessage){

        this.opcodeMessage=opcodeMessage;

    }

    /**
     * Getter to The message opcode the ERROR was sent for
     * @return The message opcode the ERROR was sent for
     */
    public short getOpcodeMessage() {
        return opcodeMessage;
    }
}
