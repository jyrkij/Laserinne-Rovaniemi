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

package com.laserinne.lasersledding;

import processing.core.PApplet;
import processing.core.PVector;

public class Collectible {
	PApplet parent;		// Parent Applet
	PVector location; 	// Location of collectible
	int radius; 		// Size of the collectible;
	
	// For calculating the cos wave.
	int i;
	int sc = 150; // amplitude
	float ang1;   // angle
	
	Collectible(PApplet p, int xLoc, int yLoc) {
		parent = p;
		location = new PVector(xLoc, yLoc);
		i = (int)parent.random(405);
		radius = 5;
	}
	
	public void update(int id) {
		
		float x = 0;
		i += 1;

		if(i > 360) {
		    i = 0;
		}
		
		ang1 = PApplet.radians(i);
		
		if(id == 1) {
			location.x = parent.width/2 + (sc * PApplet.cos(ang1));
			x = PApplet.map(location.x, 0, parent.width, 0, parent.width/2);
		}
		
		if(id == 2) {
			location.x = parent.width/2 + (sc * PApplet.cos(ang1));
			x = PApplet.map(location.x, 0, parent.width, parent.width/2, parent.width);
		}
		location.x = x;
	}
	
		// Draws the collectible 
	public void display() {		
		parent.beginShape();
			parent.vertex(location.x, location.y);
			parent.vertex(location.x+10, location.y);
		parent.endShape();
	}
	
	// Checks the distance from collectible to skier
	public boolean checkLocation(LaserSleddingSkierContestant s) {
		if( s.closeTo(this.location, radius*2) ) {
			return true;
		}
			return false;
	}
	
	
}
