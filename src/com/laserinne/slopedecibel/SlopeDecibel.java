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

package com.laserinne.slopedecibel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import processing.core.PApplet;

import com.laserinne.util.LaserinneSketch;

/**
 * Slope Decibel Meter. Gives us some visualization of how loud the contestants
 * shout.
 * 
 * Audio recording and level calculation is from
 * http://stackoverflow.com/q/5800649
 * 
 * @author Jyrki Lilja
 */
public class SlopeDecibel extends LaserinneSketch {
    protected boolean running;
    private AudioFormat format;
    private float level;
    private float max;
    
    private float currentLevelIndicatorPosition = HEIGHT;
    private float maxLevelIndicatorPosition = HEIGHT;
    private float easing = 0.05f;
    private long maxIndicatorUpdateTime;
    private long maxIndicatorPreviousUpdateTime;
    
    private final static int LEVEL_INDICATOR_SIZE = WIDTH / 3;
    private final static float LEVEL_INDICATOR_LEFT = WIDTH / 2 - LEVEL_INDICATOR_SIZE / 2;
    private final static float LEVEL_INDICATOR_RIGHT = WIDTH / 2 + LEVEL_INDICATOR_SIZE / 2;
    private final static long MAX_INDICATOR_TIME = 5000; // 5 seconds
    
    private final static float MAX_8_BITS_SIGNED = Byte.MAX_VALUE;
    private final static float MAX_8_BITS_UNSIGNED = 0xff;
    private final static float MAX_16_BITS_SIGNED = Short.MAX_VALUE;
    private final static float MAX_16_BITS_UNSIGNED = 0xffff;
    
    private static boolean MAX_VALUE_DROPS = false;
    
    /**
     * main
     * 
     * This has to be commented out when running in Processing to allow font
     * loading, but available when running in Eclipse or building with Ant /
     * exporting to application from Processing or building with Ant.
     */
    public static void main(String args[]) {
         PApplet.main(new String[] { com.laserinne.slopedecibel.SlopeDecibel.class.getName() });
    }
    
    public void setup() {
        super.setup();
        
        maxIndicatorPreviousUpdateTime = System.currentTimeMillis();
        
        format = new AudioFormat(24000f, 8, 1, true, true);
        max = 0;
        level = 0;
        
        try {
            DataLine.Info info = new DataLine.Info(
                    TargetDataLine.class, format);
            final TargetDataLine line = (TargetDataLine)
                    AudioSystem.getLine(info);
            line.open(format);
            line.start();
            Runnable runner = new Runnable() {
                int bufferSize = (int) format.getSampleRate()
                        * format.getFrameSize();
                byte[] buffer = new byte[bufferSize];
                
                @Override
                public void run() {
                    running = true;
                    while (running) {
                        int count = line.read(buffer, 0, buffer.length);
                        calculateLevel(buffer, 0, 0);
                        max = Math.max(max, level);
                        System.out.println(level);
                    }
                    line.stop();
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + e);
            System.exit(-2);
        }
    }
    
    public void draw() {
        super.draw();
        
        float dLevelIndicatorPosition = height - level * height - currentLevelIndicatorPosition;
        if (abs(dLevelIndicatorPosition) > 1) {
            currentLevelIndicatorPosition += dLevelIndicatorPosition * easing;
        }
        
        float dMaxIndicatorPosition = height - level * height - maxLevelIndicatorPosition;
        if (MAX_VALUE_DROPS && System.currentTimeMillis() - maxIndicatorPreviousUpdateTime > MAX_INDICATOR_TIME && abs(dMaxIndicatorPosition) > 1) {
            maxIndicatorPreviousUpdateTime = System.currentTimeMillis();
            maxLevelIndicatorPosition = Math.max(currentLevelIndicatorPosition, maxLevelIndicatorPosition);
        } else {
            maxLevelIndicatorPosition = Math.min(currentLevelIndicatorPosition, maxLevelIndicatorPosition);
        }
        
        drawWithLaser();
    }
    
    protected void drawWithLaser() {
        beginRaw(laserRenderer);
        stroke(LASER_COLOR);
        noFill();
        line(LEVEL_INDICATOR_LEFT, currentLevelIndicatorPosition, LEVEL_INDICATOR_RIGHT, currentLevelIndicatorPosition);
        line(LEVEL_INDICATOR_LEFT, maxLevelIndicatorPosition, LEVEL_INDICATOR_RIGHT, maxLevelIndicatorPosition);
        endRaw();
    }
    
    private void calculateLevel(byte[] buffer, int readPoint, int leftOver) {
        float max = 0;
        boolean use16Bit = (format.getSampleSizeInBits() == 16);
        boolean signed = (format.getEncoding() ==
                          AudioFormat.Encoding.PCM_SIGNED);
        boolean bigEndian = (format.isBigEndian());
        if (use16Bit) {
            for (int i=readPoint; i<buffer.length-leftOver; i+=2) {
                int value = 0;
                // deal with endianness
                int hiByte = (bigEndian ? buffer[i] : buffer[i+1]);
                int loByte = (bigEndian ? buffer[i+1] : buffer [i]);
                if (signed) {
                    short shortVal = (short) hiByte;
                    shortVal = (short) ((shortVal << 8) | (byte) loByte);
                    value = shortVal;
                } else {
                    value = (hiByte << 8) | loByte;
                }
                max = Math.max(max, value);
            } // for
        } else {
            // 8 bit - no endianness issues, just sign
            for (int i=readPoint; i<buffer.length-leftOver; i++) {
                int value = 0;
                if (signed) {
                    value = buffer [i];
                } else {
                    short shortVal = 0;
                    shortVal = (short) (shortVal | buffer [i]);
                    value = shortVal;
                }
                max = Math.max (max, value);
            } // for
        } // 8 bit
        // express max as float of 0.0 to 1.0 of max value
        // of 8 or 16 bits (signed or unsigned)
        if (signed) {
            if (use16Bit) { level = (float) max / MAX_16_BITS_SIGNED; }
            else { level = (float) max / MAX_8_BITS_SIGNED; }
        } else {
            if (use16Bit) { level = (float) max / MAX_16_BITS_UNSIGNED; }
            else { level = (float) max / MAX_8_BITS_UNSIGNED; }
        }
    } // calculateLevel
}
