/**
 * XORShiftRandom
 * 
 * @see http://stackoverflow.com/questions/453479/how-good-is-java-util-random
 * @see http://www.javamex.com/tutorials/random_numbers/xorshift.shtml
 * @see http://www.javamex.com/tutorials/random_numbers/java_util_random_subclassing.shtml
 * @see http://www.jstatsoft.org/v08/i14/paper
 * 
 * @author Jyrki Lilja
 */

public class XORShiftRandom extends java.util.Random {
    private long seed = System.nanoTime();
    
    public XORShiftRandom() {
        
    }
    
    synchronized protected int next(int nbits) {
        long x = this.seed;
        x ^= (x << 21);
        x ^= (x >>> 35);
        x ^= (x << 4);
        this.seed = x;
        x &= ((1L << nbits) -1);
        return (int) x;
    }
    
    public double nextDouble() {
        double d = super.nextDouble();
        return d;
    }
}