/*
 * $Id: TestRenderer.java,v 1.7 2002/04/05 19:41:21 jvisvanathan Exp $
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
import javax.faces.FacesContextFactory;
import javax.faces.FacesContext;
import javax.faces.FacesException;
import javax.faces.*;
import com.sun.faces.renderkit.html_basic.*;
import com.sun.faces.FacesTestCase;

/**
 *
 *  <B>TestRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderer.java,v 1.7 2002/04/05 19:41:21 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderer extends FacesTestCase
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

    //
    // Class methods
    //

    //
    // General Methods
    //

    public void testRenderer() {

        UIComponent c = null;

        // create TextRenderer instance.
        System.out.println("Testing TextRenderer");
        rendererObj = new TextRenderer();
        c = new UIOutput();
        verifyRendererMethods(rendererObj,c, "UIOutput");

        System.out.println("Testing FormRenderer");
        rendererObj = new FormRenderer();
        c = new UIForm();
        verifyRendererMethods(rendererObj,c, "UIForm");
        verifyRenderEnd(rendererObj,c, "UIForm");

        System.out.println("Testing InputRenderer");
        rendererObj = new InputRenderer();
        c = new UITextEntry();
        verifyRendererMethods(rendererObj,c, "UITextEntry");

        System.out.println("Testing SecretRenderer");
        rendererObj = new SecretRenderer();
        c = new UITextEntry();
        verifyRendererMethods(rendererObj,c, "UITextEntry"); 

        System.out.println("Testing TextAreaRenderer"); 
        rendererObj = new TextAreaRenderer();
        c = new UITextEntry();
        verifyRendererMethods(rendererObj,c, "UITextEntry");
        verifyRenderEnd(rendererObj,c, "UITextEntry");

        System.out.println("Testing ButtonRenderer"); 
        rendererObj = new ButtonRenderer();
        c = new UICommand();
        verifyRendererMethods(rendererObj,c, "UICommand");

        System.out.println("Testing HyperLinkRenderer"); 
        rendererObj = new HyperlinkRenderer();
        c = new UICommand();
        verifyRendererMethods(rendererObj,c, "UICommand");

        System.out.println("Testing CheckBoxRenderer"); 
        rendererObj = new CheckboxRenderer();
        c = new UISelectBoolean();
        verifyRendererMethods(rendererObj,c, "UISelectBoolean");
        
    }

    protected void verifyRendererMethods(Renderer renderer, UIComponent c, 
            String comp_name) {

         UIOutput output = new UIOutput();
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
        if ( ! comp_name.equals("UIOutput") ) {
            try {
                renderer.renderStart(facesContext,output);
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
        boolean result = renderer.supportsComponentType(c); 
        assertTrue(result);
        System.out.println("SupportsType() for component " + comp_name + 
                " returned " + result);

        result = renderer.supportsComponentType(comp_name);    
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
    protected void verifyRenderEnd(Renderer renderer, UIComponent c,
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
