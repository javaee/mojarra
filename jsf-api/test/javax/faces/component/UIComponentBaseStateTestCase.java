/*
 * $Id: UIComponentBaseStateTestCase.java,v 1.2 2003/10/09 19:18:24 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.mock.MockExternalContext;
import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockHttpServletResponse;
import javax.faces.mock.MockHttpSession;
import javax.faces.mock.MockLifecycle;
import javax.faces.mock.MockRenderKit;
import javax.faces.mock.MockRenderKitFactory;
import javax.faces.mock.MockServletConfig;
import javax.faces.mock.MockServletContext;
import javax.faces.mock.MockApplication;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;
import javax.faces.validator.DoubleRangeValidator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;



/**
 * <p>Test only the process{Get,Restore}State methods.</p>
 */

public class UIComponentBaseStateTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------ Instance Variables

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIComponentBaseStateTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIComponentBaseStateTestCase.class));
    }


    // ------------------------------------------------- Individual Test Methods

    public void testProcessState() throws Exception {

	UIViewRoot root1 = createTestView();
	UIViewRoot root2 = createTestView();
	assertTrue(verifyTestViewsAreEqual(root1, root2));
	applyAttributesToTestView(root1);
        Object componentState = root1.processSaveState(facesContext);
        root2.processRestoreState(facesContext, componentState);
	assertTrue(verifyTestViewsAreEqual(root1, root2));

    }

    // --------------------------------------------------------- Support Methods

    /**
     *
     * @return a UIComponent tree consisting of
     * UIViewRoot(UIForm(UIInput,UIInput,UIOutput,UICommand))
     */

    protected UIViewRoot createTestView() {
	UIViewRoot root = new UIViewRoot();
	root.setRendererType(null);
	UIForm form = new UIForm();
	form.setRendererType(null);
	UIInput input1 = new UIInputTestCase.UIInputSub();
	input1.setRendererType(null);
	UIInput input2 = new UIInputTestCase.UIInputSub();
	input2.setRendererType(null);
	UIPanel header = new UIPanel();
	header.setRendererType(null);
	UIPanel footer = new UIPanel();
	footer.setRendererType(null);
	UIOutput output1 = new UIOutput();
	output1.setRendererType(null);
	UICommand command = new UICommandTestCase.UICommandSub();
	command.setRendererType(null);
	UISelectItem selectItem = new UISelectItem();
	selectItem.setRendererType(null);

	root.getChildren().add(form);
	form.getChildren().add(input1);
	input2.getFacets().put("header", header);
	input2.getFacets().put("footer", footer);
	form.getChildren().add(input2);
	form.getChildren().add(output1);
	form.getChildren().add(command);
	form.getChildren().add(selectItem);
	return root;
    }

    protected void applyAttributesToTestView(UIViewRoot root) {
	root.setViewId("viewId");
	root.setId("page");
	root.setRenderKitId("renderKitId");
	UIForm form = (UIForm) root.getChildren().get(0);
	UIInput input = (UIInput) form.getChildren().get(0);
	input.setId("input1");
	input.setPrevious("previous1");
	input.setValid(false);
	input.setRequired(true);
	input.setRendered(false);
	input.setComponentRef("componentRef1");
	input.getAttributes().put("attr1", "attr1");
	input.addValueChangedListener(new TestValueChangedListener("ANY1",
								   PhaseId.ANY_PHASE));
	DoubleRangeValidator doubleVal = new DoubleRangeValidator();
	doubleVal.setMinimum(3.14);
	doubleVal.setMaximum(6.02);
	input.addValidator(doubleVal);

	input = (UIInput) form.getChildren().get(1);
	input.setId("input2");
	input.setPrevious("previous2");
	input.setValid(true);
	input.setRequired(false);
	// note that I'm passing "input" as the last argument to the
	// TestValueChangedListener ctor.  This exercises the logic that
	// allows attached objects to maintain pointers to the
	// components to which they are attached.
	input.addValueChangedListener(new TestValueChangedListener("ANY2",
								   PhaseId.ANY_PHASE));
	
	((UIPanel)input.getFacets().get("header")).setId("header");
	((UIPanel)input.getFacets().get("footer")).setId("footer");

	doubleVal = new DoubleRangeValidator();
	doubleVal.setMinimum(1);
	doubleVal.setMaximum(3);
	input.addValidator(doubleVal);

	UIOutput output = (UIOutput) form.getChildren().get(2);
	output.setId("output");
	output.setValueRef("valueRef");
	
	UICommand command = (UICommand) form.getChildren().get(3);
	command.setId("command");
	command.setAction("action");
	command.setActionRef("actionRef");
	command.addActionListener(new TestActionListener("ANY",
							 PhaseId.ANY_PHASE));

	UISelectItem selectItem = (UISelectItem) form.getChildren().get(4);
	selectItem.setId("selectItem");
	selectItem.setItemLabel("label");
	selectItem.setItemValue("value");
	selectItem.setItemDescription("description");
    }

    protected boolean verifyTestViewsAreEqual(UIViewRoot root1, 
					      UIViewRoot root2) {
	UIViewRootTestCase pageBaseTester = new UIViewRootTestCase("temp");
	UIInputTestCase inputBaseTester = new UIInputTestCase("temp");
	UIOutputTestCase outputBaseTester = new UIOutputTestCase("temp");
	UICommandTestCase commandBaseTester = new UICommandTestCase("temp");
	UISelectItemTestCase selectItemBaseTester = new UISelectItemTestCase("temp");
	assertTrue(pageBaseTester.propertiesAreEqual(facesContext, 
						     root1, root2));
	UIForm form1 = (UIForm) root1.getChildren().get(0);
	UIForm form2 = (UIForm) root2.getChildren().get(0);
	UIInputTestCase.UIInputSub input1 = (UIInputTestCase.UIInputSub) form1.getChildren().get(0);
	UIInputTestCase.UIInputSub input2 = (UIInputTestCase.UIInputSub) form2.getChildren().get(0);
	assertTrue(inputBaseTester.propertiesAreEqual(facesContext, 
						      input1, input2));
	assertTrue(this.propertiesAreEqual(facesContext, input1, input1));

	input1 = (UIInputTestCase.UIInputSub) form1.getChildren().get(1);
	input2 = (UIInputTestCase.UIInputSub) form2.getChildren().get(1);
	assertTrue(inputBaseTester.propertiesAreEqual(facesContext, 
						      input1, input2));
	assertTrue(inputBaseTester.listenersAreEqual(facesContext, 
						      input1, input2));

	UIOutput output1 = (UIOutput) form1.getChildren().get(2);
	UIOutput output2 = (UIOutput) form2.getChildren().get(2);
	assertTrue(outputBaseTester.propertiesAreEqual(facesContext, 
						       output1, output2));

	UICommand command1 = (UICommand) form1.getChildren().get(3);
	UICommand command2 = (UICommand) form2.getChildren().get(3);
	assertTrue(commandBaseTester.propertiesAreEqual(facesContext, 
							command1, command2));

	UISelectItem selectItem1 = 
	    (UISelectItem) form1.getChildren().get(4);
	UISelectItem selectItem2 = 
	    (UISelectItem) form2.getChildren().get(4);
	assertTrue(selectItemBaseTester.propertiesAreEqual(facesContext,
							   selectItem1,
							   selectItem2));

	return true;
    }

}
