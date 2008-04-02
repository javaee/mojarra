/*
 * $Id: TabRenderer.java,v 1.1 2003/02/15 00:57:53 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.renderkit;


import components.components.PaneComponent;
import java.io.IOException;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Render the individual {@link PaneComponent} and its children, but
 * <strong>only</strong> if this {@link PaneComponent} is currently
 * selected.  Otherwise, no output at all is sent.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Because of the fact that we
 * want standard JSP text (not nested inside Faces components) to be usable
 * on a pane, this Renderer needs to know whether it is being used in a JSP
 * environment (where the rendering of the child components writes to the
 * local value of our Pane component) or not (where we must do it ourselves).
 * This is resolved by having the <code>Pane_Tab</code> tag set a
 * render dependent attribute named "demo.renderer.TabRenderer.JSP" when it
 * creates the corresponding component, so that we can tell what is going on.
 * </p>
 */

public class TabRenderer extends BaseRenderer {


    private static Log log = LogFactory.getLog(TabRenderer.class);


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

        if (log.isDebugEnabled()) {
            log.debug("encodeBegin(" + component.getComponentId() + ")");
        }

    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("encodeChildren(" + component.getComponentId() + ")");
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("encodeEnd(" + component.getComponentId() + ")");
        }

        // Render our children only -- our parent has rendered ourself
        Iterator kids = component.getChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            encodeRecursive(context, kid);
        }

    }


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


}
