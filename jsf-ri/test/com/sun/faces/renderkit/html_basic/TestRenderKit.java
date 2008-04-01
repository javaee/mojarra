/*
 * $Id: TestRenderKit.java,v 1.13 2002/05/29 20:45:54 rkitain Exp $
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
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;
import org.apache.cactus.ServletTestCase;

/**
 *
 *  <B>TestRenderKit</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKit.java,v 1.13 2002/05/29 20:45:54 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderKit extends ServletTestCase {
//
// Protected Constants
//

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

    public void testGetRenderers() {
        renderKit = new HtmlBasicRenderKit();
     
        // 1. Verify "getRenderers(UIComponent)"
        //
        Renderer renderer = null;
        UIOutput out = new UIOutput();
        Iterator iter = renderKit.getRenderers(out);
        while (iter.hasNext()) {
            renderer = (Renderer)iter.next();
            boolean correctInstance = false;
            if (renderer instanceof TextRenderer ||
                renderer instanceof TextAndGraphicRenderer ||
                renderer instanceof MessageRenderer) {
                correctInstance = true;
            }
            assertTrue(correctInstance);
        }
         
        // 2. Verify "getRenderers(componentType)"
        //
        renderer = null;
        String cType = "Command";
        iter = renderKit.getRenderers(cType);
        while (iter.hasNext()) {
            renderer = (Renderer)iter.next();
            boolean correctInstance = false;
            if (renderer instanceof ButtonRenderer ||
                renderer instanceof HyperlinkRenderer) {
                correctInstance = true;
            }
            assertTrue(correctInstance);
        }
    }
            
    public void testGetRendererTypes() {
        renderKit = new HtmlBasicRenderKit();
        
        System.out.println("Renderer Types:");

        Iterator iter = renderKit.getRendererTypes();
        while (iter.hasNext()) {
            System.out.println((String)iter.next());
        }
    }

} // end of class TestRenderKit
