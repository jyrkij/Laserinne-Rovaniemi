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

package com.laserinne.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import oscP5.*;
import netP5.*;

public class Tracking {
  private OscP5 oscP5;

  private HashMap<Integer, Skier> _mySkierTable;
  private ArrayList<Skier> skiers;

  public Tracking() {   
    this( "239.0.0.1", 9999 );
  }


  /* Construct with custom port */
  public Tracking( String theAddress, int thePort) {
    oscP5 = new OscP5(this, theAddress, thePort);
    oscP5.plug(this, "trackingMessage", "/tracking/");

    _mySkierTable = new HashMap<Integer, Skier>();
    skiers = new ArrayList<Skier>();
  }


  /* Update new tracking data every frame */
  public void update() {
    skiers.clear();

    synchronized(_mySkierTable) {

      ArrayList<Integer> myKeysToRemove = new ArrayList<Integer>();

      Collection<Skier> mySkiers = _mySkierTable.values();
      Iterator<Skier> myIterator = mySkiers.iterator();

      while (myIterator.hasNext () ) {
        Skier mySkier = myIterator.next();

        mySkier.update();

        if ( mySkier.isDead() ) {
          System.out.println("## removing skier " + mySkier.getId() );
          myIterator.remove();
        } 
        else {
          skiers.add( mySkier );
        }
      }
    }
  }


  /* receive new tracking message */
  public void trackingMessage(float theId, float theX, float theY, float theWidth, float theHeight, float theDeltaX, float theDeltaY, float theAge, float theTimestamp  ) {

    synchronized( _mySkierTable ) {
      int myId = Math.round(theId);
      //println(myId);

      if ( _mySkierTable.containsKey( myId ) ) {
        Skier mySkier =  _mySkierTable.get( myId);

        //println(myId + "  " + theTimestamp);

        if ( mySkier.getLastTimestamp() < theTimestamp ) {

          mySkier.updateValues(  myId, theX, theY, theWidth, theHeight, theDeltaX, theDeltaY, theAge, theTimestamp);
        } 
        else {
          //println(myId + "  " + theTimestamp);
          //println("old");
        }
      } 
      else {

        Skier mySkier = new Skier(  myId, theX, theY, theWidth, theHeight, theDeltaX, theDeltaY, theAge, theTimestamp); 
        _mySkierTable.put( new Integer(myId), mySkier );
        System.out.println("## adding skier " + mySkier.getId() );
      }
    }
  }


  /* clear all tracked data */
  private void clear() {
    synchronized(_mySkierTable) {
      _mySkierTable.clear();
    }
  }


  /* catch unknown data events */
  private void oscEvent(OscMessage theOscMessage) {
    if (!theOscMessage.isPlugged() ) {
      /* print the address pattern and the typetag of the received OscMessage */
      System.out.print("### received an osc message that i dont know.");
      System.out.print(" addrpattern: "+theOscMessage.addrPattern());
      System.out.println(" typetag: "+theOscMessage.typetag());
    }
  }
}

