package protocol.nlp;

public class NlpUtil {
    public static byte convertTag(String tag){
        switch (tag){
            case "CC": return 1;
            case "CD": return 2;
            case "DT": return 3;
            case "EX": return 4;
            case "FW": return 5;
            case "IN": return 6;
            case "JJ": return 7;
            case "JJR": return 8;
            case "JJS": return 9;
            case "LS": return 10;
            case "MD": return 11;
            case "NN":
            case "NNS": return 13;
            case "NNP":
            case "NNPS": return 15;
            case "PDT": return 16;
            case "POS": return 17;
            case "PRP": return 18;
            case "PRP$": return 19;
            case "RB": return 20;
            case "RBR": return 21;
            case "RBS": return 22;
            case "RP": return 23;
            case "SYM": return 24;
            case "TO": return 25;
            case "UH": return 26;
            case "VB": return 27;
            case "VBD": return 28;
            case "VBG": return 29;
            case "VBN": return 30;
            case "VBP": return 31;
            case "VBZ": return 32;
            case "WDT": return 33;
            case "WP": return 34;
            case "WP$": return 35;
            case "WRB": return 36;
            default: return -1;
        }
    }
}
