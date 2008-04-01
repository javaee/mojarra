/*
 * $Id: TestRenderers_1.java,v 1.1 2002/05/31 19:34:14 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_1.java

package com.sun.faces.renderkit.html_basic;

import org.apache.cactus.WebRequest;
import org.apache.cactus.ServletTestCase;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Vector;
import com.sun.faces.CompareFiles;

import javax.faces.component.UITextEntry;
import javax.faces.component.UIComponent;
import com.sun.faces.renderkit.html_basic.InputRenderer;
import com.sun.faces.FileOutputResponseWrapper;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_1.java,v 1.1 2002/05/31 19:34:14 jvisvanathan Exp $
 * 
 *
 */

public class TestRenderers_1 extends ServletTestCase
{
    //
    // Protected Constants
    //
   public static final String PATH_ROOT = "./build/test/servers/tomcat40/webapps/test/";

   public static final String EXPECTED_OUTPUT_FILENAME = PATH_ROOT +
        "CorrectRenderersResponse";

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private FacesContext  facesContext = null;
    private FacesContextFactory  facesContextFactory = null;
    private FileOutputResponseWrapper wrappedResponse = null;
    
    // Attribute Instance Variables
    // Relationship Instance Variables
    //
    // Constructors and Initializers    
    //

    public TestRenderers_1() {super("TestRenderers_1");}
    public TestRenderers_1(String name) {super(name);}

    //
    // Class methods
    //

    //
    // Methods from TestCase
    //
    public void setUp() {
        System.setProperty(FactoryFinder.FACES_CONTEXT_FACTORY,
		       "com.sun.faces.context.FacesContextFactoryImpl");
        facesContextFactory = (FacesContextFactory) 
	FactoryFinder.createFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        assertTrue(null != facesContextFactory);
    
        assertTrue(request != null);
        assertTrue(response != null);
        assertTrue(config.getServletContext() != null);
          
         // create the wrapper response object that writes output
         // to a file.
        wrappedResponse = new FileOutputResponseWrapper(response);
            
        facesContext = 
	facesContextFactory.createFacesContext(config.getServletContext(),
					       request, wrappedResponse);
        assertTrue(null != facesContext);
    }     

    public void tearDown() {
    }

    public void beginRenderers(WebRequest theRequest) {
        theRequest.addParameter("/root/input_userName", "JavaServerFaces");
    } 
    
    //
    // General Methods
    //
    public void testRenderers()
    {
         try {
            // create a dummy root for the tree.
            UIComponent root = new UIComponent() {
	        public String getComponentType() { return "root"; } 
	    };
            root.setComponentId("root");
            
            // Test inputRenderer.
            System.out.println("Testing InputRenderer");
            UIComponent textEntry = new UITextEntry();
            textEntry.setComponentId("input_userName");
            root.addChild(textEntry);
             
            InputRenderer inputRenderer = new InputRenderer();
            // test decode method
            System.out.println("Testing decode method");
            inputRenderer.decode(facesContext, textEntry);
            assertTrue(((String)textEntry.getValue()).equals("JavaServerFaces"));
            
            // test encode method
            System.out.println("Testing encode method");
            inputRenderer.encodeBegin(facesContext, textEntry);
            wrappedResponse.flushBuffer();
            
            // PENDING(visvan) test rest of the renderers
            
            boolean result = false;
            try {
                CompareFiles cf = new CompareFiles();
                result = 
                cf.filesIdentical(FileOutputResponseWrapper.FACES_RESPONSE_FILENAME, 
                    EXPECTED_OUTPUT_FILENAME, null);
            } catch (Exception e ) {
                System.out.println(e.getMessage());
                result = false;
            }
            assertTrue(result);
        }
        catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
            return;
        }
    }


} // end of class TestRenderers_1
