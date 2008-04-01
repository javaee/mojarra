/*
 * $Id: Command_ButtonTag.java,v 1.29 2002/08/30 00:14:05 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
 * @version $Id: Command_ButtonTag.java,v 1.29 2002/08/30 00:14:05 jvisvanathan Exp $
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


    public void setType(String buttonType) {
        type = buttonType;
    }

    public String getType() {
        return type;
    }

    //
    // General Methods
    //

    public String getLocalRendererType() { return "ButtonRenderer"; }
    
    public UIComponent createComponent() {
        return(new UICommand());
    }    

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UICommand button = (UICommand) component;

	if (null == button.getCommandName()) {
	    button.setCommandName(getCommandName());
	}
	if (null == button.getAttribute("type")) {
            button.setAttribute("type", getType());
        }
    }


    //
    // Methods from TagSupport
    // 


} // end of class Command_ButtonTag
