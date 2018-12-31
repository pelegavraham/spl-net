package bgu.spl.net.Message;

public class ClientMessageFactory {

    public static String getMessageToSendServer(String inputCommand){

        String output="";
        byte zero = 0;

        if (inputCommand.contains("REGISTER")){

            String[] splited = inputCommand.split("\\s+");
            output= '1' + splited[1] + zero + splited[2] + zero;
        }

        else if (inputCommand.contains("LOGIN")){

            String[] splited = inputCommand.split("\\s+");
            output= '2' + splited[1] + zero + splited[2] + zero;
        }

        else if (inputCommand.contains("LOGOUT")){
            output= '3' + "";
        }

        else if (inputCommand.contains("FOLLOW")){

            String[] splited = inputCommand.split("\\s+");
            int numOfUsrs=  Integer.parseInt(splited[2]);
            String usrNameLst="";
            for(int i=3; i< 3+numOfUsrs; i++)
                usrNameLst= usrNameLst+ splited[i] + zero;
            output= '4'+ splited[1] + splited[2] + usrNameLst;
        }

        else if (inputCommand.contains("POST")){

            String[] splited = inputCommand.split("\\s+");
            int indexOfContent= inputCommand.indexOf(splited[1]);
            output= '5'+ inputCommand.substring(indexOfContent) + zero;
        }
        else if (inputCommand.contains("PM")){

            String[] splited = inputCommand.split("\\s+");
            int indexOfContent= inputCommand.indexOf(splited[2]);
            output= '6'+ splited[1] + zero + inputCommand.substring(indexOfContent) + zero;
        }
        else if (inputCommand.contains("USERLIST")){

            output= '7'+ "";
        }
        else if (inputCommand.contains("STAT")){

            String[] splited = inputCommand.split("\\s+");
            output = '8'+ splited[1] + zero;
        }
        else if (inputCommand.contains("NOTIFICATION")){

            String[] splited = inputCommand.split("\\s+");
            String content = inputCommand.substring( inputCommand.indexOf(splited[3]) ) ;
            output= '9' + splited[1] + splited[2] + zero+ content + zero;
        }

        return output;
    }
}
