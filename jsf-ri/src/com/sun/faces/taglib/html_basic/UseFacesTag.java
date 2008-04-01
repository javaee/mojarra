/*
 * $Id: UseFacesTag.java,v 1.9 2001/12/20 22:26:42 ofung Exp $
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
import javax.faces.ObjectTable;
import javax.faces.ObjectTableFactory;
import javax.faces.RenderKit;
import javax.faces.ObjectTable;

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
 * @version $Id: UseFacesTag.java,v 1.9 2001/12/20 22:26:42 ofung Exp $
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

    ObjectTable objectTable;
    ObjectTableFactory otf;
    ServletContext servletContext = pageContext.getServletContext();

    // Try to get the ObjectTable from the ServletContext
    //
    objectTable = (ObjectTable) servletContext.getAttribute(
        Constants.REF_OBJECTTABLE);
    Assert.assert_it(null != objectTable);

    // Get the RenderContext from the ObjectTable (session scope).  
    //
    RenderContextFactory rcf = null;
    RenderContext renderContext = null;

    renderContext = (RenderContext)objectTable.get(pageContext.getSession(),
        Constants.REF_RENDERCONTEXT);
    Assert.assert_it(null != renderContext);

    JspOutputMethod outputMethod = new JspOutputMethod();
    outputMethod.setPageContext(pageContext);
    renderContext.setOutputMethod(outputMethod);

    objectTable.put(pageContext.getSession(), Constants.REF_RENDERCONTEXT, 
        renderContext); 

    servletContext.setAttribute(Constants.REF_OBJECTTABLE, objectTable);
    
    return EVAL_BODY_INCLUDE;
}

} // end of class UseFacesTag 
