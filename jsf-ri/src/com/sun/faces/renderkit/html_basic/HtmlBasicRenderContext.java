
/*
 * $Id: HtmlBasicRenderContext.java,v 1.7 2001/12/03 22:47:10 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
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
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;

import java.util.Locale;

/**
 *
 *  <B>HtmlBasicRenderContext</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderContext.java,v 1.7 2001/12/03 22:47:10 edburns Exp $
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

} // end of class HtmlBasicRenderContext
