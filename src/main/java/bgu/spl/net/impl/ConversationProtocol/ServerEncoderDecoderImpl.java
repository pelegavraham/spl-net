package bgu.spl.net.impl.ConversationProtocol;

import bgu.spl.net.ServerMessages.*;
import bgu.spl.net.api.ServerEncoderDecoder;

import java.util.*;

/**
 * This class encode and encode messages in the server
 */
public class ServerEncoderDecoderImpl implements ServerEncoderDecoder<Message>
{

    // DECODER DATA HOLDERS

    /** the opcode */
    private Short opcode;

    /** the first bye of the opcode */
    private  Byte firstOpcode;

    /** the number of 0 bytes that were read */
    private int numOf0;

    //REGISTER + LOGIN + PM
    private List<Byte> userName;
    private List<Byte> password;

    //FOLLOW
    private Byte followOrUnFollow;
    private Byte numOfUsersFirstByte;
    private Short numOfUsers;
    private List<String> userNameList;
    private List<Byte> currentUserName;

    //POST + STATS
    private List<Byte> content;


    // END DECODER DATA HOLDERS

    /**
     * Constructor
     */
    public ServerEncoderDecoderImpl()
    {
        clear();
    }

    @Override
    public Message decodeNextByte(byte nextByte)
    {
       if(firstOpcode == null) // first byte of the message
       {
           firstOpcode = nextByte;
           return null;
       }

        if(opcode == null) // second byte of the message
        {
            byte[] opcodeBytes = new byte[2];
            opcodeBytes[0] = firstOpcode;
            opcodeBytes[1] = nextByte;
            opcode = bytesToShort(opcodeBytes);

            //messages that doesn't require any more data

            if(opcode == 3)
            {
                clear();
                return new LogoutMessage();
            }

            if(opcode == 7)
            {
                clear();
                return new UserListMessage();
            }

            return null; // more bytes are required
        }

        return getDecodeNextByteWithOpCode(nextByte);
    }

    @Override
    public byte[] encode(Message message)
    {
        Objects.requireNonNull(message);

        if(message instanceof ErrorMessage)
            return getError((ErrorMessage)message);

        if(message instanceof NotificationMessage)
            return getNotification((NotificationMessage) message);

        if(message instanceof AckMessage)
            return getAck((AckMessage) message);

        throw new IllegalArgumentException("Unknown message type!");
    }

    /**
     * Encode the next byte.
     * it is known that the opcode were already recived.
     * @param nextByte thr byte
     * @return the message if competed, null otherwise
     */
    private Message getDecodeNextByteWithOpCode(byte nextByte) {

        Objects.requireNonNull(opcode);

        if(opcode == 1)
            return getDecodeRegisterLoginPM(nextByte);

        if(opcode == 2)
            return getDecodeRegisterLoginPM(nextByte);


        if(opcode == 4)
            return getDecodeFollow(nextByte);

        if(opcode == 5)
            return getDecodePostStats(nextByte);

        if(opcode == 6)
            return getDecodeRegisterLoginPM(nextByte);


        if(opcode == 8)
            return getDecodePostStats(nextByte);

        throw new IllegalArgumentException("Illegal message to decode! Opcode : " + opcode );
    }

    /**
     * Encode the next byte.
     * it is known that this is a post message
     * @param nextByte thr byte
     * @return the message if competed, null otherwise
     */
    private Message getDecodePostStats(byte nextByte)
    {
        if(nextByte == '\0') // end of message
        {
            String con = new String(list2array(content));
            Short op = opcode;
            clear(); // end of message

            if(op == 5)
                return new PostMessage(con);

            if(op == 8)
                return new StatMessage(con);

            throw new IllegalStateException("just for the compiler");
        }
        else {
            content.add(nextByte);
            return null;
        }
    }

