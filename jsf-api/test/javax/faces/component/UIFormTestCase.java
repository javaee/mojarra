/*
 * $Id: UIFormTestCase.java,v 1.13 2006/03/14 16:33:22 edburns Exp $
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
		     "j_id0:j_id1", input.getClientId(facesContext));


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
		     "form:j_id0", input.getClientId(facesContext));

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
		     "j_id0:input", input.getClientId(facesContext));

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
		     "j_id0", input.getClientId(facesContext));


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
		     "j_id0", input.getClientId(facesContext));

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
		     "j_id0", form.getClientId(facesContext));

	// Case 10: UIForm only, no user specified id, isPrependId==false
	form = new UIForm();
	form.setPrependId(false);
	assertEquals("Case 10: UIForm only, no user specified id, isPrependId==false",
		     "j_id1", form.getClientId(facesContext));
    
    
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
