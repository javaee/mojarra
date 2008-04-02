/*
 * $Id: TestRenderers_2.java,v 1.59 2003/09/25 16:36:43 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_2.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.MessageImpl;
import javax.faces.component.UISelectItems;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIForm;
import javax.faces.component.base.UISelectBooleanBase;
import javax.faces.component.base.UICommandBase;
import javax.faces.component.base.UISelectItemsBase;
import javax.faces.component.base.UISelectOneBase;
import javax.faces.component.base.UIInputBase;
import javax.faces.component.base.UIOutputBase;
import javax.faces.component.base.UIGraphicBase;
import javax.faces.component.base.UIParameterBase;
import javax.faces.component.base.UINamingContainerBase;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.component.base.UIFormBase;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.TestBean;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_2.java,v 1.59 2003/09/25 16:36:43 rlubke Exp $
 * 
 *
 */

public class TestRenderers_2 extends JspFacesTestCase
{
    //
    // Instance Variables
    //
    private Application application;

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
	    "<img id=\"myGraphicImage\" src=\"/test/nonModelReferenceImage.gif;jsessionid=CE3C052680005E352476DAD1A410AAC9\" /><img id=\"id0\" src=\"/test/foo/modelReferenceImage.gif;jsessionid=CE3C052680005E352476DAD1A410AAC9\" />My name is Bobby Orr"
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
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = aFactory.getApplication();
        UIViewRoot page = new UIViewRootBase();
        page.setViewId("viewId");       
        getFacesContext().setViewRoot(page);
	assertTrue(null != getFacesContext().getResponseWriter());
    }     

    public void beginRenderers(WebRequest theRequest) {
        // for CheckboxRenderer
        theRequest.addParameter("myCheckboxOn", "on");
        theRequest.addParameter("myCheckboxYes", "yes");
        theRequest.addParameter("myCheckboxTrue", "true");
  
        // for HyperlinkRenderer
        theRequest.addParameter("action", "command");
        theRequest.addParameter("myCommand", "HyperlinkRenderer");
        // for Listbox
        theRequest.addParameter("myListbox", "100");
        // for TextEntry_Secret
        theRequest.addParameter("mySecret", "secret");
        // for Text
        theRequest.addParameter("myInputText", "text");

        theRequest.addParameter("myOutputText", "text");

        theRequest.addParameter("myTextarea", "TextAreaRenderer");

        theRequest.addParameter("myGraphicImage", "graphicimage");

        theRequest.addParameter("myOutputErrors", "outputerrors");

        theRequest.addParameter("myOutputMessage", "outputmessage");
    } 

    public void testRenderers() {

        try {
            // create a dummy root for the tree.
            UIViewRoot root = new UIViewRootBase();
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
            testTextAreaRenderer(root);
            testGraphicImageRenderer(root);            
            testOutputErrorsRenderer(root);
            testOutputMessageRenderer(root);
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
        selectBoolean.setId("myCheckbox");
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
        selectBoolean.setId("myCheckboxOn");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean); 
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        

        // test decode method

        System.out.println("    Testing decode method - parameter (yes)");
        selectBoolean.setId("myCheckboxYes");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
       
        // test decode method

        System.out.println("    Testing decode method - parameter (true)");
        selectBoolean.setId("myCheckboxTrue");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        
        // test decode method
        
        System.out.println("    Testing decode method - parameter (true)");
        selectBoolean.setId("myCheckboxTrue");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        
         
        // test decode method with checkbox disabled.
        System.out.println("    Testing decode method - parameter (yes)");
        selectBoolean.setId("mycheckboxDisabled");
        selectBoolean.getAttributes().put("disabled", "true");
        selectBoolean.setValue(Boolean.TRUE);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        // make sure the value wasn't set to false. Bug id  4883159
        assertTrue(val.booleanValue());
        selectBoolean.getAttributes().remove("disabled");
        
        // test encode method
        System.out.println("    Testing encode method - rendering checked");
        selectBoolean.setId("myCheckbox");
        selectBoolean.setSelected(true);
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().writeText("\n", null);

        System.out.println("    Testing encode method - rendering unchecked");
        selectBoolean.setSelected(false);
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().writeText("\n", null);

        System.out.println("    Testing encode method - rendering unchecked with label");
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }

    public void testHyperlinkRenderer(UIComponent root) throws IOException {
        System.out.println("Testing HyperlinkRenderer");
        UICommand command = new UICommandBase();
        command.setId("myCommand");
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
        getFacesContext().getResponseWriter().writeText("\n", null);
    }

    public void testListboxRenderer(UIComponent root) throws IOException {
        System.out.println("Testing ListboxRenderer");
        UISelectOne selectOne = new UISelectOneBase();
	UISelectItems uiSelectItems = new UISelectItemsBase();
        selectOne.setValue(null);
        selectOne.setId("myListbox");
        SelectItem item1 = new SelectItem(new Long(100), "Long1", null);
        SelectItem item2 = new SelectItem(new Long(101), "Long2", null);
        SelectItem item3 = new SelectItem(new Long(102), "Long3", null);
        SelectItem item4 = new SelectItem(new Long(103), "Long4", null);
        SelectItem[] selectItems = {item1, item2, item3, item4};
        uiSelectItems.setValue(selectItems);
	uiSelectItems.setId("items");
	Converter converter = application.createConverter("Number");
	selectOne.setConverter(converter);
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
        //selectOne.setId("myListbox");
        listboxRenderer.encodeBegin(getFacesContext(), selectOne);
        listboxRenderer.encodeEnd(getFacesContext(), selectOne);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }

    public void testSecretRenderer(UIComponent root) throws IOException {
        System.out.println("Testing SecretRenderer");
        UIInput textEntry = new UIInputBase();
        textEntry.setValue(null);
        textEntry.setId("mySecret");
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
        getFacesContext().getResponseWriter().writeText("\n", null);
    }

    public void testInputTextRenderer(UIComponent root) throws IOException {
        System.out.println("Testing InputTextRenderer");
        UIInput text = new UIInputBase();
        text.setValue(null);
        text.setId("myInputText");
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
        text.setId("myOutputText");
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
        img.setId("myGraphicImage");
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
        output.setId("myOutputErrors");
        root.getChildren().add(output);
        
        ResponseWriter originalWriter = getFacesContext().getResponseWriter();
        UIViewRoot originalRoot = getFacesContext().getViewRoot();
        
        getFacesContext().setViewRoot((UIViewRoot) root);
        
        // setup a new HtmlResponseWriter using a StringWriter. 
        // This allows us to capture the output and check for
        // correctness without using a goldenfile.
        StringWriter writer = new StringWriter();
        HtmlResponseWriter htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        ErrorsRenderer errorsRenderer = new ErrorsRenderer();       
        // populate facescontext with some errors
        // specifically, add a "global" message and
        // "component" message. 

        getFacesContext().addMessage(null,new MessageImpl(1,
            "global message summary", "global message detail"));
        getFacesContext().addMessage(output, new MessageImpl(1,
            "component message summary", "component message detail"));

        // test encode method

        // no 'for' attribute specified, so both messages should be present
        System.out.println("    Testing encode method...");
        errorsRenderer.encodeBegin(getFacesContext(), output);
        errorsRenderer.encodeEnd(getFacesContext(), output);
               
        String result = writer.toString();
        assertTrue(result.indexOf("global message summary") != -1);
        assertTrue(result.indexOf("component message summary") != -1);
        
        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }
        
        // a 'for' attribute with a zero-length string will result 
        // will result in global message not associated with a component
        // to be returned.
        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);
        output.getAttributes().put("for", "");
        errorsRenderer.encodeBegin(getFacesContext(), output);
        errorsRenderer.encodeEnd(getFacesContext(), output);
        
        result = writer.toString();
        assertTrue(result.indexOf("global message summary") != -1);
        assertTrue(result.indexOf("component message summary") == -1);
        
        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }
        
        // a 'for' attribute the specified a component with a valid 'id'
        // this will ensure scanning up the tree works.
        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);
        UIInput input = new UIInputBase();
        input.setId("errorInput");
        root.getChildren().add(input);
        root.getChildren().remove(output);
        input.getChildren().add(output);
        getFacesContext().addMessage(input, 
            new MessageImpl(1, "error message summary_1", "error message detail_1"));
                
        output.getAttributes().put("for", "errorInput");
        errorsRenderer.encodeBegin(getFacesContext(), output);
        errorsRenderer.encodeEnd(getFacesContext(), output);
        
        result = writer.toString();        
        assertTrue(result.indexOf("global message summary") == -1);
        assertTrue(result.indexOf("component message summary") == -1);
        assertTrue(result.indexOf("error message summary_1") != -1);
        
        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }
                
        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);
        input.getChildren().remove(output);
        root.getChildren().add(output);
        UIInput input1 = new UIInputBase();         
        input1.setId("errorInput1");        
        UIInput anon1 = new UIInputBase();
        anon1.setId("anon1");        
        output.getChildren().add(anon1);
        UIInput anon2 = new UIInputBase();
        anon2.setId("anon2");
        anon1.getChildren().add(anon2);        
        anon2.getChildren().add(input1);
        
        getFacesContext().addMessage(input1,
            new MessageImpl(1, "error message summary_2", "error message detail_2"));
                
        output.getAttributes().put("for", "errorInput1");
        errorsRenderer.encodeBegin(getFacesContext(), output);
        errorsRenderer.encodeEnd(getFacesContext(), output);

        result = writer.toString();
        assertTrue(result.indexOf("global message summary") == -1);
        assertTrue(result.indexOf("component message summary") == -1);
        assertTrue(result.indexOf("error message summary_1") == -1);
        assertTrue(result.indexOf("error message summary_2") != -1);
        
        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }
        
        // now scan downward where the component isn't at the very bottom,
        // but somewhere between the output component and the bottom.
        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);
        UIInput input2 = new UIInputBase();
        input2.setId("errorInput2");   
        UIInput anon3 = new UIInputBase();
        anon3.setId("anon3");
        UIInput anon4 = new UIInputBase();   
        anon4.setId("anon4");
        UIInput anon5 = new UIInputBase();
        anon5.setId("anon5");
        
        anon2.getChildren().remove(input1);
        anon1.getChildren().remove(anon2);
        
        anon1.getChildren().add(input1);
        input1.getChildren().add(anon2);
        anon2.getChildren().add(anon3);
        anon2.getChildren().add(anon4);
        anon2.getChildren().add(input2);
        input2.getChildren().add(anon5);
                                    
        getFacesContext().addMessage(input2,
                new MessageImpl(1, "error message summary_3", "error message detail_3"));

        output.getAttributes().put("for", "errorInput2");
        errorsRenderer.encodeBegin(getFacesContext(), output);
        errorsRenderer.encodeEnd(getFacesContext(), output);

        result = writer.toString();
        assertTrue(result.indexOf("global message summary") == -1);
        assertTrue(result.indexOf("component message summary") == -1);
        assertTrue(result.indexOf("error message summary_1") == -1);
        assertTrue(result.indexOf("error message summary_2") == -1);
        assertTrue(result.indexOf("error message summary_3") != -1);
        
        // clean the tree
        output.getChildren().remove(anon1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }
        
        // check for the a component down the tree when
        // the component exists in another naming container
        // further down the view as a child of the current component
        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);
        UIForm form = new UIFormBase();
        form.setId("form");
        UIInput input3 = new UIInputBase();
        input3.setId("errorInput3");
        anon1 = new UIInputBase();
        anon1.setId("anon1");        
        output.getChildren().add(anon1);
        anon1.getChildren().add(form);
        form.getChildren().add(input3);
        getFacesContext().addMessage(input3,
            new MessageImpl(1, "error message summary_4", "error message detail_4"));
        
        output.getAttributes().put("for", "errorInput3");
        errorsRenderer.encodeBegin(getFacesContext(), output);
        errorsRenderer.encodeEnd(getFacesContext(), output);

        result = writer.toString();
        assertTrue(result.indexOf("global message summary") == -1);
        assertTrue(result.indexOf("component message summary") == -1);
        assertTrue(result.indexOf("error message summary_1") == -1);
        assertTrue(result.indexOf("error message summary_2") == -1);
        assertTrue(result.indexOf("error message summary_3") == -1);
        assertTrue(result.indexOf("error message summary_4") != -1);               
        
        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }
        
        // now have multiple NamingContainers that exist
        // as a sibling of the output component
        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);
        UIForm form1 = new UIFormBase();
        form1.setId("form1");
        UIForm form2 = new UIFormBase();
        form2.setId("form2");
        UIInput input4 = new UIInputBase();
        input4.setId("errorInput4");
        UIOutput anonout1 = new UIOutputBase();
        anonout1.setId("anonout1");
        getFacesContext().getViewRoot().getChildren().add(form1);
        form1.getChildren().add(anonout1);
        anonout1.getChildren().add(form2);
        form2.getChildren().add(input4);
        getFacesContext().addMessage(input4,
            new MessageImpl(1, "error message summary_5", "error message detail_5"));
        
        output.getAttributes().put("for", "errorInput4");
        errorsRenderer.encodeBegin(getFacesContext(), output);
        errorsRenderer.encodeEnd(getFacesContext(), output);

        result = writer.toString();        
        assertTrue(result.indexOf("global message summary") == -1);
        assertTrue(result.indexOf("component message summary") == -1);
        assertTrue(result.indexOf("error message summary_1") == -1);
        assertTrue(result.indexOf("error message summary_2") == -1);
        assertTrue(result.indexOf("error message summary_3") == -1);
        assertTrue(result.indexOf("error message summary_4") == -1);  
        assertTrue(result.indexOf("error message summary_5") != -1); 
        
        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }
        
        // restore the original ResponseWriter
        getFacesContext().setResponseWriter(originalWriter);
        getFacesContext().setViewRoot(originalRoot);
    }

    public void testOutputMessageRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OutputMessageRenderer");
        UIOutput output = new UIOutputBase();
        output.setId("myOutputMessage");
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

    public void testTextAreaRenderer(UIComponent root) throws IOException {
        System.out.println("Testing TextAreaRenderer");
        UIInput textEntry = new UIInputBase();
        textEntry.setValue(null);
        textEntry.setId("myTextarea");
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
        getFacesContext().getResponseWriter().writeText("\n", null);
    }       
    
} // end of class TestRenderers_2
