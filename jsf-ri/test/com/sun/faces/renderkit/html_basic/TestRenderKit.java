/*
 * $Id: TestRenderKit.java,v 1.16 2002/09/07 16:36:06 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderKit.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.renderkit.html_basic.FormRenderer;

import java.util.Iterator;

import javax.faces.component.UIOutput;
import javax.faces.component.UIComponentBase;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;
import org.apache.cactus.ServletTestCase;

import com.sun.faces.CompareFiles;
import com.sun.faces.FileOutputResponseWrapper;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 *  <B>TestRenderKit</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKit.java,v 1.16 2002/09/07 16:36:06 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderKit extends ServletTestCase {
//
// Protected Constants
//

public static final String OUTPUT_FILENAME = 
    FileOutputResponseWrapper.FACES_RESPONSE_ROOT + "TestRenderKit_out";

public static final String CORRECT_OUTPUT_FILENAME = 
    FileOutputResponseWrapper.FACES_RESPONSE_ROOT + "TestRenderKit_correct";



//
// Class Variables
//

//
// Instance Variables
//
    private HtmlBasicRenderKit renderKit = null;

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
        renderKit = new HtmlBasicRenderKit();

        // 1. Verify "getRenderer()" returns a Renderer instance
        //  
        Renderer renderer = renderKit.getRenderer("FormRenderer");
        assertTrue(renderer instanceof FormRenderer);
    }

    public void testGetRendererTypesVariants() {
        renderKit = new HtmlBasicRenderKit();
     
        // 1. Verify "getRendererTypes(UIComponent)"
        //
        Renderer renderer = null;
        UIOutput out = new UIOutput();
        Iterator iter = renderKit.getRendererTypes(out);
	boolean correctInstance = false;
	String rendererType = null;
        while (iter.hasNext()) {
	    rendererType = (String) iter.next();
            renderer = renderKit.getRenderer(rendererType);
            if (renderer instanceof TextRenderer ||
                renderer instanceof MessageRenderer) {
                correctInstance = true;
            }
        }
	assertTrue(correctInstance);
         
        // 2. Verify "getRendererTypes(componentType)"
        //
        renderer = null;
        String cType = "javax.faces.component.UICommand";
        iter = renderKit.getRendererTypes(cType);
        while (iter.hasNext()) {
	    rendererType = (String) iter.next();
            renderer = renderKit.getRenderer(rendererType);

            correctInstance = false;
            if (renderer instanceof ButtonRenderer ||
                renderer instanceof HyperlinkRenderer) {
                correctInstance = true;
            }
        }
	assertTrue(correctInstance);
    }
            
    public void testGetRendererTypes() {
        renderKit = new HtmlBasicRenderKit();
        
        System.out.println("Renderer Types:");

        Iterator iter = renderKit.getRendererTypes();
        while (iter.hasNext()) {
            System.out.println((String)iter.next());
        }
    }

    public void testGetComponentClasses() {
	final PrintStream out;
	File file = null;
	FileOutputStream fs = null;
	Iterator componentClasses = null;
	
	try {
	    file = new File ( OUTPUT_FILENAME );
	    fs = new FileOutputStream(file);
	} catch ( Exception e ) {
	    assertTrue(false);
	}
	out = new PrintStream(fs);

	renderKit = new HtmlBasicRenderKit();
	
	componentClasses = renderKit.getComponentClasses();
	assertTrue(null != componentClasses);
	assertTrue(componentClasses.hasNext());

	while (componentClasses.hasNext()) {
	    out.println(((Class)componentClasses.next()).getName());
	}
	out.close();

	try {
	    CompareFiles cf = new CompareFiles();
	    assertTrue(cf.filesIdentical(OUTPUT_FILENAME, CORRECT_OUTPUT_FILENAME, 
					 null));
	} catch (Throwable e ) {
	    System.out.println("Throwable: " + e.getMessage());
	    assertTrue(false);
	}
	
    }

    public void testAddComponentClass() {
	renderKit = new HtmlBasicRenderKit();
	boolean bool = false;
	UIComponentBase newComp = new UIComponentBase() {
		public String getComponentType() { return "BLAH"; }
	    };
	String newCompName = 
	    "com.sun.faces.renderkit.html_basic.TestRenderKit$1";
	Iterator componentClasses = null;
	
	try {
	    renderKit.addComponentClass(null);
	}
	catch (NullPointerException e) {
	    bool = true;
	}
	assertTrue(bool);

	bool = false;
	try {
	    renderKit.addComponentClass(java.lang.String.class);
	}
	catch (IllegalArgumentException e) {
	    bool = true;
	}
	assertTrue(bool);

	bool = false;
	renderKit.addComponentClass(newComp.getClass());
	componentClasses = renderKit.getComponentClasses();
	while (componentClasses.hasNext()) {
	    if (newCompName.equals(((Class)componentClasses.next()).getName())){
		bool = true;
	    }
	}
	assertTrue(bool);

    }

    public void testAddRenderer() {
	renderKit = new HtmlBasicRenderKit();
	boolean bool = false;
	FormRenderer formRenderer = new FormRenderer();

	try {
	    renderKit.addRenderer("FormRenderer", formRenderer);
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

} // end of class TestRenderKit
