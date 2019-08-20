package protocol.oblivious.ecc;

import java.math.BigInteger;

public class CustomECPoint {
    private BigInteger x;
    private BigInteger y;

    public CustomECPoint(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public CustomECPoint() {
    }

    public BigInteger getX() {
        return x;
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }
}
