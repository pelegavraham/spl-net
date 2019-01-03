package bgu.spl.net.impl.ConversationProtocol;

import bgu.spl.net.ServerMessages.*;
import bgu.spl.net.api.ServerEncoderDecoder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Test the encoder decoder
 */
public class ServerEncoderDecoderImplTest
{

    private ServerEncoderDecoder<Message> encdec;

    @Before
    public void setUp()
    {
        encdec = new ServerEncoderDecoderImpl();
    }

    @After
    public void tearDown()
    {
        encdec = null;
    }

    @Test
    public void decodeNextByteTest()
    {
        decodeTest1();
        decodeTest2();
        decodeTest3();
        decodeTest4();
        decodeTest5();
        decodeTest6();
        decodeTest7();
        decodeTest8();
    }

    @Test
    public void encodeTest()
    {
        encodeTest1();
        encodeTest2();
        encodeTest3();
    }


    // ------------------------------------------------------------

    private void encodeTest1() // notification
    {
        short opcode = 9;
        List<Byte> bytes = new LinkedList<>();
        byte[] temp;
        byte notificationType = randomByte();
        String sender = randomString(9);
        String content = randomString(115);

        temp = shortToBytes(opcode);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        bytes.add(notificationType);

        for (byte b : sender.getBytes())
        bytes.add(b);

        bytes.add((byte)'\0');

        for (byte b : content.getBytes())
            bytes.add(b);

        bytes.add((byte)'\0');


        encode(bytes, new NotificationMessage((notificationType == 0), sender, content ));
    }

    private void encodeTest2() // ACK
    {
        encodeTest2_1();
        encodeTest2_2();
        encodeTest2_3();
        encodeTest2_4();
    }

    private void encodeTest2_1() // ACK
    {
        short opcode =10;
        short otherOpcde = (short) (new Random().nextInt(8) +1);
        List<Byte> bytes = new LinkedList<>();
        byte[] temp;

        temp = shortToBytes(opcode);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes(otherOpcde);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        encode(bytes, new AckMessage(otherOpcde));
    }

    private void encodeTest2_2() // UserListAckMessage
    {
        short opcode =10;
        List<String> users = new LinkedList<>();
        List<Byte> bytes = new LinkedList<>();
        int numOfUsers = new Random().nextInt(11) +1 ;
        byte[] temp;

        for (int i = 1; i <= numOfUsers ; i++)
            users.add(randomString(i));

        temp = shortToBytes(opcode);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes((short) 7);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes((short) numOfUsers);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        for (String s : users)
        {
            for (byte b : s.getBytes())
                bytes.add(b);

            bytes.add((byte)'\0');
        }

        encode(bytes, new UserListAckMessage(users));
    }

    private void encodeTest2_3() // StatAckMessage
    {
        short opcode =10;
        List<Byte> bytes = new LinkedList<>();
        short NumPosts = (short) new Random().nextInt(120);
        short NumFollowers = (short) new Random().nextInt(120);
        short NumFollowing = (short) new Random().nextInt(120);
        byte[] temp;

        temp = shortToBytes(opcode);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes((byte)8);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes(NumPosts);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes(NumFollowers);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes(NumFollowing);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        encode(bytes, new StatAckMessage(NumPosts, NumFollowers, NumFollowing));
    }

    private void encodeTest2_4() // FollowAckMessage
    {
        short opcode = 10;
        int numOfUsers = new Random().nextInt(12) +1;
        List<String> users = new LinkedList<>();
        List<Byte> bytes = new LinkedList<>();
        byte[] temp;

        for (int i = 1; i <= numOfUsers ; i++)
            users.add(randomString(i));

        temp = shortToBytes(opcode);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes((short)4);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes((short) numOfUsers);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        for(String s : users)
        {
            for(byte b : s.getBytes())
                bytes.add(b);

            bytes.add((byte)'\0');
        }

        encode(bytes, new FollowAckMessage(users));
    }

    private void encodeTest3() // error
    {
        short opcode =11;
        short otherOpcde = (short) (new Random().nextInt(8) +1);
        List<Byte> bytes = new LinkedList<>();
        byte[] temp;

        temp = shortToBytes(opcode);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        temp = shortToBytes(otherOpcde);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        encode(bytes, new ErrorMessage(otherOpcde));
    }

    // --------------------------------------------------------------------------

    private void decodeTest1() // register
    {
        String[] morty = new String[] {"00", "01", "4d", "6f", "72", "74", "79", "00", "61", "31", "32", "33", "00"};
        Message decoded = decode(morty);

        Assert.assertTrue(decoded instanceof RegisterMessage); // must be empty

        RegisterMessage rm = (RegisterMessage)decoded;

        Assert.assertEquals("Morty", rm.getUserName());
        Assert.assertEquals("a123", rm.getPassword());
    }

    private void decodeTest2() // login
    {
        String[] morty = new String[] {"00", "02", "4d", "6f", "72", "74", "79", "00", "61", "31", "32", "33", "00"};
        Message decoded = decode(morty);

        Assert.assertTrue(decoded instanceof LoginMessage); // must be empty

        LoginMessage lm = (LoginMessage)decoded;

        Assert.assertEquals("Morty", lm.getUserName());
        Assert.assertEquals("a123", lm.getPassword());
    }