    /**
     * Encode the next byte.
     * it is known that this is a follow message
     * @param nextByte thr byte
     * @return the message if competed, null otherwise
     */
    private Message getDecodeFollow(byte nextByte)
    {
        if(followOrUnFollow == null) // still trying to infer follow or unfollow
        {
            followOrUnFollow = nextByte;
            return null;
        }

        if(numOfUsers == null) // reading num of users
        {
            if(numOfUsersFirstByte == null) // first
            {
                numOfUsersFirstByte = nextByte;
                return null;
            }
            else // finished
            {
                byte[] arr = new byte[2];
                arr[0] = numOfUsersFirstByte;
                arr[1] = nextByte;

                numOfUsers = bytesToShort(arr);

                if(numOfUsers == 0) // no users todo : right?
                {
                    List<String> copy = new LinkedList<>();
                    boolean isFollow = (followOrUnFollow == 0);

                    clear(); // to start reeading new message
                    return new FollowMessage(isFollow, copy);
                }

                return null; // stil reading
            }
        }

        if(nextByte == '\0') // end of user name string
        {
            numOf0++;
            userNameList.add(new String(list2array(currentUserName)));
            currentUserName.clear(); // to start reading new user

            if(numOf0 == numOfUsers) // end
            {
                List<String> copy = new LinkedList<>(userNameList);
                boolean isFollow = followOrUnFollow == 0;

                clear(); // to start reeading new message
                return new FollowMessage(isFollow, copy);
            }

            return null; // there are mor users ti read
        }

        //stil reading a user
        currentUserName.add(nextByte);
        return null; // not the end

    }

    /**
     * Encode the next byte.
     * it is known that this is an register, or log in message
     * @param nextByte thr byte
     * @return the message if competed, null otherwise
     */
    private Message getDecodeRegisterLoginPM(byte nextByte)
    {
        if(numOf0 == 0) // still reading username
        {
            if(nextByte == '\0')
            {
                numOf0++;
                return null;
            }
            else {
                userName.add(nextByte);
                return null;
            }
        }

        if(numOf0 == 1) // still reading password
        {
            if(nextByte == '\0') // end of message
            {
                String username = new String(list2array(userName));
                String pass = new String(list2array(password));
                Short op = opcode;

                clear(); // end of message

                if(op == 1)
                    return new RegisterMessage(username, pass);

                if(op == 2)
                    return new LoginMessage(username, pass);

                if(op == 6)
                    return new PmMessage(username, pass);

                throw new IllegalStateException("just for the compiler");
            }
            else
            {
                password.add(nextByte);
                return null;
            }
        }

        throw new IllegalStateException("just for the compiler");
    }


    /**
     * delete curent data to start decoding new message
     */
    private void clear()
    {
        opcode = null;
        firstOpcode = null;
        numOf0 = 0;

        userName = new LinkedList<>();
        password =new LinkedList<>();

        followOrUnFollow = null;
        numOfUsersFirstByte = null;
        numOfUsers = null;
        userNameList = new LinkedList<>();
        currentUserName = new LinkedList<>();

        content = new LinkedList<>();
    }

    /**
     * convert an error message to bytes according to the protocol
     * @param message the message
     * @return the byte array
     */
    private byte[] getError(ErrorMessage message)
    {
        Objects.requireNonNull(message);

        byte[] arr = new byte[4];

        byte[] opcode = shortToBytes((short)11);
        arr[0] = opcode[0];
        arr[1] = opcode[1];

        opcode = shortToBytes(message.getOpcodeMessage());
        arr[2] = opcode[0];
        arr[3] = opcode[1];

        return arr;
    }

    /**
     * convert a notification message to bytes according to the protocol
     * @param message the message
     * @return the byte array
     */
    private byte[] getNotification(NotificationMessage message) {

        Objects.requireNonNull(message);

        List<Byte> bytes = new LinkedList<>();

        byte[] opcode = shortToBytes((short)9);
        bytes.add(opcode[0]);
        bytes.add(opcode[1]);

        bytes.add(boolToShort(!message.isPrivate()));

        for(byte b : message.getPostingUser().getBytes())
            bytes.add(b);

        bytes.add((byte)'\0');

        for(byte b : message.getContent().getBytes())
            bytes.add(b);

        bytes.add((byte)'\0');

        return list2array(bytes);
    }


    /**
     * convert am ACK message to bytes according to the protocol
     * @param message the message
     * @return the byte array
     */
    private byte[] getAck(AckMessage message)
    {
        Objects.requireNonNull(message);

        if(message instanceof UserListAckMessage)
            return getUserlistACK((UserListAckMessage) message);

        if(message instanceof StatAckMessage)
            return getStatsACK((StatAckMessage) message);

        if(message instanceof FollowAckMessage)
            return getFollowACK((FollowAckMessage) message);

        return getACK(message);
    }


