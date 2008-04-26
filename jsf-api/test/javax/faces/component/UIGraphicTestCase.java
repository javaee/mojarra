/*
 * $Id: UIGraphicTestCase.java,v 1.22 2007/04/27 22:00:15 ofung Exp $
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


import java.io.IOException;
import java.util.Iterator;
import javax.faces.el.ValueBinding;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIGraphic}.</p>
 */

public class UIGraphicTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIGraphicTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIGraphic();
        expectedFamily = UIGraphic.COMPONENT_FAMILY;
        expectedId = null;
        expectedRendererType = "javax.faces.Image";
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIGraphicTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIGraphic graphic = (UIGraphic) component;

        assertEquals(graphic.getValue(),
                     (String) component.getAttributes().get("value"));
        graphic.setValue("foo");
        assertEquals("foo", (String) component.getAttributes().get("value"));
        graphic.setValue(null);
        assertNull((String) component.getAttributes().get("value"));
        component.getAttributes().put("value", "bar");
        assertEquals("bar", graphic.getValue());
        component.getAttributes().put("value", null);
        assertNull(graphic.getValue());

        assertEquals(graphic.getUrl(),
                     (String) graphic.getAttributes().get("url"));
        graphic.setUrl("foo");
        assertEquals("foo", (String) graphic.getAttributes().get("url"));
        graphic.setUrl(null);
        assertNull((String) graphic.getAttributes().get("url"));
        graphic.getAttributes().put("url", "bar");
        assertEquals("bar", graphic.getUrl());
        graphic.getAttributes().put("url", null);
        assertNull(graphic.getUrl());

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }

    // Test a pristine UIGraphic instance
    public void testPristine() {

        super.testPristine();
        UIGraphic graphic = (UIGraphic) component;

        assertNull("no value", graphic.getValue());
        assertNull("no url", graphic.getUrl());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIGraphic graphic = (UIGraphic) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIGraphic graphic = (UIGraphic) component;

        // value
        graphic.setValue("foo.bar");
        assertEquals("expected value",
                     "foo.bar", graphic.getValue());
        graphic.setValue(null);
        assertNull("erased value", graphic.getValue());

        // Test transparency between "value" and "url" properties
        graphic.setUrl("foo");
        assertEquals("foo", (String) graphic.getValue());
        graphic.setUrl(null);
        assertNull(graphic.getValue());
        graphic.setValue("bar");
        assertEquals("bar", graphic.getUrl());
        graphic.setValue(null);
        assertNull(graphic.getUrl());

        // Transparency applies to value bindings as well
        assertNull(graphic.getValueBinding("url"));
        assertNull(graphic.getValueBinding("value"));
        request.setAttribute("foo", "bar");
        ValueBinding vb = application.createValueBinding("#{foo}");
        graphic.setValueBinding("url", vb);
        assertTrue(vb == graphic.getValueBinding("url"));
        assertTrue(vb == graphic.getValueBinding("value"));
        graphic.setValueBinding("url", null);
        assertNull(graphic.getValueBinding("url"));
        assertNull(graphic.getValueBinding("value"));
        graphic.setValueBinding("value", vb);
        assertTrue(vb == graphic.getValueBinding("url"));
        assertTrue(vb == graphic.getValueBinding("value"));
        graphic.setValueBinding("url", null);
        assertNull(graphic.getValueBinding("url"));
        assertNull(graphic.getValueBinding("value"));

    }


    public void PENDING_FIXME_testValueBindings() {

	super.PENDING_FIXME_testValueBindings();
	UIGraphic test = (UIGraphic) component;

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
        UIComponent component = new UIGraphic();
        component.setRendererType(null);
        return (component);
    }


}
