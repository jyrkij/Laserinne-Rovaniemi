package com.laserinne.util;

public abstract class SkierContestant extends FakeSkier {
    protected boolean running;
    protected boolean finished;
    protected long startTime;
    protected long finishTime;
    protected int points;
    
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
            System.out.println(this.combinedTimeAndPoints());
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
        this.points = 0;
    }
    
    public void draw(processing.core.PGraphics g) {
        g.ellipseMode(processing.core.PConstants.CENTER);
        g.ellipse(this.getX(), this.getY(), 10, 10);
    }
    
    public void update() {
        super.update();
        this.updatePoints();
    }
    
    public int points() {
        return this.points;
    }
    
    /**
     * This method returns the skier contestant's points converted to seconds.
     * Negative points should return positive value (that is they should add
     * time to the run). ±0.25 seconds/point is recommended, depending of course
     * on the point calculation method.
     * 
     * @return points converted to seconds.
     */
    public abstract float pointsToSeconds();
    
    /**
     * This method updates the points. Use whatever method you wish to
     * determine whether point should be added or not.
     * 
     * NOTE: Please remember that this method gets called on every frame so you
     * might accidentally add/remove way too many points!
     */
    public abstract void updatePoints();
    
    public float combinedTimeAndPoints() {
        return this.timeInSeconds() + this.pointsToSeconds();
    }
}
