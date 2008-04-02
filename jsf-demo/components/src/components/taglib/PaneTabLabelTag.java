/*
 * $Id: PaneTabLabelTag.java,v 1.1 2003/02/15 00:59:03 rkitain Exp $
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
 * that represents a tab button control on the tab pane.
 */
public class PaneTabLabelTag extends FacesTag {

    private static Log log = LogFactory.getLog(PaneTabLabelTag.class);

    protected String label = null;
    protected String image = null;
    protected String commandName = null;

    public String getLabel() { return label; }
    public void setLabel(String newLabel) {
        label = newLabel;
    }

    public String getImage() { return image; }
    public void setImage(String newImage) {
        image = newImage;
    }

    public String getCommandName() { return commandName; }
    public void setCommandName(String newCommandName) {
        commandName = newCommandName;
    }

    public UIComponent createComponent() {
        return (new PaneComponent());
    }

    public String getRendererType() {
        return ("TabLabel");
    }

    public void release() {
        super.release();
        this.label = null;
        this.image = null;
        this.commandName = null;
    }

    protected void overrideProperties(UIComponent component) {

        // Standard override processing
        super.overrideProperties(component);

        PaneComponent pane = (PaneComponent)component;

        if (null == pane.getAttribute("label")) {
            pane.setAttribute("label", getLabel());
        }
        if (null == pane.getAttribute("image")) {
            pane.setAttribute("image", getImage());
        }
        if (null == pane.getAttribute("commandName")) {
            pane.setAttribute("commandName", getCommandName());
        }
    }
}
