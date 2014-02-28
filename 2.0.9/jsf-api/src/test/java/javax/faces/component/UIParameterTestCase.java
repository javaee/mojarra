/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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


/**
 * <p>Unit tests for {@link UIParameter}.</p>
 */

public class UIParameterTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIParameterTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    @Override
    public void setUp() throws Exception {
        super.setUp();
        component = new UIParameter();
        expectedFamily = UIParameter.COMPONENT_FAMILY;
        expectedId = null;
        expectedRendererType = null;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIParameterTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIParameter parameter = (UIParameter) component;

        assertEquals(parameter.getValue(),
                     (String) component.getAttributes().get("value"));
        parameter.setValue("foo");
        assertEquals("foo", (String) component.getAttributes().get("value"));
        parameter.setValue(null);
        assertNull((String) component.getAttributes().get("value"));
        component.getAttributes().put("value", "bar");
        assertEquals("bar", parameter.getValue());
        component.getAttributes().put("value", null);
        assertNull(parameter.getValue());

        assertEquals(parameter.getName(),
                     (String) parameter.getAttributes().get("name"));
        parameter.setName("foo");
        assertEquals("foo", (String) parameter.getAttributes().get("name"));
        parameter.setName(null);
        assertNull((String) parameter.getAttributes().get("name"));
        parameter.getAttributes().put("name", "bar");
        assertEquals("bar", parameter.getName());
        parameter.getAttributes().put("name", null);
        assertNull(parameter.getName());

    }

    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }

    // Test a pristine UIParameter instance
    public void testPristine() {

        super.testPristine();
        UIParameter parameter = (UIParameter) component;

        assertNull("no value", parameter.getValue());
        assertNull("no name", parameter.getName());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIParameter parameter = (UIParameter) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIParameter parameter = (UIParameter) component;

        // value
        parameter.setValue("foo.bar");
        assertEquals("expected value",
                     "foo.bar", parameter.getValue());
        parameter.setValue(null);
        assertNull("erased value", parameter.getValue());

        parameter.setName("foo");
        assertEquals("foo", parameter.getName());
        parameter.setName(null);
        assertNull(parameter.getName());

    }


    public void testValueBindings() {

	super.testValueBindings();
	UIParameter test = (UIParameter) component;

	// "name" property
	request.setAttribute("foo", "bar");
	test.setName(null);
	assertNull(test.getName());
	test.setValueBinding("name", application.createValueBinding("#{foo}"));
	assertNotNull(test.getValueBinding("name"));
	assertEquals("bar", test.getName());
	test.setName("baz");
	assertEquals("baz", test.getName());
	test.setName(null);
	assertEquals("bar", test.getName());
	test.setValueBinding("name", null);
	assertNull(test.getValueBinding("name"));
	assertNull(test.getName());

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


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {

        super.checkProperties(comp1, comp2);
        UIParameter p1 = (UIParameter) comp1;
        UIParameter p2 = (UIParameter) comp2;
        assertEquals(p1.getName(), p2.getName());

    }


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIParameter();
        component.setRendererType(null);
        return (component);
    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {
        super.populateComponent(component);
        UIParameter p = (UIParameter) component;
        p.setName("foo");
    }


}
