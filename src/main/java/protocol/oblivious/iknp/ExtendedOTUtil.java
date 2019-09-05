package protocol.oblivious.iknp;

import java.math.BigInteger;

 class ExtendedOTUtil {
     static byte[]  convertFromOT(byte[] data,int length){
        byte[] tmp= new byte[length];
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<data.length;i++){
            sb.append(
                    String.format("%8s", Integer.toBinaryString(data[i] & 0xFF)).replace(' ', '0')
            );
        }
        int diff=sb.length()-tmp.length;
        for(int i=sb.length()-1;i>=diff;i--){
            tmp[i-diff]=sb.charAt(i) == '0'? (byte) 0: (byte) 1;
        }
        return tmp;
    }
     static byte[] convertForOT(byte[] column,int length){
        StringBuilder val=new StringBuilder();
        for(int i=0;i<column.length;i++){
            val.append(column[i]);
        }
        byte[] tmp=new BigInteger(val.toString(),2).toByteArray();
        if(tmp.length*8<length){
            byte[] res=new byte[length/8+1];
            System.arraycopy(tmp,0,res,res.length-tmp.length,tmp.length);
            return res;
        }
        return tmp;
    }
     static byte[] prepareByteArray(byte[] message){
        byte[] tmp=new byte[message.length+1];
        tmp[0]=126;
        System.arraycopy(message,0,tmp,1,message.length);
        return tmp;
    }
}
