package protocol.oblivious.iknp;

import com.fasterxml.jackson.core.JsonProcessingException;
import protocol.Util;
import protocol.oblivious.rsa.RsaObliviousReceiver;
import protocol.sfe.CryptoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
      //  k=(int) (m*0.1);
        k=70;
        l=length;
        s=CryptoUtil.randomBinaryVector(k);
        Q=new byte[m][k];
    }
    public void connect() throws IOException {
        StartUpMessage sum=new StartUpMessage(k,m);
        String sumString = Util.objectMapper.writeValueAsString(sum);
        Util.sendMessage(sumString,os);
        for(int i=0;i<k;i++){
            RsaObliviousReceiver ror=new RsaObliviousReceiver(is,os,s[i]);
            setColumn(ror.receive(),i);
        }
    }
    private void setColumn(byte[] data,int k){
        for(int i=0;i<m;i++){
            this.Q[i][k]=data[i];
        }
    }
    public void send(byte[] x1,byte[] x2) throws IOException {
        byte[] y0=CryptoUtil.xor(x1,CryptoUtil.Hash(this.Q[counter]));
        byte[] y1=CryptoUtil.xor(x2,CryptoUtil.Hash(CryptoUtil.xor(Q[counter],s)));
        TransferMessage transferMessage=new TransferMessage(y0,y1);
        String transferString = Util.objectMapper.writeValueAsString(transferMessage);
        Util.sendMessage(transferString,os);
        counter++;
    }


}
