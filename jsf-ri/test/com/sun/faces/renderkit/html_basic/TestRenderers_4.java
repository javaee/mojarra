/**
 * $Id: TestRenderers_4.java,v 1.3 2003/02/11 01:38:02 horwat Exp $
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
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContextFactory;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.tree.SimpleTreeImpl;

import java.util.ArrayList;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_4.java,v 1.3 2003/02/11 01:38:02 horwat Exp $
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

        SimpleTreeImpl xmlTree =
            new SimpleTreeImpl(
                getFacesContext(),
                new UICommand(),
                "treeId");
        getFacesContext().setTree(xmlTree);
        assertTrue(null != getFacesContext().getResponseWriter());
    }

    public void beginRenderers(WebRequest theRequest) {

    }

    public void testRenderers() {

        try {
            // create a dummy root for the tree.
            UINamingContainer root = new UINamingContainer() {
                public String getComponentType() {
                    return "root";
                }
            };
            root.setComponentId("root");

            testListRenderer(root);
            testGridRenderer(root);
	    getFacesContext().getResponseWriter().close();
            assertTrue(verifyExpectedOutput());
        }
        catch (Throwable t) {
            t.printStackTrace();
            assertTrue(false);
            return;
        }
    }

    public void testListRenderer(UIComponent root)
        throws IOException {
        System.out.println("Testing ListRenderer");
	ListRenderer listRenderer = null;
	UIPanel 
	    panel = null,
	    headerGroup = null,
	    bodyGroup = null,
	    footerGroup = null;
	UIOutput 
	    header1 = null,
	    header2 = null,
	    footer1 = null,
	    footer2 = null,
	    body1 = null,
	    body2 = null;
	ArrayList bodyList = new ArrayList();
	bodyList.add("row1");
	bodyList.add("row2");
	bodyList.add("row3");

	panel = new UIPanel();
	root.addChild(panel);
	
	headerGroup = new UIPanel();
	headerGroup.setComponentId("header");
	header1 = new UIOutput();
	header1.setValue("header1");
	headerGroup.addChild(header1);
	header2 = new UIOutput();
	header2.setValue("header2");
	headerGroup.addChild(header2);
	panel.addFacet("header", headerGroup);
	
	footerGroup = new UIPanel();
	footerGroup.setComponentId("footer");
	footer1 = new UIOutput();
	footer1.setValue("footer1");
	footerGroup.addChild(footer1);
	footer2 = new UIOutput();
	footer2.setValue("footer2");
	footerGroup.addChild(footer2);
	panel.addFacet("footer", footerGroup);

	bodyGroup = new UIPanel();
	bodyGroup.setValue(bodyList);
	panel.addChild(bodyGroup);

	body1 = new UIOutput();
	body1.setValue("body1");
	bodyGroup.addChild(body1);

	body2 = new UIOutput();
	body2.setValue("body2");
	bodyGroup.addChild(body2);

	listRenderer = new ListRenderer();

        System.out.println("    Testing encodeBegin method... ");
	listRenderer.encodeBegin(getFacesContext(), panel);
	listRenderer.encodeChildren(getFacesContext(), panel);
        listRenderer.encodeEnd(getFacesContext(), panel);

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
	root.addChild(panel);
	
	headerGroup = new UIPanel();
	headerGroup.setComponentId("header");
	headerGroup.setAttribute("rendererType", "Group");
	header1 = new UIOutput();
	header1.setValue("header1 ");
	headerGroup.addChild(header1);
	header2 = new UIOutput();
	header2.setValue("header2 ");
	headerGroup.addChild(header2);
	panel.addFacet("header", headerGroup);
	
	footerGroup = new UIPanel();
	footerGroup.setComponentId("footer");
	footerGroup.setAttribute("rendererType", "Group");
	footer1 = new UIOutput();
	footer1.setValue("footer1 ");
	footerGroup.addChild(footer1);
	footer2 = new UIOutput();
	footer2.setValue("footer2 ");
	footerGroup.addChild(footer2);
	panel.addFacet("footer", footerGroup);

	body1 = new UIOutput();
	body1.setValue("body1");
	panel.addChild(body1);

	body2 = new UIOutput();
	body2.setValue("body2");
	panel.addChild(body2);

	gridRenderer = new GridRenderer();

        System.out.println("    Testing encodeBegin method... ");
	gridRenderer.encodeBegin(getFacesContext(), panel);
	gridRenderer.encodeChildren(getFacesContext(), panel);
        gridRenderer.encodeEnd(getFacesContext(), panel);

    }




} // end of class TestRenderers_4
