package com.laserinne.util;

public class Snake extends Mover {
    public Snake(float x, float y, int numFollowers) {
        super(x, y);
        this.addFollowers(numFollowers);
    }
    
    public Mover head() {
        return (Mover) this;
    }
}
