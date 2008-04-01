/*
 * $Id: TestRenderers_2.java,v 1.11 2002/08/12 19:57:39 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_2.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.renderkit.html_basic.CheckboxRenderer;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.tree.XmlTreeImpl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.faces.component.SelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIInput;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_2.java,v 1.11 2002/08/12 19:57:39 eburns Exp $
 * 
 *
 */

public class TestRenderers_2 extends JspFacesTestCase
{
    //
    // Protected Constants
    //

    public static String DATE_STR = "Jan 12, 1952";
    public static String DATE_STR_LONG = "Sat, Jan 12, 1952";

    public boolean sendWriterToFile() {
        return true;
    }    

    public String getExpectedOutputFilename() {
        return "CorrectRenderersResponse_2";
    }

    public String [] getLinesToIgnore() {
        String[] lines =  {
"<a href=\"/test/faces;jsessionid=4573B0C6B316F9D0D252D46330E31063?action=command&name=HyperlinkRenderer&tree=treeId\">HyperlinkRenderer</a>"
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
	    new XmlTreeImpl(getFacesContext().getServletContext(), 
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
        // for OptionList
        theRequest.addParameter("/my_optionlist", "Blue");
        // for TextEntry_Secret
        theRequest.addParameter("/my_secret", "secret");
        // for Text
        theRequest.addParameter("/my_text", "text");
        theRequest.addParameter("/my_date", DATE_STR);
        theRequest.addParameter("/my_date2", DATE_STR_LONG);
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
            testOptionListRenderer(root);
	    /**
	       PENDING(rogerk): test the text renderers
            testSecretRenderer(root);
            testTextRenderer(root);
	    **/
	    testDateRenderer(root);
	    testDateRendererWithPattern(root);

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
        selectBoolean.setAttribute("label", "Foo");
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

    public void testOptionListRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OptionListRenderer");
        UISelectOne selectOne = new UISelectOne();
	UISelectItems uiSelectItems = new UISelectItems();
        selectOne.setValue(null);
        selectOne.setComponentId("my_optionlist");
        SelectItem item1 = new SelectItem("Red", "Red", null);
        SelectItem item2 = new SelectItem("Blue", "Blue", null);
        SelectItem item3 = new SelectItem("Green", "Green", null);
        SelectItem item4 = new SelectItem("Yellow", "Yellow", null);
        SelectItem[] selectItems = {item1, item2, item3, item4};
        uiSelectItems.setValue(selectItems);
	uiSelectItems.setComponentId("items");
	selectOne.addChild(uiSelectItems);
        root.addChild(selectOne);

        OptionListRenderer optionlistRenderer = new OptionListRenderer();

        // test decode method

        System.out.println("    Testing decode method... ");
        optionlistRenderer.decode(getFacesContext(), selectOne);
        assertTrue(((String)selectOne.getValue()).equals("Blue"));

        // test encode method

        System.out.println("    Testing encode method... ");
        selectOne.setComponentId("my_optionlist");
        selectOne.setValue("Blue");
        optionlistRenderer.encodeBegin(getFacesContext(), selectOne);
        optionlistRenderer.encodeEnd(getFacesContext(), selectOne);
        getFacesContext().getResponseWriter().write("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = optionlistRenderer.supportsComponentType("javax.faces.component.UISelectOne");
        assertTrue(result);
        result = optionlistRenderer.supportsComponentType(selectOne);
        assertTrue(result);
        result = optionlistRenderer.supportsComponentType("FooBar");
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

    public void testTextRenderer(UIComponent root) throws IOException {
        System.out.println("Testing TextRenderer");
        UIOutput text = new UIOutput();
        text.setValue(null);
        text.setComponentId("my_text");
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
        result = textRenderer.supportsComponentType("javax.faces.component.UIOutput");
        assertTrue(result);
        result = textRenderer.supportsComponentType(text);
        assertTrue(result);
        result = textRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testDateRenderer(UIComponent root) throws IOException {
        System.out.println("Testing DateRenderer");
        UIInput input = new UIInput();
        input.setValue(null);
        input.setComponentId("my_date");
	input.setAttribute("formatStyle", "MEDIUM");
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

    public void testDateRendererWithPattern(UIComponent root) throws IOException {
        System.out.println("Testing DateRenderer With Pattern");
	String formatPattern = "EEE, MMM d, yyyy";
        UIInput input = new UIInput();
        input.setValue(null);
        input.setComponentId("my_date2");
	input.setAttribute("formatPattern", formatPattern);
        root.addChild(input);

        DateRenderer dateRenderer = new DateRenderer();
	Date date = null;
	SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
	
        // test decode method
	
        System.out.println("    Testing decode method...");

        dateRenderer.decode(getFacesContext(), input);
	date = (Date) input.getValue();
	assertTrue(null != date);
	assertTrue(DATE_STR_LONG.equals(formatter.format(date)));

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

} // end of class TestRenderers2_
