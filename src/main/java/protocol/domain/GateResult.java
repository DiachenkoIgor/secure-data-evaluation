package protocol.domain;

/**
 * Created by IgorPc on 7/9/2019.
 */
public class GateResult {
    private byte[] value;
    private boolean finish;


    public GateResult(byte[] value, boolean finish) {
        this.value = value;
        this.finish = finish;
    }

    public GateResult() {
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
