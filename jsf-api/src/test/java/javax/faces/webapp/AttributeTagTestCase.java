/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;
import junit.framework.Test;
import junit.framework.TestSuite;



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
