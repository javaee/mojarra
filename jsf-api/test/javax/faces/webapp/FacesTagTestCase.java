/*
 * $Id: FacesTagTestCase.java,v 1.1 2003/03/13 22:02:37 craigmcc Exp $
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


    protected boolean                 detail = false;
    protected FacesTag                root = null;
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


    // Test double single tag rendering
    public void testDoubleSingleTag() throws Exception {

        add(null, new TestTag("A", "a"));

        render();
        reset();
        render(); // Should not create components twice
        assertEquals("/bA/eA", text());
        assertEquals("//A-a", ids());

    }


    // Test double single tag rendering with missing ids
    public void testDoubleSingleTagNoIds() throws Exception {

        add(null, new TestTag(null, "a"));

        render();
        assertEquals("//-a", ids());
        reset();
        render(); // Should not create components twice
        assertEquals("/b/e", text());
        assertEquals("//-a", ids());

    }


    // Test double multiple tag rendering
    public void testDoubleMultipleTag() throws Exception {

        FacesTag a = new TestTag("A", "a");
        FacesTag b2 = new TestTag("B2", "b2");
        add(null, a);
        add(a, new TestTag("B1", "b1"));
        add(a, b2);
        add(a, new TestTag("B3", "b3"));
        add(b2, new TestTag("C1", "c1"));
        add(b2, new TestTag("C2", "c2"));

        render();
        assertEquals("//A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", ids());
        reset();
        render(); // Should not create components twice
        assertEquals("/bA/bB1/eB1/bB2/bC1/eC1/bC2/eC2/eB2/bB3/eB3/eA", text());
        assertEquals("//A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", ids());

    }


    // Test double multiple tag rendering with missing ids
    public void testDoubleMultipleTagNoIds() throws Exception {

        FacesTag a = new TestTag("A", "a");
        FacesTag b2 = new TestTag("B2", "b2");
        add(null, a);
        add(a, new TestTag("B1", "b1"));
        add(a, b2);
        add(a, new TestTag("B3", "b3"));
        add(b2, new TestTag(null, "c1"));
        add(b2, new TestTag(null, "c2"));

        render();
        assertEquals("//A-a/B1-b1/B2-b2/-c1/-c2/B3-b3", ids());
        reset();
        render(); // Should not create components twice
        assertEquals("/bA/bB1/eB1/bB2/b/e/b/e/eB2/bB3/eB3/eA", text());
        assertEquals("//A-a/B1-b1/B2-b2/-c1/-c2/B3-b3", ids());

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

        FacesTag a = new TestTag("A", "a");
        FacesTag b2 = new TestTag("B2", "b2");
        b2.setRendered(false);
        add(null, a);
        add(a, new TestTag("B1", "b1"));
        add(a, b2);
        add(a, new TestTag("B3", "b3"));
        add(b2, new TestTag("C1", "c1"));
        add(b2, new TestTag("C2", "c2"));

        render();
        assertEquals("/bA/bB1/eB1/bC1/eC1/bC2/eC2/bB3/eB3/eA", text());
        assertEquals("//A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", ids());

    }


    // Test a component that takes responsibility for rendering children
    public void testRendersChildren() throws Exception {

        FacesTag a = new TestTag("A", "a");
        FacesTag b2 = new TestTag("B2", "b2");
        ((TestTag) b2).setRendersChildren(true);
        add(null, a);
        add(a, new TestTag("B1", "b1"));
        add(a, b2);
        add(a, new TestTag("B3", "b3"));
        add(b2, new TestTag("C1", "c1"));
        add(b2, new TestTag("C2", "c2"));

        render();
        assertEquals("/bA/bB1/eB1/bB2/eB2/bB3/eB3/eA", text());
        assertEquals("//A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", ids());

    }


    // Test simple multiple tag rendering
    public void testSimpleMultipleTag() throws Exception {

        FacesTag a = new TestTag("A", "a");
        FacesTag b2 = new TestTag("B2", "b2");
        add(null, a);
        add(a, new TestTag("B1", "b1"));
        add(a, b2);
        add(a, new TestTag("B3", "b3"));
        add(b2, new TestTag("C1", "c1"));
        add(b2, new TestTag("C2", "c2"));

        render();
        assertEquals("/bA/bB1/eB1/bB2/bC1/eC1/bC2/eC2/eB2/bB3/eB3/eA", text());
        assertEquals("//A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", ids());

    }


    // Test simple multiple tag rendering with missing ids
    public void testSimpleMultipleTagNoIds() throws Exception {

        FacesTag a = new TestTag("A", "a");
        FacesTag b2 = new TestTag("B2", "b2");
        add(null, a);
        add(a, new TestTag("B1", "b1"));
        add(a, b2);
        add(a, new TestTag("B3", "b3"));
        add(b2, new TestTag(null, "c1"));
        add(b2, new TestTag(null, "c2"));

        render();
        assertEquals("/bA/bB1/eB1/bB2/b/e/b/e/eB2/bB3/eB3/eA", text());
        assertEquals("//A-a/B1-b1/B2-b2/-c1/-c2/B3-b3", ids());

    }


    // Test simple single tag rendering
    public void testSimpleSingleTag() throws Exception {

        add(null, new TestTag("A", "a"));

        render();
        assertEquals("/bA/eA", text());
        assertEquals("//A-a", ids());

    }


    // Test simple single tag rendering with missing id
    public void testSimpleSingleTagNoIds() throws Exception {

        add(null, new TestTag());

        render();
        assertEquals("/b/e", text());
        assertEquals("//", ids());

    }


    // ------------------------------------------------------ Protected Methods


    // Add a new child tag with the specified parent (null==root tag)
    protected void add(FacesTag parent, FacesTag child) {
        if (parent == null) {
            if (root != null) {
                throw new IllegalStateException("Root tag already set");
            }
            root = child;
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


    // Return a String of all the component ids in treewalk order
    protected String ids() {
        UIComponent component = facesContext.getTree().getRoot();
        StringBuffer sb = new StringBuffer();
        if (detail) {
            System.err.println("vvvvvvvvvv");
        }
        ids(component, sb);
        if (detail) {
            System.err.println("^^^^^^^^^^");
        }
        return (sb.toString());
    }

    protected void ids(UIComponent component, StringBuffer sb) {
        if (detail) {
            System.err.println("component=" + component.getComponentId());
        }
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
            ids((UIComponent) kids.next(), sb);
        }
    }


    // Release all tags on the current page
    protected void release() {
        if (root != null) {
            release(root);
            root = null;
        }
    }

    protected void release(FacesTag tag) {
        List children = (List) tags.get(tag);
        if (children != null) {
            Iterator kids = children.iterator();
            while (kids.hasNext()) {
                release((FacesTag) kids.next());
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

    protected void render(FacesTag tag) throws JspException {
        tag.doStartTag();
        List children = (List) tags.get(tag);
        if (children != null) {
            Iterator kids = children.iterator();
            while (kids.hasNext()) {
                render((FacesTag) kids.next());
            }
        }
        tag.doEndTag();
    }


    // Reset the output buffer in our fake writer
    protected void reset() throws IOException {
        MockJspWriter writer = (MockJspWriter) pageContext.getOut();
        writer.clearBuffer();
    }


    // Return the rendered text
    protected String text() {
        MockJspWriter writer = (MockJspWriter) pageContext.getOut();
        return (writer.getBuffer());
    }


}
