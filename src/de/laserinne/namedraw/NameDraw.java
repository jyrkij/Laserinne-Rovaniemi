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

package de.laserinne.namedraw;

import geomerative.RGroup;
import geomerative.RPath;
import geomerative.RPoint;
import geomerative.RShape;
import processing.core.PApplet;

import com.laserinne.util.ContestantTracking;
import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Skier;

/**
 * 
 * @author Eero Ervast, Jyrki Lilja and Andreas Schmelas
 */

@SuppressWarnings("serial")
public class NameDraw extends LaserinneSketch {
    protected Spring spring;
    protected float x;
    protected float y;
    protected boolean overLine;
    protected int index = 0;
    protected String[] nameList = {
            "TANJA POUTIAINEN", "ANJA PAERSSON", "IHME HIIHT�J�", "HIIHTO PUMMI", "SUKSI SUOHON", "HAVUJA SAATANA"
    };
    protected Skier skier;
    
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
        spring = new Spring(0.0f, 0.0f, 20, 0.98f, 16.0f, 0.01f);
        //font.setSize(20);
    }
    
    public void draw() {
        super.draw();
        
        line(0, NameDraw.FINISH_LINE, width, NameDraw.FINISH_LINE);
        
        skier = ((ContestantTracking) tracking).firstSkierInRect(0, 0, width, height);
        if (skier == null) {
            skier = new Skier(0, (float) mouseX / width - .5f, (float) mouseY / height - .5f, 10.0f / width - .5f, 10.f / (float) height - .5f, 0, 0, 0, 0);
        }
        
        if (skier.getY() > NameDraw.FINISH_LINE && overLine == false) {
            spring.reset();
            index += 1;
            overLine = true;
            if (index >= nameList.length) {
                index = 0;
            }
        }
        
        if (skier.getY() <= NameDraw.FINISH_LINE && overLine == true) {
            overLine = false;
        }
        
        if (skier.getY() < NameDraw.FINISH_LINE) {
            drawWithLaser();
        }
    }
    
    protected void drawWithLaser() {
        spring.update(skier.getX(), skier.getY());
        spring.follow();
        
        int fontSize = font.size;
        font.setSize(15);
        RGroup myFontGroup = font.toGroup( nameList[index] );
        
        beginRaw(laserRenderer);
        stroke(NameDraw.LASER_COLOR);
        noFill();

        for (int t = 0; t < myFontGroup.elements.length; t++) {
        	RShape myFontShape = myFontGroup.elements[t].toShape();
        	RPath[] myFontPath = myFontShape.paths;
        	
        	for (int f = 0; f < myFontPath.length; f++) {
        		RPoint[] myFontPoints = myFontPath[f].getHandles();
        		beginShape();
    			for (int p = 1; p < myFontPoints.length - 1; p++) {
    				vertex(myFontPoints[p].x, myFontPoints[p].y);
    			}
        		endShape();
        	}
        }	
        endRaw();
        font.setSize(fontSize);
    }
    
    class Spring {
        // Screen values
        float xpos, ypos;
        float tempxpos, tempypos;
        boolean move = true;
        
        // Spring simulation constants
        float mass;       // Mass
        float k = 0.2f;   // Spring constant
        float damp;       // Damping
        float rest_posx;  // Rest position X
        float rest_posy;  // Rest position Y
        
        // Spring simulation variables
        //float pos = 20.0; // Position
        float velx = 0.0f;  // X Velocity
        float vely = 0.0f;  // Y Velocity
        float accel = 0;    // Acceleration
        float force = 0;    // Force
        
        // Constructor
        Spring (float x, float y, int s, float d, float m, float k_in) {
            xpos = tempxpos = x;
            ypos = tempypos = y;
            rest_posx = x;
            rest_posy = y;
            damp = d;
            mass = m;
            k = k_in;
        }
        
        void update(float x, float y) {
            if (move) {
                rest_posy = y;
                rest_posx = x;
            }
            
            force = -k * (tempypos - rest_posy);  // f=-ky
            accel = force / mass;                 // Set the acceleration, f=ma == a=f/m
            vely = damp * (vely + accel);         // Set the velocity
            tempypos = tempypos + vely;           // Updated position
            
            force = -k * (tempxpos - rest_posx);  // f=-ky
            accel = force / mass;                 // Set the acceleration, f=ma == a=f/m
            velx = damp * (velx + accel);         // Set the velocity
            tempxpos = tempxpos + velx;           // Updated position
        }
        
        void follow() {
            translate(tempxpos, tempypos);
            move = true;
        }
        
        void reset() {
            tempxpos = xpos;
            tempypos = ypos;
            rest_posx = xpos;
            rest_posy = ypos;
        }
    }
}
