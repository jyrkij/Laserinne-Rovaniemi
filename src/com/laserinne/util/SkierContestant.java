package com.laserinne.util;

import processing.core.PApplet;

public class SkierContestant extends FakeSkier {
    protected boolean running;
    protected boolean finished;
    protected int finishLine;
    
    public SkierContestant(float x, float y, PApplet processing) {
        super(x, y, processing);
        this.reset();
    }
    
    public void finishLine(int finishLine) {
        this.finishLine = finishLine;
    }
    
    public boolean finished() {
        if (this.getY() >= this.finishLine) {
            this.running = false;
            this.finished = true;
        }
        return this.finished;
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

    public void reset() {
        this.running = false;
        this.finished = false;
    }
}
