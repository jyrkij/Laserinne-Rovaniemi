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
    
    private static PApplet processing;
    
    public FakeSkier(float x, float y) {
        this.position = new PVector(x, y);
    }
    
    public static void processing(PApplet processing) {
        FakeSkier.processing = processing;
    }
    
    public void update() {
        this.position = new PVector(FakeSkier.processing.mouseX, FakeSkier.processing.mouseY);
    }
    
    public float getX() {
        return this.position.x;
    }
    
    public float getY() {
        return this.position.y;
    }
}