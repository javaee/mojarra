/*
 * $Id: Panel_ListTag.java,v 1.11 2003/09/08 20:10:15 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import com.sun.faces.taglib.FacesTag;
import javax.servlet.jsp.JspException;

/**
 * This class is the tag handler that evaluates the 
 * <code>panel_list</code> custom tag.
 */

public class Panel_ListTag extends FacesTag {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private String columnClasses = null;
    private String footerClass = null;
    private String headerClass = null;
    private String panelClass = null;
    private String rowClasses = null;
     
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    
    public Panel_ListTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    
    public void setColumnClasses(String newColumnClasses) {
        this.columnClasses = newColumnClasses;
    }
    
    public void setFooterClass(String newFooterClass) {
        this.footerClass = newFooterClass;
    }
    
    public void setHeaderClass(String newHeaderClass) {
        this.headerClass = newHeaderClass;
    }
    
    public void setPanelClass(String newPanelClass) {
        this.panelClass = newPanelClass;
    }
    
    public void setRowClasses(String newRowClasses) {
        this.rowClasses = newRowClasses;
    }
    
    public void release() {
        super.release();
        this.columnClasses = null;
        this.footerClass = null;
        this.headerClass = null;
        this.panelClass = null;
        this.rowClasses = null;
    }


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if (columnClasses != null) {
            component.setAttribute("columnClasses", columnClasses);
        }
        if (footerClass != null) {
            component.setAttribute("footerClass", footerClass);
        }
        if (headerClass != null) {
            component.setAttribute("headerClass", headerClass);
        }
        if (panelClass != null) {
            component.setAttribute("panelClass", panelClass);
        }
        if (rowClasses != null) {
            component.setAttribute("rowClasses", rowClasses);
        }
        
        // set HTML 4.0 attributes if any
        if (summary != null) {
            component.setAttribute("summary", summary);
        }
        if (width != null) {
            component.setAttribute("width", width);
        }
        if (bgcolor != null) {
            component.setAttribute("bgcolor", bgcolor);
        }
        if (frame != null) {
            component.setAttribute("frame", frame);
        }
        if (rules != null) {
            component.setAttribute("rules", rules);
        }
        if (border != null) {
            component.setAttribute("border", border);
        }
        if (cellspacing != null) {
            component.setAttribute("cellspacing", cellspacing);
        }
        if (cellpadding != null) {
            component.setAttribute("cellpadding", cellpadding);
        }
    }

    /**
     * This is implemented by faces subclasses to allow globally turing off
     * the render kit.
     */
    public String getRendererType() {
        return ("List"); 
    }

    public String getComponentType() { 
        return ("Panel"); 
    }    
}
