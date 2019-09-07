package protocol.nlp;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class POSTool {
    private POSModel model;
    private POSTaggerME tagger;

    public POSTool(InputStream from) {

        try {
            model = new POSModel(from);
            tagger = new POSTaggerME(model);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("POS TOOL problem");
        }
    }
    public String[] tag(String... sentence) {
        try {

            return tagger.tag(sentence);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("POS TOOL problem");
        }
    }

}
