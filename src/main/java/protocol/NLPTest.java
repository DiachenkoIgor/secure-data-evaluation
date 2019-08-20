package protocol;


import protocol.nlp.NlpUtil;
import protocol.nlp.POSTool;
import protocol.nlp.StemTool;

public class NLPTest {
    public static void main(String[] args) {
        POSTool posTool = new POSTool("C:\\Users\\igor\\Downloads\\en-pos-maxent.bin");
        StemTool stemTool=new StemTool();
        String wordA="Table";
        String wordB="table";
        System.out.println(posTool.tag(wordA)[0]);
        System.out.println(posTool.tag(wordB)[0]);
        System.out.println(NlpUtil.convertTag(posTool.tag(wordA)[0]));
        System.out.println(NlpUtil.convertTag(posTool.tag(wordB)[0]));

        System.out.println(stemTool.stem(wordA));
        System.out.println(stemTool.stem(wordB));

    }
}
