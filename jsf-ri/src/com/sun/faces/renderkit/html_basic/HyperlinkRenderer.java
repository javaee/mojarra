/*
 * $Id: HyperlinkRenderer.java,v 1.3 2001/11/08 20:55:26 edburns Exp $
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

// HyperlinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WComponent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>HyperlinkRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HyperlinkRenderer.java,v 1.3 2001/11/08 20:55:26 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HyperlinkRenderer extends Object implements Renderer
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

public HyperlinkRenderer()
{
    super();
    // ParameterCheck.nonNull();
    this.init();
}

protected void init()
{
    // super.init();
}

//
// Class methods
//

//
// General Methods
//

//
// Methods From Renderer
//

public boolean supportsType(WComponent c) {
    return false;
}

public boolean supportsType(String componentType) {
    return false;
}

public Iterator getSupportedAttributeNames(String componentType) {
    return null;
}

public void renderStart(RenderContext rc, WComponent c) throws IOException {
    return;
}

public void renderChildren(RenderContext rc, WComponent c) throws IOException {
    return;
}

public void renderEnd(RenderContext rc, WComponent c) throws IOException {
    return;
}

public boolean getCanRenderChildren(RenderContext rc, WComponent c) {
    return false;
}


// ----VERTIGO_TEST_START

//
// Test methods
//

public static void main(String [] args)
{
    Assert.setEnabled(true);
    HyperlinkRenderer me = new HyperlinkRenderer();
    Log.setApplicationName("HyperlinkRenderer");
    Log.setApplicationVersion("0.0");
    Log.setApplicationVersionDate("$Id: HyperlinkRenderer.java,v 1.3 2001/11/08 20:55:26 edburns Exp $");
    
}

// ----VERTIGO_TEST_END

} // end of class HyperlinkRenderer
