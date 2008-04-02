/*
 * $Id: ChartServlet.java,v 1.1 2004/03/06 01:58:07 jvisvanathan Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package components.renderkit;

import com.sun.image.codec.jpeg.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import components.model.ChartItem;

public final class ChartServlet extends HttpServlet {

    /**
     * <p>The <code>ServletConfig</code> instance for this servlet.</p>
     */
    private ServletConfig servletConfig = null;


    
    /**
     * <p>Release all resources acquired at startup time.</p>
     */
    public void destroy() {
        servletConfig = null;
    }

    /**
     * <p>Return the <code>ServletConfig</code> instance for this servlet.</p>
     */
    public ServletConfig getServletConfig() {

        return (this.servletConfig);

    }

    /**
     * <p>Return information about this Servlet.</p>
     */
    public String getServletInfo() {

        return (this.getClass().getName());

    }

    /**
     * <p>Perform initialization.</p>
     *
     * @exception ServletException if, for any reason, 
     * bn error occurred during the processing of
     * this <code>init()</code> method.
     */
    public void init(ServletConfig servletConfig) throws ServletException {
	
        // Save our ServletConfig instance
        this.servletConfig = servletConfig;
    }

    /**
     * <p>Process an incoming request, and create the corresponding
     * response.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception ServletException if a servlet error occurs during processing
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {


	// Here's where we'd get the ChartBean from the session and determine
	// whether we're generating a pie chart or bar chart...
	//
        generatePieChart(request, response);

	generateBarChart(request, response);
    }

    /**
     * <p>Process an incoming request, and create the corresponding
     * response.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception ServletException if a servlet error occurs during processing
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
	doGet(request, response);
    }

    /**
     * <p> Generate a bar chart from data.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception ServletException if a servlet error occurs during processing
     */
    private void generateBarChart(HttpServletRequest request,
		                  HttpServletResponse response)
        throws IOException, ServletException {
        
	response.setContentType("image/jpeg");
        HttpSession session = request.getSession(true);
	ChartItem[] chartItems = (ChartItem[])session.getAttribute("chart");
	if (chartItems == null) {
            System.out.println("Could not get data values from session...");
	    throw new ServletException("Could not get data values from session...");
	}
	String widthStr = (String)request.getParameter("width");
	String heightStr = (String) request.getParameter("height");
        String orientation = (String) request.getParameter("orientation");
       
        int width = 400;
        int height = 300;
        if ( orientation == null) {
            orientation = "vertical";
        }
        
        if ( widthStr != null ) {
            width = (new Integer(widthStr)).intValue();
        }
        if ( heightStr != null ) {
            height = (new Integer(heightStr)).intValue();
        }

	String title = null;
	if (title == null) {
	    title = "Bar Chart";
	}

	int barSpacing = 10;
	int barWidth = 0;
	int cx, cy;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();

	// graph dimensions
	Dimension graphDim = new Dimension(width,height);
        Rectangle graphRect = new Rectangle(graphDim);

	// border dimensions
	Dimension borderDim = new Dimension(width-2,height-2);
        Rectangle borderRect = new Rectangle(borderDim);
	
	// background color
	g2d.setColor(Color.white);
	g2d.fill(graphRect);

	// draw border
	g2d.setColor(Color.black);
	borderRect.setLocation(1,1);
        g2d.draw(borderRect);

        Font titleFont = new java.awt.Font("Courier", Font.BOLD, 12);
        FontMetrics titleFontMetrics = g2d.getFontMetrics(titleFont);

	// draw the title centered at the bottom of the bar graph
	int i = titleFontMetrics.stringWidth(title);
	g2d.setFont(titleFont);
	g2d.setColor(Color.black);
	g2d.drawString(title, Math.max((width - i)/2, 0),
	    height - titleFontMetrics.getDescent());

        int scale = 0;
	if (scale == 0) {
	    scale = 10;
	}
        
	int maxLabelWidth = 0;
	int max = 0;
	int columns = chartItems.length;
        for (i=0; i < columns; i++) {
	    ChartItem chartItem = chartItems[i];
	    String label = chartItem.getLabel();
	    int value = chartItem.getValue();
	    String colorStr = chartItem.getColor();
	    if (value > max) {
	        max = value;
	    }

	    maxLabelWidth = Math.max(titleFontMetrics.stringWidth((String)label),
                maxLabelWidth);

	    Object color = null;
	    if (colorStr != null) {
		if (colorStr.equals("red")) {
		    color = Color.red;
		} else if (colorStr.equals("green")) {
		    color = Color.green;
		} else if (colorStr.equals("blue")) {
		    color = Color.blue;
		} else if (colorStr.equals("pink")) {
		    color = Color.pink;
		} else if (colorStr.equals("orange")) {
		    color = Color.orange;
		} else if (colorStr.equals("magenta")) {
		    color = Color.magenta;
		} else if (colorStr.equals("cyan")) {
		    color = Color.cyan;
		} else if (colorStr.equals("white")) {
		    color = Color.white;
		} else if (colorStr.equals("yellow")) {
		    color = Color.yellow;
		} else if (colorStr.equals("gray")) {
		    color = Color.gray;
		} else if (colorStr.equals("darkGray")) {
		    color = Color.darkGray;
		} else {
		    color = Color.gray;
		}
	    } else {
		color = Color.gray;
	    }   

	    if ( orientation.equals("vertical")) {
                barWidth = maxLabelWidth;
		// set the next X coordinate to account for the label
		// being wider than the bar getSize().width.
		cx = (Math.max((barWidth + barSpacing),maxLabelWidth) * i) +
		    barSpacing;

		// center the bar chart
		cx += Math.max((width - (columns * (barWidth + 
		    (2 * barSpacing))))/2,0);
		   
		// set the next Y coordinate to account for the getSize().height
		// of the bar as well as the title and labels painted
		// at the bottom of the chart.
		cy = height - (value * scale) - 1 - (2 * titleFont.getSize());
	
		// draw the label
		g2d.setColor(Color.black);
		g2d.drawString((String)label, cx,
		    height - titleFont.getSize() - titleFontMetrics.getDescent());	

		// draw the shadow bar
		if (color == Color.black) {
		    g2d.setColor(Color.gray);
		}
		g2d.fillRect(cx + 5, cy - 3, barWidth,  (value * scale));
		// draw the bar with the specified color
		g2d.setColor((Color)(color));
                g2d.fillRect(cx, cy, barWidth, (value * scale));
                g2d.drawString("" + value, cx, cy - titleFontMetrics.getDescent());
            } else {
	     
		barWidth = titleFont.getSize();
		// set the Y coordinate
		cy = ((barWidth + barSpacing) * i) + barSpacing;

		// set the X coordinate to be the getSize().width of the widest
		// label
		cx = maxLabelWidth + 1;

		cx += Math.max((width - (maxLabelWidth + 1 +
	            titleFontMetrics.stringWidth("" + max) +
                    (max * scale))) / 2, 0);
		// draw the labels and the shadow
		g2d.setColor(Color.black);
		g2d.drawString((String)label, cx - maxLabelWidth - 1,
			     cy + titleFontMetrics.getAscent());
		if (color == Color.black) {
		    g2d.setColor(Color.gray);
		}
		g2d.fillRect(cx + 3, cy + 5, (value * scale), barWidth);

		// draw the bar in the current color
		g2d.setColor((Color)(color));
                g2d.fillRect(cx, cy, (value * scale), barWidth);
                g2d.drawString("" + value, cx + (value * scale) + 3,
                    cy + titleFontMetrics.getAscent());
            }
		
	}
        OutputStream output = response.getOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
        encoder.encode(bi);
        output.close();
    }

    /**
     * <p> Generate a pie chart from data.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception ServletException if a servlet error occurs during processing
     */
    private void generatePieChart(HttpServletRequest request,
		                  HttpServletResponse response)
        throws IOException, ServletException {

    }
}
