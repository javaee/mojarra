/*
 * $Id: Panel_ListTag.java,v 1.1 2002/09/09 23:52:26 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import com.sun.faces.taglib.FacesTag;
import javax.servlet.jsp.JspException;

/**
 *
 * @version $Id: Panel_ListTag.java,v 1.1 2002/09/09 23:52:26 visvan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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
    
    public String getColumnClasses() {
        return columnClasses;
    }
  
    public void setFooterClass(String newFooterClass) {
        this.footerClass = newFooterClass;
    }
    
    public String getFooterClass() {
       return footerClass;
    }

    public void setHeaderClass(String newHeaderClass) {
        this.headerClass = newHeaderClass;
    }
    
    public String getHeaderClass() {
        return headerClass;
    }
    
    public void setPanelClass(String newPanelClass) {
        this.panelClass = newPanelClass;
    }
    
    public String getPanelClass() {
        return panelClass;
    }
    
    public void setRowClasses(String newRowClasses) {
        this.rowClasses = newRowClasses;
    }
    
    public String getRowClasses() {
      return rowClasses;
    }
    
    // 
    // Methods from FacesTag
    //
    public UIComponent createComponent() {
        return (new UIPanel());
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
        if ((columnClasses != null) &&
            (component.getAttribute("columnClasses") == null)) {
            component.setAttribute("columnClasses", getColumnClasses());
        }
        if ((footerClass != null) &&
            (component.getAttribute("footerClass") == null)) {
            component.setAttribute("footerClass", getFooterClass());
        }
        if ((headerClass != null) &&
            (component.getAttribute("headerClass") == null)) {
            component.setAttribute("headerClass", getHeaderClass());
        }
        if ((panelClass != null) &&
            (component.getAttribute("panelClass") == null)) {
            component.setAttribute("panelClass", getPanelClass());
        }
        if ((rowClasses != null) &&
            (component.getAttribute("rowClasses") == null)) {
            component.setAttribute("rowClasses", getRowClasses());
        }
        
        // set HTML 4.0 attributes if any
        if ((summary != null) &&
            (component.getAttribute("summary") == null)) {
            component.setAttribute("summary", getSummary());
        }
        if ((width != null) &&
            (component.getAttribute("width") == null)) {
            component.setAttribute("width", getWidth());
        }
        if ((bgcolor != null) &&
            (component.getAttribute("bgcolor") == null)) {
            component.setAttribute("bgcolor", getBgcolor());
        }
        if ((frame != null) &&
            (component.getAttribute("frame") == null)) {
            component.setAttribute("frame", getFrame());
        }
        if ((rules != null) &&
            (component.getAttribute("rules") == null)) {
            component.setAttribute("rules", getRules());
        }
        if ((border != null) &&
            (component.getAttribute("border") == null)) {
            component.setAttribute("border", getBorder());
        }
        if ((cellspacing != null) &&
            (component.getAttribute("cellspacing") == null)) {
            component.setAttribute("cellspacing", getCellspacing());
        }
        if ((cellpadding != null) &&
            (component.getAttribute("cellpadding") == null)) {
            component.setAttribute("cellpadding", getCellpadding());
        }
    }

    /**
     *
     * This is implemented by faces subclasses to allow globally turing off
     * the render kit.
     *
     */
    public String getLocalRendererType() {
        return ("ListRenderer");
    }    

}
