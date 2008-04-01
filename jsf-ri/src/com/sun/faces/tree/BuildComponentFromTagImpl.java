/*
 * $Id: BuildComponentFromTagImpl.java,v 1.3 2002/06/25 21:42:54 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// BuildComponentFromTagImpl.java

package com.sun.faces.tree;

import java.util.Hashtable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.component.SelectItem;
import javax.faces.component.UIForm;

import org.xml.sax.Attributes;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.util.Util;

/**
 *
 *  <B>BuildComponentFromTagImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: BuildComponentFromTagImpl.java,v 1.3 2002/06/25 21:42:54 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class BuildComponentFromTagImpl extends Object implements BuildComponentFromTag
{
//
// Protected Constants
//

protected static String PARENT = "faces.parent:";

protected static String PARENT_SELECTONE = PARENT + "javax.faces.UISelectOne";

/** stores model value of the form temporarily to support implicit mapping
 * of components to properties in the bean.
 */
protected static String FORM_MODELREF = "faces.FORM_MODELREF";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

/**

* Store the mapping between shortTagName and component class

*/

protected Hashtable classMap;


//
// Constructors and Initializers    
//

public BuildComponentFromTagImpl()
{
    super();
    initializeClassMap();
}

//
// Class methods
//

//
// General Methods
//

/**

* This is a total RAD hack.

*/

private void initializeClassMap()
{
    classMap = new Hashtable(30);
    // PENDING(edburns): read this from a persistent store
    classMap.put("Form", "javax.faces.component.UIForm");
    classMap.put("Command_Button", "javax.faces.component.UICommand");
    classMap.put("Command_Hyperlink", "javax.faces.component.UICommand");
    classMap.put("SelectBoolean_Checkbox", "javax.faces.component.UISelectBoolean");
    classMap.put("RadioGroup", "javax.faces.component.UISelectOne");
    classMap.put("SelectOne_Radio", PARENT_SELECTONE);
    classMap.put("SelectOne_OptionList", "javax.faces.component.UISelectOne");
    classMap.put("SelectOne_Option", PARENT_SELECTONE);
    classMap.put("Output_Text", "javax.faces.component.UIOutput");
    classMap.put("TextEntry_Input", "javax.faces.component.UITextEntry");
    classMap.put("TextEntry_Secret", "javax.faces.component.UITextEntry");
    classMap.put("TextEntry_TextArea", "javax.faces.component.UITextEntry");
    classMap.put("Errors", "javax.faces.component.UIOutput");
}

protected boolean isSupportedTag(String shortTagName)
{
    return (null != classMap.get(shortTagName));
}

/**

* @return the property name in the UIComponent class that corresponds to
* the attrName.

*/

private String mapAttrNameToPropertyName(String attrName) {
    if (null == attrName) {
	return attrName;
    }

    if (attrName.equals("id")) {
        attrName = "componentId";
    }

    if (attrName.equals("converter")) {
        attrName = "converterReference";
    }

    if (attrName.equals("selectedValueModel")) {
        attrName = "selectedModelReference";
    }
    return attrName;
}


/**

* @return the property name in the UIComponent class that corresponds to
* the attrName.

*/

private boolean attrRequiresSpecialTreatment(String attrName) {
    boolean result = false;

    if (null == attrName) {
	return result;
    }

    if (attrName.equals("valueChangeListener") ||
	attrName.equals("commandListener") ||
	attrName.equals("formListener") ||
        attrName.equals("required") || 
        attrName.equals("format") ||
        attrName.equals("rangeMaximum") ||
        attrName.equals("lengthMaximum")) {
	result = true;
    }
    return result;
}

private void handleSpecialAttr(UIComponent child, String attrName,
			       String attrValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
    ParameterCheck.nonNull(child);
    ParameterCheck.nonNull(attrName);
    ParameterCheck.nonNull(attrValue);

    Assert.assert_it(attrRequiresSpecialTreatment(attrName));
    
    Class [] stringArg = { String.class };
    Method attrMethod = null;

    if (attrName.equals("valueChangeListener")) {
	try {
	    attrMethod = 
		child.getClass().getMethod("addValueChangeListener", 
					   stringArg);
	}
	catch (SecurityException e) {
	    System.out.println("debug: edburns: handleSpecialAttr: " + e);
	}
    }
    else if (attrName.equals("formListener")) {
	try {
	    attrMethod = 
		child.getClass().getMethod("addFormListener", 
					   stringArg);
	}
	catch (SecurityException e) {
	    System.out.println("debug: edburns: handleSpecialAttr: " + e);
	}
    } 
    else if (attrName.equals("commandListener")) {
	try {
	    attrMethod = 
		child.getClass().getMethod("addCommandListener", 
					   stringArg);
	}
	catch (SecurityException e) {
	    System.out.println("debug: edburns: handleSpecialAttr: " + e);
	}
    }

    Object [] args = { attrValue };
    if (null != attrMethod) {
	attrMethod.invoke(child, args);
    }
}


// 
// Methods from BuildComponentFromTag
//


public UIComponent createComponentForTag(String shortTagName)
{
    ParameterCheck.nonNull(shortTagName);

    UIComponent result = null;
    if (!tagHasComponent(shortTagName)) {
	return result;
    }

    String className = (String) classMap.get(shortTagName);
    Class componentClass;
    Assert.assert_it(null != className);

    // PENDING(edburns): this can be way optimized
    try {
	componentClass = Util.loadClass(className);
	result = (UIComponent) componentClass.newInstance();
    }
    catch (IllegalAccessException iae) {
	throw new RuntimeException("Can't create instance for " +
				   className + ": " + iae.getMessage());
    }
    catch (InstantiationException ie) {
	throw new RuntimeException("Can't create instance for " +
				   className + ": " + ie.getMessage());
    }
    catch (ClassNotFoundException e) {
	throw new RuntimeException("Can't find class for " + 
				   className + ": " + e.getMessage());
    }
    
    return result;
}

