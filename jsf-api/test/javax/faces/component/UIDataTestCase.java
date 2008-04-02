/*
 * $Id: UIDataTestCase.java,v 1.21 2003/11/08 01:15:36 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.LongConverter;
import javax.faces.convert.ShortConverter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.mock.MockExternalContext;
import javax.faces.mock.MockResponseWriter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIData}.</p>
 */

public class UIDataTestCase extends ValueHolderTestCaseBase {


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
        expectedId = null;
        expectedRendererType = "Table";
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

        ValueBinding vb = application.getValueBinding("foo");
        ValueBinding vbCommand = application.getValueBinding("foo.command");
        ValueBinding vbInput = application.getValueBinding("foo.input");
        ValueBinding vbOutput = application.getValueBinding("foo.output");
        UIData data = (UIData) component;
        setupModel();
        setupRenderers();
        setupTree();

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

        ValueBinding vb = application.getValueBinding("foo");
        ValueBinding vbCommand = application.getValueBinding("foo.command");
        ValueBinding vbInput = application.getValueBinding("foo.input");
        ValueBinding vbOutput = application.getValueBinding("foo.output");
        UIData data = (UIData) component;
        setupModel();
        setupRenderers();
        setupTree();

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

        data.setFirst(0);
        data.setFirst(11);
        data.setRows(0);
        data.setRows(20);
        data.setVar(null);
        data.setVar("foo");

    }


    // Test request processing lifecycle (successful input)
    public void testTreeLifecycle() throws Exception {

        ValueBinding vbCommand = application.getValueBinding("foo.command");
        ValueBinding vbInput = application.getValueBinding("foo.input");
        ValueBinding vbOutput = application.getValueBinding("foo.output");
        String before[] =
            { "input3", "input4", "input5", "input6", "input7" };
        String after[] =
            { "input3", "input4A", "input5", "input6B", "input7" };

        // Set up for this test
        setupModel();
        setupRenderers();
        setupTree();
        UIData data = (UIData) component;

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
        root.processDecodes(facesContext);
        assertEquals("/data:5:command" +
                     "/data:7:command",
                     TestDataActionListener.trace());
        assertEquals("", TestDataValidator.trace());
        assertEquals("", TestDataValueChangeListener.trace());
        checkMessages(0);

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

        //   RENDER RESPONSE
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_3.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_3.xml");

    }


    // Test rendering the tree and validate the output twice
    public void testTreeRendering() throws Exception {

        // Set up for this test
        setupModel();
        setupRenderers();
        setupTree();

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
        setupTree();

        // Validate the rendered output
        ((UIData) component).setFirst(7);
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_5.xml");
        renderResponse();
        checkResponse("/javax/faces/component/UIDataTestCase_5.xml");

    }


    // Test updating the tree's per-row values and validate the output twice
    public void testTreeUpdating() throws Exception {

        ValueBinding vbCommand = application.getValueBinding("foo.command");
        ValueBinding vbInput = application.getValueBinding("foo.input");
        ValueBinding vbOutput = application.getValueBinding("foo.output");

        // Set up for this test
        setupModel();
        setupRenderers();
        setupTree();
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

        ValueBinding vbCommand = application.getValueBinding("foo.command");
        ValueBinding vbInput = application.getValueBinding("foo.input");
        ValueBinding vbOutput = application.getValueBinding("foo.output");
        String before[] =
            { "input3", "input4", "input5", "input6", "input7" };

        // Set up for this test
        setupModel();
        setupRenderers();
        setupTree();
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


    public void testValueBindings() {

	super.testValueBindings();
	UIData test = (UIData) component;

	// "first" property
	request.setAttribute("foo", new Integer(5));
	test.setValueBinding("first", application.getValueBinding("#{foo}"));
	assertEquals(5, test.getFirst());
	test.setFirst(10);
	assertEquals(10, test.getFirst());
	assertNotNull(test.getValueBinding("first"));

	// "rowIndex" property
	request.setAttribute("foo", new Integer(5));
	test.setValueBinding("rowIndex", application.getValueBinding("#{foo}"));
	assertEquals(5, test.getRowIndex());
	test.setRowIndex(10);
	assertEquals(10, test.getRowIndex());
	assertNotNull(test.getValueBinding("rowIndex"));

	// "rows" property
	request.setAttribute("foo", new Integer(5));
	test.setValueBinding("rows", application.getValueBinding("#{foo}"));
	assertEquals(5, test.getRows());
	test.setRows(10);
	assertEquals(10, test.getRows());
	assertNotNull(test.getValueBinding("rows"));

	// "value" property
	request.setAttribute("foo", "bar");
	test.setValue(null);
	assertNull(test.getValue());
	test.setValueBinding("value", application.getValueBinding("#{foo}"));
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
	request.setAttribute("foo", "bar");
	test.setVar(null);
	assertNull(test.getVar());
	test.setValueBinding("var", application.getValueBinding("#{foo}"));
	assertNotNull(test.getValueBinding("var"));
	assertEquals("bar", test.getVar());
	test.setVar("baz");
	assertEquals("baz", test.getVar());
	test.setVar(null);
	assertEquals("bar", test.getVar());
	test.setValueBinding("var", null);
	assertNull(test.getValueBinding("var"));
	assertNull(test.getVar());

   }


    // --------------------------------------------------------- Support Methods


    // Check that the number of queued messages equals the expected count
    protected void checkMessages(int expected) {

        int n = 0;
        Iterator messages = facesContext.getMessages();
        while (messages.hasNext()) {
            FacesMessage message = (FacesMessage) messages.next();
            n++;
            // System.err.println(message.getSummary());
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
            renderKitFactory.getRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT);
        renderKit.addRenderer("Button", new ButtonRenderer());
        renderKit.addRenderer("Table", new TableRenderer());
        renderKit.addRenderer("Text", new TextRenderer());

    }


    // Set up the component tree corresponding to the data model
    protected void setupTree() throws Exception {

        // Attach our UIData to the view root
        UIData data = (UIData) component;
        data.setId("data");
        UIViewRoot root = new UIViewRoot();
        root.setRenderKitId(RenderKitFactory.DEFAULT_RENDER_KIT);
        root.setViewId("/view");
        facesContext.setViewRoot(root);
        root.getChildren().add(data);

        // Set up columns with facets and fields for each property
        UIColumn column;
        UICommand command;
        UIInput input;
        UIOutput output;
        UIOutput label;

        column = new UIColumn();
        column.setId("commandColumn");
        label = new UIOutput();
        label.setId("commandHeader");
        label.setValue("Command Header");
        column.getFacets().put("header", label);
        label = new UIOutput();
        label.setId("commandFooter");
        label.setValue("Command Footer");
        column.getFacets().put("footer", label);
        command = new UICommand();
        command.setId("command");
        command.setValueBinding("value",
				application.getValueBinding("#{foo.command}"));
        column.getChildren().add(command);
        data.getChildren().add(column);
        command.addActionListener(new TestDataActionListener());

        column = new UIColumn();
        column.setId("inputColumn");
        label = new UIOutput();
        label.setId("inputHeader");
        label.setValue("Input Header");
        column.getFacets().put("header", label);
        label = new UIOutput();
        label.setId("inputFooter");
        label.setValue("Input Footer");
        column.getFacets().put("footer", label);
        input = new UIInput();
        input.setId("input");
        input.setValueBinding("value",
			      application.getValueBinding("#{foo.input}"));
        column.getChildren().add(input);
        data.getChildren().add(column);
        input.addValidator(new TestDataValidator());
        input.addValueChangeListener(new TestDataValueChangeListener());

        column = new UIColumn();
        column.setId("outputColumn");
        label = new UIOutput();
        label.setId("outputHeader");
        label.setValue("Output Header");
        column.getFacets().put("header", label);
        label = new UIOutput();
        label.setId("outputFooter");
        label.setValue("Output Footer");
        column.getFacets().put("footer", label);
        output = new UIOutput();
        output.setId("output");
        output.setValueBinding("value",
			       application.getValueBinding("#{foo.output}"));
        column.getChildren().add(output);
        data.getChildren().add(column);

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
                 ((ValueHolder) component).getValue() + "'/>\n");

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
            writer.write
                ("<table id='" + data.getClientId(context) + "'>\n");
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
                    writer.write("<column-header id='" +
                                 header.getClientId(context) + "'>\n");
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
                    writer.write("<column id='" + column.getClientId(context) +
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
                    writer.write("<column-footer id='" +
                                 footer.getClientId(context) + "'>\n");
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
            // System.err.println("decode(" + clientId + ")");

            // Save the previous value for future ValueChangeEvent handling
            input.setPrevious(input.getValue());

            // Decode incoming request parameters
            Map params = context.getExternalContext().getRequestParameterMap();
            if (params.containsKey(clientId)) {
                // System.err.println("  '" + input.currentValue(context) +
                //                    "' --> '" + params.get(clientId) + "'");
                input.setValue((String) params.get(clientId));
            }

        }

        public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }
            ResponseWriter writer = context.getResponseWriter();
            writer.write
                ("<text id='" + component.getClientId(context) + "' value='" +
                 ((ValueHolder) component).getValue() + "'/>\n");

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
