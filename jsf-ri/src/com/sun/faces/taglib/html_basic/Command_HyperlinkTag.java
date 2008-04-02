/*
 * $Id: Command_HyperlinkTag.java,v 1.24 2002/10/22 21:26:58 jvisvanathan Exp $
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
 * @version $Id: Command_HyperlinkTag.java,v 1.24 2002/10/22 21:26:58 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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

    protected String target = null;
    protected String label = null;
    protected String commandname = null;
    protected String image = null;

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
    
    public String getLabel() { return label; }
    public void setLabel(String newLabel) { 
        label = newLabel;
    }
 
    public void setImage(String newImage) {
        image = newImage;
    }

    public String getImage() {
        return image;
    }

//
// General Methods
//

    public String getLocalRendererType() { return "Hyperlink"; }

    public UIComponent createComponent() {
        return (new UICommand());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UICommand link = (UICommand) component;
        if (null == link.getCommandName()) {
	    link.setCommandName(getCommandName());
	}
        if (null == component.getAttribute("label")) {
            component.setAttribute("label", getLabel());
        }
        if (null == component.getAttribute("image")) {
            component.setAttribute("image", getImage());
        }
	if (null == component.getAttribute("target")) {
	    component.setAttribute("target", getTarget());
	}
    }

    
//
// Methods from TagSupport
// 


} // end of class Command_HyperlinkTag
