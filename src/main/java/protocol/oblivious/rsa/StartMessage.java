package protocol.oblivious.rsa;

import java.math.BigInteger;

/**
 * Created by IgorPc on 7/9/2019.
 */
public class StartMessage {
    private BigInteger publicExponent;
    private BigInteger modulus;
    private BigInteger x0;
    private BigInteger x1;

    public StartMessage() {
    }

    public StartMessage(BigInteger publicExponent, BigInteger modulus, BigInteger x0, BigInteger x1) {
        this.publicExponent = publicExponent;
        this.modulus = modulus;
        this.x0 = x0;
        this.x1 = x1;
    }

    public BigInteger getPublicExponent() {
        return publicExponent;
    }

    public void setPublicExponent(BigInteger publicExponent) {
        this.publicExponent = publicExponent;
    }

    public BigInteger getModulus() {
        return modulus;
    }

    public void setModulus(BigInteger modulus) {
        this.modulus = modulus;
    }

    public BigInteger getX0() {
        return x0;
    }

    public void setX0(BigInteger x0) {
        this.x0 = x0;
    }

    public BigInteger getX1() {
        return x1;
    }

    public void setX1(BigInteger x1) {
        this.x1 = x1;
    }
}
