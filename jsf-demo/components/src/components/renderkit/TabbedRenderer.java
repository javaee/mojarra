/*
 * $Id: TabbedRenderer.java,v 1.1 2003/02/15 00:57:54 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.renderkit;


import components.components.PaneComponent;
import components.components.PaneSelectedEvent;
import java.io.IOException;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Render our associated {@link PaneComponent} as a tabbed control, with
 * each of its immediate child {@link PaneComponent}s representing a single
 * tab.  Measures are taken to ensure that exactly one of the child tabs is
 * selected, and only the selected child pane's contents will be rendered.
 * </p>
 */

public class TabbedRenderer extends BaseRenderer {


    private static Log log = LogFactory.getLog(TabbedRenderer.class);


    public AttributeDescriptor getAttributeDescriptor
        (UIComponent component, String name) {
        return (null); // FIXME
    }


    public AttributeDescriptor getAttributeDescriptor
        (String componentType, String name) {
        return (null); // FIXME
    }


    public Iterator getAttributeNames(UIComponent component) {
        return (null); // FIXME
    }


    public Iterator getAttributeNames(String componentType) {
        return (null); // FIXME
    }


    public boolean supportsComponentType(UIComponent component) {
        return (component instanceof PaneComponent);
    }


    public boolean supportsComponentType(String componentType) {
        return (componentType.equals(PaneComponent.TYPE));
    }


    public void decode(FacesContext context, UIComponent component)
        throws IOException {
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isTraceEnabled()) {
            log.trace("encodeBegin(" + component.getComponentId() + ")");
        }

        // Render the outer border and tabs of our owning table
        String paneClass = (String) component.getAttribute("paneClass");
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<table");
        if (paneClass != null) {
            writer.write(" class=\"");
            writer.write(paneClass);
            writer.write("\"");
        }
        writer.write(">\n");

    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isTraceEnabled()) {
            log.trace("encodeChildren(" + component.getComponentId() + ")");
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isTraceEnabled()) {
            log.trace("encodeEnd(" + component.getComponentId() + ")");
        }

        // Ensure that exactly one of our child PaneComponents is selected
        Iterator kids = component.getChildren();
        PaneComponent firstPane = null;
        PaneComponent selectedPane = null;
        int n = 0;
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (!(kid instanceof PaneComponent)) {
                continue;
            }
            PaneComponent pane = (PaneComponent) kid;
            n++;
            if (firstPane == null) {
                firstPane = pane;
            }
            if (pane.isSelected()) {
                if (selectedPane == null) {
                    selectedPane = pane;
                } else {
                    pane.setSelected(false);
                }
            }
        }
        if ((selectedPane == null) && (firstPane != null)) {
            firstPane.setSelected(true);
            selectedPane = firstPane;
        }

        // Render the labels for our tabs
        String selectedClass =
            (String) component.getAttribute("selectedClass");
        String unselectedClass =
            (String) component.getAttribute("unselectedClass");
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<tr>\n");
        int percent;
        if (n > 0) {
            percent = 100 / n;
        } else {
            percent = 100;
        }
            
        kids = component.getChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (!(kid instanceof PaneComponent)) {
                continue;
            }
            PaneComponent pane = (PaneComponent) kid;
            writer.write("<td width=\"");
            writer.write("" + percent);
            writer.write("%\"");
            if (pane.isSelected() && (selectedClass != null)) {
                writer.write(" class=\"");
                writer.write(selectedClass);
                writer.write("\"");
            } else if (!pane.isSelected() && (unselectedClass != null)) {
                writer.write(" class=\"");
                writer.write(unselectedClass);
                writer.write("\"");
            }
            writer.write(">");

            UIComponent facet = (UIComponent)pane.getFacet("label");
            if (facet != null) {
                if (pane.isSelected() && (selectedClass != null)) {
                    facet.setAttribute("paneTabLabelClass", selectedClass);
                } else if (!pane.isSelected() && (unselectedClass != null)) {
                    facet.setAttribute("paneTabLabelClass", unselectedClass);
                }
                facet.encodeBegin(context);
            }
            writer.write("</td>\n");
        }
        writer.write("</tr>\n");

        // Begin the containing element for the selected child pane
        String contentClass = (String) component.getAttribute("contentClass");
        writer.write("<tr><td width=\"100%\" colspan=\"");
        writer.write("" + n);
        writer.write("\"");
        if (contentClass != null) {
            writer.write(" class=\"");
            writer.write(contentClass);
            writer.write("\"");
        }
        writer.write(">\n");

        // Render the selected child pane
        selectedPane.encodeBegin(context);
        if (selectedPane.getRendersChildren()) {
            selectedPane.encodeChildren(context); // We know Pane does this
        }
        selectedPane.encodeEnd(context);

        // End the containing element for the selected child pane
        writer.write("\n</td></tr>\n");

        // Render the ending of our owning element and table
        writer.write("</table>\n");
    }
}
