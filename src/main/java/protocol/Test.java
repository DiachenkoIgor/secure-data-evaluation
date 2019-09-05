package protocol;

import com.squareup.jnagmp.Gmp;
import protocol.math.GMP;


import java.io.IOException;
import java.math.BigInteger;
import java.security.*;

public class Test {
    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IOException, InterruptedException {


  }

    public static byte[] Hash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(data);
        return md.digest(data);
    }
    private static void modCheck(){

        GMP a=new GMP("68903435569786809661289367123124590064452248234471643465338887533472407110509904542414159541439707520900908724507");
        GMP b=new GMP("6277101735386680763835789423061264271957123915200845512077");
        GMP res=new GMP();
        long startTime=0;
        for (int i=0;i<20000;i++)
        {
            if(i==1)
            {
                startTime=System.currentTimeMillis();
            }
            a.mod(b,res);
        }
        System.out.println("My native " + ((System.currentTimeMillis()-startTime))+ "  milliseconds");
        System.out.println(res);
        BigInteger a2=new BigInteger("68903435569786809661289367123124590064452248234471643465338887533472407110509904542414159541439707520900908724507");
        BigInteger b2=new BigInteger("6277101735386680763835789423061264271957123915200845512077");
        BigInteger res2=null;
        startTime=0;
        for (int i=0;i<20000;i++)
        {
            if(i==1)
            {
                startTime=System.currentTimeMillis();
            }

            res2=a2.mod(b2);
        }
        System.out.println("Java " + ((System.currentTimeMillis()-startTime))+ "  milliseconds");
        System.out.println(res2);
    }
    private static void addCheck(){

        GMP a=new GMP("3713154180540231711282286160048719788565249557092190048136");
        GMP b=new GMP("6277101735386680763835789423061264271957123915200845512077");
        GMP res=new GMP();
        long startTime=0;
        for (int i=0;i<20000;i++)
        {
            if(i==1)
            {
                startTime=System.currentTimeMillis();
            }
            a.add(b,res);
        }
        System.out.println("My native " + ((System.currentTimeMillis()-startTime))+ "  milliseconds");
        System.out.println(res);

        BigInteger a2=new BigInteger("3713154180540231711282286160048719788565249557092190048136");
        BigInteger b2=new BigInteger("6277101735386680763835789423061264271957123915200845512077");
        BigInteger res2=null;
        startTime=0;
        for (int i=0;i<20000;i++)
        {
            if(i==1)
            {
                startTime=System.currentTimeMillis();
            }

            res2=a2.add(b2);
        }
        System.out.println("Java " + ((System.currentTimeMillis()-startTime))+ "  milliseconds");
        System.out.println(res2);

    }
    private static void modInverseCheck(){
        GMP a=new GMP("3713154180540231711282286160048719788565249557092190048136");
        GMP b=new GMP("6277101735386680763835789423061264271957123915200845512077");
        GMP res=new GMP();
        long startTime=0;
        for (int i=0;i<20000;i++)
        {
            if(i==1)
            {
                startTime=System.currentTimeMillis();
            }
            a.modInverse(b,res);
        }
        System.out.println("My native " + ((System.currentTimeMillis()-startTime))+ "  milliseconds");
        System.out.println(res);
        BigInteger a1=new BigInteger("3713154180540231711282286160048719788565249557092190048136");
        BigInteger b1=new BigInteger("6277101735386680763835789423061264271957123915200845512077");
        BigInteger res1=null;
        startTime=0;
        for (int i=0;i<20000;i++)
        {
            if(i==1)
            {
                startTime=System.currentTimeMillis();
            }

            res1=Gmp.modInverse(a1,b1);
        }
        System.out.println("JNA " + ((System.currentTimeMillis()-startTime))+ "  milliseconds");
        System.out.println(res1);
        BigInteger a2=new BigInteger("3713154180540231711282286160048719788565249557092190048136");
        BigInteger b2=new BigInteger("6277101735386680763835789423061264271957123915200845512077");
        BigInteger res2=null;
        startTime=0;
        for (int i=0;i<20000;i++)
        {
            if(i==1)
            {
                startTime=System.currentTimeMillis();
            }

            res2=a2.modInverse(b2);
        }
        System.out.println("Java " + ((System.currentTimeMillis()-startTime))+ "  milliseconds");
        System.out.println(res2);
    }
}
