/*
 * $Id: UISelectItemsTestCase.java,v 1.11 2005/08/22 22:08:20 ofung Exp $
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

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UISelectItems}.</p>
 */

public class UISelectItemsTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectItemsTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UISelectItems();
        expectedFamily = UISelectItems.COMPONENT_FAMILY;
        expectedId = null;
        expectedRendererType = null;
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UISelectItemsTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UISelectItems selectItems = (UISelectItems) component;

        assertEquals(selectItems.getValue(),
                     component.getAttributes().get("value"));
        SelectItem item = new SelectItem("foo");
        selectItems.setValue(item);
        assertEquals(item, component.getAttributes().get("value"));
        selectItems.setValue(null);
        assertNull(component.getAttributes().get("value"));
        component.getAttributes().put("value", "bar");
        assertEquals("bar", selectItems.getValue());
        component.getAttributes().put("value", null);
        assertNull(selectItems.getValue());

    }
    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }

    // Test a pristine UISelectItems instance
    public void testPristine() {

        super.testPristine();
        UISelectItems selectItems = (UISelectItems) component;
        assertNull("no value", selectItems.getValue());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UISelectItems selectItems = (UISelectItems) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UISelectItems selectItems = (UISelectItems) component;

        // value
        SelectItem item = new SelectItem("foo");
        selectItems.setValue(item);
        assertEquals("expected value",
                     item, selectItems.getValue());
        selectItems.setValue(null);
        assertNull("erased value", selectItems.getValue());
    }


    public void testValueBindings() {

	super.testValueBindings();
	UISelectItems test = (UISelectItems) component;

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
        UIComponent component = new UISelectItems();
        component.setRendererType(null);
        return (component);
    }


}
