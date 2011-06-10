/*
 * @(#)Blink.java	1.7 00/08/10
 *
 * Copyright (c) 1997, 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

/**
 * I love blinking things.
 *
 * @author Arthur van Hoff
 * @modified 04/24/96 Jim Hagen use getBackground
 * @modified 02/05/98 Mike McCloskey removed use of deprecated methods
 * @modified 04/23/99 Josh Bloch, use timer instead of explicit multithreading.
 * @modified 07/10/00 Daniel Peek brought to code conventions, minor changes
 */

import java.awt.*;
import java.util.*;

public class Blink extends java.applet.Applet {
    private Timer timer;              // Schedules the blinking
    private String labelString;       // The label for the window
    private int delay;                // the delay time between blinks

    public void init() {
        String blinkFrequency = getParameter("speed");
        delay = (blinkFrequency == null) ? 400 :
            (1000 / Integer.parseInt(blinkFrequency));
        labelString = getParameter("lbl");
        if (labelString == null)
            labelString = "Blink";
        Font font = new java.awt.Font("TimesRoman", Font.PLAIN, 24);
        setFont(font);
    }

    public void start() {
        timer = new Timer();     //creates a new timer to schedule the blinking
        timer.schedule(new TimerTask() {      //creates a timertask to schedule
            // overrides the run method to provide functionality 
            public void run() {  
                repaint();
            }
        }
            , delay, delay);
    }

    public void paint(Graphics g) {
        int fontSize = g.getFont().getSize();
        int x = 0, y = fontSize, space;
        int red =   (int) ( 50 * Math.random());
        int green = (int) ( 50 * Math.random());
        int blue =  (int) (256 * Math.random());
        Dimension d = getSize();
        g.setColor(Color.black);
        FontMetrics fm = g.getFontMetrics();
        space = fm.stringWidth(" ");
        for (StringTokenizer t = new StringTokenizer(labelString); 
             t.hasMoreTokens();) {
            String word = t.nextToken();
            int w = fm.stringWidth(word) + space;
            if (x + w > d.width) {
                x = 0;
                y += fontSize;  //move word to next line if it doesn't fit
            }
            if (Math.random() < 0.5)
                g.setColor(new java.awt.Color((red + y*30) % 256, 
                                              (green + x/3) % 256, blue));
            else
                g.setColor(getBackground());
            g.drawString(word, x, y);
            x += w;  //shift to the right to draw the next word
        }
    }
    
    public void stop() {
        timer.cancel();  //stops the timer
    }

    public String getAppletInfo() {
        return "Title: Blinker\n"
            + "Author: Arthur van Hoff\n" 
            + "Displays multicolored blinking text.";
    }
    
    public String[][] getParameterInfo() {
        String pinfo[][] = {
            {"speed", "string", "The blink frequency"},
            {"lbl", "string", "The text to blink."},
        };
        return pinfo;
    }
}
