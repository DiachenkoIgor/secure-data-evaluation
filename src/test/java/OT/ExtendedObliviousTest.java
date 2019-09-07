package OT;

import org.junit.Assert;
import org.junit.Test;
import protocol.oblivious.fastGC.Cipher;
import protocol.oblivious.iknp.ExtendedObliviousReceiver;
import protocol.oblivious.iknp.ExtendedObliviousSender;
import util.IOTests;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class ExtendedObliviousTest extends IOTests {

    @Test
    public void test() throws IOException, InterruptedException {
        BigInteger data1=new BigInteger(100,secureRandom);
        BigInteger data2=new BigInteger(100,secureRandom);
        new Thread(()->{
            try {
                ExtendedObliviousSender eos=new ExtendedObliviousSender(pis1,pos1,100,100);
                eos.connect();
                for(int i=0;i<100;i++) {
                    eos.send(data1.toByteArray(), data2.toByteArray());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(200);
        byte[] r=new byte[100];
        for(int i=0;i<100;i++){
            r[i]=(byte)(i%2);
        }

        ExtendedObliviousReceiver receiver=new ExtendedObliviousReceiver(pis2,pos2,r,100);
        receiver.initialize();
        for(int i=0;i<100;i++){
            byte[] receive = receiver.receive();
            if(i%2==0){
                Assert.assertEquals(data1,new BigInteger(receive));
            }else {
                Assert.assertEquals(data2,new BigInteger(receive));
            }
        }
    }
}
