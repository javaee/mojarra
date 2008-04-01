/*
 * $Id: Command_HyperlinkTag.java,v 1.22 2002/07/19 22:44:25 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Command_HyperlinkTag.java

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
 * @version $Id: Command_HyperlinkTag.java,v 1.22 2002/07/19 22:44:25 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Command_HyperlinkTag extends Command_ButtonTag
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

    protected String target = null;


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
    public String getCommandName() { return commandname; }
    public void setCommandName(String newCommandname) {
        ParameterCheck.nonNull(newCommandname);
        commandname = newCommandname;
    }

    public void setTarget(String newTarget) {
	target = newTarget;
    }

    public String getTarget() {
	return target;
    }

//
// General Methods
//

    public String getLocalRendererType() { return "HyperlinkRenderer"; }

    public UIComponent createComponent() {
        return (new UICommand());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UICommand link = (UICommand) component;

	if (null == link.getAttribute("target")) {
	    link.setAttribute("target", getTarget());
	}
    }

    
//
// Methods from TagSupport
// 


} // end of class Command_HyperlinkTag
