/**
 * SnakeRun
 * 
 * @package FIS
 * @author Jyrki Lilja
 */

package com.laserinne.snakerun;

import java.util.ArrayList;

import com.laserinne.util.RandomWalkOscillator;

import geomerative.RFont;
import geomerative.RG;
import laserschein.Laser3D;
import laserschein.Laserschein;
import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

public class SnakeRun extends PApplet {
    private boolean snakeOnLeftIsRunning = false,
                    snakeOnRightIsRunning = false,
                    laserOn = false;
    
    private Laserschein laser;
    private Laser3D renderer;
    
    private Mover leftSnake,
                  rightSnake;
    private ArrayList<PVector> leftPoints,
                               rightPoints;
    private FakeSkier leftSkier,
                      rightSkier;
    private RFont font;
    
    public static final int LASER_COLOR = 0xFFFF0000;
    public static final int SCREEN_COLOR = 0xFF0000FF;
    
    /**
     * main
     * 
     * This has to be commented out in testing to allow font loading, but
     * available when exporting to application
     */
    public static void main(String args[]) {
         PApplet.main(new String[] { com.laserinne.snakerun.SnakeRun.class.getName() });
    }
    
    public void setup() {
        size(640, 480, PGraphicsOpenGL.OPENGL);
        hint(ENABLE_OPENGL_4X_SMOOTH);
        hint(ENABLE_NATIVE_FONTS);
        hint(ENABLE_DEPTH_SORT);
        frameRate(-1); // Use maximum frame rate.
        laser = new Laserschein(this, Laserschein.EASYLASEUSB2);
        renderer = laser.renderer();
        
        smooth();
        
        colorMode(RGB);
        stroke(SnakeRun.SCREEN_COLOR);
        noFill();
        
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
        
        // Initialize fake skiers
        leftSkier = new FakeSkier(width / 4, 0, this);
        rightSkier = new FakeSkier(width * 3 / 4, 0, this);
        
        RG.init(this);
        font = new RFont("Laserfont.ttf", 80, RFont.CENTER);
    }
    
    public void draw() {
        background(0);
        stroke(SnakeRun.SCREEN_COLOR);
        
        float cameraZ = (float) ((height/2.0) / Math.tan(PI * 60.0 / 360.0));
        // perspective(PI / 3.0, width/height, cameraZ/10.0, cameraZ*10.0);
        
        float eyeX = width / 2.0f,
              eyeY = height / 2.0f,
              eyeZ = (float) ((height / 2.0) / Math.tan(PI * 60.0 / 360.0)),
              centerX = width / 2.0f,
              centerY = height / 2.0f,
              centerZ = 0,
              upX = 0,
              upY = 1,
              upZ = 0;
        camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        
        // rotateX(-PI * 1 / 3);
        
        // Draw FPS on screen
        pushMatrix();
        translate(30, 80);
        font.setAlign(RFont.LEFT);
        font.draw(new Integer(Math.round(frameRate)).toString());
        popMatrix();
        
        drawPath(leftPoints);
        drawPath(rightPoints);
        
        line(width / 4, 0, width / 4, height);
        line(width / 2, 0, width / 2, height);
        line(width * 3 / 4, 0, width * 3 / 4, height);
        
        if (laserOn) {
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
            
            beginRaw(renderer);
            stroke(SnakeRun.LASER_COLOR);
            leftSnake.draw();
            rightSnake.draw();
            endRaw();
            stroke(SnakeRun.SCREEN_COLOR);
            
            handleSkier(leftSkier, leftSnake);
            // handleSkier(rightSkier, rightSnake);
        }
    }
    
    private void handleSkier(FakeSkier skier, Mover snakeHead) {
        skier.update();
        int followerCount = snakeHead.followerCount(),
            /**
             * Allow the skier to be between 1 / 2 and 2 / 3 of the snake. If
             * the skier is faster accelerate the snake and vice verca.
             */
            lastAllowedIndex = followerCount * 2 / 3,
            firstAllowedIndex = followerCount * 1 / 2;
        Mover m = snakeHead;
        boolean skierInSnake = false;
        stroke(SnakeRun.SCREEN_COLOR);
        while (m.follower() != null) {
            if (m.closeTo(new PVector(skier.getX(), skier.getY()), 10)) {
                if (m.index() > lastAllowedIndex) {
                    snakeHead.changeTopSpeed(-0.02f);
                }
                if (m.index() < firstAllowedIndex) {
                    snakeHead.changeTopSpeed(0.01f);
                }
                stroke(0, 255, 0);
                skierInSnake = true;
                break;
            }
            m = m.follower();
        }
        if (!skierInSnake) {
            snakeHead.changeTopSpeed(-0.02f);
        }
        ellipseMode(CENTER);
        ellipse(skier.getX(), skier.getY(), 10, 10);
        stroke(SnakeRun.SCREEN_COLOR);
    }
    
    private void drawPath(ArrayList<PVector> points) {
        beginShape();
        for (PVector p : points) {
            // First point is the first control point as well.
            if (points.get(0) == p) {
                curveVertex(p.x, p.y);
            }
            curveVertex(p.x, p.y);
            // Last point is the last control point as well.
            if (points.get(points.size() - 1) == p) {
                curveVertex(p.x, p.y);
            }
        }
        endShape();
        
        // Show the points
        beginShape();
        for (PVector p : points) {
            ellipseMode(CENTER);
            ellipse(p.x, p.y, 3, 3);
        }
        endShape();
    }
    
    private void generatePaths() {
        int steps = 25;
        
        leftPoints.clear();
        leftPoints.add(new PVector(width * 1 / 4, 0));
        rightPoints.clear();
        rightPoints.add(new PVector(width * 3 / 4, 0));
        
        RandomWalkOscillator rwo = new RandomWalkOscillator();
        
        for (int step = 0; step <= steps; step++) {
            float walkValue = (float) rwo.nextStep();
            
            // Calculate points for left and right snakes.
            float x, y = (height / steps) * (step + 1);
            x = width / 4 + walkValue;
            if (x < width / 20) {
                x = width / 20;
            } else if (x > width * 9 / 20) {
                x = width * 9 / 20;
            }
            leftPoints.add(new PVector(x, y));
            
            x += width / 2;
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
            generatePaths();
            leftSnake.reset(width / 4, 0);
            leftSnake.targets(leftPoints);
            leftSnake.topSpeed(0.25f);
            snakeOnLeftIsRunning = false;
            
            rightSnake.reset(width * 3 / 4, 0);
            rightSnake.targets(rightPoints);
            rightSnake.topSpeed(0.25f);
            snakeOnRightIsRunning = false;
        } else if (key == 's') {
            // Start/stop
            snakeOnLeftIsRunning = !snakeOnLeftIsRunning;
            snakeOnRightIsRunning = !snakeOnRightIsRunning;
        } else if (key == 'r') {
            // Reset positions
            leftSnake.reset(width / 4, 0);
            leftSnake.targets(leftPoints);
            leftSnake.topSpeed(0.25f);
            snakeOnLeftIsRunning = false;
            
            rightSnake.reset(width * 3 / 4, 0);
            rightSnake.targets(rightPoints);
            rightSnake.topSpeed(0.25f);
            snakeOnRightIsRunning = false;
        } else if (key == 'l') {
            // Switch laser on/off
            laserOn = !laserOn;
        }
    }
}