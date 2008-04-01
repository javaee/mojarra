/*
 * $Id: TestRenderer.java,v 1.4 2001/12/20 22:26:44 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderer.java

package com.sun.faces.renderkit.html_basic;

import junit.framework.TestCase;
import java.io.IOException;
import javax.faces.RenderKit;
import javax.servlet.ServletRequest;
import javax.faces.RenderContextFactory;
import javax.faces.RenderContext;
import javax.faces.FacesException;
import javax.faces.*;
import com.sun.faces.renderkit.html_basic.*;

/**
 *
 *  <B>TestRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderer.java,v 1.4 2001/12/20 22:26:44 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderer extends TestCase
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
    RenderContext context;
    RenderContextFactory factory;
    javax.faces.Renderer rendererObj;
    java.util.Iterator typeIt;
    String type, rendererName;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public TestRenderer(String name) {
        super(name);
    }

    public void setUp() {
        // create renderContext
        try {
            factory = RenderContextFactory.newInstance();
            System.out.println("HtmlBasicRenderContextFactory: got factory: " +
                           factory);
            context = factory.newRenderContext(null);
            System.out.println("HtmlBasicRenderContextFactory: got context: " +
                           context);
        }
        catch (Exception e) {
            System.out.println("Exception getting factory!!! " + e.getMessage());
        }
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public void testRenderer() {

        WComponent c = null;

        // create TextRenderer instance.
        System.out.println("Testing TextRenderer");
        rendererObj = new TextRenderer();
        c = new WOutput();
        verifyRendererMethods(rendererObj,c, "WOutput");

        System.out.println("Testing FormRenderer");
        rendererObj = new FormRenderer();
        c = new WForm();
        verifyRendererMethods(rendererObj,c, "WForm");
        verifyRenderEnd(rendererObj,c, "WForm");

        System.out.println("Testing InputRenderer");
        rendererObj = new InputRenderer();
        c = new WTextEntry();
        verifyRendererMethods(rendererObj,c, "WTextEntry");

        System.out.println("Testing SecretRenderer");
        rendererObj = new SecretRenderer();
        c = new WTextEntry();
        verifyRendererMethods(rendererObj,c, "WTextEntry"); 

        System.out.println("Testing TextAreaRenderer"); 
        rendererObj = new TextAreaRenderer();
        c = new WTextEntry();
        verifyRendererMethods(rendererObj,c, "WTextEntry");
        verifyRenderEnd(rendererObj,c, "WTextEntry");

        System.out.println("Testing ButtonRenderer"); 
        rendererObj = new ButtonRenderer();
        c = new WCommand();
        verifyRendererMethods(rendererObj,c, "WCommand");

        System.out.println("Testing HyperLinkRenderer"); 
        rendererObj = new HyperlinkRenderer();
        c = new WCommand();
        verifyRendererMethods(rendererObj,c, "WCommand");

        System.out.println("Testing CheckBoxRenderer"); 
        rendererObj = new CheckboxRenderer();
        c = new WSelectBoolean();
        verifyRendererMethods(rendererObj,c, "WSelectBoolean");
        
    }

    protected void verifyRendererMethods(Renderer renderer, WComponent c, 
            String comp_name) {

         WOutput output = new WOutput();
	 boolean gotException = false;
        // test renderStart method
 
        System.out.println("Testing renderStart() method"); 
	gotException = false;
        try {
            renderer.renderStart(null, null);    
        } catch ( Exception e ) {
	    gotException = true;
            System.out.println("Expected exception: " + e.getMessage());
        }
	assertTrue(gotException);

	gotException = false;
        if ( ! comp_name.equals("WOutput") ) {
            try {
                renderer.renderStart(context,output);
            } catch ( Exception e ) {
	       gotException = true;
                System.out.println("Expected exception: " + e.getMessage());
            }
	    assertTrue(gotException);
        }

        /* test renderChildren method
        try {
            renderer.renderChildren(null, null);    
        } catch ( Exception e ) {
            System.out.println("Expected exception: in Renderer.renderChildren()");
        } */
  
        System.out.println("Testing supportType()");
        // test supportTypes method.
        boolean result = renderer.supportsType(c); 
        assertTrue(result);
        System.out.println("SupportsType() for component " + comp_name + 
                " returned " + result);

        result = renderer.supportsType(comp_name);    
        assertTrue(result);
        System.out.println("SupportsType() for component " + comp_name + 
                " returned " + result);
        System.out.println();
    }

    /**
     * This method tests the renderEnd method of renderers.
     * Some HTML elements don't have end tags. So this method
     * is separated out so that it can be invoked only for
     * renderers that implement renderEnd method. 
     */
    protected void verifyRenderEnd(Renderer renderer, WComponent c,
            String comp_name) {

        // test renderEnd method
        System.out.println("Testing renderEnd()");
        boolean gotException = false;
        try {
            renderer.renderComplete(null, null);
        } catch ( Exception e ) {
            gotException = true;
            System.out.println("Expected exception in Renderer.renderEnd()");
        }
        assertTrue(gotException);
        System.out.println();
    }


} // end of class TestRenderKit
