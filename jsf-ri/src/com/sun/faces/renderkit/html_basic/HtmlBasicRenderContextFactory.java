/*
 * $Id: HtmlBasicRenderContextFactory.java,v 1.4 2001/12/20 22:26:39 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicRenderContextFactory.java

package com.sun.faces.renderkit.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.faces.RenderContextFactory;
import javax.faces.RenderContext;
import javax.faces.FacesException;


/**
 *
 *  <B>HtmlBasicRenderContextFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderContextFactory.java,v 1.4 2001/12/20 22:26:39 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HtmlBasicRenderContextFactory extends RenderContextFactory
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

public HtmlBasicRenderContextFactory()
{
    super();
}

//
// Methods from RenderContextFactory
//

//
// Class methods
//

public RenderContext newRenderContext(ServletRequest request) throws FacesException {
    RenderContext result = new HtmlBasicRenderContext(request);
    return result;
}

//
// General Methods
//

} // end of class HtmlBasicRenderContextFactory
