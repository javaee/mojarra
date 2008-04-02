/*
 * $Id: GridRenderer.java,v 1.33 2004/03/31 18:48:35 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit.html_basic;


import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <B>GridRenderer</B> is a class that renders <code>UIPanel</code> component
 * as a "Grid".
 */

public class GridRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(GridRenderer.class);
    
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

    public boolean getRendersChildren() {
        return true;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " +
                      component.getId());
        }
        
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component "
                          + component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        
        // Render the beginning of this panel
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", component);
        writeIdAttributeIfNecessary(context, writer, component);
        String styleClass =
            (String) component.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        Util.renderPassThruAttributes(writer, component);
        writer.writeText("\n", null);

        // Render the header facet (if any)
        UIComponent header = getFacet(component, "header");
        String headerClass =
            (String) component.getAttributes().get("headerClass");
        if (header != null) {
            writer.startElement("thead", component);
            writer.writeText("\n", null);
            writer.startElement("tr", header);
            writer.startElement("th", header);
            if (headerClass != null) {
                writer.writeAttribute("class", headerClass, "headerClass");
            }
            writer.writeAttribute("colspan", "" + getColumnCount(component),
                                  null);
            writer.writeAttribute("scope", "colgroup", null);
            encodeRecursive(context, header);
            writer.endElement("th");
            writer.endElement("tr");
            writer.writeText("\n", null);
            writer.endElement("thead");
            writer.writeText("\n", null);
        }

        // Render the footer facet (if any)
        UIComponent footer = getFacet(component, "footer");
        String footerClass =
            (String) component.getAttributes().get("footerClass");
        if (footer != null) {
            writer.startElement("tfoot", component);
            writer.writeText("\n", null);
            writer.startElement("tr", footer);
            writer.startElement("td", footer);
            if (footerClass != null) {
                writer.writeAttribute("class", footerClass, "footerClass");
            }
            writer.writeAttribute("colspan", "" + getColumnCount(component),
                                  null);
            encodeRecursive(context, footer);
            writer.endElement("td");
            writer.endElement("tr");
            writer.writeText("\n", null);
            writer.endElement("tfoot");
            writer.writeText("\n", null);
        }

    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding children " + component.getId());
        }

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        // Set up the variables we will need
        ResponseWriter writer = context.getResponseWriter();
        int columns = getColumnCount(component);
        String columnClasses[] = getColumnClasses(component);
        int columnStyle = 0;
        int columnStyles = columnClasses.length;
        String rowClasses[] = getRowClasses(component);
        int rowStyle = 0;
        int rowStyles = rowClasses.length;
        boolean open = false;
        UIComponent facet = null;
        Iterator kids = null;
        int i = 0;

        // Render our children, starting a new row as needed
        writer.startElement("tbody", component);
        writer.writeText("\n", null);

        if (null != (kids = getChildren(component))) {
            while (kids.hasNext()) {
                UIComponent child = (UIComponent) kids.next();
                if ((i % columns) == 0) {
                    if (open) {
                        writer.endElement("tr");
                        writer.writeText("\n", null);
                        open = false;
                    }
                    writer.startElement("tr", component);
                    if (rowStyles > 0) {
                        writer.writeAttribute("class", rowClasses[rowStyle++],
                                              "rowClasses");
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
                    try {
                        writer.writeAttribute("class",
                                              columnClasses[columnStyle++],
                                              "columns");
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    if (columnStyle >= columnStyles) {
                        columnStyle = 0;
                    }
                }
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
        writer.writeText("\n", null);
        if (log.isTraceEnabled()) {
            log.trace("End encoding children " + component.getId());
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        // Render the ending of this panel
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("table");
        writer.writeText("\n", null);
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }
    }


    /**
     * Returns an array of stylesheet classes to be applied to
     * each column in the list in the order specified. Every column may or
     * may not have a stylesheet
     */
    private String[] getColumnClasses(UIComponent component) {
        String values = (String) component.getAttributes().get("columnClasses");
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
    private int getColumnCount(UIComponent component) {
        int count;
        Object value = component.getAttributes().get("columns");
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
        String values = (String) component.getAttributes().get("rowClasses");
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
