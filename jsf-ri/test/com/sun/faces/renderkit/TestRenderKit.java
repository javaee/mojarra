/*
 * $Id: TestRenderKit.java,v 1.24 2006/03/29 23:05:00 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// TestRenderKit.java

package com.sun.faces.renderkit;

import com.sun.faces.cactus.FileOutputResponseWriter;
import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.renderkit.html_basic.FormRenderer;
import com.sun.faces.renderkit.html_basic.TextRenderer;
import org.apache.cactus.ServletTestCase;

import javax.faces.FactoryFinder;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;

import javax.servlet.ServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.cactus.WebRequest;

/**
 * <B>TestRenderKit</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKit.java,v 1.24 2006/03/29 23:05:00 rlubke Exp $
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

    public TestRenderKit() {
        super("TestRenderKit");
    }


    public TestRenderKit(String name) {
        super(name);
    }
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
        Renderer renderer = renderKit.getRenderer("javax.faces.Form",
                                                  "javax.faces.Form");
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
        assertTrue(
            renderKit.getRenderer("Form", "Form") instanceof FormRenderer);
        renderKit.addRenderer("Form", "Form", textRenderer);
        assertTrue(
            renderKit.getRenderer("Form", "Form") instanceof TextRenderer);

        bool = false;
        try {
            renderKit.addRenderer("BlahFamily", null, formRenderer);
        } catch (NullPointerException e) {
            bool = true;
        }
        assertTrue(bool);

        bool = false;
        try {
            renderKit.addRenderer(null, "BlahRenderer", formRenderer);
        } catch (NullPointerException e) {
            bool = true;
        }
        assertTrue(bool);

        bool = false;
        try {
            renderKit.addRenderer("BlahFamily", "BlahRenderer", null);
        } catch (NullPointerException e) {
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
        stream.write(new byte[]{(byte) 'c', (byte) 'd', (byte) 'e'}, 1, 2);
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
	// use an invalid encoding
        try {
            renderKit.createResponseWriter(new StringWriter(), null, "foo");

            fail("IllegalArgumentException Should Have Been Thrown!");

        } catch (IllegalArgumentException iae) {
        }
	
	ResponseWriter writer = null;

	// see that the proper content type is picked up based on the
	// contentTypeList param
	writer = renderKit.createResponseWriter(new StringWriter(), 
						"application/xhtml+xml,text/html", 
						"ISO-8859-1");
	assertEquals(writer.getContentType(), "application/xhtml+xml");
	writer = renderKit.createResponseWriter(new StringWriter(), 
						"text/html,application/xhtml+xml",
						"ISO-8859-1");
	assertEquals(writer.getContentType(), "text/html");

	// see that IAE is thrown if the content type isn't known
	try {
	    writer = renderKit.createResponseWriter(new StringWriter(), 
						    "application/pdf",
						    "ISO-8859-1");
	    
            fail("IllegalArgumentException Should Have Been Thrown!");

        } catch (IllegalArgumentException iae) {
        }

    }

    public void beginCreateResponseWriterAllMedia(WebRequest theRequest) {
        theRequest.addHeader("Accept", "*/*");
    }

    public void testCreateResponseWriterAllMedia() throws Exception {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
                                                            RenderKitFactory.HTML_BASIC_RENDER_KIT);
        ResponseWriter writer = null;

        // see that the proper content type is picked up based on the
        // accept header  
        writer = renderKit.createResponseWriter(new StringWriter(), null, "ISO-8859-1");
        assertEquals(writer.getContentType(), "text/html");
    }

    public void beginCreateResponseWriter1(WebRequest theRequest) {
        theRequest.addHeader("Accept", "text/html; q=0.2, application/xhtml+xml; q=0.8, application/xml; q=0.5, */*");
    }

    public void testCreateResponseWriter1() throws Exception {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
                                                            RenderKitFactory.HTML_BASIC_RENDER_KIT);
        ResponseWriter writer = null;

        // see that the proper content type is picked up based on the
        // accept header  
        writer = renderKit.createResponseWriter(new StringWriter(), null, "ISO-8859-1");
        assertEquals(writer.getContentType(), "application/xhtml+xml");
    }

    public void beginCreateResponseWriter2(WebRequest theRequest) {
        theRequest.addHeader("Accept", "text/html; q=0.2, application/xhtml+xml; q=0.8, application/xml; q=0.9, */*");
    }

    public void testCreateResponseWriter2() throws Exception {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
                                                            RenderKitFactory.HTML_BASIC_RENDER_KIT);
        ResponseWriter writer = null;
                                                                                                                   
        // see that the proper content type is picked up based on the
        // accept header
        writer = renderKit.createResponseWriter(new StringWriter(), null, "ISO-8859-1");
        assertEquals(writer.getContentType(), "application/xhtml+xml");
    }

    public void beginCreateResponseWriter3(WebRequest theRequest) {
        theRequest.addHeader("Accept", "text/html, application/xhtml+xml; q=0.8, application/xml; q=0.9, */*");
    }
                                                                                                                   
    public void testCreateResponseWriter3() throws Exception {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
                                                            RenderKitFactory.HTML_BASIC_RENDER_KIT);
        ResponseWriter writer = null;
                                                                                                                   
        // see that the proper content type is picked up based on the
        // accept header
        writer = renderKit.createResponseWriter(new StringWriter(), null, "ISO-8859-1");
        assertEquals(writer.getContentType(), "text/html");
    }

    public void beginCreateResponseWriter4(WebRequest theRequest) {
        theRequest.addHeader("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, */*");
        theRequest.addHeader("Accept", "text/html; level=1");
    }
                                                                                                                           
    public void testCreateResponseWriter4() throws Exception {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
                                                            RenderKitFactory.HTML_BASIC_RENDER_KIT);
        ResponseWriter writer = null;
                                                                                                                           
        // see that the proper content type is picked up based on the
        // accept header
        writer = renderKit.createResponseWriter(new StringWriter(), null, "ISO-8859-1");
        assertEquals(writer.getContentType(), "text/html");
    }

    // Response has unsupported content type..
    public void testCreateResponseWriter5() throws Exception {
        ((ServletResponse)getFacesContext().getExternalContext().getResponse()).setContentType("image/png");
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
                                                            RenderKitFactory.HTML_BASIC_RENDER_KIT);
        ResponseWriter writer = null;
                                                                                                                         
        // see that the proper content type is picked up based on the
        // accept header
        writer = renderKit.createResponseWriter(new StringWriter(), null, "ISO-8859-1");
        assertEquals(writer.getContentType(), "text/html");
    }


} // end of class TestRenderKit
