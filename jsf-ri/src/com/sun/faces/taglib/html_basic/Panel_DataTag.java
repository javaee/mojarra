/*
 * $Id: Panel_DataTag.java,v 1.14 2003/10/07 20:15:53 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import javax.faces.component.UIComponent;
import com.sun.faces.taglib.BaseComponentTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>panel_data</code> custom tag.
 */

public class Panel_DataTag extends BaseComponentTag {
    
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

    // var is not expression enabled
    public void setVar(String var) {
        this.var = var;
    }

    public void release() {
        super.release();
        this.var = null;
    }

    
    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if (var != null) {
            component.getAttributes().put("var", var);
        }
    }

    public String getRendererType() { 
        return ("Data"); 
    }    

    public String getComponentType() { 
        return ("PanelData"); 
    }    

}
