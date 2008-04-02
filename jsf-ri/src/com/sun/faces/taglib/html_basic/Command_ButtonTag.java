/*
 * $Id: Command_ButtonTag.java,v 1.45 2003/09/25 16:36:28 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;

import com.sun.faces.taglib.BaseComponentTag;

/**
 * This class is the tag handler that evaluates the <code>command_button</code>
 * custom tag.
 */

public class Command_ButtonTag extends BaseComponentTag
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables
    protected String image = null;
    protected String actionRef = null;
    protected boolean immediate = false;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //


    //
    // Class methods
    //

    // 
    // Accessors
    //

    public void setActionRef(String newActionRef) {
        actionRef = newActionRef;
    }

    public void setImage(String newImage) {
        image = newImage;
    }

    public void setImmediate(boolean newImmediate) {
        immediate = newImmediate;
    }


    //
    // General Methods
    //

    public String getRendererType() { return "Button"; }
    public String getComponentType() { return "Command"; }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UICommand button = (UICommand) component;
        if (actionRef != null ) {
	    button.setActionRef(actionRef);
	}
        if (action != null ) {
	    button.setAction(action);
	}

        if (null != type) {
            button.getAttributes().put("type", type);
        }

        if (null != value) {
            button.setValue(value);
        }

        if (null != image) {
            button.getAttributes().put("image", image);
        }

	button.setImmediate(immediate);
    }


    //
    // Methods from TagSupport
    // 


} // end of class Command_ButtonTag
