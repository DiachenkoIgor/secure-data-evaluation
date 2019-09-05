package protocol.oblivious.ecc;

import protocol.Util.Util;
import protocol.oblivious.ObliviousReceiver;
import protocol.Util.CryptoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.spec.ECPoint;

public class EccObliviousReceiver extends ECOperations implements ObliviousReceiver {
    private InputStream inputStream;
    private OutputStream outputStream;
    private ECPoint R;
    private BigInteger nB;
    private ECPoint Pchoice;
    private int choice;

    public EccObliviousReceiver(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    public void setChoice(int choice){
        super.init();
        this.choice=choice;
        this.nB= generateSecret();
    }
    private BigInteger generateSecret(){
        byte[] tmp=new byte[3];
        CryptoUtil.generateRandomBytes(tmp);
        byte[] res=new byte[tmp.length+1];
        res[0]=126;
        System.arraycopy(tmp,0,res,1,tmp.length);
        return new BigInteger(res);
    }
    private void generateR(){
        this.R=scalmult(this.G,generateSecret());
        //    this.R=scalmult(this.G,new BigInteger("1234"));
    }

    @Override
    public byte[] receive() throws IOException {
        //1
        EccStageOneMessage stageOneMessage = Util.objectMapper.readValue(Util.receiveMessage(inputStream), EccStageOneMessage.class);
        fillEccParameters(stageOneMessage);
        generateR();
        //2
        ECPoint BSecret=scalmult(this.Pchoice,this.nB);
        ECPoint Mix0=addPoint(scalmult(stageOneMessage.getM1(),this.nB),this.R);
        ECPoint Mix1=addPoint(scalmult(stageOneMessage.getM2(),this.nB),this.R);
        ECPoint Two3=scalmult(this.R,this.nB);
        EccStageTwoMessage eccStageTwoMessage=
                new EccStageTwoMessage(BSecret,Mix0,Mix1,Two3);

        String stageTwoMessageString = Util.objectMapper.writeValueAsString(eccStageTwoMessage);
        Util.sendMessage(stageTwoMessageString,outputStream);
        //4-5
        EccStageFourMessage stageFourMessage = Util.objectMapper.readValue(Util.receiveMessage(inputStream), EccStageFourMessage.class);
        if(this.choice==0){
            ECPoint fiveA=substructPoint(stageFourMessage.getFour0(),
                    scalmult(stageOneMessage.getM1(),this.nB));
            ECPoint fiveB=substructPoint(stageFourMessage.getFour1(),scalmult(fiveA,this.nB));
            return decodeMessage(fiveB);
        }
        else {
            ECPoint fiveA=substructPoint(stageFourMessage.getFour2(),
                    scalmult(stageOneMessage.getM2(),this.nB));
            ECPoint fiveB=substructPoint(stageFourMessage.getFour3(),scalmult(fiveA,this.nB));
            return decodeMessage(fiveB);

        }

    }
    public void close(){
        super.close();
    }
    private void fillEccParameters(EccStageOneMessage stageOneMessage){
        this.N=stageOneMessage.getN();
        this.A=stageOneMessage.getA();
        this.B=stageOneMessage.getB();
        this.G=stageOneMessage.getG();
        this.Pchoice = this.choice==0 ? stageOneMessage.getP():reversePoint(stageOneMessage.getP());
    }
}
