/*
 * $Id: TestRenderKit.java,v 1.16.32.3 2007/04/27 21:28:25 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TestRenderKit.java

package com.sun.faces.renderkit;

import com.sun.faces.FileOutputResponseWriter;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.renderkit.html_basic.FormRenderer;
import com.sun.faces.renderkit.html_basic.TextRenderer;
import org.apache.cactus.ServletTestCase;

import javax.faces.FactoryFinder;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.apache.cactus.WebRequest;

/**
 * <B>TestRenderKit</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKit.java,v 1.16.32.3 2007/04/27 21:28:25 ofung Exp $
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
        Writer wrappedWriter = new Writer() {
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
            };

        ResponseWriter writer = null;

        // see that the proper content type is picked up based on the
        // accept header
            writer = renderKit.createResponseWriter(wrappedWriter, null, "ISO-8859-1");
            assertEquals(writer.getContentType(), "text/html");

    }




} // end of class TestRenderKit
