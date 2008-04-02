/*
 * $Id: UIComponentBaseStateTestCase.java,v 1.7 2003/09/18 00:53:48 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentTestCase;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.context.FacesContext;
import javax.faces.convert.LongConverter;
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

	UIViewRootBase root1 = createTestView();
	UIViewRootBase root2 = createTestView();
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

    protected UIViewRootBase createTestView() {
	UIViewRootBase root = new UIViewRootBase();
	root.setRendererType(null);
	UIFormBase form = new UIFormBase();
	form.setRendererType(null);
	UIInputBase input1 = new UIInputBaseTestCase.UIInputSub();
	input1.setRendererType(null);
	UIInputBase input2 = new UIInputBaseTestCase.UIInputSub();
	input2.setRendererType(null);
	UIPanelBase header = new UIPanelBase();
	header.setRendererType(null);
	UIPanelBase footer = new UIPanelBase();
	footer.setRendererType(null);
	UIOutputBase output1 = new UIOutputBase();
	output1.setRendererType(null);
	UICommandBase command = new UICommandBaseTestCase.UICommandSub();
	command.setRendererType(null);
	UISelectItemBase selectItem = new UISelectItemBase();
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

    protected void applyAttributesToTestView(UIViewRootBase root) {
	root.setViewId("viewId");
	root.setId("page");
	root.setRenderKitId("renderKitId");
	UIFormBase form = (UIFormBase) root.getChildren().get(0);
	UIInputBase input = (UIInputBase) form.getChildren().get(0);
	input.setId("input1");
	input.setPrevious("previous1");
	input.setValid(false);
	input.setRequired(true);
	input.setRendered(false);
	input.setComponentRef("componentRef1");
	input.setAttribute("attr1", "attr1");
	input.addValueChangedListener(new TestValueChangedListener("ANY1",
								   PhaseId.ANY_PHASE));
	DoubleRangeValidator doubleVal = new DoubleRangeValidator();
	doubleVal.setMinimum(3.14);
	doubleVal.setMaximum(6.02);
	input.addValidator(doubleVal);

	input = (UIInputBase) form.getChildren().get(1);
	input.setId("input2");
	input.setPrevious("previous2");
	input.setValid(true);
	input.setRequired(false);
	// note that I'm passing "input" as the last argument to the
	// TestValueChangedListener ctor.  This exercises the logic that
	// allows attached objects to maintain pointers to the
	// components to which they are attached.
	input.addValueChangedListener(new TestValueChangedListener("ANY2",
								   PhaseId.ANY_PHASE, input));

	((UIPanelBase)input.getFacets().get("header")).setId("header");
	((UIPanelBase)input.getFacets().get("footer")).setId("footer");

	doubleVal = new DoubleRangeValidator();
	doubleVal.setMinimum(1);
	doubleVal.setMaximum(3);
	input.addValidator(doubleVal);

	UIOutputBase output = (UIOutputBase) form.getChildren().get(2);
	output.setId("output");
	output.setValueRef("valueRef");
	output.setConverter(new LongConverter());
	
	UICommandBase command = (UICommandBase) form.getChildren().get(3);
	command.setId("command");
	command.setAction("action");
	command.setActionRef("actionRef");
	command.addActionListener(new TestActionListener("ANY",
							 PhaseId.ANY_PHASE));

	UISelectItemBase selectItem = (UISelectItemBase) form.getChildren().get(4);
	selectItem.setId("selectItem");
	selectItem.setItemLabel("label");
	selectItem.setItemValue("value");
	selectItem.setItemDescription("description");
    }

    protected boolean verifyTestViewsAreEqual(UIViewRootBase root1, 
					      UIViewRootBase root2) {
	UIViewRootBaseTestCase pageBaseTester = new UIViewRootBaseTestCase("temp");
	UIInputBaseTestCase inputBaseTester = new UIInputBaseTestCase("temp");
	UIOutputBaseTestCase outputBaseTester = new UIOutputBaseTestCase("temp");
	UICommandBaseTestCase commandBaseTester = new UICommandBaseTestCase("temp");
	UISelectItemBaseTestCase selectItemBaseTester = new UISelectItemBaseTestCase("temp");
	assertTrue(pageBaseTester.propertiesAreEqual(facesContext, 
						     root1, root2));
	UIFormBase form1 = (UIFormBase) root1.getChildren().get(0);
	UIFormBase form2 = (UIFormBase) root2.getChildren().get(0);
	UIInputBaseTestCase.UIInputSub input1 = (UIInputBaseTestCase.UIInputSub) form1.getChildren().get(0);
	UIInputBaseTestCase.UIInputSub input2 = (UIInputBaseTestCase.UIInputSub) form2.getChildren().get(0);
	assertTrue(inputBaseTester.propertiesAreEqual(facesContext, 
						      input1, input2));
	assertTrue(this.propertiesAreEqual(facesContext, input1, input1));

	input1 = (UIInputBaseTestCase.UIInputSub) form1.getChildren().get(1);
	input2 = (UIInputBaseTestCase.UIInputSub) form2.getChildren().get(1);
	assertTrue(inputBaseTester.propertiesAreEqual(facesContext, 
						      input1, input2));
	assertTrue(inputBaseTester.listenersAreEqual(facesContext, 
						      input1, input2));

	UIOutputBase output1 = (UIOutputBase) form1.getChildren().get(2);
	UIOutputBase output2 = (UIOutputBase) form2.getChildren().get(2);
	assertTrue(outputBaseTester.propertiesAreEqual(facesContext, 
						       output1, output2));

	UICommandBase command1 = (UICommandBase) form1.getChildren().get(3);
	UICommandBase command2 = (UICommandBase) form2.getChildren().get(3);
	assertTrue(commandBaseTester.propertiesAreEqual(facesContext, 
							command1, command2));

	UISelectItemBase selectItem1 = 
	    (UISelectItemBase) form1.getChildren().get(4);
	UISelectItemBase selectItem2 = 
	    (UISelectItemBase) form2.getChildren().get(4);
	assertTrue(selectItemBaseTester.propertiesAreEqual(facesContext,
							   selectItem1,
							   selectItem2));

	return true;
    }

}
