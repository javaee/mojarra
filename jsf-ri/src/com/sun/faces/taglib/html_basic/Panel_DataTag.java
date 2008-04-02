/*
 * $Id: Panel_DataTag.java,v 1.8 2003/07/16 00:00:12 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>panel_data</code> custom tag.
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

    public void release() {
        super.release();
        this.var = null;
    }

    
    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if (var != null) {
            component.setAttribute("var", var);
        }
    }

    public String getLocalRendererType() { 
        return ("Data"); 
    }    

    public String getComponentType() { 
        return ("Panel"); 
    }    

}
