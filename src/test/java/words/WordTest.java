package words;

import org.junit.Assert;
import org.junit.Test;
import protocol.words_eval.WordComparisonConstructor;
import protocol.words_eval.WordComparisonEvaluator;
import util.IOTests;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class WordTest {

    @Test
    public void testWordComparisonTenTimes() throws IOException {
        for (int i = 0; i < 10; i++) {
            PipedInputStream pis1 = new PipedInputStream();
            PipedInputStream pis2 = new PipedInputStream();
            PipedOutputStream pos1 = new PipedOutputStream();
            PipedOutputStream pos2 = new PipedOutputStream();
            pis1.connect(pos2);
            pis2.connect(pos1);
            new Thread(() -> {
                WordComparisonConstructor wcc = new WordComparisonConstructor(pis1, pos1, "nlp/en-pos-maxent.bin");

                try {
                    Assert.assertTrue(wcc.compareWord("tables"));
                    wcc.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
            WordComparisonEvaluator wce = new WordComparisonEvaluator(pis2, pos2, "nlp/en-pos-maxent.bin");
            wce.compareWord("tables");
            wce.stop();
        }
    }
    @Test
    public void testWordComparisonTenTimesWithoutInit() throws IOException, InterruptedException {

        PipedInputStream pis1 = new PipedInputStream();
        PipedInputStream pis2 = new PipedInputStream();
        PipedOutputStream pos1 = new PipedOutputStream();
        PipedOutputStream pos2 = new PipedOutputStream();
        pis1.connect(pos2);
        pis2.connect(pos1);
        WordComparisonConstructor wcc = new WordComparisonConstructor(pis1, pos1, "nlp/en-pos-maxent.bin");
        WordComparisonEvaluator wce = new WordComparisonEvaluator(pis2, pos2, "nlp/en-pos-maxent.bin");

        for (int i = 0; i < 10; i++) {

            new Thread(() -> {
                try {
                    Assert.assertTrue(wcc.compareWord("Honorificabilitudinitatibxcvnerwaerwqreqwrtdbvbaerertqwerzv"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
            Thread.sleep(100);
            wce.compareWord("Honorificabilitudinitatibxcvnerwaerwqreqwrtdbvbaerertqwerzv");
            Thread.sleep(100);
        }
    }


}
