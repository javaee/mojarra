/*
 * $Id: HtmlBasicRenderContext.java,v 1.10 2002/01/10 22:20:10 edburns Exp $
 */


/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicRenderContext.java

package com.sun.faces.renderkit.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.OutputMethod;
import javax.faces.ObjectManager;
import javax.faces.RenderContext;
import javax.faces.RenderKit;
import javax.faces.WComponent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;

import java.util.Locale;
import java.util.Stack;

/**
 *
 *  <B>HtmlBasicRenderContext</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderContext.java,v 1.10 2002/01/10 22:20:10 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */


public class HtmlBasicRenderContext extends RenderContext {
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

    private RenderKit renderKit = null;
    private OutputMethod outputMethod = null;

    private HttpSession session = null;
    private ServletRequest request;

    private Stack stack;

    private ObjectManager objectManager = null;

//
// Constructors and Initializers    
//

public HtmlBasicRenderContext(ServletRequest req) {
    ParameterCheck.nonNull(req);
    request = req;
    if (req instanceof HttpServletRequest) {
	session = ((HttpServletRequest)req).getSession();
    }
    renderKit = new HtmlBasicRenderKit();

    stack = new Stack();
}

//
// Class methods
//

//
// General Methods
//
// Methods from RenderContext.
//
public RenderKit getRenderKit() {
    return renderKit;
}

public Locale getLocale(){
    return null;
}

public ObjectManager getObjectManager() {
    if (null == objectManager) {
	Assert.assert_it(null != session);
	// The per-app ObjectManager instance is created in the
	// FacesServlet.init() method, and stored in the
	// ServletContext's attr set.
	objectManager = (ObjectManager) session.getServletContext().
	    getAttribute(Constants.REF_OBJECTMANAGER);
	Assert.assert_it(null != objectManager, 
			 "ObjectManager cannot be null.  Should already be created.");
    }
    return objectManager;
}

public OutputMethod getOutputMethod() {
    return outputMethod;
}

public void setOutputMethod(OutputMethod om) {
    ParameterCheck.nonNull(om);
    outputMethod = om;
}

public HttpSession getSession() {
    return session;
}

public WComponent peekAtAncestor(int level) {
    WComponent c;
    try {
        c = (WComponent)stack.get(level);
        return c;
    } catch (ArrayIndexOutOfBoundsException e) {
        return null;
    }
}

public void pushChild(WComponent c){
    stack.push(c);
}

public WComponent popChild() {
    WComponent c;
    if (stack.empty()) {
        return null;
    } else { 
        c = (WComponent)stack.pop();
        return c;
    }
}

} // end of class HtmlBasicRenderContext
