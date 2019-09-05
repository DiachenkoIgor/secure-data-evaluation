package protocol.oblivious.fastGC;

import java.math.BigInteger;

class NPUtil {
    static byte[] prepareByteArray(byte[] message){
        byte[] tmp=new byte[message.length+1];
        tmp[0]=126;
        System.arraycopy(message,0,tmp,1,message.length);
        return tmp;
    }
    static byte[] decodeByteArray(BigInteger decrypt){
        byte[] d=decrypt.toByteArray();
        byte[] tmp=new byte[d.length-1];
        System.arraycopy(d,1,tmp,0,tmp.length);
        return tmp;
    }
}
