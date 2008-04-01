/*
 * $Id: UseFacesTag.java,v 1.10 2002/01/10 22:20:12 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// UseFacesTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.renderkit.html_basic.JspOutputMethod;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.RenderContextFactory;
import javax.faces.ObjectManager;
import javax.faces.RenderKit;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.ServletContext;

/**
 *
 *  <B>UseFacesTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: UseFacesTag.java,v 1.10 2002/01/10 22:20:12 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class UseFacesTag extends TagSupport
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

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public UseFacesTag()
{
    super();
}

//
// Class methods
//

//
// General Methods
//

/**
 * This method initializes the <code>RenderContext</code>.
 * It is invoked when this tag is first encountered.
 * @return an integer value indicating subsequent page evaluation.
 * @throws a <code>JspException</code>
 */
public int doStartTag() throws JspException {

    ObjectManager objectManager;
    ServletContext servletContext = pageContext.getServletContext();

    // Try to get the ObjectManager from the ServletContext
    //
    objectManager = (ObjectManager) servletContext.getAttribute(
        Constants.REF_OBJECTMANAGER);
    Assert.assert_it(null != objectManager);

    // Get the RenderContext from the ObjectManager (session scope).  
    //
    RenderContextFactory rcf = null;
    RenderContext renderContext = null;

    renderContext = (RenderContext)objectManager.get(pageContext.getSession(),
        Constants.REF_RENDERCONTEXT);
    Assert.assert_it(null != renderContext);

    JspOutputMethod outputMethod = new JspOutputMethod();
    outputMethod.setPageContext(pageContext);
    renderContext.setOutputMethod(outputMethod);

    objectManager.put(pageContext.getSession(), Constants.REF_RENDERCONTEXT, 
        renderContext); 

    return EVAL_BODY_INCLUDE;
}

} // end of class UseFacesTag 
