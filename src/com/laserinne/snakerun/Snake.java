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

package com.laserinne.snakerun;

import com.laserinne.util.Mover;

/**
 * Snake is just a extension to Mover to name things more nicely. This might
 * include further Snake related changes.
 * 
 * @author Jyrki Lilja
 */

public class Snake extends Mover {
    public Snake(float x, float y, int numFollowers) {
        super(x, y);
        this.addFollowers(numFollowers);
    }
    
    public Mover head() {
        return (Mover) this;
    }
}
