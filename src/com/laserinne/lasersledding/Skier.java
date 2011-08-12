/**
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


public class Skier {

	PApplet parent;		// Parent Applet
	PVector location;	// Location of skier
	float time;		 	// Stores the finishing time
	float timeTemp;	 	// Time temp
	int size;		 	// Skier blob size
	
	Skier(PApplet p) {
		parent = p;
		location = new PVector(parent.mouseX, parent.mouseY);
		size = 20;
		time = 0;
		timeTemp = parent.millis();
	}
	
	// Updates the skier position
	public void update(float locX, float locY) {
		location.x = locX;
		location.y = locY;
	}
	
	// Displays the skier
	public void display() {
		parent.ellipse(location.x, location.y, size, size);
	}
	
	// Checks if skier has crossed the finish line. Currently hardcoded to -50 px from height.
	public boolean finished() {
		if(location.y > parent.height-50) {
			return true;
		} else {
			return false;
		}
	}
	
	// Calculates time
	public void calculateTime() {
		time = ( parent.millis()-timeTemp ) / 1000;
	}
}
