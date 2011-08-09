/**
 * FakeSkier
 * 
 * @package FIS/SnakeRun
 * @author Jyrki Lilja
 */

package com.laserinne.util;

import processing.core.PApplet;
import processing.core.PVector;

class FakeSkier {
    private PVector position;
    private PApplet processing;
    
    public FakeSkier(float x, float y, PApplet processing) {
        this.position = new PVector(x, y);
        this.processing = processing;
    }
    
    public FakeSkier(PVector startPosition, PApplet processing) {
        this.position = startPosition;
        this.processing = processing;
    }
    
    public void update() {
        this.position = new PVector(this.processing.mouseX, this.processing.mouseY);
    }
    
    public float getX() {
        return this.position.x;
    }
    
    public float getY() {
        return this.position.y;
    }
}