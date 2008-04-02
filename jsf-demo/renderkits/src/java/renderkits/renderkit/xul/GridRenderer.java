/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package renderkits.renderkit.xul;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * <B>GridRenderer</B> is a class that renders <code>UIPanel</code> component
 * as a "Grid".
 */

public class GridRenderer extends BaseRenderer {

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

    public boolean getRendersChildren() {
        return true;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component is null");
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
        writer.writeText("\n", null);
        writer.startElement("grid", component);
        writeIdAttributeIfNecessary(context, writer, component);
        String styleClass =
              (String) component.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        writer.writeText("\n", null);

        writer.startElement("columns", component);
        writer.writeText("\n", null);
        int columns = getColumnCount(component);
        for (int i = 0; i < columns; i++) {
            writer.startElement("column", component);
            writer.endElement("column");
            writer.writeText("\n", null);
        }
        writer.endElement("columns");
        writer.writeText("\n", null);
        writer.startElement("rows", component);
        writer.writeText("\n", null);
    }


    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component is null");
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
        Iterator<UIComponent> kids = null;
        int i = 0;

        // Render our children, starting a new row as needed

        if (null != (kids = getChildren(component))) {
            while (kids.hasNext()) {
                UIComponent child = kids.next();
                if ((i % columns) == 0) {
                    if (open) {
                        writer.endElement("row");
                        writer.writeText("\n", null);
                        open = false;
                    }
                    writer.startElement("row", component);
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
                writer.startElement("box", component);
                writer.writeText("\n", null);
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
                writer.endElement("box");
                writer.writeText("\n", null);
                i++;
            }
        }
        if (open) {
            writer.endElement("row");
            writer.writeText("\n", null);
        }
        writer.writeText("\n", null);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding children " + component.getId());
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component is null");
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
        writer.endElement("rows");
        writer.writeText("\n", null);
        writer.endElement("grid");
        writer.writeText("\n", null);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding component " + component.getId());
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
     * Returns number of columns of the grid converting the value
     * specified to int if necessary.
     */
    private int getColumnCount(UIComponent component) {
        int count;
        Object value = component.getAttributes().get("columns");
        if ((value != null) && (value instanceof Integer)) {
            count = (Integer) value;
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
