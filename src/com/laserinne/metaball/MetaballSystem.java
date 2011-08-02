package com.laserinne.metaball;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PVector;

public class MetaballSystem {
    private ArrayList<Ball> balls;
    private float goo;
    private float threshold;
    private float minSize;
    
    public MetaballSystem(ArrayList<Ball> balls, float goo, float threshold) {
        this.balls = balls;
        this.goo = goo;
        this.threshold = threshold;
        this.minSize = Collections.min(balls, new Comparator<Ball>() {
            public int compare(Ball b, Ball b2) {
                if (b.size() < b2.size())
                    return -1;
                else if (b.size() > b2.size())
                    return 1;
                else
                    return 0;
            }
        }).size();
    }
    
    public ArrayList<Ball> balls() {
        return new ArrayList<Ball>(balls);
    }
    
    public float force(PVector position) {
        if (Float.isNaN(position.x) && Float.isNaN(position.y)) {
            System.err.println("MetaballSystem.force(" + position + ")");
            //System.exit(1);
        }
        float force = 0.0f;
        for (Ball ball : this.balls) {
            float div = (float) Math.pow(
             PVector.sub(
              ball.position(),
              position
             ).mag(),
             this.goo
            );
            if (div != 0) {
                force += ball.size() / div;
            } else {
                force += 10000; // "big number"
            }
        }
        return force;
    }
    
    public PVector normal(PVector position) {
        if (Float.isNaN(position.x) && Float.isNaN(position.y)) {
            System.err.println("MetaballSystem.normal(" + position + ")");
            //System.exit(1);
        }
        PVector normal = new PVector();
        for (Ball ball : this.balls) {
            float div = (float) Math.pow(
             PVector.sub(
              ball.position(),
              position
             ).mag(),
             2 + this.goo
            );
            PVector vec = PVector.sub(ball.position(), position);
            vec.mult(-this.goo * ball.size() / div);
            normal.add(vec);
        }
        normal.normalize();
        return normal;
    }
    
    public PVector tangent(PVector position) {
        if (Float.isNaN(position.x) && Float.isNaN(position.y)) {
            System.err.println("MetaballSystem.tangent(" + position + ")");
            //System.exit(1);
        }
        PVector normal = this.normal(position);
        return new PVector(-normal.y, normal.x);
    }
    
    public PVector stepOnceTowardsBorder(PVector position) {
        if (Float.isNaN(position.x) && Float.isNaN(position.y)) {
            System.err.println("MetaballSystem.stepOnceTowardsBorder(" + position + ")");
            //System.exit(1);
        }
        float force = this.force(position);
        PVector normal = this.normal(position);
        float stepsize = (float) (Math.pow(this.minSize / this.threshold, 1 / this.goo) - Math.pow(this.minSize / force, 1 / this.goo) + 0.01);
        return PVector.add(position, PVector.mult(normal, stepsize));
    }
    
    public PVector trackTheBorder(PVector position) {
        if (Float.isNaN(position.x) && Float.isNaN(position.y)) {
            System.err.println("MetaballSystem.trackTheBorder(" + position + ")");
            //System.exit(1);
        }
        float force = 9999999;
        while (force > this.threshold) {
            force = this.force(position);
            position = this.stepOnceTowardsBorder(position);
        }
        return position;
    }
}
