/*
 * $Id: TestRenderers_1.java,v 1.30 2003/07/23 16:32:21 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_1.java

package com.sun.faces.renderkit.html_basic;

import org.apache.cactus.WebRequest;
import com.sun.faces.JspFacesTestCase;

import javax.faces.FacesException;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIForm;
import javax.faces.component.UICommand;
import javax.faces.component.UISelectOne;
import javax.faces.component.SelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import com.sun.faces.RIConstants;
import com.sun.faces.tree.SimpleTreeImpl;

import com.sun.faces.renderkit.html_basic.FormRenderer;
import com.sun.faces.renderkit.html_basic.ButtonRenderer;
import com.sun.faces.renderkit.html_basic.RadioRenderer;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_1.java,v 1.30 2003/07/23 16:32:21 rkitain Exp $
 * 
 *
 */

public class TestRenderers_1 extends JspFacesTestCase
{
    //
    // Protected Constants
    //

   public static final String TEST_URI = "/faces/form/FormRenderer/";
   
    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    
    //
    // Attribute Instance Variables
    // Relationship Instance Variables
    //
    // Constructors and Initializers    
    //

    public TestRenderers_1() {
        super("TestRenderers_1");
    }
    
    public TestRenderers_1(String name) {
        super(name);
    }
   
    //
    // Class methods
    //

    //
    // Methods from TestCase
    
    public void setUp() {
        super.setUp();
        
	SimpleTreeImpl xmlTree = 
	    new SimpleTreeImpl(getFacesContext(),
			    new UICommand(), "treeId");
	getFacesContext().setTree(xmlTree);
        assertTrue(getFacesContext().getResponseWriter() != null);
     }     

    // Methods from FacesTestCase
    public boolean sendWriterToFile() {
        return true;
    }    

    public String getExpectedOutputFilename() {
        return "CorrectRenderersResponse";
    }    

    public String [] getLinesToIgnore() {
        String[] lines =  {

	    "<form method=\"post\" action=\"/test/facestreeId;jsessionid=428000A9A343A01F7BBB2F460FDE5EC0\">"
};
        return lines;
    }    
    
    public void beginRenderers(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
       // theRequest.addParameter("name", "FormRenderer");
        //theRequest.addParameter("action", "form");
        theRequest.addParameter("radio_renderer", "Two");
        theRequest.addParameter("name", "ButtonRenderer");
        theRequest.addParameter("name", "button");
    } 
    
    //
    // General Methods
    //
    public void testRenderers() {
        try {
            // create a dummy root for the tree.
            UINamingContainer root = new UINamingContainer() {
	        public String getComponentType() { return "root"; } 
	    };
            root.setComponentId("root");
	    // Call this twice to test the multiple forms in a page logic.
            verifyFormRenderer(root, 0);
            verifyFormRenderer(root, 1);
            verifyRadioRenderer(root);
            verifyButtonRenderer(root);

            assertTrue(verifyExpectedOutput());

	    String stringToCheck = "id="+"\""+"formRenderer0"+"\"";
	    assertTrue(verifyExpectedStringInOutput(stringToCheck));
        }
        catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
            return;
        }
    }
    
    public void verifyFormRenderer(UIComponent root, 
				   int expectedFormNumber) throws IOException {
         boolean result = false;
         
        // Test FormRenderer.
        System.out.println("Testing FormRenderer");
        UIForm uiForm = new UIForm();
        uiForm.setComponentId("formRenderer" + expectedFormNumber);
        uiForm.setFormName("basicForm");
        root.addChild(uiForm);

        FormRenderer formRenderer = new FormRenderer();
        // test decode method
        System.out.println("Testing decode method");
        
        formRenderer.decode(getFacesContext(), uiForm);
        
        // make sure formEvent was queued.
        System.out.println("Testing getApplicationEvent: ");
       
        // test encode method
        System.out.println("Testing encode method");
        formRenderer.encodeBegin(getFacesContext(), uiForm);
        getFacesContext().getResponseWriter().write("\n");
        
        // test encode method
        System.out.println("Testing encodeEnd method");
        formRenderer.encodeEnd(getFacesContext(), uiForm);
        getFacesContext().getResponseWriter().write("\n");
        
	// test that our form number is correct.
	Integer formNumber = (Integer)
	    uiForm.getAttribute(RIConstants.FORM_NUMBER_ATTR);
	assertTrue(null != formNumber);
	assertTrue(formNumber.intValue() == expectedFormNumber);
    }
    
    public void verifyButtonRenderer(UIComponent root) throws IOException {
         boolean result = false;
         
        // Test ButtonRenderer.
        System.out.println("Testing ButtonRenderer");
        UICommand uiCommand = new UICommand();
        uiCommand.setComponentId("buttonRenderer");
        uiCommand.setAttribute("label", "Login");
        root.addChild(uiCommand);

        ButtonRenderer buttonRenderer = new ButtonRenderer();
        // test decode method
        System.out.println("Testing decode method");
        buttonRenderer.decode(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().write("\n");
        
        // test encode method
        System.out.println("Testing encode method");
        buttonRenderer.encodeBegin(getFacesContext(), uiCommand);
        buttonRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().write("\n");
        try {
            getFacesContext().getResponseWriter().flush();
        } catch (Exception e ) {
            throw new FacesException("Exception while flushing buffer");
        } 

        // Test <Button> element rendering
        uiCommand = new UICommand();
        uiCommand.setComponentId("myButton");
        uiCommand.setAttribute("type", "submit");
        root.addChild(uiCommand);
        System.out.println("Testing encode (<button> rendering) method");
        buttonRenderer.encodeBegin(getFacesContext(), uiCommand);
        buttonRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().write("\n");
        try {
            getFacesContext().getResponseWriter().flush();
        } catch (Exception e ) {
            throw new FacesException("Exception while flushing buffer");
        } 

    }
    
    public void verifyRadioRenderer(UIComponent root) throws IOException {
         boolean result = false;
         

        // Test RadioRenderer.
        System.out.println("Testing RadioRenderer");
        UISelectOne uiSelectOne = new UISelectOne();
	UISelectItems uiSelectItems = new UISelectItems();
        uiSelectOne.setComponentId("radio_renderer");
        root.addChild(uiSelectOne);

        SelectItem item1 = new SelectItem("One", "One",null);
        SelectItem item2 = new SelectItem("Two", "Two", null);
        SelectItem item3 = new SelectItem("Three", "Three" ,null);
        
        SelectItem[] items = {item1, item2,item3};
        uiSelectItems.setValue(items);
	uiSelectItems.setComponentId("items");
	uiSelectOne.addChild(uiSelectItems);
        
        RadioRenderer radioRenderer = new RadioRenderer();
        // test decode method
        System.out.println("Testing decode method");
        radioRenderer.decode(getFacesContext(), uiSelectOne);
        assertTrue(((String)uiSelectOne.getValue()).equals("Two"));

        // test encode method
        System.out.println("Testing encode method");
        radioRenderer.encodeBegin(getFacesContext(), uiSelectOne);
        radioRenderer.encodeEnd(getFacesContext(), uiSelectOne);
       
    }
    
} // end of class TestRenderers_1
