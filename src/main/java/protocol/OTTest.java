package protocol;

import protocol.oblivious.ecc.EccObliviousReceiver;
import protocol.oblivious.ecc.EccObliviousSender;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class OTTest {
    public static void main(String[] args) throws IOException {
        PipedInputStream pis1 = new PipedInputStream();
        PipedInputStream pis2 = new PipedInputStream();
        PipedOutputStream pos1 = new PipedOutputStream();
        PipedOutputStream pos2 = new PipedOutputStream();
        pis1.connect(pos2);
        pis2.connect(pos1);
        EccObliviousSender eos=new EccObliviousSender(pis1,pos1);

        new Thread(() -> {
            try {
                long t=System.currentTimeMillis();
                eos.init("hello".getBytes("ASCII"),"hello2".getBytes("ASCII"));
                System.out.println(System.currentTimeMillis()-t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        EccObliviousReceiver eor=new EccObliviousReceiver(pis2,pos2);
        eor.setChoice(0);
        System.out.println(eor.receive());
        eor.close();

    }
}
