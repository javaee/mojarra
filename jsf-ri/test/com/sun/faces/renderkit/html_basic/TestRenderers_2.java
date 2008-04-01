/*
 * $Id: TestRenderers_2.java,v 1.2 2002/06/05 23:00:07 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_2.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.CompareFiles;
import com.sun.faces.FileOutputResponseWrapper;
import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.html_basic.CheckboxRenderer;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.tree.XmlTreeImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.faces.component.SelectItem;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UITextEntry;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cactus.WebRequest;
import org.apache.cactus.ServletTestCase;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_2.java,v 1.2 2002/06/05 23:00:07 rkitain Exp $
 * 
 *
 */

public class TestRenderers_2 extends ServletTestCase
{
    //
    // Protected Constants
    //
   public static final String PATH_ROOT = "./build/test/servers/tomcat40/webapps/test/";

   public static final String EXPECTED_OUTPUT_FILENAME = PATH_ROOT +
        "CorrectRenderersResponse_2";

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private FacesContext  facesContext = null;
    private FacesContextFactory  facesContextFactory = null;
    private FileOutputResponseWrapper wrappedResponse = null;

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
        System.setProperty(FactoryFinder.FACES_CONTEXT_FACTORY,
		       "com.sun.faces.context.FacesContextFactoryImpl");
        facesContextFactory = (FacesContextFactory) 
	FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        assertTrue(null != facesContextFactory);
    
        assertTrue(request != null);
        assertTrue(response != null);
        assertTrue(config.getServletContext() != null);
          
         // create the wrapper response object that writes output
         // to a file.
        wrappedResponse = new FileOutputResponseWrapper(response);
            
        facesContext = 
	facesContextFactory.createFacesContext(config.getServletContext(),
					       request, wrappedResponse);
        assertTrue(null != facesContext);

        // this stuff is needed for testing HyperlinkRenderer.encode()

