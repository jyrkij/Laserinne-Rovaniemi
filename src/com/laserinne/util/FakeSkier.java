/**
 * FakeSkier
 * 
 * @package FIS/SnakeRun
 * @author Jyrki Lilja
 */

package com.laserinne.util;

import processing.core.PVector;

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