/*
 * $Id: Command_HyperlinkTag.java,v 1.33 2003/07/29 16:25:23 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;

import com.sun.faces.taglib.FacesTag;


/**
 * This class is the tag handler that evaluates the 
 * <code>command_hyperlink</code> custom tag.
 */

public class Command_HyperlinkTag extends FacesTag
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
    protected String commandname = null;
    protected String image = null;
    protected String actionRef = null;

    
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

    public void setCommandName(String newCommandname) {
        ParameterCheck.nonNull(newCommandname);
        commandname = newCommandname;
    }
 
    public void setImage(String newImage) {
        image = newImage;
    }
    
    public void setActionRef(String newActionRef) {
        actionRef = newActionRef;
    }

    //
    // General Methods
    //

    public String getLocalRendererType() { 
        return "Hyperlink"; 
    }
    public String getComponentType() { 
        return "Command"; 
    }



    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UICommand link = (UICommand) component;
        if (actionRef != null ) {
	    link.setActionRef(actionRef);
	}
        if (action != null ) {
	    link.setAction(action);
	}
        if (null != label) {
            link.setValue(label);
        }
        if (null != image) {
            link.setAttribute("image", image);
        }
    }

    
    //
    // Methods from TagSupport
    // 


} // end of class Command_HyperlinkTag
