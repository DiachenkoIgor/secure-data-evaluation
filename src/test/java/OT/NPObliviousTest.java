package OT;

import org.junit.*;
import protocol.oblivious.fastGC.NPObliviousReceiver;
import protocol.oblivious.fastGC.NPObliviousSender;
import util.IOTests;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

public class NPObliviousTest extends IOTests {



    @Test
    public void testOT() throws IOException {
        BigInteger data1=new BigInteger(100,secureRandom);
        BigInteger data2=new BigInteger(100,secureRandom);

        new Thread(()->{
            try {
                NPObliviousSender sender=new NPObliviousSender(100,pis1,pos1);
                for(int i=0;i<1000;i++) {
                    sender.send(data1.toByteArray(), data2.toByteArray());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        NPObliviousReceiver nor=new NPObliviousReceiver(pis2,pos2);
        for(int i=0;i<1000;i++) {
            byte[] receive = nor.receive(0);
            Assert.assertEquals(data1, new BigInteger(receive));
        }
    }

}
