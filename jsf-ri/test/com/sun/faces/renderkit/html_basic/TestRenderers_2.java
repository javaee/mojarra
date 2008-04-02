/*
 * $Id: TestRenderers_2.java,v 1.32 2002/10/11 00:58:36 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_2.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.renderkit.html_basic.CheckboxRenderer;
import com.sun.faces.renderkit.html_basic.NumberRenderer;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.tree.XmlTreeImpl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import javax.faces.component.SelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.MessageImpl;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.TestBean;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_2.java,v 1.32 2002/10/11 00:58:36 jvisvanathan Exp $
 * 
 *
 */

public class TestRenderers_2 extends JspFacesTestCase
{
    //
    // Protected Constants
    //

    public static String DATE_STR = "Jan 12, 1952";
    public static String DATE_STR_LONG = "Sat, Jan 12, 1952 AD at 12:31:31 PM";
    
    public static String TIME_STR = "12:31:31 PM";
   public static String NUMBER_STR = "47%";
   public static String NUMBER_STR_PATTERN = "1999.8765432";
        
    public boolean sendWriterToFile() {
        return true;
    }    

    public String getExpectedOutputFilename() {
        return "CorrectRenderersResponse_2";
    }

    public String [] getLinesToIgnore() {
        String[] lines =  {
	    "<a href=\"/test/faces;jsessionid=4573B0C6B316F9D0D252D46330E31063?action=command&name=HyperlinkRenderer&tree=treeId\">HyperlinkRenderer</a>",
	    "<img src=\"\">",
	    "<img src=\";jsessionid=614035E9A2D45743F4E803A0B536E386\">",
	    "<img src=\";jsessionid=614035E9A2D45743F4E803A0B536E386\">"

};
        return lines;
    }   
 
    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private FacesContextFactory  facesContextFactory = null;

    // Attribute Instance Variables
    // Relationship Instance Variables
    //
    // Constructors and Initializers    
    //

    public TestRenderers_2() {super("TestRenderers_2");}
    public TestRenderers_2(String name) {super(name);}

    //
    // Class methods
    //

    //
    // Methods from TestCase
    //
    public void setUp() {
	super.setUp();

        XmlTreeImpl xmlTree = 
	    new XmlTreeImpl(getFacesContext(), 
			    new UICommand(), "treeId", "");
        getFacesContext().setResponseTree(xmlTree);
	assertTrue(null != getFacesContext().getResponseWriter());
    }     

    public void beginRenderers(WebRequest theRequest) {
        // for CheckboxRenderer
        theRequest.addParameter("/my_checkbox_on", "on");
        theRequest.addParameter("/my_checkbox_yes", "yes");
        theRequest.addParameter("/my_checkbox_true", "true");
        // for HyperlinkRenderer
        theRequest.addParameter("action", "command");
        theRequest.addParameter("name", "HyperlinkRenderer");
        // for Listbox
        theRequest.addParameter("/my_listbox", "Blue");
        // for TextEntry_Secret
        theRequest.addParameter("/my_secret", "secret");
        // for Text
        theRequest.addParameter("/my_input_text", "text");

        // for Text - NumberFormat
        theRequest.addParameter("/my_number", "47%");
        theRequest.addParameter("/my_number2", "1999.8769432");

        theRequest.addParameter("/my_input_date", DATE_STR);
        theRequest.addParameter("/my_input_date2", DATE_STR_LONG);
        theRequest.addParameter("/my_output_date", DATE_STR);
        theRequest.addParameter("/my_input_time", TIME_STR);
        theRequest.addParameter("/my_output_time", TIME_STR);
        theRequest.addParameter("/my_output_date2", DATE_STR_LONG);
        theRequest.addParameter("/my_output_text", "text");

        theRequest.addParameter("/my_textarea", "TextAreaRenderer");

        theRequest.addParameter("/my_graphic_image", "graphicimage");

        theRequest.addParameter("/my_output_errors", "outputerrors");

        theRequest.addParameter("/my_output_message", "outputmessage");
    } 

