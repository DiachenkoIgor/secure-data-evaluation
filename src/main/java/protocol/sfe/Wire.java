package protocol.sfe;

/**
 * Created by IgorPc on 7/8/2019.
 */
public class Wire {
    private byte value;

    public Wire() {
        this.value = -1;
    }

    public byte getValue() {
        if(this.value==-1){
            throw new IllegalArgumentException("Value wasn't set!!");
        }
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
