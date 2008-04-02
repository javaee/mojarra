/*
 * $Id: GridRenderer.java,v 1.17 2003/09/04 19:52:16 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit.html_basic;


import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;

/**
 *
 *  Render a <code>UIPanel</code> component in the proposed "Grid" style.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: GridRenderer.java,v 1.17 2003/09/04 19:52:16 rkitain Exp $
 *  
 */

public class GridRenderer extends HtmlBasicRenderer {
    
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public GridRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        String styleClass = (String) component.getAttribute("styleClass");
        
        // Render the beginning of this panel
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", component);
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        Util.renderPassThruAttributes(writer, component);

	writer.writeText("\n", null);
    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        String footerClass = (String) component.getAttribute("footerClass");
        String headerClass = (String) component.getAttribute("headerClass");
        int columns = getColumns(component);
        String columnClasses[] = getColumnClasses(component);
        int columnStyle = 0;
        int columnStyles = columnClasses.length;
        String rowClasses[] = getRowClasses(component);
        int rowStyle = 0;
        int rowStyles = rowClasses.length;
        boolean open = false;
	UIComponent facet = null;
	Iterator kids = null;

	if (null != (facet = (UIComponent) component.getFacets().get("header"))) {
	    
	    if (headerClass != null) {
		writer.startElement("tr", facet);
		writer.startElement("th", facet);
		writer.writeAttribute("class", headerClass, "headerClass");
	    } else {
                writer.startElement("tr", facet);
                writer.startElement("th", facet);
	    }
	    writer.writeAttribute("colspan", new Integer(columns), "columns");
	    writer.startElement("thead", facet);

	    encodeRecursive(context, facet);
	    writer.endElement("thead");
            writer.endElement("th");
            writer.endElement("tr");
	    writer.writeText("\n", null);
	}

	int i = 0;
	writer.startElement("tbody", component);
	if (null != (kids = component.getChildren().iterator())) {
	    while (kids.hasNext()) {
		if ((i % columns) == 0) {
		    if (open) {
			writer.endElement("tr");
			writer.writeText("\n", null);
			open = false;
		    }
		    writer.startElement("tr", component);
		    if (rowStyles > 0) {
			writer.writeAttribute("class", rowClasses[rowStyle++], "rowClasses");
			if (rowStyle >= rowStyles) {
			    rowStyle = 0;
			}
		    }
                    writer.writeText("\n", null);
		    open = true;
		    columnStyle = 0;
		}
		writer.startElement("td", component);
		if (columnStyles > 0) {
		    writer.writeAttribute("class", columnClasses[columnStyle++], "columns");
		    if (columnStyle >= columnStyles) {
			columnStyle = 0;
		    }
		}
		UIComponent child = (UIComponent) kids.next();
		encodeRecursive(context, child);
		writer.endElement("td");
		writer.writeText("\n", null);
		i++;
	    }
	}
        if (open) {
            writer.endElement("tr");
	    writer.writeText("\n", null);
        }
	writer.endElement("tbody");

	if (null != (facet = (UIComponent) component.getFacets().get("footer"))) {

	    if (footerClass != null) {
		writer.startElement("tr", facet);
		writer.startElement("th", facet);
		writer.writeAttribute("class", footerClass, "footerClass");
	    } else {
		writer.startElement("tr", facet);
		writer.startElement("th", facet);
	    }
	    
	    writer.writeAttribute("colspan", new Integer(columns), "columns");
	    writer.startElement("tfoot", facet);

            encodeRecursive(context, facet);
	    writer.endElement("tfoot");
	    writer.endElement("th");
	    writer.endElement("tr");
	    writer.writeText("\n", null);
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        // Render the ending of this panel
        ResponseWriter writer = context.getResponseWriter();
	writer.endElement("table");
	writer.writeText("\n", null);
    }

    /**
     * Renders nested children of panel by invoking the encode methods
     * on the components. This handles components nested inside
     * panel_group, panel_grid tags.
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
    * Returns an array of stylesheet classes to be applied to
    * each column in the list in the order specified. Every column may or
    * may not have a stylesheet
    */
    private String[] getColumnClasses(UIComponent component) {
        String values = (String) component.getAttribute("columnClasses");
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
     * Returns number of columns of the grid converting the value
     * specified to int if necessary.
     */
    private int getColumns(UIComponent component) {
        int count;
        Object value = component.getAttribute("columns");
        if ((value != null) && (value instanceof Integer)) {
            count = ((Integer) value).intValue();
        } else {
            count = 2;
        }
        if (count < 1) {
            count = 1;
        }
        return (count);
    }

   /**
    * Returns an array of stylesheet classes to be applied to
    * each row in the list in the order specified. Every row may or 
    * may not have a stylesheet
    */
    private String[] getRowClasses(UIComponent component) {
        String values = (String) component.getAttribute("rowClasses");
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
