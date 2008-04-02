/*
 * $Id: AttributeTagTestCase.java,v 1.6 2005/10/19 19:51:23 edburns Exp $
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
 * <p>Unit tests for <code>AttributeTag</code>.</p>
 */

public class AttributeTagTestCase extends TagTestCaseBase {


    // ------------------------------------------------------ Instance Variables


    protected UIComponentTag ctag = null; // Component tag
    protected UIComponentTag rtag = null; // Root tag


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public AttributeTagTestCase(String name) {

        super(name);

    }


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up our root and component tags.
     */
    public void setUp() throws Exception {

        super.setUp();

        rtag = new TestTag("ROOT", "root") {
                protected void setProperties(UIComponent component) {
                }
            };
        rtag.setPageContext(this.pageContext);
        ctag = new TestOutputTag();
        ctag.setParent(this.rtag);
        ctag.setPageContext(this.pageContext);

        rtag.doStartTag();
        ctag.doStartTag();

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(AttributeTagTestCase.class));

    }


    /**
     * Clear our root and component tags.
     */
    public void tearDown() throws Exception {

        ctag.doEndTag();
        rtag.doEndTag();

        ctag = null;
        rtag = null;

        super.tearDown();

    }


    // ------------------------------------------------- Individual Test Methods


    // Test literal name and literal value
    public void testLiteralLiteral() throws Exception {

        UIComponent component = ((UIComponentTag) ctag).getComponentInstance();
        assertNotNull(component);
        assertTrue(!component.getAttributes().containsKey("foo"));
        AttributeTag tag = new AttributeTag();
        tag.setName("foo");
        tag.setValue("bar");
        add(tag);
        tag.doStartTag();
        assertEquals("bar",
                     (String) component.getAttributes().get("foo"));
        tag.doEndTag();
        
    }

    // Test literal name and expression value
    public void testLiteralExpression() throws Exception {

        UIComponent component = ((UIComponentTag) ctag).getComponentInstance();
        assertNotNull(component);
        assertTrue(!component.getAttributes().containsKey("foo"));
        AttributeTag tag = new AttributeTag();
        tag.setName("foo");
        tag.setValue("#{barValue}");
        add(tag);
        request.setAttribute("barValue", "bar");
        tag.doStartTag();
        assertEquals("bar",
                     (String) component.getAttributes().get("foo"));
        tag.doEndTag();
        
    }


    // Test expression name and literal value
    public void testExpressionLiteral() throws Exception {

        UIComponent component = ((UIComponentTag) ctag).getComponentInstance();
        assertNotNull(component);
        assertTrue(!component.getAttributes().containsKey("foo"));
        AttributeTag tag = new AttributeTag();
        tag.setName("#{fooValue}");
        tag.setValue("bar");
        add(tag);
        request.setAttribute("fooValue", "foo");
        tag.doStartTag();
        assertEquals("bar",
                     (String) component.getAttributes().get("foo"));
        tag.doEndTag();
        
    }


    // Test expression name and expression value
    public void testExpressionExpression() throws Exception {

        UIComponent component = ((UIComponentTag) ctag).getComponentInstance();
        assertNotNull(component);
        assertTrue(!component.getAttributes().containsKey("foo"));
        AttributeTag tag = new AttributeTag();
        tag.setName("#{fooValue}");
        tag.setValue("#{barValue}");
        add(tag);
        request.setAttribute("fooValue", "foo");
        request.setAttribute("barValue", "bar");
        tag.doStartTag();
        assertEquals("bar",
                     (String) component.getAttributes().get("foo"));
        tag.doEndTag();
        
    }


    // Test pre-existing attribute
    public void testPreExisting() throws Exception {

        UIComponent component = ((UIComponentTag) ctag).getComponentInstance();
        assertNotNull(component);
        component.getAttributes().put("foo", "bap");
        AttributeTag tag = new AttributeTag();
        tag.setName("foo");
        tag.setValue("bar");
        add(tag);
        tag.doStartTag();
        assertEquals("bap",
                     (String) component.getAttributes().get("foo"));
        tag.doEndTag();
        
    }


    // ------------------------------------------------------- Protected Methods


    // Add the specified AttributeTag to our component tag
    protected void add(AttributeTag tag) {

        tag.setParent(root);
        tag.setPageContext(this.pageContext);

    }


}