    private void decodeTest3() // stat
    {
        String[] morty = new String[] {"00", "08", "4d", "6f", "72", "74", "79", "00"};
        Message decoded = decode(morty);

        Assert.assertTrue(decoded instanceof StatMessage); // must be empty

        StatMessage sm = (StatMessage)decoded;

        Assert.assertEquals("Morty", sm.getUserName());
    }

    private void decodeTest4() // post
    {
        String[] morty = new String[] {"00", "05", "4e", "6f", "62", "6f", "64", "79", "20", "65", "78", "69", "73", "74", "73", "20", "6f", "6e", "20", "70", "75", "72", "70", "6f", "73", "65", "2c", "20", "6e", "6f", "62", "6f", "64", "79", "20", "62", "65", "6c", "6f", "6e", "67", "73", "20", "61", "6e", "79", "77", "68", "65", "72", "65", "2c", "20", "65", "76", "65", "72", "79", "62", "6f", "64", "79", "e2", "80", "99", "73", "20", "67", "6f", "6e", "6e", "61", "20", "64", "69", "65", "2e", "20", "43", "6f", "6d", "65", "20", "77", "61", "74", "63", "68", "20", "54", "56", "2e", "00"};
        Message decoded = decode(morty);

        Assert.assertTrue(decoded instanceof PostMessage); // must be empty

        PostMessage pm = (PostMessage)decoded;

        Assert.assertEquals("Nobody exists on purpose, nobody belongs anywhere, everybody’s gonna die. Come watch TV.", pm.getContent());
    }

    private void decodeTest5() // logout
    {
        String[] morty = new String[] {"00", "03"};
        Message decoded = decode(morty);

        Assert.assertTrue(decoded instanceof LogoutMessage); // must be empty
    }

    private void decodeTest6() //Follow
    {
        short opcode = 4;
        byte follow = randomByte();
        int numOfUsers;
        List<String> users = new LinkedList<>();
        List<Byte> bytes = new LinkedList<>();
        byte[] temp;

        numOfUsers = new Random().nextInt(12) +1 ;

        for (int i = 1; i <= numOfUsers ; i++)
            users.add(randomString(i));

        temp = shortToBytes(opcode);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        bytes.add(follow);

        temp = shortToBytes((short) numOfUsers);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        for(String s : users)
        {
            for(byte b : s.getBytes())
                bytes.add(b);

            bytes.add((byte)'\0');
        }

        Message message = decode(bytes);

        Assert.assertTrue(message instanceof FollowMessage);

        FollowMessage fm = (FollowMessage)message;

        //let check if we received what we started with
        Assert.assertTrue( ((follow == 0) & fm.isFollow() ) | ((follow == 1) & !fm.isFollow() ) );
        Assert.assertEquals(users, fm.getUsers());
    }

    private void decodeTest7() //PM
    {
        short opcode = 6;
        String user = randomString(7);
        String content = randomString(83);
        List<Byte> bytes = new LinkedList<>();
        byte[] temp;

        temp = shortToBytes(opcode);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        for(byte b : user.getBytes())
            bytes.add(b);

        bytes.add((byte)'\0');

        for(byte b : content.getBytes())
            bytes.add(b);

        bytes.add((byte)'\0');

        Message message = decode(bytes);

        Assert.assertTrue(message instanceof PmMessage);

        PmMessage pm = (PmMessage)message;

        //let check if we received what we started with
        Assert.assertEquals(user, pm.getReciver());
        Assert.assertEquals(content, pm.getContent());
    }

    private void decodeTest8() //user list
    {
        short opcode = 7;
        List<Byte> bytes = new LinkedList<>();
        byte[] temp;

        temp = shortToBytes(opcode);
        bytes.add(temp[0]);
        bytes.add(temp[1]);

        Message message = decode(bytes);

        Assert.assertTrue(message instanceof UserListMessage);
    }

    // ---------------------------------------------------------------------------------------

    private void encode(List<Byte> expected, Message actual)
    {
        encode(expected, encdec.encode(actual));
    }


    private void encode(List<Byte> expected, byte[] actual)
    {
        Assert.assertEquals(expected.size(), actual.length);

        int size = expected.size();

        for (int i = 0; i < size; i++)
            Assert.assertEquals(expected.remove(0).byteValue(), actual[i]);
    }


    private Message decode(String[] hex)
    {
       return decode(hexToByte(hex));
    }

    private Message decode(List<Byte> bytes)
    {
        Message decoded = null;

        while(decoded == null && !bytes.isEmpty())
            decoded = encdec.decodeNextByte(bytes.remove(0));

        Assert.assertTrue(bytes.isEmpty()); // must be empty
        Assert.assertNotNull(decoded); // must be decoded

        return decoded;
    }

    private String randomString(int length)
    {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= length; i++) {
            sb.append((char)(r.nextInt(26) + 'a'));
        }

        return sb.toString();
    }

    private byte randomByte()
    {
        if(new Random().nextInt(3) == 0)
             return  0;
        else
            return  1;
    }

    private List<Byte> hexToByte(String[] hexString) {

        List<Byte> bytes = new LinkedList<>();

        for(String s : hexString)
            bytes.add(hexToByte(s));

        return bytes;
    }

    private byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

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
}
