package protocol.words_eval;

import protocol.sfe.SFEEvaluator;

import java.io.InputStream;
import java.io.OutputStream;

public class ByteComparisonEvaluator {
    private InputStream is;
    private OutputStream os;
    private SFEEvaluator evaluator;

    public ByteComparisonEvaluator(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
        this.evaluator =new SFEEvaluator(is,os);
    }

    public boolean compare(byte[] bits) {
        if(bits.length!=8){
            throw new IllegalArgumentException("Bits size array is not 8");
        }
        return !this.evaluator.calculate(bits);
    }
}
