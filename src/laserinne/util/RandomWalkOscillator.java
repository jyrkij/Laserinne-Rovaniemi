/**
 * RandomWalkOscillator
 * 
 * Algorithm from http://arcadiaresearch.com/blog/random-walk-oscillator.html
 * This isn't proper/full implementation.
 * 
 * @package FIS/SnakeRun
 * @author Jyrki Lilja
 */

class RandomWalkOscillator {
    private java.util.Random generator;
    private int d = 1;
    private double walkValue = 0.0;
    private double g;
    
    public RandomWalkOscillator() {
        this(1);
    }
    
    public RandomWalkOscillator(double g) {
        this.g = g;
        this.generator = new XORShiftRandom();
        this.generator.setSeed(3000); // This line is here to provide controlled randomness.
    }
    
    public double nextStep() {
        double p = this.generator.nextDouble();
        double A_inverse;
        if (this.d == 0) {
            A_inverse = p;
        } else {
            A_inverse = A_inverse(p);
        }
        double U_inverse = U_inverse(A_inverse, 0, 30);
        if (U_inverse + this.walkValue > this.walkValue) {
            this.d++;
        } else if (U_inverse + this.walkValue < this.walkValue) {
            this.d--;
        }
        
        this.walkValue += U_inverse;
        
        return this.walkValue;
    }
    
    private double A_inverse(double p) {
        return -(Math.log(p * (Math.pow(Math.E, - this.d * this.g) - 1) + 1)) / (this.d * this.g);
    }
    
    private double U_inverse(double p, double mu, double sigma) {
        return sigma * Math.sqrt(3) * (2 * p - 1) + mu;
    }
}
