package protocol.Util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class Util {

    public static final ObjectMapper objectMapper=new ObjectMapper();
    static {
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
    }

    public static byte[] byteToBitArray(byte value){
        String binaryString = Integer.toBinaryString(value);
        byte[] pos_value_bits=new byte[8];
        for(int i=0;i<binaryString.length();i++){
            pos_value_bits[i]=(byte)Character.getNumericValue(binaryString.charAt(binaryString.length()-1-i));
        }
        return pos_value_bits;
    }

    public static  String receiveMessage(InputStream inputStream) throws IOException {

        int q=0;
        int t;
        boolean stop=true;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        while (stop){
            t=inputStream.read();
            if(t=='{'){
                q++;
            }
            if(t=='}'){
                q--;
                if(q==0) stop=false;
            }
            baos.write(t);
        }
        String result = new String(baos.toByteArray(), "UTF-8");
        //System.out.println("Read - "+result);
        return result;
    }
    public static void sendMessage(String m, OutputStream os) throws IOException {
  //     System.out.println("Write - "+m);
        os.write(m.getBytes("UTF-8"));
        os.flush();
    }
    public static <T> void shuffleArray(T[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Object a = ar[index];
            ar[index] = ar[i];
            ar[i] = (T) a;
        }
    }
    public static  byte[] concatByteArrays(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
