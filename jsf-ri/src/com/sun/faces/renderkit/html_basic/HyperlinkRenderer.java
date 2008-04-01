/*
 * $Id: HyperlinkRenderer.java,v 1.7 2001/11/21 22:32:39 visvan Exp $
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
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WCommand;
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
 * @version $Id: HyperlinkRenderer.java,v 1.7 2001/11/21 22:32:39 visvan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HyperlinkRenderer extends Object implements Renderer {
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

    public HyperlinkRenderer() {
        super(); 
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init() {
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
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof WCommand ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsType(String componentType) { 
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals("WCommand")) {
            supports = true;
        }
        return supports;
    }

    public Iterator getSupportedAttributeNames(String componentType) {
        return null;
    }

    public void renderStart(RenderContext rc, WComponent c) 
        throws IOException, FacesException {
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        WCommand wCommand = null;
        if ( supportsType(c)) {
            wCommand = (WCommand) c;
        } else {
            throw new FacesException("Invalid component type. " +
                      "Expected WCommand");
        }

        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );
 
        StringBuffer output = new StringBuffer();
        output.append("<a href=");
        output.append(wCommand.getAttribute(rc, "target"));
        output.append(">");
        if (wCommand.getAttribute(rc, "image") != null) {
            output.append("<img src=");
            output.append(wCommand.getAttribute(rc, "image"));
            output.append(">");
        }
        if (wCommand.getAttribute(rc, "text") != null) {
            output.append(wCommand.getAttribute(rc, "text"));
        }
        output.append("</a>");

        outputMethod.writeText(output.toString());
        outputMethod.flush(); 
    }

    public void renderChildren(RenderContext rc, WComponent c) 
        throws IOException {
        return;
    }

    public void renderEnd(RenderContext rc, WComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean getCanRenderChildren(RenderContext rc, WComponent c) {
        return false;
    }

} // end of class HyperlinkRenderer
