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

import com.laserinne.util.SkierContestant;

public class LaserSleddingSkierContestant extends SkierContestant {
	
	private float score;
	
	public LaserSleddingSkierContestant() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public float scoreToSeconds() {
		return score * -0.25f;
	}

	@Override
	public void updateScore() {
		// TODO Auto-generated method stub

	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
}
