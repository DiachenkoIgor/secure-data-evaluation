package protocol.oblivious.iknp;

import java.math.BigInteger;

public class TransferMessage {
    BigInteger y0;
    BigInteger y1;

    public TransferMessage(BigInteger y0, BigInteger y1) {
        this.y0 = y0;
        this.y1 = y1;
    }

    public TransferMessage() {
    }

    public BigInteger getY0() {
        return y0;
    }

    public BigInteger getY1() {
        return y1;
    }
}
