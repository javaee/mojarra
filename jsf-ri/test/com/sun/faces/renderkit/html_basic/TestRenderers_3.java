/**
 * $Id: TestRenderers_3.java,v 1.7 2002/12/18 20:55:10 eburns Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// TestRenderers_3.java

package com.sun.faces.renderkit.html_basic;
import java.io.IOException;

import javax.faces.component.SelectItem;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContextFactory;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.sun.faces.renderkit.html_basic.HiddenRenderer;
import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.tree.XmlTreeImpl;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_3.java,v 1.7 2002/12/18 20:55:10 eburns Exp $
 * 
 *
 */

public class TestRenderers_3 extends JspFacesTestCase {
    //
    // Protected Constants
    //
    public static String DATE_STR = "Jan 12, 1952";
    
    public static String NUMBER_STR = "47%";
   
    public boolean sendWriterToFile() {
        return true;
    }

    public String getExpectedOutputFilename() {
        return "CorrectRenderersResponse_3";
    }

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private FacesContextFactory facesContextFactory = null;

    // Attribute Instance Variables
    // Relationship Instance Variables
    //
    // Constructors and Initializers    
    //

    public TestRenderers_3() {
        super("TestRenderers_3");
    }
    public TestRenderers_3(String name) {
        super(name);
    }

    //
    // Class methods
    //

    //
    // Methods from TestCase
    //
    public void setUp() {
        super.setUp();

        XmlTreeImpl xmlTree =
            new XmlTreeImpl(
                getFacesContext(),
                new UICommand(),
                "treeId",
                "");
        getFacesContext().setResponseTree(xmlTree);
        assertTrue(null != getFacesContext().getResponseWriter());
    }

    public void beginRenderers(WebRequest theRequest) {
        theRequest.addParameter("my_menu", "Blue");
        theRequest.addParameter("my_listbox", "Blue");
        theRequest.addParameter("my_checkboxlist", "Blue");
        theRequest.addParameter("my_onemenu", "Blue");
        // parameters to test hidden renderer
        theRequest.addParameter("my_number_hidden", NUMBER_STR);
        theRequest.addParameter("my_input_date_hidden", DATE_STR);

    }

    public void testRenderers() {

        try {
            // create a dummy root for the tree.
            UINamingContainer root = new UINamingContainer() {
                public String getComponentType() {
                    return "root";
                }
            };
            root.setComponentId("root");

            testSelectManyMenuRenderer(root);
            testSelectManyListboxRenderer(root);
            testSelectManyCheckboxListRenderer(root);
            testSelectOneMenuRenderer(root);
            testHiddenRenderer(root);
            assertTrue(verifyExpectedOutput());
        }
        catch (Throwable t) {
            t.printStackTrace();
            assertTrue(false);
            return;
        }
    }

