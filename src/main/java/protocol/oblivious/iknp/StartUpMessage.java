package protocol.oblivious.iknp;

public class StartUpMessage {
    private int k;
    private int m;

    public StartUpMessage(int k,int m) {
        this.k = k;
        this.m=m;
    }

    public StartUpMessage() {
    }

    public int getK() {
        return k;
    }

    public int getM() {
        return m;
    }
}
