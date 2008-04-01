/*
 * $Id: CheckboxRenderer.java,v 1.4 2001/11/17 01:32:59 edburns Exp $
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

// CheckboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WComponent;
import javax.faces.WSelectBoolean;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>CheckboxRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CheckboxRenderer.java,v 1.4 2001/11/17 01:32:59 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CheckboxRenderer extends Object implements Renderer {
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

    public CheckboxRenderer() {
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
        return false;
    }

    public boolean supportsType(String componentType) {
        return false;
    }

    public Iterator getSupportedAttributeNames(String componentType) {
        return null;
    }

    public void renderStart(RenderContext rc, WComponent c) throws IOException {

        OutputMethod outputMethod = rc.getOutputMethod();
        WSelectBoolean wSelectBoolean = (WSelectBoolean)c;
        StringBuffer output = new StringBuffer();
        output.append("<input type=checkbox");
        if (wSelectBoolean.getAttribute(rc, "checked") != null) {
            output.append(" checked");
        }
        output.append(" name=");
        output.append(wSelectBoolean.getAttribute(rc, "name"));
        output.append(" value=");
        output.append(wSelectBoolean.getAttribute(rc, "value"));
        output.append(">");
        if (wSelectBoolean.getAttribute(rc, "label") != null) {
            output.append(" ");
            output.append(wSelectBoolean.getAttribute(rc, "label"));
        }

        outputMethod.writeText(output.toString());
        outputMethod.flush();
    }

    public void renderChildren(RenderContext rc, WComponent c) 
        throws IOException {
        return;
    }

    public void renderEnd(RenderContext rc, WComponent c) throws IOException {
        return;
    }

    public boolean getCanRenderChildren(RenderContext rc, WComponent c) {
        return false;
    }


} // end of class CheckboxRenderer
