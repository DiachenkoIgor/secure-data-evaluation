package protocol.oblivious;

import java.io.IOException;

public interface ObliviousReceiver {
    byte[] receive() throws IOException;
    void setChoice(int choice);
}
