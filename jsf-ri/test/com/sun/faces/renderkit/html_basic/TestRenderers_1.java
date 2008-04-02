/*
 * $Id: TestRenderers_1.java,v 1.20 2002/11/25 19:56:44 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.event.FormEvent;
import javax.faces.event.CommandEvent;
import javax.faces.event.FacesEvent;
import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.tree.XmlTreeImpl;

import com.sun.faces.renderkit.html_basic.FormRenderer;
import com.sun.faces.renderkit.html_basic.ButtonRenderer;
import com.sun.faces.renderkit.html_basic.RadioRenderer;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_1.java,v 1.20 2002/11/25 19:56:44 jvisvanathan Exp $
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
        
	XmlTreeImpl xmlTree = 
	    new XmlTreeImpl(getFacesContext(),
			    new UICommand(), "treeId", "");
	getFacesContext().setRequestTree(xmlTree);
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

	"<form method=\"post\" action=\"/test/facestreeId;jsessionid=24B278ACCA9F795C78E4F80903111C29\">"
};
        return lines;
    }    
    
    public void beginRenderers(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
       // theRequest.addParameter("name", "FormRenderer");
        //theRequest.addParameter("action", "form");
        theRequest.addParameter("/radio_renderer", "Two");
        theRequest.addParameter("name", "ButtonRenderer");
        theRequest.addParameter("name", "button");
    } 
    
    //
    // General Methods
    //
    public void testRenderers() {
        try {
            // create a dummy root for the tree.
            UIComponentBase root = new UIComponentBase() {
	        public String getComponentType() { return "root"; } 
	    };
            root.setComponentId("root");
            verifyFormRenderer(root);
            verifyRadioRenderer(root);
            verifyButtonRenderer(root);

            assertTrue(verifyExpectedOutput());
        }
        catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
            return;
        }
    }
    
    public void verifyFormRenderer(UIComponent root) throws IOException {
         boolean result = false;
         
        // Test FormRenderer.
        System.out.println("Testing FormRenderer");
        UIComponent uiForm = new UIForm();
        uiForm.setComponentId("formRenderer");
        uiForm.setValue("FormRenderer");
        root.addChild(uiForm);

        FormRenderer formRenderer = new FormRenderer();
        // test decode method
        System.out.println("Testing decode method");
        
        formRenderer.decode(getFacesContext(), uiForm);
        
        // make sure formEvent was queued.
        System.out.println("Testing getApplicationEvent: ");
        Iterator it = getFacesContext().getApplicationEvents();
        assertTrue(it != null );
        
        /* assertTrue(it.hasNext());
        FacesEvent event = (FacesEvent) it.next();
        assertTrue(event instanceof FormEvent); */
        
      
        // test encode method
        System.out.println("Testing encode method");
        formRenderer.encodeBegin(getFacesContext(), uiForm);
        getFacesContext().getResponseWriter().write("\n");
        
        // test encode method
        System.out.println("Testing encodeEnd method");
        formRenderer.encodeEnd(getFacesContext(), uiForm);
        getFacesContext().getResponseWriter().write("\n");
        
        // test supportComponentType method
        System.out.println("Testing supportsComponentType method"); 
        result = formRenderer.supportsComponentType("Test"); 
        assertTrue(!result);

        result = formRenderer.supportsComponentType(uiForm); 
        assertTrue(result);
        
    }
    
    public void verifyButtonRenderer(UIComponent root) throws IOException {
         boolean result = false;
         
        // Test ButtonRenderer.
        System.out.println("Testing ButtonRenderer");
        UIComponent uiCommand = new UICommand();
        uiCommand.setComponentId("buttonRenderer");
        uiCommand.setValue("ButtonRenderer");
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
        uiCommand.setValue("button");
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

        // test supportComponentType method
        System.out.println("Testing supportsComponentType method"); 
        result = buttonRenderer.supportsComponentType("Test"); 
        assertTrue(!result);

        result = buttonRenderer.supportsComponentType(uiCommand); 
        assertTrue(result);
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
       
        // test supportComponentType method
        System.out.println("Testing supportsComponentType method"); 
        result = radioRenderer.supportsComponentType("Test"); 
        assertTrue(!result);

        result = radioRenderer.supportsComponentType(uiSelectOne); 
        assertTrue(result);
    }
    
} // end of class TestRenderers_1
