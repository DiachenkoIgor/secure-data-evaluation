package protocol.oblivious.fastGC;

import java.security.*;
import java.math.*;

public final class Cipher {
    private static final int unitLength = 160;   // SHA-1 has 160-bit output.

    private static final BigInteger mask = BigInteger.ONE.
            shiftLeft(80).subtract(BigInteger.ONE);

    private static ThreadLocal<MessageDigest> localSha1=new ThreadLocal<>();

    public static BigInteger encrypt(BigInteger key, BigInteger msg, int msgLength) {
      return msg.xor(getPaddingOfLength(key,msgLength));
    }

    public static BigInteger decrypt(BigInteger key,BigInteger cph, int cphLength) {
        return cph.xor(getPaddingOfLength(key, cphLength));
    }

    private static MessageDigest getSha(){
        MessageDigest sha = localSha1.get();
        if(sha==null){
            try {
                sha=MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            localSha1.set(sha);
        }
        return sha;
    }
    public static BigInteger getPaddingOfLength(BigInteger key, int padLength) {
        MessageDigest sha = getSha();

        sha.update(key.toByteArray());
        BigInteger pad = BigInteger.ZERO;
        byte[] tmp = new byte[unitLength / 8];
        for (int i = 0; i < padLength / unitLength; i++) {
            System.arraycopy(sha.digest(), 0, tmp, 0, unitLength/8);
            pad = pad.shiftLeft(unitLength).xor(new BigInteger(1, tmp));
            sha.update(tmp);
        }
        System.arraycopy(sha.digest(), 0, tmp, 0, unitLength/8);
        pad = pad.shiftLeft(padLength % unitLength).
                xor((new BigInteger(1, tmp)).
                        shiftRight(unitLength - (padLength % unitLength)));
        return pad;
    }

    public static BigInteger encrypt(BigInteger r, BigInteger key, BigInteger msg, int msgLength) {
        return msg.xor(getPaddingOfLength(r, key, msgLength));
    }

    public static BigInteger decrypt(BigInteger r, BigInteger key, BigInteger cph, int cphLength) {
        return cph.xor(getPaddingOfLength(r, key, cphLength));
    }

    private static BigInteger getPaddingOfLength(BigInteger j, BigInteger key, int padLength) {
        MessageDigest sha = getSha();
        sha.update(j.toByteArray());
        sha.update(key.toByteArray());
        BigInteger pad = BigInteger.ZERO;
        byte[] tmp = new byte[unitLength / 8];
        for (int i = 0; i < padLength / unitLength; i++) {
            System.arraycopy(sha.digest(), 0, tmp, 0, unitLength/8);
            pad = pad.shiftLeft(unitLength).xor(new BigInteger(1, tmp));
            sha.update(tmp);
        }
        System.arraycopy(sha.digest(), 0, tmp, 0, unitLength/8);
        pad = pad.shiftLeft(padLength % unitLength).
                xor((new BigInteger(1, tmp)).shiftRight(unitLength - (padLength % unitLength)));
        return pad;
    }
}
