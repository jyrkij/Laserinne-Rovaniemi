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
import java.util.Collections;
import java.util.Comparator;

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
		size(640,480,P3D);
		frameRate(60);
		
		// Allocate memory for skier and collectible
		sk1 = new LaserSleddingSkierContestant(mouseX, mouseY, 10, 10);
		sk2 = new LaserSleddingSkierContestant(mouseX+300, mouseY, 10, 10);
		p1Collectibles = new ArrayList<Collectible>();
		p2Collectibles = new ArrayList<Collectible>();
		pointsP1 = 0;
		pointsP2 = 0;
		
		// Finishline
		LaserSleddingSkierContestant.finishLine(height-50);
		
		// Create the collectibles and their positions.
		for(int i = 0; i < COLLECTIBLE_NUMBER; i++) {
			int x = (int)random(width/2);
			int y = (int)random(height-50);
			
			p1Collectibles.add(new Collectible(this, x, y));
			p2Collectibles.add(new Collectible(this, x+width/2, y));
			
		}
		
		// TODO: Implement interface for sorting
		// Sort p1Collectibles
		Collections.sort(p1Collectibles, new Comparator<Collectible>() {

			@Override
			public int compare(Collectible c1, Collectible c2) {
				return (int)(c1.location.y - c2.location.y);
			}
		});
		
		// Sort p2Collectibles
		Collections.sort(p2Collectibles, new Comparator<Collectible>() {
			
			@Override
			public int compare(Collectible c1, Collectible c2) {
				return (int)(c1.location.y - c2.location.y);
			}
		});
		
		// Initialize laserschein
		laserschein = new Laserschein(this, Laserschein.EASYLASEUSB2);
		scanSpeed = 17000;
		laser = laserschein.renderer();
		laser.noSmooth();
		
		// Text 
		RG.init(this);
		font = new RFont( "Laserfont.ttf", 32, RFont.CENTER);
		
		sk1.start();
		sk2.start();
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
		for(int i = 0; i < p1Collectibles.size(); i++) {
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
		
		endRaw();
		
		// Checks if skier1 has crossed the finish line and calculates points
		if(sk1.finished() && pointsP1 == 0) {
			sk1.setScore(COLLECTIBLE_NUMBER - p1Collectibles.size());
		}
		
		// Checks if skier2 has crossed the finish line and calculates points
		if(sk2.finished() && pointsP2 == 0) {
			sk2.setScore(COLLECTIBLE_NUMBER - p2Collectibles.size());
		}
		
		// End the round if both of the skiers has finished
		if(sk1.finished() && sk2.finished()) {
			delay(100);
		    noLoop();
			background(0);
			pushMatrix();
			translate(width/2, height/2);
		    beginRaw(laser);
		    	font.draw(LaserSleddingSkierContestant.winner(sk1, sk2));
		    endRaw();
		    popMatrix();
		}
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
			
			// Reset the skiers
			sk1.reset();
			sk2.reset();
			
			// Start skiers
			sk1.start();
			sk2.start();
			loop();
		}
	}
}
