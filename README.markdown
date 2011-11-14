# Laserinne Sketches

The master repository for these sketches is
(https://github.com/jyrkij/Laserinne-Rovaniemi).

These sketches are originally for Levi World Cup 2011, which never was held.
Most of them are requested by Levi. Their purpose is to show off how people
could interact with laser scanner(s) and how to do it safely. The basic
idea is to track skiers and project something around them.

All the sketches are built on top of [Processing](http://processing.org/). For projection we use
[Laserchein](https://github.com/allesblinkt/Laserschein) by allesblinkt.
For tracking we use
[laserinneTSPS](https://github.com/m9dfukc/laserinneTSPS) by Andreas
Schmelas.

## 1 Sketches

All the sketches except Slopedecibel interact with skiers. Competitive
sketches (LaserSledding and SnakeRun) show the winner based on time and
points.

### 1.1 LaserSledding

LaserSledding gives us a competition between two players. Their aim is to
collect all the items projected on the slope. By collecting an item they
receive bonus as reduction of time.

### 1.2 NameDraw

NameDraw projects names behind skiers. This was meant to be used for
drawing the top 10 skiers.

### 1.3 SlopeDecibel

SlopeDecibel shows relative amount of gain the microphone receives and
keeps the highest.

### 1.4 SnakeRun

SnakeRun projects two snakes in which the competitors must stay in. If they
fail to stay in, there is a time added to their total time.

## 2 Key Commands

All the sketches support following keys:

 - ''r'' as reset. Resets all to defaults.
 - ''s'' as show calibration. Shows Laserschein's calibration window.

In addition to these, competitions support ''n'' as new game. It generates
new routes and resets positions.

## 3 Class description and hierarchy

There are a few supporting classes in com.laserinne.util. They should have
comments that should explain the functionality. By looking at the sketches
we've implemented you should be able to understand how they work.

### 3.1 General class organization

All the sketches have their own namespace. If there's a class that only one
sketch uses, put it into sketch's namespace. If there's a possibility that
multiple sketches use the same class, put it in com.laserinne.util.

All sketches *must* extend com.laserinne.util.LaserinneSketch. If the
sketch is a competition, it *must* extend
com.laserinne.util.TwoPlayerCompetition.

### 3.2 Class hierarchy

    |- PApplet
       |- LaserinneSketch
          |- SlopeDecibel
          |- NameDraw
          |- TwoPlayerCompetition
             |- LaserSledding
             |- SnakeRun

#### 3.2.1 LaserinneSketch

Abstract superclass for Laserinne sketches.

You are supposed to extend this to get some of the ready made functionality
for sketches made to Levi.

Remember: When you've overridden a method you almost always want to call the
superclass method as the first call in your method! This gives you the
benefits of having the functionality that's already there in your sketch as
well.

#### 3.2.2 TwoPlayerCompetition

Abstract superclass for games/competitions for two players.

You are supposed to extend this to get some of the ready made functionality
for competitions/games for two players.

Remember: When you've overridden a method you almost always want to call the
superclass method as the first call in your method! This gives you the
benefits of having the functionality that's already there in your sketch as
well.

