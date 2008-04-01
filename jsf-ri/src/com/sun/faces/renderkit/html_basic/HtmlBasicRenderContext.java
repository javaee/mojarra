
/*
 * $Id: HtmlBasicRenderContext.java,v 1.1 2001/11/07 00:18:33 rogerk Exp $
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

import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.RenderKit;
import javax.servlet.ServletRequest;
import java.util.Locale;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>HtmlBasicRenderContext</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderContext.java,v 1.1 2001/11/07 00:18:33 rogerk Exp $
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

    private RenderKit renderKit;
    private OutputMethod outputMethod;

//
// Constructors and Initializers    
//

public HtmlBasicRenderContext() {
    renderKit = RenderKit.getRenderKitForClient(null);
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
System.out.println("HTMLBASICRENDERCONTEXT.SETOUTPUTMETHOD:OUT:"+om);
//    ParameterCheck.nonNull(om);
    outputMethod = om;
}




// ----VERTIGO_TEST_START

//
// Test methods
//

public static void main(String [] args)
{
    Assert.setEnabled(true);
    HtmlBasicRenderContext me = new HtmlBasicRenderContext();
    Log.setApplicationName("HtmlBasicRenderContext");
    Log.setApplicationVersion("0.0");
    Log.setApplicationVersionDate("$Id: HtmlBasicRenderContext.java,v 1.1 2001/11/07 00:18:33 rogerk Exp $");
    
}

// ----VERTIGO_TEST_END

} // end of class HtmlBasicRenderContext
