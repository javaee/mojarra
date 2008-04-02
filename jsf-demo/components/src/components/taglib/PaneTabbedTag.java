/*
 * $Id: PaneTabbedTag.java,v 1.1 2003/02/15 00:59:03 rkitain Exp $
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
 * that represents a the overall tabbed pane control.
 */
public class PaneTabbedTag extends FacesTag {


    private static Log log = LogFactory.getLog(PaneTabbedTag.class);


    private String contentClass = null;
    public void setContentClass(String contentClass) {
        this.contentClass = contentClass;
    }


    private String paneClass = null;
    public void setPaneClass(String paneClass) {
        this.paneClass = paneClass;
    }


    private String selectedClass = null;
    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }


    private String unselectedClass = null;
    public void setUnselectedClass(String unselectedClass) {
        this.unselectedClass = unselectedClass;
    }


    public UIComponent createComponent() {
        return (new PaneComponent());
    }


    public String getRendererType() {
        return ("Tabbed");
    }


    public void release() {
        super.release();
        contentClass = null;
        paneClass = null;
        selectedClass = null;
        unselectedClass = null;
    }


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if ((contentClass != null) &&
            (component.getAttribute("contentClass") == null)) {
            component.setAttribute("contentClass", contentClass);
        }
        if ((paneClass != null) &&
            (component.getAttribute("paneClass") == null)) {
            component.setAttribute("paneClass", paneClass);
        }
        if ((selectedClass != null) &&
            (component.getAttribute("selectedClass") == null)) {
            component.setAttribute("selectedClass", selectedClass);
        }
        if ((unselectedClass != null) &&
            (component.getAttribute("unselectedClass") == null)) {
            component.setAttribute("unselectedClass", unselectedClass);
        }
    }


}
