package protocol.sfe;

import protocol.domain.exceptions.YaoAESCryptographyException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;

public class CryptoUtil {
    private static final int AES_KEY_SIZE = 128;




    private static SecureRandom random;
    private static KeyGenerator kgen;
    private static KeyPairGenerator keyPairGeneratorRsa;
    private static KeyPairGenerator keyPairGeneratorEcc;
    private static Cipher cipher;

    static {
        random = new SecureRandom();
        try {
            kgen = KeyGenerator.getInstance("AES");
            kgen.init(AES_KEY_SIZE);
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            keyPairGeneratorRsa = KeyPairGenerator.getInstance("RSA");
            keyPairGeneratorRsa.initialize(1024);

            keyPairGeneratorEcc = KeyPairGenerator.getInstance("EC","SunEC");
            ECGenParameterSpec ecsp=new ECGenParameterSpec("secp192k1");
            keyPairGeneratorEcc.initialize(ecsp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }


    public static byte[] AESdecrypt(byte[] encrypted,byte[] key)
    {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return cipher.doFinal(encrypted);
        }catch (BadPaddingException bpe){
            throw new YaoAESCryptographyException("Wrong AES decrypt");
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new YaoAESCryptographyException("Wrong AES decrypt");
        }
    }

    public static byte[] AESencrypt(byte[] plain,byte[] key)
    {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(plain);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new YaoAESCryptographyException("Wrong AES encrypt");
        }
    }

    public static RSAKey[] generateRsaKeys(){
        // Generate the KeyPair
        KeyPair keyPair = keyPairGeneratorRsa.generateKeyPair();

        // Get the public and private key
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        return new RSAKey[]{publicKey,privateKey};
    }

    public static ECKey[] generateEccKeys(){
        // Generate the KeyPair
        KeyPair keyPair = keyPairGeneratorEcc.generateKeyPair();
        return new ECKey[]{(ECKey) keyPair.getPublic(),(ECKey)keyPair.getPrivate()};
       }

    public static byte[] generateHash(byte[] data,byte[] key){
        try {
            Mac mac=Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec=new SecretKeySpec(key,"RawBytes");
            mac.init(secretKeySpec);

            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException e) {

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("HMAC");
    }


    public static byte[] generateEncodedAESKey(){
        return kgen.generateKey().getEncoded();
    }
    public static void generateRandomBytes(byte[] dst){
        random.nextBytes(dst);
    }
}
