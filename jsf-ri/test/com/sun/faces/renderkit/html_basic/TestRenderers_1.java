/*
 * $Id: TestRenderers_1.java,v 1.65 2005/05/21 00:01:05 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_1.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.RIConstants;
import com.sun.faces.application.ViewHandlerImpl;
import com.sun.faces.util.Util;

import org.apache.cactus.WebRequest;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIViewRoot;
import javax.el.ValueExpression;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Test encode and decode methods in Renderer classes.
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_1.java,v 1.65 2005/05/21 00:01:05 jayashri Exp $
 */

public class TestRenderers_1 extends JspFacesTestCase {

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

        UIViewRoot xmlView = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        xmlView.setViewId("viewId");
        getFacesContext().setViewRoot(xmlView);
        assertTrue(getFacesContext().getResponseWriter() != null);
        
        // Spoof a loadBundle action...
        ResourceBundle bundle = new Messages_en();
        if (null == bundle) {
            return;
        }

        HashMap toStore = new HashMap();
        Enumeration keys = bundle.getKeys();
        String key = null;
        while (keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            toStore.put(key, bundle.getString(key));
        }
        getFacesContext().getExternalContext().
            getRequestMap().put("Messages", toStore);
    }


    // Methods from FacesTestCase
    public boolean sendWriterToFile() {
        return true;
    }


    public String getExpectedOutputFilename() {
        return "CorrectRenderersResponse";
    }


    public static final String ignore[] = {
    };


    public String[] getLinesToIgnore() {
        return ignore;
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
    public void testRenderers() throws Exception {
//        com.sun.faces.util.DebugUtil.waitForDebugger();
        
        Map sessionMap = getFacesContext().getExternalContext().getSessionMap();
       
        // create a dummy root for the tree.
        UIViewRoot root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        root.setId("root");
        root.setViewId("/root");
        ViewHandlerImpl viewHandler = new ViewHandlerImpl();
        
        getFacesContext().getApplication().setViewHandler(viewHandler);
        getFacesContext().setViewRoot(root);
     /*   Object view = 
	    Util.getStateManager(getFacesContext()).saveSerializedView(getFacesContext());
	getFacesContext().getExternalContext().getRequestMap().put(RIConstants.SAVED_STATE, view);*/
        // Call this twice to test the multiple forms in a page logic.
        getFacesContext().getResponseWriter().startDocument();
        verifyFormRenderer(root, 0);
        verifyFormRenderer(root, 1);
        verifyRadioRenderer(root);
        verifyButtonRenderer(root);
        verifyLinkRenderer(root);
        getFacesContext().getResponseWriter().endDocument();

        assertTrue(verifyExpectedOutput());
        sessionMap.remove("Messages");
        String stringToCheck = "id=" + "\"" + "formRenderer0" + "\"";
        assertTrue(verifyExpectedStringInOutput(stringToCheck));

    }


    public void verifyLinkRenderer(UIComponent root) throws IOException {
        verifyOutputLinkRenderer(root);
        verifyCommandLinkRenderer(root);
    }


    public void verifyOutputLinkRenderer(UIComponent root) throws IOException {
        // Provide attributes for all possible lookups
        // make sure that valueRef is returned and no others.
        System.out.println("Testing Link Renderer...");
        LinkRenderer linkRenderer = new LinkRenderer();
        UIOutput uiOutput = new UIOutput();
        UIOutput output = new UIOutput();
        uiOutput.setId("labelLink1");
        uiOutput.setValue("hrefValue");
        output.setValue("PASSED");
        output.setValueExpression("value",
                               Util.getValueExpression("#{TestBean.modelLabel}"));
        uiOutput.getChildren().add(output);
        root.getChildren().add(uiOutput);
        System.out.println("Testing label lookup from local value...");
        linkRenderer.encodeBegin(getFacesContext(), uiOutput);
        linkRenderer.encodeChildren(getFacesContext(), uiOutput);
        linkRenderer.encodeEnd(getFacesContext(), uiOutput);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // No value this round, ensure the valueRef for the button label
        // is pulled from the model
        uiOutput = new UIOutput();
        uiOutput.setId("labelLink2");
        uiOutput.setValue("hrefValue");
        output = new UIOutput();
        output.setValueExpression("value",
                               Util.getValueExpression("#{TestBean.modelLabel}"));
        uiOutput.getChildren().add(output);
        root.getChildren().add(uiOutput);
        System.out.println("Testing label lookup from model...");
        linkRenderer.encodeBegin(getFacesContext(), uiOutput);
        linkRenderer.encodeChildren(getFacesContext(), uiOutput);
        linkRenderer.encodeEnd(getFacesContext(), uiOutput);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // No valueRef or explicit label.  Pull value from the
        // specified ResourceBundle using the key
        uiOutput = new UIOutput();
        output = new UIOutput();
        uiOutput.setId("labelLink3");
        uiOutput.setValue("hrefValue");
        ValueExpression vb = Util.getValueExpression("#{Messages.passedkey}");
        output.setValueExpression("value", vb);
        uiOutput.getChildren().add(output);
        root.getChildren().add(uiOutput);
        System.out.println("Testing label lookup from ResourceBundle...");
        linkRenderer.encodeBegin(getFacesContext(), uiOutput);
        linkRenderer.encodeChildren(getFacesContext(), uiOutput);
        linkRenderer.encodeEnd(getFacesContext(), uiOutput);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // All lookup methods fail, test of link should be empty
        
        uiOutput = new UIOutput();
        output = new UIOutput();
        uiOutput.setId("labelLink4");
        uiOutput.setValue("hrefValue");
        uiOutput.getAttributes().put("rel", "rel");
        uiOutput.getAttributes().put("rev", "rev");
        uiOutput.getAttributes().put("shape", "shape");
        uiOutput.getAttributes().put("coords", "coords");
        uiOutput.getAttributes().put("hreflang", "hreflang");
        output.setValueExpression("value",
                               Util.getValueExpression("#{NonBean.label}"));
        uiOutput.getChildren().add(output);
        root.getChildren().add(uiOutput);
        System.out.println("Testing empty label...");
        linkRenderer.encodeBegin(getFacesContext(), uiOutput);
        linkRenderer.encodeChildren(getFacesContext(), uiOutput);
        linkRenderer.encodeEnd(getFacesContext(), uiOutput);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test link as image
        uiOutput = new UIOutput();
        UIGraphic graphic = new UIGraphic();
        uiOutput.setId("linkImage");
        uiOutput.setValue("hrefValue");
        graphic.setValue("duke.gif");
        uiOutput.getChildren().add(graphic);
        root.getChildren().add(uiOutput);
        System.out.println("Testing link as image");
        linkRenderer.encodeBegin(getFacesContext(), uiOutput);
        linkRenderer.encodeChildren(getFacesContext(), uiOutput);
        linkRenderer.encodeEnd(getFacesContext(), uiOutput);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test link as image with image specified in resource bundle
        uiOutput = new UIOutput();
        graphic = new UIGraphic();
        uiOutput.setId("linkImage2");
        uiOutput.setValue("hrefValue");
        vb = Util.getValueExpression("#{Messages.imagekey}");
        graphic.setValueExpression("value", vb);
        uiOutput.getChildren().add(graphic);
        root.getChildren().add(uiOutput);
        System.out.println("Testing link image via resource lookup");
        linkRenderer.encodeBegin(getFacesContext(), uiOutput);
        linkRenderer.encodeChildren(getFacesContext(), uiOutput);
        linkRenderer.encodeEnd(getFacesContext(), uiOutput);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test link with parameters
        uiOutput = new UIOutput();
        uiOutput.setId("paramLink");
        uiOutput.setValue("hrefValue");
        root.getChildren().add(uiOutput);
        UIParameter parameter1 = new UIParameter();
        UIParameter parameter2 = new UIParameter();
        parameter1.setId("param1");
        parameter1.setName("parameter1");
        parameter1.setValue("param_value1");
        parameter2.setId("param2");
        parameter2.setName("parameter2");
        parameter2.setValue("param_value2");
        uiOutput.getChildren().add(parameter1);
        uiOutput.getChildren().add(parameter2);
        uiOutput.getChildren().add(graphic);
        System.out.println("Testing link with UIParameters...");
        linkRenderer.encodeBegin(getFacesContext(), uiOutput);
        linkRenderer.encodeChildren(getFacesContext(), uiOutput);
        linkRenderer.encodeEnd(getFacesContext(), uiOutput);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }


    public void verifyCommandLinkRenderer(UIComponent root) throws IOException {
        // Provide attributes for all possible lookups
        // make sure that valueRef is returned and no others.
        System.out.println("Testing Link Renderer...");
        LinkRenderer linkRenderer = new LinkRenderer();
        UICommand uiCommand = new UICommand();
        UIOutput output = new UIOutput();
        UIForm form = new UIForm();
        uiCommand.setId("labelLink1");
        output.setValue("PASSED");
        output.setValueExpression("value",
                               Util.getValueExpression("#{TestBean.modelLabel}"));
        uiCommand.getChildren().add(output);
        form.getChildren().add(uiCommand);
        root.getChildren().add(form);
        System.out.println("Testing label lookup from local value...");
        linkRenderer.encodeBegin(getFacesContext(), uiCommand);
        linkRenderer.encodeChildren(getFacesContext(), uiCommand);
        linkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // No value this round, ensure the valueRef for the button label
        // is pulled from the model
        uiCommand = new UICommand();
        uiCommand.setId("labelLink2");
        output = new UIOutput();
        output.setValueExpression("value",
                               Util.getValueExpression("#{TestBean.modelLabel}"));
        uiCommand.getChildren().add(output);
        form.getChildren().add(uiCommand);
        System.out.println("Testing label lookup from model...");
        linkRenderer.encodeBegin(getFacesContext(), uiCommand);
        linkRenderer.encodeChildren(getFacesContext(), uiCommand);
        linkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // No valueRef or explicit label.  Pull value from the
        // specified ResourceBundle using the key
        uiCommand = new UICommand();
        output = new UIOutput();
        uiCommand.setId("labelLink3");
        ValueExpression vb = Util.getValueExpression("#{Messages.passedkey}");
        output.setValueExpression("value", vb);
        uiCommand.getChildren().add(output);
        form.getChildren().add(uiCommand);
        System.out.println("Testing label lookup from ResourceBundle...");
        linkRenderer.encodeBegin(getFacesContext(), uiCommand);
        linkRenderer.encodeChildren(getFacesContext(), uiCommand);
        linkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // All lookup methods fail, test of link should be empty
        
        uiCommand = new UICommand();
        output = new UIOutput();
        uiCommand.setId("labelLink4");
        uiCommand.getAttributes().put("rel", "rel");
        uiCommand.getAttributes().put("rev", "rev");
        uiCommand.getAttributes().put("shape", "shape");
        uiCommand.getAttributes().put("coords", "coords");
        uiCommand.getAttributes().put("hreflang", "hreflang");
        output.setValueExpression("value",
                               Util.getValueExpression("#{NonBean.label}"));
        uiCommand.getChildren().add(output);
        form.getChildren().add(uiCommand);
        System.out.println("Testing empty label...");
        linkRenderer.encodeBegin(getFacesContext(), uiCommand);
        linkRenderer.encodeChildren(getFacesContext(), uiCommand);
        linkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test link as image
        uiCommand = new UICommand();
        UIGraphic graphic = new UIGraphic();
        uiCommand.setId("linkImage");
        graphic.setValue("duke.gif");
        uiCommand.getChildren().add(graphic);
        form.getChildren().add(uiCommand);
        System.out.println("Testing link as image");
        linkRenderer.encodeBegin(getFacesContext(), uiCommand);
        linkRenderer.encodeChildren(getFacesContext(), uiCommand);
        linkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test link as image with image specified in resource bundle
        uiCommand = new UICommand();
        graphic = new UIGraphic();
        uiCommand.setId("linkImage2");
        vb = Util.getValueExpression("#{Messages.imagekey}");
        graphic.setValueExpression("value", vb);
        uiCommand.getChildren().add(graphic);
        form.getChildren().add(uiCommand);
        System.out.println("Testing link image via resource lookup");
        linkRenderer.encodeBegin(getFacesContext(), uiCommand);
        linkRenderer.encodeChildren(getFacesContext(), uiCommand);
        linkRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // Test link with parameters
        uiCommand = new UICommand();
        uiCommand.setId("paramLink");
        uiCommand.setValue("link with parameters");
        form.getChildren().add(uiCommand);
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
        System.out.println("Testing link with UIParameters...");
        linkRenderer.encodeBegin(getFacesContext(), uiCommand);
        linkRenderer.encodeChildren(getFacesContext(), uiCommand);
        linkRenderer.encodeEnd(getFacesContext(), uiCommand);
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
        ValueExpression vb = Util.getValueExpression("#{Messages.imagekey}");
        uiCommand.setValueExpression("image", vb);
        root.getChildren().add(uiCommand);
        System.out.println("Testing link image via resource lookup");
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
        uiCommand.setValueExpression("value",
                                  Util.getValueExpression(
                                      "#{TestBean.modelLabel}"));
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
        uiCommand.setValueExpression("value",
                                  Util.getValueExpression(
                                      "#{TestBean.modelLabel}"));
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
        vb = Util.getValueExpression("#{Messages.passedkey}");
        uiCommand.setValueExpression("value", vb);
        root.getChildren().add(uiCommand);
        System.out.println("Testing label lookup from ResourceBundle...");
        buttonRenderer.encodeBegin(getFacesContext(), uiCommand);
        buttonRenderer.encodeEnd(getFacesContext(), uiCommand);
        getFacesContext().getResponseWriter().writeText("\n", null);

        // All lookup methods fail, the value attribute should be empty
        uiCommand = new UICommand();
        uiCommand.setId("labelButton4");
        uiCommand.getAttributes().put("type", "reset");
        uiCommand.setValueExpression("value",
                                  Util.getValueExpression("#{NonBean.label}"));
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
        uiSelectOne.getAttributes().put("enabledClass", "enabledClass");
        uiSelectOne.getAttributes().put("disabledClass", "disabledClass");
        uiSelectOne.getAttributes().put("styleClass", "styleClass");
        uiSelectOne.getAttributes().put("tabindex", new Integer(5));
        uiSelectOne.getAttributes().put("title", "title");
        UISelectItems uiSelectItems = new UISelectItems();
        uiSelectOne.setId("radioRenderer");
        root.getChildren().add(uiSelectOne);

        SelectItem item1 = new SelectItem("One", "One", null);
        item1.setDisabled(true);
        SelectItem item2 = new SelectItem("Two", "Two", null);
        SelectItem item3 = new SelectItem("Three", "Three", null);

        SelectItem item4 = new SelectItem("Four", "Four", null);
        SelectItem item5 = new SelectItem("Five", "Five", null);
        SelectItem[] itemsArray = {item4, item5};
        SelectItemGroup itemGroup = new SelectItemGroup("group", null, true,
                                                        itemsArray);
        SelectItem[] items = {item1, item2, item3, itemGroup};
        uiSelectItems.setValue(items);
        uiSelectItems.setId("items");
        uiSelectOne.getChildren().add(uiSelectItems);

        RadioRenderer radioRenderer = new RadioRenderer();
        // test decode method
        System.out.println("Testing decode method");
        radioRenderer.decode(getFacesContext(), uiSelectOne);
        assertTrue(((String) uiSelectOne.getSubmittedValue()).equals("Two"));

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
        return new Object[][]{
            {"failedkey", "RES-BUNDLE FAILED"},
            {"passedkey", "RES-BUNDLE PASSED"},
            {"imagekey", "resduke.gif"}
        };
    }
}
