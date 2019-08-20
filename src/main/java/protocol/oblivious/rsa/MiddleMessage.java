package protocol.oblivious.rsa;

        import java.math.BigInteger;

/**
 * Created by IgorPc on 7/9/2019.
 */
public class MiddleMessage {
    private BigInteger v;

    public MiddleMessage() {
    }

    public MiddleMessage(BigInteger v) {
        this.v = v;
    }

    public BigInteger getV() {
        return v;
    }

    public void setV(BigInteger v) {
        this.v = v;
    }
}
