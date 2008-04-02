/*
 * $Id: TestRendererAttributes.java,v 1.8 2003/04/29 20:52:35 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRendererAttributes.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.FacesException;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;

import java.util.Iterator;

import com.sun.faces.CompareFiles;
import com.sun.faces.FileOutputResponseWrapper;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.ServletFacesTestCase;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.cactus.ServletTestCase;
import junit.framework.TestCase;

/**
 *
 *  <B>TestRendererAttributes.java</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRendererAttributes.java,v 1.8 2003/04/29 20:52:35 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

    public class TestRendererAttributes extends ServletFacesTestCase // ServletTestCase
{
//
// Protected Constants
//

public static final String OUTPUT_FILENAME = 
    FileOutputResponseWrapper.FACES_RESPONSE_ROOT + "TestRendererAttributes_out";

public static final String CORRECT_OUTPUT_FILENAME = 
    FileOutputResponseWrapper.FACES_RESPONSE_ROOT + "TestRendererAttributes_correct";

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

    public TestRendererAttributes() {super("TestRendererAttributes.java");}
    public TestRendererAttributes(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

    public void testGetAttributeNames() {
/* FIXME
	int result = -1;
	final PrintStream out;
	File file = null;
	FileOutputStream fs = null;
	
	try {
	    file = new File ( OUTPUT_FILENAME );
	    fs = new FileOutputStream(file);
	} catch ( Exception e ) {
	    assertTrue(false);
	}
	out = new PrintStream(fs);
	RenderKit renderKit = new HtmlBasicRenderKit();

        Iterator componentClasses = renderKit.getComponentClasses();
        while (componentClasses.hasNext()) {
            Class c = (Class)componentClasses.next();
            UIComponent component = null;
            try {
                component = (UIComponent)c.newInstance();
            } catch (InstantiationException ie) {
                throw new RuntimeException("Class Instantiation Exception:"+
                    ie.getMessage());
            } catch (IllegalAccessException ia) {
                throw new RuntimeException("Illegal Access Exception:"+
                    ia.getMessage());
            } 
            String componentType = component.getComponentType();
            Iterator rendererTypes = renderKit.getRendererTypes(
                componentType);
            while (rendererTypes.hasNext()) {
                String name, rendererType = (String)rendererTypes.next();
                Renderer renderer = renderKit.getRenderer(rendererType);
	        AttributeDescriptor attrDesc;

	        Iterator attrNames = renderer.getAttributeNames((component));
	        out.println(rendererType);
	        while (attrNames.hasNext()) {
		    name = (String) attrNames.next();
		    out.println("\t" + name + "\n");
		    attrDesc = renderer.getAttributeDescriptor(
                        component, name);
		    out.println("\t\t" + attrDesc.getDescription() + "\n");
		    out.println("\t\t" + attrDesc.getDisplayName() + "\n");
		    out.println("\t\t" + attrDesc.getName() + "\n");
		    out.println("\t\t" + attrDesc.getType() + "\n");
                }
	    }
	}
	out.close();

	try {
	    CompareFiles cf = new CompareFiles();
	    assertTrue(cf.filesIdentical(OUTPUT_FILENAME, CORRECT_OUTPUT_FILENAME, null));
	} catch (Throwable e ) {
	    System.out.println("File Comparison failed: diff -u " + 
			       OUTPUT_FILENAME + " " + 
			       CORRECT_OUTPUT_FILENAME);
	    System.out.println("Throwable: " + e.getMessage());
	    assertTrue(false);
	}

        // Test IllegalArgument Exceptions

        UICommand u = new UICommand();
        TextRenderer tr = new TextRenderer();
        boolean bool = false;
        try {
            tr.getAttributeNames(u);
        }
        catch (IllegalArgumentException e) {
            bool = true;
        }
        assertTrue(bool);      

        bool = false;
        try {
            tr.getAttributeNames(u.getComponentType());
        }
        catch (IllegalArgumentException e) {
            bool = true;
        }
        
        // passing invalid attribute name should throw exception
        bool = false;
        try {
            tr.getAttributeDescriptor(u.getComponentType(), "doesnotexist");
        }
        catch (IllegalArgumentException e) {
            bool = true;
        }
        assertTrue(bool);
        
        // passing invalid component type should throw exception
        bool = false;
        try {
            tr.getAttributeDescriptor("Invalidtype", "doesnotexist");
        }
        catch (IllegalArgumentException e) {
            bool = true;
        }
        assertTrue(bool);
*/
    }

} // end of class TestRendererAttributes
