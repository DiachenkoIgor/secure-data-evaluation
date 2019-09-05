package protocol.oblivious.fastGC.model;

import java.io.Serializable;
import java.math.BigInteger;

public class NPOTTransfer implements Serializable {
    private BigInteger m1;
    private BigInteger m2;
    private BigInteger r;

    public NPOTTransfer(BigInteger m1, BigInteger m2, BigInteger r) {
        this.m1 = m1;
        this.m2 = m2;
        this.r = r;
    }

    public NPOTTransfer() {
    }

    public BigInteger getM1() {
        return m1;
    }

    public BigInteger getM2() {
        return m2;
    }

    public BigInteger getR() {
        return r;
    }
}
