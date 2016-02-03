/*
 * $Id: UISelectOneTestCase.java,v 1.27 2007/04/27 22:00:15 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * <p>Unit tests for {@link UISelectOne}.</p>
 */

public class UISelectOneTestCase extends UIInputTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectOneTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UISelectOne();
        expectedFamily = UISelectOne.COMPONENT_FAMILY;
        expectedRendererType = "javax.faces.Menu";
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UISelectOneTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test a pristine UISelectOne instance
    public void testPristine() {

        super.testPristine();
        UISelectOne selectOne = (UISelectOne) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UISelectOne selectOne = (UISelectOne) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UISelectOne selectOne = (UISelectOne) component;

    }


    // Test validation of value against the valid list
    public void testValidation() throws Exception {

        // Put our component under test in a tree under a UIViewRoot
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.getChildren().add(component);

        // Add valid options to the component under test
        UISelectOne selectOne = (UISelectOne) component;
        selectOne.getChildren().add(new UISelectItemSub("foo", null, null));
        selectOne.getChildren().add(new UISelectItemSub("bar", null, null));
        selectOne.getChildren().add(new UISelectItemSub("baz", null, null));

        // Validate a value that is on the list
        selectOne.setValid(true);
        selectOne.setSubmittedValue("bar");
        selectOne.setRendererType(null); // We don't have any renderers
        selectOne.validate(facesContext);
        assertTrue(selectOne.isValid());

        // Validate a value that is not on the list
        selectOne.getAttributes().put("label", "mylabel");
        selectOne.setValid(true);
        selectOne.setSubmittedValue("bop");
        selectOne.validate(facesContext);
        assertTrue(!selectOne.isValid());
        Iterator messages = facesContext.getMessages();
        while (messages.hasNext()) {
            FacesMessage message = (FacesMessage) messages.next();
            assertTrue(message.getSummary().indexOf("mylabel") >= 0);
        }
    }

    // Test validation of component with UISelectItems pointing to map
    public void testValidation2() throws Exception {

         // Put our component under test in a tree under a UIViewRoot
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.getChildren().add(component);

        // Add valid options to the component under test
        Map map = new HashMap();
        map.put("key_foo", "foo");
        map.put("key_bar", "bar");
        map.put("key_baz", "baz");
        UISelectItems items = new UISelectItems();
        items.setValue(map);
        UISelectOne selectOne = (UISelectOne) component;
        selectOne.getChildren().add(items);

        selectOne.setValid(true);
        selectOne.setSubmittedValue("foo");
        selectOne.validate(facesContext);
        assertTrue(selectOne.isValid());

        // Validate one value on the list and one not on the list
        selectOne.setValid(true);
        selectOne.setSubmittedValue("car");
        selectOne.setRendererType(null); // We don't have any renderers
        selectOne.validate(facesContext);
        assertTrue(!selectOne.isValid());

    }


    private String legalValues[] =
    { "A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3" };


    private String illegalValues[] =
    { "D1", "D2", "Group A", "Group B", "Group C" };

    // Test validation against a nested list of available options
    public void testValidateNested() throws Exception {

        // Set up UISelectOne with nested UISelectItems
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.getChildren().add(component);
        UISelectOne selectOne = (UISelectOne) component;
        UISelectItems selectItems = new UISelectItems();
        selectItems.setValue(setupOptions());
        selectOne.getChildren().add(selectItems);
        selectOne.setRequired(true);
        checkMessages(0);

        // Verify that all legal values will validate
        for (int i = 0; i < legalValues.length; i++) {
            selectOne.setValid(true);
            selectOne.setSubmittedValue(legalValues[i]);
            selectOne.validate(facesContext);
            assertTrue("Value '" + legalValues[i] + "' found",
                       selectOne.isValid());
            checkMessages(0);
        }

        // Verify that illegal values will not validate
        for (int i = 0; i < illegalValues.length; i++) {
            selectOne.setValid(true);
            selectOne.setSubmittedValue(illegalValues[i]);
            selectOne.validate(facesContext);
            assertTrue("Value '" + illegalValues[i] + "' not found",
                       !selectOne.isValid());
            checkMessages(i + 1);
        }


    }


    // Test validation of a required field
    public void testValidateRequired() throws Exception {

        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.getChildren().add(component);
        UISelectOne selectOne = (UISelectOne) component;
        selectOne.getChildren().add(new UISelectItemSub("foo", null, null));
        selectOne.getChildren().add(new UISelectItemSub("bar", null, null));
        selectOne.getChildren().add(new UISelectItemSub("baz", null, null));
        selectOne.setRequired(true);
        checkMessages(0);

        selectOne.setValid(true);
        selectOne.setSubmittedValue("foo");
        selectOne.validate(facesContext);
        checkMessages(0);
        assertTrue(selectOne.isValid());

        selectOne.setValid(true);
        selectOne.setSubmittedValue("");
        selectOne.validate(facesContext);
        checkMessages(1);
        assertTrue(!selectOne.isValid());

        selectOne.setValid(true);
        selectOne.setSubmittedValue(null);
        // awiner: see UIInputTestCase
        selectOne.validate(facesContext);
        checkMessages(1);
        assertTrue(selectOne.isValid());

    }


    // Test that appropriate properties are value binding enabled
    public void testValueBindings() {

	super.testValueBindings();
	UISelectOne test = (UISelectOne) component;

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

    public void testSelectItemsIterator() {
        // sub test 1 : non-selectItem at end
        UISelectOne selectOne = (UISelectOne) component;
        selectOne.getChildren().add(new UISelectItemSub("orr", null, null));
        selectOne.getChildren().add(new UISelectItemSub("esposito", null, null));
        UIParameter param = new UIParameter();
        param.setName("param");
        param.setValue("paramValue");
        selectOne.getChildren().add(param);
        Iterator iter = new SelectItemsIterator(selectOne);
        while (iter.hasNext()) {
            Object object = iter.next();
            assertTrue(object instanceof javax.faces.model.SelectItem);
            assertTrue((((SelectItem)object).getValue().equals("orr")) || 
                (((SelectItem)object).getValue().equals("esposito")));
        }

        // sub test 2: non-selectitem in middle
        selectOne = new UISelectOne();
        selectOne.getChildren().add(new UISelectItemSub("gretsky", null, null));
        selectOne.getChildren().add(param);
        selectOne.getChildren().add(new UISelectItemSub("howe", null, null));
        iter = new SelectItemsIterator(selectOne);
        while (iter.hasNext()) {
            Object object = iter.next();
            assertTrue(object instanceof javax.faces.model.SelectItem);
            assertTrue((((SelectItem)object).getValue().equals("gretsky")) || 
                (((SelectItem)object).getValue().equals("howe")));
        }
    }


    // --------------------------------------------------------- Support Methods


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UISelectOne();
        component.setRendererType(null);
        return (component);
    }


    protected void setupNewValue(UIInput input) {

        input.setSubmittedValue("foo");
        UISelectItem si = new UISelectItem();
        si.setItemValue("foo");
        si.setItemLabel("foo label");
        input.getChildren().add(si);

    }


    // Create an options list with nested groups
    protected List setupOptions() {
        SelectItemGroup group, subgroup;
        subgroup = new SelectItemGroup("Group C");
        subgroup.setSelectItems(new SelectItem[]
            { new SelectItem("C1"),
              new SelectItem("C2"),
              new SelectItem("C3") });
        List options = new ArrayList();
        options.add(new SelectItem("A1"));
        group = new SelectItemGroup("Group B");
        group.setSelectItems(new SelectItem[]
            { new SelectItem("B1"),
              subgroup,
              new SelectItem("B2"),
              new SelectItem("B3") });
        options.add(group);
        options.add(new SelectItem("A2"));
        options.add(new SelectItem("A3"));
        return (options);
    }


}
