package protocol.oblivious.iknp;

public class TransferMessage {
    byte[] y0;
    byte[] y1;

    public TransferMessage(byte[] y0, byte[] y1) {
        this.y0 = y0;
        this.y1 = y1;
    }

    public TransferMessage() {
    }

    public byte[] getY0() {
        return y0;
    }

    public byte[] getY1() {
        return y1;
    }
}
