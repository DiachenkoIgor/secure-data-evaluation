package protocol.words_eval;

import protocol.Util.Util;
import protocol.nlp.NlpUtil;
import protocol.nlp.POSTool;
import protocol.nlp.StemTool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WordComparisonConstructor {
    private InputStream is;
    private OutputStream os;
    private POSTool posTool;
    private StemTool stemTool;
    private ByteComparisonConstructor bcc;

    public WordComparisonConstructor(InputStream is, OutputStream os, String pathToTrainData) {
        this.is = is;
        this.os = os;
        this.posTool=new POSTool(Util.getResourceAsStream(pathToTrainData));
        this.stemTool=new StemTool();
        this.bcc=new ByteComparisonConstructor(is,os);
    }

    public boolean compareWord(String word) throws IOException {
        String word_root = stemTool.stem(word);
        word_root=word_root.toLowerCase();
        int length = word_root.length();

        boolean compare_result= bcc.compare(Util.byteToBitArray((byte) length),false);

        String pos_res= posTool.tag(word)[0];
        byte pos_byte = NlpUtil.convertTag(pos_res);

        compare_result &= bcc.compare(Util.byteToBitArray(pos_byte),false);

        if(!compare_result) return false;

        if(word_root.length()*8>80){
            bcc.initExtendedOblivious(word_root.length()*8);
            for(int i=0;i<word_root.length();i++){
                boolean char_compare = bcc.compare(Util.byteToBitArray((byte) word_root.charAt(i)),true);
                if(!char_compare) return false;
            }

        }else {

            for (int i = 0; i < word_root.length(); i++) {
                boolean char_compare = bcc.compare(Util.byteToBitArray((byte) word_root.charAt(i)),false);
                if (!char_compare) return false;
            }
        }
        return true;
    }
    public void stop() throws IOException {
        this.is.close();
        this.os.close();
    }
}
