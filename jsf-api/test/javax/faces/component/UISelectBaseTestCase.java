/*
 * $Id: UISelectBaseTestCase.java,v 1.4 2003/02/20 22:46:51 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import javax.faces.mock.MockFacesContext;
import javax.faces.context.FacesContext;


/**
 * <p>Test case for the <strong>javax.faces.UISelectBase</strong>
 * concrete class.</p>
 */

public class UISelectBaseTestCase extends UIInputTestCase {


    // ----------------------------------------------------- Instance Variables

    // ----------------------------------------------------- Class Variables

    public static final int NORMAL_ORDER_CASE = 0;
    public static final int CRAZY_ORDER_CASE = 1;
    public static final int TWO_NAMING_CONTAINER_CASE = 2;
    public static final int THREE_NAMING_CONTAINER_CASE = 3;
    public static final int ROOT_ID_THREE_NAMING_CONTAINER_CASE = 4;
    public static final int NAMED_SELECTBASE_TWO_NAMING_CONTAINER_CASE = 5;
    public static final int NAMED_SELECTBASE_THREE_NAMING_CONTAINER_CASE = 6;

    public static final String ROOT_ID = "rootId";
    public static final String ZERO_ID = "zeroId";
    public static final String ONE_ID = "oneId";

    //
    // Methods to be overridden by subclass
    // 

    public UISelectBase newUISelectBaseSubclass() {
	assertTrue(false);
	return null;
    }


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectBaseTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------ Individual Test Methods


    public void testNoOp() {

    }

    private void buildTree(int order,
			   UIComponent component,
			   UIComponent zero,
			   UIComponent one,
			   UIComponent zero_zero,
			   UIComponent zero_one,
			   UIComponent one_zero,
			   UIComponent one_one) {
	switch (order) {
	case NORMAL_ORDER_CASE:
	    component.addChild(zero);
	    component.addChild(one);
	    
	    zero.addChild(zero_zero);
	    zero.addChild(zero_one);
	    one.addChild(one_zero);
	    one.addChild(one_one);
	    break;
	case CRAZY_ORDER_CASE:
	    zero.addChild(zero_zero);
	    component.addChild(zero);
	    one.addChild(one_one);
	    zero.addChild(zero_one);
	    component.addChild(one);
	    one.addChild(one_zero);
	    break;
	default:
	    assertTrue(false);
	}
    }
    
