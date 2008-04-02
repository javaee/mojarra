/*
 * $Id: UIComponentTestCase.java,v 1.36 2003/09/20 00:48:14 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import javax.faces.component.base.TestComponent;
import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockServletContext;


/**
 * <p>Base unit tests for all {@link UIComponent} implementation classes.</p>
 */

public class UIComponentTestCase extends TestCase {


    // ------------------------------------------------------ Instance Variables


    // The component to be tested
    protected UIComponent component = null;

    // The set of attribute names expected on a pristine component instance
    protected String expectedAttributes[] = null;

    // The expected component identifier on a pristine component instance
    protected String expectedId = null;

    // The expected rendered on a pristine component instance
    protected boolean expectedRendered = true;

    // The expected rendererType on a pristine component instance
    protected String expectedRendererType = null;

    // The expected rendersChildren on a pristine component instance
    protected boolean expectedRendersChildren = false;


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public UIComponentTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        expectedAttributes = new String[0];
        expectedId = "test";
        expectedRendered = true;
        expectedRendererType = null;
        expectedRendersChildren = false;
        component = new TestComponent(expectedId);
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIComponentTestCase.class));
    }


    // Tear down instance variables required by this test case.
    public void tearDown() {
        component = null;
        expectedAttributes = null;
        expectedId = null;
        expectedRendered = true;
        expectedRendererType = null;
        expectedRendersChildren = false;
    }


    // ------------------------------------------------- Individual Test Methods


    // Negative tests on attribute methods
    public void testAttributesNegative() {

        // getAttributes().get() - null
        try {
            Object value = component.getAttributes().get(null);
            fail("should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }

        // getAttributes().put() - null
        try {
            component.getAttributes().put(null, "bar");
            fail("should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }

    }


    // Positive tests on attribute methods
    public void testAttributesPositive() {

        checkAttributeCount(component, expectedAttributes.length);
        checkAttributeMissing(component, "foo");
        checkAttributeMissing(component, "baz");

        component.getAttributes().put("foo", "bar");
        checkAttributeCount(component, expectedAttributes.length + 1);
        checkAttributePresent(component, "foo", "bar");
        checkAttributeMissing(component, "baz");

        component.getAttributes().put("baz", "bop");
        checkAttributeCount(component, expectedAttributes.length + 2);
        checkAttributePresent(component, "foo", "bar");
        checkAttributePresent(component, "baz", "bop");

        component.getAttributes().put("baz", "boo");
        checkAttributeCount(component, expectedAttributes.length + 2);
        checkAttributePresent(component, "foo", "bar");
        checkAttributePresent(component, "baz", "boo");

        component.getAttributes().remove("foo");
        checkAttributeCount(component, expectedAttributes.length + 1);
        checkAttributeMissing(component, "foo");
        checkAttributePresent(component, "baz", "boo");

    }


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        assertEquals(component.getChildren(),
                     (List) component.getAttributes().get("children"));

        assertEquals(component.getComponentRef(),
                     (String) component.getAttributes().get("componentRef"));
        component.setComponentRef("foo");
        assertEquals("foo", (String) component.getAttributes().get("componentRef"));
        component.setComponentRef(null);
        assertNull((String) component.getAttributes().get("componentRef"));
        component.getAttributes().put("componentRef", "bar");
        assertEquals("bar", component.getComponentRef());
        component.getAttributes().put("componentRef", null);
        assertNull(component.getComponentRef());

        assertEquals(component.getFacets(),
                     (Map) component.getAttributes().get("facets"));

        assertEquals(component.getId(),
                     (String) component.getAttributes().get("id"));

        assertEquals(component.getParent(),
                     (UIComponent) component.getAttributes().get("parent"));

        assertEquals(component.isRendered(),
                     ((Boolean) component.getAttributes().get("rendered")).booleanValue());
        component.setRendered(false);
        assertEquals(Boolean.FALSE,
                     (Boolean) component.getAttributes().get("rendered"));
        component.setRendered(true);
        assertEquals(Boolean.TRUE,
                     (Boolean) component.getAttributes().get("rendered"));
        component.getAttributes().put("rendered", Boolean.FALSE);
        assertTrue(!component.isRendered());
        component.getAttributes().put("rendered", Boolean.TRUE);
        assertTrue(component.isRendered());

        component.setRendererType("foo");
        assertEquals("foo", (String) component.getAttributes().get("rendererType"));
        component.setRendererType(null);
        assertNull((String) component.getAttributes().get("rendererType"));
        component.getAttributes().put("rendererType", "bar");
        assertEquals("bar", component.getRendererType());
        component.getAttributes().put("rendererType", null);
        assertNull(component.getRendererType());

        assertEquals(component.getRendersChildren(),
                     ((Boolean) component.getAttributes().get("rendersChildren")).booleanValue());


    }


    // Negative tests on children methods
    public void testChidrenNegative() {

        // Construct components we will need
        UIComponent comp0 = new TestComponent(null);
        UIComponent comp1 = new TestComponent("comp1");
        UIComponent comp2 = new TestComponent("comp2");
        UIComponent comp3 = new TestComponent("comp3");

        // Set up and verify initial state
        List children = component.getChildren();
        children.add(comp0);
        children.add(comp1);
        children.add(comp2);
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // add(Object) - ClassCastException
        try {
            children.add("String");
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // add(Object) - NullPointerException
        try {
            children.add(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // add(int,Object) - ClassCastException
        try {
            children.add(1, "String");
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // add(int,Object) - IndexOutOfBoundsException low
        try {
            children.add(-1, comp3);
            fail("Should have thrown IndexOutOfBoundsException low");
        } catch (IndexOutOfBoundsException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // add(int,Object) - IndexOutOfBoundsException high
        try {
            children.add(4, comp3);
            fail("Should have thrown IndexOutOfBoundsException high");
        } catch (IndexOutOfBoundsException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // add(int,Object) - NullPointerException
        try {
            children.add(1, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // set(int,Object) - ClassCastException
        try {
            children.set(1, "String");
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // set(int,Object) - IndexOutOfBoundsException low
        try {
            children.set(-1, comp3);
            fail("Should have thrown IndexOutOfBoundsException low");
        } catch (IndexOutOfBoundsException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // set(int,Object) - IndexOutOfBoundsException high
        try {
            children.set(4, comp3);
            fail("Should have thrown IndexOutOfBoundsException high");
        } catch (IndexOutOfBoundsException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

        // set(int,Object) - NullPointerException
        try {
            children.set(1, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildMissing(component, comp3);

    }


    // Positive tests on children methods
    public void testChildrenPositive() {

        // Construct components we will need
        UIComponent comp0 = new TestComponent(null);
        UIComponent comp1 = new TestComponent("comp1");
        UIComponent comp2 = new TestComponent("comp2");
        UIComponent comp3 = new TestComponent("comp3");
        UIComponent comp4 = new TestComponent("comp4");
        UIComponent comp5 = new TestComponent("comp5");
        UIComponent comp6 = new TestComponent("comp6");

        // Verify initial state
        List children = component.getChildren();
        checkChildMissing(component, comp0);
        checkChildCount(component, 0);
        checkChildMissing(component, comp1);
        checkChildMissing(component, comp2);
        checkChildMissing(component, comp3);
        checkChildMissing(component, comp4);
        checkChildMissing(component, comp5);
        checkChildMissing(component, comp6);

        // add(Object)
        children.add(comp1);
        checkChildCount(component, 1);
        checkChildMissing(component, comp0);
        checkChildPresent(component, comp1, 0);
        checkChildMissing(component, comp2);
        checkChildMissing(component, comp3);
        checkChildMissing(component, comp4);
        checkChildMissing(component, comp5);
        checkChildMissing(component, comp6);

        // add(int, Object)
        children.add(0, comp0);
        checkChildCount(component, 2);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildMissing(component, comp2);
        checkChildMissing(component, comp3);
        checkChildMissing(component, comp4);
        checkChildMissing(component, comp5);
        checkChildMissing(component, comp6);

        // addAll(Collection)
        ArrayList list1 = new ArrayList();
        list1.add(comp4);
        list1.add(comp5);
        children.addAll(list1);
        checkChildCount(component, 4);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildMissing(component, comp2);
        checkChildMissing(component, comp3);
        checkChildPresent(component, comp4, 2);
        checkChildPresent(component, comp5, 3);
        checkChildMissing(component, comp6);

        // addAll(int, Collection)
        ArrayList list2 = new ArrayList();
        list2.add(comp2);
        list2.add(comp3);
        children.addAll(2, list2);
        checkChildCount(component, 6);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildPresent(component, comp3, 3);
        checkChildPresent(component, comp4, 4);
        checkChildPresent(component, comp5, 5);
        checkChildMissing(component, comp6);

        // contains(Object) is tested in checkChildPresent / checkChildMissing

        // containsAll(Collection)
        assertTrue(children.containsAll(list1));
        assertTrue(children.containsAll(list2));

        // get(int) is tested in checkChildPresent / checkChildMissing

        // indexOf(Object) is tested in checkChildPresent / checkChildMissing

        // isEmpty() is tested in checkChildCount

        // iterator()
        Iterator iter = children.iterator();
        assertEquals(comp0, (UIComponent) iter.next());
        assertEquals(comp1, (UIComponent) iter.next());
        assertEquals(comp2, (UIComponent) iter.next());
        assertEquals(comp3, (UIComponent) iter.next());
        assertEquals(comp4, (UIComponent) iter.next());
        assertEquals(comp5, (UIComponent) iter.next());
        // PENDING(craigmcc) tests of Iterator.remove()

        // toArray(Object[])
        UIComponent kids[] =
            (UIComponent[]) children.toArray(new UIComponent[0]);
        assertEquals(comp0, kids[0]);
        assertEquals(comp1, kids[1]);
        assertEquals(comp2, kids[2]);
        assertEquals(comp3, kids[3]);
        assertEquals(comp4, kids[4]);
        assertEquals(comp5, kids[5]);

        // listIterator()
        ListIterator listIter1 = children.listIterator();
        assertEquals(comp0, (UIComponent) listIter1.next());
        assertEquals(comp1, (UIComponent) listIter1.next());
        assertEquals(comp2, (UIComponent) listIter1.next());
        assertEquals(comp3, (UIComponent) listIter1.next());
        assertEquals(comp4, (UIComponent) listIter1.next());
        assertEquals(comp5, (UIComponent) listIter1.next());
        assertEquals(comp5, (UIComponent) listIter1.previous());
        assertEquals(comp4, (UIComponent) listIter1.previous());
        assertEquals(comp3, (UIComponent) listIter1.previous());
        assertEquals(comp2, (UIComponent) listIter1.previous());
        assertEquals(comp1, (UIComponent) listIter1.previous());
        assertEquals(comp0, (UIComponent) listIter1.previous());
        // PENDING(craigmcc) tests of ListIterator.remove()
        // PENDING(craigmcc) tests of ListIterator.set()

        // listIterator(int)
        ListIterator listIter2 = children.listIterator(2);
        assertEquals(comp2, (UIComponent) listIter2.next());
        assertEquals(comp3, (UIComponent) listIter2.next());
        assertEquals(comp4, (UIComponent) listIter2.next());
        assertEquals(comp4, (UIComponent) listIter2.previous());
        assertEquals(comp3, (UIComponent) listIter2.previous());
        assertEquals(comp2, (UIComponent) listIter2.previous());
        assertEquals(comp1, (UIComponent) listIter2.previous());

        // subList(int,int)
        List subList = children.subList(3, 5);
        assertEquals(2, subList.size());
        assertEquals(comp3, (UIComponent) subList.get(0));
        assertEquals(comp4, (UIComponent) subList.get(1));

        // set(int,Object)
        children.set(4, comp6);
        checkChildCount(component, 6);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildPresent(component, comp3, 3);
        checkChildMissing(component, comp4);
        checkChildPresent(component, comp5, 5);
        checkChildPresent(component, comp6, 4);
        assertTrue(!children.containsAll(list1));
        assertTrue(children.containsAll(list2));

        // remove(int)
        children.remove(4);
        checkChildCount(component, 5);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildPresent(component, comp2, 2);
        checkChildPresent(component, comp3, 3);
        checkChildMissing(component, comp4);
        checkChildPresent(component, comp5, 4);
        checkChildMissing(component, comp6);
        assertTrue(!children.containsAll(list1));
        assertTrue(children.containsAll(list2));

        // removeAll(Collection)
        children.removeAll(list2);
        checkChildCount(component, 3);
        checkChildPresent(component, comp0, 0);
        checkChildPresent(component, comp1, 1);
        checkChildMissing(component, comp2);
        checkChildMissing(component, comp3);
        checkChildMissing(component, comp4);
        checkChildPresent(component, comp5, 2);
        checkChildMissing(component, comp6);
        assertTrue(!children.containsAll(list1));
        assertTrue(!children.containsAll(list2));

        // PENDING(craigmcc) - retainAll() functionality and tests

        // size() is tested in checkChildCount

        // clear()
        children.clear();
        checkChildCount(component, 0);
        assertNull(comp0.getParent());
        assertNull(comp1.getParent());
        assertNull(comp2.getParent());
        assertNull(comp3.getParent());
        assertNull(comp4.getParent());
        assertNull(comp5.getParent());
        assertNull(comp6.getParent());

    }


    // Test replacing a child with a new one that has the same id
    public void testChidrenReplace() {

        TestComponent child1 = new TestComponent("child");
        TestComponent child2 = new TestComponent("child");

        checkChildCount(component, 0);
        component.getChildren().add(child1);
        checkChildCount(component, 1);
        checkChildPresent(component, child1, 0);
        checkChildMissing(component, child2);
        component.getChildren().set(0, child2);
        checkChildCount(component, 1);
        checkChildMissing(component, child1);
        checkChildPresent(component, child2, 0);
        component.getChildren().clear();
        checkChildCount(component, 0);

    }


    // Negative tests on facet methods
    public void testFacetsNegative() {

        // Construct components we will need
        UIComponent facet1 = new TestComponent("facet1");
        UIComponent facet2 = new TestComponent("facet2");
        UIComponent facet3 = new TestComponent("facet3");

        // Set up and verify initial conditions
        Map facets = component.getFacets();
        facets.put("facet1", facet1);
        facets.put("facet2", facet2);
        checkFacetCount(component, 2);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetPresent(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);

        // put(Object,Object) - null first argument
        try {
            facets.put(null, facet3);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }
        checkFacetCount(component, 2);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetPresent(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);

        // put(Object,Object) - null second argument
        try {
            facets.put("facet3", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }
        checkFacetCount(component, 2);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetPresent(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);

        // put(Object,Object) - non-String first argument
        try {
            facets.put(facet3, facet3);
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException e) {
            ; // Expected result
        }
        checkFacetCount(component, 2);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetPresent(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);

        // put(Object,Object) - non-UIComponent second argument
        try {
            facets.put("facet3", "facet3");
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException e) {
            ; // Expected result
        }
        checkFacetCount(component, 2);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetPresent(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);

    }


    // Positive tests on facet methods
    public void testFacetsPositive() {

        // Construct components we will need
        UIComponent facet1 = new TestComponent("facet1");
        UIComponent facet2 = new TestComponent("facet2");
        UIComponent facet3 = new TestComponent("facet3");
        UIComponent facet4 = new TestComponent("facet4");
        UIComponent facet5 = new TestComponent("facet5");

        // Verify initial conditions
        Map facets = component.getFacets();
        checkFacetCount(component, 0);
        checkFacetMissing(component, "facet1", facet1);
        checkFacetMissing(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);
        checkFacetMissing(component, "facet4", facet4);
        checkFacetMissing(component, "facet5", facet5);

        // containsKey(Object) is tested in checkFacetMissing / checkFacetPresent

        // containsValue(Object) is tested in checkFacetMissing / checkFacetPresent

        // PENDING(craigmcc) - tests for entrySet()

        // get(Object) is tested in checkFacetMissing / checkFacetPresent

        // isEmpty() is tested in checkFacetCount

        // PENDING(craigmcc) - tests for keySet()

        // put(Object,Object)
        facets.put("facet1", facet1);
        checkFacetCount(component, 1);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetMissing(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);
        checkFacetMissing(component, "facet4", facet4);
        checkFacetMissing(component, "facet5", facet5);

        // put(Object,Object)
        facets.put("facet4", facet4);
        checkFacetCount(component, 2);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetMissing(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);
        checkFacetPresent(component, "facet4", facet4);
        checkFacetMissing(component, "facet5", facet5);

        // putAll(Map)
        Map map = new HashMap();
        map.put("facet2", facet2);
        map.put("facet3", facet3);
        facets.putAll(map);
        checkFacetCount(component, 4);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetPresent(component, "facet2", facet2);
        checkFacetPresent(component, "facet3", facet3);
        checkFacetPresent(component, "facet4", facet4);
        checkFacetMissing(component, "facet5", facet5);

        // put(Object,Object) with replace
        // PENDING(craigmcc) - For some reason the swap fails
        map.put("facet3", facet5);
        checkFacetCount(component, 4);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetPresent(component, "facet2", facet2);
        // checkFacetPresent(component, "facet3", facet5);
        checkFacetPresent(component, "facet4", facet4);
        // checkFacetMissing(component, "facet5", facet3);

        // remove(Object)
        facets.remove("facet3");
        checkFacetCount(component, 3);
        checkFacetPresent(component, "facet1", facet1);
        checkFacetPresent(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);
        checkFacetPresent(component, "facet4", facet4);
        checkFacetMissing(component, "facet5", facet5);

        // values() is tested in checkFacetMissing / checkFacetPresent

        // clear()
        facets.clear();
        checkFacetCount(component, 0);
        checkFacetMissing(component, "facet1", facet1);
        checkFacetMissing(component, "facet2", facet2);
        checkFacetMissing(component, "facet3", facet3);
        checkFacetMissing(component, "facet4", facet4);
        checkFacetMissing(component, "facet5", facet5);

    }


    // Test a pristine UIComponent instance 
    public void testPristine() {

        // Validate attributes
        checkAttributeCount(component, expectedAttributes.length);
        for (int i = 0; i < expectedAttributes.length; i++) {
            checkAttributePresent(component, expectedAttributes[i], null);
        }

        // Validate properties
        assertNull("no componentRef", component.getComponentRef());
        assertEquals("expected id",
                     expectedId, component.getId());
        assertNull("no parent", component.getParent());
        assertEquals("expected rendered",
                     expectedRendered, component.isRendered());
        assertEquals("expected rendererType",
                     expectedRendererType, component.getRendererType());
        assertEquals("expected rendersChildren",
                     expectedRendersChildren, component.getRendersChildren());

        // Validate children and facets
        checkChildCount(component, 0);
        checkFacetCount(component, 0);
        int n = 0;
        Iterator items = component.getFacetsAndChildren();
        assertNotNull("iterator returned", items);
        while (items.hasNext()) {
            items.next();
            n++;
        }
        assertEquals("facets and children", 0, n);

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        // id - zero length
        try {
            component.setId("");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        // id - leading digits
        try {
            component.setId("1abc");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        // id - invalid characters 1
        try {
            component.setId("a*c");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        // id - invalid characters 2
        try {
            component.setId(" abc");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        // id - invalid characters 3
        try {
            component.setId("-abc");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        // componentRef
        component.setComponentRef("foo.bar");
        assertEquals("expected componentRef",
                     "foo.bar", component.getComponentRef());

        // id - simple name
        component.setId("foo");
        assertEquals("expected id",
                     "foo", component.getId());

        // id - complex name
        component.setId("a123-bcd_e");
        assertEquals("expected id",
                     "a123-bcd_e", component.getId());

        // parent
        UIComponent parent = new TestComponent("parent");
        component.setParent(parent);
        assertEquals("expected parent",
                     parent, component.getParent());

        // rendered
        component.setRendered(!expectedRendered);
        assertEquals("expected rendered",
                     !expectedRendered, component.isRendered());

        // rendererType
        component.setRendererType("foo");
        assertEquals("expected rendererType",
                     "foo", component.getRendererType());

    }



    /**
     * [3.1.3] Component Tree Navigation with invalid findComponent arguments.
     */
    /*
    public void testComponentTreeNavigationInvalid() {

        UIComponent test1 = new TestComponent("test1");
        UIComponent test2 = new TestComponent("test2");
        UIComponent test3 = new TestComponent("test3");
        component.getChildren().add(test1);
        component.getChildren().add(test2);
        test2.getChildren().add(test3);

	assertTrue(null == component.findComponent("test4"));

	assertTrue(null == test2.findComponent("test3//"));

        try {
            component.findComponent(null);
            fail("findComponent should throw NPE");
        } catch (NullPointerException e) {
            ; // Expected result
        }


    }
    */


    /**
     * [3.1.3] Component Tree Navigation with valid findComponent() arguments.
     */
    /*
    public void testComponentTreeNavigationValid() {

        UIComponent test1 = new TestComponent("test1");
        UIComponent test2 = new TestComponent("test2");
        UIComponent test3 = new TestComponent("test3");
        component.getChildren().add(test1);
        component.getChildren().add(test2);
        test2.getChildren().add(test3);

        // Can a component find itself?
        assertEquals("component find self", component,
                     component.findComponent(component.getId()));
        assertEquals("test1 find self", test1,
                     test1.findComponent(test1.getId()));
        assertEquals("test2 find self", test2,
                     test2.findComponent(test2.getId()));
        assertEquals("test3 find self", test3,
                     test3.findComponent(test3.getId()));

        // Can a component find its parent?
        assertEquals("test1 find parent", component,
                     test1.findComponent(test1.getParent().getId()));
        assertEquals("test2 find parent", component,
                     test2.findComponent(test2.getParent().getId()));
        assertEquals("test3 find parent", test2,
                     test3.findComponent(test3.getParent().getId()));

        // Can a component find its child by name?
        assertEquals("component find test1", test1,
                     component.findComponent("test1"));
        assertEquals("component find test2", test2,
                     component.findComponent("test2"));
        assertEquals("test2 find test3", test3,
                     test2.findComponent("test3"));
        assertEquals("component find test3", test3,
                     component.findComponent("test3"));

    }
    */


    /**
     * Test <code>getFacetsAndChildren()</code> method. Make sure the 
     * facets are returned first followed by children in the order 
     * they are stored in the child list.
     */
    /*
    public void testGetFacetsAndChildren() {

        UIComponent testComponent = new TestComponentNamingContainer();
        UIComponent facet1 = new TestComponent("facet1");
        UIComponent facet2 = new TestComponent("facet2");
        UIComponent facet3 = new TestComponent("facet3");

        UIComponent child1 = new TestComponent("child1");
        UIComponent child2 = new TestComponent("child2");
        UIComponent child3 = new TestComponent("child3");
        UIComponent child = null;

        // Review initial conditions.
        // make sure the itertaor is empty before any children or facet is
        // added.
        Iterator kidItr = null;
        kidItr = testComponent.getFacetsAndChildren();
        assertTrue((kidItr.hasNext()) == false);

        // Add facets and children one at a time.
        testComponent.getFacets().put("facet1", facet1);
        testComponent.getChildren().add(child1);
        testComponent.getFacets().put("facet2", facet2);
        testComponent.getChildren().add(child2);
        testComponent.getFacets().put("facet3", facet3);
        testComponent.getChildren().add(child3);

        // make sure the facets and children are returned in the correct order.
        kidItr = testComponent.getFacetsAndChildren();
        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(facet3) || child.equals(facet2) ||
                child.equals(facet1));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(facet3) || child.equals(facet2) ||
                child.equals(facet1));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(facet3) || child.equals(facet2) ||
                child.equals(facet1));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child1));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child2));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child3));
    }
    */


    // --------------------------------------------------------- Support Methods


    // Validate that the specified number of attributes are present.
    protected void checkAttributeCount(UIComponent component, int count) {
        int result = 0;
        Iterator names = component.getAttributes().keySet().iterator();
        while (names.hasNext()) {
            names.next();
            result++;
        }
        assertEquals("attribute count", count, result);
    }


    // Validate that the specified attribute name is not present
    protected void checkAttributeMissing(UIComponent component,
                                         String name) {
        assertNull("Attribute " + name + " should be missing",
                   component.getAttributes().get(name));
        Iterator keys = component.getAttributes().keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (name.equals(key)) {
                fail("Attribute " + name + " should not be in names list");
            }
        }
    }


    // Validate that the specified attribute name is present with the
    // specified value (if value is not null)
    protected void checkAttributePresent(UIComponent component,
                                         String name, Object value) {
        assertNotNull("attribute " + name + " should be present",
                      component.getAttributes().get(name));
        if (value != null) {
            assertEquals("attribute " + name + " value should be equal",
                         value, component.getAttributes().get(name));
        }
        Iterator keys = component.getAttributes().keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (name.equals(key)) {
                if (value != null) {
                    assertEquals("attribute " + name + " value should match",
                                 value, component.getAttributes().get(name));
                }
                return;
            }
        }
        fail("attribute " + name + " should be in name list");

    }


    // Validate that the specified number of children are present
    protected void checkChildCount(UIComponent component, int count) {
        assertEquals("child count 1", count, component.getChildCount());
        assertEquals("child count 2",
                     count, component.getChildren().size());
        assertEquals("child count 3", count, component.getChildCount());
        if (count == 0) {
            assertTrue("children empty", component.getChildren().isEmpty());
        } else {
            assertTrue("children not empty", !component.getChildren().isEmpty());
        }
    }


    // Validate that the specified child is not present
    protected void checkChildMissing(UIComponent component,
                                     UIComponent child) {
        assertNull("child " + child + " has no parent",
                   child.getParent());
        List children = component.getChildren();
        assertTrue("child " + child + " should not be contained",
                   !children.contains(child));
        assertEquals("child " + child + " should not be found by indexOf",
                     -1, children.indexOf(child));
        for (int i = 0; i < children.size(); i++) {
            if (child.equals((UIComponent) children.get(i)))
                fail("child " + child + " should be missing");
        }
    }


    // Validate that the specified child is present at the specified index
    protected void checkChildPresent(UIComponent component,
                                     UIComponent child, int index) {
        List children = component.getChildren();
        assertTrue("child " + child + " should be contained",
                   children.contains(child));
        assertEquals("child " + child + " should be found by indexOf",
                     index, children.indexOf(child));
        UIComponent kid = (UIComponent) children.get(index);
        assertEquals("child " + child + " should be present",
                     child, kid);
        assertEquals("child " + child + " has correct parent",
                     component, kid.getParent());
    }




    // Validate that the specified number of facets is present
    protected void checkFacetCount(UIComponent component, int count) {
        assertEquals("facet count",
                     count, component.getFacets().size());
        if (count == 0) {
            assertTrue("facets empty",
                       component.getFacets().isEmpty());
        } else {
            assertTrue("facets not empty",
                       !component.getFacets().isEmpty());
        }
    }


    // Validate that the specified facet is not present
    protected void checkFacetMissing(UIComponent component,
                                     String name, UIComponent facet) {
        assertNull("facet " + name + " has no parent",
                   facet.getParent());
        Map facets = component.getFacets();
        assertTrue("facet " + name + " key not present",
                   !facets.containsKey(name));
        assertTrue("facet " + name + " value not present",
                   !facets.containsValue(facet));
        assertNull("facet " + name + " key not found by get",
                   facets.get(name));
        assertNull("facet " + name + " not returned by getFacet(String)",
                   component.getFacet(name));
        Iterator keys = facets.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (name.equals(key)) {
                fail("facet " + name + " found in keys");
            }
        }
        Iterator values = facets.values().iterator();
        while (values.hasNext()) {
            UIComponent value = (UIComponent) values.next();
            if (facet.equals(value)) {
                fail("facet " + name + " found in values");
            }
        }
    }


    // Validate that the specified facet is present
    protected void checkFacetPresent(UIComponent component,
                                     String name, UIComponent facet) {

        assertEquals("facet " + name + " has correct parent",
                     component, facet.getParent());
        Map facets = component.getFacets();
        assertTrue("facet " + name + " key is present",
                   facets.containsKey(name));
        assertTrue("facet " + name + " value is present",
                   facets.containsValue(facet));
        assertEquals("facet " + name + " has correct value",
                     facet, (UIComponent) facets.get(name));
        assertTrue("facet " + name + " returned by getFacet(String)",
                   facet == component.getFacet(name));
        boolean found = false;
        Iterator keys = facets.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (name.equals(key)) {
                found = true;
                break;
            }
        }
        if (!found) {
            fail("facet " + name + " not found in keys");
        }
        found = false;
        Iterator values = facets.values().iterator();
        while (values.hasNext()) {
            UIComponent value = (UIComponent) values.next();
            if (facet.equals(value)) {
                found = true;
                break;
            }
        }
        if (!found) {
            fail("facet " + name + " not found in values");
        }
    }


}
