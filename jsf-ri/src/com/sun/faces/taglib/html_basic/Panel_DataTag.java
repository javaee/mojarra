/*
 * $Id: Panel_DataTag.java,v 1.3 2003/02/20 22:49:18 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import com.sun.faces.taglib.FacesTag;
import javax.servlet.jsp.JspException;

/**
 *
 * @version $Id: Panel_DataTag.java,v 1.3 2003/02/20 22:49:18 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Panel_DataTag extends FacesTag {
    
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    protected String var = null;
    
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    
    public Panel_DataTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    //
    // Methods from FacesTag
    //
     public UIComponent createComponent() {
        return (new UIPanel());
    }
    
    public void release() {
        super.release();
        this.var = null;
    }

    
    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if ((var != null) &&
            (component.getAttribute("var") == null)) {
            component.setAttribute("var", getVar());
        }
    }

    public String getLocalRendererType() {
       return ("Data"); 
    }    

}
