/*
 * $Id: GridRenderer.java,v 1.48 2007/07/06 18:21:57 rlubke Exp $
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


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;

/**
 * <B>GridRenderer</B> is a class that renders <code>UIPanel</code> component
 * as a "Grid".
 */

public class GridRenderer extends HtmlBasicRenderer {

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
            logger.log(Level.FINER, "Begin encoding component " +
                                    component.getId());
        }

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component "
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
        RenderKitUtils.renderPassThruAttributes(context, writer, component);
        writer.writeText("\n", component, null);

        // render the caption facet (if present)
        UIComponent caption = getFacet(component, "caption");
        if (caption != null) {
            String captionClass = (String)
                  component.getAttributes().get("captionClass");
            String captionStyle = (String)
                  component.getAttributes().get("captionStyle");
            writer.startElement("caption", component);
            if (captionClass != null) {
                writer.writeAttribute("class", captionClass, "captionClass");
            }
            if (captionStyle != null) {
                writer.writeAttribute("style", captionStyle, "captionStyle");
            }
            encodeRecursive(context, caption);
            writer.endElement("caption");
        }

        // Render the header facet (if any)
        UIComponent header = getFacet(component, "header");
        String headerClass =
              (String) component.getAttributes().get("headerClass");
        if (header != null) {
            writer.startElement("thead", component);
            writer.writeText("\n", component, null);
            writer.startElement("tr", header);
            writer.startElement("th", header);
            if (headerClass != null) {
                writer.writeAttribute("class", headerClass, "headerClass");
            }
            writer.writeAttribute("colspan", String.valueOf(getColumnCount(component)),
                                  null);
            writer.writeAttribute("scope", "colgroup", null);
            encodeRecursive(context, header);
            writer.endElement("th");
            writer.endElement("tr");
            writer.writeText("\n", component, null);
            writer.endElement("thead");
            writer.writeText("\n", component, null);
        }

        // Render the footer facet (if any)
        UIComponent footer = getFacet(component, "footer");
        String footerClass =
              (String) component.getAttributes().get("footerClass");
        if (footer != null) {
            writer.startElement("tfoot", component);
            writer.writeText("\n", component, null);
            writer.startElement("tr", footer);
            writer.startElement("td", footer);
            if (footerClass != null) {
                writer.writeAttribute("class", footerClass, "footerClass");
            }
            writer.writeAttribute("colspan", String.valueOf(getColumnCount(component)),
                                  null);
            encodeRecursive(context, footer);
            writer.endElement("td");
            writer.endElement("tr");
            writer.writeText("\n", component, null);
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

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " +
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
        int i = 0;

        // Render our children, starting a new row as needed
        writer.startElement("tbody", component);
        writer.writeText("\n", component, null);
        for (Iterator<UIComponent> kids = getChildren(component);
             kids.hasNext();) {
            UIComponent child = kids.next();
            if ((i % columns) == 0) {
                if (open) {
                    writer.endElement("tr");
                    writer.writeText("\n", component, null);
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
                writer.writeText("\n", component, null);
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
                    // why is this here?
                }
                if (columnStyle >= columnStyles) {
                    columnStyle = 0;
                }
            }
            encodeRecursive(context, child);
            writer.endElement("td");
            writer.writeText("\n", component, null);
            i++;
        }
        if (open) {
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }
        writer.endElement("tbody");
        writer.writeText("\n", component, null);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding children " + component.getId());
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
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " +
                            component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }
        // Render the ending of this panel
        ResponseWriter writer = context.getResponseWriter();
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
     * @param component the UIComponent of interest
     *
     * @return an array of stylesheet classes to be applied to
     *  each column in the list in the order specified. Every column may or
     *  may not have a stylesheet.
     */
    private String[] getColumnClasses(UIComponent component) {

        String values = (String) component.getAttributes().get("columnClasses");
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
     * @param component the UIComponent of interest
     *
     * @return the number of columns of the grid converting the value
     * specified to int if necessary.
     */
    private int getColumnCount(UIComponent component) {

        int count;
        Object value = component.getAttributes().get("columns");
        if ((value != null) && (value instanceof Integer)) {
            count = ((Integer) value);
        } else {
            count = 2;
        }
        if (count < 1) {
            count = 1;
        }
        return (count);

    }


    /**
     * @param component the UIComponent of interest
     *
     * @return an array of stylesheet classes to be applied to
     * each row in the list in the order specified. Every row may or
     * may not have a stylesheet.
     */
    private String[] getRowClasses(UIComponent component) {

        String values = (String) component.getAttributes().get("rowClasses");
        if (values == null) {
            return (new String[0]);
        }
        values = values.trim();
        ArrayList<String> list = new ArrayList<String>();
        while (values.length() > 0) {
            int comma = values.indexOf(',');
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
