/*
 * $Id: ListRenderer.java,v 1.11 2003/08/08 16:20:22 rkitain Exp $
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
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.mozilla.util.Assert;

/**
 *
 *  Render a <code>UIPanel</code> component in the proposed "List" style.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ListRenderer.java,v 1.11 2003/08/08 16:20:22 rkitain Exp $
 *  
 */

public class ListRenderer extends HtmlBasicRenderer {
    
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

    public ListRenderer() {
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

        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );   

        String panelClass = (String) component.getAttribute("panelClass");
        
        // Render the beginning of this panel
	writer.startElement("table");
        if (panelClass != null) {
	    writer.writeAttribute("class", panelClass);
        }
	Util.renderPassThruAttributes(writer, component);
	writer.writeText('\n');
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

        // Set up variables we will need
        // PENDING (visvan) is it possible to use hardcoded column headings
        // without using stylesheets ?
        String footerClass = (String) component.getAttribute("footerClass");
        String headerClass = (String) component.getAttribute("headerClass");
        String columnClasses[] = getColumnClasses(component);
        int columnStyle = 0;
        int columnStyles = columnClasses.length;
        String rowClasses[] = getRowClasses(component);
        int rowStyle = 0;
        int rowStyles = rowClasses.length;
	UIComponent facet = null;
	Iterator kids = null;

        // Process the table header (if any)
        if (null != (facet = component.getFacet("header"))) {
	    writer.startElement("tr");
	    writer.writeText('\n');
	    // If the header has kids, render them recursively
	    if (null != (kids = facet.getChildren())) {
		while (kids.hasNext()) {
		    UIComponent kid = (UIComponent) kids.next();
		    // write out the table header
		    if (null != headerClass) {
			writer.startElement("th");
			writer.writeAttribute("class", headerClass);
		    }
		    else {
			writer.startElement("th");
			writer.writeText('\n');
		    }
		    // encode the children
		    encodeRecursive(context, kid);
		    // write out the table footer
		    writer.endElement("th");
		    writer.writeText('\n');
		}
	    }
	    else {
		// if the header has no kids, just render it
		facet.encodeBegin(context);
		if (facet.getRendersChildren()) {
		    facet.encodeChildren(context);
		}
		facet.encodeEnd(context);
            }
	    writer.endElement("tr");
	    writer.writeText('\n');
        }

	if (null != (kids = component.getChildren())) {
	    // Process each grouping of data items to be processed
            Map requestMap = context.getExternalContext().getRequestMap();
	    while (kids.hasNext()) {
		UIComponent group = (UIComponent) kids.next();
		String var = (String) group.getAttribute("var");
            
		Iterator rows = getIterator(context, group);
		while (rows.hasNext()) {
		    
		    // Start the next row to be rendered
		    Object row = rows.next(); // Model data from the list
		    if (var != null) {
			// set model bean in request scope. nested components
			// will use this to get their values.
			requestMap.put(var, row);
		    }
		    writer.startElement("tr");
		    if (rowStyles > 0) {
			writer.writeAttribute("class", rowClasses);
			if (rowStyle >= rowStyles) {
			    rowStyle = 0;
			}
		    }
		    writer.writeText('\n');
		    
		    // Process each column to be rendered
		    columnStyle = 0;
		    Iterator columns = group.getChildren();
		    // number of columns will equal the total number of elements
		    // in the iterator. No of rows will be equal to the number of
		    // rows in the list bean.
		    while (columns.hasNext()) {
			UIComponent column = (UIComponent) columns.next();
			writer.startElement("td");
			if (columnStyles > 0) {
			    writer.writeAttribute("class", columnClasses[columnStyle++]);
			    if (columnStyle >= columnStyles) {
				columnStyle = 0;
			    }
			}
			encodeRecursive(context, column);
			writer.endElement("td");
			writer.writeText('\n');
		    }
		    
		    // Finish the row that was just rendered
		    writer.endElement("tr");
		    writer.writeText('\n');
		    if (var != null) {
			requestMap.remove(var);
		    }
		}
            }
        }

        // Process the table footer (if any)
        if (null != (facet = component.getFacet("footer"))) {
	    writer.startElement("tr");
	    writer.writeText('\n');
	    // If the footer has kids, render them recursively
	    if (null != (kids = facet.getChildren())) {
		while (kids.hasNext()) {
		    UIComponent kid = (UIComponent) kids.next();
		    // write out the table footer
		    if (null != footerClass) {
			writer.startElement("td");
			writer.writeAttribute("class", footerClass);
		    }
		    else {
			writer.startElement("th");
	                writer.writeText('\n');
		    }
		    // encode the children
		    encodeRecursive(context, kid);
		    // write out the table footer
		    writer.endElement("th");
	            writer.writeText('\n');
		}
	    }
	    else {
		// if the footer has no kids, just render it
		facet.encodeBegin(context);
		if (facet.getRendersChildren()) {
		    facet.encodeChildren(context);
		}
		facet.encodeEnd(context);
            }
	    writer.endElement("tr");
	    writer.writeText('\n');
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        // Render the ending of this panel
        ResponseWriter writer = context.getResponseWriter();
	writer.endElement("table");
	writer.writeText('\n');
    }


    /**
     * Renders nested children of panel by invoking the encode methods
     * on the components. This handles components nested inside
     * panel_data, panel_group tags.
     */
    private void encodeRecursive(FacesContext context, UIComponent component)
        throws IOException {
        
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren();
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
     * Returns an iterator over data collection to be rendered. Each item
     * in the iterator will correspond to a row in the list.
     */
    private Iterator getIterator(FacesContext context, UIComponent component) {

        // Process the current value of this component appropriately
        Object value = ((UIPanel)component).currentValue(context);
        if (value == null) {
            return (Collections.EMPTY_LIST.iterator());
        } else if (value instanceof Collection) {
            return (((Collection) value).iterator());
        } else if (value instanceof Iterator) {
            return ((Iterator) value);
        } else if (value instanceof Map) {
            return (((Map) value).entrySet().iterator());
        } else if (value.getClass().isArray()) {
            try {
                // Array of objects does not require copying
                return (Arrays.asList((Object[]) value).iterator());
            } catch (ClassCastException e) {
                // Make a copy for arrays of primitives
                int length = Array.getLength(value);
                ArrayList list = new ArrayList(length);
                for (int i = 0; i < length; i++) {
                    list.add(Array.get(value, i));
                }
                return (list.iterator());
            }
        } else {
            // Create a single-item list
            ArrayList list = new ArrayList(1);
            list.add(value);
            return (list.iterator());
        }
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
