/*
 * $Id: HyperlinkRenderer.java,v 1.14 2002/04/05 19:41:16 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HyperlinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.FacesContext;
import javax.faces.Renderer;
import javax.faces.UICommand;
import javax.faces.UIComponent;

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
 * @version $Id: HyperlinkRenderer.java,v 1.14 2002/04/05 19:41:16 jvisvanathan Exp $
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

    public boolean supportsComponentType(UIComponent c) { 
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof UICommand ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsComponentType(String componentType) { 
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_UICOMMAND)) {
            supports = true;
        }
        return supports;
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

    public void renderStart(FacesContext fc, UIComponent c) 
        throws IOException, FacesException {
        ParameterCheck.nonNull(fc);
        ParameterCheck.nonNull(c);

        UICommand wCommand = null;
        if ( supportsComponentType(c)) {
            wCommand = (UICommand) c;
        } else {
            throw new FacesException("Invalid component type. " +
                      "Expected UICommand");
        }

        OutputMethod outputMethod = fc.getOutputMethod();
        Assert.assert_it(outputMethod != null );
 
        StringBuffer output = new StringBuffer();
        output.append("<A HREF=\"");
        output.append(wCommand.getAttribute(fc, "target"));
        output.append("\">");
        if (wCommand.getAttribute(fc, "image") != null) {
            output.append("<IMAGE SRC=\"");
            output.append(wCommand.getAttribute(fc, "image"));
            output.append("\">");
        }
        if (wCommand.getAttribute(fc, "text") != null) {
            output.append(wCommand.getAttribute(fc, "text"));
        }
        output.append("</A>");

        outputMethod.writeText(output.toString());
        outputMethod.flush(); 
    }

    public void renderChildren(FacesContext fc, UIComponent c) 
        throws IOException {
        return;
    }

    public void renderComplete(FacesContext fc, UIComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean getCanRenderChildren(FacesContext fc, UIComponent c) {
        return false;
    }

} // end of class HyperlinkRenderer
