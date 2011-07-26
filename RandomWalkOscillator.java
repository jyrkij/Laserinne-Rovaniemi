/**
 * RandomWalkOscillator
 * 
 * Algorithm from http://arcadiaresearch.com/blog/random-walk-oscillator.html
 * This isn't proper/full implementation.
 * 
 * @package FIS/SnakeRun
 * @author Jyrki Lilja
 */

import java.util.Random;

class RandomWalkOscillator {
    private Random generator;
    private int numberOfWalkStepsAwayFromZero = 1;
    private double walkValue = 0.0;
    private double g;
    
    public RandomWalkOscillator() {
        this(1.0);
    }
    
    public RandomWalkOscillator(double g) {
        this.g = g;
        this.generator = new Random();
    }
    
    public double nextStep() {
        double p = this.generator.nextDouble();
        double a = -(Math.log(p * (Math.pow(Math.E, - this.numberOfWalkStepsAwayFromZero * this.g) - 1) + 1)) / (this.numberOfWalkStepsAwayFromZero * this.g);
        double u = 1 * Math.sqrt(3) * (2 * a - 1) + 0;
        if (u + this.walkValue > this.walkValue) {
            this.numberOfWalkStepsAwayFromZero++;
        } else if (u + this.walkValue < this.walkValue) {
            this.numberOfWalkStepsAwayFromZero--;
        }
        double nextStep = u;
        if (this.numberOfWalkStepsAwayFromZero == 0) {
            nextStep = p;
            this.numberOfWalkStepsAwayFromZero = (int) Math.signum(p);
        }
        
        this.walkValue += nextStep;
        
        return this.walkValue;
    }
}