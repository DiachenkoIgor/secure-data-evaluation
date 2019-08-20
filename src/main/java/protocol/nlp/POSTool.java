package protocol.nlp;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class POSTool {
    private POSModel model;
    private POSTaggerME tagger;
    private String pathToTrainData;

    public POSTool(String pathToTrainData) {
        this.pathToTrainData = pathToTrainData;

        try {
            InputStream modelStream = new FileInputStream(pathToTrainData);
            model = new POSModel(modelStream);
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
