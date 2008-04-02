/*
 * $Id: Command_HyperlinkTag.java,v 1.32 2003/07/16 00:00:08 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
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

    protected String href = null;
    protected String label = null;
    protected String commandname = null;
    protected String image = null;
    protected String actionRef = null;
    protected String action = null;
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Command_HyperlinkTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    public String getCommandName() { 
        return commandname; 
    }
    public void setCommandName(String newCommandname) {
        ParameterCheck.nonNull(newCommandname);
        commandname = newCommandname;
    }

    public void setHref(String newHref) {
	href = newHref;
    }

    public String getHref() {
	return href;
    }
    
    public String getLabel() { 
        return label; 
    }
    public void setLabel(String newLabel) { 
        label = newLabel;
    }
 
    public void setImage(String newImage) {
        image = newImage;
    }

    public String getImage() {
        return image;
    }
    
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
            component.setAttribute("label", label);
        }
        if (null != image) {
            component.setAttribute("image", image);
        }
        if (null != href) {
	    component.setAttribute("href", href);
	}
    }

    
    //
    // Methods from TagSupport
    // 


} // end of class Command_HyperlinkTag
