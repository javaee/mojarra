/*
 * $Id: TestRenderKit.java,v 1.12 2004/01/30 07:02:13 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderKit.java

package com.sun.faces.renderkit;

import com.sun.faces.renderkit.html_basic.FormRenderer;
import com.sun.faces.renderkit.html_basic.TextRenderer;

import java.io.Writer;
import java.util.Iterator;

import javax.faces.component.UIOutput;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.ResponseStream;

import com.sun.faces.util.Util;


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
 * @version $Id: TestRenderKit.java,v 1.12 2004/01/30 07:02:13 rkitain Exp $
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
        renderKit = renderKitFactory.getRenderKit(getFacesContext(),
						  RenderKitFactory.HTML_BASIC_RENDER_KIT);

        // 1. Verify "getRenderer()" returns a Renderer instance
        //  
        Renderer renderer = renderKit.getRenderer("javax.faces.Form", "javax.faces.Form");
        assertTrue(renderer instanceof FormRenderer);

	// 2. Verify "getRenderer()" returns null
	// 
	renderer = renderKit.getRenderer("Foo", "Bar");
	assertTrue(renderer == null);

	// 3. Verify NPE
	//
	boolean exceptionThrown = false;
	try {
	    renderer = renderKit.getRenderer(null, null);
	} catch (NullPointerException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
    }

    public void testAddRenderer() {
	boolean bool = false;
	FormRenderer formRenderer = new FormRenderer();
	TextRenderer textRenderer = new TextRenderer();

        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = renderKitFactory.getRenderKit(getFacesContext(),
						  RenderKitFactory.HTML_BASIC_RENDER_KIT);
	// Test to see if addRenderer replaces the renderer if given
	// the same rendererType.
	//
        renderKit.addRenderer("Form", "Form", formRenderer);
	assertTrue(renderKit.getRenderer("Form", "Form") instanceof FormRenderer);
        renderKit.addRenderer("Form", "Form", textRenderer);
	assertTrue(renderKit.getRenderer("Form", "Form") instanceof TextRenderer);

	bool = false;
	try {
	    renderKit.addRenderer("BlahFamily", null, formRenderer);
	}
	catch (NullPointerException e) {
	    bool = true;
	}
	assertTrue(bool);

	bool = false;
	try {
	    renderKit.addRenderer(null, "BlahRenderer", formRenderer);
	}
	catch (NullPointerException e) {
	    bool = true;
	}
	assertTrue(bool);
	
	bool = false;
	try {
	    renderKit.addRenderer("BlahFamily", "BlahRenderer", null);
	}
	catch (NullPointerException e) {
	    bool = true;
	}
	assertTrue(bool);
	
    }
    
    public void testCreateResponseStream() throws Exception {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = renderKitFactory.getRenderKit(getFacesContext(),
						  RenderKitFactory.HTML_BASIC_RENDER_KIT);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ResponseStream stream = renderKit.createResponseStream(out);
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

    public void testCreateResponseWriter() throws Exception {

        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
						  RenderKitFactory.HTML_BASIC_RENDER_KIT);
	boolean exceptionThrown = false;
        try {
            renderKit.createResponseWriter(new Writer() {
                public void close() throws IOException {
                }
                public void flush() throws IOException {
                }
                public void write(char cbuf) throws IOException {
                }
                public void write(char[] cbuf, int off,
                    int len) throws IOException {
                }
                public void write(int c) throws IOException {
                }
                public void write(String str) throws IOException {
                }
                public void write(String str, int off,
                    int len) throws IOException {
                }
            }, null, "foo");

	} catch (IllegalArgumentException iae) {
	    exceptionThrown = true;
	}

	assertTrue(exceptionThrown);
    }

} // end of class TestRenderKit
