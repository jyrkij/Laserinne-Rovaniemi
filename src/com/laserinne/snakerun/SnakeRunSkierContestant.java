package com.laserinne.snakerun;

import processing.core.PVector;

import com.laserinne.util.Mover;
import com.laserinne.util.SkierContestant;
import com.laserinne.util.Snake;

public class SnakeRunSkierContestant extends SkierContestant {
    private boolean inSnake;
    
    public SnakeRunSkierContestant(float x, float y) {
        super(x, y);
    }
    
    public boolean inSnake(Snake snake) {
        this.update();
        this.inSnake = false;
        int followerCount = snake.followerCount(),
            /**
             * Allow the skier to be between 1 / 2 and 2 / 3 of the snake. If
             * the skier is faster accelerate the snake and vice versa.
             */
            lastAllowedIndex = followerCount * 2 / 3,
            firstAllowedIndex = followerCount * 1 / 2;
        Mover m = snake.head();
        while (m.follower() != null) {
            if (m.closeTo(new PVector(this.getX(), this.getY()), 10)) {
                if (m.index() > lastAllowedIndex) {
                    snake.changeTopSpeed(-0.02f);
                }
                if (m.index() < firstAllowedIndex) {
                    snake.changeTopSpeed(0.01f);
                }
                this.inSnake = true;
                break;
            }
            m = m.follower();
        }
        if (!this.inSnake) {
            snake.changeTopSpeed(-0.02f);
        }
        return this.inSnake;
    }
    
    public void draw(processing.core.PGraphics g) {
        int originalStroke = g.strokeColor;
        if (this.inSnake) {
            g.stroke(0xFF00FF00);
        }
        super.draw(g);
        g.stroke(originalStroke);
    }
}