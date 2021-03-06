/*
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

import java.util.ArrayList;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Mover is the basis of moving objects and following leaders.
 * 
 * @author Jyrki Lilja
 */
public class Mover {
    private PVector location;
    private PVector velocity;
    private PVector acceleration;
    private float topSpeed;
    private Mover follower;
    private PVector target;
    private boolean running;
    private ArrayList<PVector> targets;
    private static final float FOLLOWER_MIN_DISTANCE = 1.0f;
    /**
     * @field index current index in head followers. -1 if head.
     * @access private
     */
    private int index;
    /**
     * @field followerCount count of followers to come.
     * @access private
     */
    private int followerCount;
    
    /**
     * Constructor.
     * 
     * @param x Initial x position
     * @param y Initial y position
     * @param processing Processing instance
     */
    public Mover(float x, float y) {
        this.location = new PVector(x, y);
        this.velocity = new PVector(0.0f, 0.0f);
        this.acceleration = new PVector(0.0f, 0.0f);
        this.topSpeed = 0.25f;
        this.follower = null;
        this.target = null;
        this.running = false;
        this.targets = new ArrayList<PVector>();
        this.index = -1;
        this.followerCount = 0;
    }
    
    /**
     * targets
     * 
     * Sets the targets.
     * 
     * @param targets Targets to be set.
     */
    @SuppressWarnings("unchecked")
    public void targets(ArrayList<PVector> targets) {
        this.targets = (ArrayList<PVector>) targets.clone();
        if (this.follower != null) {
            this.follower.targets(targets);
        }
    }
    
    /**
     * @return the next follower.
     */
    public Mover follower() {
        if (this.follower != null) {
            return this.follower;
        }
        return null;
    }
    
    /**
     * run
     * start
     * Sets the first target & starts running the Mover.
     */
    public void start() {
        if (!this.running && this.targets.size() > 0) {
            this.running = true;
            if (this.target == null) {
                this.target = this.targets.remove(0);
            }
        }
    }
    
    /**
     * stops the Mover
     */
    public void stop() {
        this.running = false;
        if (this.follower != null) {
            this.follower.stop();
        }
    }
    
    /**
     * @return is the mover running?
     */
    public boolean running() {
        return this.running;
    }
    
    /**
     * update
     * 
     * Moves the Movers closer to target.
     * Decides when to switch to next target.
     */
    public void update() {
        if (this.target != null && this.running) {
            this.acceleration = PVector.sub(this.target, this.location);
            this.acceleration.normalize();
            this.acceleration.mult(1);
            this.velocity.add(this.acceleration);
            this.velocity.limit(this.topSpeed);
            if (this.follower != null) {
                if (this.followerDistance() >= Mover.FOLLOWER_MIN_DISTANCE) {
                    this.follower.start();
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
    
    /**
     * draw
     * 
     * Draws the Mover.
     */
    public void draw(PGraphics g) {
        g.ellipseMode(PConstants.CENTER);
        g.ellipse(this.location.x, this.location.y, 20, 20);
        if (this.follower != null) {
            this.follower.draw(g);
        }
    }
    
    /**
     * reset
     * 
     * Resets this and its followers
     * 
     * @param x Coordinates to reset to.
     * @param y Coordinates to reset to.
     */
    public void reset(float x, float y, ArrayList<PVector> targets) {
        this.location.set(x, y, 0.0f);
        this.velocity.set(0.0f, 0.0f, 0.0f);
        this.acceleration.set(0.0f, 0.0f, 0.0f);
        this.topSpeed(0.25f);
        this.target = null;
        this.running = false;
        this.targets(targets);
        if (this.follower != null) {
            this.follower.reset(x, y, targets);
        }
    }
    
    /**
     * addFollowers
     * 
     * Adds ``count'' followers.
     * 
     * @param count Amount of followers to be added.
     */
    public void addFollowers(int count) {
        this.addFollowers(count, 0);
        this.followerCount = count;
    }
    
    /**
     * addFollowers
     * 
     * Adds ``count'' - ``index'' followers.
     * 
     * @param count Amount of followers to be added.
     * @param index index of the follower. First follower has index 0.
     */
    private void addFollowers(int count, int index) {
        this.follower = new Mover(this.location.x, this.location.y);
        this.index = index;
        index++;
        if (count - index > 0) {
            this.follower.addFollowers(count, index);
        }
    }
    
    /**
     * @return current follower index.
     */
    public int index() {
        return this.index;
    }
    
    /**
     * @return follower count
     */
    public int followerCount() {
        return this.followerCount;
    }
    
    /**
     * changeTopSpeed
     * 
     * Adds ``amount'' to topSpeed of this and all followers.
     * 
     * @param amount Speed change.
     */
    public void changeTopSpeed(float amount) {
        if (this.topSpeed + amount >= 0) {
            this.topSpeed += amount;
        }
        if (this.follower != null) {
            this.follower.changeTopSpeed(amount);
        }
    }
    
    /**
     * Getter for this.topSpeed
     */
    public float topSpeed() {
        return this.topSpeed;
    }
    
    /**
     * topSpeed
     *
     * Sets topSpeed to ``speed''.
     *
     * @param speed
     */
    public void topSpeed(float speed) {
        this.topSpeed = speed;
        if (this.follower != null) {
            this.follower.topSpeed(speed);
        }
    }
    
    /**
     * closeTo
     * 
     * Determines whether this is inside target radius.
     * 
     * @param target
     * @param radius
     * @return boolean Close to target?
     */
    public boolean closeTo(PVector target, float radius) {
        PVector diff = PVector.sub(target, this.location);
        if (diff.mag() < radius) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * closeTo
     * 
     * Simplified version of closeTo(PVector target, float radius). Accepts only
     * target, radius defaults to 10.0f.
     * 
     * @param target
     * @return boolean Within 10.0f to target.
     */
    public boolean closeTo(PVector target) {
        return this.closeTo(target, 10);
    }
    
    /**
     * followerDistance
     * 
     * Returns the distance between this and follower.
     * 
     * @return Distance between this and follower.
     */
    private float followerDistance() {
        return PVector.sub(this.location, this.follower.location).mag();
    }
}
