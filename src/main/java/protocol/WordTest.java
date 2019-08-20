package protocol;

import protocol.words_eval.WordComparisonConstructor;
import protocol.words_eval.WordComparisonEvaluator;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class WordTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            PipedInputStream pis1 = new PipedInputStream();
            PipedInputStream pis2 = new PipedInputStream();
            PipedOutputStream pos1 = new PipedOutputStream();
            PipedOutputStream pos2 = new PipedOutputStream();
            pis1.connect(pos2);
            pis2.connect(pos1);

            new Thread(() -> {
                WordComparisonConstructor wcc = new WordComparisonConstructor(pis1, pos1, "C:\\Users\\igor\\Downloads\\en-pos-maxent.bin");

                try {
                    System.out.println(wcc.compareWord("tables"));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
            Thread.sleep(100);
            WordComparisonEvaluator wce = new WordComparisonEvaluator(pis2, pos2, "C:\\Users\\igor\\Downloads\\en-pos-maxent.bin");
            wce.compareWord("tables");
            Thread.sleep(200);
        }
    }
}
