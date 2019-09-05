package protocol.oblivious.rsa;

import protocol.Util.CryptoUtil;
import protocol.Util.Util;

import java.io.*;
import java.math.BigInteger;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by IgorPc on 7/9/2019.
 */
public class RsaObliviousSender /*implements ObliviousSender*/ {
    private InputStream inputStream;
    private OutputStream outputStream;
    private BigInteger[] messages;

    public RsaObliviousSender(InputStream inputStream, OutputStream outputStream, byte[]... messages) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.messages=new BigInteger[messages.length];
        for(int i=0;i<messages.length;i++){
            byte[] a = messages[i];
            byte[] tmp=new byte[a.length+1];
            tmp[0]=126;
            System.arraycopy(a,0,tmp,1,a.length);
            this.messages[i]=new BigInteger(tmp);
        }
    }

    public void send()  {
        try {

            RSAKey[] rsaKeys = CryptoUtil.generateRsaKeys();
            RSAPublicKey publicKey = (RSAPublicKey) rsaKeys[0];
            RSAPrivateKey privateKey = (RSAPrivateKey) rsaKeys[1];

            StartMessage startMessage = generateStartMessage(publicKey);

            String stringStartMessage = Util.objectMapper.writeValueAsString(startMessage);

            Util.sendMessage(stringStartMessage,outputStream);

            MiddleMessage middleMessage = Util.objectMapper.readValue(Util.receiveMessage(inputStream), MiddleMessage.class);


            EndMessage endMessage = generateEndMessage(privateKey, middleMessage.getV(), startMessage, publicKey);

            String stringEndMessage = Util.objectMapper.writeValueAsString(endMessage);

            Util.sendMessage(stringEndMessage,outputStream);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private EndMessage generateEndMessage(RSAPrivateKey rsaPrivateKey,BigInteger v,StartMessage startMessage,RSAPublicKey rsaPublicKey){
        BigInteger k0=v.subtract(startMessage.getX0()).modPow(rsaPrivateKey.getPrivateExponent(),rsaPublicKey.getModulus());
        BigInteger k1=v.subtract(startMessage.getX1()).modPow(rsaPrivateKey.getPrivateExponent(),rsaPublicKey.getModulus());

        BigInteger m0=messages[0].add(k0);
        BigInteger m1=messages[1].add(k1);

        return new EndMessage(m0,m1);
    }

    private StartMessage generateStartMessage(RSAPublicKey rsaPublicKey){
        BigInteger x0=BigInteger.valueOf(Double.doubleToLongBits(Math.random()));
        BigInteger x1=BigInteger.valueOf(Double.doubleToLongBits(Math.random()));
        return new StartMessage(rsaPublicKey.getPublicExponent(),rsaPublicKey.getModulus(),x0,x1);
    }

}
