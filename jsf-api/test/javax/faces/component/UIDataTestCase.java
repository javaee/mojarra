/*
 * $Id: UIDataTestCase.java,v 1.46 2008/03/24 20:07:19 rlubke Exp $
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


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import com.sun.faces.mock.MockExternalContext;
import com.sun.faces.mock.MockResponseWriter;
import javax.faces.FacesException;
import javax.faces.TestUtil;
import javax.faces.component.UIComponentBaseTestCase;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIData}.</p>
 */

public class UIDataTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIDataTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIData();
        expectedFamily = UIData.COMPONENT_FAMILY;
        expectedId = null;
        expectedRendererType = "javax.faces.Table";
        expectedRendersChildren = true;
        beans = new ArrayList();
        for (int i = 0; i < 10; i++) {
            TestDataBean bean = new TestDataBean();
            bean.setCommand("command" + i);
            bean.setInput("input" + i);
            bean.setOutput("output" + i);
            beans.add(bean);
        }
        model = new ListDataModel(beans);
        assertEquals(10, model.getRowCount());
        swriter = new StringWriter();
        facesContext.setResponseWriter(new MockResponseWriter(swriter, null));
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIDataTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
        beans = null;
        model = null;
        swriter = null;
    }


    // ------------------------------------------------------ Instance Variables


    // The list of beans represented by our DataModel instance
    protected List beans = null;


    // The DataModel wrapping our list of beans
    protected DataModel model = null;


    // The StringWriter used to buffer our rendered output
    protected StringWriter swriter = null;


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIData data = (UIData) component;

        assertEquals(data.getValue(),
                     (String) component.getAttributes().get("value"));
        data.setValue("foo");
        assertEquals("foo", (String) component.getAttributes().get("value"));
        data.setValue(null);
        assertNull((String) component.getAttributes().get("value"));
        component.getAttributes().put("value", "bar");
        assertEquals("bar", data.getValue());
        component.getAttributes().put("value", null);
        assertNull(data.getValue());

        data.setFirst(6);
        assertEquals(data.getFirst(),
                     ((Integer) data.getAttributes().get("first")).intValue());
        data.getAttributes().put("first", new Integer(7));
        assertEquals(data.getFirst(),
                     ((Integer) data.getAttributes().get("first")).intValue());

        data.setRows(10);
        assertEquals(data.getRows(),
                     ((Integer) data.getAttributes().get("rows")).intValue());
        data.getAttributes().put("rows", new Integer(20));
        assertEquals(data.getRows(),
                     ((Integer) data.getAttributes().get("rows")).intValue());

        assertEquals(data.getVar(),
                     (String) data.getAttributes().get("var"));
        data.setVar("foo");
        assertEquals("foo", (String) data.getAttributes().get("var"));
        data.setVar(null);
        assertNull((String) data.getAttributes().get("var"));
        data.getAttributes().put("var", "bar");
        assertEquals("bar", data.getVar());
        data.getAttributes().put("var", null);
        assertNull(data.getVar());

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a UIData instance with a pristine DataModel and "var"
    public void testModelPristine() throws Exception {

        UIData data = (UIData) component;
        setupModel();
        assertEquals("correct first", 3, data.getFirst());
        assertEquals("correct rowCount", beans.size(), data.getRowCount());
        assertEquals("correct rowIndex", -1, data.getRowIndex());
        assertEquals("correct rows", 5, data.getRows());
        assertTrue("correct value", model == (DataModel) data.getValue());

    }


    // Test reading the per-row data values
    public void testModelRead() throws Exception {

        ValueBinding vb = application.createValueBinding("foo");
        ValueBinding vbCommand = application.createValueBinding("foo.command");
        ValueBinding vbInput = application.createValueBinding("foo.input");
        ValueBinding vbOutput = application.createValueBinding("foo.output");
        UIData data = (UIData) component;
        setupModel();
        setupRenderers();
        setupTree(true);

        for (int i = 0; i < beans.size(); i++) {

            // Set the row index
            data.setRowIndex(i);
            assertEquals(i, data.getRowIndex());

            // Validate the row data
            assertTrue(beans.get(i) == data.getRowData());
            TestDataBean bean = (TestDataBean) data.getRowData();
            assertNotNull("Row " + i + " data exists", bean);
            assertEquals("command" + i, bean.getCommand());
            assertEquals("input" + i, bean.getInput());
            assertEquals("output" + i, bean.getOutput());

            // Validate the exposed value
            Object foo = vb.getValue(facesContext);
            assertNotNull("Row " + i + " data exposed", foo);
            assertTrue("Row " + i + " data correct",
                       foo == bean);

            // Validate value references to the exposed value
            assertEquals("command" + i, vbCommand.getValue(facesContext));
            assertEquals("input" + i, vbInput.getValue(facesContext));
            assertEquals("output" + i, vbOutput.getValue(facesContext));

        }

        data.setRowIndex(-1);
        Object foo = vb.getValue(facesContext);
        assertNull("Data object removed", foo);

    }


    // Test writing the per-row data values
    public void testModelWrite() throws Exception {

        ValueBinding vb = application.createValueBinding("foo");
        ValueBinding vbCommand = application.createValueBinding("foo.command");
        ValueBinding vbInput = application.createValueBinding("foo.input");
        ValueBinding vbOutput = application.createValueBinding("foo.output");
        UIData data = (UIData) component;
        setupModel();
        setupRenderers();
        setupTree(true);

        for (int i = 0; i < beans.size(); i++) {

            // Set the row index (skipping even rows)
            if ((i % 2) == 0) {
                continue;
            }
            data.setRowIndex(i);
            assertEquals(i, data.getRowIndex());

            // Validate the row data
            assertTrue(beans.get(i) == data.getRowData());
            TestDataBean bean = (TestDataBean) data.getRowData();
            assertNotNull("Row " + i + " data exists", bean);

            // Validate the exposed value
            Object foo = vb.getValue(facesContext);
            assertNotNull("Row " + i + " data exposed", foo);
            assertTrue("Row " + i + " data correct",
                       foo == bean);

            // Update via the exposed bean
            bean.setCommand("command" + i + "A");
            bean.setInput("input" + i + "B");
            bean.setOutput("output" + i + "C");

            // Validate updated value references
            assertEquals("command" + i + "A", vbCommand.getValue(facesContext));
            assertEquals("input" + i + "B", vbInput.getValue(facesContext));
            assertEquals("output" + i + "C", vbOutput.getValue(facesContext));

            // Update via value references
            vbCommand.setValue(facesContext, "command" + i + "D");
            vbInput.setValue(facesContext, "input" + i + "E");
            vbOutput.setValue(facesContext, "output" + i + "F");

            // Validate bean properties
            assertEquals("command" + i + "D", bean.getCommand());
            assertEquals("input" + i + "E", bean.getInput());
            assertEquals("output" + i + "F", bean.getOutput());

        }


        // Revalidate the entire collection to catch stray updates
        for (int i = 0; i < beans.size(); i++) {
            data.setRowIndex(i);
            assertEquals(i, data.getRowIndex());
            TestDataBean bean = (TestDataBean) data.getRowData();
            assertNotNull("Row " + i + " data exists", bean);
            if ((i % 2) == 0) {
                assertEquals("command" + i, bean.getCommand());
                assertEquals("input" + i, bean.getInput());
                assertEquals("output" + i, bean.getOutput());
            } else {
                assertEquals("command" + i + "D", bean.getCommand());
                assertEquals("input" + i + "E", bean.getInput());
                assertEquals("output" + i + "F", bean.getOutput());
            }
        }

    }


    // Test a pristine UIData instance
    public void testPristine() {

        super.testPristine();
        UIData data = (UIData) component;

        assertNull("no value", data.getValue());
        assertEquals("no first", 0, data.getFirst());
        assertEquals("no rows", 0, data.getRows());
        assertNull("no var", data.getVar());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIData data = (UIData) component;

        try {
            data.setFirst(-1);
            fail("Should have thrown IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            data.setRows(-1);
            fail("Should have thrown IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIData data = (UIData) component;

        // value
        data.setValue("foo.bar");
        assertEquals("expected value",
                     "foo.bar", data.getValue());
        data.setValue(null);
        assertNull("erased value", data.getValue());
        data.setFirst(0);
        data.setFirst(11);
        data.setRows(0);
        data.setRows(20);
        data.setVar(null);
        data.setVar("foo");

    }


    // Test request processing lifecycle (successful input)
    public void testTreeLifecycle() throws Exception {

        ValueBinding vbCommand = application.createValueBinding("foo.command");
        ValueBinding vbInput = application.createValueBinding("foo.input");
        ValueBinding vbOutput = application.createValueBinding("foo.output");
        String before[] =
            { "input3", "input4", "input5", "input6", "input7" };
        String after[] =
            { "input3", "input4A", "input5", "input6B", "input7" };
        String nulls[] =
            { null, null, null, null, null };

        // Set up for this test
        setupModel();
        setupRenderers();
        UICommand command = setupTree(true);
        UIData data = (UIData) component;
        checkLocalValues(nulls);

        // Set up our fake request parameters (two command invocations)
        Map params = new HashMap();
        params.put("data:5:command", "");
        params.put("data:7:command", "");
        params.put("data:3:input", "input3");
        params.put("data:4:input", "input4A");
        params.put("data:5:input", "input5");
        params.put("data:6:input", "input6B");
        params.put("data:7:input", "input7");
        MockExternalContext econtext =
          (MockExternalContext) facesContext.getExternalContext();
        econtext.setRequestParameterMap(params);
        checkMessages(0);

        // Simulate the Request Processing Lifecycle
        TestDataActionListener.trace(null);
        TestDataValidator.trace(null);
        TestDataValueChangeListener.trace(null);
        UIViewRoot root = (UIViewRoot) data.getParent();

        //   APPLY REQUEST VALUES
	command.setImmediate(true);
        root.processDecodes(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("", TestDataValidator.trace());
        assertEquals("", TestDataValueChangeListener.trace());
        checkMessages(0);
        checkSubmittedValues(after);

        //   PERFORM VALIDATIONS
        root.processValidators(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("/data:3:input/input3" +
                     "/data:4:input/input4A" +
                     "/data:5:input/input5" +
                     "/data:6:input/input6B" +
                     "/data:7:input/input7",
                     TestDataValidator.trace());
        assertEquals("/data:4:input/input4/input4A" +
                     "/data:6:input/input6/input6B",
                     TestDataValueChangeListener.trace());
        checkLocalValues(after);
        checkModelInputs(before);
        checkMessages(0);

        //   UPDATE MODEL VALUES
        root.processUpdates(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("/data:3:input/input3" +
                     "/data:4:input/input4A" +
                     "/data:5:input/input5" +
                     "/data:6:input/input6B" +
                     "/data:7:input/input7",
                     TestDataValidator.trace());
        assertEquals("/data:4:input/input4/input4A" +
                     "/data:6:input/input6/input6B",
                     TestDataValueChangeListener.trace());
        checkModelInputs(after);
        checkMessages(0);
        checkLocalValues(nulls);

        //   RENDER RESPONSE
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_3.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_3.xml");

    }

    UIComponent getNamingContainer(UIComponent start) {
        UIComponent namingContainer = start.getParent();
        while (namingContainer != null) {
            if (namingContainer instanceof NamingContainer) {
                return namingContainer;
            }
            namingContainer = namingContainer.getParent();
        }
        return null;
    }


    public void testInvokeOnComponent() throws Exception {

        // Set up for this test
        setupModel();
        setupRenderers();
        UIViewRoot viewRoot = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        viewRoot.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        viewRoot.setViewId("/view");
        facesContext.setViewRoot(viewRoot);
        
        UIForm form1 = new UIForm(), form2 = new UIForm();
        form1.setId("form1");
        form2.setId("form2");
        viewRoot.getChildren().add(form1);
        viewRoot.getChildren().add(form2);
        setupTree2(form1, true, true);
        
        // replace the "component" ivar with a new instance.
        component = new UIData();
        List beans = new ArrayList();
        
        for (int i = 0; i < 10; i++) {
            TestDataBean bean = new TestDataBean();
            bean.setCommand("command" + i);
            bean.setInput("input" + i);
            bean.setOutput("output" + i);
            beans.add(bean);
        }
        DataModel model = new ListDataModel(beans);
        assertEquals(10, model.getRowCount());

        setupModel();
        setupTree2(form2, true, true);
        
        boolean exceptionThrown = false, found = false;
        // At this point we have two forms, each containing a UIData 
        
        UIData data1 = (UIData) viewRoot.findComponent("form1:data");
        UIData data2 = (UIData) viewRoot.findComponent("form2:data");
        assertNotNull(data1);
        assertNotNull(data2);

	// Negative case 0, null pointers
	exceptionThrown = false;
	FacesContext nullContext = null;
	ContextCallback nullCallback = null;
	try {
	    viewRoot.invokeOnComponent(nullContext, "form:input7", 
				   nullCallback);
	}
	catch (NullPointerException npe) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

 	exceptionThrown = false;
	try {
	    viewRoot.invokeOnComponent(facesContext, null, 
				   nullCallback);
	}
	catch (NullPointerException npe) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

 	exceptionThrown = false;
	try {
	    viewRoot.invokeOnComponent(nullContext, null, 
				   nullCallback);
	}
	catch (NullPointerException npe) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

        
        // Case 1, positive invoke on form1:data:commandHeader component of the 4th row
        found = false;
        data1.setRowIndex(3);
        assertEquals(3, data1.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "form1:data:4:commandHeader",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context, 
                                                    UIComponent component) {
                      UIData data = (UIData) getNamingContainer(component);
                      UIForm form = (UIForm) getNamingContainer(data);
                      assertEquals(4, data.getRowIndex());
                      assertEquals("form1", form.getId());
                      assertEquals("commandHeader", component.getId());
                  }
        });
        assertEquals(3, data1.getRowIndex());
        assertTrue(found);
        
        // Case 2, positive invoke on form2:data:commandHeader component of the 5th row
        data2.setRowIndex(4);
        found = false;
        assertEquals(4, data2.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "form2:data:5:commandHeader",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context, 
                                                    UIComponent component) {
                      UIData data = (UIData) getNamingContainer(component);
                      UIForm form = (UIForm) getNamingContainer(data);
                      assertEquals(5, data.getRowIndex());
                      assertEquals("form2", form.getId());
                      assertEquals("commandHeader", component.getId());
                  }
        });
        assertEquals(4, data2.getRowIndex());
        assertTrue(found);
        
        // Case 3, not found invoke on form2:data:5:yoyodyne
        data2.setRowIndex(4);
        found = false;
        assertEquals(4, data2.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "form2:data:5:yoyodyne",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context, 
                                                    UIComponent component) {
                      fail();
                 }
        });
        assertEquals(4, data2.getRowIndex());
        assertTrue(!found);
        
        // Case 4, not found due to invalid rowIndex
        data1.setRowIndex(3);
        found = false;
        assertEquals(3, data1.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "form1:data:999:commandHeader",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context, 
                                                    UIComponent component) {
                      fail();
                 }
        });
        assertEquals(3, data1.getRowIndex());
        assertTrue(!found);
        
        // Case 5, not found due to invalid clientId (too many ':')
        data2.setRowIndex(6);
        found = false;
        exceptionThrown = false;
        assertEquals(6, data2.getRowIndex());
        try {
            found = viewRoot.invokeOnComponent(facesContext, "form2:data::7:commandHeader",
                    new ContextCallback() {

                      public void invokeContextCallback(FacesContext context, 
                                                        UIComponent component) {
                          fail();
                     }
            });
        } 
        catch (FacesException fe) {
            assertTrue(fe.getCause() instanceof NumberFormatException);
            exceptionThrown = true;
        }
        assertEquals(6, data2.getRowIndex());
        assertTrue(!found);
        assertTrue(exceptionThrown);
        
        // Case 6, not found due to callback throwing Exception
        found = false;
        exceptionThrown = true;
        data1.setRowIndex(3);
        assertEquals(3, data1.getRowIndex());
        try {
            found = viewRoot.invokeOnComponent(facesContext, "form1:data:4:commandHeader",
                    new ContextCallback() {

                      public void invokeContextCallback(FacesContext context, 
                                                        UIComponent component) {
                          throw new IllegalStateException();
                      }
            });
        } catch (FacesException ex) {
            assertTrue(ex.getCause() instanceof IllegalStateException);
            exceptionThrown = true;
        }
        assertEquals(3, data1.getRowIndex());
        assertTrue(!found);
        assertTrue(exceptionThrown);

        // Case 7, positive: ensure UIData-level facets are considered
        found = false;
        data1.setRowIndex(3);
        assertEquals(3, data1.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "form1:data:uidataHeader",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context,
                                                    UIComponent component) {
                      UIData data = (UIData) getNamingContainer(component);
                      UIForm form = (UIForm) getNamingContainer(data);
                      assertEquals("form1", form.getId());
                      assertEquals("uidataHeader", component.getId());
                  }
        });
        assertEquals(3, data1.getRowIndex());
        assertTrue(found);

        found = false;
        data1.setRowIndex(3);
        assertEquals(3, data1.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "form1:data:uidataFooter",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context,
                                                    UIComponent component) {
                      UIData data = (UIData) getNamingContainer(component);
                      UIForm form = (UIForm) getNamingContainer(data);
                      assertEquals("form1", form.getId());
                      assertEquals("uidataFooter", component.getId());
                  }
        });
        assertEquals(3, data1.getRowIndex());
        assertTrue(found);


    }


    public void testInvokeOnComponent2() throws Exception {

        setupModel();
        setupRenderers();
        UIViewRoot viewRoot = facesContext.getApplication().getViewHandler().createView(facesContext, "/view");
        viewRoot.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        facesContext.setViewRoot(viewRoot);
        UIForm form = new UIForm();
        form.setId("form");
        viewRoot.getChildren().add(form);
        UIData data = (UIData) component;
        data.setId("data3");
        form.getChildren().add(data);
        UIColumn column = new UIColumn();
        data.getChildren().add(column);
        UIOutput output = new UIOutput();
        output.setId("test4");
        column.getChildren().add(output);

        data.setRowIndex(3);
        assertEquals(3, data.getRowIndex());
        boolean found = viewRoot.invokeOnComponent(facesContext, "form:data3:4:test4",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context,
                                                    UIComponent component) {
                      UIData data = (UIData) getNamingContainer(component);
                      UIForm form = (UIForm) getNamingContainer(data);
                      assertEquals(4, data.getRowIndex());
                      assertEquals("form", form.getId());
                      assertEquals("test4", component.getId());
                  }
        });
        assertEquals(3, data.getRowIndex());
        assertTrue(found);

    }

    /**
     * <p>Test invokeOnComponent on the following tree.</p>
     *
     * <code><pre>
     * id:null
     * type:UIViewRoot
     *
     *   id:outerData
     *   type:UIData
     *
     *     id:outerColumn
     *     type:UIColumn
     *
     *       id:form1
     *       type:UIForm
     *
     *         id:data
     *         type:UIData
     *
     *           id:commandColumn
     *           type:UIColumn
     *
     *             id:commandFooter
     *             type:UIOutput
     *
     *             id:commandHeader
     *             type:UIOutput
     *
     *             id:command
     *             type:UICommand
     *
     *           id:inputColumn
     *           type:UIColumn
     *
     *             id:inputFooter
     *             type:UIOutput
     *
     *             id:inputHeader
     *             type:UIOutput
     *
     *             id:input
     *             type:UIInput
     *
     *           id:outputColumn
     *           type:UIColumn
     *
     *             id:outputFooter
     *             type:UIOutput
     *
     *             id:outputHeader
     *             type:UIOutput
     *
     *             id:output
     *             type:UIOutput
     *
     *           id:constantColumn
     *           type:UIColumn
     *
     *             id:constantFooter
     *             type:UIOutput
     *
     *             id:constantHeader
     *             type:UIOutput
     *
     *             id:constant
     *             type:UIOutput
     *
     *       id:form2
     *       type:UIForm
     *
     *         id:data
     *         type:UIData
     *
     *           id:commandColumn
     *           type:UIColumn
     *
     *             id:commandFooter
     *             type:UIOutput
     *
     *             id:commandHeader
     *             type:UIOutput
     *
     *             id:command
     *             type:UICommand
     *
     *           id:inputColumn
     *           type:UIColumn
     *
     *             id:inputFooter
     *             type:UIOutput
     *
     *             id:inputHeader
     *             type:UIOutput
     *
     *             id:input
     *             type:UIInput
     *
     *           id:outputColumn
     *           type:UIColumn
     *
     *             id:outputFooter
     *             type:UIOutput
     *
     *             id:outputHeader
     *             type:UIOutput
     *
     *             id:output
     *             type:UIOutput
     *
     *           id:constantColumn
     *           type:UIColumn
     *
     *             id:constantFooter
     *             type:UIOutput
     *
     *             id:constantHeader
     *             type:UIOutput
     *
     *             id:constant
     *             type:UIOutput
     *</pre></code>
     *
     *
     */

    public void testInvokeOnComponentNested() throws Exception {

	UIData
	    outer = (UIData) component,
	    inner = new UIData();
	List innerBeans = new ArrayList();
        for (int i = 0; i < 3; i++) {
            TestDataBean bean = new TestDataBean();
            bean.setCommand("innerCommand" + i);
            bean.setInput("innerInput" + i);
            bean.setOutput("innerOutput" + i);
            innerBeans.add(bean);
        }
	DataModel innerDataModel = new ListDataModel(innerBeans);


	// set up the model for the outer table.
	setupModel();

	// set up the tree for the outer data table
        setupRenderers();
        UIViewRoot viewRoot = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        viewRoot.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        viewRoot.setViewId("/view");
        facesContext.setViewRoot(viewRoot);
	outer.setId("outerData");
	viewRoot.getChildren().add(outer);

	UIColumn column = new UIColumn();
	column.setId("outerColumn");
	outer.getChildren().add(column);

        // Set up for this test
        setupModel();

        UIForm form1 = new UIForm(), form2 = new UIForm();
        form1.setId("form1");
        form2.setId("form2");
        column.getChildren().add(form1);
        column.getChildren().add(form2);

        // replace the "component" ivar with a new instance.
        component = new UIData();
        List beans = new ArrayList();

        for (int i = 0; i < 10; i++) {
            TestDataBean bean = new TestDataBean();
            bean.setCommand("command" + i);
            bean.setInput("input" + i);
            bean.setOutput("output" + i);
            beans.add(bean);
        }
        DataModel model = new ListDataModel(beans);
        assertEquals(10, model.getRowCount());

        setupModel();
        setupTree(form1, true, true);

        component = new UIData();
        beans = new ArrayList();

        for (int i = 0; i < 10; i++) {
            TestDataBean bean = new TestDataBean();
            bean.setCommand("command" + i);
            bean.setInput("input" + i);
            bean.setOutput("output" + i);
            beans.add(bean);
        }
        model = new ListDataModel(beans);
        assertEquals(10, model.getRowCount());

        setupModel();
        setupTree(form2, true, true);

        boolean exceptionThrown = false, found = false;
        // At this point we have two forms, each containing a UIData
        UIData outerData = (UIData) viewRoot.findComponent("outerData");
        UIData data1 = (UIData) viewRoot.findComponent("outerData:form1:data");
        UIData data2 = (UIData) viewRoot.findComponent("outerData:form2:data");
        assertNotNull(data1);
        assertNotNull(data2);


        // Case 1, positive invoke on form1:data:commandHeader component of the 4th row
        found = false;
        outerData.setRowIndex(1);
        assertEquals(1, outerData.getRowIndex());
        data1.setRowIndex(3);
        assertEquals(3, data1.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "outerData:2:form1:data:4:commandHeader",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context,
                                                    UIComponent component) {
                      UIData data = (UIData) getNamingContainer(component);
                      UIForm form = (UIForm) getNamingContainer(data);
                      assertEquals(4, data.getRowIndex());
                      assertEquals("form1", form.getId());
                      assertEquals("commandHeader", component.getId());
                  }
        });
        assertEquals(1, outerData.getRowIndex());
        assertEquals(3, data1.getRowIndex());
        assertTrue(found);

        // Case 2, positive invoke on form2:data:commandHeader component of the 5th row
        outerData.setRowIndex(3);
        assertEquals(3, outerData.getRowIndex());
        data2.setRowIndex(4);
        found = false;
        assertEquals(4, data2.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "outerData:1:form2:data:5:commandHeader",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context,
                                                    UIComponent component) {
                      UIData data = (UIData) getNamingContainer(component);
                      UIForm form = (UIForm) getNamingContainer(data);
                      assertEquals(5, data.getRowIndex());
                      assertEquals("form2", form.getId());
                      assertEquals("commandHeader", component.getId());
                  }
        });
        assertEquals(3, outerData.getRowIndex());
        assertEquals(4, data2.getRowIndex());
        assertTrue(found);

        // Case 3, not found invoke on form2:data:5:yoyodyne
        outerData.setRowIndex(1);
        assertEquals(1, outerData.getRowIndex());
        data2.setRowIndex(4);
        found = false;
        assertEquals(4, data2.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "outerData:2:form2:data:5:yoyodyne",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context,
                                                    UIComponent component) {
                      fail();
                 }
        });
        assertEquals(1, outerData.getRowIndex());
        assertEquals(4, data2.getRowIndex());
        assertTrue(!found);

        // Case 4, not found due to invalid rowIndex
        outerData.setRowIndex(1);
        assertEquals(1, outerData.getRowIndex());
        data1.setRowIndex(3);
        found = false;
        assertEquals(3, data1.getRowIndex());
        found = viewRoot.invokeOnComponent(facesContext, "outerData:3:form1:data:999:commandHeader",
                new ContextCallback() {

                  public void invokeContextCallback(FacesContext context,
                                                    UIComponent component) {
                      fail();
                 }
        });
        assertEquals(1, outerData.getRowIndex());
        assertEquals(3, data1.getRowIndex());
        assertTrue(!found);

        // Case 5, not found due to invalid clientId (too many ':')
        outerData.setRowIndex(3);
        assertEquals(3, outerData.getRowIndex());
        data2.setRowIndex(6);
        found = false;
        exceptionThrown = false;
        assertEquals(6, data2.getRowIndex());
        try {
            found = viewRoot.invokeOnComponent(facesContext, "outerData:1:form2:data::7:commandHeader",
                    new ContextCallback() {

                      public void invokeContextCallback(FacesContext context,
                                                        UIComponent component) {
                          fail();
                     }
            });
        }
        catch (FacesException fe) {
            assertTrue(fe.getCause() instanceof NumberFormatException);
            exceptionThrown = true;
        }
        assertEquals(3, outerData.getRowIndex());
        assertEquals(6, data2.getRowIndex());
        assertTrue(!found);
        assertTrue(exceptionThrown);

        // Case 6, not found due to callback throwing Exception
        found = false;
        exceptionThrown = true;
        outerData.setRowIndex(3);
        assertEquals(3, outerData.getRowIndex());
        data1.setRowIndex(3);
        assertEquals(3, data1.getRowIndex());
        try {
            found = viewRoot.invokeOnComponent(facesContext, "outerData:1:form1:data:4:commandHeader",
                    new ContextCallback() {

                      public void invokeContextCallback(FacesContext context,
                                                        UIComponent component) {
                          throw new IllegalStateException();
                      }
            });
        } catch (FacesException ex) {
            assertTrue(ex.getCause() instanceof IllegalStateException);
            exceptionThrown = true;
        }
        assertEquals(3, outerData.getRowIndex());
        assertEquals(3, data1.getRowIndex());
        assertTrue(!found);
        assertTrue(exceptionThrown);


    }

    // Test request processing lifecycle (with controls in header facets)
    public void testTreeLifecycleFacets() throws Exception {

        ValueBinding vbCommand = application.createValueBinding("foo.command");
        ValueBinding vbInput = application.createValueBinding("foo.input");
        ValueBinding vbOutput = application.createValueBinding("foo.output");
        String before[] =
            { "input3", "input4", "input5", "input6", "input7" };
        String after[] =
            { "input3", "input4A", "input5", "input6B", "input7" };
        String nulls[] =
            { null, null, null, null, null };

	// Instantiate and store a bean used to count calls
	UIDataHeaderBean hb = new UIDataHeaderBean();
	facesContext.getExternalContext().getRequestMap().
	    put("hb", hb);

        // Set up for this test
        setupModel();
        setupRenderers();
        UICommand command = setupTree(false); // command and input in headers
        UIData data = (UIData) component;
        checkLocalValues(nulls);

        // Set up our fake request parameters (three command invocations)
        Map params = new HashMap();
	params.put("data:hcommand", "");
        params.put("data:5:command", "");
        params.put("data:7:command", "");
	params.put("data:hinput", "New Value");
        params.put("data:3:input", "input3");
        params.put("data:4:input", "input4A");
        params.put("data:5:input", "input5");
        params.put("data:6:input", "input6B");
        params.put("data:7:input", "input7");
        MockExternalContext econtext =
          (MockExternalContext) facesContext.getExternalContext();
        econtext.setRequestParameterMap(params);
        checkMessages(0);

        // Simulate the Request Processing Lifecycle
        TestDataActionListener.trace(null);
        TestDataValidator.trace(null);
        TestDataValueChangeListener.trace(null);
        UIViewRoot root = (UIViewRoot) data.getParent();

        //   APPLY REQUEST VALUES
	command.setImmediate(true);
        root.processDecodes(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("", TestDataValidator.trace());
        assertEquals("", TestDataValueChangeListener.trace());
        checkMessages(0);
        checkSubmittedValues(after);

        //   PERFORM VALIDATIONS
        root.processValidators(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("/data:3:input/input3" +
                     "/data:4:input/input4A" +
                     "/data:5:input/input5" +
                     "/data:6:input/input6B" +
                     "/data:7:input/input7",
                     TestDataValidator.trace());
        assertEquals("/data:4:input/input4/input4A" +
                     "/data:6:input/input6/input6B",
                     TestDataValueChangeListener.trace());
        checkLocalValues(after);
        checkModelInputs(before);
        checkMessages(0);

        //   UPDATE MODEL VALUES
        root.processUpdates(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("/data:3:input/input3" +
                     "/data:4:input/input4A" +
                     "/data:5:input/input5" +
                     "/data:6:input/input6B" +
                     "/data:7:input/input7",
                     TestDataValidator.trace());
        assertEquals("/data:4:input/input4/input4A" +
                     "/data:6:input/input6/input6B",
                     TestDataValueChangeListener.trace());
        checkModelInputs(after);
        checkMessages(0);
        checkLocalValues(nulls);
	assertEquals("Header input property updated",
		     "New Value", hb.getValue());

        //   RENDER RESPONSE
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_8.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_8.xml");

	// Check call counts
	assertEquals("header action called once", 1, hb.getActionCount());
	assertEquals("header validate called once", 1, hb.getValidateCount());
	assertEquals("header update called once", 1, hb.getUpdateCount());

    }


    // Test request processing lifecycle (modify model in Invoke Application)
    public void testTreeModify1() throws Exception {

        ValueBinding vbCommand = application.createValueBinding("foo.command");
        ValueBinding vbInput = application.createValueBinding("foo.input");
        ValueBinding vbOutput = application.createValueBinding("foo.output");
        String before[] =
            { "input3", "input4", "input5", "input6", "input7" };
        String after[] =
            { "input3", "input4A", "input5", "input6B", "input7" };
        String nulls[] =
            { null, null, null, null, null };

        // Set up for this test
        setupModel();
        setupRenderers();
        UICommand command = setupTree(true);
        UIData data = (UIData) component;
        checkLocalValues(nulls);

        // Set up our fake request parameters (two command invocations)
        Map params = new HashMap();
        params.put("data:5:command", "");
        params.put("data:7:command", "");
        params.put("data:3:input", "input3");
        params.put("data:4:input", "input4A");
        params.put("data:5:input", "input5");
        params.put("data:6:input", "input6B");
        params.put("data:7:input", "input7");
        MockExternalContext econtext =
          (MockExternalContext) facesContext.getExternalContext();
        econtext.setRequestParameterMap(params);
        checkMessages(0);

        // Simulate the Request Processing Lifecycle
        TestDataActionListener.trace(null);
        TestDataValidator.trace(null);
        TestDataValueChangeListener.trace(null);
        UIViewRoot root = (UIViewRoot) data.getParent();

        //   APPLY REQUEST VALUES
	command.setImmediate(true);
        root.processDecodes(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("", TestDataValidator.trace());
        assertEquals("", TestDataValueChangeListener.trace());
        checkMessages(0);
        checkSubmittedValues(after);

        //   PERFORM VALIDATIONS
        root.processValidators(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("/data:3:input/input3" +
                     "/data:4:input/input4A" +
                     "/data:5:input/input5" +
                     "/data:6:input/input6B" +
                     "/data:7:input/input7",
                     TestDataValidator.trace());
        assertEquals("/data:4:input/input4/input4A" +
                     "/data:6:input/input6/input6B",
                     TestDataValueChangeListener.trace());
        checkLocalValues(after);
        checkModelInputs(before);
        checkMessages(0);

        //   UPDATE MODEL VALUES
        root.processUpdates(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("/data:3:input/input3" +
                     "/data:4:input/input4A" +
                     "/data:5:input/input5" +
                     "/data:6:input/input6B" +
                     "/data:7:input/input7",
                     TestDataValidator.trace());
        assertEquals("/data:4:input/input4/input4A" +
                     "/data:6:input/input6/input6B",
                     TestDataValueChangeListener.trace());
        checkModelInputs(after);
        checkMessages(0);
        checkLocalValues(nulls);

        // INVOKE APPLICATION
        beans.remove(5);

        //   RENDER RESPONSE
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_6.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_6.xml");

    }


    // Test request processing lifecycle (modify model in Apply Request Values)
    public void testTreeModify2() throws Exception {

        ValueBinding vbCommand = application.createValueBinding("foo.command");
        ValueBinding vbInput = application.createValueBinding("foo.input");
        ValueBinding vbOutput = application.createValueBinding("foo.output");
        String before[] =
            { "input3", "input4", "input5", "input6", "input7" };
        String after[] =
            { "input3", "input4A", "input5", "input6B", "input7" };
        String nulls[] =
            { null, null, null, null, null };

        // Set up for this test
        setupModel();
        setupRenderers();
        UICommand command = setupTree(true);
        UIData data = (UIData) component;
        checkLocalValues(nulls);

        // Set up our fake request parameters (two command invocations)
        Map params = new HashMap();
        params.put("data:5:command", "");
        params.put("data:7:command", "");
        params.put("data:3:input", "input3");
        params.put("data:4:input", "input4A");
        params.put("data:5:input", "input5");
        params.put("data:6:input", "input6B");
        params.put("data:7:input", "input7");
        MockExternalContext econtext =
          (MockExternalContext) facesContext.getExternalContext();
        econtext.setRequestParameterMap(params);
        checkMessages(0);

        // Simulate the Request Processing Lifecycle
        TestDataActionListener.trace(null);
        TestDataValidator.trace(null);
        TestDataValueChangeListener.trace(null);
        UIViewRoot root = (UIViewRoot) data.getParent();

        //   APPLY REQUEST VALUES
	command.setImmediate(true);
        root.processDecodes(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("", TestDataValidator.trace());
        assertEquals("", TestDataValueChangeListener.trace());
        checkMessages(0);
        checkSubmittedValues(after);

        beans.remove(5); // Simulate model modification in immediate=true action

        //   PERFORM VALIDATIONS - skipped

        //   UPDATE MODEL VALUES - skipped

        // INVOKE APPLICATION - skipped

        //   RENDER RESPONSE
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_7.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_7.xml");

    }


    // Test rendering the tree and validate the output twice
    public void testTreeRendering() throws Exception {

        // Set up for this test
        setupModel();
        setupRenderers();
        setupTree(true);

        // Validate the rendered output
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_1.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_1.xml");

    }


    // Test rendering the tree when there are not enough available rows
    public void testTreeTail() throws Exception {

        // Set up for this test
        setupModel();
        setupRenderers();
        setupTree(true);

        // Validate the rendered output
        ((UIData) component).setFirst(7);
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_5.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_5.xml");

    }


    // Test updating the tree's per-row values and validate the output twice
    public void testTreeUpdating() throws Exception {

        ValueBinding vbCommand = application.createValueBinding("foo.command");
        ValueBinding vbInput = application.createValueBinding("foo.input");
        ValueBinding vbOutput = application.createValueBinding("foo.output");

        // Set up for this test
        setupModel();
        setupRenderers();
        setupTree(true);
        UIData data = (UIData) component;

        // Use value references to update certain values directly
        data.setRowIndex(4);
        vbCommand.setValue(facesContext, "command4A");
        vbInput.setValue(facesContext, "input4B");
        vbOutput.setValue(facesContext, "output4C");
        data.setRowIndex(6);
        vbCommand.setValue(facesContext, "command6D");
        vbInput.setValue(facesContext, "input6E");
        vbOutput.setValue(facesContext, "output6F");
        data.setRowIndex(-1);

        // Validate the response (twice)
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_2.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_2.xml");

    }


    // Test request processing lifecycle (validation errors)
    public void testTreeValidation() throws Exception {

        ValueBinding vbCommand = application.createValueBinding("foo.command");
        ValueBinding vbInput = application.createValueBinding("foo.input");
        ValueBinding vbOutput = application.createValueBinding("foo.output");
        String before[] =
            { "input3", "input4", "input5", "input6", "input7" };

        // Set up for this test
        setupModel();
        setupRenderers();
        setupTree(true);
        UIData data = (UIData) component;

        // Set up our fake request parameters (no command invocations)
        Map params = new HashMap();
        params.put("data:3:input", "input3A");
        params.put("data:4:input", "bad");
        params.put("data:5:input", "input5");
        params.put("data:6:input", "bad");
        params.put("data:7:input", "input7B");
        MockExternalContext econtext =
          (MockExternalContext) facesContext.getExternalContext();
        econtext.setRequestParameterMap(params);

        // Simulate the Request Processing Lifecycle
        TestDataActionListener.trace(null);
        TestDataValidator.trace(null);
        TestDataValueChangeListener.trace(null);
        UIViewRoot root = (UIViewRoot) data.getParent();

        //   APPLY REQUEST VALUES
        root.processDecodes(facesContext);
        assertEquals("", TestDataActionListener.trace());
        assertEquals("", TestDataValidator.trace());
        assertEquals("", TestDataValueChangeListener.trace());
        checkMessages(0);

        //   PERFORM VALIDATIONS
        root.processValidators(facesContext);
        assertEquals("", TestDataActionListener.trace());
        assertEquals("/data:3:input/input3A" +
                     "/data:4:input/bad/ERROR" +
                     "/data:5:input/input5" +
                     "/data:6:input/bad/ERROR" +
                     "/data:7:input/input7B",
                     TestDataValidator.trace());
        assertEquals("/data:3:input/input3/input3A" +
                     "/data:7:input/input7/input7B",
                     TestDataValueChangeListener.trace());
        checkModelInputs(before);
        checkMessages(2);

        //   UPDATE MODEL VALUES - skipped due to validation errors

        //   RENDER RESPONSE (twice)
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_4.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_4.xml");

    }

    public void testNestedTablesWithIds() throws Exception {
	UIData
	    outer = (UIData) component,
	    inner = new UIData();
	List innerBeans = new ArrayList();
        for (int i = 0; i < 3; i++) {
            TestDataBean bean = new TestDataBean();
            bean.setCommand("innerCommand" + i);
            bean.setInput("innerInput" + i);
            bean.setOutput("innerOutput" + i);
            innerBeans.add(bean);
        }
	DataModel innerDataModel = new ListDataModel(innerBeans);

	// set up the model for the outer table.
	setupModel();

	// set up the tree for the outer data table
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
	root.setViewId("/view");
	facesContext.setViewRoot(root);
	outer.setId("outerData");
	root.getChildren().add(outer);

	UIColumn column = new UIColumn();
	column.setId("outerColumn");
	outer.getChildren().add(column);


	component = inner;
	setupModel();
	setupTree(column, true, true);

	UIDataHeaderBean hb = new UIDataHeaderBean();
	facesContext.getExternalContext().getRequestMap().
	    put("hb", hb);

	HashMap foo = new HashMap();
	foo.put("input", "input");
	foo.put("output", "output");
	foo.put("component", "component");
	request.setAttribute("foo", foo);
	request.removeAttribute("foo");
        setupRenderers();

	renderResponse();
	checkResponse("/javax/faces/component/UIDataTestCase_9_withIds.xml");
    }

    public void testNestedTablesWithoutIds() throws Exception {
	UIData
	    outer = (UIData) component,
	    inner = new UIData();
	List innerBeans = new ArrayList();
        for (int i = 0; i < 3; i++) {
            TestDataBean bean = new TestDataBean();
            bean.setCommand("innerCommand" + i);
            bean.setInput("innerInput" + i);
            bean.setOutput("innerOutput" + i);
            innerBeans.add(bean);
        }
	DataModel innerDataModel = new ListDataModel(innerBeans);

	// set up the model for the outer table.
	setupModel();

	// set up the tree for the outer data table
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
	root.setViewId("/view");
	facesContext.setViewRoot(root);
	root.getChildren().add(outer);

	UIColumn column = new UIColumn();
	outer.getChildren().add(column);


	component = inner;
	setupModel();
	setupTree(column, true, false);

	UIDataHeaderBean hb = new UIDataHeaderBean();
	facesContext.getExternalContext().getRequestMap().
	    put("hb", hb);

	HashMap foo = new HashMap();
	foo.put("input", "input");
	foo.put("output", "output");
	foo.put("component", "component");
	request.setAttribute("foo", foo);
	request.removeAttribute("foo");
        setupRenderers();

	renderResponse();
	checkResponse("/javax/faces/component/UIDataTestCase_9_withoutIds.xml");
    }


    public void PENDING_FIXME_testValueBindings() {

	super.PENDING_FIXME_testValueBindings();
	UIData test = (UIData) component;

	// "first" property
	request.setAttribute("foo", new Integer(5));
	test.setValueBinding("first", application.createValueBinding("#{foo}"));
	assertEquals(5, test.getFirst());
	test.setFirst(10);
	assertEquals(10, test.getFirst());
	assertNotNull(test.getValueBinding("first"));

        // "rowIndex" property
        try {
            request.setAttribute("foo", new Integer(5));
            test.setValueBinding("rowIndex",
                                 application.createValueBinding("#{foo}"));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
        request.removeAttribute("foo");

	// "rows" property
	request.setAttribute("foo", new Integer(5));
	test.setValueBinding("rows", application.createValueBinding("#{foo}"));
	assertEquals(5, test.getRows());
	test.setRows(10);
	assertEquals(10, test.getRows());
	assertNotNull(test.getValueBinding("rows"));

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

        // "var" property
        try {
            request.setAttribute("foo", "bar");
            test.setValueBinding("var",
                                 application.createValueBinding("#{foo}"));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
        request.removeAttribute("foo");

    }


    // --------------------------------------------------------- Support Methods


    // Check that the per-row local values of the input component are correct
    protected void checkLocalValues(String values[]) {

        UIData data = (UIData) component;
        int first = data.getFirst();
        for (int i = 0; i < values.length; i++) {
            data.setRowIndex(i + first);
            assertTrue("Row " + (i + first) + " available",
                       data.isRowAvailable());
            UIInput input = (UIInput) data.findComponent("input");
            assertNotNull("Row " + (i + first) + " input exists", input);
            assertEquals("Row " + (i + first) + " input clientId",
                         "data:" + (i + first) + ":input",
                         input.getClientId(facesContext));
            assertEquals("Row " + (i + first) + " input localValue",
                         values[i],
                         (String) input.getLocalValue());
        }
        data.setRowIndex(-1);

    }

    // Check that the per-row submitted values of the input component are correct
    protected void checkSubmittedValues(String values[]) {

        UIData data = (UIData) component;
        int first = data.getFirst();
        for (int i = 0; i < values.length; i++) {
            data.setRowIndex(i + first);
            assertTrue("Row " + (i + first) + " available",
                       data.isRowAvailable());
            UIInput input = (UIInput) data.findComponent("input");
            assertNotNull("Row " + (i + first) + " input exists", input);
            assertEquals("Row " + (i + first) + " input clientId",
                         "data:" + (i + first) + ":input",
                         input.getClientId(facesContext));
            assertEquals("Row " + (i + first) + " input submittedValue",
                         values[i],
                         (String) input.getSubmittedValue());
        }
        data.setRowIndex(-1);

    }


    // Check that the number of queued messages equals the expected count
    // and that eacdh of them is of severity ERROR
    protected void checkMessages(int expected) {

        int n = 0;
        Iterator messages = facesContext.getMessages();
        while (messages.hasNext()) {
            FacesMessage message = (FacesMessage) messages.next();
            assertEquals("Severity == ERROR",
                         FacesMessage.SEVERITY_ERROR,
                         message.getSeverity());
            n++;
        }
        assertEquals("expected message count", expected, n);

    }


    // Check the current values of the model objects being rendered
    protected void checkModelInputs(String values[]) {

        for (int i = 0; i < values.length; i++) {
            TestDataBean bean = (TestDataBean) beans.get(i + 3);
            assertEquals("correct input value", values[i], bean.getInput());
        }

    }


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {

        super.checkProperties(comp1, comp2);
        UIData d1 = (UIData) comp1;
        UIData d2 = (UIData) comp2;
        assertEquals(d1.getFirst(), d2.getFirst());
        assertEquals(d1.getRows(), d2.getRows());
        assertEquals(d1.getVar(), d2.getVar());

    }


    // Check the rendered response against the specified golden resource
    protected void checkResponse(String resource) throws Exception {

        InputStream stream = this.getClass().getResourceAsStream(resource);
        Reader reader = new InputStreamReader(stream);
        String response = swriter.getBuffer().toString();
        int chars = 0;
        boolean eof = false;
        boolean errors = false;
        int lines = 0;
        while (true) {

            lines++;

            // Read the next line from the response
            StringBuffer actual = new StringBuffer();
            while (true) {
                if (chars >= response.length()) {
                    eof = true;
                    break;
                }
                char ch = response.charAt(chars++);
                if (ch == '<') {
                    actual.append(ch);
                    break;
                }
            }
            if (eof) {
                break;
            }
            while (true) {
                if (chars >= response.length()) {
                    eof = true;
                    break;
                }
                char ch = response.charAt(chars++);
                actual.append(ch);
                if (ch == '>') {
                    break;
                }
            }
            if (eof) {
                break;
            }

            // Read the corresponding line from the resource
            StringBuffer expect = new StringBuffer();
            while (true) {
                int ch = reader.read();
                if (ch < 0) {
                    eof = true;
                    break;
                }
                if ((char) ch == '<') {
                    expect.append((char) ch);
                    break;
                }
            }
            if (eof) {
                break;
            }
            while (true) {
                int ch = reader.read();
                if (ch < 0) {
                    eof = true;
                    break;
                }
                expect.append((char) ch);
                if ((char) ch == '>') {
                    break;
                }
            }
            if (eof) {
                break;
            }

            // Compare the expected and actual lines
            if (!expect.toString().equals(actual.toString())) {
                errors = true;
                System.err.println("EXP(" + lines + "): " + expect.toString());
                System.err.println("ACT(" + lines + "): " + actual.toString());
            }

        }

        reader.close();
        assertTrue("no golden resource mismatches", !errors);

    }


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIData();
        component.setRendererType(null);
        return (component);
    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {
        super.populateComponent(component);
        UIData d = (UIData) component;
        d.setFirst(5);
        d.setRows(10);
        d.setVar("foo");
    }


    // Render the entire response tree
    private void renderResponse() throws IOException {
        swriter.getBuffer().setLength(0);
        renderResponse(facesContext.getViewRoot());
    }


    // Render the response tree recursively
    private void renderResponse(UIComponent component) throws IOException {
        component.encodeBegin(facesContext);
        if (component.getRendersChildren()) {
            component.encodeChildren(facesContext);
        } else {
            Iterator kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                renderResponse((UIComponent) kids.next());
            }
        }
        component.encodeEnd(facesContext);
    }

    // Set up the UIData properties for an associated DataModel
    protected void setupModel() throws Exception {
        UIData data = (UIData) component;
        data.setFirst(3);
        data.setRows(5);
        data.setVar("foo");
        data.setValue(model);
        assertNotNull(data.getValue());
        assertTrue(model == data.getValue());
    }


    // Set up dummy renderers
    protected void setupRenderers() throws Exception {

        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit =
            renderKitFactory.getRenderKit(facesContext,
					  RenderKitFactory.HTML_BASIC_RENDER_KIT);
        renderKit.addRenderer(UICommand.COMPONENT_FAMILY,
			      "javax.faces.Button", new ButtonRenderer());
        renderKit.addRenderer(UIData.COMPONENT_FAMILY,
			      "javax.faces.Table", new TableRenderer());
        renderKit.addRenderer(UIInput.COMPONENT_FAMILY,
			      "javax.faces.Text", new TextRenderer());
        renderKit.addRenderer(UIOutput.COMPONENT_FAMILY,
			      "javax.faces.Text", new TextRenderer());

    }

    /* If labels == true the tree looks like this

     * root: viewId: /view
     *
     *  data: id: data
     *
     *    column: id: commandColumn
     *
     *      facets
     *
     *	      header: id: commandHeader
     *
     *	      footer: id: commandFooter
     *
     *      command: id: command value: #{foo.command}
     *               actionListener: TestDataActionListener()
     *
     *    column: id: inputColumn
     *
     *      facets:
     *
     *        header: id: inputHeader
     *
     *        footer: id: inputFooter
     *
     *      input: id: input value: #{foo.input} validator: TestDataValidator
     *             valueChangeListener: TestValueChangeListener:
     *
     *    column: id: outputColumn
     *
     *      facets:
     *
     *        header: id: outputHeader
     *
     *        header: id: inputHeader
     *
     *       output: id: output: value: #{foo.output}
     *
     *    column: id: constantColumn
     *
     *      facets:
     *
     *        header: id: outputHeader
     *
     *        header: id: inputHeader
     *
     *      constant: id: constant: value: Constant Value
     *
     * If labels == false, the tree looks like
     * root: viewId: /view
     *
     *   data: id: data
     *
     *     column: id commandColumn
     *
     *       facets:
     *
     *         header: UICommand id: commandAction actionListener: #{hb.action}
     *                 value: Command Action
     *
     *         footer: UIOutput id: commandFooter
     *
     *       command: id: command value: #{foo.command}
     *                actionListener: TestDataActionListener()
     *
     *     column: id inputColumn
     *
     *       facets:
     *
     *         header: UIInput id: hinput validator: #{hb.validate}
     *
     *                 value: #{hb.value}
     *
     *         footer: UIOutput id: inputFooter
     *
     *       input: id: input value: #{foo.input} validator: TestDataValidator
     *              valueChangeListener: TestValueChangeListener:
     *
     *     column: id: outputColumn
     *
     *       facets:
     *
     *         header: id: outputHeader
     *
     *         header: id: inputHeader
     *
     *       output: id: output: value: #{foo.output}
     *
     *    column: id: constantColumn
     *
     *      facets:
     *
     *        header: id: outputHeader
     *
     *        header: id: inputHeader
     *
     *      constant: id: constant: value: Constant Value
     *
     *
     *
     */

    // Set up the component tree corresponding to the data model
    // labels==true - header facet of command and input contain labels
    // labels==false - header facet of command and input contain controls
    protected UICommand setupTree(boolean labels) throws Exception {
	return setupTree(null, labels, true);
    }

    // Set up the component tree corresponding to the data model
    // labels==true - header facet of command and input contain labels
    // labels==false - header facet of command and input contain controls
    // ids==true hard coded ids
    // ids==false no ids
    protected UICommand setupTree(UIComponent root, boolean labels, boolean ids) throws Exception {

        // Attach our UIData to the view root
        UIData data = (UIData) component;
	if (ids) {
	    data.setId("data");
	}
	if (null == root) {
	    UIViewRoot viewRoot = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	    viewRoot.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
	    viewRoot.setViewId("/view");
	    facesContext.setViewRoot(viewRoot);
	    root = viewRoot;
	}
        root.getChildren().add(data);

        // Set up columns with facets and fields for each property
        UIColumn column;
        UICommand command;
        UIInput input;
        UIOutput output;
        UIOutput label;
        UIOutput constant;
	UICommand hcommand;
	UIInput hinput;

        column = new UIColumn();
	if (ids) {
	    column.setId("commandColumn");
	}
	if (labels) {
	    label = new UIOutput();
	    if (ids) {
		label.setId("commandHeader");
	    }
	    label.setValue("Command Header");
	    column.getFacets().put("header", label);
	} else {
	    hcommand = new UICommand();
	    if (ids) {
		hcommand.setId("hcommand");
	    }
	    hcommand.setImmediate(true);
	    hcommand.setActionListener
		(application.createMethodBinding
		 ("#{hb.action}",
		  new Class[] { ActionEvent.class }));
	    hcommand.setValue("Command Action");
	    column.getFacets().put("header", hcommand);
	}
        label = new UIOutput();
	if (ids) {
	    label.setId("commandFooter");
	}
        label.setValue("Command Footer");
        column.getFacets().put("footer", label);
        command = new UICommand();
	if (ids) {
	    command.setId("command");
	}
        command.setValueBinding("value",
				application.createValueBinding("#{foo.command}"));
        column.getChildren().add(command);
        data.getChildren().add(column);
        command.addActionListener(new TestDataActionListener());

        column = new UIColumn();
	if (ids) {
	    column.setId("inputColumn");
	}
	if (labels) {
	    label = new UIOutput();
	    if (ids) {
		label.setId("inputHeader");
	    }
	    label.setValue("Input Header");
	    column.getFacets().put("header", label);
	} else {
	    hinput = new UIInput();
	    if (ids) {
		hinput.setId("hinput");
	    }
	    hinput.setValidator
		(application.createMethodBinding
		 ("#{hb.validate}",
		  new Class[] { FacesContext.class,
				UIComponent.class,
				Object.class }));
	    hinput.setValueBinding
		("value",
		 application.createValueBinding("#{hb.value}"));
	    column.getFacets().put("header", hinput);
	}
        label = new UIOutput();
	if (ids) {
	    label.setId("inputFooter");
	}
        label.setValue("Input Footer");
        column.getFacets().put("footer", label);
        input = new UIInput();
	if (ids) {
	    input.setId("input");
	}
        input.setValueBinding("value",
			      application.createValueBinding("#{foo.input}"));
        column.getChildren().add(input);
        data.getChildren().add(column);
        input.addValidator(new TestDataValidator());
        input.addValueChangeListener(new TestDataValueChangeListener());

        column = new UIColumn();
	if (ids) {
	    column.setId("outputColumn");
	}
        label = new UIOutput();
	if (ids) {
	    label.setId("outputHeader");
	}
        label.setValue("Output Header");
        column.getFacets().put("header", label);
        label = new UIOutput();
	if (ids) {
	    label.setId("outputFooter");
	}
        label.setValue("Output Footer");
        column.getFacets().put("footer", label);
        output = new UIOutput();
	if (ids) {
	    output.setId("output");
	}
        output.setValueBinding("value",
			       application.createValueBinding("#{foo.output}"));
        column.getChildren().add(output);
        data.getChildren().add(column);

        column = new UIColumn();
	if (ids) {
	    column.setId("constantColumn");
	}
        label = new UIOutput();
	if (ids) {
	    label.setId("constantHeader");
	}
        label.setValue("Constant Header");
        column.getFacets().put("header", label);
        label = new UIOutput();
	if (ids) {
	    label.setId("constantFooter");
	}
        label.setValue("Constant Footer");
        column.getFacets().put("footer", label);
        constant = new UIOutput();
	if (ids) {
	    constant.setId("constant");
	}
        constant.setValue("Constant Value");
        column.getChildren().add(constant);
        data.getChildren().add(column);

	return command;

    }

    // Set up the component tree corresponding to the data model
    // labels==true - header facet of command and input contain labels
    // labels==false - header facet of command and input contain controls
    // ids==true hard coded ids
    // ids==false no ids
    protected UICommand setupTree2(UIComponent root,
                                   boolean labels,
                                   boolean ids) throws Exception {

        // Attach our UIData to the view root
        UIData data = (UIData) component;
        if (ids) {
            data.setId("data");
        }
        if (null == root) {
            UIViewRoot viewRoot = facesContext.getApplication().getViewHandler()
                  .createView(facesContext, null);
            viewRoot.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
            viewRoot.setViewId("/view");
            facesContext.setViewRoot(viewRoot);
            root = viewRoot;
        }
        root.getChildren().add(data);

        // setup a UIData-level header and footer
        UIOutput header = new UIOutput();
        header.setId("uidataHeader");
        data.getFacets().put("header", header);
        header.getClientId(facesContext);
        UIOutput footer = new UIOutput();
        footer.setId("uidataFooter");
        data.getFacets().put("footer", footer);
        footer.getClientId(facesContext);

        // Set up columns with facets and fields for each property
        UIColumn column;
        UICommand command;
        UIInput input;
        UIOutput output;
        UIOutput label;
        UIOutput constant;
        UICommand hcommand;
        UIInput hinput;

        column = new UIColumn();
        if (ids) {
            column.setId("commandColumn");
        }
        if (labels) {
            label = new UIOutput();
            if (ids) {
                label.setId("commandHeader");
            }
            label.setValue("Command Header");
            column.getFacets().put("header", label);
        } else {
            hcommand = new UICommand();
            if (ids) {
                hcommand.setId("hcommand");
            }
            hcommand.setImmediate(true);
            hcommand.setActionListener
                  (application.createMethodBinding
                        ("#{hb.action}",
                         new Class[]{ActionEvent.class}));
            hcommand.setValue("Command Action");
            column.getFacets().put("header", hcommand);
        }
        label = new UIOutput();
        if (ids) {
            label.setId("commandFooter");
        }
        label.setValue("Command Footer");
        column.getFacets().put("footer", label);
        command = new UICommand();
        if (ids) {
            command.setId("command");
        }
        command.setValueBinding("value",
                                application.createValueBinding("#{foo.command}"));
        column.getChildren().add(command);
        data.getChildren().add(column);
        command.addActionListener(new TestDataActionListener());

        column = new UIColumn();
        if (ids) {
            column.setId("inputColumn");
        }
        if (labels) {
            label = new UIOutput();
            if (ids) {
                label.setId("inputHeader");
            }
            label.setValue("Input Header");
            column.getFacets().put("header", label);
        } else {
            hinput = new UIInput();
            if (ids) {
                hinput.setId("hinput");
            }
            hinput.setValidator
                  (application.createMethodBinding
                        ("#{hb.validate}",
                         new Class[]{FacesContext.class,
                                     UIComponent.class,
                                     Object.class}));
            hinput.setValueBinding
                  ("value",
                   application.createValueBinding("#{hb.value}"));
            column.getFacets().put("header", hinput);
        }
        label = new UIOutput();
        if (ids) {
            label.setId("inputFooter");
        }
        label.setValue("Input Footer");
        column.getFacets().put("footer", label);
        input = new UIInput();
        if (ids) {
            input.setId("input");
        }
        input.setValueBinding("value",
                              application.createValueBinding("#{foo.input}"));
        column.getChildren().add(input);
        data.getChildren().add(column);
        input.addValidator(new TestDataValidator());
        input.addValueChangeListener(new TestDataValueChangeListener());

        column = new UIColumn();
        if (ids) {
            column.setId("outputColumn");
        }
        label = new UIOutput();
        if (ids) {
            label.setId("outputHeader");
        }
        label.setValue("Output Header");
        column.getFacets().put("header", label);
        label = new UIOutput();
        if (ids) {
            label.setId("outputFooter");
        }
        label.setValue("Output Footer");
        column.getFacets().put("footer", label);
        output = new UIOutput();
        if (ids) {
            output.setId("output");
        }
        output.setValueBinding("value",
                               application.createValueBinding("#{foo.output}"));
        column.getChildren().add(output);
        data.getChildren().add(column);

        column = new UIColumn();
        if (ids) {
            column.setId("constantColumn");
        }
        label = new UIOutput();
        if (ids) {
            label.setId("constantHeader");
        }
        label.setValue("Constant Header");
        column.getFacets().put("header", label);
        label = new UIOutput();
        if (ids) {
            label.setId("constantFooter");
        }
        label.setValue("Constant Footer");
        column.getFacets().put("footer", label);
        constant = new UIOutput();
        if (ids) {
            constant.setId("constant");
        }
        constant.setValue("Constant Value");
        column.getChildren().add(constant);
        data.getChildren().add(column);

        return command;

    }



    // --------------------------------------------------------- Private Classes


    // "Button" Renderer
    class ButtonRenderer extends Renderer {

        public void decode(FacesContext context, UIComponent component) {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

            if (!(component instanceof ActionSource)) {
                return;
            }
            String clientId = component.getClientId(context);
            Map params = context.getExternalContext().getRequestParameterMap();
            if (params.containsKey(clientId)) {
                component.queueEvent(new ActionEvent(component));
            }

        }

        public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }
            ResponseWriter writer = context.getResponseWriter();
            writer.write
                ("<button id='" + component.getClientId(context) + "' value='" +
                 ((UICommand) component).getValue() + "'/>\n");

        }

        public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

        public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

    }


    // "Table" Renderer
    class TableRenderer extends Renderer {

	public boolean getRendersChildren() { return true; }

        public void decode(FacesContext context, UIComponent component) {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

        public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }
            UIData data = (UIData) component;
            ResponseWriter writer = context.getResponseWriter();

            // Render the beginning of the table
            data.setRowIndex(-1);
            String tableId = data.getClientId(context);
            writer.write
                ("<table id='" + tableId + "'>\n");
            UIComponent header = (UIComponent) data.getFacet("header");
            if (header != null) {
                writer.write
                    ("<table-header id='" + header.getClientId(context) +
                     "'>\n");
                encodeRecursive(context, header);
                writer.write("</table-header>\n");
            }

            // Render the column headers


        }

        public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }
            UIData data = (UIData) component;
            ResponseWriter writer = context.getResponseWriter();
            Iterator kids;

            // Render the column headers
            kids = data.getChildren().iterator();
            data.setRowIndex(-1);
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                if (!(kid instanceof UIColumn)) {
                    continue;
                }
                UIColumn column = (UIColumn) kid;
                UIComponent header = column.getFacet("header");
                if (header != null) {
                    String headerClientId = header.getClientId(context);
                    writer.write("<column-header id='" +
                                 headerClientId + "'>\n");
                    encodeRecursive(context, header);
                    writer.write("</column-header>\n");
                }
            }

            // Render the individual rows
            int done = 0;
            int rows = data.getRows();
            int rowId = data.getFirst() - 1;
            while (true) {
                if ((rows > 0) && (++done > rows)) {
                    break;
                }
		data.setRowIndex(++rowId);
		if (!data.isRowAvailable()) {
		    break;
		}
                writer.write("<table-row rowId='" + rowId + "'>\n");
                kids = data.getChildren().iterator();
                while (kids.hasNext()) {
                    UIComponent kid = (UIComponent) kids.next();
                    if (!(kid instanceof UIColumn)) {
                        continue;
                    }
                    UIColumn column = (UIColumn) kid;
                    String columnClientId = column.getClientId(context);
                    writer.write("<column id='" + columnClientId +
                                 "'>\n");
                    encodeRecursive(context, column);
                    writer.write("</column>\n");
                }
                writer.write("</table-row>\n");
            }

            // Render the column footers
            kids = data.getChildren().iterator();
            data.setRowIndex(-1);
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                if (!(kid instanceof UIColumn)) {
                    continue;
                }
                UIColumn column = (UIColumn) kid;
                UIComponent footer = column.getFacet("footer");
                if (footer != null) {
                    String footerClientId = footer.getClientId(context);
                    writer.write("<column-footer id='" +
                                 footerClientId + "'>\n");
                    encodeRecursive(context, footer);
                    writer.write("</column-footer>\n");
                }
            }

        }

        public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }
            UIData data = (UIData) component;
            ResponseWriter writer = context.getResponseWriter();

            // Render the ending of the table
            UIComponent footer = (UIComponent) data.getFacet("footer");
            if (footer != null) {
                writer.write
                    ("<table-footer id='" + footer.getClientId(context) +
                     "'>\n");
                encodeRecursive(context, footer);
                writer.write("</table-footer>\n");
            }
            writer.write("</table>\n");

        }

        private void encodeRecursive(FacesContext context,
                                     UIComponent component)
            throws IOException {

            component.encodeBegin(context);
            if (component.getRendersChildren()) {
                component.encodeChildren(context);
            } else {
                Iterator kids = component.getChildren().iterator();
                while (kids.hasNext()) {
                    UIComponent kid = (UIComponent) kids.next();
                    encodeRecursive(context, kid);
                }
            }
            component.encodeEnd(context);
        }


    }


    // "Text" Renderer
    // NOTE - No conversion processing, assumes only Strings!
    class TextRenderer extends Renderer {

        public void decode(FacesContext context, UIComponent component) {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

            if (!(component instanceof UIInput)) {
                return;
            }
            UIInput input = (UIInput) component;
            String clientId = input.getClientId(context);

            // Decode incoming request parameters
            Map params = context.getExternalContext().getRequestParameterMap();
            if (params.containsKey(clientId)) {
                input.setSubmittedValue((String) params.get(clientId));
            }
        }

        public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

            Object value;
            if (component instanceof UIInput) {
                UIInput input = (UIInput) component;
                value = input.getSubmittedValue();
                if (value == null) {
                    value = input.getValue();
                }
            } else {
                value = ((UIOutput) component).getValue();
            }

            ResponseWriter writer = context.getResponseWriter();
            writer.write
                ("<text id='" + component.getClientId(context) + "' value='" +
                 value + "'/>\n");

        }

        public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

        public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

    }


}