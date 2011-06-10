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

package javax.faces.component;


import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.LongConverter;
import javax.faces.convert.ShortConverter;


/**
 * <p>Unit tests for {@link UIOutput}.</p>
 */

public class UIOutputTestCase extends ValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIOutputTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() throws Exception  {
        super.setUp();
        component = new UIOutput();
        expectedFamily = UIOutput.COMPONENT_FAMILY;
        expectedId = null;
        expectedRendererType = "javax.faces.Text";
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIOutputTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIOutput output = (UIOutput) component;

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UIOutput instance
    public void testPristine() {

        super.testPristine();
        UIOutput output = (UIOutput) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIOutput output = (UIOutput) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIOutput output = (UIOutput) component;

    }


    public void testValueBindings() {

	super.testValueBindings();
	UIOutput test = (UIOutput) component;

	// "converter" property
	request.setAttribute("foo", new LongConverter());
	test.setConverter(null);
	assertNull(test.getConverter());
	test.setValueBinding("converter", application.createValueBinding("#{foo}"));
	assertNotNull(test.getValueBinding("converter"));
	assertTrue(test.getConverter() instanceof LongConverter);
	test.setConverter(new ShortConverter());
	assertTrue(test.getConverter() instanceof ShortConverter);
	test.setConverter(null);
	assertTrue(test.getConverter() instanceof LongConverter);
	test.setValueBinding("converter", null);
	assertNull(test.getValueBinding("converter"));
	assertNull(test.getConverter());

	// "value" property
	request.setAttribute("foo", "bar");
	test.setValue(null);
	assertNull(test.getValue());
	test.setValueBinding("value", application.createValueBinding("#{foo}"));
	assertNotNull(test.getValueBinding("value"));
	assertEquals("bar", test.getValue());
	test.setValue("baz");
	assertEquals("baz", test.getValue());
	test.setValue(null);
	assertEquals("bar", test.getValue());
	test.setValueBinding("value", null);
	assertNull(test.getValueBinding("value"));
	assertNull(test.getValue());

    }


    


    


    // --------------------------------------------------------- Support Methods


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIOutput();
        component.setRendererType(null);
        return (component);
    }


}
