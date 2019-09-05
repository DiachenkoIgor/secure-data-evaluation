package protocol.sfe;


import protocol.Util.CryptoUtil;

public abstract class Gate {
   private byte[][] table;
   private byte[][] encodeResult;
   private byte[][] labels;
   private byte[][] result_macA;
   private byte[][] result_macB;
   private Gate nextGate;
   private Wire[] input;
   private Wire output;

    public Gate(Wire input, Wire output) {
        this.input=new Wire[2];
        this.input[0] = input;
        this.output = output;
        this.labels=new byte[6][];
        this.result_macA =new byte[4][];
        this.result_macB =new byte[4][];
        this.encodeResult=new byte[4][];
        this.table =new byte[][]{{0,0,0},{0,1,0},{1,0,0},{1,1,0}};
        createTable();
        initializeLabels();
        encodeTable();
    }

    public Gate(Wire inputA,Wire inputB, Wire output) {
        this.input=new Wire[2];
        this.input[0] = inputA;
        this.input[1] = inputB;
        this.output = output;
        this.labels=new byte[6][];
        this.result_macA =new byte[4][];
        this.result_macB =new byte[4][];
        this.encodeResult=new byte[4][];
        this.table =new byte[][]{{0,0,0},{0,1,0},{1,0,0},{1,1,0}};
        createTable();
    }


    private void initializeLabels() {
        try {
            labels[0] = CryptoUtil.generateEncodedAESKey();
            labels[1] = CryptoUtil.generateEncodedAESKey();
            labels[2] = CryptoUtil.generateEncodedAESKey();
            labels[3] = CryptoUtil.generateEncodedAESKey();
            byte[] z0Label=new byte[2];
            CryptoUtil.generateRandomBytes(z0Label);
            byte[] z1Label=new byte[2];
            CryptoUtil.generateRandomBytes(z1Label);
            labels[4] = z0Label;
            labels[5] = z1Label;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new IllegalStateException();
        }

    }

    protected abstract void createTable();

    public void calculate(){
        for(int i=0;i<table.length;i++) {
            if(table[i][0]==input[0].getValue() && table[i][1]==input[1].getValue())
            setOutput(table[i][2]);
        }
    }

    protected byte[][] getTable() {
        return table;
    }


    private void encodeTable(){
        for(int i=0;i<encodeResult.length;i++){
            encodeResult[i]=table[i][2]==0? labels[4]:labels[5];
        }
        for(int i=0;i<encodeResult.length;i++){
            byte[] key=table[i][1]==0 ? labels[2]:labels[3];
            this.result_macB[i]=CryptoUtil.generateHMAC(encodeResult[i],key);
            encodeResult[i]=CryptoUtil.AESencrypt(encodeResult[i],key);
        }
        for(int i=0;i<encodeResult.length;i++){
            byte[] key=table[i][0]==0 ? labels[0]:labels[1];
            this.result_macA[i]=CryptoUtil.generateHMAC(encodeResult[i],key);
            encodeResult[i]=CryptoUtil.AESencrypt(encodeResult[i],key);
        }
    }
    public Gate getNext(){return this.nextGate;}
    public void setNext(Gate gate){
        this.nextGate=gate;
    }
    public void setOutput(byte outV){
        this.output.setValue(outV);
    }
    public byte getInputValue(){
        return input[0].getValue();
    }
    public byte[] getInputValueLabel(){
        return labels[input[0].getValue()];
    }
    public byte getOutputValue(){
        return this.output.getValue();
    }

    public byte[][] getEncodeResult() {
        return encodeResult;
    }

    public byte[][] getLabels() {
        return labels;
    }

    public byte[][] getResult_macA() {
        return result_macA;
    }
    public byte[][] getResult_macB() {
        return result_macB;
    }

    public boolean isCalculated(){
        return !(input[1]==null);
    }

    public Wire getOutput() {
        return output;
    }
}
