package bgu.spl.net.api;

public interface ClientEncoderDecoder<T> {

    T decodeNextByte(byte nextByte);

    byte[] encode(T message);
}
