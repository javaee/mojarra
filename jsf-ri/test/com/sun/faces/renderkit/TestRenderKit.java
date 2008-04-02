/*
 * $Id: TestRenderKit.java,v 1.4 2003/08/28 15:53:48 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderKit.java

package com.sun.faces.renderkit;

import com.sun.faces.renderkit.html_basic.FormRenderer;

import java.util.Iterator;

import javax.faces.component.UIOutput;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.ResponseStream;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;
import org.apache.cactus.ServletTestCase;

import com.sun.faces.CompareFiles;
import com.sun.faces.FileOutputResponseWrapper;
import com.sun.faces.FileOutputResponseWriter;
import com.sun.faces.ServletFacesTestCase;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

/**
 *
 *  <B>TestRenderKit</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKit.java,v 1.4 2003/08/28 15:53:48 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderKit extends ServletFacesTestCase {
//
// Protected Constants
//

public static final String OUTPUT_FILENAME = 
    FileOutputResponseWriter.FACES_RESPONSE_ROOT + "TestRenderKit_out";

public static final String CORRECT_OUTPUT_FILENAME = 
    FileOutputResponseWriter.FACES_RESPONSE_ROOT + "TestRenderKit_correct";



//
// Class Variables
//

//
// Instance Variables
//
    private RenderKit renderKit = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestRenderKit() {super("TestRenderKit");}
    public TestRenderKit(String name) {super(name);}
//
// Class methods
//

//
// General Methods
//

    public void testGetRenderer() {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = renderKitFactory.getRenderKit("DEFAULT");

        // 1. Verify "getRenderer()" returns a Renderer instance
        //  
        Renderer renderer = renderKit.getRenderer("Form");
        assertTrue(renderer instanceof FormRenderer);
    }

    public void testAddRenderer() {
	boolean bool = false;
	FormRenderer formRenderer = new FormRenderer();

        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = renderKitFactory.getRenderKit("DEFAULT");
	try {
	    renderKit.addRenderer("Form", formRenderer);
	}
	catch (IllegalArgumentException e) {
	    bool = true;
	}
	assertTrue(bool);

	bool = false;
	try {
	    renderKit.addRenderer(null, formRenderer);
	}
	catch (NullPointerException e) {
	    bool = true;
	}
	assertTrue(bool);

	bool = false;
	try {
	    renderKit.addRenderer("BlahRenderer", null);
	}
	catch (NullPointerException e) {
	    bool = true;
	}
	assertTrue(bool);
	
    }
    
    public void testGetResponseStream() throws Exception {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = renderKitFactory.getRenderKit("DEFAULT");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ResponseStream stream = renderKit.getResponseStream(out);
        stream.write('a');
        stream.write((byte) 'b');
        stream.write(new byte[] { (byte) 'c', (byte) 'd', (byte) 'e' }, 1, 2);
        stream.flush();
        String result = out.toString();
        assertTrue(result.equals("abde"));
        try {
            stream.close();
        } catch (IOException ioe) {
            ; // ignore
        }        
    }

} // end of class TestRenderKit
