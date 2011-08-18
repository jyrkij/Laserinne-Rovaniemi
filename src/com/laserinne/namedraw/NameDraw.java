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

package com.laserinne.namedraw;

import processing.core.*;
import geomerative.*;
import laserschein.*;


@SuppressWarnings("serial")
public class NameDraw extends PApplet {
	
	Laserschein laser;

//	PFont f;  // Global font variable
	float x;  // horizontal location of headline
	float y;
	boolean overLine;
	int index = 0;
	float easing = 0.05f;
	float targetX, targetY;
	RFont font;

	String[] nameList = {
			"TANJA POUTIAINEN", "ANJA PAERSSON", "IHME HIIHTÄJÄs", "HIIHTO PUMMI", "SUKSI SUOHON", "HAVUJA SAATANA"
	};

	public void setup() {
		size(600, 600, P3D);
		frameRate(-1);
		overLine = false;
		laser = new Laserschein(this, Laserschein.EASYLASEUSB2);
		RG.init(this);
		font = new RFont( "Laserfont.ttf", 14, RFont.CENTER);
	}

	public void draw() {
		background(0);
		stroke(255);
		line(0, 500, width, 500);

		
		// Easing
		targetX = mouseX;
		float dx = targetX - x;
		if(abs(dx) > 1)  {
			x += dx * easing;
		}

		targetY = mouseY;
		float dy = targetY - y;
		if(abs(dy) > 1) {
			y += dy * easing;
		} 

		if (mouseY > 500) {
			noStroke();
		}  else {
			stroke(255);
		}
		
		Laser3D renderer = laser.renderer();
		beginRaw(renderer);
		noFill();
		translate(x, y);
		font.draw(nameList[index]);
		endRaw();
		
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
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { NameDraw.class.getCanonicalName() });
	}
}

/*void mouseClicked() {
 println(index);
 index += 1;
 if (x >= 6) {
 index = 0;
 }
 } */
