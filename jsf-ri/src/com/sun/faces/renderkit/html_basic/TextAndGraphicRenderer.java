/*
 * $Id: TextAndGraphicRenderer.java,v 1.10 2002/03/16 00:09:37 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextAndGraphicRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.UIComponent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>TextAndGraphicRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextAndGraphicRenderer.java,v 1.10 2002/03/16 00:09:37 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextAndGraphicRenderer extends Object implements Renderer
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

public TextAndGraphicRenderer()
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

public boolean supportsComponentType(UIComponent c) {
    return false;
}

public boolean supportsComponentType(String componentType) {
    return false;
}
    public Iterator getSupportedAttributeNames(String componentType) throws FacesException {
        return null;
    }

    public Iterator getSupportedAttributes(String componentType) throws FacesException {
	return null;
    }

    public PropertyDescriptor getAttributeDescriptor(String attributeName)
	throws FacesException {
	return null;
    }


public void renderStart(RenderContext rc, UIComponent c) 
    throws IOException, FacesException {
    return;
}

public void renderChildren(RenderContext rc, UIComponent c) throws IOException {
    return;
}

public void renderComplete(RenderContext rc, UIComponent c) throws IOException {
    return;
}

public boolean getCanRenderChildren(RenderContext rc, UIComponent c) {
    return false;
}

} // end of class TextAndGraphicRenderer
