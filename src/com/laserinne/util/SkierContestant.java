package com.laserinne.util;

import processing.core.PApplet;

public class SkierContestant extends FakeSkier {
    protected boolean running;
    protected boolean finished;
    protected int finishLine;
    protected long startTime;
    protected long finishTime;
    
    public SkierContestant(float x, float y, PApplet processing) {
        super(x, y, processing);
        this.reset();
    }
    
    public void finishLine(int finishLine) {
        this.finishLine = finishLine;
    }
    
    public boolean finished() {
        if (this.getY() >= this.finishLine && !this.finished) {
            this.running = false;
            this.finished = true;
            this.finishTime = System.currentTimeMillis();
            System.out.println(this.timeInSeconds());
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
        this.startTime = System.currentTimeMillis();
    }
    
    public float timeInSeconds() {
        return (this.finishTime - this.startTime) / 1000.0f;
    }

    public void reset() {
        this.running = false;
        this.finished = false;
        this.startTime = 0;
    }
}
