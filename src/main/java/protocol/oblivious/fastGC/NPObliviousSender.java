package protocol.oblivious.fastGC;

import protocol.Util.Util;
import protocol.oblivious.fastGC.model.ChooserPublicKey;
import protocol.oblivious.fastGC.model.InitializeMessage;
import protocol.oblivious.fastGC.model.NPOTTransfer;

import java.math.*;
import java.io.*;
import java.security.SecureRandom;


public class NPObliviousSender {

    private static SecureRandom rnd = new SecureRandom();
    private static final int certainty = 80;
    private final static int rLength = 40;

    private final static int qLength = 512; //512;
    private final static int pLength = 15360; //15360;

    private BigInteger p, q, g, C, r;
    private BigInteger Cr, gr;

    private int msgBitLength;


    private InputStream is;
    private OutputStream os;


    public NPObliviousSender(int msgBitLength, InputStream in,
                             OutputStream out) throws IOException {
        this.msgBitLength = msgBitLength;
        this.is = in;
        this.os = out;
        initialize();
    }

    private void initialize() throws IOException {
        //File keyfile = new File("NPOTKey");
        File keyfile = new File("src/main/resources/NPOTKey");
        if (keyfile.exists()) {
            FileInputStream fin = new FileInputStream(keyfile);
            ObjectInputStream fois = new ObjectInputStream(fin);

            try {
                C = (BigInteger) fois.readObject();
                p = (BigInteger) fois.readObject();
                q = (BigInteger) fois.readObject();
                g = (BigInteger) fois.readObject();
                gr = (BigInteger) fois.readObject();
                r = (BigInteger) fois.readObject();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            fois.close();

            Util.sendMessage(
                    Util.objectMapper.writeValueAsString(
                            new InitializeMessage(C, p, q, g, gr, msgBitLength)),
                    os
            );

            Cr = C.modPow(r, p);
        } else {
            BigInteger pdq;
            q = new BigInteger(qLength, certainty, rnd);
            int c = 0;
            do {

                pdq = new BigInteger(pLength - qLength, rnd);
                pdq = pdq.clearBit(0);
                p = q.multiply(pdq).add(BigInteger.ONE);
            } while (!p.isProbablePrime(certainty));
            c = 0;
            do {

                g = new BigInteger(pLength - 1, rnd);
            } while ((g.modPow(pdq, p)).equals(BigInteger.ONE)
                    || (g.modPow(q, p)).equals(BigInteger.ONE));


            r = (new BigInteger(qLength, rnd)).mod(q);
            gr = g.modPow(r, p);
            C = (new BigInteger(qLength, rnd)).mod(q);

            Util.sendMessage(
                    Util.objectMapper.writeValueAsString(
                            new InitializeMessage(C, p, q, g, gr, msgBitLength)),
                    os
            );

            Cr = C.modPow(r, p);

        /*    FileOutputStream fout = new FileOutputStream(keyfile);
            ObjectOutputStream foos = new ObjectOutputStream(fout);

            foos.writeObject(C);
            foos.writeObject(p);
            foos.writeObject(q);
            foos.writeObject(g);
            foos.writeObject(gr);
            foos.writeObject(r);

            foos.flush();
            foos.close();*/

        }
    }

    public void send(byte[] m1, byte[] m2) throws IOException {

        BigInteger msg1 = new BigInteger(NPUtil.prepareByteArray(m1));
        BigInteger msg2 = new BigInteger(NPUtil.prepareByteArray(m2));
        BigInteger pk0 = null;
        pk0 = Util.objectMapper.readValue(Util.receiveMessage(is), ChooserPublicKey.class).getPk();

        pk0 = pk0.modPow(r, p);
        BigInteger pk1 = Cr.multiply(pk0.modInverse(p)).mod(p);
        BigInteger R = generateR();
        NPOTTransfer npotTransfer = new NPOTTransfer(
                Cipher.encrypt(R, pk0, msg1, msgBitLength),
                Cipher.encrypt(R, pk1, msg2, msgBitLength),
                R
        );
        Util.sendMessage(
                Util.objectMapper.writeValueAsString(
                        npotTransfer),
                os
        );
    }

    private BigInteger generateR() {
        return new BigInteger(rLength, rnd);
    }
}
