package protocol.oblivious.ecc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.math.BigInteger;
import java.security.spec.ECPoint;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class EccStageOneMessage {
    private BigInteger N;
    private BigInteger A;
    private BigInteger B;
    private CustomECPoint G;
    private CustomECPoint P;
    private CustomECPoint M1;
    private CustomECPoint M2;

    public EccStageOneMessage(BigInteger n, BigInteger a, BigInteger b, ECPoint g, ECPoint p, ECPoint m1, ECPoint m2) {
        N = n;
        A = a;
        B = b;
        G = ECOperations.convertECPointToCustom(g);
        P = ECOperations.convertECPointToCustom(p);
        M1 = ECOperations.convertECPointToCustom(m1);
        M2 = ECOperations.convertECPointToCustom(m2);
    }

    public EccStageOneMessage() {
    }

    public BigInteger getN() {
        return N;
    }

    public void setN(BigInteger n) {
        N = n;
    }

    public BigInteger getA() {
        return A;
    }

    public void setA(BigInteger a) {
        A = a;
    }

    public BigInteger getB() {
        return B;
    }

    public void setB(BigInteger b) {
        B = b;
    }

    public ECPoint getG() {
        return ECOperations.convertCustomECPoint(G);
    }

    public void setG(ECPoint g) {
        G = ECOperations.convertECPointToCustom(g);
    }

    public ECPoint getP() {
        return ECOperations.convertCustomECPoint(P);
    }

    public void setP(ECPoint p) {
        P = ECOperations.convertECPointToCustom(p);
    }

    public ECPoint getM1() {
        return ECOperations.convertCustomECPoint(M1);
    }

    public void setM1(ECPoint m1) {
        M1 = ECOperations.convertECPointToCustom(m1);
    }

    public ECPoint getM2() {
        return ECOperations.convertCustomECPoint(M2);
    }

    public void setM2(ECPoint m2) {
        M2 = ECOperations.convertECPointToCustom(m2);
    }
}
