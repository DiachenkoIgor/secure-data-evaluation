package protocol.oblivious.fastGC.model;

import java.io.Serializable;
import java.math.BigInteger;

public class InitializeMessage implements Serializable {
    private BigInteger c;
    private BigInteger p;
    private BigInteger q;
    private BigInteger g;
    private BigInteger gr;
    private int msgBitLength;

    public InitializeMessage(BigInteger c, BigInteger p, BigInteger q, BigInteger g, BigInteger gr, int msgBitLength) {
        this.c = c;
        this.p = p;
        this.q = q;
        this.g = g;
        this.gr = gr;
        this.msgBitLength = msgBitLength;
    }
    public InitializeMessage(){}

    public BigInteger getC() {
        return c;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getGr() {
        return gr;
    }

    public int getMsgBitLength() {
        return msgBitLength;
    }
}
