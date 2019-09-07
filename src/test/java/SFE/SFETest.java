package SFE;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import protocol.Util.CryptoUtil;
import protocol.sfe.*;
import util.IOTests;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class SFETest extends IOTests {
    private Wire i1;
    private Wire i2;
    private XORGate g1;

    private SFEEvaluator sfeEvaluator;
    private SFEConstructor sfeConstructor;

    @Before
    public void prepareGate() throws IOException {
         i1=new Wire();
         i2=new Wire();
        Wire o1=new Wire();
        Wire o2=new Wire();
        Wire o3=new Wire();
        Wire o4=new Wire();
        g1=new XORGate(i1,o1);
        AndGate g2=new AndGate(i2,o2);
        AndGate g3=new AndGate(o1,o2,o3);
        AndGate g4=new AndGate(o3,o4);
        g1.setNext(g2);
        g2.setNext(g3);
        g3.setNext(g4);

        sfeConstructor = new SFEConstructor(pis1,pos1);
        sfeEvaluator=new SFEEvaluator(pis2,pos2);
    }

    @Test(timeout =1000)
    public void testGateTrue() throws InterruptedException {
        i1.setValue((byte)1);
        i2.setValue((byte)1);

        new Thread(()->{
            sfeEvaluator.calculate(new byte[]{0,1,1},false);

        }).start();
        boolean calculate = sfeConstructor.calculate(g1, false, CryptoUtil.AES_KEY_SIZE);
        Assert.assertTrue(calculate);
    }

    @Test(timeout =1000)
    public void testGateTrue2() throws InterruptedException {
        i1.setValue((byte)0);
        i2.setValue((byte)1);

        new Thread(()->{
            sfeEvaluator.calculate(new byte[]{1,1,1},false);

        }).start();
        boolean calculate = sfeConstructor.calculate(g1, false, CryptoUtil.AES_KEY_SIZE);
        Assert.assertTrue(calculate);
    }

    @Test(timeout =1000)
    public void testGateFalse() throws InterruptedException {
        i1.setValue((byte)0);
        i2.setValue((byte)1);

        new Thread(()->{
            sfeEvaluator.calculate(new byte[]{1,1,0},false);

        }).start();
        boolean calculate = sfeConstructor.calculate(g1, false, CryptoUtil.AES_KEY_SIZE);
        Assert.assertFalse(calculate);
    }

    @Test(timeout =1000)
    public void testGateFalse2() throws InterruptedException {
        i1.setValue((byte)1);
        i2.setValue((byte)1);

        new Thread(()->{
            sfeEvaluator.calculate(new byte[]{1,1,0},false);

        }).start();
        boolean calculate = sfeConstructor.calculate(g1, false, CryptoUtil.AES_KEY_SIZE);
        Assert.assertFalse(calculate);
    }
}
