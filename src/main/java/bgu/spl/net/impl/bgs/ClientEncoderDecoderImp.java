package bgu.spl.net.impl.bgs;

import bgu.spl.net.Message.ClientMessageFactory;
import bgu.spl.net.api.ClientEncoderDecoder;

import java.io.UnsupportedEncodingException;

public class ClientEncoderDecoderImp implements ClientEncoderDecoder<String> {

    @Override
    public String decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(String message) {
        String stringToServer = ClientMessageFactory.getMessageToSendServer(message);
        try {
            return (stringToServer+"\n").getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
