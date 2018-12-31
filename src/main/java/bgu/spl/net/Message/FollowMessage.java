package bgu.spl.net.Message;

import java.util.List;

public class FollowMessage implements Message{

    short opcode;
    short follow;
    short numOfUsers;
    String usrMameList;

    public FollowMessage(short follow, short numOfUsers, String usrMameList){

        opcode=4;
        this.follow=follow;
        this.numOfUsers=numOfUsers;
        this.usrMameList=usrMameList;

    }

    public String send() {
        return opcode + "" + follow + "" + numOfUsers + usrMameList ;
    }
}
