package protocol.words_eval;

import protocol.nlp.NlpUtil;
import protocol.nlp.POSTool;
import protocol.nlp.StemTool;
import protocol.Util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WordComparisonEvaluator {
    private InputStream is;
    private OutputStream os;
    private POSTool posTool;
    private StemTool stemTool;
    private ByteComparisonEvaluator bce;

    public WordComparisonEvaluator (InputStream is, OutputStream os, String pathToTrainData) {
        this.is = is;
        this.os = os;
        this.posTool=new POSTool(pathToTrainData);
        this.stemTool=new StemTool();
        this.bce=new ByteComparisonEvaluator(is,os);
    }

    public void compareWord(String word) throws IOException {
        String word_root = stemTool.stem(word);
        word_root=word_root.toLowerCase();
        int length = word_root.length();
        byte[] size_bits = Util.byteToBitArray((byte) length);

        boolean compare_res = this.bce.compare(size_bits,false);

        String pos_res= posTool.tag(word)[0];
        byte pos_byte = NlpUtil.convertTag(pos_res);

        byte[] pos_bits = Util.byteToBitArray(pos_byte);
        compare_res&=this.bce.compare(pos_bits,false);
        if(!compare_res) return;

        if(word_root.length()*8>80){
            byte[] choice = Util.byteToBitArray((byte) word_root.charAt(0));
            for(int i=1;i<word_root.length();i++){
                choice=Util.concatByteArrays(choice,
                        Util.byteToBitArray((byte) word_root.charAt(i)));
            }
            this.bce.prepareForExtendedOT(choice);
            for(int i=0;i<word_root.length();i++){
                if(!this.bce.compare(choice,true)) return;
            }

        }else {
            for (int i = 0; i < word_root.length(); i++) {
                if (!this.bce.compare(Util.byteToBitArray((byte) word_root.charAt(i)),false)) return;
            }
        }
    }

    public void stop() throws IOException {
        this.is.close();
        this.os.close();
    }
}
