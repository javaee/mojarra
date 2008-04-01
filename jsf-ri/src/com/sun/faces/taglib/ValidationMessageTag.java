/*
 * $Id: ValidationMessageTag.java,v 1.1 2002/07/23 18:04:09 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidationMessageTag.java

package com.sun.faces.taglib;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import javax.faces.webapp.FacesTag;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;

import java.util.Iterator;

import java.io.IOException;

/**
 *

 *  <B>ValidationMessageTag</B> is a convenience tag allowing content
 *  authors flexible validation error reporting.  See the tld for
 *  attribute description.

 *

 * @version $Id: ValidationMessageTag.java,v 1.1 2002/07/23 18:04:09 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ValidationMessageTag extends TagSupport
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

protected String componentId = null;
protected String showAll = null;
protected String color = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public ValidationMessageTag()
{
    super();
}

//
// Class methods
//

//
// Accessors
//

public String getComponentId()
{
    return componentId;
}

public void setComponentId(String newComponentId)
{
    componentId = newComponentId;
}

public String getShowAll()
{
    return showAll;
}

public void setShowAll(String newShowAll)
{
    showAll = newShowAll;
}

public String getColor()
{
    return color;
}

public void setColor(String newColor)
{
    color = newColor;
}



//
// General Methods
//

//
// Methods from TagSupport
//

public int doEndTag() throws JspException 
{
    Tag tag = getParent();
    FacesContext facesContext = null;
    UIComponent parentComponent;
    Iterator messageIter = null;
    String curColor = null;
    JspWriter out = null;
    Message curMessage = null;

    while ((tag != null) && !(tag instanceof FacesTag)) {
	tag = tag.getParent();
    }
    if (tag == null) { // FIXME - i18n
	throw new JspException("Not nested in a FacesTag");
    }
    parentComponent = ((FacesTag)tag).getComponent();
    Assert.assert_it(null != parentComponent);

    facesContext = (FacesContext) 
	pageContext.getAttribute(FacesContext.FACES_CONTEXT_ATTR, 
				 PageContext.REQUEST_SCOPE);
    Assert.assert_it(null != facesContext);

    // If the tag says to show all messages...
    if (null != getShowAll()) {
	messageIter = facesContext.getMessagesAll();
    }
    else {
	// if not, if there is a component for which messages should be
	// specified...
	if (null != getComponentId()) {
	    try {
		parentComponent = 
		    parentComponent.findComponent(getComponentId());
	    }
	    catch (Throwable e) {
		return EVAL_PAGE;
		// PENDING(edburns): do something
	    }
	}
	// else, just use the first UIComponent ancestor.
	messageIter = facesContext.getMessages(parentComponent);
    }
    Assert.assert_it(null != messageIter);
    
    if (null == (curColor = getColor())) {
	color = "RED";
    }
    try {
	if (messageIter.hasNext()) {
	    out = pageContext.getOut();
	    out.println("\n<FONT COLOR=\"" + color + "\">");
	}
	while (messageIter.hasNext()) {
	    curMessage = (Message) messageIter.next();
	    out.println("\t" + curMessage.getSummary() + "<BR>");
	    out.println("\tSeverity: " + curMessage.getSeverity() + "<BR>");
	    out.println("\t" + curMessage.getDetail());
	}
	if (null != out) {
	    out.println("</FONT>");
	}
    }
    catch (IOException e) {
	// PENDING(edburns): do something with this
    }
	
    return EVAL_PAGE;
}

// The testcase for this class is Faces_Basic 


} // end of class ValidationMessageTag
