/*
 * $Id: TableRenderer.java,v 1.27 2005/06/09 22:37:48 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit.html_basic;


import com.sun.faces.util.Util;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Render a {@link UIData} component as a two-dimensional table.</p>
 */

public class TableRenderer extends HtmlBasicRenderer {
  
    public boolean getRendersChildren() {
        return true;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Begin encoding component " + component.getId());
        }

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("No encoding necessary " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        UIData data = (UIData) component;
        data.setRowIndex(-1);

        // Render the beginning of the table
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", data);
        writeIdAttributeIfNecessary(context, writer, component);
        String styleClass = (String) data.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        Util.renderPassThruAttributes(context, writer, component,
                                      new String[]{"rows"});
        writer.writeText("\n", null);

        // Render the header facets (if any)
        UIComponent header = getFacet(data, "header");
        int headerFacets = getFacetCount(data, "header");
        String headerClass = (String) data.getAttributes().get("headerClass");
        if ((header != null) || (headerFacets > 0)) {
            writer.startElement("thead", data);
            writer.writeText("\n", null);
        }
        if (header != null) {
            writer.startElement("tr", header);
            writer.startElement("th", header);
            if (headerClass != null) {
                writer.writeAttribute("class", headerClass, "headerClass");
            }
            writer.writeAttribute("colspan", "" + getColumnCount(data), null);
            writer.writeAttribute("scope", "colgroup", null);
            encodeRecursive(context, header);
            writer.endElement("th");
            writer.endElement("tr");
            writer.writeText("\n", null);
        }
        if (headerFacets > 0) {
            writer.startElement("tr", data);
            writer.writeText("\n", null);
            Iterator columns = getColumns(data);
            while (columns.hasNext()) {
                UIColumn column = (UIColumn) columns.next();
                String columnHeaderClass = 
                    (String) column.getAttributes().get("headerClass");
                writer.startElement("th", column);
                if (columnHeaderClass != null) {
                    writer.writeAttribute("class", columnHeaderClass, 
                        "columnHeaderClass");
                } else if (headerClass != null) {
                    writer.writeAttribute("class", headerClass, "headerClass");
                }
                writer.writeAttribute("scope", "col", null);
                UIComponent facet = getFacet(column, "header");
                if (facet != null) {
                    encodeRecursive(context, facet);
                }
                writer.endElement("th");
                writer.writeText("\n", null);
            }
            writer.endElement("tr");
            writer.writeText("\n", null);
        }
        if ((header != null) || (headerFacets > 0)) {
            writer.endElement("thead");
            writer.writeText("\n", null);
        }

        // Render the footer facets (if any)
        UIComponent footer = getFacet(data, "footer");
        int footerFacets = getFacetCount(data, "footer");
        String footerClass = (String) data.getAttributes().get("footerClass");
        if ((footer != null) || (footerFacets > 0)) {
            writer.startElement("tfoot", data);
            writer.writeText("\n", null);
        }
        if (footer != null) {
            writer.startElement("tr", footer);
            writer.startElement("td", footer);
            if (footerClass != null) {
                writer.writeAttribute("class", footerClass, "footerClass");
            }
            writer.writeAttribute("colspan", "" + getColumnCount(data), null);
            encodeRecursive(context, footer);
            writer.endElement("td");
            writer.endElement("tr");
            writer.writeText("\n", null);
        }
        if (footerFacets > 0) {
            writer.startElement("tr", data);
            writer.writeText("\n", null);
            Iterator columns = getColumns(data);
            while (columns.hasNext()) {
                UIColumn column = (UIColumn) columns.next();
                String columnFooterClass = 
                    (String) column.getAttributes().get("footerClass");
                writer.startElement("td", column);
                if (columnFooterClass != null) {
                    writer.writeAttribute("class", columnFooterClass, 
                        "columnFooterClass");
                } else if (footerClass != null) {
                    writer.writeAttribute("class", footerClass, "footerClass");
                }
                UIComponent facet = getFacet(column, "footer");
                if (facet != null) {
                    encodeRecursive(context, facet);
                }
                writer.endElement("td");
                writer.writeText("\n", null);
            }
            writer.endElement("tr");
            writer.writeText("\n", null);
        }
        if ((footer != null) || (footerFacets > 0)) {
            writer.endElement("tfoot");
            writer.writeText("\n", null);
        }

    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Begin encoding children " + component.getId());
        }
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("No encoding necessary " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        UIData data = (UIData) component;

        // Set up variables we will need
        String columnClasses[] = getColumnClasses(data);
        int columnStyle = 0;
        int columnStyles = columnClasses.length;
        String rowClasses[] = getRowClasses(data);
        int rowStyles = rowClasses.length;
        ResponseWriter writer = context.getResponseWriter();
        Iterator kids = null;
        Iterator grandkids = null;

        // Iterate over the rows of data that are provided
        int processed = 0;
        int rowIndex = data.getFirst() - 1;
        int rows = data.getRows();
        int rowStyle = 0;

        writer.startElement("tbody", component);
        writer.writeText("\n", null);
        while (true) {

            // Have we displayed the requested number of rows?
            if ((rows > 0) && (++processed > rows)) {
                break;
            }
            // Select the current row
            data.setRowIndex(++rowIndex);
            if (!data.isRowAvailable()) {
                break; // Scrolled past the last row
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
            kids = getColumns(data);
            while (kids.hasNext()) {

                // Identify the next renderable column
                UIColumn column = (UIColumn) kids.next();

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
                grandkids = getChildren(column);
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
        writer.endElement("tbody");
        writer.writeText("\n", null);

        // Clean up after ourselves
        data.setRowIndex(-1);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"End encoding children " +
                      component.getId());
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("No encoding necessary " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        UIData data = (UIData) component;
        data.setRowIndex(-1);
        ResponseWriter writer = context.getResponseWriter();

        // Render the ending of this table
        writer.endElement("table");
        writer.writeText("\n", null);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"End encoding component " + component.getId());
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return an array of stylesheet classes to be applied to
     * each column in the table in the order specified. Every column may or
     * may not have a stylesheet.</p>
     *
     * @param data {@link UIData} component being rendered
     */
    private String[] getColumnClasses(UIData data) {

        String values = (String) data.getAttributes().get("columnClasses");
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
     * that are nested in the specified {@link UIData}.</p>
     *
     * @param data {@link UIData} component being analyzed
     */
    private int getColumnCount(UIData data) {

        int columns = 0;
        Iterator kids = getColumns(data);
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            columns++;
        }
        return (columns);

    }


    /**
     * <p>Return an Iterator over the <code>UIColumn</code> children
     * of the specified <code>UIData</code> that have a
     * <code>rendered</code> property of <code>true</code>.</p>
     *
     * @param data <code>UIData</code> for which to extract children
     */
    private Iterator getColumns(UIData data) {

        List results = new ArrayList();
        Iterator kids = data.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if ((kid instanceof UIColumn) && kid.isRendered()) {
                results.add(kid);
            }
        }
        return (results.iterator());

    }


    /**
     * <p>Return the number of child <code>UIColumn</code> components
     * nested in the specified <code>UIData</code> that have a facet with
     * the specified name.</p>
     *
     * @param data <code>UIData</code> component being analyzed
     * @param name Name of the facet being analyzed
     */
    private int getFacetCount(UIData data, String name) {

        int n = 0;
        Iterator kids = getColumns(data);
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (getFacet(kid, name) != null) {
                n++;
            }
        }
        return (n);

    }


    /**
     * <p>Return an array of stylesheet classes to be applied to
     * each row in the table, in the order specified.  Every row may or
     * may not have a stylesheet.</p>
     *
     * @param data {@link UIData} component being rendered
     */
    private String[] getRowClasses(UIData data) {

        String values = (String) data.getAttributes().get("rowClasses");
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
