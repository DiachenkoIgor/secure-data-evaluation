package protocol.oblivious.ecc;

import protocol.WordTestWithoutInit;
import protocol.math.GMP;
import protocol.oblivious.CipollasAlgorithm;
import protocol.sfe.CryptoUtil;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

import java.math.BigInteger;
import java.security.interfaces.ECKey;
import java.security.spec.ECPoint;

public abstract class ECOperations {
    protected BigInteger N;
    protected BigInteger A;
    protected BigInteger B;
    protected ECPoint G;
    protected ECPoint P;

    //GMP
    private GMP slope=new GMP();
    private GMP TWO=new GMP("2");
    private GMP THREE=new GMP("3");
    private GMP tmp=new GMP();
    private GMP tmp2=new GMP();
    private GMP tmp3=new GMP();
    private GMP tmp4=new GMP();
    private GMP GA=new GMP();
    private GMP GN=new GMP();
    private GMP Gx=new GMP();
    private GMP Gx1=new GMP();
    private GMP Gy=new GMP();
    private GMP Gy1=new GMP();

    public ECOperations(BigInteger n, BigInteger a, BigInteger b, ECPoint g) {
        N = n;
        A = a;
        B = b;
        G = g;
    }
    public ECOperations(){

      }
      protected void init(){
          ECKey[] eccKeys = CryptoUtil.generateEccKeys();
          ECPublicKeyImpl publicKey=(ECPublicKeyImpl) eccKeys[0];
          ECPrivateKeyImpl privateKey=(ECPrivateKeyImpl) eccKeys[1];
          this.N=publicKey.getParams().getOrder();
          this.A=publicKey.getParams().getCurve().getA();
          this.B=publicKey.getParams().getCurve().getB();
          this.G=publicKey.getParams().getGenerator();
          this.P=publicKey.getW();
          GA.fromByteArray(this.A.toByteArray());
          GN.fromByteArray(this.N.toByteArray());
      }

    protected  ECPoint reversePoint(ECPoint point){
        return new ECPoint(point.getAffineX(),point.getAffineY().negate().mod(N));
    }
    protected ECPoint encodeMessage(BigInteger t){
        byte[] m=t.toByteArray();
        byte[] tmp = new byte[m.length + 1];
        System.arraycopy(m,0,tmp,0,m.length);
        int quantityofIterations=(int)Math.pow(2,8)-1;
        BigInteger x=new BigInteger(tmp);
        BigInteger N=calculateN(x);
        BigInteger y=BigInteger.ZERO;
        while (y.equals(BigInteger.ZERO)){
            y=CipollasAlgorithm.c(N,this.N);
            x=x.add(BigInteger.ONE);
            quantityofIterations--;
            if(quantityofIterations==0){
                throw new RuntimeException("Can't find Y!!");
            }
            N=calculateN(x);
        }

        return new ECPoint(x.subtract(BigInteger.ONE),y);

    }
    public static byte[] decodeMessage(ECPoint p){
        byte[] bytes = p.getAffineX().toByteArray();
        byte[] res=new byte[bytes.length-2];
        System.arraycopy(bytes,1,res,0,res.length);
        return  res;
    }
    private  BigInteger calculateN(BigInteger X){
        return   X.pow(3).add(X.add(A)).add(B).mod(N);
    }
    protected ECPoint scalmult(ECPoint P, BigInteger kin){

        BigInteger TWO = new BigInteger("2");
        ECPoint R = ECPoint.POINT_INFINITY,S = P;
        BigInteger k = kin;
        int length = k.bitLength();
        byte[] binarray = new byte[length];
        for(int i=0;i<=length-1;i++){
            binarray[i] = k.mod(TWO).byteValue();
            k = k.divide(TWO);
        }
        for(int i = length-1;i >= 0;i--){
            R = doublePoint(R);
            if(binarray[i]== 1)
                R = addPoint(R, S);
        }
        return R;
    }
    protected ECPoint substructPoint(ECPoint r,ECPoint s){
        return   addPoint(r,new ECPoint(s.getAffineX(),s.getAffineY().negate().mod(this.N)));
    }
    protected   ECPoint addPoint(ECPoint r, ECPoint s) {
        if (r.equals(s))
            return doublePoint(r);
        else if (r.equals(ECPoint.POINT_INFINITY))
            return s;
        else if (s.equals(ECPoint.POINT_INFINITY))
            return r;
        return addPointWithGmp(r,s);
    }
    private ECPoint addPointWithGmp(ECPoint r,ECPoint s){
        Gx.fromByteArray(r.getAffineX().toByteArray());
        Gy.fromByteArray(r.getAffineY().toByteArray());
        Gx1.fromByteArray(s.getAffineX().toByteArray());
        Gy1.fromByteArray(s.getAffineY().toByteArray());
        Gy.subtract(Gy1,tmp);  Gx.subtract(Gx1,tmp2); tmp2.modInverse(GN,tmp2); tmp.multiply(tmp2,tmp); tmp.mod(GN,tmp);
        tmp.modPow(TWO,GN,tmp3); tmp3.subtract(Gx,tmp3); tmp3.subtract(Gx1,tmp3); tmp3.mod(GN,tmp3);
        BigInteger Xout=new BigInteger(tmp3.toString());
        Gy1.negate(tmp2); tmp2.mod(GN,tmp2); Gx1.subtract(tmp3,tmp4); tmp.multiply(tmp4,tmp4); tmp2.add(tmp4,tmp2); tmp2.mod(GN,tmp2);
        BigInteger Yout=new BigInteger(tmp2.toString());
        return new ECPoint(Xout,Yout);

    }
    protected   ECPoint doublePoint(ECPoint r) {
        if (r.equals(ECPoint.POINT_INFINITY))
            return r;
        return doublePointWithGMP(r);
    }

    private ECPoint doublePointWithGMP(ECPoint r){

        Gx.fromByteArray(r.getAffineX().toByteArray());
        Gy.fromByteArray(r.getAffineY().toByteArray());
        Gx.pow(2,slope);
        slope.multiply(THREE,slope);
        slope.add(GA,slope);
        slope.mod(GN,slope);
        Gy.multiply(TWO,tmp);   tmp.modInverse(GN,tmp); slope.multiply(tmp,slope);
        slope.pow(2,tmp2); Gx.multiply(TWO,tmp); tmp2.subtract(tmp,tmp); tmp.mod(GN,tmp);
        BigInteger Xout=new BigInteger(tmp.toString());
        Gy.negate(tmp2); Gx.subtract(tmp,tmp); slope.multiply(tmp,tmp); tmp2.add(tmp,tmp2); tmp2.mod(GN,tmp2);
        BigInteger Yout=new BigInteger(tmp2.toString());

        return new ECPoint(Xout, Yout);
    }
    public static CustomECPoint convertECPointToCustom(ECPoint point){
        return new CustomECPoint(point.getAffineX(),point.getAffineY());
    }

    public static ECPoint convertCustomECPoint(CustomECPoint point){
        if(point.getX() == null){
            return ECPoint.POINT_INFINITY;
        }
        return new ECPoint(point.getX(),point.getY());
    }
    public static void printPoint(ECPoint point){
        System.out.println(point.getAffineX()+"      "+point.getAffineY());
    }

}
