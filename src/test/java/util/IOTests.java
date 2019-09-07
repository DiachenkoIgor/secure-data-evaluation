package util;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.security.SecureRandom;

public class IOTests {
    protected PipedInputStream pis1;
    protected PipedInputStream pis2;
    protected PipedOutputStream pos1;
    protected PipedOutputStream pos2;
    protected static SecureRandom secureRandom;

    @BeforeClass
    public static void initializeRandom(){
        secureRandom=new SecureRandom();
    }

    @Before
    public void prepareStreams() throws IOException {
        pis1=new PipedInputStream();
        pis2=new PipedInputStream();
        pos1=new PipedOutputStream();
        pos2=new PipedOutputStream();
        pis1.connect(pos2);
        pis2.connect(pos1);
    }

    @After
    public void closeStreams() throws IOException {
        pis1.close();
        pis2.close();
        pos1.close();
        pos2.close();
    }
}
