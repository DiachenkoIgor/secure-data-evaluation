package protocol.sfe;

public class ORGate extends Gate {
    public ORGate(Wire input, Wire output) {
        super(input, output);
    }

    public ORGate(Wire inputA, Wire inputB, Wire output) {
        super(inputA, inputB, output);
    }

    @Override
    protected void createTable() {
        byte[][] table = getTable();
        for(int i=0;i<table.length;i++){
            table[i][2]=(byte)(table[i][0]|table[i][1]);
        }
    }
}
