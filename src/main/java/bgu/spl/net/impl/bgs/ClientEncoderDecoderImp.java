package bgu.spl.net.impl.bgs;

import bgu.spl.net.api.ClientEncoderDecoder;

public class ClientEncoderDecoderImp implements ClientEncoderDecoder<String> {

    @Override
    public String decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(String message) {
        ClientMessageFactory factory= new ClientMessageFactory();
        byte[] stringToServer = factory.getMessageToSendServer(message);
            return stringToServer;
    }
}
