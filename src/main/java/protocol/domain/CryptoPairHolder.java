package protocol.domain;

public class CryptoPairHolder {
    private byte[] cipher;
    private byte[] hashA;
    private byte[] hashB;

    public CryptoPairHolder(byte[] encodedRes, byte[] hash,byte[] hashB) {
        this.cipher = encodedRes;
        this.hashA = hash;
        this.hashB=hashB;
    }

    public CryptoPairHolder() {
    }

    public byte[] getCipher() {
        return cipher;
    }


    public byte[] getHashB() {
        return hashB;
    }

    public void setHashB(byte[] hashB) {
        this.hashB = hashB;
    }

    public void setCipher(byte[] cipher) {
        this.cipher = cipher;
    }

    public byte[] getHashA() {
        return hashA;
    }

    public void setHashA(byte[] hashA) {
        this.hashA = hashA;
    }
}