    public void testRenderers() {

        try {
            // create a dummy root for the tree.
            UIComponentBase root = new UIComponentBase() {
                public String getComponentType() { return "root"; }
            };
            root.setComponentId("root");

            testCheckboxRenderer(root);
            testHyperlinkRenderer(root);
            testListboxRenderer(root);
            testSecretRenderer(root);
            testInputTextRenderer(root);
            testOutputTextRenderer(root);
	    testInputDateRenderer(root);
	    testInputTimeRenderer(root);
	    testInputDateTimeRendererWithPattern(root);

            testOutputDateRenderer(root);
            testOutputTimeRenderer(root);
	    testOutputDateTimeRendererWithPattern(root);

            testTextAreaRenderer(root);

            testInputNumberRenderer(root);
	    testInputNumberRendererWithPattern(root);
            
            testOutputNumberRenderer(root);
	    testOutputNumberRendererWithPattern(root);

            testGraphicImageRenderer(root);

            testOutputErrorsRenderer(root);

            testOutputMessageRenderer(root);
            testInputNumberRendererWithCharacter(root);
            assertTrue(verifyExpectedOutput());
        } catch (Throwable t) {
            t.printStackTrace();
            assertTrue(false);
            return;
        }
    }
    
    //
    // General Methods
    //
    public void testCheckboxRenderer(UIComponent root) throws IOException {
        System.out.println("Testing CheckboxRenderer");
        UISelectBoolean selectBoolean = new UISelectBoolean();
        selectBoolean.setValue(null);
        selectBoolean.setComponentId("my_checkbox");
        root.addChild(selectBoolean);
             
        CheckboxRenderer checkboxRenderer = new CheckboxRenderer();

        // First test no parameter coming in - (the checkbox
        // is not checked)
         
        // test decode method

        System.out.println("    Testing decode method - no parameter");
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        Boolean val = (Boolean)selectBoolean.getValue();
        assertTrue(!val.booleanValue());

        // Test parameter coming in - (the checkbox has been checked)

        // test decode method

        System.out.println("    Testing decode method - parameter (on)");
        selectBoolean.setComponentId("my_checkbox_on");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean); 
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        

        // test decode method

        System.out.println("    Testing decode method - parameter (yes)");
        selectBoolean.setComponentId("my_checkbox_yes");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
            
        // test decode method

        System.out.println("    Testing decode method - parameter (true)");
        selectBoolean.setComponentId("my_checkbox_true");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
            
        // test encode method

        System.out.println("    Testing encode method - rendering checked");
        selectBoolean.setComponentId("my_checkbox");
        selectBoolean.setSelected(true);
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().write("\n");

        System.out.println("    Testing encode method - rendering unchecked");
        selectBoolean.setSelected(false);
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().write("\n");

