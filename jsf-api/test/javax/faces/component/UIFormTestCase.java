/*
 * $Id: UIFormTestCase.java,v 1.11 2005/04/04 17:23:34 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIInput;
import javax.faces.render.RenderKitFactory;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIForm}.</p>
 */

public class UIFormTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIFormTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIForm();
        expectedFamily = UIForm.COMPONENT_FAMILY;
        expectedId = null;
        expectedRendererType = "javax.faces.Form";
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIFormTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UIForm instance
    public void testPristine() {

        super.testPristine();
        UIForm form = (UIForm) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIForm form = (UIForm) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIForm form = (UIForm) component;

    }

    // test prependId does the right thing.
    public void testPrependId() throws Exception {

	UIForm form = null;
	UIInput input = null;
    UINamingContainer container = null;
	UIViewRoot root = null;
	
	// Case 1: no user specified id anywhere, isPrependId==true
	root = new UIViewRoot();
	form = new UIForm();
	input = new UIInput();
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
	
	form.getChildren().add(input);
	root.getChildren().add(form);

	facesContext.setViewRoot(root);

	assertEquals("Case 1: no user specified id anywhere, isPrependId==true .",
		     "_id0:_id1", input.getClientId(facesContext));


	// Case 2: user specified id on form only, isPrependId==true
	root = new UIViewRoot();
	form = new UIForm();
	input = new UIInput();
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);	
	form.setId("form");

	form.getChildren().add(input);
	root.getChildren().add(form);

	facesContext.setViewRoot(root);

	assertEquals("Case 2: user specified id on form only, isPrependId==true .",
		     "form:_id0", input.getClientId(facesContext));

	// Case 3: user specified id on input only, isPrependId==true
	root = new UIViewRoot();
	form = new UIForm();
	input = new UIInput();
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);	
	input.setId("input");

	form.getChildren().add(input);
	root.getChildren().add(form);

	facesContext.setViewRoot(root);

	assertEquals("Case 3: user specified id on input only, isPrependId==true .",
		     "_id0:input", input.getClientId(facesContext));

	// Case 4: user specified id everywhere, isPrependId==true
	root = new UIViewRoot();
	form = new UIForm();
	input = new UIInput();
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);	
	form.setId("form");
	input.setId("input");

	form.getChildren().add(input);
	root.getChildren().add(form);

	facesContext.setViewRoot(root);

	assertEquals("Case 4: user specified id everywhere, isPrependId==true .",
		     "form:input", input.getClientId(facesContext));

	// Case 5: no user specified id anywhere, isPrependId==false
	root = new UIViewRoot();
	form = new UIForm();
	input = new UIInput();
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);	
	form.setPrependId(false);
	form.getChildren().add(input);
	root.getChildren().add(form);

	facesContext.setViewRoot(root);

	assertEquals("Case 5: no user specified id anywhere, isPrependId==false .",
		     "_id0", input.getClientId(facesContext));


	// Case 6: user specified id on form only, isPrependId==false
	root = new UIViewRoot();
	form = new UIForm();
	input = new UIInput();
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);	
	form.setPrependId(false);
	form.setId("form");

	form.getChildren().add(input);
	root.getChildren().add(form);

	facesContext.setViewRoot(root);

	assertEquals("Case 6: user specified id on form only, isPrependId==false .",
		     "_id0", input.getClientId(facesContext));

	// Case 7: user specified id on input only, isPrependId==false
	root = new UIViewRoot();
	form = new UIForm();
	input = new UIInput();
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);	
	form.setPrependId(false);
	input.setId("input");

	form.getChildren().add(input);
	root.getChildren().add(form);

	facesContext.setViewRoot(root);

	assertEquals("Case 7: user specified id on input only, isPrependId==false .",
		     "input", input.getClientId(facesContext));

	// Case 8: user specified id everywhere, isPrependId==false
	root = new UIViewRoot();
	form = new UIForm();
	input = new UIInput();
	root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);	
	form.setPrependId(false);
	form.setId("form");
	input.setId("input");

	form.getChildren().add(input);
	root.getChildren().add(form);

	facesContext.setViewRoot(root);

	assertEquals("Case 8: user specified id everywhere, isPrependId==false .",
		     "input", input.getClientId(facesContext));

	// Case 9: UIForm only, no user specified id, isPrependId==true
	form = new UIForm();
	assertEquals("Case 9: UIForm only, no user specified id, isPrepend==true",
		     "_id0", form.getClientId(facesContext));

	// Case 10: UIForm only, no user specified id, isPrependId==false
	form = new UIForm();
	form.setPrependId(false);
	assertEquals("Case 10: UIForm only, no user specified id, isPrependId==false",
		     "_id1", form.getClientId(facesContext));
    
    
    // Case 11: UIForm has a parent NamingContainer, isPrependId==true"
    root = new UIViewRoot();
    container = new UINamingContainer();
    form = new UIForm();
    input = new UIInput();
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);    
    form.setPrependId(true);
    container.setId("subview");
    form.setId("form");
    input.setId("input");
    
    form.getChildren().add(input);
    container.getChildren().add(form);
    root.getChildren().add(container);
    
    facesContext.setViewRoot(root);
    
    assertEquals("Case 11: UIForm has a parent NamingContainer, isPrependId==true",
            "subview:form:input", input.getClientId(facesContext));
    
    // Case 12: UIForm has a parent NamingContainer, isPrependId==false
    form.setPrependId(false);
    input.setId("input");
    assertEquals("Case 12: UIForm has a parent NamingContainer, isPrependId==false",
            "subview:input", input.getClientId(facesContext));
    

    }


}
