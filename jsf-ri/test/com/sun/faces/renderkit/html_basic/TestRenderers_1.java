/*
 * $Id: TestRenderers_1.java,v 1.45 2003/10/08 00:43:26 rlubke Exp $
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
import java.util.ResourceBundle;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.faces.component.UIForm;
import javax.faces.component.UICommand;
import javax.faces.component.UISelectOne;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.model.SelectItem;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.jstl.core.Config;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ViewHandlerImpl;

import com.sun.faces.renderkit.html_basic.FormRenderer;
import com.sun.faces.renderkit.html_basic.ButtonRenderer;
import com.sun.faces.renderkit.html_basic.HyperlinkRenderer;
import com.sun.faces.renderkit.html_basic.RadioRenderer;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_1.java,v 1.45 2003/10/08 00:43:26 rlubke Exp $
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

	UIViewRoot xmlView = new UIViewRoot();
	xmlView.setViewId("viewId");
	getFacesContext().setViewRoot(xmlView);
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
	    "<form id=\"formRenderer0\" method=\"post\" action=\"/test/facesviewId;jsessionid=4DC027279CF0211363CCDC2807A21D05\">",
	    "<form id=\"formRenderer1\" method=\"post\" action=\"/test/facesviewId;jsessionid=4DC027279CF0211363CCDC2807A21D05\">"
	};
        return lines;
    }    
    
    public void beginRenderers(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
       // theRequest.addParameter("name", "FormRenderer");
        //theRequest.addParameter("action", "form");
        theRequest.addParameter("radioRenderer", "Two");
        theRequest.addParameter("name", "ButtonRenderer");
        theRequest.addParameter("name", "button");
    } 
    
    //
    // General Methods
    //
    public void testRenderers() {
        Map sessionMap = getFacesContext().getExternalContext().getSessionMap();
        // Spoof a setBundle action...
        LocalizationContext locContext = new LocalizationContext(new Messages_en(), Locale.ENGLISH);
        sessionMap.put("Messages", locContext);


        try {
            // create a dummy root for the tree.
            UIViewRoot root = new UIViewRoot();
            root.setId("root");
            root.setViewId("/root");
            ViewHandlerImpl viewHandler = new ViewHandlerImpl();
            List mappings = new ArrayList();
            mappings.add("/faces");
            viewHandler.setFacesMapping(mappings);
            getFacesContext().getApplication().setViewHandler(viewHandler);
            getFacesContext().setViewRoot(root);
	    // Call this twice to test the multiple forms in a page logic.
            getFacesContext().getResponseWriter().startDocument();
            verifyFormRenderer(root, 0);
            verifyFormRenderer(root, 1);
            verifyRadioRenderer(root);
            verifyButtonRenderer(root);
            verifyHyperlinkRenderer(root);
            getFacesContext().getResponseWriter().endDocument();

            assertTrue(verifyExpectedOutput());
            sessionMap.remove("Messages");
	    String stringToCheck = "id="+"\""+"formRenderer0"+"\"";
	    assertTrue(verifyExpectedStringInOutput(stringToCheck));
        }
        catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
            return;
        }


    }

    public void verifyHyperlinkRenderer(UIComponent root) throws IOException {
        // Provide attributes for all possible lookups
        // make sure that valueRef is returned and no others.
        System.out.println("Testing Hyperlink Renderer...");
        HyperlinkRenderer hyperlinkRenderer = new HyperlinkRenderer();
        UICommand uiCommand = new UICommand();
        uiCommand.setId("labelLink1");
        uiCommand.setValue("PASSED");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        uiCommand.getAttributes().put("key", "failed.key");
        uiCommand.setValueRef("TestBean.modelLabel");
        root.getChildren().add(uiCommand);
        System.out.println("Testing label lookup from local value...");
        hyperlinkRenderer.encodeBegin(getFacesContext(), uiCommand);
        hyperlinkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // No value this round, ensure the valueRef for the button label
        // is pulled from the model
        uiCommand = new UICommand();
        uiCommand.setId("labelLink2");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        uiCommand.getAttributes().put("key", "failed.key");
        uiCommand.setValueRef("TestBean.modelLabel");
        root.getChildren().add(uiCommand);
        System.out.println("Testing label lookup from model...");
        hyperlinkRenderer.encodeBegin(getFacesContext(), uiCommand);
        hyperlinkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // No valueRef or explicit label.  Pull value from the
        // specified ResourceBundle using the key
        uiCommand = new UICommand();
        uiCommand.setId("labelLink3");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        uiCommand.getAttributes().put("key", "passed.key");
        root.getChildren().add(uiCommand);
        System.out.println("Testing label lookup from ResourceBundle...");
        hyperlinkRenderer.encodeBegin(getFacesContext(), uiCommand);
        hyperlinkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // All lookup methods fail, test of hyperlink should be empty
        uiCommand = new UICommand();
        uiCommand.setId("labelLink4");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        uiCommand.getAttributes().put("key", "non.key");
        uiCommand.setValueRef("NonBean.label");
        root.getChildren().add(uiCommand);
        System.out.println("Testing empty label...");
        hyperlinkRenderer.encodeBegin(getFacesContext(), uiCommand);
        hyperlinkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test hyperlink as image
        uiCommand = new UICommand();
        uiCommand.setId("hyperlinkImage");
        uiCommand.getAttributes().put("image", "duke.gif");
        uiCommand.setValue("SHOUD NOT BE HERE");
        root.getChildren().add(uiCommand);
        System.out.println("Testing hyperlink as image");
        hyperlinkRenderer.encodeBegin(getFacesContext(), uiCommand);
        hyperlinkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test hyperlink as image with image specified in resource bundle
        uiCommand = new UICommand();
        uiCommand.setId("hyperlinkImage2");
        uiCommand.getAttributes().put("imageKey", "image.key");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        root.getChildren().add(uiCommand);
        System.out.println("Testing hyperlink image via resource lookup");
        hyperlinkRenderer.encodeBegin(getFacesContext(), uiCommand);
        hyperlinkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test hyperlink with parameters
        uiCommand = new UICommand();
        uiCommand.setId("paramLink");
        uiCommand.setValue("hyperlink with parameters");
        root.getChildren().add(uiCommand);
        UIParameter parameter1 = new UIParameter();
        UIParameter parameter2 = new UIParameter();
        parameter1.setId("param1");
        parameter1.setName("parameter1");
        parameter1.setValue("param_value1");
        parameter2.setId("param2");
        parameter2.setName("parameter2");
        parameter2.setValue("param_value2");
        uiCommand.getChildren().add(parameter1);
        uiCommand.getChildren().add(parameter2);
        System.out.println("Testing hyperlink with UIParameters...");
        hyperlinkRenderer.encodeBegin(getFacesContext(), uiCommand);
        hyperlinkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }

    public void verifyFormRenderer(UIComponent root,
				   int expectedFormNumber) throws IOException {
         boolean result = false;
         
        // Test FormRenderer.
        System.out.println("Testing FormRenderer");
        UIForm uiForm = new UIForm();
        uiForm.setId("formRenderer" + expectedFormNumber);
        //uiForm.setFormName("basicForm");
        root.getChildren().add(uiForm);

        FormRenderer formRenderer = new FormRenderer();
        // test decode method
        System.out.println("Testing decode method");
        
        formRenderer.decode(getFacesContext(), uiForm);
        
        // make sure formEvent was queued.
        System.out.println("Testing getApplicationEvent: ");
       
        // test encode method
        System.out.println("Testing encode method");
        formRenderer.encodeBegin(getFacesContext(), uiForm);
        getFacesContext().getResponseWriter().writeText("\n", null);
        
        // test encode method
        System.out.println("Testing encodeEnd method");
        formRenderer.encodeEnd(getFacesContext(), uiForm);
        getFacesContext().getResponseWriter().writeText("\n", null);
        
	// test that our form number is correct.
	Integer formNumber = (Integer)
	    uiForm.getAttributes().get(RIConstants.FORM_NUMBER_ATTR);
	assertTrue(null != formNumber);
	assertTrue(formNumber.intValue() == expectedFormNumber);
    }
    
    public void verifyButtonRenderer(UIComponent root) throws IOException {
         boolean result = false;
         
        // Test ButtonRenderer.
        System.out.println("Testing ButtonRenderer");
        UICommand uiCommand = new UICommand();
        ButtonRenderer buttonRenderer = new ButtonRenderer();

        // Test <Button> element rendering
        uiCommand = new UICommand();
        uiCommand.setId("imageButton");
        uiCommand.getAttributes().put("image", "duke.gif");
        uiCommand.getAttributes().put("type", "submit");
        uiCommand.setValue("SHOUD NOT BE HERE");
        root.getChildren().add(uiCommand);
        System.out.println("Testing encode (<button> rendering) method");
        getFacesContext().getResponseWriter().writeText("\n", null);
        buttonRenderer.encodeBegin(getFacesContext(), uiCommand);
        buttonRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test button as image with image specified in resource bundle
        uiCommand = new UICommand();
        uiCommand.setId("imageButton2");
        uiCommand.getAttributes().put("imageKey", "image.key");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        root.getChildren().add(uiCommand);
        System.out.println("Testing hyperlink image via resource lookup");
        buttonRenderer.encodeBegin(getFacesContext(), uiCommand);
        buttonRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

// ------------------  Test label determination ------------------------------
        // Provide attributes for all possible lookups
        // make sure that valueRef is returned and no others.
        uiCommand = new UICommand();
        uiCommand.setId("labelButton1");
        uiCommand.getAttributes().put("type", "submit");
        uiCommand.setValue("PASSED");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        uiCommand.getAttributes().put("key", "failed.key");
        uiCommand.setValueRef("TestBean.modelLabel");
        root.getChildren().add(uiCommand);
        System.out.println("Testing label lookup from local value...");
        buttonRenderer.encodeBegin(getFacesContext(), uiCommand);
        buttonRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // No value this round, ensure the valueRef for the button label
        // is pulled from the model
        uiCommand = new UICommand();
        uiCommand.setId("labelButton2");
        uiCommand.getAttributes().put("type", "reset");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        uiCommand.getAttributes().put("key", "failed.key");
        uiCommand.setValueRef("TestBean.modelLabel");
        root.getChildren().add(uiCommand);
        System.out.println("Testing label lookup from model...");
        buttonRenderer.encodeBegin(getFacesContext(), uiCommand);
        buttonRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // No valueRef or explicit label.  Pull value from the
        // specified ResourceBundle using the key
        uiCommand = new UICommand();
        uiCommand.setId("labelButton3");
        uiCommand.getAttributes().put("type", "submit");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        uiCommand.getAttributes().put("key", "passed.key");
        root.getChildren().add(uiCommand);
        System.out.println("Testing label lookup from ResourceBundle...");
        buttonRenderer.encodeBegin(getFacesContext(), uiCommand);
        buttonRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // All lookup methods fail, the value attribute should be empty
        uiCommand = new UICommand();
        uiCommand.setId("labelButton4");
        uiCommand.getAttributes().put("type", "reset");
        uiCommand.setValueRef("NonBean.label");
        uiCommand.getAttributes().put(RIConstants.BUNDLE_ATTR, "Messages");
        uiCommand.getAttributes().put("key", "non.key");
        root.getChildren().add(uiCommand);
        System.out.println("Testing empty label...");
        buttonRenderer.encodeBegin(getFacesContext(), uiCommand);
        buttonRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }
    
    public void verifyRadioRenderer(UIComponent root) throws IOException {
         boolean result = false;
         

        // Test RadioRenderer.
        System.out.println("Testing RadioRenderer");
        UISelectOne uiSelectOne = new UISelectOne();
	UISelectItems uiSelectItems = new UISelectItems();
        uiSelectOne.setId("radioRenderer");
        root.getChildren().add(uiSelectOne);

        SelectItem item1 = new SelectItem("One", "One",null);
        SelectItem item2 = new SelectItem("Two", "Two", null);
        SelectItem item3 = new SelectItem("Three", "Three" ,null);
        
        SelectItem[] items = {item1, item2,item3};
        uiSelectItems.setValue(items);
	uiSelectItems.setId("items");
	uiSelectOne.getChildren().add(uiSelectItems);
        
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

class Messages_en extends ListResourceBundle {
    /**
     * See class description.
     */
    protected Object[][] getContents() {
        return new Object[][] {
            { "failed.key", "RES-BUNDLE FAILED" },
            { "passed.key", "RES-BUNDLE PASSED" },
            { "image.key", "resduke.gif" }
        };
    }
}
