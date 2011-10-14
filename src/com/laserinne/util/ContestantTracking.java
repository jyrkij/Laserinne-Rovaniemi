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

package com.laserinne.util;

import java.util.Collections;

public class ContestantTracking extends Tracking {
    public ContestantTracking() {
        super();
    }
    
    public ContestantTracking(String theAddress, int thePort) {
        super(theAddress, thePort);
    }
    
    public void trackingMessage(float theId, float theX, float theY, float theWidth, float theHeight, float theDeltaX, float theDeltaY, float theAge, float theTimestamp  ) {
        super.trackingMessage(theId, theX, theY, theWidth, theHeight, theDeltaX, theDeltaY, theAge, theTimestamp);
    }
    
    public void update() {
        super.update();
        Collections.sort(skiers, new java.util.Comparator<Skier>() {
            public int compare(Skier o1, Skier o2) {
                return o1.getId() - o2.getId();
            }
        });
    }
    
    public Skier firstSkierInRect(int x0, int y0, int x1, int y1) {
        for (Skier s : skiers) {
            if (s.getX() >= x0 && s.getX() <= x1 && s.getY() >= y0 && s.getY() <= y1) {
//                System.out.printf("Skier#%d is inside rect (%d, %d), (%d, %d)\n", s.getId(), x0, y0, x1, y1);
                return s;
            }
        }
        return null;
    }
}
