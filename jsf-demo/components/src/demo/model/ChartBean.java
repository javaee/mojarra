/*
 * $Id: ChartBean.java,v 1.2 2004/05/12 18:46:48 ofung Exp $
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

package demo.model;

import java.util.ArrayList;
import java.util.Collection;

import components.model.ChartItem;
public class ChartBean {

    // Bar Chart Properties -------------------------
    
    public static final int	VERTICAL = 0;
    public static final int 	HORIZONTAL = 1;

    private int orientation = VERTICAL;
    public int getOrientation() {
        return orientation;
    }
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    
    // ----------------------------------------------
    
    private int columns = 0;
    public int getColumns() {
        return columns;
    }
    public void setColumns(int columns) {
        this.columns = columns;
    }

    private ArrayList chartItems = null;
    public Collection getChartItems() {
        return chartItems;
    }

    private String title = null;
    public String getTitle() {
        return title;
    }
    public void setTitle() {
        this.title = title;
    }

    private int scale = 10;
    public int getScale() {
        return scale;
    }
    public void setScale(int scale) {
        this.scale = scale;
    }

    private int width = 400;
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    private int height = 300;
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height= height;
    }

    public ChartBean() {

	setWidth(400);
	setHeight(300);
	setColumns(2);
	setOrientation(ChartBean.HORIZONTAL);

        chartItems = new ArrayList(columns);
	chartItems.add(new ChartItem("one", 10, "red"));
	chartItems.add(new ChartItem("two", 20, "blue"));

    }
}
