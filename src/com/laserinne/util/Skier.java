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

import java.util.ArrayList;
import java.util.Random;

import processing.core.PVector;

public class Skier {
  private int id;

  private ArrayList<PVector> historyList;

  private PVector[] history = new PVector[100];

  private int historyPointer  = 0;

  private PVector position;
  private PVector previousPosition;

  private float dx;
  private float dy;

  private float age;

  private float w;
  private float h;

  private int c;

  public PVector direction;




  private float lastTimestamp;
  private long lastUpdate;
  
  private static int width;
  private static int height;




  public Skier( int theId, float theX, float theY, float theWidth, float theHeight, float theDeltaX, float theDeltaY, float theAge, float theTimestamp  ) {
    setId(theId); 


    historyList = new ArrayList<PVector>();
    position = new PVector();


    direction = new PVector(0, 0);
    previousPosition = new PVector();

    position.x = mapX(theX);
    position.y = mapY(theY);

    previousPosition.x = mapX(theX);
    previousPosition.y = mapY(theY);

    dx = mapX(theDeltaX);
    dy = mapY(theDeltaY);

    w= mapX(theWidth);
    h= mapY(theHeight);

    age= theAge;

    setLastTimestamp(theTimestamp);
    lastUpdate = System.currentTimeMillis();

    Random r = new Random();
    c = 0xFF000000 | r.nextInt(256) << 16 | r.nextInt(256) << 8 | r.nextInt(256);
  }


  public void updateValues( int theId, float theX, float theY, float theWidth, float theHeight, float theDeltaX, float theDeltaY, float theAge, float theTimestamp  ) {
    setId(theId); 




    history[historyPointer] = new PVector(position.x, position.y);

    historyPointer++;

    if (historyPointer >= history.length ) historyPointer = 0;

    position.x = mapX(theX);
    position.y = mapX(theY);

    dx = mapX(theDeltaX);
    dy = mapY(theDeltaY);

    w= mapX(theWidth);
    h= mapY(theHeight);

    age= theAge;

    setLastTimestamp(theTimestamp);
    lastUpdate = System.currentTimeMillis();
  }


  public void update() {

    historyList.clear();

    for (int i = 0; i < history.length; i++) {
      int myPosition = i + historyPointer;

      if ( myPosition >=  history.length) myPosition -= history.length;

      if ( history[ myPosition ] != null) {
        historyList.add( history[ myPosition ] );
      }
    }

    float myLearningRate = 0.001f;
    PVector myDirection = new PVector(previousPosition.x, previousPosition.y );
    myDirection.sub(position);

    if ( myDirection.mag() > 0) {

      direction.x = ((1-myLearningRate) * direction.x) + ( myLearningRate * myDirection.x);
      direction.y = ((1-myLearningRate) * direction.y) + ( myLearningRate * myDirection.y);
    }


    previousPosition.x = position.x;
    previousPosition.y = position.y;
  }







  public boolean isDead() {
    if (System.currentTimeMillis() - lastUpdate > 200) {
      return true;
    } 

    return false;
  }


  private float mapX( float theValue ) {
    return map(theValue, -0.5f, 0.5f, 0, width);
  }


  private float mapY( float theValue ) {
    return map(theValue, -0.5f, 0.5f, 0, height);
  }
  
  static private final float map(float value,
      float istart, float istop,
      float ostart, float ostop) {
      return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }
    
    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public int getId() {
        return id;
    }
    
    private void setId(int id) {
        this.id = id;
    }
    
    public float getLastTimestamp() {
        return lastTimestamp;
    }
    
    private void setLastTimestamp(float lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }
    
    public static final void width(int width) {
        Skier.width = width;
    }
    
    public static final void height(int height) {
        Skier.height = height;
    }
}
