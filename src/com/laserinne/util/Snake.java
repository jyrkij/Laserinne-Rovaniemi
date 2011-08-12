package com.laserinne.util;

/**
 * Snake
 * 
 * @package com.laserinne.util
 * @author Jyrki Lilja
 */

public class Snake extends Mover {
    public Snake(float x, float y, int numFollowers) {
        super(x, y);
        this.addFollowers(numFollowers);
    }
    
    public Mover head() {
        return (Mover) this;
    }
}