    /**
     * convert an ACK  message to bytes according to the protocol
     * @param message the message
     * @return the byte array
     */
    private byte[] getACK(AckMessage message) {

        Objects.requireNonNull(message);

        byte[] arr = new byte[4];

        byte[] opcode = shortToBytes((short)10);
        arr[0] = opcode[0];
        arr[1] = opcode[1];

        opcode = shortToBytes(message.getOpcodeMessage());
        arr[2] = opcode[0];
        arr[3] = opcode[1];

        return arr;
    }

    /**
     * convert am ACK of user list message to bytes according to the protocol
     * @param message the message
     * @return the byte array
     */
    private byte[] getUserlistACK(UserListAckMessage message) {

        Objects.requireNonNull(message);

        List<Byte> bytes = new LinkedList<>();

        byte[] opcode = shortToBytes((short) 10);
        bytes.add(opcode[0]);
        bytes.add(opcode[1]);

        opcode = shortToBytes((short) 7);
        bytes.add(opcode[0]);
        bytes.add(opcode[1]);

        opcode = shortToBytes((short) message.getUsers().size());
        bytes.add(opcode[0]);
        bytes.add(opcode[1]);

        for(String user : message.getUsers())
        {
            for(byte b : user.getBytes())
                bytes.add(b);

            bytes.add((byte)'\0');
        }

        return list2array(bytes);
    }

    /**
     * convert am ACK of follow message to bytes according to the protocol
     * @param message the message
     * @return the byte array
     */
    private byte[] getFollowACK(FollowAckMessage message) {

        Objects.requireNonNull(message);

        List<Byte> bytes = new LinkedList<>();

        byte[] opcode = shortToBytes((short) 10);
        bytes.add(opcode[0]);
        bytes.add(opcode[1]);

        opcode = shortToBytes((short) 4);
        bytes.add(opcode[0]);
        bytes.add(opcode[1]);

        opcode = shortToBytes((short) message.getUsers().size());
        bytes.add(opcode[0]);
        bytes.add(opcode[1]);

        for(String user : message.getUsers())
        {
            for(byte b : user.getBytes())
                bytes.add(b);

            bytes.add((byte)'\0');
        }

        return list2array(bytes);
    }


    /**
     * convert am ACK of stats message to bytes according to the protocol
     * @param message the message
     * @return the byte array
     */
    private byte[] getStatsACK(StatAckMessage message)
    {
        Objects.requireNonNull(message);

        byte[] arr = new byte[10];

        byte[] opcode = shortToBytes((short) 10);
        arr[0] = opcode[0];
        arr[1] = opcode[1];

        opcode = shortToBytes((short) 8);
        arr[2] = opcode[0];
        arr[3] = opcode[1];

        opcode = shortToBytes((short) message.getNumOfPosts());
        arr[4] = opcode[0];
        arr[5] = opcode[1];

        opcode = shortToBytes((short) message.getNumOFollowers());
        arr[6] = opcode[0];
        arr[7] = opcode[1];

        opcode = shortToBytes((short) message.getNumOFollowing());
        arr[8] = opcode[0];
        arr[9] = opcode[1];

        return arr;
    }


    // -------------------------------------------------------------------------------



    /**
     * convert a short to 2 bytes
     * @param num the short
     * @return arra it size of 2 bytes representing that short
     */
    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    /**
     * convert array of bytes to short
     * @param byteArr the array of bytes
     * @return the shore ther are representing
     */
    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    /**
     * convert bool to byte
     * @param bool the boolean
     * @return the byte for it
     */
    private byte boolToShort(boolean bool)
    {
        if(bool)
            return 1;

        return 0;
    }

    /**
     * Convert a list of bytes to byles aray
     * @param bytes the list
     * @return the array
     */
    private  byte[] list2array(List<Byte> bytes)
    {
        byte[] arr = new byte[bytes.size()];

        Iterator<Byte> it = bytes.iterator();
        for (int i = 0; i < arr.length; i++)
            arr[i] = it.next();

        return arr;
    }
}