/*
 * $Id: HtmlBasicRenderContextFactory.java,v 1.2 2001/11/17 01:33:00 edburns Exp $
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
 * @version $Id: HtmlBasicRenderContextFactory.java,v 1.2 2001/11/17 01:33:00 edburns Exp $
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
    RenderContext result = new HtmlBasicRenderContext();
    return result;
}

//
// General Methods
//

} // end of class HtmlBasicRenderContextFactory
