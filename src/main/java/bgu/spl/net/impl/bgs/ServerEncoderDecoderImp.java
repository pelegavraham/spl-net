package bgu.spl.net.impl.bgs;

import bgu.spl.net.Message.ClientToServerMessage;
import bgu.spl.net.Message.ServerToClientMessage;
import bgu.spl.net.api.ServerEncoderDecoder;

import java.io.UnsupportedEncodingException;

public class ServerEncoderDecoderImp<T> implements ServerEncoderDecoder<T> {
    @Override
    public T decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(T message) {
        return null;
    }
}
