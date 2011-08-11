package com.laserinne.util;

public class SkierContestant extends FakeSkier {
    protected boolean running;
    protected boolean finished;
    protected long startTime;
    protected long finishTime;
    
    protected static int finishLine;
    
    public SkierContestant(float x, float y) {
        super(x, y);
        this.reset();
    }
    
    public static void finishLine(int finishLine) {
        SkierContestant.finishLine = finishLine;
    }
    
    public boolean finished() {
        if (this.getY() >= SkierContestant.finishLine && !this.finished) {
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
    
    public void draw(processing.core.PGraphics g) {
        g.ellipseMode(processing.core.PConstants.CENTER);
        g.ellipse(this.getX(), this.getY(), 10, 10);
    }
}
