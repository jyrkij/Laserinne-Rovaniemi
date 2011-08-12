/**
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

import processing.core.PVector;

/**
 * FakeSkier is a "fake" implementation of Skier seen in other Laserinne
 * sketches. Basically this makes it possible to use mouse to "simulate" skier
 * tracking.
 * 
 * @author Jyrki Lilja
 */

class FakeSkier {
    private PVector position;
    
    public FakeSkier(float x, float y) {
        this.position = new PVector(x, y);
    }
    
    public void update() {
        
    }
    
    public float getX() {
        return this.position.x;
    }
    
    public float getY() {
        return this.position.y;
    }
    
    public void setPosition(int x, int y) {
        this.position = new PVector(x, y);
    }
}