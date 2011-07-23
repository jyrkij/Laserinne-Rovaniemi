/**
 * Mover
 * 
 * Adapted from http://processing.org/learning/pvector/
 * 
 * @package FIS/SnakeRun
 * @author Jyrki Lilja
 */

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;

class Mover {
    private PApplet processing;
    private PVector location;
    private PVector velocity;
    private PVector acceleration;
    public float topspeed;
    private Mover follower;
    private PVector target;
    private boolean running;
    private ArrayList<PVector> targets;
    private static final float FOLLOWER_MIN_DISTANCE = 10.0f;
    
    public Mover(float x, float y, PApplet processing) {
        this.processing = processing;
        this.location = new PVector(x, y);
        this.velocity = new PVector(0.0f, 0.0f);
        this.acceleration = new PVector(0.0f, 0.0f);
        this.topspeed = 0.25f;
        this.follower = null;
        this.target = null;
        this.running = false;
        this.targets = new ArrayList<PVector>();
    }
    
    @SuppressWarnings("unchecked")
    public void targets(ArrayList<PVector> targets) {
        this.targets = (ArrayList<PVector>) targets.clone();
        if (this.follower != null) {
            this.follower.targets(targets);
        }
    }
    
    public void run() {
        if (!this.running && this.targets.size() > 0) {
            this.running = true;
            this.target = this.targets.remove(0);
        }
    }
    
    public void update() {
        if (this.target != null && this.running) {
            this.acceleration = PVector.sub(this.target, this.location);
            this.acceleration.normalize();
            this.acceleration.mult(1);
            this.velocity.add(this.acceleration);
            this.velocity.limit(this.topspeed);
            if (this.follower != null) {
                if (this.followerDistance() >= Mover.FOLLOWER_MIN_DISTANCE) {
                    this.follower.run();
                }
                this.follower.update();
            }
            this.location.add(velocity);
            if (this.closeTo(this.target)) {
                if (this.targets.size() > 0) {
                    this.target = this.targets.remove(0);
                }
            }
        }
    }
    
    public void draw() {
        processing.stroke(255, 0, 0);
        processing.ellipse(location.x, location.y, 20, 20);
        if (this.follower != null) {
            this.follower.draw();
        }
    }
    
    public void reset(float x, float y) {
        this.location.set(x, y, 0.0f);
        this.velocity.set(0.0f, 0.0f, 0.0f);
        this.acceleration.set(0.0f, 0.0f, 0.0f);
        this.target = null;
        this.running = false;
        this.targets.clear();
        if (this.follower != null) {
            this.follower.reset(x, y);
        }
    }
    
    public void addFollowers(int count) {
        this.follower = new Mover(this.location.x, this.location.y, this.processing);
        if (count > 1) {
            this.follower.addFollowers(count - 1);
        }
    }
    
    public void changeTopSpeed(float amount) {
        this.topspeed += amount;
        if (this.follower != null) {
            this.follower.changeTopSpeed(amount);
        }
    }
    
    public boolean closeTo(PVector target, float radius) {
        PVector diff = PVector.sub(target, this.location);
        if (diff.mag() < radius) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean closeTo(PVector target) {
        return this.closeTo(target, 10);
    }
    
    private float followerDistance() {
        return PVector.sub(this.location, this.follower.location).mag();
    }
}
