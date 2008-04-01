/*
 * $Id: HtmlBasicRenderContext.java,v 1.9 2001/12/20 22:26:39 ofung Exp $
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

import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.RenderKit;
import javax.faces.WComponent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;

import java.util.Locale;
import java.util.Stack;

/**
 *
 *  <B>HtmlBasicRenderContext</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderContext.java,v 1.9 2001/12/20 22:26:39 ofung Exp $
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
