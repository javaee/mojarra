/*
 * $Id: UIComponentTagTestCase.java,v 1.28 2005/10/19 19:51:23 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.webapp;


import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.FacesEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.sun.faces.mock.MockApplication;
import com.sun.faces.mock.MockExternalContext;
import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockHttpSession;
import com.sun.faces.mock.MockJspWriter;
import com.sun.faces.mock.MockLifecycle;
import com.sun.faces.mock.MockPageContext;
import com.sun.faces.mock.MockRenderKit;
import com.sun.faces.mock.MockRenderKitFactory;
import com.sun.faces.mock.MockServlet;
import com.sun.faces.mock.MockServletConfig;
import com.sun.faces.mock.MockServletContext;


/**
 * <p>Base unit tests for all UIComponentTag classes.</p>
 */

public class UIComponentTagTestCase extends TagTestCaseBase {


    // ----------------------------------------------------- Instance Variables

    protected Map                     tags = new HashMap();


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIComponentTagTestCase(String name) {

        super(name);

    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIComponentTagTestCase.class));

    }

    /**
     * <p>Since the JSP tag no longer creates the response writer, we
     * must do it ourselves.</p>
     */ 

    public void setUp() throws Exception {
	super.setUp();

        ResponseWriter writer = facesContext.getResponseWriter();
        if (writer == null) {
	    RenderKitFactory renderFactory = (RenderKitFactory)
		FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	    RenderKit renderKit = 
		renderFactory.getRenderKit
                (facesContext, facesContext.getViewRoot().getRenderKitId());
            writer = 
		renderKit.createResponseWriter(new Writer() {
		    public void close() throws IOException {
			pageContext.getOut().close();
		    }
		    public void flush() throws IOException {
                        // PENDING(craigmcc) - causes problems with includes
			// pageContext.getOut().flush();
		    }
                    public void write(char cbuf) throws IOException {
			pageContext.getOut().write(cbuf);
		    }
		    public void write(char[] cbuf, int off, 
				      int len) throws IOException {
			pageContext.getOut().write(cbuf, off, len);
		    }
		    public void write(int c) throws IOException {
			pageContext.getOut().write(c);
		    }
		    public void write(String str) throws IOException {
			pageContext.getOut().write(str);
		    }
		    public void write(String str, int off, 
				      int len) throws IOException {
			pageContext.getOut().write(str, off, len);
		    }
		},
                                               null,
            pageContext.getRequest().getCharacterEncoding());
	    
            facesContext.setResponseWriter(writer);
	}
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() throws Exception {
        tags.clear();
	super.tearDown();

    }


    // ------------------------------------------------ Individual Test Methods


    // Test multiple tag rendering with ids
    public void testMultipleTagWithId() throws Exception {

        configure("C1", "C2", true, false);

        render();
        assertEquals("/bA/bB1/eB1/bB2/bC1/eC1/bC2/eC2/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();
        reset();
        render();
        assertEquals("/bA/bB1/eB1/bB2/bC1/eC1/bC2/eC2/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

    }


    // Test multiple tag rendering with missing id
    // Scenario 1 -- both ids missing
    public void testMultipleTagWithoutId1() throws Exception {

        configure(null, null, true, false);

        render();
        assertEquals("/bA/bB1/eB1/bB2/b" + UIViewRoot.UNIQUE_ID_PREFIX + "0/e" + UIViewRoot.UNIQUE_ID_PREFIX + "0/b" + UIViewRoot.UNIQUE_ID_PREFIX + "1/e" + UIViewRoot.UNIQUE_ID_PREFIX + "1/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/" + UIViewRoot.UNIQUE_ID_PREFIX + "0-c1/" + UIViewRoot.UNIQUE_ID_PREFIX + "1-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
        assertEquals("/bA/bB1/eB1/bB2/b" + UIViewRoot.UNIQUE_ID_PREFIX + "0/e" + UIViewRoot.UNIQUE_ID_PREFIX + "0/b" + UIViewRoot.UNIQUE_ID_PREFIX + "1/e" + UIViewRoot.UNIQUE_ID_PREFIX + "1/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/" + UIViewRoot.UNIQUE_ID_PREFIX + "0-c1/" + UIViewRoot.UNIQUE_ID_PREFIX + "1-c2/B3-b3", tree());
        verifyB2();

    }


    // Test multiple tag rendering with missing id
    // Scenario 2 -- first id missing
    public void testMultipleTagWithoutId2() throws Exception {

        configure(null, "C2", true, false);

        render();
        assertEquals("/bA/bB1/eB1/bB2/b" + UIViewRoot.UNIQUE_ID_PREFIX + "0/e" + UIViewRoot.UNIQUE_ID_PREFIX + "0/bC2/eC2/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/" + UIViewRoot.UNIQUE_ID_PREFIX + "0-c1/C2-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
        assertEquals("/bA/bB1/eB1/bB2/b" + UIViewRoot.UNIQUE_ID_PREFIX + "0/e" + UIViewRoot.UNIQUE_ID_PREFIX + "0/bC2/eC2/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/" + UIViewRoot.UNIQUE_ID_PREFIX + "0-c1/C2-c2/B3-b3", tree());
        verifyB2();

    }


    // Test multiple tag rendering with missing id
    // Scenario 3 -- second id missing
    public void testMultipleTagWithoutId3() throws Exception {

        configure("C1", null, true, false);

        render();
        assertEquals("/bA/bB1/eB1/bB2/bC1/eC1/b" + UIViewRoot.UNIQUE_ID_PREFIX + "0/e" + UIViewRoot.UNIQUE_ID_PREFIX + "0/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/C1-c1/" + UIViewRoot.UNIQUE_ID_PREFIX + "0-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
        assertEquals("/bA/bB1/eB1/bB2/bC1/eC1/b" + UIViewRoot.UNIQUE_ID_PREFIX + "0/e" + UIViewRoot.UNIQUE_ID_PREFIX + "0/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/C1-c1/" + UIViewRoot.UNIQUE_ID_PREFIX + "0-c2/B3-b3", tree());
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
        assertEquals("/bA/bB1/eB1/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
        assertEquals("/bA/bB1/eB1/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

    }


    // Test a component that takes responsibility for rendering children
    public void testRendersChildren() throws Exception {

        configure("C1", "C2", true, true);

        render();
        assertEquals("/bA/bB1/eB1/bB2/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

        reset();
        render();
        assertEquals("/bA/bB1/eB1/bB2/eB2/bB3/eB3/eA", text());
        assertEquals("/ROOT/A-a/B1-b1/B2-b2/C1-c1/C2-c2/B3-b3", tree());
        verifyB2();

    }


    // Test single tag rendering with id
    public void testSingleTagWithId() throws Exception {

        add(null, new TestTag("A", "a"));

        render();
        assertEquals("/bA/eA", text());
        assertEquals("/ROOT/A-a", tree());

        reset();
        render();
        assertEquals("/bA/eA", text());
        assertEquals("/ROOT/A-a", tree());

    }


    // Test single tag rendering without id
    public void testSingleTagWithoutId() throws Exception {

        add(null, new TestTag(null, "a"));

        render();
        assertEquals("/b" + UIViewRoot.UNIQUE_ID_PREFIX + "0/e" + UIViewRoot.UNIQUE_ID_PREFIX + "0", text());
        assertEquals("/ROOT/" + UIViewRoot.UNIQUE_ID_PREFIX + "0-a", tree());

        reset();
        render();
        assertEquals("/b" + UIViewRoot.UNIQUE_ID_PREFIX + "0/e" + UIViewRoot.UNIQUE_ID_PREFIX + "0", text());
        assertEquals("/ROOT/" + UIViewRoot.UNIQUE_ID_PREFIX + "0-a", tree());

    }

    public void testUniqueId() throws Exception {
	UIComponentTag tag = new TestTag();
	boolean exceptionThrown = false;
	try {
	    tag.setId(UIViewRoot.UNIQUE_ID_PREFIX + "ha");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
	
	exceptionThrown = false;
	try {
	    tag.setId("ha");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue(!exceptionThrown);

    }


    // Test configuring value bindings instead of literal values
    public void testValueBindings() throws Exception {

	TestTag tag = new TestTag();
	tag.setLabel("#{foo}");
	tag.setRendered("#{bar}");
	add(root, tag);
	request.setAttribute("foo", "bap");
	request.setAttribute("bar", Boolean.FALSE);
	reset();
	render();

	UIViewRoot vr = facesContext.getViewRoot();
	assertEquals(1, vr.getChildren().size());
	UIComponent component = (UIComponent) vr.getChildren().get(0);
	assertEquals("bap", ((TestComponent) component).getLabel());
	assertTrue(!component.isRendered());
	assertNotNull(component.getValueBinding("label"));
	assertNotNull(component.getValueBinding("rendered"));

    }


    // ------------------------------------------------------ Protected Methods


    // Add a new child tag with the specified parent (null==root tag)
    protected void add(Tag parent, Tag child) {

        if (parent == null) {
            if (root != null) {
                throw new IllegalStateException("Root tag already set");
            }
	    root = new TestTag("ROOT", "root") {
		    protected void setProperties(UIComponent component) {
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

        UIComponentTag a = new TestTag("A", "a");
        UIComponentTag b2 = new TestTag("B2", "b2");
        b2.setRendered(rendered ? "true" : "false");
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

	try {
	    facesContext.getViewRoot().encodeAll(facesContext);
	}
	catch (IOException e) {
	    throw new JspException(e);
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

        pageContext.clearPageScope();
        pageContext.removeAttribute("javax.faces.webapp.AUTO_INDEX",
                                    PageContext.REQUEST_SCOPE);
        MockJspWriter writer = (MockJspWriter) pageContext.getOut();
        writer.clearBuffer();

    }


    // Return a String of all the component ids in treewalk order
    protected String tree() {

        UIComponent component = facesContext.getViewRoot();
        StringBuffer sb = new StringBuffer();
        tree(component, sb);
        return (sb.toString());

    }

    protected void tree(UIComponent component, StringBuffer sb) {

        sb.append("/");
        if (component.getId() != null) {
            sb.append(component.getId());
        }
        if (component instanceof TestComponent) {
            String label = ((TestComponent) component).getLabel();
            if (label != null) {
                sb.append("-");
                sb.append(label);
            }
        }
        Iterator kids = component.getChildren().iterator();
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

        UIComponent b2c = facesContext.getViewRoot().findComponent("B2");
        assertNotNull("B2 component exists", b2c);
        assertEquals("B2 component id", "B2", b2c.getId());
        assertEquals("B2 child count", 2,
                     b2c.getChildren().size());
        assertNotNull("B2 header facet",
                      (UIComponent) b2c.getFacets().get("header"));
        assertNotNull("B2 footer facet",
                      (UIComponent) b2c.getFacets().get("footer"));

    }


}
