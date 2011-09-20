/*
 *                This file is part of Laserinne.
 * 
 *  Laser projections in public space, inspiration and
 *  information, exploring the aesthetic and interactive possibilities of
 *  laser-based displays.
 * 
 *  http://www.laserinne.com/
 * 
 * Laserinne is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Laserinne is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Laserinne. If not, see <http://www.gnu.org/licenses/>.
 */

package com.laserinne.util;

/**
 * RandomWalkOscillator provides random points attracted to zero.
 * 
 * Algorithm from http://arcadiaresearch.com/blog/random-walk-oscillator.html
 * This isn't proper/full implementation.
 * 
 * @author Jyrki Lilja
 */

public class RandomWalkOscillator {
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
