/*
 * $Id: Command_ButtonTag.java,v 1.39 2003/07/16 00:00:08 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;

import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the <code>command_button</code>
 * custom tag.
 */

public class Command_ButtonTag extends FacesTag
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

    protected String label = null;
    protected String image = null;
    protected String actionRef = null;
    protected String action = null;
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Command_ButtonTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    public void setActionRef(String newActionRef) {
        actionRef = newActionRef;
    }

    public String getActionRef() {
        return actionRef;
    }
    
    public void setAction(String newAction) {
        action = newAction;
    }

    public String getAction() {
        return action;
    }
    
    public void setType(String buttonType) {
        type = buttonType;
    }

    public String getType() {
        return type;
    }

    public String getLabel() { return label; }
    public void setLabel(String newLabel) {
        label = newLabel;
    }

    public String getImage() { return image; }
    public void setImage(String newImage) {
        image = newImage;
    }

    //
    // General Methods
    //

    public String getLocalRendererType() { return "Button"; }
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
            button.setAttribute("type", type);
        }

        if (null != label) {
            button.setAttribute("label", label);
        }

        if (null != image) {
            button.setAttribute("image", image);
        }
    }


    //
    // Methods from TagSupport
    // 


} // end of class Command_ButtonTag