        System.out.println("    Testing encode method - rendering unchecked with label");
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().write("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = checkboxRenderer.supportsComponentType("javax.faces.component.UISelectBoolean");
        assertTrue(result);
        result = checkboxRenderer.supportsComponentType(selectBoolean);
        assertTrue(result);
        result = checkboxRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testHyperlinkRenderer(UIComponent root) throws IOException {
        System.out.println("Testing HyperlinkRenderer");
        UICommand command = new UICommand();
        command.setValue("HyperlinkRenderer");
        command.setComponentId("my_command");
        root.addChild(command);

        HyperlinkRenderer hyperlinkRenderer = new HyperlinkRenderer();

        System.out.println("    Testing decode method...");
        hyperlinkRenderer.decode(getFacesContext(), command);

        // Verify command event was set for the application..
        System.out.println("    Testing added application event (commandEvent)..");
        Iterator iter = getFacesContext().getApplicationEvents();
        assertTrue(iter != null); 

        // Test encode method

        System.out.println("    Testing encode method...");
        hyperlinkRenderer.encodeBegin(getFacesContext(), command);
         hyperlinkRenderer.encodeEnd(getFacesContext(), command);
        getFacesContext().getResponseWriter().write("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = hyperlinkRenderer.supportsComponentType("javax.faces.component.UICommand");
        assertTrue(result);
        result = hyperlinkRenderer.supportsComponentType(command);
        assertTrue(result);
        result = hyperlinkRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
        
    }

    public void testListboxRenderer(UIComponent root) throws IOException {
        System.out.println("Testing ListboxRenderer");
        UISelectOne selectOne = new UISelectOne();
	UISelectItems uiSelectItems = new UISelectItems();
        selectOne.setValue(null);
        selectOne.setComponentId("my_listbox");
        SelectItem item1 = new SelectItem("Red", "Red", null);
        SelectItem item2 = new SelectItem("Blue", "Blue", null);
        SelectItem item3 = new SelectItem("Green", "Green", null);
        SelectItem item4 = new SelectItem("Yellow", "Yellow", null);
        SelectItem[] selectItems = {item1, item2, item3, item4};
        uiSelectItems.setValue(selectItems);
	uiSelectItems.setComponentId("items");
	selectOne.addChild(uiSelectItems);
        root.addChild(selectOne);

        ListboxRenderer listboxRenderer = new ListboxRenderer();

        // test decode method

        System.out.println("    Testing decode method... ");
        listboxRenderer.decode(getFacesContext(), selectOne);
        String value = (String)selectOne.getValue();
        assertTrue(value.equals("Blue"));

        // test encode method

        System.out.println("    Testing encode method... ");
        selectOne.setComponentId("my_listbox");
        selectOne.setValue("Blue");
        listboxRenderer.encodeBegin(getFacesContext(), selectOne);
        listboxRenderer.encodeEnd(getFacesContext(), selectOne);
        getFacesContext().getResponseWriter().write("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = listboxRenderer.supportsComponentType("javax.faces.component.UISelectOne");
        assertTrue(result);
        result = listboxRenderer.supportsComponentType(selectOne);
        assertTrue(result);
        result = listboxRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testSecretRenderer(UIComponent root) throws IOException {
        System.out.println("Testing SecretRenderer");
        UIInput textEntry = new UIInput();
        textEntry.setValue(null);
        textEntry.setComponentId("my_secret");
        root.addChild(textEntry);

        SecretRenderer secretRenderer = new SecretRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        secretRenderer.decode(getFacesContext(), textEntry);
        assertTrue(((String)textEntry.getValue()).equals("secret"));

        // test encode method

        System.out.println("    Testing encode method...");
        secretRenderer.encodeBegin(getFacesContext(), textEntry);
        secretRenderer.encodeEnd(getFacesContext(), textEntry);
        getFacesContext().getResponseWriter().write("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = secretRenderer.supportsComponentType("javax.faces.component.UIInput");
        assertTrue(result);
        result = secretRenderer.supportsComponentType(textEntry);
        assertTrue(result);
        result = secretRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testInputTextRenderer(UIComponent root) throws IOException {
        System.out.println("Testing InputTextRenderer");
        UIInput text = new UIInput();
        text.setValue(null);
        text.setComponentId("my_input_text");
        root.addChild(text);

        TextRenderer textRenderer = new TextRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        textRenderer.decode(getFacesContext(), text);
        assertTrue(((String)text.getValue()).equals("text"));

        // test encode method

        System.out.println("    Testing encode method...");
        textRenderer.encodeBegin(getFacesContext(), text);
        textRenderer.encodeEnd(getFacesContext(), text);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = textRenderer.supportsComponentType("javax.faces.component.UIInput");
        assertTrue(result);
        result = textRenderer.supportsComponentType(text);
        assertTrue(result);
        result = textRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testOutputTextRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OutputTextRenderer");
        UIOutput text = new UIOutput();
        text.setValue(null);
        text.setComponentId("my_output_text");
        root.addChild(text);

        TextRenderer textRenderer = new TextRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        textRenderer.decode(getFacesContext(), text);

        // test encode method

        System.out.println("    Testing encode method...");
        textRenderer.encodeBegin(getFacesContext(), text);
        textRenderer.encodeEnd(getFacesContext(), text);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = textRenderer.supportsComponentType("javax.faces.component.UIOutput");
        assertTrue(result);
        result = textRenderer.supportsComponentType(text);
        assertTrue(result);
        result = textRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testGraphicImageRenderer(UIComponent root) throws IOException {
        System.out.println("Testing GraphicImageRenderer");
        UIGraphic img = new UIGraphic();
        img.setURL("/nonModelReferenceImage.gif");
        img.setComponentId("my_graphic_image");
        root.addChild(img);

        ImageRenderer imageRenderer = new ImageRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        imageRenderer.decode(getFacesContext(), img);

        // test encode method

        System.out.println("    Testing encode method...");
        imageRenderer.encodeBegin(getFacesContext(), img);
        imageRenderer.encodeEnd(getFacesContext(), img);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = imageRenderer.supportsComponentType("javax.faces.component.UIGraphic");
        assertTrue(result);
        result = imageRenderer.supportsComponentType(img);
        assertTrue(result);
        result = imageRenderer.supportsComponentType("FooBar");
        assertTrue(!result);

        System.out.println("    Testing graphic support of modelReference...");
	img = new UIGraphic();
	TestBean testBean = (TestBean) 
	    getFacesContext().getHttpSession().getAttribute("TestBean");
	assertTrue(null != testBean); // set in FacesTestCaseService
	testBean.setImagePath("/foo/modelReferenceImage.gif");
	img.setModelReference("TestBean.imagePath");

        imageRenderer.encodeBegin(getFacesContext(), img);
        imageRenderer.encodeEnd(getFacesContext(), img);
        getFacesContext().getResponseWriter().flush();

    }

    public void testOutputErrorsRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OutputErrorsRenderer");
        UIOutput output = new UIOutput();
        output.setComponentId("my_output_errors");
        root.addChild(output);

        ErrorsRenderer errorsRenderer = new ErrorsRenderer();

        // populate facescontext with some errors
        // specifically, add a "global" message and
        // "component" message. 

        getFacesContext().addMessage(null,new MessageImpl(1,
            "global message summary", "global message detail"));
        getFacesContext().addMessage(output, new MessageImpl(1,
            "component message summary", "component message detail"));

        // test encode method

        System.out.println("    Testing encode method...");
        errorsRenderer.encodeBegin(getFacesContext(), output);
        errorsRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = errorsRenderer.supportsComponentType("javax.faces.component.UIOutput");
        assertTrue(result);
        result = errorsRenderer.supportsComponentType(output);
        assertTrue(result);
        result = errorsRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testOutputMessageRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OutputMessageRenderer");
        UIOutput output = new UIOutput();
        output.setComponentId("my_output_message");
        output.setValue("My name is {0} {1}");
        UIParameter param1, param2 = null;
        param1 = new UIParameter();
        param1.setComponentId("p1");
        param2 = new UIParameter();
        param2.setComponentId("p2");
        param1.setValue("Bobby");
        param2.setValue("Orr");
        output.addChild(param1);
        output.addChild(param2);
        root.addChild(output);

        MessageRenderer messageRenderer = new MessageRenderer();

        // test encode method

        System.out.println("    Testing encode method...");

        messageRenderer.encodeBegin(getFacesContext(), output);
        messageRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = messageRenderer.supportsComponentType(
            "javax.faces.component.UIOutput");
        assertTrue(result);
        result = messageRenderer.supportsComponentType(output);
        assertTrue(result);
        result = messageRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }
        

    public void testInputDateRenderer(UIComponent root) throws IOException {
        System.out.println("Testing Input_DateRenderer");
        UIInput input = new UIInput();
        input.setValue(null);
        input.setComponentId("my_input_date");
	input.setAttribute("dateStyle", "medium");
        root.addChild(input);

        DateRenderer dateRenderer = new DateRenderer();
	Date date = null;
	DateFormat formatter = 
	    DateFormat.getDateInstance(DateFormat.MEDIUM,
				       getFacesContext().getLocale());
	
        // test decode method
	
        System.out.println("    Testing decode method...");

        dateRenderer.decode(getFacesContext(), input);
	date = (Date) input.getValue();
	assertTrue(null != date);
	assertTrue(DATE_STR.equals(formatter.format(date)));

        // test encode method

        System.out.println("    Testing encode method...");
        dateRenderer.encodeBegin(getFacesContext(), input);
        dateRenderer.encodeEnd(getFacesContext(), input);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = dateRenderer.supportsComponentType("javax.faces.component.UIInput");
        assertTrue(result);
        result = dateRenderer.supportsComponentType(input);
        assertTrue(result);
        result = dateRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testInputTimeRenderer(UIComponent root) throws IOException {
        System.out.println("Testing Input_TimeRenderer");
        UIInput input = new UIInput();
        input.setValue(null);
        input.setComponentId("my_input_time");
	input.setAttribute("timeStyle", "medium");
        root.addChild(input);

        TimeRenderer timeRenderer = new TimeRenderer();
	Date date = null;
	DateFormat formatter = 
	    DateFormat.getTimeInstance(DateFormat.MEDIUM,
				       getFacesContext().getLocale());
	
        // test decode method
	
        System.out.println("    Testing decode method...");

        timeRenderer.decode(getFacesContext(), input);
	date = (Date) input.getValue();
	assertTrue(null != date);
	String what = formatter.format(date);
	assertTrue(TIME_STR.equals(formatter.format(date)));

        // test encode method

        System.out.println("    Testing encode method...");
        timeRenderer.encodeBegin(getFacesContext(), input);
        timeRenderer.encodeEnd(getFacesContext(), input);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = timeRenderer.supportsComponentType("javax.faces.component.UIInput");
        assertTrue(result);
        result = timeRenderer.supportsComponentType(input);
        assertTrue(result);
        result = timeRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testInputDateTimeRendererWithPattern(UIComponent root) throws IOException {
        System.out.println("Testing Input_DateRenderer With Pattern");
	String formatPattern = "EEE, MMM d, yyyy G 'at' hh:mm:ss a";
        UIInput input = new UIInput();
        input.setValue(null);
        input.setComponentId("my_input_date2");
	input.setAttribute("formatPattern", formatPattern);
        root.addChild(input);

        DateTimeRenderer dateTimeRenderer = new DateTimeRenderer();
	Date date = null;
	SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
	
        // test decode method
	
        System.out.println("    Testing decode method...");

        dateTimeRenderer.decode(getFacesContext(), input);
	date = (Date) input.getValue();
	assertTrue(null != date);
	assertTrue(DATE_STR_LONG.equals(formatter.format(date)));

        // test encode method

        System.out.println("    Testing encode method...");
        dateTimeRenderer.encodeBegin(getFacesContext(), input);
        dateTimeRenderer.encodeEnd(getFacesContext(), input);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = dateTimeRenderer.supportsComponentType("javax.faces.component.UIInput");
        assertTrue(result);
        result = dateTimeRenderer.supportsComponentType(input);
        assertTrue(result);
        result = dateTimeRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testTextAreaRenderer(UIComponent root) throws IOException {
        System.out.println("Testing TextAreaRenderer");
        UIInput textEntry = new UIInput();
        textEntry.setValue(null);
        textEntry.setComponentId("my_textarea");
        root.addChild(textEntry);

        TextAreaRenderer textAreaRenderer = new TextAreaRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        textAreaRenderer.decode(getFacesContext(), textEntry);
        assertTrue(((String)textEntry.getValue()).equals("TextAreaRenderer"));

        // test encode method

        System.out.println("    Testing encode method...");
        textAreaRenderer.encodeBegin(getFacesContext(), textEntry);
        textAreaRenderer.encodeEnd(getFacesContext(), textEntry);
        getFacesContext().getResponseWriter().write("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = textAreaRenderer.supportsComponentType("javax.faces.component.UIInput");
        assertTrue(result);
        result = textAreaRenderer.supportsComponentType(textEntry);
        assertTrue(result);
        result = textAreaRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }
    
    public void testInputNumberRenderer(UIComponent root) throws IOException {
        System.out.println("Testing NumberRenderer for UIInput ");
        UIInput input = new UIInput();
        input.setValue(null);
        input.setComponentId("my_number");
	input.setAttribute("numberStyle", "percent");
        root.addChild(input);

        NumberRenderer numberRenderer = new NumberRenderer();
	Number number = null;
	NumberFormat formatter = 
	    NumberFormat.getPercentInstance(getFacesContext().getLocale());
	
        // test decode method
	
        System.out.println("    Testing decode method...");

        numberRenderer.decode(getFacesContext(), input);
	number = (Number) input.getValue();
	assertTrue(null != number);
	assertTrue(NUMBER_STR.equals(formatter.format(number)));

        // test encode method

        System.out.println("    Testing encode method...");
        numberRenderer.encodeBegin(getFacesContext(), input);
        numberRenderer.encodeEnd(getFacesContext(), input);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = numberRenderer.supportsComponentType("javax.faces.component.UIInput");
        assertTrue(result);
        result = numberRenderer.supportsComponentType(input);
        assertTrue(result);
        result = numberRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }
    
    public void testInputNumberRendererWithPattern(UIComponent root) 
            throws IOException {
        System.out.println("Testing NumberRenderer With Pattern for UIInput ");
	String formatPattern = "####.000";
        UIInput input = new UIInput();
        input.setValue(null);
        input.setComponentId("my_number2");
	input.setAttribute("formatPattern", formatPattern);
        root.addChild(input);

        NumberRenderer numberRenderer = new NumberRenderer();
	Number number = null;
	DecimalFormat formatter = new DecimalFormat(formatPattern);
	
        // test decode method
	
        System.out.println("    Testing decode method...");

        numberRenderer.decode(getFacesContext(), input);
	number= (Number) input.getValue();
	assertTrue(null != number);
	assertTrue("1999.877".equals(formatter.format(number)));

        // test encode method

        System.out.println("    Testing encode method...");
        numberRenderer.encodeBegin(getFacesContext(), input);
        numberRenderer.encodeEnd(getFacesContext(), input);
        getFacesContext().getResponseWriter().write("\n");
        getFacesContext().getResponseWriter().flush();
    }    

    public void testOutputNumberRenderer(UIComponent root) throws IOException {
        System.out.println("Testing NumberRenderer for UIOutput");
        UIOutput output = new UIOutput();
        output.setValue(new Double(.99));
        output.setComponentId("my_number3");
	output.setAttribute("numberStyle", "percent");
        root.addChild(output);

        NumberRenderer numberRenderer = new NumberRenderer();
	Number number = null;
	NumberFormat formatter = 
	    NumberFormat.getPercentInstance(getFacesContext().getLocale());
	
        // test encode method
        System.out.println("    Testing encode method...");
        numberRenderer.encodeBegin(getFacesContext(), output);
        numberRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().write("\n");
        getFacesContext().getResponseWriter().flush();

        System.out.println(" Testing supportsComponentType methods..");

        boolean result = false;
        result = numberRenderer.supportsComponentType("javax.faces.component.UIInput");
        assertTrue(result);
        result = numberRenderer.supportsComponentType(output);
        assertTrue(result);
        result = numberRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }
    
    public void testOutputNumberRendererWithPattern(UIComponent root) throws IOException {
        System.out.println("Testing NumberRenderer With Pattern for UIOutput");
	String formatPattern = "####.000";
        UIOutput output = new UIOutput();
        output.setValue(new Double(999));
        output.setComponentId("my_number4");
	output.setAttribute("formatPattern", formatPattern);
        root.addChild(output);

        NumberRenderer numberRenderer = new NumberRenderer();
	Number number = null;
	DecimalFormat formatter = new DecimalFormat(formatPattern);
	
        // test encode method
        System.out.println("    Testing encode method...");
        numberRenderer.encodeBegin(getFacesContext(), output);
        numberRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().write("\n");
        getFacesContext().getResponseWriter().flush();
    }  


    public void testOutputDateRenderer(UIComponent root) throws IOException {
        System.out.println("Testing Output_DateRenderer");
        UIOutput output = new UIOutput();
        output.setComponentId("my_output_date");
	output.setAttribute("dateStyle", "medium");
	output.setValue(DATE_STR);
        root.addChild(output);

        DateRenderer dateRenderer = new DateRenderer();
	Date date = null;
	DateFormat formatter = 
	    DateFormat.getDateInstance(DateFormat.MEDIUM,
				       getFacesContext().getLocale());
	
        // test decode method
	
        System.out.println("    Testing decode method...");

        dateRenderer.decode(getFacesContext(), output);

        // test encode method

        System.out.println("    Testing encode method...");
        dateRenderer.encodeBegin(getFacesContext(), output);
        dateRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = dateRenderer.supportsComponentType("javax.faces.component.UIOutput");
        assertTrue(result);
        result = dateRenderer.supportsComponentType(output);
        assertTrue(result);
        result = dateRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testOutputTimeRenderer(UIComponent root) throws IOException {
        System.out.println("Testing Output_TimeRenderer");
        UIOutput output = new UIOutput();
        output.setComponentId("my_output_time");
	output.setAttribute("timeStyle", "medium");
	output.setValue(TIME_STR);
        root.addChild(output);

        TimeRenderer timeRenderer = new TimeRenderer();
	Date date = null;
	DateFormat formatter = 
	    DateFormat.getTimeInstance(DateFormat.MEDIUM,
				       getFacesContext().getLocale());
	
        // test decode method
	
        System.out.println("    Testing decode method...");

        timeRenderer.decode(getFacesContext(), output);

        // test encode method

        System.out.println("    Testing encode method...");
        timeRenderer.encodeBegin(getFacesContext(), output);
        timeRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = timeRenderer.supportsComponentType("javax.faces.component.UIOutput");
        assertTrue(result);
        result = timeRenderer.supportsComponentType(output);
        assertTrue(result);
        result = timeRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testOutputDateTimeRendererWithPattern(UIComponent root) throws IOException {
        System.out.println("Testing DateRenderer With Pattern");
	String formatPattern = "EEE, MMM d, yyyy G 'at' hh:mm:ss a";
        UIOutput output = new UIOutput();
        output.setValue(DATE_STR_LONG);
        output.setComponentId("my_output_date2");
	output.setAttribute("formatPattern", formatPattern);
        root.addChild(output);

        DateTimeRenderer dateRenderer = new DateTimeRenderer();
	Date date = null;
	SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
	
        // test decode method
	
        System.out.println("    Testing decode method...");

        dateRenderer.decode(getFacesContext(), output);

        // test encode method

        System.out.println("    Testing encode method...");
        dateRenderer.encodeBegin(getFacesContext(), output);
        dateRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = dateRenderer.supportsComponentType("javax.faces.component.UIOutput");
        assertTrue(result);
        result = dateRenderer.supportsComponentType(output);
        assertTrue(result);
        result = dateRenderer.supportsComponentType("FooBar");
        assertTrue(!result);

	// testing encode with invalid date
	output.setValue("invalidDate");
        System.out.println("    Testing encode method...");
        dateRenderer.encodeBegin(getFacesContext(), output);
        dateRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().write("\n");
        getFacesContext().getResponseWriter().flush();
	assertTrue(!output.isValid());
	
    }
    
    public void testInputNumberRendererWithCharacter(UIComponent root) 
            throws IOException {
        System.out.println("Testing NumberRenderer With Character input ");
	UIInput input = new UIInput();
        char testchar='9';
        input.setValue(new Character(testchar));
        input.setComponentId("my_character");
	
        NumberRenderer numberRenderer = new NumberRenderer();
	String expectedStr = numberRenderer.getCurrentValue(getFacesContext(),input);
        assertTrue(expectedStr.equals("9"));
    } 

} // end of class TestRenderers2_
