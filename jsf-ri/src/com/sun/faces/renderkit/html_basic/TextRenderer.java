/*
 * $Id: TextRenderer.java,v 1.5 2001/11/08 23:36:49 edburns Exp $
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
 * @version $Id: TextRenderer.java,v 1.5 2001/11/08 23:36:49 edburns Exp $
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
    
    public Iterator getSupportedAttributeNames(String componentType) {
        return null;
    }

    public void renderStart(RenderContext rc, WComponent c )
            throws IOException { 
        try {
            WOutput label = null;
            if ( c instanceof WOutput ) {
                label = (WOutput) c;
            }
            String text = (String) label.getValue();
            if ( text != null ) { 
                OutputMethod outputMethod = rc.getOutputMethod();
                StringBuffer out = new StringBuffer();
                out.append(text);
                outputMethod.writeText(out.toString());
                outputMethod.flush();
            }
            
        } catch(IOException ioe) {
            System.err.println("Error rendering Output_Text: " + ioe);
        }
        return;
    }


    public void renderChildren(RenderContext rc, WComponent c) 
            throws IOException {
        return;
    }

    public void renderEnd(RenderContext rc, WComponent c) 
            throws IOException {
        return;
    }

    public boolean supportsType(String componentType) {
        return true;
    }
    
    public boolean supportsType(WComponent c) {
        return true;
    }


    // ----VERTIGO_TEST_START

    //
    // Test methods
    //

    public static void main(String [] args)
    {
        Assert.setEnabled(true);
        TextRenderer me = new TextRenderer();
        Log.setApplicationName("TextRenderer");
        Log.setApplicationVersion("0.0");
        Log.setApplicationVersionDate("$Id: TextRenderer.java,v 1.5 2001/11/08 23:36:49 edburns Exp $");
    
    }  

    // ----VERTIGO_TEST_END

} // end of class TextRenderer
