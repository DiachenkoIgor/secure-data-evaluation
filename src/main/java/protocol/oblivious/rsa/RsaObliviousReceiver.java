package protocol.oblivious.rsa;

import protocol.Util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Random;

/**
 * Created by IgorPc on 7/9/2019.
 */
public class RsaObliviousReceiver /*implements ObliviousReceiver*/ {
    private BigInteger k;
    private BigInteger v;
    private int b;
    private InputStream inputStream;
    private OutputStream outputStream;

    public RsaObliviousReceiver(InputStream inputStream, OutputStream outputStream, int b) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.b=b;
        generateK();
    }

    public byte[] receive() throws IOException {
        StartMessage startMessage = Util.objectMapper.readValue(Util.receiveMessage(inputStream), StartMessage.class);

        BigInteger v = this.k.pow(startMessage.getPublicExponent().intValue())
                .add(b==0?startMessage.getX0():startMessage.getX1()).mod(startMessage.getModulus());
        Util.sendMessage(Util.objectMapper.writeValueAsString(new MiddleMessage(v)),outputStream);
        EndMessage endMessage = Util.objectMapper.readValue(Util.receiveMessage(inputStream), EndMessage.class);

        if(b==0){
            byte[] bytes = endMessage.getM0().subtract(this.k).toByteArray();
            byte[] res=new byte[bytes.length-1];
            System.arraycopy(bytes,1,res,0,res.length);
            return res;
        }else {
            byte[] bytes = endMessage.getM1().subtract(this.k).toByteArray();
            byte[] res=new byte[bytes.length-1];
            System.arraycopy(bytes,1,res,0,res.length);
            return res;
        }
    }

    private void generateK(){
        this.k=BigInteger.valueOf(new Random().nextInt(1024)+100);
    }

}
