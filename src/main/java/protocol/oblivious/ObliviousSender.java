package protocol.oblivious;

public interface ObliviousSender {
    void send();
    void init(byte[] m0,byte[] m1);
}
