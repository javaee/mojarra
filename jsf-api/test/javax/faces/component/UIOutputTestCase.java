/*
 * $Id: UIOutputTestCase.java,v 1.22 2007/04/27 22:00:15 ofung Exp $
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
    public void setUp() {
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


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
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


    


    public void testConverterState() {

        UIOutput output = (UIOutput) createComponent();
        DateTimeConverter converter = new DateTimeConverter();
        converter.setPattern("MM-dd-yy");
        output.setConverter(converter);
        output.markInitialState();
        assertTrue(output.initialStateMarked());
        assertTrue(converter.initialStateMarked());

        Object result = output.saveState(facesContext);
        // initial state has been marked an no changes
        // have occurred, we should have null state.
        assertNull(result);

        // setup the scenario again, but this time,
        // update the converter pattern.
        output = (UIOutput) createComponent();
        converter = new DateTimeConverter();
        converter.setPattern("MM-dd-yy");
        output.setConverter(converter);
        output.markInitialState();
        assertTrue(output.initialStateMarked());
        assertTrue(converter.initialStateMarked());

        // now tweak the converter
        converter.setPattern("dd-MM-yy");
        result = output.saveState(facesContext);
        assertTrue(result instanceof Object[]);
        Object[] state = (Object[]) result;

        // state should have a lenght of 2.  The first element
        // is the state from UIComponentBase, where the second
        // is the converter state.  The first element in this
        // case should be null
        assertTrue(state.length == 2);
        assertTrue(state[0] == null);
        assertTrue(state[1] != null);

        output = (UIOutput) createComponent();
        converter = new DateTimeConverter();
        output.setConverter(converter);

        // now validate what we've restored
        // first, ensure converter is null.  This will
        // be the case when initialState has been marked
        // for the component.
        output.restoreState(facesContext, state);
        assertTrue(output.getConverter() != null);
        assertTrue("dd-MM-yy".equals(converter.getPattern()));

        // now validate the case where UIOutput has some event
        // that adds a converter *after* initial state has been
        // marked.  This will cause the component to save full
        // state.
        output = (UIOutput) createComponent();
        output.markInitialState();
        output.setConverter(converter);
        assertTrue(!output.initialStateMarked());
        assertTrue(!converter.initialStateMarked());

        result = output.saveState(facesContext);
        assertNotNull(result);

        // this time, both elements in the state array will not
        // be null.  If we call retoreState() on a new component instance
        // without setting a converter, we should have a new DateTimeConverter
        // *with* the expected pattern.
        assertTrue(result instanceof Object[]);
        state = (Object[]) result;
        assertTrue(state.length == 2);
        assertTrue(state[1] instanceof StateHolderSaver);
        output = (UIOutput) createComponent();
        assertNull(output.getConverter());
        output.restoreState(facesContext, state);
        Converter c = output.getConverter();
        assertNotNull(c);
        assertTrue(c instanceof DateTimeConverter);
        converter = (DateTimeConverter) c;
        assertTrue("dd-MM-yy".equals(converter.getPattern()));

    }


    // --------------------------------------------------------- Support Methods


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIOutput();
        component.setRendererType(null);
        return (component);
    }


}
