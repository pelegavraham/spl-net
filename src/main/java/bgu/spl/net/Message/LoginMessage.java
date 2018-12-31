package bgu.spl.net.Message;

public class LoginMessage implements Message{

    private short opcode;
    private String userName;
    private String password;

    public LoginMessage(String userName, String password){

        opcode=2;
        this.userName=userName;
        this.password=password;

    }

    public String send(){
        return opcode + userName + '0' + password + '0';
    }

    public short getOpcode() {
        return opcode;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
