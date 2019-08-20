package protocol.oblivious.rsa;

import java.math.BigInteger;

/**
 * Created by IgorPc on 7/9/2019.
 */
public class EndMessage {
    private BigInteger m0;
    private BigInteger m1;

    public EndMessage(BigInteger m0, BigInteger m1) {
        this.m0 = m0;
        this.m1 = m1;
    }

    public EndMessage() {
    }

    public BigInteger getM0() {
        return m0;
    }

    public void setM0(BigInteger m0) {
        this.m0 = m0;
    }

    public BigInteger getM1() {
        return m1;
    }

    public void setM1(BigInteger m1) {
        this.m1 = m1;
    }
}
