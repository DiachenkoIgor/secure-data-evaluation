package protocol.domain;

/**
 * Created by IgorPc on 7/9/2019.
 */
public class ContinueMessage {
    private boolean continued;
    private byte code;

    public ContinueMessage(boolean continued, byte code) {
        this.continued = continued;
        this.code = code;
    }

    public ContinueMessage() {
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public boolean isContinued() {
        return continued;
    }

    public void setContinued(boolean continued) {
        this.continued = continued;
    }
}
