
package protocol.sfe;

import protocol.Util.Util;
import protocol.domain.ContinueMessage;
import protocol.domain.CryptoPairHolder;
import protocol.domain.GarbledTableMessage;
import protocol.domain.GateResult;
import protocol.domain.exceptions.SFEIOException;
import protocol.oblivious.fastGC.NPObliviousSender;
import protocol.oblivious.iknp.ExtendedObliviousSender;

import java.io.*;
import java.util.Arrays;


public class SFEConstructor {
    private InputStream is;
    private OutputStream os;
    private NPObliviousSender sender;
    private ExtendedObliviousSender eos;

    public SFEConstructor(InputStream is, OutputStream os) {

            this.os = os;
            this.is = is;
    }

    public void prepareForExtendedTransfer(int quantityOfPairs,int length) throws IOException {
        this.eos=new ExtendedObliviousSender(is,os,quantityOfPairs,length);
        this.eos.connect();
    }
    public boolean calculate(Gate start,boolean useExtended, int messageBitLength) {
        try {
            boolean f = true;
            Gate g = start;
            while (f) {
                boolean isFinished = calculateLogic(g,useExtended,messageBitLength);
                if (isFinished) {
                    if (checkForNextNotCalculatedGates(g)) {
                        sendContinueMessage(false,(byte)-1);
                        throw new RuntimeException("Size of compare bits is different");
                    } else {
                        Gate last = g;
                        while (last.getNext() != null) {
                            last = last.getNext();
                        }
                        sendContinueMessage(false,last.getOutputValue());
                        return last.getOutputValue() == 1;
                    }
                }
                if (!checkForNextNotCalculatedGates(g)) {
                    if(useExtended){
                        Gate last = g;
                        while (last.getNext() != null) {
                            last = last.getNext();
                        }
                        sendContinueMessage(false,last.getOutputValue());
                        return last.getOutputValue() == 1;
                    }
                    sendContinueMessage(false,(byte)-1);
                    throw new RuntimeException("Size of compare bits is different");
                } else {
                    if(g.isCalculated()){
                        g=g.getNext();
                        continue;
                    }
                    sendContinueMessage(true,(byte)2);
                    g = g.getNext();
                    f = true;
                }
            }

        }catch (IOException ex){
            ex.printStackTrace();
            throw new SFEIOException("Something went wrong for SFEConstructor!!");
        }
        throw new SFEIOException("Something went wrong for SFEConstructor Protocol!!");
    }

    private boolean calculateLogic(Gate gate, boolean useExtended, int messageBitLength) throws IOException {
        if(gate.isCalculated()) {
            gate.calculate();
            return false;
        }
        CryptoPairHolder[] encodedTable=new CryptoPairHolder[4];

        for(int i=0;i<gate.getEncodeResult().length;i++){
            encodedTable[i]=new CryptoPairHolder(gate.getEncodeResult()[i],gate.getResult_macA()[i],gate.getResult_macB()[i]);
        }
        Util.shuffleArray(encodedTable);

        GarbledTableMessage tableMessage = createGarbledTableMessage(encodedTable,gate);
        Util.sendMessage(Util.objectMapper.writeValueAsString(tableMessage),os);

        byte[][] otValues = prepareValueForOT(gate);
        if(this.sender==null && !useExtended){
            try {
                this.sender=new NPObliviousSender(messageBitLength,is,os);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(useExtended){
            this.eos.send(otValues[0],otValues[1]);
        }else {
            this.sender.send(otValues[0], otValues[1]);
        }

        GateResult gateResult = Util.objectMapper.readValue(Util.receiveMessage(is), GateResult.class);

        gate.setOutput(defineResult(gate,gateResult.getValue()));
        return gateResult.isFinish();
    }

    private  byte defineResult(Gate gate,byte[] label){
        if(Arrays.equals(label,gate.getLabels()[4])){

            return 0;
        }else {

            return 1;
        }
    }
    private GarbledTableMessage createGarbledTableMessage(CryptoPairHolder[] encodedTable, Gate gate){
        return  new GarbledTableMessage(encodedTable, gate.getInputValueLabel());
    }

    private byte[][] prepareValueForOT(Gate gate){
        byte[] a = gate.getLabels()[2];
        byte[] b = gate.getLabels()[3];
        return new byte[][]{a,b};
    }
    private boolean checkForNextNotCalculatedGates(Gate g){
        Gate gate=g.getNext();
        while (gate!=null){
            if(gate.isCalculated())
            {
                gate.calculate();
                gate=gate.getNext();
                continue;
            }
            return true;
        }
        return false;
    }
    private void sendContinueMessage(boolean val,byte res) throws IOException{
        Util.sendMessage(
                Util.objectMapper.writeValueAsString(new ContinueMessage(val,res)),
                os
        );
    }

}

