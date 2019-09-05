package protocol.oblivious.iknp;

import protocol.Util.Util;
import protocol.oblivious.fastGC.Cipher;
import protocol.oblivious.fastGC.NPObliviousSender;
import protocol.Util.CryptoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class ExtendedObliviousReceiver {
    private InputStream is;
    private OutputStream os;
    private byte[] r;
    private byte[][] T;
    private int k;
    private int m;
    private int counter=0;
    private int messageLength;

    public ExtendedObliviousReceiver(InputStream is,OutputStream os,byte[] r,int messageLength) {
        this.messageLength=messageLength;
        this.is=is;
        this.os=os;
        this.r = r;
    }
    public void initialize() throws IOException {
        StartUpMessage stageOneMessage = Util.objectMapper.readValue(Util.receiveMessage(is), StartUpMessage.class);
        this.k=stageOneMessage.getK();
        this.m=stageOneMessage.getM();
        initializeMatrix();
        NPObliviousSender sender=new NPObliviousSender(m,is,os);
        for(int i=0;i<k;i++){
            byte[] column = getColumn(i);
            sender.send(ExtendedOTUtil.convertForOT(column,m),
                    ExtendedOTUtil.convertForOT(CryptoUtil.xor(r,column),m));
        }
    }

    public byte[] receive() throws IOException {
        TransferMessage transfer = Util.objectMapper.readValue(Util.receiveMessage(is), TransferMessage.class);
        byte[] bytes=null;
        if(r[counter]==0){
           BigInteger t = new BigInteger(
                   ExtendedOTUtil.convertForOT(this.T[counter++],k));
            bytes=Cipher.decrypt(
                   t,transfer.getY0(),this.messageLength).toByteArray();
        }else {
            BigInteger t = new BigInteger(
                    ExtendedOTUtil.convertForOT(this.T[counter++],k));
          bytes=Cipher.decrypt(
                   t,transfer.getY1(),this.messageLength).toByteArray();
        }

        byte[] tmp=new byte[bytes.length-1];
        System.arraycopy(bytes,1,tmp,0,tmp.length);
        bytes=tmp;
        return bytes;
    }
    private byte[] getColumn(int j){
        byte[] res=new byte[m];
        for(int i=0;i<m;i++){
            res[i]=this.T[i][j];
        }
        return res;
    }
    private void initializeMatrix(){
        this.T=new byte[m][];
        for(int i=0;i<m;i++){
            this.T[i]=CryptoUtil.randomBinaryVector(k);
        }
    }

}
