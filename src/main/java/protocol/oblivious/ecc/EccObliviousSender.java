package protocol.oblivious.ecc;

import protocol.Util.Util;
import protocol.oblivious.ObliviousSender;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.spec.ECPoint;

public class EccObliviousSender extends ECOperations implements ObliviousSender {
    private InputStream inputStream;
    private OutputStream outputStream;
    private BigInteger m0;
    private BigInteger m1;
    private ECPoint Pn0;
    private ECPoint Pn1;
    private ECPoint P0;
    private ECPoint P1;

    public EccObliviousSender(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    public void init(byte[] m0,byte[] m1){
        super.init();
        this.m0=new BigInteger(prepareByteArray(m0));
        this.m1=new BigInteger(prepareByteArray(m1));
        this.Pn0=encodeMessage(this.m0);
        this.Pn1=encodeMessage(this.m1);
        this.P0=this.P;
        this.P1=reversePoint(this.P);
    }
    private byte[] prepareByteArray(byte[] message){
        byte[] tmp=new byte[message.length+1];
        tmp[0]=126;
        System.arraycopy(message,0,tmp,1,message.length);
        return tmp;
    }
    public void send()  {
        try {
            ECPoint One0=scalmult(this.P0,m0);
            ECPoint One1=scalmult(this.P1,m1);
            EccStageOneMessage stageOneMessage=new EccStageOneMessage(N,A,B,G,P,
                    One0,One1);
            //1
            String stageOneMessageString = Util.objectMapper.writeValueAsString(stageOneMessage);
            Util.sendMessage(stageOneMessageString,outputStream);

            //3
            EccStageTwoMessage eccStageTwoMessage = Util.objectMapper.readValue(Util.receiveMessage(inputStream), EccStageTwoMessage.class);
            ECPoint m0SessionKey=scalmult(eccStageTwoMessage.getTwo0(),m0);
            ECPoint m1SessionKey=scalmult(eccStageTwoMessage.getTwo0(),m1);
            ECPoint H1=scalmult(
                    substructPoint(eccStageTwoMessage.getTwo1(),m0SessionKey),m0);
            ECPoint H2=scalmult(
                    substructPoint(eccStageTwoMessage.getTwo2(),m1SessionKey),m1);

            //4
            ECPoint four0=addPoint(m0SessionKey,H1);
            ECPoint four1=addPoint(scalmult(eccStageTwoMessage.getTwo3(),m0),Pn0);
            ECPoint four3=addPoint(m1SessionKey,H2);
            ECPoint four4=addPoint(scalmult(eccStageTwoMessage.getTwo3(),m1),Pn1);

            EccStageFourMessage stageFourMessage=new EccStageFourMessage(four0,four1,four3,four4);
            String stageFourMessageString = Util.objectMapper.writeValueAsString(stageFourMessage);
            Util.sendMessage(stageFourMessageString,outputStream);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void close(){
        super.close();
    }
}
