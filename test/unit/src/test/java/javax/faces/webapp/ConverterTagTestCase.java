/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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

import javax.faces.component.ValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.convert.IntegerConverter;
import junit.framework.Test;
import junit.framework.TestSuite;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * <p>
 * Unit tests for <code>ConverterTag</code>.</p>
 */
public class ConverterTagTestCase extends TagTestCaseBase {

    // ------------------------------------------------------ Instance Variables
    protected UIComponentTag ctag = null; // Component tag
    protected UIComponentTag rtag = null; // Root tag

    // ------------------------------------------------------------ Constructors
    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConverterTagTestCase(String name) {
        super(name);
    }

    // ---------------------------------------------------- Overall Test Methods
    /**
     * Set up our root and component tags.
     * @throws java.lang.Exception
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        rtag = new TagTestImpl("ROOT", "root") {
            @Override
            protected void setProperties(UIComponent component) {
            }
        };
        rtag.setPageContext(this.pageContext);
        ctag = new OutputTagTestImpl();
        ctag.setParent(this.rtag);
        ctag.setPageContext(this.pageContext);

        rtag.doStartTag();
        ctag.doStartTag();
    }

    /**
     * Return the tests include
     * @return d in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ConverterTagTestCase.class));
    }

    /**
     * Clear our root and component tags.
     * @throws java.lang.Exception
     */
    @Override
    public void tearDown() throws Exception {
        ctag.doEndTag();
        rtag.doEndTag();

        ctag = null;
        rtag = null;

        super.tearDown();
    }

    // ------------------------------------------------- Individual Test Methods
    // Test literal converter id
    public void testLiteral() throws Exception {
        UIComponent component = ctag.getComponentInstance();
        assertNotNull(component);
        assertNull(((ValueHolder) component).getConverter());
        ConverterTag tag = new ConverterTag();
        tag.setConverterId("Integer");
        add(tag);
        tag.doStartTag();
        Converter converter = ((ValueHolder) component).getConverter();
        assertNotNull(converter);
        assertTrue(converter instanceof IntegerConverter);
        tag.doEndTag();
    }

    // Test expression converter id
    public void testExpression() throws Exception {
        UIComponent component = ctag.getComponentInstance();
        assertNotNull(component);
        assertNull(((ValueHolder) component).getConverter());
        ConverterTag tag = new ConverterTag();
        tag.setConverterId("#{foo}");
        add(tag);
        request.setAttribute("foo", "Integer");
        tag.doStartTag();
        Converter converter = ((ValueHolder) component).getConverter();
        assertNotNull(converter);
        assertTrue(converter instanceof IntegerConverter);
        tag.doEndTag();
    }

    // ------------------------------------------------------- Protected Methods
    // Add the specified ConverterTag to our component tag
    protected void add(ConverterTag tag) {
        tag.setParent(ctag);
        tag.setPageContext(this.pageContext);
    }
}