    public void testSelectManyListboxRenderer(UIComponent root)
        throws IOException {
        System.out.println("Testing SelectManyListboxRenderer");
        UISelectMany selectMany = new UISelectMany();
        UISelectItems uiSelectItems = new UISelectItems();
        selectMany.setValue(null);
        selectMany.setComponentId("my_listbox");
        SelectItem item1 = new SelectItem("Red", "Red", null);
        SelectItem item2 = new SelectItem("Blue", "Blue", null);
        SelectItem item3 = new SelectItem("Green", "Green", null);
        SelectItem item4 = new SelectItem("Yellow", "Yellow", null);
        SelectItem[] selectItems = { item1, item2, item3, item4 };
	Object selectedValues[] = null;
        uiSelectItems.setValue(selectItems);
        uiSelectItems.setComponentId("manyitems");
        selectMany.addChild(uiSelectItems);
        root.addChild(selectMany);

        ListboxRenderer selectManyListboxRenderer =
            new ListboxRenderer();

        // test decode method
        System.out.println("    Testing decode method... ");
        selectManyListboxRenderer.decode(getFacesContext(), selectMany);
	assertTrue(null != (selectedValues = selectMany.getSelectedValues()));
	assertTrue(1 == selectedValues.length);
        assertTrue(((String)selectedValues[0]).equals("Blue"));

        // test encode method

        System.out.println("    Testing encode method... ");
        selectManyListboxRenderer.encodeBegin(getFacesContext(), selectMany);
        selectManyListboxRenderer.encodeEnd(getFacesContext(), selectMany);
        getFacesContext().getResponseWriter().write("\n");
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");
        boolean result =
            selectManyListboxRenderer.supportsComponentType(
                "javax.faces.component.UISelectMany");
        assertTrue(result);
        result = selectManyListboxRenderer.supportsComponentType(selectMany);
        assertTrue(result);
        result = selectManyListboxRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testSelectManyCheckboxListRenderer(UIComponent root)
        throws IOException {
        System.out.println("Testing SelectManyCheckboxListRenderer");
        UISelectMany selectMany = new UISelectMany();
        UISelectItems uiSelectItems = new UISelectItems();
        selectMany.setValue(null);
        selectMany.setComponentId("my_checkboxlist");
        SelectItem item1 = new SelectItem("Red", "Red", null);
        SelectItem item2 = new SelectItem("Blue", "Blue", null);
        SelectItem item3 = new SelectItem("Green", "Green", null);
        SelectItem item4 = new SelectItem("Yellow", "Yellow", null);
        SelectItem[] selectItems = { item1, item2, item3, item4 };
	Object selectedValues[] = null;
        uiSelectItems.setValue(selectItems);
        uiSelectItems.setComponentId("manyitems");
        selectMany.addChild(uiSelectItems);
        root.addChild(selectMany);

        SelectManyCheckboxListRenderer selectManyCheckboxListRenderer =
            new SelectManyCheckboxListRenderer();

        // test decode method

        System.out.println("    Testing decode method... ");
        selectManyCheckboxListRenderer.decode(getFacesContext(), selectMany);
	assertTrue(null != (selectedValues = selectMany.getSelectedValues()));
	assertTrue(1 == selectedValues.length);
        assertTrue(((String)selectedValues[0]).equals("Blue"));


        // test encode method
        System.out.println("    Testing encode method... ");
        selectManyCheckboxListRenderer.encodeBegin(
            getFacesContext(),
            selectMany);
        selectManyCheckboxListRenderer.encodeEnd(getFacesContext(), 
						 selectMany);
        getFacesContext().getResponseWriter().write("\n");
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result =
            selectManyCheckboxListRenderer.supportsComponentType(
                "javax.faces.component.UISelectMany");
        assertTrue(result);
        result =
            selectManyCheckboxListRenderer.supportsComponentType(selectMany);
        assertTrue(result);
        result = selectManyCheckboxListRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testSelectManyMenuRenderer(UIComponent root)
        throws IOException {
        System.out.println("Testing SelectManyMenuRenderer");
        UISelectMany selectMany = new UISelectMany();
        UISelectItems uiSelectItems = new UISelectItems();
        selectMany.setValue(null);
        selectMany.setComponentId("my_menu");
        SelectItem item1 = new SelectItem("Red", "Red", null);
        SelectItem item2 = new SelectItem("Blue", "Blue", null);
        SelectItem item3 = new SelectItem("Green", "Green", null);
        SelectItem item4 = new SelectItem("Yellow", "Yellow", null);
        SelectItem[] selectItems = { item1, item2, item3, item4 };
	Object selectedValues[] = null;
        uiSelectItems.setValue(selectItems);
        uiSelectItems.setComponentId("manyitems");
        selectMany.addChild(uiSelectItems);
        root.addChild(selectMany);

        MenuRenderer selectManyMenuRenderer =
            new MenuRenderer();

        // test decode method
        System.out.println("    Testing decode method... ");
        selectManyMenuRenderer.decode(getFacesContext(), selectMany);
	assertTrue(null != (selectedValues = selectMany.getSelectedValues()));
	assertTrue(1 == selectedValues.length);
        assertTrue(((String)selectedValues[0]).equals("Blue"));

        // test encode method
        System.out.println("    Testing encode method... ");
        selectManyMenuRenderer.encodeBegin(getFacesContext(), selectMany);
        selectManyMenuRenderer.encodeEnd(getFacesContext(), selectMany);
        getFacesContext().getResponseWriter().write("\n");
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result =
            selectManyMenuRenderer.supportsComponentType(
                "javax.faces.component.UISelectMany");
        assertTrue(result);
        result = selectManyMenuRenderer.supportsComponentType(selectMany);
        assertTrue(result);
        result = selectManyMenuRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }

    public void testSelectOneMenuRenderer(UIComponent root)
        throws IOException {
        System.out.println("Testing SelectOneMenuRenderer");
        UISelectOne selectOne = new UISelectOne();
        UISelectItems uiSelectItems = new UISelectItems();
        selectOne.setValue(null);
        selectOne.setComponentId("my_onemenu");
        SelectItem item1 = new SelectItem("Red", "Red", null);
        SelectItem item2 = new SelectItem("Blue", "Blue", null);
        SelectItem item3 = new SelectItem("Green", "Green", null);
        SelectItem item4 = new SelectItem("Yellow", "Yellow", null);
        SelectItem[] selectItems = { item1, item2, item3, item4 };
        String selectedValue = null;
        uiSelectItems.setValue(selectItems);
        uiSelectItems.setComponentId("manyitems");
        selectOne.addChild(uiSelectItems);
        root.addChild(selectOne);

        MenuRenderer selectOneMenuRenderer =
            new MenuRenderer();

        // test decode method
        System.out.println("    Testing decode method... ");
        selectOneMenuRenderer.decode(getFacesContext(), selectOne); 
        selectedValue = (String)selectOne.getSelectedValue();
        assertTrue(selectedValue.equals("Blue"));

        // test encode method
        System.out.println("    Testing encode method... ");
        selectOneMenuRenderer.encodeBegin(getFacesContext(), selectOne);
        selectOneMenuRenderer.encodeEnd(getFacesContext(), selectOne);
        getFacesContext().getResponseWriter().write("\n");
        getFacesContext().getResponseWriter().flush();

        System.out.println("    Testing supportsComponentType methods..");

        boolean result =
            selectOneMenuRenderer.supportsComponentType(
                "javax.faces.component.UISelectOne");
        assertTrue(result);
        result = selectOneMenuRenderer.supportsComponentType(selectOne);
        assertTrue(result);
        result = selectOneMenuRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }
    
    public void testHiddenRenderer(UIComponent root) throws IOException {
        System.out.println("Testing Input_DateRenderer");
        UIInput input1 = new UIInput();
        input1.setValue(null);
        input1.setComponentId("my_input_date_hidden");
        input1.setAttribute("converter", "date");
	input1.setAttribute("dateStyle", "medium");
        root.addChild(input1);
        HiddenRenderer hiddenRenderer = new HiddenRenderer();
        
        DateFormat dateformatter = 
	    DateFormat.getDateInstance(DateFormat.MEDIUM,
				       getFacesContext().getLocale());
        
        // test hidden renderer with converter set to date
        // test decode method
	System.out.println("    Testing decode method...");
        hiddenRenderer.decode(getFacesContext(), input1);
	Date date = (Date) input1.getValue();
	assertTrue(null != date);
	assertTrue(DATE_STR.equals(dateformatter.format(date)));
        
        // test encode method
        System.out.println("    Testing encode method...");
        hiddenRenderer.encodeBegin(getFacesContext(), input1);
        hiddenRenderer.encodeEnd(getFacesContext(), input1);
        getFacesContext().getResponseWriter().flush();
        
        // test hidden renderer with converter set to number
        UIInput input2 = new UIInput();
        input2.setValue(null);
        input2.setComponentId("my_number_hidden");
        input2.setAttribute("converter", "number");
	input2.setAttribute("numberStyle", "percent");
        root.addChild(input2);

	NumberFormat numberformatter = 
	    NumberFormat.getPercentInstance(getFacesContext().getLocale());
        // test decode method
        System.out.println("    Testing decode method...");
        hiddenRenderer.decode(getFacesContext(), input2);
	Number number = (Number) input2.getValue();
	assertTrue(null != number);
	assertTrue(NUMBER_STR.equals(numberformatter.format(number)));
   
        // test encode method
        System.out.println("    Testing encode method...");
        hiddenRenderer.encodeBegin(getFacesContext(), input2);
        hiddenRenderer.encodeEnd(getFacesContext(), input2);
        getFacesContext().getResponseWriter().flush();
       

        System.out.println("    Testing supportsComponentType methods..");

        boolean result = false;
        result = hiddenRenderer.supportsComponentType("javax.faces.component.UIInput");
        assertTrue(result);
        result = hiddenRenderer.supportsComponentType(input1);
        assertTrue(result);
        result = hiddenRenderer.supportsComponentType("FooBar");
        assertTrue(!result);
    }
} // end of class TestRenderers_3