    private void verifyIds(int namingContainerCaseType,
			   FacesContext context,
			   UIComponent zero,
			   UIComponent one,
			   UIComponent zero_zero,
			   UIComponent zero_one,
			   UIComponent one_zero,
			   UIComponent one_one) {
	String expectedId = null;
	switch (namingContainerCaseType) {
	case TWO_NAMING_CONTAINER_CASE:
	    expectedId = 
		"id0";
	    assertEquals("zeroId == " + expectedId,
			 zero.getClientId(context), expectedId);
	    
	    expectedId = 
		"id1";
	    assertEquals("oneId == " + expectedId,
			 one.getClientId(context),expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id0";
	    assertEquals("zero_zeroId == " + expectedId,
			 zero_zero.getClientId(context), expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id1";
	    assertEquals("zero_oneId == " + expectedId,
			 zero_one.getClientId(context), expectedId);

	    expectedId = 
		"id1" + 
		UIComponent.SEPARATOR_CHAR + 
		"id0";
	    assertEquals("one_zeroId == " + expectedId,
			 one_zero.getClientId(context), expectedId);

	    expectedId = 
		"id1" + 
		UIComponent.SEPARATOR_CHAR + 
		"id1";
	    assertEquals("one_oneId == " + expectedId,
			 one_one.getClientId(context), expectedId);
	    break;
	case NAMED_SELECTBASE_TWO_NAMING_CONTAINER_CASE:
	    expectedId = 
		ZERO_ID;
	    assertEquals("zeroId == " + expectedId,
			 zero.getClientId(context), expectedId);
	    
	    expectedId = 
		ONE_ID;
	    assertEquals("oneId == " + expectedId,
			 one.getClientId(context),expectedId);

	    expectedId = 
		ZERO_ID + 
		UIComponent.SEPARATOR_CHAR + 
		"id0";
	    assertEquals("zero_zeroId == " + expectedId,
			 zero_zero.getClientId(context), expectedId);

	    expectedId = 
		ZERO_ID + 
		UIComponent.SEPARATOR_CHAR + 
		"id1";
	    assertEquals("zero_oneId == " + expectedId,
			 zero_one.getClientId(context), expectedId);

	    expectedId = 
		ONE_ID + 
		UIComponent.SEPARATOR_CHAR + 
		"id0";
	    assertEquals("one_zeroId == " + expectedId,
			 one_zero.getClientId(context), expectedId);

	    expectedId = 
		ONE_ID + 
		UIComponent.SEPARATOR_CHAR + 
		"id1";
	    assertEquals("one_oneId == " + expectedId,
			 one_one.getClientId(context), expectedId);
	    break;
	case THREE_NAMING_CONTAINER_CASE:
	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id0";
	    assertEquals("zeroId == " + expectedId,
			 zero.getClientId(context), expectedId);
	    
	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id1";
	    assertEquals("oneId == " + expectedId,
			 one.getClientId(context),expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id0";
	    assertEquals("zero_zeroId == " + expectedId,
			 zero_zero.getClientId(context), expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id1";
	    assertEquals("zero_oneId == " + expectedId,
			 zero_one.getClientId(context), expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id1" + 
		UIComponent.SEPARATOR_CHAR + 
		"id0";
	    assertEquals("one_zeroId == " + expectedId,
			 one_zero.getClientId(context), expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		"id1" + 
		UIComponent.SEPARATOR_CHAR + 
		"id1";
	    assertEquals("one_oneId == " + expectedId,
			 one_one.getClientId(context), expectedId);

	    break;
	case NAMED_SELECTBASE_THREE_NAMING_CONTAINER_CASE:
	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		ZERO_ID;
	    assertEquals("zeroId == " + expectedId,
			 zero.getClientId(context), expectedId);
	    
	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		ONE_ID;
	    assertEquals("oneId == " + expectedId,
			 one.getClientId(context),expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		ZERO_ID + 
		UIComponent.SEPARATOR_CHAR + 
		"id0";
	    assertEquals("zero_zeroId == " + expectedId,
			 zero_zero.getClientId(context), expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		ZERO_ID + 
		UIComponent.SEPARATOR_CHAR + 
		"id1";
	    assertEquals("zero_oneId == " + expectedId,
			 zero_one.getClientId(context), expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		ONE_ID + 
		UIComponent.SEPARATOR_CHAR + 
		"id0";
	    assertEquals("one_zeroId == " + expectedId,
			 one_zero.getClientId(context), expectedId);

	    expectedId = 
		"id0" + 
		UIComponent.SEPARATOR_CHAR + 
		ONE_ID + 
		UIComponent.SEPARATOR_CHAR + 
		"id1";
	    assertEquals("one_oneId == " + expectedId,
			 one_one.getClientId(context), expectedId);

	    break;
	default:
	    assertTrue(false);
	}
    }	
				

    /**

    * <p>Test that the NamingContainer portion of the UISelectBase
    * implementation works correctly in the case where none of the
    * components have Ids.</p>

    * <p>Create two UISelectBase instances that are the child of one root
    * NamingContainer.  Add two UISelectItem instances to each
    * UISelectBase.  Get the client side ids for all of these things and
    * make sure they are unique.</p>

    */ 

    public void doSelectItemNoIds() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();
	UISelectItem 
	    zero_zero = new UISelectItem(),
	    zero_one = new UISelectItem(),
	    one_zero = new UISelectItem(),
	    one_one = new UISelectItem();

	buildTree(NORMAL_ORDER_CASE,
		  component,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
	verifyIds(TWO_NAMING_CONTAINER_CASE,
		  context,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
    }

    public void doSelectItemNoIdsCrazyOrder() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();
	UISelectItem 
	    zero_zero = new UISelectItem(),
	    zero_one = new UISelectItem(),
	    one_zero = new UISelectItem(),
	    one_one = new UISelectItem();

	buildTree(CRAZY_ORDER_CASE,
		  component,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
	verifyIds(TWO_NAMING_CONTAINER_CASE,
		  context,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
    }

    public void doSelectItemNoIdsExtraNonNamingContainerRootChild() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UIComponent rootChild = new UIComponentBase() {
		public String getComponentType() { return "normalRoot";}
	    };
	component.addChild(rootChild);
	
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();
	UISelectItem 
	    zero_zero = new UISelectItem(),
	    zero_one = new UISelectItem(),
	    one_zero = new UISelectItem(),
	    one_one = new UISelectItem();

	buildTree(NORMAL_ORDER_CASE,
		  rootChild,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
	verifyIds(TWO_NAMING_CONTAINER_CASE,
		  context,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
    }

    public void doSelectItemNoIdsExtraNamingContainerRootChild() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UIComponent rootChild = new UINamingContainer();
	component.addChild(rootChild);
	
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();
	UISelectItem 
	    zero_zero = new UISelectItem(),
	    zero_one = new UISelectItem(),
	    one_zero = new UISelectItem(),
	    one_one = new UISelectItem();

	buildTree(NORMAL_ORDER_CASE,
		  rootChild,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
	verifyIds(THREE_NAMING_CONTAINER_CASE,
		  context,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
    }

    public void doSelectItemWithNamedRoot() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();
	UISelectItem 
	    zero_zero = new UISelectItem(),
	    zero_one = new UISelectItem(),
	    one_zero = new UISelectItem(),
	    one_one = new UISelectItem();

	buildTree(NORMAL_ORDER_CASE,
		  component,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
	// Naming the root node should have no effect on generated the
	// ids.
	component.setComponentId(ROOT_ID);
	verifyIds(TWO_NAMING_CONTAINER_CASE,
		  context,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
    }

    public void doSelectItemWithNamedSelectBase() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();
	UISelectItem 
	    zero_zero = new UISelectItem(),
	    zero_one = new UISelectItem(),
	    one_zero = new UISelectItem(),
	    one_one = new UISelectItem();

	buildTree(NORMAL_ORDER_CASE,
		  component,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
	zero.setComponentId(ZERO_ID);
	one.setComponentId(ONE_ID);
	verifyIds(NAMED_SELECTBASE_TWO_NAMING_CONTAINER_CASE,
		  context,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
    }

    public void doSelectItemWithNamedSelectBaseExtraNamingContainerRootChild() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UIComponent root = new UINamingContainer();
	root.addChild(component);
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();
	UISelectItem 
	    zero_zero = new UISelectItem(),
	    zero_one = new UISelectItem(),
	    one_zero = new UISelectItem(),
	    one_one = new UISelectItem();

	buildTree(NORMAL_ORDER_CASE,
		  component,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
	zero.setComponentId(ZERO_ID);
	one.setComponentId(ONE_ID);
	verifyIds(NAMED_SELECTBASE_THREE_NAMING_CONTAINER_CASE,
		  context,
		  zero, 
		  one, 
		  zero_zero,
		  zero_one,
		  one_zero,
		  one_one);
    }

    public void doDuplicateSelectItemIdsInNamedParent() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();
	UISelectItem 
	    zero_zero = new UISelectItem(),
	    zero_one = new UISelectItem(),
	    one_zero = new UISelectItem(),
	    one_one = new UISelectItem();

	// Mimic the ordering in JSP
	zero.setComponentId("shipType");
	component.addChild(zero);

	zero_zero.setComponentId("nextDay");
	zero.addChild(zero_zero);
	zero_one.setComponentId("nextWeek");
	zero.addChild(zero_one);

	one.setComponentId("verticalRadio");
	component.addChild(one);

	one_zero.setComponentId("nextDay");
	one.addChild(one_zero);
	one_one.setComponentId("nextWeek");
	one.addChild(one_one);

    }

    public void doDuplicateSelectItemIdsInUnnamedParent() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();
	UISelectItem 
	    zero_zero = new UISelectItem(),
	    zero_one = new UISelectItem(),
	    one_zero = new UISelectItem(),
	    one_one = new UISelectItem();

	// Mimic the ordering in JSP
	component.addChild(zero);

	zero_zero.setComponentId("nextDay");
	zero.addChild(zero_zero);
	zero_one.setComponentId("nextWeek");
	zero.addChild(zero_one);

	component.addChild(one);

	one_zero.setComponentId("nextDay");
	one.addChild(one_zero);
	one_one.setComponentId("nextWeek");
	one.addChild(one_one);

    }

    public void doDuplicateSelectBaseIds() {

	FacesContext context = new MockFacesContext();
	component = new UINamingContainer();
	UISelectBase 
	    zero = newUISelectBaseSubclass(), 
	    one = newUISelectBaseSubclass();

	// Mimic the ordering in JSP
	boolean exceptionThrown = false;
	try {
	    zero.setComponentId("shipType");
	    component.addChild(zero);
	    one.setComponentId("shipType");
	    component.addChild(one);
	}
	catch (IllegalStateException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

    }

}
