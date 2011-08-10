package com.laserinne.util;

import processing.core.PApplet;
import processing.core.PVector;


public class SkierContestant extends FakeSkier {
    protected boolean running;
    protected int finishLine;
    
    public SkierContestant(float x, float y, PApplet processing) {
        super(x, y, processing);
    }
    
    public void finishLine(int finishLine) {
        this.finishLine = finishLine;
    }
    
    public boolean finished() {
        if (this.getY() >= finishLine) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean running() {
        return this.running;
    }
    
    public void running(boolean running) {
        this.running = running;
    }
    
    public void start() {
        this.running = true;
    }
}
