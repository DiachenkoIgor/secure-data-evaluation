package protocol.oblivious.fastGC.model;

import java.math.BigInteger;

public class ChooserPublicKey {
    BigInteger pk;

    public ChooserPublicKey() {
    }

    public ChooserPublicKey(BigInteger pk) {
        this.pk = pk;
    }

    public BigInteger getPk() {
        return pk;
    }
}
