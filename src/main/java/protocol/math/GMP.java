package protocol.math;



public final class GMP extends Number implements Comparable<GMP>
{
    private Pointer native_ptr;
    private int refCount = 1;
    private static final long serialVersionUID = -8287574255936472291L;

    static
    {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        System.load(classloader.getResource("gnu/libnativegmp.dll").getFile());
        natInitializeLibrary();
    }


    public static final GMP ZERO = new GMP(0);

    public GMP()
    {
        super();
        natInitialize();
    }

    private GMP(String s,int base)
    {
        this();
        fromString(s,base);
    }

    public GMP(String s)
    {
        this(s,10);
    }

    public GMP(long n)
    {
        this();
        fromLong(n);
    }

    public GMP(int n)
    {
        this();
        fromLong(n);
    }

    private synchronized void acquireRef()
    {
        refCount++;
    }

    private synchronized void releaseRef()
    {
        refCount--;
        if (refCount == 0)
        {
            natFinalize();
            native_ptr = null;
        }
    }

    protected void finalize()
    {
        releaseRef();
    }

    public void clean(){
        releaseRef();
    }


    public void fromByteArray(byte[] v)
    {
        acquireRef();
        natFromByteArray(v);
        releaseRef();
    }

    public void fromBI(GMP x)
    {
        acquireRef();
        x.acquireRef();
        natFromBI(x.native_ptr);
        x.releaseRef();
        releaseRef();
    }

    public void fromLong(long n)
    {
        acquireRef();
        natFromLong(n);
        releaseRef();
    }

    public int fromString(String s, int rdx)
    {
        acquireRef();
        int result = natFromString(s, rdx);
        releaseRef();
        return result;
    }

    public void fromSignedMagnitude(byte[] m, boolean isNegative)
    {
        acquireRef();
        natFromSignedMagnitude(m, isNegative);
        releaseRef();
    }

    public String toString(int b)
    {
        acquireRef();
        String result = natToString(b);
        releaseRef();
        return result;
    }

    public String toString()
    {
        return toString(10);
    }

    public void toByteArray(byte[] r)
    {
        acquireRef();
        natToByteArray(r);
        releaseRef();
    }

    public double doubleValue()
    {
        acquireRef();
        double result = natDoubleValue();
        releaseRef();
        return result;
    }

    public int absIntValue()
    {
        acquireRef();
        int result = natAbsIntValue();
        releaseRef();
        return result;
    }

    public int compare(GMP x)
    {
        acquireRef();
        x.acquireRef();
        int result = natCompare(x.native_ptr);
        x.releaseRef();
        releaseRef();
        return result;
    }

