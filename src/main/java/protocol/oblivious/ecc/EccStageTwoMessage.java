package protocol.oblivious.ecc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.security.spec.ECPoint;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)

public class EccStageTwoMessage {
    private CustomECPoint Two0;
    private CustomECPoint Two1;
    private CustomECPoint Two2;
    private CustomECPoint Two3;

    public EccStageTwoMessage(ECPoint two0, ECPoint two1, ECPoint two2, ECPoint two3) {
        Two0 = ECOperations.convertECPointToCustom(two0);
        Two1 = ECOperations.convertECPointToCustom(two1);
        Two2 = ECOperations.convertECPointToCustom(two2);
        Two3 = ECOperations.convertECPointToCustom(two3);
    }

    public EccStageTwoMessage() {
    }

    public ECPoint getTwo0() {
        return ECOperations.convertCustomECPoint(Two0);
    }

    public ECPoint getTwo1() {
        return ECOperations.convertCustomECPoint(Two1);
    }

    public ECPoint getTwo2() {
        return ECOperations.convertCustomECPoint(Two2);
    }


    public ECPoint getTwo3() {
        return ECOperations.convertCustomECPoint(Two3);
    }
}
