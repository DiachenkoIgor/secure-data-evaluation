package protocol.sfe;

import protocol.Util.CryptoUtil;
import protocol.Util.Util;
import protocol.domain.ContinueMessage;
import protocol.domain.CryptoPairHolder;
import protocol.domain.GarbledTableMessage;
import protocol.domain.GateResult;
import protocol.domain.exceptions.SFEIOException;
import protocol.domain.exceptions.YaoAESCryptographyException;
import protocol.oblivious.fastGC.NPObliviousReceiver;
import protocol.oblivious.iknp.ExtendedObliviousReceiver;

import java.io.*;
import java.util.Arrays;

/**
 * Created by IgorPc on 7/9/2019.
 */
public class SFEEvaluator {
    private InputStream is;
    private OutputStream os;
    private NPObliviousReceiver NPObliviousReceiver;
    private ExtendedObliviousReceiver eor;

    public SFEEvaluator(InputStream is, OutputStream os) {
            this.os = os;
            this.is = is;


    }
    public void prepareForExtendedTransfer(byte[] choice,int messageLength) throws IOException {
        this.eor=new ExtendedObliviousReceiver(is,os,choice,messageLength);
        this.eor.initialize();
    }
    public boolean calculate(byte[] compareValue,boolean useExtended)  {
        boolean b=false;
        try {
            int pos = 0;
            boolean flag = true;
            while (flag) {

                GarbledTableMessage tableMessage = Util.objectMapper.readValue(Util.receiveMessage(is),GarbledTableMessage.class);
                CryptoPairHolder[] cryptoPairHolders = tableMessage.getCryptoPairHolders();
                createHalfEncryptTable(tableMessage.getLabel(), cryptoPairHolders);

                byte[] bKey=null;
                if(this.NPObliviousReceiver ==null && !useExtended){
                    this.NPObliviousReceiver =new NPObliviousReceiver(is,os);
                }
                if(useExtended) {
                    bKey=this.eor.receive();
                }else {
                    bKey = this.NPObliviousReceiver.receive(compareValue[pos++]);
                }
                byte[] result = createResultFromHalfEncrypted(cryptoPairHolders, bKey);

                Util.sendMessage(
                        Util.objectMapper.writeValueAsString(new GateResult(result, compareValue.length == pos)),
                        os);

                ContinueMessage continueMessage = Util.objectMapper.readValue(Util.receiveMessage(is),ContinueMessage.class);
                if(continueMessage.isContinued()){
                    flag=true;
                }else {
                    switch (continueMessage.getCode()){
                        case -1: throw new RuntimeException("Size of compare bits is different");
                        case 0: {
                            flag=false;
                            b=false;
                            break;
                        }
                        case 1: {
                            flag=false;
                            b=true;
                            break;
                        }
                    }
                    if(continueMessage.getCode()<0){
                        throw new RuntimeException("Size of compare bits is different");
                    }
                }
                flag = continueMessage.isContinued();
            }
        }catch (IOException ex){
            ex.printStackTrace();
            throw new SFEIOException("Something went wrong for SFEEvaluator!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private void createHalfEncryptTable(byte[] aKey,CryptoPairHolder[] cryptoPairHolders){

        boolean success=false;
        for (int i = 0; i < cryptoPairHolders.length; i++) {
            try {
                byte[] half_encrypted = CryptoUtil.AESdecrypt(cryptoPairHolders[i].getCipher(), aKey);
                byte[] compare_hash = CryptoUtil.generateHMAC(half_encrypted, aKey);
                if(!Arrays.equals(compare_hash,cryptoPairHolders[i].getHashA())){
                    cryptoPairHolders[i]=null;
                    continue;
                }
                cryptoPairHolders[i].setCipher(half_encrypted);
                success=true;
            } catch (YaoAESCryptographyException e) {
                cryptoPairHolders[i]=null;
            }
        }
        if (!success) {
            throw new YaoAESCryptographyException("Something went wrong for half decryption!!");
        }
    }

    private byte[] createResultFromHalfEncrypted(CryptoPairHolder[] pairs,byte[] bKey){
        byte[] result=null;
        for (int i = 0; i <pairs.length; i++) {
            if(pairs[i]==null){
                continue;
            }
            try {
                byte[] plain = CryptoUtil.AESdecrypt(pairs[i].getCipher(), bKey);

                byte[] compare_hash = CryptoUtil.generateHMAC(plain, bKey);
                if(!Arrays.equals(compare_hash,pairs[i].getHashB())) continue;
                result = CryptoUtil.AESdecrypt(pairs[i].getCipher(), bKey);
                break;
            } catch (YaoAESCryptographyException e) {
                continue;
            }
        }

        if(result==null){
            throw new YaoAESCryptographyException("Something went wrong for result decryption!!");
        }
        return result;
    }
}
