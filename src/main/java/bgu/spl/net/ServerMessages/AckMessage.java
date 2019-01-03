package bgu.spl.net.ServerMessages;

/**
 * This class represent a message to server is sending to theclient to inform him ( acknowledging him ) about the action he did
 */
public class AckMessage implements ServerToClientMessage {

    /** The message opcode the ACK was sent for */
    private short opcodeMessage;

    /**
     * Consrtuctor
     * @param opcodeMessage The message opcode the ACK was sent for
     */
    public AckMessage(short opcodeMessage){

        this.opcodeMessage=opcodeMessage;

    }

    /**
     * Getter to the message code
     * @return  The message opcode the ACK was sent for
     */
    public short getOpcodeMessage() {
        return opcodeMessage;
    }
}
