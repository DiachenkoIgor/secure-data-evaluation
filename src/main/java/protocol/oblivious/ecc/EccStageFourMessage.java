package protocol.oblivious.ecc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.security.spec.ECPoint;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)

public class EccStageFourMessage {
     private CustomECPoint Four0;
     private CustomECPoint Four1;
     private CustomECPoint Four2;
     private CustomECPoint Four3;

    public EccStageFourMessage(ECPoint four0, ECPoint four1, ECPoint four2, ECPoint four3) {
        Four0 = ECOperations.convertECPointToCustom(four0);
        Four1 = ECOperations.convertECPointToCustom(four1);
        Four2 = ECOperations.convertECPointToCustom(four2);
        Four3 = ECOperations.convertECPointToCustom(four3);
    }

    public EccStageFourMessage() {
    }

    public ECPoint getFour0() {
        return ECOperations.convertCustomECPoint(Four0);
    }

    public ECPoint getFour1() {
        return ECOperations.convertCustomECPoint(Four1);
    }

    public ECPoint getFour2() {
        return ECOperations.convertCustomECPoint(Four2);
    }

    public ECPoint getFour3() {
        return ECOperations.convertCustomECPoint(Four3);
    }

}
