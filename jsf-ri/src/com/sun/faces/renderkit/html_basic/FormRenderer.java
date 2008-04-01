/*
 * $Id: FormRenderer.java,v 1.5 2001/11/17 01:32:59 edburns Exp $
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

// FormRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WForm;
import javax.faces.WComponent;

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
 * @version $Id: FormRenderer.java,v 1.5 2001/11/17 01:32:59 edburns Exp $
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

    public Iterator getSupportedAttributeNames(String componentType) {
        return null;
    }

    public boolean supportsType(String componentType) {
        return true;
    }

    public boolean supportsType(WComponent c) {
        return true;
    }


    public void renderStart(RenderContext rc, WComponent c ) 
            throws IOException {
        // render the form
        try {
            WForm form = null;
            OutputMethod outputMethod = rc.getOutputMethod();
            if ( c instanceof WForm ) {
                form = (WForm) c;
            }
            StringBuffer out = new StringBuffer();
            out.append("<FORM ");

            String form_name = (String) form.getAttribute(rc, "name");
            if (form_name != null) {
                out.append("NAME=\"" + form_name + "\"");
            }
            out.append(">");
            outputMethod.writeText(out.toString());
            outputMethod.flush();

        } catch(IOException ioe) {
            System.err.println("Error rendering Form Tag: " + ioe);
        }
    }

    public void renderChildren(RenderContext rc, 
            WComponent c) throws IOException {
        return;
    }

    public void renderEnd(RenderContext rc, WComponent c )
            throws IOException {

        // render the form
        try {
            OutputMethod outputMethod = rc.getOutputMethod();
            StringBuffer out = new StringBuffer();
            out.append("</FORM>");
            outputMethod.writeText(out.toString());
            outputMethod.flush();
        } catch(IOException ioe) {
            System.err.println("Error rendering Form Tag: " + ioe);
        }

        return;
    }


} // end of class FormRenderer
