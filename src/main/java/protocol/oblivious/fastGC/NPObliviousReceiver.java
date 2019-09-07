package protocol.oblivious.fastGC;

import protocol.Util.Util;
import protocol.oblivious.fastGC.model.ChooserPublicKey;
import protocol.oblivious.fastGC.model.InitializeMessage;
import protocol.oblivious.fastGC.model.NPOTTransfer;

import java.math.*;

import java.io.*;
import java.security.SecureRandom;


public class NPObliviousReceiver  {
    private static SecureRandom rnd = new SecureRandom();

    private int msgBitLength;
    private BigInteger p, q, g, C;
    private BigInteger gr;
    private InputStream is;
    private OutputStream os;

    private BigInteger[] data = null;

    private BigInteger key;
    private int choice;

    public NPObliviousReceiver(
                        InputStream in, OutputStream out) throws IOException {
        this.is=in;
        this.os=out;

        initialize();
    }

    public byte[] receive(int choice) throws IOException {
        this.choice=choice;
        step1();
        return step2();
    }

    private void initialize() throws IOException {
        InitializeMessage im = Util.objectMapper.readValue(Util.receiveMessage(is),InitializeMessage.class);

        C  = im.getC();
        p  = im.getP();
        q  = im.getQ();
        g  = im.getG();
        gr = im.getGr();
        msgBitLength = im.getMsgBitLength();

    }

    private void step1() throws IOException {
        BigInteger k = (new BigInteger(q.bitLength(), rnd)).mod(q);
        BigInteger gk=g.modPow(k,p);
        BigInteger c_over_gk = C.multiply(gk.modInverse(p)).mod(p);
        key=gr.modPow(k,p);
        BigInteger pk=null;
        if(choice==1){
            pk=c_over_gk;
        }else {
            pk=gk;
        }
        Util.sendMessage(
                Util.objectMapper.writeValueAsString(
                        new ChooserPublicKey(pk)),
                os
        );

    }

    private byte[] step2() throws IOException {
        NPOTTransfer npotTransfer=Util.objectMapper.readValue(Util.receiveMessage(is),NPOTTransfer.class);

        BigInteger decrypt=null;

        if(choice==1){
            decrypt = Cipher.decrypt(npotTransfer.getR(),key,  npotTransfer.getM2(), msgBitLength);
        }else {
            decrypt=Cipher.decrypt(npotTransfer.getR(), key, npotTransfer.getM1(), msgBitLength);
        }
        return NPUtil.decodeByteArray(decrypt);
    }
}
