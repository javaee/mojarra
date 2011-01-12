/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.sandbox.render;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.RIConstants;
import com.sun.faces.sandbox.util.Util;

/** <p>Render a {@link UIData} component as a two-dimensional table.</p> */

public class AccessibleTableRenderer extends HtmlBasicRenderer{

    // ---------------------------------------------------------- Public Methods

    private static final String HEADER_ID_LIST_ATTR_NAME =
            RIConstants.FACES_PREFIX + "HeaderIds";

    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
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
        Util.renderPassThruAttributes(writer,
                                      component,
                                      new String[]{"rows"});
        writer.writeText("\n", null);

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
        UIComponent colGroups = getFacet(data, "colgroups");
        if (null != colGroups) {
            encodeRecursive(context, colGroups);
        }

        // Render the header facets (if any)
        UIComponent header = getFacet(data, "header");
        int headerFacets = getFacetCount(data, "header");
        String headerClass = (String) data.getAttributes().get("headerClass");
        if ((header != null) || (headerFacets > 0)) {
            // WCAG 5.2
            writer.startElement("thead", data);
            writer.writeText("\n", null);
        }
        if (header != null) {
            writer.startElement("tr", header);
            // WCAG 5.1
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
                // WCAG 5.1
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
            // WCAG 5.2
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

        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        if (!component.isRendered()) {           
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
        List bodyRows = getBodyRows(data);
        boolean wroteTbody = false;

        // WCAG 5.2
        // If there is no "bodyrows" attribute, or it is empty,
        if (null == bodyRows || bodyRows.isEmpty()) {
            // contain the entire body in a single tbody element.
            wroteTbody = true;
            writer.startElement("tbody", component);
        }
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
            if (null != bodyRows && bodyRows.contains(Integer.valueOf(data.getRowIndex()))) {
                // close out the previous tbody.
                if (wroteTbody) {
                    writer.endElement("tbody");
                }
                writer.startElement("tbody", component);
                wroteTbody = true;
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
            int i = 0;
            while (kids.hasNext()) {

                // Identify the next renderable column
                UIColumn column = (UIColumn) kids.next();
                Boolean isRowHeader = null;

                // Render the beginning of this cell

                // If the rowHeader attribute was set to true on this column...
                if (null != (isRowHeader =
                        (Boolean) column.getAttributes().get("rowHeader")) &&
                    isRowHeader.booleanValue()) {
                    // WCAG 5.2.  Generate th with scope=row.
                    writer.startElement("th", column);
                    writer.writeAttribute("scope", "row", null);
                }
                else {
                    writer.startElement("td", column);
                }
                // WCAG 5.2 render the "headers" attribute with data
                // from the headers facet, if present.
                List headerIds = (List) context.
                        getExternalContext().getRequestMap().
                        get(HEADER_ID_LIST_ATTR_NAME);
                if (null != headerIds) {
                    writer.writeAttribute("headers", headerIds.get(i++),
                            "headers");
                }
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
                if (null != isRowHeader && isRowHeader.booleanValue()) {
                    writer.endElement("th");
                }
                else {
                    writer.endElement("td");
                }
                writer.writeText("\n", null);

            }

            // Render the ending of this row
            writer.endElement("tr");
            writer.writeText("\n", null);

        }
        // If there was no bodyrows attribute, or it was empty.
        if (wroteTbody) {
            writer.endElement("tbody");
        }
        writer.writeText("\n", null);

        // Clean up after ourselves
        data.setRowIndex(-1);

    }

    private List getBodyRows(UIData data) throws NumberFormatException {
        List result = null;
        String bodyRows = (String) data.getAttributes().get("bodyrows");
        if (null != bodyRows) {
            String [] rows = bodyRows.split(",");
            if (null != rows) {
                result = new ArrayList(rows.length);
                for (int i = 0; i < rows.length; i++) {
                    result.add(Integer.valueOf(rows[i]));
                }                
            }
        }

        return result;
    }


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }
        if (!component.isRendered()) {
            return;
        }
        UIData data = (UIData) component;
        data.setRowIndex(-1);
        ResponseWriter writer = context.getResponseWriter();

        // Render the ending of this table
        writer.endElement("table");
        writer.writeText("\n", null);

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

        return ((String[]) list.toArray(new String[list.size()]));

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
            kids.next();
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
                results.add((UIColumn) kid);
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
       
        return ((String[]) list.toArray(new String[list.size()]));

    }

}
