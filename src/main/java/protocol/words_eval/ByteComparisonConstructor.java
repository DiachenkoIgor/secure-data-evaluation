package protocol.words_eval;

import protocol.sfe.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteComparisonConstructor {
    private InputStream is;
    private OutputStream os;
    private SFEConstructor sfeConstructor;

    public ByteComparisonConstructor(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
        this.sfeConstructor=new SFEConstructor(is,os);
    }
    private Gate constructGateForByteEvaluation(byte[] bits){
        Wire i=new Wire();
        Wire i2=new Wire();
        Wire o=new Wire();
        Wire o2=new Wire();
        Wire o3=new Wire();
        i.setValue(bits[0]);
        i2.setValue(bits[1]);
        XORGate startXOR=new XORGate(i,o);
        XORGate startXOR2=new XORGate(i2,o2);
        ORGate startOR=new ORGate(o,o2,o3);
        startXOR.setNext(startXOR2);
        startXOR2.setNext(startOR);
        ORGate nextOR=startOR;
        for(int j=2;j<8;j++){
            Wire inp=new Wire();
            inp.setValue(bits[j]);
            Wire out=new Wire();
            XORGate XOR=new XORGate(inp,out);
            ORGate OR=new ORGate(nextOR.getOutput(),out,new Wire());
            nextOR.setNext(XOR);
            XOR.setNext(OR);
            nextOR=OR;
        }
        return startXOR;
    }

    public boolean compare(byte[] bits) {
        if(bits.length!=8){
            throw new IllegalArgumentException("Bits size array is not 8");
        }
        Gate gate = constructGateForByteEvaluation(bits);
        return !this.sfeConstructor.calculate(gate);
    }
    public void close(){
        sfeConstructor.close();
    }
}
