/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * $Id: TestRenderers_4.java,v 1.12 2004/01/27 21:06:13 eburns Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// TestRenderers_4.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;

import javax.faces.component.UIPanel;
import javax.faces.component.UICommand;
import javax.faces.component.UIOutput;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContextFactory;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;

import java.util.ArrayList;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_4.java,v 1.12 2004/01/27 21:06:13 eburns Exp $
 * 
 *
 */

public class TestRenderers_4 extends JspFacesTestCase {
    //
    // Protected Constants
    //

    public boolean sendWriterToFile() {
        return true;
    }

    public String getExpectedOutputFilename() {
        return "CorrectRenderersResponse_4";
    }

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private FacesContextFactory facesContextFactory = null;

    // Attribute Instance Variables
    // Relationship Instance Variables
    //
    // Constructors and Initializers    
    //

    public TestRenderers_4() {
        super("TestRenderers_4");
    }
    public TestRenderers_4(String name) {
        super(name);
    }

    //
    // Class methods
    //

    //
    // Methods from TestCase
    //
    public void setUp() {
        super.setUp();
        UIViewRoot page = new UIViewRoot();
        page.setViewId("viewId");       
        getFacesContext().setViewRoot(page);
        assertTrue(null != getFacesContext().getResponseWriter());
    }

    public void beginRenderers(WebRequest theRequest) {

    }

    public void testRenderers() {

        try {
            // create a dummy root for the tree.
            UIViewRoot root = new UIViewRoot();
            root.setId("root");

            testGridRenderer(root);
	    
	    root = new UIViewRoot();
            root.setId("root");
            testGridRendererWithNonRenderedChildren(root);

	    getFacesContext().getResponseWriter().close();
            assertTrue(verifyExpectedOutput());
        }
        catch (Throwable t) {
            t.printStackTrace();
            assertTrue(false);
            return;
        }
    }

    public void testGridRenderer(UIComponent root)
        throws IOException {
        System.out.println("Testing GridRenderer");
	GridRenderer gridRenderer = null;
	UIPanel 
	    panel = null,
	    headerGroup = null,
	    footerGroup = null;
	UIOutput 
	    header1 = null,
	    header2 = null,
	    footer1 = null,
	    footer2 = null,
	    body1 = null,
	    body2 = null;

	panel = new UIPanel();
	root.getChildren().add(panel);
	
	headerGroup = new UIPanel();
	headerGroup.setId("header");
	headerGroup.setRendererType("javax.faces.Group");
	header1 = new UIOutput();
	header1.setValue("header1 ");
	headerGroup.getChildren().add(header1);
	header2 = new UIOutput();
	header2.setValue("header2 ");
	headerGroup.getChildren().add(header2);
	panel.getFacets().put("header", headerGroup);
	
	footerGroup = new UIPanel();
	footerGroup.setId("footer");
	footerGroup.setRendererType("javax.faces.Group");
	footer1 = new UIOutput();
	footer1.setValue("footer1 ");
	footerGroup.getChildren().add(footer1);
	footer2 = new UIOutput();
	footer2.setValue("footer2 ");
	footerGroup.getChildren().add(footer2);
	panel.getFacets().put("footer", footerGroup);

	body1 = new UIOutput();
	body1.setValue("body1");
	panel.getChildren().add(body1);

	body2 = new UIOutput();
	body2.setValue("body2");
	panel.getChildren().add(body2);

	gridRenderer = new GridRenderer();

        System.out.println("    Testing encodeBegin method... ");
	gridRenderer.encodeBegin(getFacesContext(), panel);
	gridRenderer.encodeChildren(getFacesContext(), panel);
        gridRenderer.encodeEnd(getFacesContext(), panel);

    }

    public void testGridRendererWithNonRenderedChildren(UIComponent root)
        throws IOException {
        System.out.println("Testing GridRenderer");
	GridRenderer gridRenderer = null;
	UIPanel 
	    panel = null,
	    headerGroup = null,
	    footerGroup = null;
	UIOutput 
	    header1 = null,
	    header2 = null,
	    footer1 = null,
	    footer2 = null,
	    body1 = null,
	    body2 = null;

	panel = new UIPanel();
	root.getChildren().add(panel);
	
	// the header should not be rendered
	headerGroup = new UIPanel();
	headerGroup.setRendered(false);
	headerGroup.setId("header");
	headerGroup.setRendererType("javax.faces.Group");
	header1 = new UIOutput();
	header1.setValue("header1 ");
	headerGroup.getChildren().add(header1);
	header2 = new UIOutput();
	header2.setValue("header2 ");
	headerGroup.getChildren().add(header2);
	panel.getFacets().put("header", headerGroup);
	
	footerGroup = new UIPanel();
	footerGroup.setId("footer");
	footerGroup.setRendererType("javax.faces.Group");
	footer1 = new UIOutput();
	footer1.setValue("footer1 ");
	footerGroup.getChildren().add(footer1);
	footer2 = new UIOutput();
	footer2.setValue("footer2 ");
	footerGroup.getChildren().add(footer2);
	panel.getFacets().put("footer", footerGroup);

	// this child should not be rendered
	body1 = new UIOutput();
	body1.setRendered(false);
	body1.setValue("body1");
	panel.getChildren().add(body1);

	body2 = new UIOutput();
	body2.setValue("body2");
	panel.getChildren().add(body2);

	gridRenderer = new GridRenderer();

        System.out.println("    Testing encodeBegin method... ");
	gridRenderer.encodeBegin(getFacesContext(), panel);
	gridRenderer.encodeChildren(getFacesContext(), panel);
        gridRenderer.encodeEnd(getFacesContext(), panel);

    }




} // end of class TestRenderers_4
