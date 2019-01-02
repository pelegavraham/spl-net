package bgu.spl.net.impl.bgs;

public class ClientMessageFactory {


    public byte[] getMessageToSendServer(String input) {

        byte[] output = null;
        byte zero = 0;

        if(input.contains("REGISTER")){

            String[] splited= input.split("\\s+");
            byte[] opcode = shortToBytes((short)1);
            byte[] usrName = splited[1].getBytes();
            byte[] passwrd = splited[2].getBytes();
            output = new byte[opcode.length + usrName.length + 1 + passwrd.length + 1];
            int index= 2;
            output[0]= opcode[0];
            output[1]= opcode[1];
            for( int i = 0 ; i < usrName.length; i++) {
                output[index] = usrName[i];
                index++;
            }
            output[index] = zero;
            index++;
            for( int i = 0 ; i < passwrd.length; i++) {
                output[index] = passwrd[i];
                index++;
            }
            output[index] = zero;

        }
        else if(input.contains("LOGIN")){

            String[] splited = input.split("\\s+");

            byte[] opcode = shortToBytes((short)2);
            byte[] usrName = splited[1].getBytes();
            byte[] passwrd = splited[2].getBytes();

            output = new byte[opcode.length + usrName.length + 1 + passwrd.length + 1];
            int index= 2;
            output[0]= opcode[0];
            output[1]= opcode[1];
            for( int i = 0 ; i < usrName.length; i++) {
                output[index] = usrName[i];
                index++;
            }
            output[index] = zero;
            index++;
            for( int i = 0 ; i < passwrd.length; i++) {
                output[index] = passwrd[i];
                index++;
            }
            output[index] = zero;

        }
        else if(input.contains("LOGOUT")){

            byte[] opcode = shortToBytes((short)3);
            output= opcode;

        }
        else if(input.contains("FOLLOW")){

            String[] splited = input.split("\\s+");
            byte[] opcode = shortToBytes((short)4);
            byte follow = splited[1].getBytes()[0];
            short numberOfUsers = bytesToShort(splited[2].getBytes());
            byte[] numOfUsers = shortToBytes(numberOfUsers);
            int numOfByteTo_UserNameList= numberOfUsers;  //the number of the zero's
            for(int i=3 ; i< 3+ numberOfUsers ; i++){
                numOfByteTo_UserNameList= numOfByteTo_UserNameList + splited[i].getBytes().length;
            }
            output = new byte[ opcode.length + 1 + numOfUsers.length + numOfByteTo_UserNameList];
            output[0] = opcode[0];
            output[1] = opcode[1];
            output[2] = follow;
            output[3] = numOfUsers[0];
            output[4] = numOfUsers[1];
            int outputIndex = 5;
            for(int i=3; i< 3 + numberOfUsers ; i++){

                byte[] user = splited[i].getBytes();

                for (int j=0; j< user.length ; j++){
                    output[outputIndex]= user[j];
                    outputIndex++;
                }

                output[outputIndex]= zero;
                outputIndex++;
            }

        }
        else if(input.contains(("POST"))){

            String[] splited = input.split("\\s+");
            byte[] opcode = shortToBytes((short)5);
            String content = input.substring( input.indexOf(splited[1]) );
            byte[] contentBytes = content.getBytes();
            output= new byte[ opcode.length + contentBytes.length + 1];
            output[0] = opcode[0];
            output[1] = opcode[1];
            int index =2;
            for (int i =0 ; i < contentBytes.length ; i++){
                output[index]= contentBytes[i];
                index++;
            }
            output[index] = zero;

        }
        else if(input.contains("PM")){

            String[] splited = input.split("\\s+");
            byte[] opcode = shortToBytes((short)6);
            byte[] usrName = splited[1].getBytes();
            String content = input.substring( input.indexOf(splited[2]) );
            byte[] contentBytes = content.getBytes();
            output= new byte[ opcode.length + 1 + contentBytes.length + 1];
            output[0] = opcode[0];
            output[1] = opcode[1];
            int index =2;
            for(int i=0 ; i< usrName.length ; i++){
                output[index] = usrName[i];
                index++;
            }
            output[index] =zero;
            index++;
            for(int i=0 ; i< contentBytes.length ; i++){
                output[index] = contentBytes[i];
                index++;
            }
            output[index] = zero;

        }
        else if(input.contains("USERLIST")){

            byte[] opcode = shortToBytes((short)7);
            output = opcode;

        }
        else if(input.contains("STAT")){

            String[] splited = input.split("\\s+");
            byte[] opcode = shortToBytes((short)8);
            byte[] usrName = splited[1].getBytes();
            output = new byte[ opcode.length + usrName.length + 1];
            output[0] = opcode[0];
            output[1] = opcode[1];
            int index = 2;
            for(int i=0; i<usrName.length ; i++){
                output[index] = usrName[i];
                index++;
            }
            output[index] = zero;

        }
        return output;
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}
