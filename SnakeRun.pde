/**
 * SnakeRun
 * 
 * @package FIS
 * @author Jyrki Lilja
 */

import java.util.ArrayList;
import java.util.Random;

import laserschein.Laser3D;
import laserschein.Laserschein;
import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

public class SnakeRun extends PApplet {
    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = 271041992974403855L;
//    private float leftSpeed,
//                  rightSpeed;
    private boolean snakeOnLeftIsRunning = false,
                    snakeOnRightIsRunning = false,
                    laserOn = false;
    
    private Mover leftSnake,
                  rightSnake;
    
    private Laserschein laser;
    
    private ArrayList<PVector> leftPoints,
                               rightPoints;
    
    public static void main(String args[]) {
        PApplet.main(new String[] {"--present", "SnakeRun"});
    }
    
    public void setup() {
        size(640, 480, PGraphicsOpenGL.OPENGL);
        hint(ENABLE_OPENGL_4X_SMOOTH);
        hint(ENABLE_NATIVE_FONTS);
        hint(ENABLE_DEPTH_SORT);
        frameRate(-1); // Use maximum frame rate.
        laser = new Laserschein(this);//, Laserschein.EASYLASEUSB2);
        
        smooth();
        
//        leftSpeed = frameRate;
//        rightSpeed = frameRate;
        
        colorMode(RGB);
        stroke(255, 255, 255);
        
        // Create the snakes
        leftSnake = new Mover(width / 4, 0, this);
        leftSnake.addFollowers(10);
        rightSnake = new Mover(width * 3 / 4, 0, this);
        rightSnake.addFollowers(20);
        
        // Create paths
        leftPoints = new ArrayList<PVector>();
        rightPoints = new ArrayList<PVector>();
        generatePaths();
        
        // Assign paths to snakes
        leftSnake.targets(leftPoints);
        rightSnake.targets(rightPoints);
    }
    
    public void draw() {
        background(0);
        if (laserOn) {
            Laser3D renderer = laser.renderer();
            beginRaw(renderer);
            noFill();
            stroke(255, 0, 0);
            
            if (snakeOnLeftIsRunning) {
                leftSnake.run();
                leftSnake.update();
                if (leftSnake.closeTo(leftPoints.get(leftPoints.size() - 1), 15)) {
                    System.out.println("Left finished.");
                    snakeOnLeftIsRunning = false;
                }
            }
            
            if (snakeOnRightIsRunning) {
                rightSnake.run();
                rightSnake.update();
                if (rightSnake.closeTo(rightPoints.get(rightPoints.size() - 1), 15)) {
                    System.out.println("Right finished.");
                    snakeOnRightIsRunning = false;
                }
            }
            
            leftSnake.draw();
            rightSnake.draw();
            
            endRaw();
        } else {
            // Draw the paths & points to screen when Laser is off.
            noFill();
            stroke(0, 0, 255);
            drawPath(leftPoints);
            drawPath(rightPoints);
        }
    }
    
    private void drawPath(ArrayList<PVector> points) {
        beginShape();
        for (PVector p : points) {
            // First point is the first control point as well.
            if (points.get(0) == p) {
                curveVertex((float) p.x, (float) p.y);
            }
            curveVertex((float) p.x, (float) p.y);
            // Last point is the last control point as well.
            if (points.get(points.size() - 1) == p) {
                curveVertex((float) p.x, (float) p.y);
            }
        }
        endShape();
        
        // Show the points
        beginShape();
        for (PVector p : points) {
            ellipse((float) p.x - 5, (float) p.y - 5, (float) 10, (float) 10);
        }
        endShape();
    }
    
    private void generatePaths() {
        int xSineFactor = 25,
            xStepFactor = 50,
            yStep = 25;
        
        int numberOfWalkStepsAwayFromZero = 1;
        float walkValue = 0.0f;
        
        leftPoints.clear();
        leftPoints.add(new PVector(width * 1 / 4, 0));
        rightPoints.clear();
        rightPoints.add(new PVector(width * 3 / 4, 0));
        
        for (int step = 0; step <= (height / yStep); step++) {
            /**
             * Random walk oscillator for x coordinate from
             * http://arcadiaresearch.com/blog/random-walk-oscillator.html
             * This isn't proper/full implementation.
             */
            Random generator = new Random();
            float p = generator.nextFloat();
            float g = 1;
            float a_ = (float) - (Math.log(p * (Math.pow(Math.E, - numberOfWalkStepsAwayFromZero * g) - 1) + 1)) / (numberOfWalkStepsAwayFromZero * g);
            float u_ = 1 * (float) Math.sqrt(3) * (2 * a_ - 1) + 0;
            if (u_ + walkValue > walkValue) {
                numberOfWalkStepsAwayFromZero++;
            } else if (u_ + walkValue < walkValue) {
                numberOfWalkStepsAwayFromZero--;
            }
            float nextStep = u_;
            if (numberOfWalkStepsAwayFromZero == 0) {
                nextStep = p;
                numberOfWalkStepsAwayFromZero = (int) Math.signum(p);
            }
            
            walkValue += nextStep;
            
            // Calculate points for left and right snakes.
            float x, y = yStep * (step + 1);
            x = width / 4 + walkValue * xStepFactor + sin(y * y * y * xSineFactor) * y / xSineFactor;
            if (x < width / 20) {
                x = width / 20;
            } else if (x > width * 9 / 20) {
                x = width * 9 / 20;
            }
            leftPoints.add(new PVector(x, y));
            
            x = width * 3 / 4 + walkValue * xStepFactor;
            if (x < width * 11 / 20) {
                x = width * 11 / 20;
            }
            if (x > width * 19 / 20) {
                x = width * 19 / 20;
            }
            rightPoints.add(new PVector(x, y));
        }
    }
    
    public void keyPressed() {
        if (key == CODED) {
            // Snake speed control for test purposes
            if (keyCode == UP) { // add speed for *Left snake*
                leftSnake.changeTopSpeed(0.25f);
            } else if (keyCode == DOWN) { // slow down *Left snake*
                leftSnake.changeTopSpeed(-0.25f);
            } else if (keyCode == RIGHT) { // add speed for *Right snake*
                rightSnake.changeTopSpeed(0.25f);
            } else if (keyCode == LEFT) { // slow down *Right snake*
                rightSnake.changeTopSpeed(-0.25f);
            }
        } else if (key == 'n') {
            // Generate new paths & reset positions
            leftSnake.reset(width / 4, 0);
            rightSnake.reset(width * 3 / 4, 0);
            generatePaths();
            leftSnake.targets(leftPoints);
            rightSnake.targets(rightPoints);
            snakeOnLeftIsRunning = false;
            snakeOnRightIsRunning = false;
        } else if (key == 's') {
            // Start/stop
            snakeOnLeftIsRunning = !snakeOnLeftIsRunning;
            snakeOnRightIsRunning = !snakeOnRightIsRunning;
        } else if (key == 'r') {
            // Reset positions
            leftSnake.reset(width / 4, 0);
            leftSnake.targets(leftPoints);
            rightSnake.reset(width * 3 / 4, 0);
            rightSnake.targets(rightPoints);
            snakeOnLeftIsRunning = false;
            snakeOnRightIsRunning = false;
        } else if (key == 'l') {
            // Switch laser on/off
            laserOn = !laserOn;
        }
    }
}
