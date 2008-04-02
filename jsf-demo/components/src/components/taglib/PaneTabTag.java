/*
 * $Id: PaneTabTag.java,v 1.1 2003/02/15 00:59:03 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.taglib;


import components.components.PaneComponent;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.webapp.FacesTag;
import javax.servlet.jsp.JspException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class creates a <code>PaneComponent</code> instance
 * that represents an individual tab on the overall control.
 */
public class PaneTabTag extends FacesTag {

    private static Log log = LogFactory.getLog(PaneTabTag.class);

    // The selected flag for this pane
    private boolean selected = false;
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public UIComponent createComponent() {
        return (new PaneComponent());
    }

    public String getRendererType() {
        return ("Tab");
    }

    public void release() {
        super.release();
        this.selected = false;
    }

    protected void overrideProperties(UIComponent component) {

        // Standard override processing
        super.overrideProperties(component);
        if (selected && getCreated() &&
            !((PaneComponent) component).isSelected()) {
            log.debug("OVERRIDING " + component.getComponentId());
            component.setAttribute("selected", Boolean.TRUE);
        }
    }
}
