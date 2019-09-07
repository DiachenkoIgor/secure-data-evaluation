package words;

import org.junit.Assert;
import org.junit.Test;
import protocol.nlp.NlpUtil;
import protocol.nlp.POSTool;
import protocol.nlp.StemTool;

public class NLPTest {

    @Test
    public void testNLPToolsWithSameWord(){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        POSTool posTool = new POSTool(classloader.getResourceAsStream("nlp/en-pos-maxent.bin"));
        StemTool stemTool=new StemTool();
        String wordA="Table";
        String wordB="table";

        Assert.assertEquals(posTool.tag(wordA)[0],posTool.tag(wordB)[0]);
        Assert.assertEquals(NlpUtil.convertTag(posTool.tag(wordA)[0]),NlpUtil.convertTag(posTool.tag(wordB)[0]));

        Assert.assertEquals(stemTool.stem(wordA.toLowerCase()),stemTool.stem(wordB.toLowerCase()));
    }

    @Test
    public void testNLPToolsWithAnotherWord(){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        POSTool posTool = new POSTool(classloader.getResourceAsStream("nlp/en-pos-maxent.bin"));
        StemTool stemTool=new StemTool();
        String wordA="Hello";
        String wordB="table";

        Assert.assertNotEquals(posTool.tag(wordA)[0],posTool.tag(wordB)[0]);
        Assert.assertNotEquals(NlpUtil.convertTag(posTool.tag(wordA)[0]),NlpUtil.convertTag(posTool.tag(wordB)[0]));

        Assert.assertNotEquals(stemTool.stem(wordA),stemTool.stem(wordB));
    }


}
