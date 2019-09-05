package protocol.oblivious.iknp;

import protocol.Util;
import protocol.oblivious.ObliviousSender;
import protocol.oblivious.rsa.RsaObliviousSender;
import protocol.sfe.CryptoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class ExtendedObliviousReceiver {
    private InputStream is;
    private OutputStream os;
    private byte[] r;
    private byte[][] T;
    private int k;
    private int m;
    private int counter=0;

    public ExtendedObliviousReceiver(InputStream is,OutputStream os,byte[] r) {
        this.is=is;
        this.os=os;
        this.r = r;
    }
    public void initialize() throws IOException {
        StartUpMessage stageOneMessage = Util.objectMapper.readValue(Util.receiveMessage(is), StartUpMessage.class);
        this.k=stageOneMessage.getK();
        this.m=stageOneMessage.getM();
        initializeMatrix();
        for(int i=0;i<k;i++){
            byte[] column = getColumn(i);
            RsaObliviousSender obliviousSender=
                    new RsaObliviousSender(is,os, column,CryptoUtil.xor(r,column));
            obliviousSender.send();

        }
    }
    public byte[] receive() throws IOException {
        TransferMessage transfer = Util.objectMapper.readValue(Util.receiveMessage(is), TransferMessage.class);
        byte[] bytes=null;
        if(r[counter]==0){
            bytes = CryptoUtil.cleanRes(CryptoUtil.xor(transfer.y0, CryptoUtil.Hash(T[counter++])));
        }else {
            bytes = CryptoUtil.cleanRes(CryptoUtil.xor(transfer.y1, CryptoUtil.Hash(T[counter++])));
        }
        if(bytes.length==15){
            byte[] arr=new byte[16];
            System.arraycopy(bytes,0,arr,1,bytes.length);
            bytes=arr;
        }
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
