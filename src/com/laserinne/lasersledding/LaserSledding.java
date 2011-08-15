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

import java.util.ArrayList;

import processing.core.PApplet;
import geomerative.*;
import laserschein.*;

@SuppressWarnings("serial")
public class LaserSledding extends PApplet {

	final int COLLECTIBLE_NUMBER = 5;
	
	LaserSleddingSkierContestant sk1, sk2;
	ArrayList<Collectible> p1Collectibles, p2Collectibles;
	int pointsP1, pointsP2;
	
	Laserschein laserschein;
	Laser3D laser;
	int scanSpeed;
	
	RFont font;
	
	public void setup() {
		size(600,600,P3D);
		frameRate(100);
		
		// Allocate memory for skier and collectible
		sk1 = new LaserSleddingSkierContestant(mouseX, mouseY);
		sk2 = new LaserSleddingSkierContestant(mouseX+300, mouseY);
		p1Collectibles = new ArrayList<Collectible>();
		p2Collectibles = new ArrayList<Collectible>();
		pointsP1 = 0;
		pointsP2 = 0;
		
		
		// Create the collectibles
		for(int i = 0; i < COLLECTIBLE_NUMBER; i++) {
			int x = (int)random(width/2);
			int y = (int)random(height-50);
			
			p1Collectibles.add(new Collectible(this, x, y));
			p2Collectibles.add(new Collectible(this, x+width/2, y));
		}
		
		
		// Initialize laserschein
		laserschein = new Laserschein(this, Laserschein.EASYLASEUSB2);
		scanSpeed = 20000;
		
		laser = laserschein.renderer();
		laser.noSmooth();
		
		// Text 
		RG.init(this);
		font = new RFont( "Laserfont.ttf", 20, RFont.CENTER);
	}
	
	public void draw() {
		laserschein.output().setScanSpeed(scanSpeed);
		background(0);
		stroke(255,0,0);
		noFill();
	
		beginRaw(laser);
		
		// Display Skiers
		sk1.setPosition(mouseX, mouseY);
		sk1.draw(g);
		sk2.setPosition(mouseX+width/2, mouseY);
		sk2.draw(g);
		
		// Check location and display collectibles
		/*for(int i = 0; i < p1Collectibles.size(); i++) {
			if(p1Collectibles.get(i).checkLocation(sk1)) {
				p1Collectibles.remove(i);
			}
			else {
				p1Collectibles.get(i).update(1);
				p1Collectibles.get(i).display();
			}
		}
			
		for(int i = 0; i < p2Collectibles.size(); i++) {
			if(p2Collectibles.get(i).checkLocation(sk2)) {
				p2Collectibles.remove(i);
			}
			else {
				p2Collectibles.get(i).update(2);
				p2Collectibles.get(i).display();
			}
		}
		
		// Draw track lines
		//line(width/2, 0, width/2, height);
		//line(0,height-50, width, height-50);
		
		endRaw();
		
		// Checks if skier1 has crossed the finish line and calculates points
		if(sk1.finished() && pointsP1 == 0) {
			
			pointsP1 = COLLECTIBLE_NUMBER - p1Collectibles.size();
			
			if(sk1.time == 0) {
				sk1.calculateTime();
				System.out.println("P1 time: " + sk1.time);
				System.out.println("P1 points: " + pointsP1);
			}
		}
		
		// Checks if skier2 has crossed the finish line and calculates points
		if(sk2.finished() && pointsP2 == 0) {
			
			pointsP2 = COLLECTIBLE_NUMBER - p2Collectibles.size();
			
			sk2.calculateTime();
			System.out.println("P2 time: " + sk2.time);
			System.out.println("P2 points: " + pointsP2);
			
		}
		
		// End the round if both of the skiers has finished
		if(sk1.finished() && sk2.finished()) {
			
		    noLoop();

			background(0);
			pushMatrix();
			translate((float)(width/2),(float)(height/2.0+font.size/3.0));
		    beginRaw(laser);
		    delay(500);
		    
		    if(pointsP1 > pointsP2) {
		    	font.draw("Player 1 WINS!");
		    }
		    else if(pointsP2 > pointsP1) {
		    	font.draw("Player 2 WINS!");
		    }
		    else {
		    	font.draw("Draw!");
		    }
		    popMatrix();
		    endRaw();
		}
	*/
	}
	public static void main(String args[]) {
		PApplet.main(new String[] { LaserSledding.class.getCanonicalName() });
	}
	
public void keyPressed() {
		
		if(key == CODED) {
			if(keyCode == UP) {
				scanSpeed += 1000;
				System.out.println("Scanspeed: " + scanSpeed);
			}
			if(keyCode == DOWN) {
				scanSpeed -= 1000;
				System.out.println("Scanspeed: " + scanSpeed);
			}
		}
		
		if(key == 'n') {
			// Allocate memory for skier and collectible
		//	sk1 = new Skier(this);
		//	sk2 = new Skier(this);
			p1Collectibles = new ArrayList<Collectible>();
			p2Collectibles = new ArrayList<Collectible>();
			pointsP1 = 0;
			pointsP2 = 0;
			
			// Create the collectibles
			for(int i = 0; i < COLLECTIBLE_NUMBER; i++) {
				int x = (int)random(300);
				int y = (int)random(height-50);
				
				p1Collectibles.add(new Collectible(this, x, y));
				p2Collectibles.add(new Collectible(this, x+width/2, y));
			}
			loop();
		}
	}
}
