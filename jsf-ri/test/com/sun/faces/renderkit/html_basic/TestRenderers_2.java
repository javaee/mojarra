/*
 * $Id: TestRenderers_2.java,v 1.50 2003/08/21 14:18:24 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_2.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.renderkit.html_basic.CheckboxRenderer;
import com.sun.faces.renderkit.html_basic.NumberRenderer;
import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import javax.faces.component.UISelectItems;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIPage;
import javax.faces.component.base.UISelectBooleanBase;
import javax.faces.component.base.UICommandBase;
import javax.faces.component.base.UISelectItemsBase;
import javax.faces.component.base.UISelectOneBase;
import javax.faces.component.base.UIInputBase;
import javax.faces.component.base.UIOutputBase;
import javax.faces.component.base.UIGraphicBase;
import javax.faces.component.base.UIParameterBase;
import javax.faces.component.base.UINamingContainerBase;
import javax.faces.component.base.UIPageBase;
import javax.faces.FactoryFinder;
import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.application.MessageImpl;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.TestBean;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_2.java,v 1.50 2003/08/21 14:18:24 rlubke Exp $
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
	    "<img id=\"my_graphic_image\" src=\"/test/nonModelReferenceImage.gif;jsessionid=41647E428768CD64158970BF4CD4E59D\"><img id=\"id0\" src=\"/test/foo/modelReferenceImage.gif;jsessionid=41647E428768CD64158970BF4CD4E59D\">My name is Bobby Orr"
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
        UIPage page = new UIPageBase();
        page.setTreeId("treeId");       
        getFacesContext().setRoot(page);
	assertTrue(null != getFacesContext().getResponseWriter());
    }     

    public void beginRenderers(WebRequest theRequest) {
        // for CheckboxRenderer
        theRequest.addParameter("my_checkbox_on", "on");
        theRequest.addParameter("my_checkbox_yes", "yes");
        theRequest.addParameter("my_checkbox_true", "true");
  
        // for HyperlinkRenderer
        theRequest.addParameter("action", "command");
        theRequest.addParameter("my_command", "HyperlinkRenderer");
        // for Listbox
        theRequest.addParameter("my_listbox", "100");
        // for TextEntry_Secret
        theRequest.addParameter("my_secret", "secret");
        // for Text
        theRequest.addParameter("my_input_text", "text");

        // for Text - NumberFormat
        theRequest.addParameter("my_number", "47%");
        theRequest.addParameter("my_number2", "1999.8769432");

        theRequest.addParameter("my_input_date", DATE_STR);
        theRequest.addParameter("my_input_date2", DATE_STR_LONG);
        theRequest.addParameter("my_output_date", DATE_STR);
        theRequest.addParameter("my_input_time", TIME_STR);
        theRequest.addParameter("my_output_time", TIME_STR);
        theRequest.addParameter("my_output_date2", DATE_STR_LONG);
        theRequest.addParameter("my_output_text", "text");

        theRequest.addParameter("my_textarea", "TextAreaRenderer");

        theRequest.addParameter("my_graphic_image", "graphicimage");

        theRequest.addParameter("my_output_errors", "outputerrors");

        theRequest.addParameter("my_output_message", "outputmessage");
    } 

    public void testRenderers() {

        try {
            // create a dummy root for the tree.
            UINamingContainerBase root = new UINamingContainerBase() {
                public String getComponentType() { return "root"; }
            };
            root.setId("root");

            testCheckboxRenderer(root);
            // PENDING (visvan) revisit this test case once HyperLinkRenderer
            // is fixed.
            // testHyperlinkRenderer(root);
            getFacesContext().getResponseWriter().startDocument();
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
            // order in which the messages are output is indeterminant since
            // the messages are stored in a HashMap. so its difficult test
            // using the golden file approach.
            // testOutputErrorsRenderer(root);
            testOutputMessageRenderer(root);
            testInputNumberRendererWithCharacter(root);
            getFacesContext().getResponseWriter().endDocument();
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
        UISelectBoolean selectBoolean = new UISelectBooleanBase();
        selectBoolean.setValue(null);
        selectBoolean.setId("my_checkbox");
        root.getChildren().add(selectBoolean);
             
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
        selectBoolean.setId("my_checkbox_on");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean); 
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        

        // test decode method

        System.out.println("    Testing decode method - parameter (yes)");
        selectBoolean.setId("my_checkbox_yes");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
       
        // test decode method

        System.out.println("    Testing decode method - parameter (true)");
        selectBoolean.setId("my_checkbox_true");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        
        // test decode method
        
        System.out.println("    Testing decode method - parameter (true)");
        selectBoolean.setId("my_checkbox_true");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        
         
        // test decode method with checkbox disabled.
        System.out.println("    Testing decode method - parameter (yes)");
        selectBoolean.setId("mycheckbox_disabled");
        selectBoolean.setAttribute("disabled", "true");
        selectBoolean.setValue(Boolean.TRUE);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        // make sure the value wasn't set to false. Bug id  4883159
        assertTrue(val.booleanValue());
        selectBoolean.setAttribute("disabled", null);
        
        // test encode method
        System.out.println("    Testing encode method - rendering checked");
        selectBoolean.setId("my_checkbox");
        selectBoolean.setSelected(true);
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().writeText("\n");

        System.out.println("    Testing encode method - rendering unchecked");
        selectBoolean.setSelected(false);
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().writeText("\n");

        System.out.println("    Testing encode method - rendering unchecked with label");
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().writeText("\n");
    }

    public void testHyperlinkRenderer(UIComponent root) throws IOException {
        System.out.println("Testing HyperlinkRenderer");
        UICommand command = new UICommandBase();
        command.setId("my_command");
        command.setRendererType("Hyperlink");
        root.getChildren().add(command);

        HyperlinkRenderer hyperlinkRenderer = new HyperlinkRenderer();

        System.out.println("    Testing decode method...");
        hyperlinkRenderer.decode(getFacesContext(), command);

        // Verify command event was set for the application..
        System.out.println("    Testing added application event (commandEvent)..");
        Iterator iter = getFacesContext().getFacesEvents();
        assertTrue(iter.hasNext()); 

        // Test encode method

        System.out.println("    Testing encode method...");
        hyperlinkRenderer.encodeBegin(getFacesContext(), command);
        hyperlinkRenderer.encodeEnd(getFacesContext(), command);
        getFacesContext().getResponseWriter().writeText("\n");
    }

    public void testListboxRenderer(UIComponent root) throws IOException {
        System.out.println("Testing ListboxRenderer");
        UISelectOne selectOne = new UISelectOneBase();
	UISelectItems uiSelectItems = new UISelectItemsBase();
        selectOne.setValue(null);
        selectOne.setId("my_listbox");
        SelectItem item1 = new SelectItem(new Long(100), "Long1", null);
        SelectItem item2 = new SelectItem(new Long(101), "Long2", null);
        SelectItem item3 = new SelectItem(new Long(102), "Long3", null);
        SelectItem item4 = new SelectItem(new Long(103), "Long4", null);
        SelectItem[] selectItems = {item1, item2, item3, item4};
        uiSelectItems.setValue(selectItems);
	uiSelectItems.setId("items");
        selectOne.setConverter("Number");
        selectOne.getChildren().add(uiSelectItems);
        root.getChildren().add(selectOne);

        ListboxRenderer listboxRenderer = new ListboxRenderer();

        // test decode method
        System.out.println("    Testing decode method... ");
        listboxRenderer.decode(getFacesContext(), selectOne);
        Object value = selectOne.getValue();
        assertTrue(value.equals(new Long(100)));

        // test encode method
        System.out.println("    Testing encode method... ");
        //selectOne.setId("my_listbox");
        listboxRenderer.encodeBegin(getFacesContext(), selectOne);
        listboxRenderer.encodeEnd(getFacesContext(), selectOne);
        getFacesContext().getResponseWriter().writeText("\n");
    }

    public void testSecretRenderer(UIComponent root) throws IOException {
        System.out.println("Testing SecretRenderer");
        UIInput textEntry = new UIInputBase();
        textEntry.setValue(null);
        textEntry.setId("my_secret");
        root.getChildren().add(textEntry);

        SecretRenderer secretRenderer = new SecretRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        secretRenderer.decode(getFacesContext(), textEntry);
        assertTrue(((String)textEntry.getValue()).equals("secret"));

        // test encode method

        System.out.println("    Testing encode method...");
        secretRenderer.encodeBegin(getFacesContext(), textEntry);
        secretRenderer.encodeEnd(getFacesContext(), textEntry);
        getFacesContext().getResponseWriter().writeText("\n");
    }

    public void testInputTextRenderer(UIComponent root) throws IOException {
        System.out.println("Testing InputTextRenderer");
        UIInput text = new UIInputBase();
        text.setValue(null);
        text.setId("my_input_text");
        root.getChildren().add(text);

        TextRenderer textRenderer = new TextRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        textRenderer.decode(getFacesContext(), text);
        assertTrue(((String)text.getValue()).equals("text"));

        // test encode method

        System.out.println("    Testing encode method...");
        textRenderer.encodeBegin(getFacesContext(), text);
        textRenderer.encodeEnd(getFacesContext(), text);
    }

    public void testOutputTextRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OutputTextRenderer");
        UIOutput text = new UIOutputBase();
        text.setValue(null);
        text.setId("my_output_text");
        root.getChildren().add(text);

        TextRenderer textRenderer = new TextRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        textRenderer.decode(getFacesContext(), text);

        // test encode method

        System.out.println("    Testing encode method...");
        textRenderer.encodeBegin(getFacesContext(), text);
        textRenderer.encodeEnd(getFacesContext(), text);
    }

    public void testGraphicImageRenderer(UIComponent root) throws IOException {
        System.out.println("Testing GraphicImageRenderer");
        UIGraphic img = new UIGraphicBase();
        img.setURL("/nonModelReferenceImage.gif");
        img.setId("my_graphic_image");
        root.getChildren().add(img);

        ImageRenderer imageRenderer = new ImageRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        imageRenderer.decode(getFacesContext(), img);

        // test encode method

        System.out.println("    Testing encode method...");
        imageRenderer.encodeBegin(getFacesContext(), img);
        imageRenderer.encodeEnd(getFacesContext(), img);

        System.out.println("    Testing graphic support of modelReference...");
	root.getChildren().remove(img);
	img = new UIGraphicBase();
	root.getChildren().add(img);
	TestBean testBean = (TestBean) 
	    (Util.getValueBinding("TestBean")).getValue(getFacesContext());
	assertTrue(null != testBean); // set in FacesTestCaseService
	testBean.setImagePath("/foo/modelReferenceImage.gif");
	img.setValueRef("TestBean.imagePath");

        imageRenderer.encodeBegin(getFacesContext(), img);
        imageRenderer.encodeEnd(getFacesContext(), img);
    }

    public void testOutputErrorsRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OutputErrorsRenderer");
        UIOutput output = new UIOutputBase();
        output.setId("my_output_errors");
        root.getChildren().add(output);

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
    }

    public void testOutputMessageRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OutputMessageRenderer");
        UIOutput output = new UIOutputBase();
        output.setId("my_output_message");
        output.setValue("My name is {0} {1}");
        UIParameter param1, param2 = null;
        param1 = new UIParameterBase();
        param1.setId("p1");
        param2 = new UIParameterBase();
        param2.setId("p2");
        param1.setValue("Bobby");
        param2.setValue("Orr");
        output.getChildren().add(param1);
        output.getChildren().add(param2);
        root.getChildren().add(output);

        MessageRenderer messageRenderer = new MessageRenderer();

        // test encode method

        System.out.println("    Testing encode method...");

        messageRenderer.encodeBegin(getFacesContext(), output);
        messageRenderer.encodeEnd(getFacesContext(), output);
    }
        

    public void testInputDateRenderer(UIComponent root) throws IOException {
        System.out.println("Testing Input_DateRenderer");
        UIInput input = new UIInputBase();
        input.setValue(null);
        input.setId("my_input_date");
	input.setAttribute("dateStyle", "medium");
        root.getChildren().add(input);

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
    }

    public void testInputTimeRenderer(UIComponent root) throws IOException {
        System.out.println("Testing Input_TimeRenderer");
        UIInput input = new UIInputBase();
        input.setValue(null);
        input.setId("my_input_time");
	input.setAttribute("timeStyle", "medium");
        root.getChildren().add(input);

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
    }

    public void testInputDateTimeRendererWithPattern(UIComponent root) throws IOException {
        System.out.println("Testing Input_DateRenderer With Pattern");
	String formatPattern = "EEE, MMM d, yyyy G 'at' hh:mm:ss a";
        UIInput input = new UIInputBase();
        input.setValue(null);
        input.setId("my_input_date2");
	input.setAttribute("formatPattern", formatPattern);
        root.getChildren().add(input);

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
    }

    public void testTextAreaRenderer(UIComponent root) throws IOException {
        System.out.println("Testing TextAreaRenderer");
        UIInput textEntry = new UIInputBase();
        textEntry.setValue(null);
        textEntry.setId("my_textarea");
        root.getChildren().add(textEntry);

        TextAreaRenderer textAreaRenderer = new TextAreaRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        textAreaRenderer.decode(getFacesContext(), textEntry);
        assertTrue(((String)textEntry.getValue()).equals("TextAreaRenderer"));

        // test encode method

        System.out.println("    Testing encode method...");
        textAreaRenderer.encodeBegin(getFacesContext(), textEntry);
        textAreaRenderer.encodeEnd(getFacesContext(), textEntry);
        getFacesContext().getResponseWriter().writeText("\n");
    }
    
    public void testInputNumberRenderer(UIComponent root) throws IOException {
        System.out.println("Testing NumberRenderer for UIInput ");
        UIInput input = new UIInputBase();
        input.setValue(null);
        input.setId("my_number");
	input.setAttribute("numberStyle", "percent");
        root.getChildren().add(input);

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
    }
    
    public void testInputNumberRendererWithPattern(UIComponent root) 
            throws IOException {
        System.out.println("Testing NumberRenderer With Pattern for UIInput ");
	String formatPattern = "####.000";
        UIInput input = new UIInputBase();
        input.setValue(null);
        input.setId("my_number2");
	input.setAttribute("formatPattern", formatPattern);
        root.getChildren().add(input);

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
        getFacesContext().getResponseWriter().writeText("\n");
    }    

    public void testOutputNumberRenderer(UIComponent root) throws IOException {
        System.out.println("Testing NumberRenderer for UIOutput");
        UIOutput output = new UIOutputBase();
        output.setValue(new Double(.99));
        output.setId("my_number3");
	output.setAttribute("numberStyle", "percent");
        root.getChildren().add(output);

        NumberRenderer numberRenderer = new NumberRenderer();
	Number number = null;
	NumberFormat formatter = 
	    NumberFormat.getPercentInstance(getFacesContext().getLocale());
	
        // test encode method
        System.out.println("    Testing encode method...");
        numberRenderer.encodeBegin(getFacesContext(), output);
        numberRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().writeText("\n");
    }
    
    public void testOutputNumberRendererWithPattern(UIComponent root) throws IOException {
        System.out.println("Testing NumberRenderer With Pattern for UIOutput");
	String formatPattern = "####.000";
        UIOutput output = new UIOutputBase();
        output.setValue(new Double(999));
        output.setId("my_number4");
	output.setAttribute("formatPattern", formatPattern);
        root.getChildren().add(output);

        NumberRenderer numberRenderer = new NumberRenderer();
	Number number = null;
	DecimalFormat formatter = new DecimalFormat(formatPattern);
	
        // test encode method
        System.out.println("    Testing encode method...");
        numberRenderer.encodeBegin(getFacesContext(), output);
        numberRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().writeText("\n");
    }  


    public void testOutputDateRenderer(UIComponent root) throws IOException {
        System.out.println("Testing Output_DateRenderer");
        UIOutput output = new UIOutputBase();
        output.setId("my_output_date");
	output.setAttribute("dateStyle", "medium");
	output.setValue(DATE_STR);
        root.getChildren().add(output);

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
    }

    public void testOutputTimeRenderer(UIComponent root) throws IOException {
        System.out.println("Testing Output_TimeRenderer");
        UIOutput output = new UIOutputBase();
        output.setId("my_output_time");
	output.setAttribute("timeStyle", "medium");
	output.setValue(TIME_STR);
        root.getChildren().add(output);

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
    }

    public void testOutputDateTimeRendererWithPattern(UIComponent root) throws IOException {
        System.out.println("Testing DateRenderer With Pattern");
	String formatPattern = "EEE, MMM d, yyyy G 'at' hh:mm:ss a";
        UIOutput output = new UIOutputBase();
        output.setValue(DATE_STR_LONG);
        output.setId("my_output_date2");
	output.setAttribute("formatPattern", formatPattern);
        root.getChildren().add(output);

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

	// testing encode with invalid date
	output.setValue("invalidDate");
        System.out.println("    Testing encode method...");
        dateRenderer.encodeBegin(getFacesContext(), output);
        dateRenderer.encodeEnd(getFacesContext(), output);
        getFacesContext().getResponseWriter().writeText("\n");
        // decode() simply sets valid to true for UIOutput components. 
        // So valid will never be false.	
    }
    
    public void testInputNumberRendererWithCharacter(UIComponent root) 
            throws IOException {
        System.out.println("Testing NumberRenderer With Character input ");
	UIInput input = new UIInputBase();
        char testchar='9';
        input.setValue(new Character(testchar));
        input.setId("my_character");
	
        NumberRenderer numberRenderer = new NumberRenderer();
	String expectedStr = numberRenderer.getCurrentValue(getFacesContext(),input);
        assertTrue(expectedStr.equals("9"));
    } 
} // end of class TestRenderers2_
