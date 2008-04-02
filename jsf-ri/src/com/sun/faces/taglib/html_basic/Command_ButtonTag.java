/*
 * $Id: Command_ButtonTag.java,v 1.33 2003/03/21 18:28:46 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Command_ButtonTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: Command_ButtonTag.java,v 1.33 2003/03/21 18:28:46 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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

    protected String commandname = null;
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

    public String getCommandName() { return commandname; }
    public void setCommandName(String newCommandname) { 
	ParameterCheck.nonNull(newCommandname);
	commandname = newCommandname;
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
    
    public UIComponent createComponent() {
        return(new UICommand());
    }    

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UICommand button = (UICommand) component;

	if (null == button.getCommandName()) {
	    button.setCommandName(getCommandName());
	}
        if (null == button.getActionRef() && actionRef != null ) {
	    button.setActionRef(actionRef);
	}
        if (null == button.getAction() && action != null ) {
	    button.setAction(action);
	}
        
	if (null == button.getAttribute("type")) {
            button.setAttribute("type", getType());
        }

        if (null == button.getAttribute("label")) {
            button.setAttribute("label", getLabel());
        }
        if (null == button.getAttribute("image")) {
            button.setAttribute("image", getImage());
        }
    }


    //
    // Methods from TagSupport
    // 


} // end of class Command_ButtonTag
