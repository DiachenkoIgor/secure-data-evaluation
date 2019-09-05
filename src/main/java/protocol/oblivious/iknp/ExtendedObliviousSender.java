package protocol.oblivious.iknp;

import protocol.Util.Util;
import protocol.oblivious.fastGC.Cipher;
import protocol.oblivious.fastGC.NPObliviousReceiver;
import protocol.Util.CryptoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class ExtendedObliviousSender {
    private InputStream is;
    private OutputStream os;
    private byte[] s;
    private byte[][] Q;
    private int k;
    private int counter=0;
    private int m;
    private int l;


    public ExtendedObliviousSender(InputStream is,OutputStream os,int quantityOfpairs,int length){
        this.is=is;
        this.os=os;
        m=quantityOfpairs;
        k=70;
        l=length;
        s=CryptoUtil.randomBinaryVector(k);
        Q=new byte[m][k];
    }

    public void connect() throws IOException {
        StartUpMessage sum=new StartUpMessage(k,m);
        String sumString = Util.objectMapper.writeValueAsString(sum);
        Util.sendMessage(sumString,os);
        NPObliviousReceiver NPObliviousReceiver =new NPObliviousReceiver(is,os);
        for(int i=0;i<k;i++){
            byte[] receive = NPObliviousReceiver.receive(s[i]);
           setColumn(
                   ExtendedOTUtil.convertFromOT(receive,m),
                   i);
        }
    }

    private void setColumn(byte[] data,int k){
        for(int i=0;i<m;i++){
            this.Q[i][k]=data[i];
        }
    }
    public void send(byte[] x1,byte[] x2) throws IOException {

        BigInteger msg1=new BigInteger(
                ExtendedOTUtil.prepareByteArray(x1));
        BigInteger msg2=new BigInteger(ExtendedOTUtil.prepareByteArray(x2));

       BigInteger y0Q = new BigInteger(
               ExtendedOTUtil.convertForOT(this.Q[counter],k));
        BigInteger y1Q = new BigInteger(
                ExtendedOTUtil.convertForOT(CryptoUtil.xor(Q[counter],s),k));
        BigInteger y0=Cipher.encrypt(y0Q,msg1,l);
        BigInteger y1=Cipher.encrypt(y1Q,msg2,l);

       TransferMessage transferMessage=new TransferMessage(y0,y1);
        String transferString = Util.objectMapper.writeValueAsString(transferMessage);
        Util.sendMessage(transferString,os);
        counter++;
    }

    public static byte[] decodeMessage(BigInteger data){
        byte[] bytes = data.toByteArray();
        byte[] res=new byte[bytes.length-1];
        System.arraycopy(bytes,1,res,0,res.length);
        return  res;
    }

}
