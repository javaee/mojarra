/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
