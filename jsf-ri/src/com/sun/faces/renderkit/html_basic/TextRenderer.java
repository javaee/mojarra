/*
 * $Id: TextRenderer.java,v 1.9 2001/11/29 00:12:33 edburns Exp $
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

// TextRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WOutput;
import javax.faces.WComponent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>TextRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextRenderer.java,v 1.9 2001/11/29 00:12:33 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextRenderer extends Object implements Renderer
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

    public TextRenderer()
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


    public void renderStart(RenderContext rc, WComponent c )
        throws IOException, FacesException { 

        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        WOutput label = null;
        if ( supportsType(c)) {
            label = (WOutput) c;
        } else {
            throw new FacesException("Invalid component type. Expected WOutput");
        }
        String text = (String) label.getValue();
        if ( text != null ) { 
            OutputMethod outputMethod = rc.getOutputMethod();
            Assert.assert_it(outputMethod != null );
            StringBuffer out = new StringBuffer();
            out.append(text);
            outputMethod.writeText(out.toString());
            outputMethod.flush();
        }
        return;
    }

    public void renderChildren(RenderContext rc, WComponent c) 
            throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, WComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals("WOutput")) {
            supports = true;
        }
        return supports;
    }
    
    public boolean supportsType(WComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof WOutput ) {
            supports = true;
        }
        return supports;
    }


} // end of class TextRenderer
