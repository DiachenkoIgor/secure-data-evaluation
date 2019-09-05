package protocol;

import protocol.words_eval.WordComparisonConstructor;
import protocol.words_eval.WordComparisonEvaluator;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class WordTestWithoutInit {
    public static void main(String[] args) throws IOException, InterruptedException {
        PipedInputStream pis1 = new PipedInputStream();
        PipedInputStream pis2 = new PipedInputStream();
        PipedOutputStream pos1 = new PipedOutputStream();
        PipedOutputStream pos2 = new PipedOutputStream();
        pis1.connect(pos2);
        pis2.connect(pos1);
        WordComparisonConstructor wcc = new WordComparisonConstructor(pis1, pos1, "C:\\Users\\igor\\Downloads\\en-pos-maxent.bin");
        WordComparisonEvaluator wce = new WordComparisonEvaluator(pis2, pos2, "C:\\Users\\igor\\Downloads\\en-pos-maxent.bin");

        for (int i = 0; i < 100; i++) {

            new Thread(() -> {
                try {
                  //  System.out.println(wcc.compareWord("Honorificabilitudinitatib"));
                    System.out.println(wcc.compareWord("Honorificabilitudinitatib"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
            Thread.sleep(100);
           wce.compareWord("Honorificabilitudinitatib");
            Thread.sleep(100);
        }
    }

}
