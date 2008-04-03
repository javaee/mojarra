/*
 * $Id: TableRenderer.java,v 1.44 2007/04/27 22:01:03 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.renderkit.html_basic;


import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.logging.Level;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;

/** <p>Render a {@link UIData} component as a two-dimensional table.</p> */

public class TableRenderer extends HtmlBasicRenderer {

    // ---------------------------------------------------------- Public Methods


    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "component"));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding component " + component.getId());
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
        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                component,
                                                new String[]{"rows"});
        writer.writeText("\n", component, null);

        UIComponent caption = getFacet(data, "caption");
        if (caption != null) {
            String captionClass =
                  (String) data.getAttributes().get("captionClass");
            String captionStyle = (String)
                  data.getAttributes().get("captionStyle");
            writer.startElement("caption", data);
            if (captionClass != null) {
                writer.writeAttribute("class", captionClass, "captionClass");
            }
            if (captionStyle != null) {
                writer.writeAttribute("style", captionStyle, "captionStyle");
            }
            encodeRecursive(context, caption);
            writer.endElement("caption");
        }

        // get all of the columns
        List<UIColumn> columns = getColumns(data);
        // Render the header facets (if any)
        UIComponent header = getFacet(data, "header");
        int headerFacets = getFacetCount("header", columns);
        String headerClass = (String) data.getAttributes().get("headerClass");
        if ((header != null) || (headerFacets > 0)) {
            writer.startElement("thead", data);
            writer.writeText("\n", component, null);
        }
        if (header != null) {
            writer.startElement("tr", header);
            writer.startElement("th", header);
            if (headerClass != null) {
                writer.writeAttribute("class", headerClass, "headerClass");
            }
            writer.writeAttribute("colspan", String.valueOf(columns.size()), null);
            writer.writeAttribute("scope", "colgroup", null);
            encodeRecursive(context, header);
            writer.endElement("th");
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }
        if (headerFacets > 0) {
            writer.startElement("tr", data);
            writer.writeText("\n", component, null);
            for (UIColumn column: columns) {
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
                writer.writeText("\n", component, null);
            }
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }
        if ((header != null) || (headerFacets > 0)) {
            writer.endElement("thead");
            writer.writeText("\n", component, null);
        }

        // Render the footer facets (if any)
        UIComponent footer = getFacet(data, "footer");
        int footerFacets = getFacetCount("footer", columns);
        String footerClass = (String) data.getAttributes().get("footerClass");
        if ((footer != null) || (footerFacets > 0)) {
            writer.startElement("tfoot", data);
            writer.writeText("\n", component, null);
        }
        if (footerFacets > 0) {
            writer.startElement("tr", data);
            writer.writeText("\n", component, null);
            for (UIColumn column : columns) {
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
                writer.writeText("\n", component, null);
            }
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }
        if (footer != null) {
            writer.startElement("tr", footer);
            writer.startElement("td", footer);
            if (footerClass != null) {
                writer.writeAttribute("class", footerClass, "footerClass");
            }
            writer.writeAttribute("colspan", String.valueOf(columns.size()), null);
            encodeRecursive(context, footer);
            writer.endElement("td");
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }
        if ((footer != null) || (footerFacets > 0)) {
            writer.endElement("tfoot");
            writer.writeText("\n", component, null);
        }

    }


    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "component"));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding children " + component.getId());
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
        int numColumnClasses = columnClasses.length;
        String rowClasses[] = getRowClasses(data);
        int numRowClasses = rowClasses.length;
        ResponseWriter writer = context.getResponseWriter();

        // Iterate over the rows of data that are provided
        int processed = 0;
        int rowIndex = data.getFirst() - 1;
        int rows = data.getRows();
        int rowStyle = 0;

        writer.startElement("tbody", component);
        writer.writeText("\n", component, null);
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
            if (numRowClasses > 0) {
                writer.writeAttribute("class", rowClasses[rowStyle++],
                                      "rowClasses");
                if (rowStyle >= numRowClasses) {
                    rowStyle = 0;
                }
            }
            writer.writeText("\n", component, null);

            // Iterate over the child UIColumn components for each row
            int columnStyleIdx = 0;
            List<UIColumn> columns = getColumns(data);
            int numberOfColumnClasses = columnClasses.length;

            for (UIColumn column : columns) {

                // Render the beginning of this cell
                writer.startElement("td", column);
                if (numberOfColumnClasses > 0) {
                    writer.writeAttribute("class", columnClasses[columnStyleIdx++],
                                          "columnClasses");
                    if (columnStyleIdx >= numberOfColumnClasses) {
                        columnStyleIdx = 0;
                    }
                }               

                // Render the contents of this cell by iterating over
                // the kids of our kids
                for (Iterator<UIComponent> gkids = getChildren(column);
                     gkids.hasNext(); ) {
                    encodeRecursive(context, gkids.next());
                }

                // Render the ending of this cell
                writer.endElement("td");
                writer.writeText("\n", component, null);

            }

            // Render the ending of this row
            writer.endElement("tr");
            writer.writeText("\n", component, null);

        }
        writer.endElement("tbody");
        writer.writeText("\n", component, null);

        // Clean up after ourselves
        data.setRowIndex(-1);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "End encoding children " +
                                    component.getId());
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "component"));
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
        writer.writeText("\n", component, null);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding component " + component.getId());
        }

    }


    public boolean getRendersChildren() {

        return true;

    }

    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return an array of stylesheet classes to be applied to
     * each column in the table in the order specified. Every column may or
     * may not have a stylesheet.</p>
     *
     * @param data {@link UIData} component being rendered
     * @return an array of column classes
     */
    private String[] getColumnClasses(UIData data) {

        String values = (String) data.getAttributes().get("columnClasses");
        if (values == null) {
            return (new String[0]);
        }
        values = values.trim();
        ArrayList<String> list = new ArrayList<String>();
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
        return (list.toArray(results));

    }


    /**
     * <p>Return an Iterator over the <code>UIColumn</code> children
     * of the specified <code>UIData</code> that have a
     * <code>rendered</code> property of <code>true</code>.</p>
     *
     * @param data <code>UIData</code> for which to extract children
     * @return the List of all UIColumn children
     */
    private List<UIColumn> getColumns(UIData data) {

        int childCount = data.getChildCount();
        if (childCount > 0) {
            List<UIColumn> results = new ArrayList<UIColumn>(childCount);
            for (UIComponent kid : data.getChildren()) {
                if ((kid instanceof UIColumn) && kid.isRendered()) {
                    results.add((UIColumn) kid);
                }
            }
            return results;
        } else {
            return Collections.emptyList();
        }

    }


    /**
     * <p>Return the number of child <code>UIColumn</code> components
     * nested in the specified <code>UIData</code> that have a facet with
     * the specified name.</p>
     *
     * @param name Name of the facet being analyzed
     * @param columns the columns to search
     * @return the number of columns associated with the specified
     *  Facet name
     */
    private int getFacetCount(String name, List<UIColumn> columns) {

        int n = 0;
        if (!columns.isEmpty()) {
            for (UIColumn column : columns) {
                if (getFacet(column, name) != null) {
                    n++;
                }
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
     * @return an array of row classes
     */
    private String[] getRowClasses(UIData data) {

        String values = (String) data.getAttributes().get("rowClasses");
        if (values == null) {
            return (new String[0]);
        }
        values = values.trim();
        ArrayList<String> list = new ArrayList<String>();
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
        return (list.toArray(results));

    }

}
