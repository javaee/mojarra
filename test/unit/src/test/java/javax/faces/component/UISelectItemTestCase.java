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
package javax.faces.component;

import javax.faces.model.SelectItem;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link UISelectItem}.</p>
 */
public class UISelectItemTestCase extends UIComponentBaseTestCase {

    // ------------------------------------------------------------ Constructors
    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectItemTestCase(String name) {
        super(name);
    }

    // ---------------------------------------------------- Overall Test Methods
    // Set up instance variables required by this test case.
    @Override
    public void setUp() throws Exception {
        super.setUp();
        component = new UISelectItem();
        expectedFamily = UISelectItem.COMPONENT_FAMILY;
        expectedId = null;
        expectedRendererType = null;
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UISelectItemTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods
    // Suppress lifecycle tests since we do not have a renderer
    @Override
    public void testLifecycleManagement() {
    }

    // Test attribute-property transparency
    @Override
    public void testAttributesTransparency() {
        super.testAttributesTransparency();
        UISelectItem selectItem = (UISelectItem) component;

        assertEquals(selectItem.getValue(),
                component.getAttributes().get("value"));
        SelectItem item = new SelectItem("foo");
        selectItem.setValue(item);
        assertEquals(item, component.getAttributes().get("value"));
        selectItem.setValue(null);

        assertNull(component.getAttributes().get("value"));
        component.getAttributes().put("value", "bar");
        assertEquals("bar", selectItem.getValue());
        component.getAttributes().put("value", null);
        assertNull(selectItem.getValue());

        assertEquals(selectItem.getItemDescription(),
                (String) selectItem.getAttributes().get("itemDescription"));
        selectItem.setItemDescription("foo");
        assertEquals("foo", (String) selectItem.getAttributes().get("itemDescription"));
        selectItem.setItemDescription(null);
        assertNull((String) selectItem.getAttributes().get("itemDescription"));
        selectItem.getAttributes().put("itemDescription", "bar");
        assertEquals("bar", selectItem.getItemDescription());
        selectItem.getAttributes().put("itemDescription", null);
        assertNull(selectItem.getItemDescription());

        assertEquals(selectItem.isItemDisabled(),
                ((Boolean) selectItem.getAttributes().get("itemDisabled")).
                booleanValue());
        selectItem.setItemDisabled(true);
        assertTrue(((Boolean) selectItem.getAttributes().
                get("itemDisabled")).booleanValue());
        selectItem.setItemDisabled(false);
        assertFalse(((Boolean) selectItem.getAttributes().
                get("itemDisabled")).booleanValue());
        selectItem.getAttributes().put("itemDisabled", Boolean.FALSE);
        assertFalse(selectItem.isItemDisabled());
        selectItem.getAttributes().put("itemDisabled", Boolean.TRUE);
        assertTrue(selectItem.isItemDisabled());

        assertEquals(selectItem.getItemLabel(),
                (String) selectItem.getAttributes().get("itemLabel"));
        selectItem.setItemLabel("foo");
        assertEquals("foo", (String) selectItem.getAttributes().get("itemLabel"));
        selectItem.setItemLabel(null);
        assertNull((String) selectItem.getAttributes().get("itemLabel"));
        selectItem.getAttributes().put("itemLabel", "bar");
        assertEquals("bar", selectItem.getItemLabel());
        selectItem.getAttributes().put("itemLabel", null);
        assertNull(selectItem.getItemLabel());

        assertEquals(selectItem.getItemValue(),
                (String) selectItem.getAttributes().get("itemValue"));
        selectItem.setItemValue("foo");
        assertEquals("foo", (String) selectItem.getAttributes().get("itemValue"));
        selectItem.setItemValue(null);
        assertNull((String) selectItem.getAttributes().get("itemValue"));
        selectItem.getAttributes().put("itemValue", "bar");
        assertEquals("bar", selectItem.getItemValue());
        selectItem.getAttributes().put("itemValue", null);
        assertNull(selectItem.getItemValue());
    }

    // Test a pristine UISelectItem instance
    @Override
    public void testPristine() {
        super.testPristine();
        UISelectItem selectItem = (UISelectItem) component;

        assertNull("no value", selectItem.getValue());
        assertNull("no itemDescription", selectItem.getItemDescription());
        assertFalse("no itemDisabled", selectItem.isItemDisabled());
        assertNull("no itemLabel", selectItem.getItemLabel());
        assertNull("no itemValue", selectItem.getItemValue());
    }

    // Test setting properties to invalid values
    @Override
    public void testPropertiesInvalid() throws Exception {
        super.testPropertiesInvalid();
        UISelectItem selectItem = (UISelectItem) component;
    }

    // Test setting properties to valid values
    @Override
    public void testPropertiesValid() throws Exception {
        super.testPropertiesValid();
        UISelectItem selectItem = (UISelectItem) component;

        // value
        SelectItem item = new SelectItem("foo");
        selectItem.setValue(item);
        assertEquals("expected value",
                item, selectItem.getValue());
        selectItem.setValue(null);
        assertNull("erased value", selectItem.getValue());

        selectItem.setItemDescription("foo");
        assertEquals("foo", selectItem.getItemDescription());
        selectItem.setItemDescription(null);
        assertNull(selectItem.getItemDescription());

        selectItem.setItemDisabled(false);
        assertFalse(selectItem.isItemDisabled());
        selectItem.setItemDisabled(true);
        assertTrue(selectItem.isItemDisabled());

        selectItem.setItemLabel("foo");
        assertEquals("foo", selectItem.getItemLabel());
        selectItem.setItemLabel(null);
        assertNull(selectItem.getItemLabel());

        selectItem.setItemValue("foo");
        assertEquals("foo", selectItem.getItemValue());
        selectItem.setItemValue(null);
        assertNull(selectItem.getItemValue());
    }

    public void PENDING_FIXME_testValueBindings() {

        super.testValueBindings();
        UISelectItem test = (UISelectItem) component;

        // "itemDescription" property
        request.setAttribute("foo", "bar");
        test.setItemDescription(null);
        assertNull(test.getItemDescription());
        test.setValueBinding("itemDescription", application.createValueBinding("#{foo}"));
        assertNotNull(test.getValueBinding("itemDescription"));
        assertEquals("bar", test.getItemDescription());
        test.setItemDescription("baz");
        assertEquals("baz", test.getItemDescription());
        test.setItemDescription(null);
        assertEquals("bar", test.getItemDescription());
        test.setValueBinding("itemDescription", null);
        assertNull(test.getValueBinding("itemDescription"));
        assertNull(test.getItemDescription());

        // "itemDisabled" property
        assertFalse(test.isItemDisabled());
        request.setAttribute("foo", Boolean.TRUE);
        test.setValueBinding("itemDisabled", application.createValueBinding("#{foo}"));
        assertNotNull(test.getValueBinding("itemDisabled"));
        assertTrue(test.isItemDisabled());
        test.setItemDisabled(false);
        assertFalse(test.isItemDisabled());
        test.setValueBinding("itemDisabled", null);
        assertNull(test.getValueBinding("itemDisabled"));
        assertFalse(test.isItemDisabled());

        // "itemLabel" property
        request.setAttribute("foo", "bar");
        test.setItemLabel(null);
        assertNull(test.getItemLabel());
        test.setValueBinding("itemLabel", application.createValueBinding("#{foo}"));
        assertNotNull(test.getValueBinding("itemLabel"));
        assertEquals("bar", test.getItemLabel());
        test.setItemLabel("baz");
        assertEquals("baz", test.getItemLabel());
        test.setItemLabel(null);
        assertEquals("bar", test.getItemLabel());
        test.setValueBinding("itemLabel", null);
        assertNull(test.getValueBinding("itemLabel"));
        assertNull(test.getItemLabel());

        // "itemValue" property
        request.setAttribute("foo", "bar");
        test.setItemValue(null);
        assertNull(test.getItemValue());
        test.setValueBinding("itemValue", application.createValueBinding("#{foo}"));
        assertNotNull(test.getValueBinding("itemValue"));
        assertEquals("bar", test.getItemValue());
        test.setItemValue("baz");
        assertEquals("baz", test.getItemValue());
        test.setItemValue(null);
        assertEquals("bar", test.getItemValue());
        test.setValueBinding("itemValue", null);
        assertNull(test.getValueBinding("itemValue"));
        assertNull(test.getItemValue());

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
    @Override
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {
        super.checkProperties(comp1, comp2);
        UISelectItem si1 = (UISelectItem) comp1;
        UISelectItem si2 = (UISelectItem) comp2;
        assertEquals(si1.getItemDescription(), si2.getItemDescription());
        assertEquals(si1.isItemDisabled(), si2.isItemDisabled());
        assertEquals(si1.getItemLabel(), si2.getItemLabel());
        assertEquals(si1.getItemValue(), si2.getItemValue());
    }

    // Create a pristine component of the type to be used in state holder tests
    @Override
    protected UIComponent createComponent() {
        UIComponent component = new UISelectItem();
        component.setRendererType(null);
        return (component);
    }

    // Populate a pristine component to be used in state holder tests
    @Override
    protected void populateComponent(UIComponent component) {
        super.populateComponent(component);
        UISelectItem si = (UISelectItem) component;
        si.setItemDescription("item description");
        si.setItemLabel("item label");
        si.setItemValue("item value");
    }
}
