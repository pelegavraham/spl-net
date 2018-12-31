package bgu.spl.net.Message;

public interface ServerToClientMessage {
    short opcode=0;
    public String sendAsFormat();
    public String sendAsOutput();
}
