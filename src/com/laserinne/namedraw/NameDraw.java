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

package com.laserinne.namedraw;

import geomerative.RFont;
import geomerative.RGroup;
import geomerative.RPath;
import geomerative.RPoint;
import geomerative.RShape;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import processing.core.PApplet;
import seltar.motion.Motion;

import com.laserinne.util.ContestantTracking;
import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Skier;

/**
 * @author Eero Ervast and Jyrki Lilja and Andreas Schmelas
 */

@SuppressWarnings("serial")
public class NameDraw extends LaserinneSketch {
    protected float x;
    protected float y;
    protected boolean overLine;
    protected int index = -1;
    protected ArrayList<String> nameList;
    protected Skier skier;
    protected String currentName;
    
    protected DisplayableName displayableName;
    
    protected static int FINISH_LINE = NameDraw.HEIGHT - 20;
    
    /**
     * main
     * 
     * This has to be commented out when running in Processing to allow font
     * loading, but available when running in Eclipse or building with Ant /
     * exporting to application from Processing or building with Ant.
     */
    public static void main(String args[]) {
         PApplet.main(new String[] { com.laserinne.namedraw.NameDraw.class.getName() });
    }
    
    public void setup() {
        super.setup();
        tracking = new ContestantTracking();
        overLine = false;
        
        /*
         * Load names
         */
        try {
            FileReader fileReader = new FileReader("bin/data/names.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            this.nameList = new ArrayList<String>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                this.nameList.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        currentName = " ";
    }
    
    public void draw() {
        super.draw();
        
        line(0, NameDraw.FINISH_LINE, width, NameDraw.FINISH_LINE);
        
        /*
         * GUI for selecting name
         */
        pushMatrix();
        translate(10, 100);
        int fontAlign = font.align;
        int fontSize = font.size;
        int guiTextSize = 16;
        font.setAlign(RFont.LEFT);
        font.setSize(guiTextSize);
        for (int i = 0; i < this.nameList.size(); i++) {
            translate(0, guiTextSize * 1.25f);
            if (index == i) {
                drawText(String.format("- %d -: %s", i, nameList.get(i)));
            } else {
                drawText(String.format("%d: %s", i, nameList.get(i)));
            }
        }
        font.setAlign(fontAlign);
        font.setSize(fontSize);
        popMatrix();
        
        /*
         * Skier
         */
        skier = ((ContestantTracking) tracking).firstSkierInRect(0, 0, width, height);
        if (skier == null) {
            skier = new Skier(0, (float) mouseX / width - .5f, (float) mouseY / height - .5f, 10.0f / width - .5f, 10.f / (float) height - .5f, 0, 0, 0, 0);
        }
        
        if (skier.getY() <= NameDraw.FINISH_LINE) {
            drawWithLaser();
        }
    }
    
    protected void drawWithLaser() {
        int fontSize = font.size;
        font.setSize(20);
        beginRaw(laserRenderer);
        stroke(NameDraw.LASER_COLOR);
        noFill();
        if (displayableName != null)
            displayableName.updateAndDraw(skier.getX(), skier.getY());
        endRaw();
        font.setSize(fontSize);
    }
    
    public void keyPressed() {
        super.keyPressed();
        if (key == CODED) {
            if (keyCode == UP) {
                if (index <= 0) {
                    index = nameList.size();
                }
                index--;
            } else if (keyCode == DOWN) {
                index++;
                if (index == nameList.size()) {
                    index = 0;
                }
            }
        } else if (key == RETURN || key == ENTER) {
            System.out.println("New name: " + nameList.get(index) + ".");
            currentName = nameList.get(index);
            displayableName = new DisplayableName(nameList.get(index));
        }
    }
    
    public void drawChar(char c, float x, float y) {
        RGroup myFontGroup = font.toGroup(String.format("%c", c));
        if (myFontGroup.elements != null) {
            for (int t = 0; t < myFontGroup.elements.length; t++) {
                RShape myFontShape = myFontGroup.elements[t].toShape();
                RPath[] myFontPath = myFontShape.paths;
                
                for (int f = 0; f < myFontPath.length; f++) {
                    RPoint[] myFontPoints = myFontPath[f].getHandles();
                    beginShape();
                    for (int p = 1; p < myFontPoints.length - 1; p++) {
                        vertex(myFontPoints[p].x + x, myFontPoints[p].y + y);
                    }
                    endShape();
                }
            }
        }
    }
    
    class DisplayableName {
        protected String name;
        protected char[] chars;
        protected int length;
        protected Motion[] springs;
        protected final static float initialDamping = .9f;
        
        public DisplayableName(String name) {
            this.name = name;
            this.chars = this.name.toCharArray();
            this.length = this.name.length();
            this.springs = new Motion[this.length];
            for (int i = 0; i < this.length; i++) {
                this.springs[i] = new Motion(width / 2f, height / 2f);
                this.springs[i].setDamping(this.generateDamping((float) i / this.length));
            }
        }
        
        public void updateAndDraw(float x, float y) {
            Motion s = null;
            int k = - this.length / 2;
            for (int i = 0; i < this.length; i++) {
                s = this.springs[i];
                k++;
                s.springTo(x + k * font.size / 3 + 1, y);
                s.move();
                drawChar(this.chars[i], s.getX(), s.getY());
            }
        }
        
        private float generateDamping(float position) {
            return - .25f * (position * position) + .249f * position + this.initialDamping;
        }
    }
}
