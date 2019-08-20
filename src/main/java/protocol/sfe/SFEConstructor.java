
package protocol.sfe;

import protocol.Util;
import protocol.domain.ContinueMessage;
import protocol.domain.CryptoPairHolder;
import protocol.domain.GarbledTableMessage;
import protocol.domain.GateResult;
import protocol.domain.exceptions.SFEIOException;
import protocol.oblivious.ObliviousSender;
import protocol.oblivious.ecc.EccObliviousSender;
import protocol.oblivious.rsa.RsaObliviousSender;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;


/**
 * Created by IgorPc on 7/9/2019.
 */
public class SFEConstructor {
    private InputStream is;
    private OutputStream os;
    private ObliviousSender sender;

    public SFEConstructor( InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
        this.sender=new EccObliviousSender(is,os);
    }

    public boolean calculate(Gate start) {
        try {
            boolean f = true;
            Gate g = start;
            while (f) {
                boolean isFinished = calculateLogic(g);
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

    private boolean calculateLogic(Gate gate) throws IOException {
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
        String asString = Util.objectMapper.writeValueAsString(tableMessage);
        Util.sendMessage(asString,os);

        byte[][] otValues = prepareValueForOT(gate);
        sender.init(otValues[0],otValues[1]);
        sender.send();


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
        String continueM = Util.objectMapper.writeValueAsString(new ContinueMessage(val,res));
        Util.sendMessage(continueM, os);
    }

}

