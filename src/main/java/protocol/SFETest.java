package protocol;

import protocol.Util.CryptoUtil;
import protocol.sfe.*;

import javax.crypto.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.security.*;

public class SFETest {
    public static void main(String[] args) throws IOException, InterruptedException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        Wire i1=new Wire();
        Wire i2=new Wire();
        Wire o1=new Wire();
        Wire o2=new Wire();
        Wire o3=new Wire();
        Wire o4=new Wire();
        XORGate g1=new XORGate(i1,o1);
        AndGate g2=new AndGate(i2,o2);
        AndGate g3=new AndGate(o1,o2,o3);
        AndGate g4=new AndGate(o3,o4);
        g1.setNext(g2);
        g2.setNext(g3);
        g3.setNext(g4);
        i1.setValue((byte)1);
        i2.setValue((byte)1);

        PipedInputStream pis1=new PipedInputStream();
        PipedInputStream pis2=new PipedInputStream();
        PipedOutputStream pos1=new PipedOutputStream();
        PipedOutputStream pos2=new PipedOutputStream();
        pis1.connect(pos2);
        pis2.connect(pos1);
/*
        int port=5678;
        ServerSocket ss=new ServerSocket(port);
        new Thread(()->{
            try {
                Socket accept = ss.accept();
                SFEConstructor sfeConstructor = new SFEConstructor(g1, accept.getInputStream(), accept.getOutputStream());
                System.out.println(sfeConstructor.calculate());
                accept.close();
                ss.close();
            }catch (Exception ex){
                ex.printStackTrace();
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(400);
        Socket s=new Socket("127.0.0.1",port);
        SFEEvaluator sfeEvaluator=new SFEEvaluator(s.getInputStream(),s.getOutputStream(),new byte[]{0,0});
        sfeEvaluator.calculate();*/
    new Thread(()->{
        SFEConstructor sfeConstructor = new SFEConstructor( pis1,pos1);
        long t=System.currentTimeMillis();
            System.out.println(sfeConstructor.calculate(g1,false,CryptoUtil.AES_KEY_SIZE));
        System.out.println(System.currentTimeMillis()-t);

    }).start();
        Thread.sleep(100);
        SFEEvaluator sfeEvaluator=new SFEEvaluator(pis2,pos2);
        sfeEvaluator.calculate(new byte[]{0,1,1},false);

    }
}
