package protocol.words_eval;

import protocol.nlp.NlpUtil;
import protocol.nlp.POSTool;
import protocol.nlp.StemTool;
import protocol.sfe.SFEEvaluator;
import protocol.Util;

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

    public void compareWord(String word)  {

        String pos_res= posTool.tag(word)[0];
        byte pos_byte = NlpUtil.convertTag(pos_res);

        byte[] pos_bits = Util.byteToBitArray(pos_byte);
        if(!this.bce.compare(pos_bits)) return;
        String word_root = stemTool.stem(word);
        word_root=word_root.toLowerCase();
        int length = word_root.length();
        byte[] size_bits = Util.byteToBitArray((byte) length);

        boolean size_compare = this.bce.compare(size_bits);
        if(!size_compare)return;

        for(int i=0;i<word_root.length();i++){
            if(!this.bce.compare(Util.byteToBitArray((byte) word_root.charAt(i)))) return;
        }
    }


}
