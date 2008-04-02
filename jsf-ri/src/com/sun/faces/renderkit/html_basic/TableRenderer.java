/*
 * $Id: TableRenderer.java,v 1.2 2003/09/11 15:27:28 craigmcc Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
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

package com.sun.faces.renderkit.html_basic;


import com.sun.faces.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;


/**
 * <p>Render a {@link UIData} component as a two-dimensional table.</p>
 */

public class TableRenderer extends HtmlBasicRenderer {


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

	if ((context == null) || (component == null)) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	if (!component.isRendered()) {
	    return;
	}
	UIData data = (UIData) component;
	data.setRowIndex(0);

	// Render the beginning of the table
	ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", data);
	String styleClass = (String) data.getAttribute("styleClass");
	if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
	}
        // PENDING(craigmcc) - This method has issues with non-String attrs
        //	Util.renderPassThruAttributes(writer, component);
	writer.writeText("\n", null);

	// Render the header facet (if any)
	UIComponent header = (UIComponent) data.getFacets().get("header");
	if (header != null) {
	    String headerClass = (String) data.getAttribute("headerClass");
            writer.startElement("tr", header);
            writer.startElement("td", header);
            writer.writeAttribute("colspan", "" + getColumnCount(data), null);
	    if (headerClass != null) {
                writer.writeAttribute("class", headerClass, "headerClass");
	    }
	    writer.writeText("\n", null);
	    encodeRecursive(context, header);
            writer.endElement("td");
            writer.endElement("tr");
            writer.writeText("\n", null);
	}

    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

	if ((context == null) || (component == null)) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	if (!component.isRendered()) {
	    return;
	}
	UIData data = (UIData) component;

        // Set up variables we will need
        String footerClass = (String) data.getAttribute("footerClass");
        String headerClass = (String) data.getAttribute("headerClass");
        String columnClasses[] = getColumnClasses(data);
        int columnStyle = 0;
        int columnStyles = columnClasses.length;
        String rowClasses[] = getRowClasses(data);
        int rowStyles = rowClasses.length;
        ResponseWriter writer = context.getResponseWriter();
	UIComponent facet = null;
	Iterator kids = null;
	Iterator grandkids = null;

	// Render the table header row
	// PENDING(craigmcc) - what if some or all children do not have one?
        writer.startElement("tr", data);
	if (headerClass != null) {
            writer.writeAttribute("class", headerClass, "headerClass");
	}
	writer.writeText("\n", null);
	kids = data.getChildren().iterator();
	data.setRowIndex(0);
	while (kids.hasNext()) {
	    UIComponent kid = (UIComponent) kids.next();
	    if (!(kid instanceof UIColumn)) {
		continue;
	    }
            writer.startElement("td", kid);
	    facet = (UIComponent) kid.getFacets().get("header");
	    if (facet != null) {
		encodeRecursive(context, facet);
	    }
            writer.endElement("td");
            writer.writeText("\n", null);
	}
        writer.endElement("tr");
        writer.writeText("\n", null);

	// Iterate over the rows of data that are provided
	Map requestMap = context.getExternalContext().getRequestMap();
	String var = data.getVar();
	Object old = null;
	if (var != null) {
	    old = requestMap.get(var);
	}
	int first = data.getFirst(); // One relative
	if (first < 1) {
	    first = 1;
	}
	int rows = data.getRows();
        int rowStyle = 0;
	int rowCount = data.getRowCount();
	int showCount = 0;
        for (int i = first; i <= rowCount; i++) {

	    // Have we displayed the requested number of rows?
	    if ((rows > 0) && (++showCount > rows)) {
		break;
	    }

	    // Expose the current row in the specified request attribute
	    data.setRowIndex(i);
	    Object row = data.getRowData();
	    if (var != null) {
		requestMap.put(var, row);
	    }

	    // Render the beginning of this row
            writer.startElement("tr", data);
	    if (rowStyles > 0) {
                writer.writeAttribute("class", rowClasses[rowStyle++],
                                      "rowClasses");
		if (rowStyle >= rowStyles) {
		    rowStyle = 0;
		}
	    }
	    writer.writeText("\n", null);

	    // Iterate over the child UIColumn components for each row
	    columnStyle = 0;
	    kids = data.getChildren().iterator();
	    while (kids.hasNext()) {

		// Identify the next relevant child component
		UIComponent kid = (UIComponent) kids.next();
		if (!(kid instanceof UIColumn)) {
		    continue;
		}
		UIColumn column = (UIColumn) kid;

		// Render the beginning of this cell
                writer.startElement("td", column);
		if (columnStyles > 0) {
                    writer.writeAttribute("class", columnClasses[columnStyle++],
                                          "columnClasses");
		    if (columnStyle >= columnStyles) {
			columnStyle = 0;
		    }
		}

		// Render the contents of this cell by iterating over
		// the kids of our kids
		grandkids = column.getChildren().iterator();
		while (grandkids.hasNext()) {
		    encodeRecursive(context, (UIComponent) grandkids.next());
		}
		
		// Render the ending of this cell
                writer.endElement("td");
		writer.writeText("\n", null);

	    }

	    // Render the ending of this row
            writer.endElement("tr");
            writer.writeText("\n", null);

	}

	// Clean up after ourselves
	data.setRowIndex(0);
	if (var != null) {
	    if (old != null) {
		requestMap.put(var, old);
	    } else {
		requestMap.remove(var);
	    }
	}

	// Render the table footer row
	// PENDING(craigmcc) - what if some or all children do not have one?
        writer.startElement("tr", data);
	if (footerClass != null) {
            writer.writeAttribute("class", footerClass, "footerClass");
	}
	writer.writeText("\n", null);
	kids = data.getChildren().iterator();
	while (kids.hasNext()) {
	    UIComponent kid = (UIComponent) kids.next();
	    if (!(kid instanceof UIColumn)) {
		continue;
	    }
            writer.startElement("td", kid);
	    facet = (UIComponent) kid.getFacets().get("footer");
	    if (facet != null) {
		encodeRecursive(context, facet);
	    }
            writer.endElement("td");
	    writer.writeText("\n", null);
	}
        writer.endElement("tr");
	writer.writeText("\n", null);

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

	if ((context == null) || (component == null)) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	if (!component.isRendered()) {
	    return;
	}
	UIData data = (UIData) component;
	data.setRowIndex(0);

	// Render the footer facet (if any)
	ResponseWriter writer = context.getResponseWriter();
	UIComponent footer = (UIComponent) data.getFacets().get("footer");
	if (footer != null) {
	    String footerClass = (String) data.getAttribute("footerClass");
            writer.startElement("tr", footer);
            writer.startElement("td", footer);
            writer.writeAttribute("colspan", "" + getColumnCount(data), null);
	    writer.write("\"");
	    if (footerClass != null) {
                writer.writeAttribute("class", footerClass, "footerClass");
	    }
	    writer.writeText("\n", null);
	    encodeRecursive(context, footer);
            writer.endElement("td");
            writer.endElement("tr");
            writer.writeText("\n", null);
	}

	// Render the ending of this table
        writer.endElement("table");
        writer.writeText("\n", null);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Render nested child components by invoking the encode methods
     * on those components.</p>
     */
    private void encodeRecursive(FacesContext context, UIComponent component)
        throws IOException {

        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);
    }


    /**
     * <p>Return an array of stylesheet classes to be applied to
     * each column in the table in the order specified. Every column may or
     * may not have a stylesheet.</p>
     *
     * @param data {@link UIData} component being rendered
     */
    private String[] getColumnClasses(UIData data) {

        String values = (String) data.getAttribute("columnClasses");
        if (values == null) {
            return (new String[0]);
        }
        values = values.trim();
        ArrayList list = new ArrayList();
        while (values.length() > 0) {
            int comma = values.indexOf(",");
            if (comma >= 0) {
                list.add(values.substring(0, comma).trim());
                values = values.substring(comma + 1);
            } else {
                list.add(values.trim());
                values = "";
            }
        }
        String results[] = new String[list.size()];
        return ((String[]) list.toArray(results));

    }


    /**
     * <p>Return the number of child <code>UIColumn</code> components
     * are nested in the specified {@link UIData}.</p>
     *
     * @param data {@link UIData} component being analyzed
     */
    private int getColumnCount(UIData data) {

	int columns = 0;
	Iterator kids = data.getChildren().iterator();
	while (kids.hasNext()) {
	    UIComponent kid = (UIComponent) kids.next();
	    if (kid instanceof UIColumn) {
		columns++;
	    }
	}
	return (columns);

    }


    /**
     * <p>Return an array of stylesheet classes to be applied to
     * each row in the table, in the order specified.  Every row may or 
     * may not have a stylesheet.</p>
     *
     * @param data {@link UIData} component being rendered
     */
    private String[] getRowClasses(UIData data) {

        String values = (String) data.getAttribute("rowClasses");
        if (values == null) {
            return (new String[0]);
        }
        values = values.trim();
        ArrayList list = new ArrayList();
        while (values.length() > 0) {
            int comma = values.indexOf(",");
            if (comma >= 0) {
                list.add(values.substring(0, comma).trim());
                values = values.substring(comma + 1);
            } else {
                list.add(values.trim());
                values = "";
            }
        }
        String results[] = new String[list.size()];
        return ((String[]) list.toArray(results));

    }


}