        HtmlBasicRenderKit renderKit = new HtmlBasicRenderKit();
        config.getServletContext().setAttribute(RIConstants.DEFAULT_RENDER_KIT,
            renderKit);
        XmlTreeImpl xmlTree = new XmlTreeImpl(
            config.getServletContext(), new UICommand(), "treeId");
        facesContext.setResponseTree(xmlTree);
    }     

    public void tearDown() {
        config.getServletContext().removeAttribute(
            RIConstants.DEFAULT_RENDER_KIT);
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
    } 

    public void testRenderers() {

        try {
            // create a dummy root for the tree.
            UIComponent root = new UIComponent() {
                public String getComponentType() { return "root"; }
            };
            root.setComponentId("root");

            testCheckboxRenderer(root);
            testHyperlinkRenderer(root);
            testOptionListRenderer(root);
            testSecretRenderer(root);
            testTextRenderer(root);

            assertTrue(filesCompare());
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
        checkboxRenderer.decode(facesContext, selectBoolean);
        Boolean val = (Boolean)selectBoolean.getValue();
        assertTrue(!val.booleanValue());

        // Test parameter coming in - (the checkbox has been checked)

        // test decode method

        System.out.println("    Testing decode method - parameter (on)");
        selectBoolean.setComponentId("my_checkbox_on");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(facesContext, selectBoolean); 
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        

        // test decode method

        System.out.println("    Testing decode method - parameter (yes)");
        selectBoolean.setComponentId("my_checkbox_yes");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(facesContext, selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
            
        // test decode method

        System.out.println("    Testing decode method - parameter (true)");
        selectBoolean.setComponentId("my_checkbox_true");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(facesContext, selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
            
        // test encode method

        System.out.println("    Testing encode method - rendering checked");
        selectBoolean.setComponentId("my_checkbox");
        selectBoolean.setSelected(true);
        checkboxRenderer.encodeBegin(facesContext, selectBoolean);
        wrappedResponse.getWriter().print("\n");

        System.out.println("    Testing encode method - rendering unchecked");
        selectBoolean.setSelected(false);
        checkboxRenderer.encodeBegin(facesContext, selectBoolean);
        wrappedResponse.getWriter().print("\n");

        System.out.println("    Testing encode method - rendering unchecked with label");
        selectBoolean.setAttribute("label", "Foo");
        checkboxRenderer.encodeBegin(facesContext, selectBoolean);
        wrappedResponse.getWriter().print("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = checkboxRenderer.supportsComponentType("SelectBoolean");
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
        hyperlinkRenderer.decode(facesContext, command);

        // Verify command event was set for the application..
        System.out.println("    Testing added application event (commandEvent)..");
        Iterator iter = facesContext.getApplicationEvents();
        assertTrue(iter != null); 

        // Test encode method

        System.out.println("    Testing encode method...");
        hyperlinkRenderer.encodeBegin(facesContext, command);
        wrappedResponse.getWriter().print("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = hyperlinkRenderer.supportsComponentType("Command");
        assertTrue(result);
        result = hyperlinkRenderer.supportsComponentType(command);
        assertTrue(result);
        result = hyperlinkRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
        
    }

    public void testOptionListRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OptionListRenderer");
        UISelectOne selectOne = new UISelectOne();
        selectOne.setValue(null);
        selectOne.setComponentId("my_optionlist");
        SelectItem item1 = new SelectItem("Red", "Red", null);
        SelectItem item2 = new SelectItem("Blue", "Blue", null);
        SelectItem item3 = new SelectItem("Green", "Green", null);
        SelectItem item4 = new SelectItem("Yellow", "Yellow", null);
        SelectItem[] selectItems = {item1, item2, item3, item4};
        selectOne.setItems(selectItems);
        root.addChild(selectOne);

        OptionListRenderer optionlistRenderer = new OptionListRenderer();

        // test decode method

        System.out.println("    Testing decode method... ");
        optionlistRenderer.decode(facesContext, selectOne);
        assertTrue(((String)selectOne.getValue()).equals("Blue"));

        // test encode method

        System.out.println("    Testing encode method... ");
        selectOne.setComponentId("my_optionlist");
        selectOne.setValue("Blue");
        optionlistRenderer.encodeBegin(facesContext, selectOne);
        wrappedResponse.getWriter().print("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = optionlistRenderer.supportsComponentType("SelectOne");
        assertTrue(result);
        result = optionlistRenderer.supportsComponentType(selectOne);
        assertTrue(result);
        result = optionlistRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testSecretRenderer(UIComponent root) throws IOException {
        System.out.println("Testing SecretRenderer");
        UITextEntry textEntry = new UITextEntry();
        textEntry.setValue(null);
        textEntry.setComponentId("my_secret");
        root.addChild(textEntry);

        SecretRenderer secretRenderer = new SecretRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        secretRenderer.decode(facesContext, textEntry);
        assertTrue(((String)textEntry.getValue()).equals("secret"));

        // test encode method

        System.out.println("    Testing encode method...");
        secretRenderer.encodeBegin(facesContext, textEntry);
        wrappedResponse.getWriter().print("\n");

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = secretRenderer.supportsComponentType("TextEntry");
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
        textRenderer.decode(facesContext, text);
        assertTrue(((String)text.getValue()).equals("text"));

        // test encode method

        System.out.println("    Testing encode method...");
        textRenderer.encodeBegin(facesContext, text);
        wrappedResponse.flushBuffer();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = textRenderer.supportsComponentType("Output");
        assertTrue(result);
        result = textRenderer.supportsComponentType(text);
        assertTrue(result);
        result = textRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    private boolean filesCompare() {
        boolean result = false;
        try {
            String ignoreStr = "<a href=\"/test/faces;jsessionid=4573B0C6B316F9D0D252D46330E31063?action=command&name=HyperlinkRenderer&tree=treeId\">HyperlinkRenderer</a>";
            CompareFiles cf = new CompareFiles();
            ArrayList list = new ArrayList();
            list.add(ignoreStr);
            result = 
                cf.filesIdentical(
                    FileOutputResponseWrapper.FACES_RESPONSE_FILENAME,
                    EXPECTED_OUTPUT_FILENAME, list);
        } catch (Exception e ) {
            System.out.println(e.getMessage());
            return false;
        }     
        return result;
    }

} // end of class TestRenderers2_
