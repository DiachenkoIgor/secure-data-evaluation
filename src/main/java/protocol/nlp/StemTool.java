package protocol.nlp;

import org.tartarus.snowball.SnowballStemmer;

public class StemTool {
   private SnowballStemmer stemmer;


    public StemTool() {
        try {
            Class stemClass = Class.forName("org.tartarus.snowball.ext.englishStemmer");
            this.stemmer = (SnowballStemmer) stemClass.newInstance();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }


    public String stem(String word){
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
