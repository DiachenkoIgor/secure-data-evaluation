package protocol.words_eval;

import protocol.Util.CryptoUtil;
import protocol.sfe.SFEEvaluator;

import java.io.IOException;
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

    public void prepareForExtendedOT(byte[] choice) throws IOException {
        this.evaluator.prepareForExtendedTransfer(choice,CryptoUtil.AES_KEY_SIZE);
    }

    public boolean compare(byte[] bits,boolean useExtended) {
        if(bits.length!=8 && !useExtended){
            throw new IllegalArgumentException("Bits size array is not 8");
        }
        return !this.evaluator.calculate(bits,useExtended);
    }
}
