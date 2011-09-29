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

import processing.core.PApplet;

import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Tracking;

/**
 * 
 * @author Eero Ervast and Jyrki Lilja
 */

@SuppressWarnings("serial")
public class NameDraw extends LaserinneSketch {
    protected Spring[] spring = new Spring[1]; //I've deleted int num = 1; above so the number of the springs is assigned here without a separate command.
    protected float x;
    protected float y;
    protected boolean overLine;
    protected int index = 0;
    protected String[] nameList = {
            "TANJA POUTIAINEN", "ANJA PAERSSON", "IHME HIIHT€J€", "HIIHTO PUMMI", "SUKSI SUOHON", "HAVUJA SAATANA"
    };
    
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
        tracking = new Tracking();
        overLine = false;
        spring[0] = new Spring(70.0f, 160.0f, 20, 0.98f, 8.0f, 0.1f);
    }
    
    public void draw() {
        super.draw();
        
        line(0, 500, width, 500);
    }
    
    protected void drawWithLaser() {
        for(int i = 0; i < 1; i++)  {
            spring[i].update();
            spring[i].follow();
        }
        
        if (mouseY > 500 && overLine == false) {
            index += 1;
            overLine = true;
            if (index >= 6) {
                index = 0;
            }
        }
        
        if (mouseY <= 500 && overLine == true) {
            overLine = false;
        }
        
        if (mouseY > 500) {
            noStroke();
        } else {
            stroke(NameDraw.LASER_COLOR);
        }
        
        beginRaw(laserRenderer);
        stroke(NameDraw.LASER_COLOR);
        noFill();
        font.draw(nameList[index]);
        endRaw();
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
        
        void update() {
            if (move) {
                rest_posy = mouseY;
                rest_posx = mouseX;
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
    }
}
