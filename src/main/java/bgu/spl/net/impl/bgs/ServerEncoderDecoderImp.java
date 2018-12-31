package bgu.spl.net.impl.bgs;

import bgu.spl.net.Message.ClientToServerMessage;
import bgu.spl.net.api.ServerEncoderDecoder;

import java.io.UnsupportedEncodingException;

public class ServerEncoderDecoderImp<T> implements ServerEncoderDecoder<T> {
    @Override
    public T decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(T message) {
        if(!( message instanceof ClientToServerMessage))
            try {
                throw new Exception("Is not a message");
            } catch (Exception e) {
                e.printStackTrace();
            }
        ClientToServerMessage clientToServerMessage= (ClientToServerMessage)message;
        try {
            return (clientToServerMessage.send()).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
