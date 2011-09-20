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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class LaserSledding extends com.laserinne.util.TwoPlayerCompetition {

	final int COLLECTIBLE_NUMBER = 6;
	
	ArrayList<Collectible> p1Collectibles, p2Collectibles;	
	int scanSpeed;
    
    public static void main(String args[]) {
        PApplet.main(new String[] { LaserSledding.class.getCanonicalName() });
    }
    
	public void setup() {
		super.setup();
		
		newGame();
	}
	
	public void draw() {
	    super.draw();
	    
		// Checks if leftSkier has crossed the finish line and calculates points
		if(leftSkier.finished()) {
			((LaserSleddingSkierContestant) leftSkier).setScore(COLLECTIBLE_NUMBER - p1Collectibles.size());
		}
		
		// Checks if rightSkierhas crossed the finish line and calculates points
		if(rightSkier.finished()) {
			((LaserSleddingSkierContestant) rightSkier).setScore(COLLECTIBLE_NUMBER - p2Collectibles.size());
		}
		
		// Display Skiers
//		leftSkier.setPosition(mouseX, mouseY);
		leftSkier.draw(g);
//		rightSkier.setPosition(mouseX+width/2, mouseY);
		rightSkier.draw(g);
		
		drawWithLaser();
	}
	
	protected void drawGame() {
        leftSkier.update();
        rightSkier.update();
        
        // Check location and display collectibles
        for(int i = 0; i < p1Collectibles.size(); i++) {
            if(p1Collectibles.get(i).checkLocation((LaserSleddingSkierContestant) leftSkier)) {
                p1Collectibles.remove(i);
            }
            else {
                p1Collectibles.get(i).update(1);
                p1Collectibles.get(i).display();
            }
        }
            
        for(int i = 0; i < p2Collectibles.size(); i++) {
            if(p2Collectibles.get(i).checkLocation((LaserSleddingSkierContestant) rightSkier)) {
                p2Collectibles.remove(i);
            }
            else {
                p2Collectibles.get(i).update(2);
                p2Collectibles.get(i).display();
            }
        }
	}
	
	protected void newGame() {
	    createCollectibles();
        
        sortCollectibles(p1Collectibles);
        sortCollectibles(p2Collectibles);
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
		} else {
		    super.keyPressed();
		}
	}
	
	public void createCollectibles() {
		
		// Allocate memory skiers for collectibles
		leftSkier = new LaserSleddingSkierContestant();
		rightSkier = new LaserSleddingSkierContestant();
		p1Collectibles = new ArrayList<Collectible>();
		p2Collectibles = new ArrayList<Collectible>();

		// Create the collectibles and their positions.
		for(int i = 0; i < COLLECTIBLE_NUMBER; i++) {
			int x = (int)random(width/2);
			int y = (int)this.random(LaserSledding.START_LINE*2f, LaserSledding.FINISH_LINE);
			
			double angle = Math.random() * Math.PI;
			p1Collectibles.add(new Collectible(this, x, y, angle));
			p2Collectibles.add(new Collectible(this, x + width / 2, y, angle));
		}
	}
	
	public void sortCollectibles(ArrayList<Collectible> list) {
		
		Collections.sort(list, new Comparator<Collectible>() {

			@Override
			public int compare(Collectible c1, Collectible c2) {
				return (int)(c1.location.y - c2.location.y);
			}
		});
	}
}