    public void add(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natAdd(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void subtract(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natSubtract(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void multiply(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natMultiply(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void divide(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natQuotient(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void remainder(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natRemainder(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void quotientAndRemainder(GMP x, GMP q, GMP r)
    {
        acquireRef();
        x.acquireRef();
        q.acquireRef();
        r.acquireRef();
        natQuotientAndRemainder(x.native_ptr, q.native_ptr, r.native_ptr);
        r.releaseRef();
        q.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void mod(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natModulo(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void pow(int n, GMP r)
    {
        acquireRef();
        r.acquireRef();
        natPow(n, r.native_ptr);
        r.releaseRef();
        releaseRef();
    }

    public void modPow(GMP e, GMP m, GMP r)
    {
        acquireRef();
        e.acquireRef();
        m.acquireRef();
        r.acquireRef();
        natModPow(e.native_ptr, m.native_ptr, r.native_ptr);
        r.releaseRef();
        m.releaseRef();
        e.releaseRef();
        releaseRef();
    }

    public void modInverse(GMP m, GMP r)
    {
        acquireRef();
        m.acquireRef();
        r.acquireRef();
        natModInverse(m.native_ptr, r.native_ptr);
        r.releaseRef();
        m.releaseRef();
        releaseRef();
    }

    public void gcd(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natGCD(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void shiftLeft(int n, GMP r)
    {
        acquireRef();
        r.acquireRef();
        natShiftLeft(n, r.native_ptr);
        r.releaseRef();
        releaseRef();
    }

    public void shiftRight(int n, GMP r)
    {
        acquireRef();
        r.acquireRef();
        natShiftRight(n, r.native_ptr);
        r.releaseRef();
        releaseRef();
    }

    public void abs(GMP r)
    {
        acquireRef();
        r.acquireRef();
        natAbs(r.native_ptr);
        r.releaseRef();
        releaseRef();
    }

    public void negate(GMP r)
    {
        acquireRef();
        r.acquireRef();
        natNegate(r.native_ptr);
        r.releaseRef();
        releaseRef();
    }

    public int bitLength()
    {
        acquireRef();
        int result = natBitLength();
        releaseRef();
        return result;
    }

    public int bitCount()
    {
        acquireRef();
        int result = natSetBitCount();
        releaseRef();
        return result;
    }

    public void and(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natAnd(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void or(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natOr(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void xor(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natXor(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void andNot(GMP x, GMP r)
    {
        acquireRef();
        x.acquireRef();
        r.acquireRef();
        natAndNot(x.native_ptr, r.native_ptr);
        r.releaseRef();
        x.releaseRef();
        releaseRef();
    }

    public void not(GMP r)
    {
        acquireRef();
        r.acquireRef();
        natNot(r.native_ptr);
        r.releaseRef();
        releaseRef();
    }

    public void flipBit(int n, GMP r)
    {
        acquireRef();
        r.acquireRef();
        natFlipBit(n, r.native_ptr);
        r.releaseRef();
        releaseRef();
    }

    public int testBit(int n)
    {
        acquireRef();
        int result = natTestBit(n);
        releaseRef();
        return result;
    }

    public void setBit(int n, boolean setIt, GMP r)
    {
        acquireRef();
        r.acquireRef();
        natSetBit(n, setIt, r.native_ptr);
        r.releaseRef();
        releaseRef();
    }

    public int testPrimality(int certainty)
    {
        acquireRef();
        int result = natTestPrimality(certainty);
        releaseRef();
        return result;
    }

    public int lowestSetBit()
    {
        acquireRef();
        int result = natLowestSetBit();
        releaseRef();
        return result;
    }

    /* Comparable */
    public int compareTo(GMP x)
    {
        return compare(x);
    }

    /* Number */
    public float floatValue()
    {
        return (float) doubleValue();
    }

    public long longValue()
    {
        long result;
        GMP abs = new GMP();
        if(this.compareTo(ZERO) < 0)
            this.negate(abs);
        else
            abs.fromBI(this);
        abs.shiftRight(32,abs);
        result = abs.absIntValue();
        result <<= 32;
        result |= this.absIntValue() & 0xFFFFFFFFL;
        return this.compareTo(ZERO) < 0 ? - result : result;
    }

    public int intValue()
    {
        int result = absIntValue();
        return compare(new GMP(0)) < 0 ? - result : result;
    }

    //Native methods .........................................................

    public static native void natInitializeLibrary();

    private native void natInitialize();
    private native void natFinalize();

    private native void natFromLong(long n);
    private native void natFromBI(Pointer x);
    private native void natFromByteArray(byte[] v);
    private native int natFromString(String s, int rdx);
    private native void natFromSignedMagnitude(byte[] m, boolean isNegative);

    private native String natToString(int base);
    private native void natToByteArray(byte[] r);
    private native int natAbsIntValue();
    private native double natDoubleValue();

    private native int natCompare(Pointer y);
    private native void natAdd(Pointer x, Pointer r);
    private native void natSubtract(Pointer x, Pointer r);
    private native void natMultiply(Pointer x, Pointer r);
    private native void natQuotient(Pointer x, Pointer r);
    private native void natRemainder(Pointer x, Pointer r);
    private native void natQuotientAndRemainder(Pointer x, Pointer q, Pointer r);
    private native void natModulo(Pointer m, Pointer r);
    private native void natPow(int n, Pointer r);
    private native void natModPow(Pointer e, Pointer m, Pointer r);
    private native void natModInverse(Pointer x, Pointer r);
    private native void natGCD(Pointer x, Pointer r);
    private native int natTestPrimality(int c);
    private native void natShiftLeft(int n, Pointer r);
    private native void natShiftRight(int n, Pointer r);
    private native int natLowestSetBit();
    private native void natAbs(Pointer r);
    private native void natNegate(Pointer r);
    private native int natBitLength();
    private native int natSetBitCount();
    private native void natXor(Pointer x, Pointer r);
    private native void natOr(Pointer x, Pointer r);
    private native void natAnd(Pointer x, Pointer r);
    private native void natAndNot(Pointer x, Pointer r);
    private native void natFlipBit(int n, Pointer r);
    private native int natTestBit(int n);
    private native void natSetBit(int n, boolean setIt, Pointer r);
    private native void natNot(Pointer r);


}