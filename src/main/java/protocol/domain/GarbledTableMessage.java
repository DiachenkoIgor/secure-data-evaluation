package protocol.domain;

import java.io.Serializable;

/**
 * Created by IgorPc on 7/9/2019.
 */
public class GarbledTableMessage implements Serializable {
    private CryptoPairHolder[] cryptoPairHolders;
    private byte[] label;

    public GarbledTableMessage(CryptoPairHolder[] cryptoPairHolders, byte[] label) {
        this.cryptoPairHolders = cryptoPairHolders;
        this.label = label;
    }

    public GarbledTableMessage() {
    }

    public CryptoPairHolder[] getCryptoPairHolders() {
        return cryptoPairHolders;
    }

    public byte[] getLabel() {
        return label;
    }
}
