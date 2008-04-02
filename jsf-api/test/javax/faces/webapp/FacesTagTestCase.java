/*
 * $Id: FacesTagTestCase.java,v 1.4 2003/04/29 18:13:09 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockHttpServletResponse;
import javax.faces.mock.MockHttpSession;
import javax.faces.mock.MockJspWriter;
import javax.faces.mock.MockPageContext;
import javax.faces.mock.MockServlet;
import javax.faces.mock.MockServletConfig;
import javax.faces.mock.MockServletContext;


/**
 * <p>Base unit tests for all FacesTag classes.</p>
 */

public class FacesTagTestCase extends TestCase {


    // ----------------------------------------------------- Instance Variables


    protected MockServletConfig       config = null;
    protected MockFacesContext        facesContext = null;
    protected MockPageContext         pageContext = null;
    protected MockHttpServletRequest  request = null;
    protected MockHttpServletResponse response = null;
    protected MockServlet             servlet = null;
    protected MockServletContext      servletContext = null;
    protected MockHttpSession         session = null;

    protected Tag                     root = null;
    protected Map                     tags = new HashMap();


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public FacesTagTestCase(String name) {

        super(name);

    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {

        // Set up Servlet API Objects
        servletContext = new MockServletContext();
        config = new MockServletConfig(servletContext);
        servlet = new MockServlet(config);
        session = new MockHttpSession();
        request = new MockHttpServletRequest(session);
        response = new MockHttpServletResponse();

        // Set up JSP API Objects
        pageContext = new MockPageContext();
        pageContext.initialize(servlet, request, response, null,
                               true, 1024, true);

        // Set up Faces API Objects
        facesContext = new MockFacesContext();
        facesContext.setServletContext(servletContext);
        facesContext.setServletRequest(request);
        facesContext.setServletResponse(response);
        facesContext.setTree(new MockTree(new TestNamingContainer()));

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(FacesTagTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        config = null;
        facesContext = null;
        pageContext = null;
        request = null;
        response = null;
        servlet = null;
        servletContext = null;
        session = null;

        root = null;
        tags.clear();

    }


    // ------------------------------------------------ Individual Test Methods


    // Test multiple tag rendering with ids
    public void testMultipleTagWithId() throws Exception {

        configure("C1", "C2", true, false);

        render();
        assertEquals("/bROOT/bA/bB1/eB1/bB2/bC1/eC1/bC2/eC2/eB2/bB3/eB3/eA/eROOT", text());
	System.out.println("1 tree: " + tree()); System.out.flush();
        assertEquals("//ROOT/A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();
        reset();
	com.sun.faces.util.DebugUtil.waitForDebugger();
        render();
	System.out.println("1 text: " + text());
        assertEquals("/bA/bB1/eB1/bB2/bC1/eC1/bC2/eC2/eB2/bB3/eB3/eA", text());
	System.out.println("1 tree: " + tree()); System.out.flush();
        assertEquals("//A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

    }


    // Test multiple tag rendering with missing id
    // Scenario 1 -- both ids missing
    public void testMultipleTagWithoutId1() throws Exception {

        configure(null, null, true, false);

        render();
        assertEquals("/bROOT/bA/bB1/eB1/bB2/b/e/b/e/eB2/bB3/eB3/eA/eROOT", 
		     text());
	System.out.println("2 tree: " + tree()); System.out.flush();
        assertEquals("//ROOT/A-a/B1-b1/B2-b2/-c1/-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
	System.out.println("2 text: " + text());
        assertEquals("/bA/bB1/eB1/bB2/b/e/b/e/eB2/bB3/eB3/eA", text());
	System.out.println("2 tree: " + tree()); System.out.flush();
        assertEquals("//A-a/B1-b1/B2-b2/-c1/-c2/B3-b3", tree());
        verifyB2();

    }


    // Test multiple tag rendering with missing id
    // Scenario 2 -- first id missing
    public void testMultipleTagWithoutId2() throws Exception {

        configure(null, "C2", true, false);

        render();
        assertEquals("/bROOT/bA/bB1/eB1/bB2/b/e/bC2/eC2/eB2/bB3/eB3/eA/eROOT", 
		     text());
	System.out.println("3 tree: " + tree()); System.out.flush();
        assertEquals("//ROOT/A-a/B1-b1/B2-b2/-c1/C2-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
	System.out.println("3 text: " + text());
        assertEquals("/bA/bB1/eB1/bB2/b/e/bC2/eC2/eB2/bB3/eB3/eA", text());
	System.out.println("3 tree: " + tree()); System.out.flush();
        assertEquals("//A-a/B1-b1/B2-b2/-c1/C2-c2/B3-b3", tree());
        verifyB2();

    }


    // Test multiple tag rendering with missing id
    // Scenario 3 -- second id missing
    public void testMultipleTagWithoutId3() throws Exception {

        configure("C1", null, true, false);

        render();
        assertEquals("/bROOT/bA/bB1/eB1/bB2/bC1/eC1/b/e/eB2/bB3/eB3/eA/eROOT",
		     text());
	System.out.println("4 tree: " + tree()); System.out.flush();
        assertEquals("//ROOT/A-a/B1-b1/B2-b2/C1-c1/-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
	System.out.println("4 text: " + text());
        assertEquals("/bA/bB1/eB1/bB2/bC1/eC1/b/e/eB2/bB3/eB3/eA", text());
	System.out.println("4 tree: " + tree()); System.out.flush();
        assertEquals("//A-a/B1-b1/B2-b2/C1-c1/-c2/B3-b3", tree());
        verifyB2();

    }


    // Test a pristine setup environment
    public void testPristine() {

        assertNotNull(config);
        assertNotNull(facesContext);
        assertNotNull(pageContext);
        assertNotNull(request);
        assertNotNull(response);
        assertNotNull(servlet);
        assertNotNull(servletContext);
        assertNotNull(session);

        assertNull(root);
        assertEquals(0, tags.size());

    }


    // Test suppressing rendering with rendered=false
    public void testRenderedFalse() throws Exception {

        configure("C1", "C2", false, false);

        render();
        assertEquals("/bROOT/bA/bB1/eB1/bC1/eC1/bC2/eC2/bB3/eB3/eA/eROOT", 
		     text());
	System.out.println("5 tree: " + tree()); System.out.flush();
        assertEquals("//ROOT/A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
	System.out.println("5 text: " + text());
        assertEquals("/bA/bB1/eB1/bC1/eC1/bC2/eC2/bB3/eB3/eA", text());
	System.out.println("5 tree: " + tree()); System.out.flush();
        assertEquals("//A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

    }


    // Test a component that takes responsibility for rendering children
    public void testRendersChildren() throws Exception {

        configure("C1", "C2", true, true);

        render();
        assertEquals("/bROOT/bA/bB1/eB1/bB2/eB2/bB3/eB3/eA/eROOT", text());
	System.out.println("6 tree: " + tree()); System.out.flush();
        assertEquals("//ROOT/A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
	System.out.println("6 text: " + text());
        assertEquals("/bA/bB1/eB1/bB2/eB2/bB3/eB3/eA", text());
	System.out.println("6 tree: " + tree()); System.out.flush();
        assertEquals("//A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

    }


    // Test single tag rendering with id
    public void testSingleTagWithId() throws Exception {

        add(null, new TestTag("A", "a"));

        render();
        assertEquals("/bROOT/bA/eA/eROOT", text());
	System.out.println("7 tree: " + tree()); System.out.flush();
        assertEquals("//ROOT/A-a", tree());

        reset();
        render();
	System.out.println("7 text: " + text());
        assertEquals("/bA/eA", text());
	System.out.println("7 tree: " + tree()); System.out.flush();
        assertEquals("//A-a", tree());

    }


    // Test single tag rendering without id
    public void testSingleTagWithoutId() throws Exception {

        add(null, new TestTag(null, "a"));

        render();
        assertEquals("/bROOT/b/e/eROOT", text());
	System.out.println("8 tree: " + tree()); System.out.flush();
        assertEquals("//ROOT/-a", tree());

        reset();
        render();
	System.out.println("8 text: " + text());
        assertEquals("/b/e", text());
	System.out.println("8 tree: " + tree()); System.out.flush();
        assertEquals("//-a", tree());

    }


    // ------------------------------------------------------ Protected Methods


    // Add a new child tag with the specified parent (null==root tag)
    protected void add(Tag parent, Tag child) {

        if (parent == null) {
            if (root != null) {
                throw new IllegalStateException("Root tag already set");
            }
	    root = new TestTag("ROOT", "root") {
		    protected void overrideProperties(UIComponent component) {
		    }
		};
	    add(root, child);
	    root.setPageContext(this.pageContext);
        } else {
            List children = (List) tags.get(parent);
            if (children == null) {
                children = new ArrayList();
                tags.put(parent, children);
            }
            children.add(child);
            child.setParent(parent);
        }
        child.setPageContext(this.pageContext);

    }


    // Configure the tree of tags, with specified ids for C1 and C2
    protected void configure(String id1, String id2,
                             boolean rendered, boolean children) {

        FacesTag a = new TestTag("A", "a");
        FacesTag b2 = new TestTag("B2", "b2");
        b2.setRendered(rendered);
        ((TestTag) b2).setRendersChildren(children);
        FacetTag f1 = new FacetTag();
        f1.setName("header");
        FacetTag f2 = new FacetTag();
        f2.setName("footer");

        add(null, a);
        add(a, new TestTag("B1", "b1"));
        add(a, b2);
        add(a, new TestTag("B3", "b3"));
        add(b2, f1);
        add(f1, new TestTag("H", "h"));
        add(b2, new TestTag(id1, "c1"));
        add(b2, new TestTag(id2, "c2"));
        add(b2, f2);
        add(f2, new TestTag("F", "f"));

    }


    // Release all tags on the current page
    protected void release() {

        if (root != null) {
            release(root);
            root = null;
        }

    }

    protected void release(Tag tag) {

        List children = (List) tags.get(tag);
        if (children != null) {
            Iterator kids = children.iterator();
            while (kids.hasNext()) {
                release((Tag) kids.next());
            }
            tags.remove(tag);
        }
        tag.setParent(null);
        tag.setPageContext(null);

    }


    // Render the current page by recursively processing all of the tags
    protected void render() throws JspException {

        if (root != null) {
            render(root);
        }

    }

    protected void render(Tag tag) throws JspException {

        tag.doStartTag();
        List children = (List) tags.get(tag);
        if (children != null) {
            Iterator kids = children.iterator();
            while (kids.hasNext()) {
                render((Tag) kids.next());
            }
        }
        tag.doEndTag();

    }


    // Reset the output buffer in our fake writer
    protected void reset() throws IOException {

        MockJspWriter writer = (MockJspWriter) pageContext.getOut();
        writer.clearBuffer();

    }


    // Return a String of all the component ids in treewalk order
    protected String tree() {

        UIComponent component = facesContext.getTree().getRoot();
        StringBuffer sb = new StringBuffer();
        tree(component, sb);
        return (sb.toString());

    }

    protected void tree(UIComponent component, StringBuffer sb) {

        sb.append("/");
        if (component.getComponentId() != null) {
            sb.append(component.getComponentId());
        }
        if (component instanceof TestComponent) {
            String label = ((TestComponent) component).getLabel();
            if (label != null) {
                sb.append("-");
                sb.append(label);
            }
        }
        Iterator kids = component.getChildren();
        while (kids.hasNext()) {
            tree((UIComponent) kids.next(), sb);
        }

    }


    // Return the rendered text
    protected String text() {

        MockJspWriter writer = (MockJspWriter) pageContext.getOut();
        return (writer.getBuffer());

    }


    // Verify the characteristics of a component with id "B2"
    protected void verifyB2() {

        UIComponent b2c = facesContext.getTree().getRoot().findComponent("B2");
        assertNotNull("B2 component exists", b2c);
        assertEquals("B2 component id", "B2", b2c.getComponentId());
        assertEquals("B2 child count", 2, b2c.getChildCount());
        assertNotNull("B2 header facet", b2c.getFacet("header"));
        assertNotNull("B2 footer facet", b2c.getFacet("footer"));

    }


}
