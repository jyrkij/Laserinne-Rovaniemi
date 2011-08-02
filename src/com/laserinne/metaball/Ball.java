package com.laserinne.metaball;

import processing.core.PVector;

public class Ball {
    private PVector position;
    private float size;
    private PVector initialPosition;
    private PVector edgePosition;
    private boolean tracking;
    
    public Ball(PVector position, float size) {
        this.position = position;
        this.size = size;
    }
    
    public PVector position() {
        return this.position;
    }
    
    public void position(PVector position) {
        this.position = position;
    }
    
    public float size() {
        return this.size;
    }
    
    public PVector initialPosition() {
        return this.initialPosition;
    }
    
    public void initialPosition(PVector position) {
        if (Double.isNaN(position.x) && Double.isNaN(position.y)) {
            System.err.println("Ball.initialPosition(" + position + ")");
            //System.exit(1);
        }
        this.initialPosition = position;
    }
    
    public PVector edgePosition() {
        return this.edgePosition;
    }
    
    public void edgePosition(PVector position) {
        if (Double.isNaN(position.x) && Double.isNaN(position.y)) {
            System.err.println("Ball.edgePosition(" + position + ")");
            //System.exit(1);
        }
        this.edgePosition = position;
    }
    
    public boolean tracking() {
        return this.tracking;
    }
    
    public void tracking(boolean tracking) {
        this.tracking = tracking;
    }
}