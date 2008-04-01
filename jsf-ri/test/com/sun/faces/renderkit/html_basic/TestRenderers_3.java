/**
 * $Id: TestRenderers_3.java,v 1.1 2002/09/04 22:32:40 eburns Exp $
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
import javax.faces.component.UIComponentBase;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContextFactory;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.tree.XmlTreeImpl;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_3.java,v 1.1 2002/09/04 22:32:40 eburns Exp $
 * 
 *
 */

public class TestRenderers_3 extends JspFacesTestCase {
    //
    // Protected Constants
    //
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
                getFacesContext().getServletContext(),
                new UICommand(),
                "treeId",
                "");
        getFacesContext().setResponseTree(xmlTree);
        assertTrue(null != getFacesContext().getResponseWriter());
    }

    public void beginRenderers(WebRequest theRequest) {
        theRequest.addParameter("/my_menu", "Blue");
        theRequest.addParameter("/my_listbox", "Blue");
        theRequest.addParameter("/my_checkboxlist", "Blue");

    }

    public void testRenderers() {

        try {
            // create a dummy root for the tree.
            UIComponentBase root = new UIComponentBase() {
                public String getComponentType() {
                    return "root";
                }
            };
            root.setComponentId("root");

            testSelectManyMenuRenderer(root);
            testSelectManyListboxRenderer(root);
            testSelectManyCheckboxListRenderer(root);

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

        SelectManyListboxRenderer selectManyListboxRenderer =
            new SelectManyListboxRenderer();

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

        SelectManyMenuRenderer selectManyMenuRenderer =
            new SelectManyMenuRenderer();

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

} // end of class TestRenderers_3