public boolean tagHasComponent(String shortTagName)
{
    ParameterCheck.nonNull(shortTagName);
    boolean result = false;
    String value;

    // Return true if the classMap has an entry for shortTagName, and
    // the entry does not start with the string PARENT.

    if (null != (value = (String) classMap.get(shortTagName))) {
	result = 0 != value.indexOf(PARENT);
    }
    return result;
}

public boolean isNestedComponentTag(String shortTagName)
{
    ParameterCheck.nonNull(shortTagName);
    boolean result = false;
    String value;

    // Return true if the classMap has an entry for shortTagName, and
    // the entry DOES start with the string PARENT.

    if (null != (value = (String) classMap.get(shortTagName))) {
	result = 0 == value.indexOf(PARENT);
    }
    return result;
}

public void handleNestedComponentTag(UIComponent parent, 
				     String shortTagName, Attributes attrs)
{
    ParameterCheck.nonNull(parent);
    ParameterCheck.nonNull(shortTagName);
    ParameterCheck.nonNull(attrs);

    if (null == parent || null == shortTagName || null == attrs) {
	return;
    }
    String val = (String) classMap.get(shortTagName);
    if (null == val || (0 != val.indexOf(PARENT))) {
	return;
    }
    
    // At this point, we know that we are in a nested tag.

    // PENDING(edburns): check that parent is really the correct parent
    // for this shortTagName, for now, it's just UISelectOne

    UISelectOne uiSelectOne = (UISelectOne) parent;

    int attrLen, i = 0;
    String attrName, attrValue;
    String itemLabel = null, itemValue = null, itemDesc = null;
    boolean checked = false;

    attrLen = attrs.getLength();
    for (i = 0; i < attrLen; i++) {
	attrName = mapAttrNameToPropertyName(attrs.getLocalName(i));
	attrValue = attrs.getValue(i);

	if (attrName.equals("value")) {
	    itemValue = attrValue;
	}
	if (attrName.equals("label")) {
	    itemLabel = attrValue;
	}
	if (attrName.equals("description")) {
	    itemDesc = attrValue;
	}
	if ((attrName.equals("checked") || attrName.equals("selected")) &&
	    attrValue.equals("true")) {
	    checked = true;
	}
    }


    /**********
	       I hate to comment out code, but I need to get my testcase
	       working before the fix for UISelectOne is ready.
    
    Assert.assert_it(false, "PENDING(amy): fixme");
    SelectItem [] oldItems = uiSelectOne.getItems();
    SelectItem [] newItems = null;

    if (null != oldItems) {
	newItems = new SelectItem[oldItems.length + 1];
	System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);
	newItems[oldItems.length] = new SelectItem(itemValue, itemLabel, 
						   itemDesc);
    }
    else {
	newItems = new SelectItem[1];
	newItems[0] = new SelectItem(itemValue, itemLabel, itemDesc);
    }
	
    uiSelectOne.setItems(newItems);
    // if it is checked, make sure the model knows about it.
    // we should update selectedValue only if it is null
    // in the model bean otherwise we would be overwriting
    // the value in model bean, losing any earlier updates. 
    if (checked && null == uiSelectOne.getSelectedValue()) {
	uiSelectOne.setSelectedValue(itemValue);
    }

    ************/
}

public void applyAttributesToComponentInstance(UIComponent child, 
					       Attributes attrs)
{
    ParameterCheck.nonNull(child);
    ParameterCheck.nonNull(attrs);
    int attrLen, i = 0;
    String attrName, attrValue;

    attrLen = attrs.getLength();
    for (i = 0; i < attrLen; i++) {
	attrName = mapAttrNameToPropertyName(attrs.getLocalName(i));
	attrValue = attrs.getValue(i);

	// First, try to set it as a bean property
	try {
	    PropertyUtils.setNestedProperty(child, attrName, attrValue);
	} catch (NoSuchMethodException e) {
	    // If that doesn't work, see if it requires special
	    // treatment
	    try {
		if (attrRequiresSpecialTreatment(attrName)) {
		    handleSpecialAttr(child, attrName, attrValue);
		}
		else {
		    // If that doesn't work, this will.
		    child.setAttribute(attrName, attrValue);
		}
	    }
	    catch (IllegalAccessException innerE) {
		innerE.printStackTrace();
		System.out.println(innerE.getMessage());
		Assert.assert_it(false);
	    }
	    catch (IllegalArgumentException innerE) {
		innerE.printStackTrace();
		System.out.println(innerE.getMessage());
		Assert.assert_it(false);
	    }
	    catch (InvocationTargetException innerE) {
		innerE.printStackTrace();
		System.out.println(innerE.getMessage());
		Assert.assert_it(false);
	    }
	    catch (NoSuchMethodException innerE) {
		innerE.printStackTrace();
		System.out.println(innerE.getMessage());
		Assert.assert_it(false);
	    }
	}
	catch (InvocationTargetException e) {
	    e.printStackTrace();
	    System.out.println(e.getMessage());
	    Assert.assert_it(false);
	}
	catch (IllegalAccessException e) {
	    e.printStackTrace();
	    System.out.println(e.getMessage());
	    Assert.assert_it(false);
	}
    }
    
    // cleanup: make sure we have the necessary required attributes
    if (child.getComponentId() == null) {
	String gId = Util.generateId();
	child.setComponentId(gId);
    }
    

}

// The testcase for this class is TestTreebuilder.java

} // end of class BuildComponentFromTagImpl
