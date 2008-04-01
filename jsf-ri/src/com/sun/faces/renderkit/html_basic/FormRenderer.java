/*
 * $Id: FormRenderer.java,v 1.11 2001/12/20 22:26:39 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util; // for saveToken()

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WForm;
import javax.faces.WComponent;

import javax.servlet.http.HttpSession;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>FormRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormRenderer.java,v 1.11 2001/12/20 22:26:39 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class FormRenderer extends Object implements Renderer
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
    public FormRenderer()
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

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_WFORM)) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsType(WComponent c) {

        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof WForm ) {
            supports = true;
        }
        return supports;
    }


    public void renderStart(RenderContext rc, WComponent c ) 
        throws IOException, FacesException {

        // render the form
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );
        
        WForm form = null; 
        if ( supportsType(c)) {
            form = (WForm) c;
        } else {
            throw new FacesException("Invalid component type. Expected WForm");
        }
        StringBuffer out = new StringBuffer();
        out.append("<FORM METHOD=\"POST\" ");

        String form_name = (String) form.getAttribute(rc, "name");
        if (form_name != null) {
            out.append("NAME=\"" + form_name + "\"");
        }
        out.append(">");
        outputMethod.writeText(out.toString());
        outputMethod.flush();
    }

    public void renderChildren(RenderContext rc, 
            WComponent c) throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, WComponent c )
            throws IOException, FacesException {
        // render the form
        OutputMethod outputMethod = rc.getOutputMethod();
        StringBuffer out = new StringBuffer();
	HttpSession session = rc.getSession();
	
	// Add a transaction token
        if (session != null) {
            String token = Util.saveToken(session);
            if (token != null) {
                out.append("\n<INPUT TYPE=\"HIDDEN\" NAME=\"");
                out.append(Constants.REQUEST_TOKEN_KEY);
                out.append("\" VALUE=\"");
                out.append(token);
                out.append("\">\n");
            }
        }
        out.append("</FORM>");
        outputMethod.writeText(out.toString());
        outputMethod.flush();
        return;
    }


} // end of class FormRenderer
