/*
 * $Id: TreeBuilder.java,v 1.4 2002/03/15 20:58:03 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TreeBuilder.java

package com.sun.faces.treebuilder;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import org.apache.jasper_hacked.compiler.JspParseListener;
import javax.faces.UIComponent;
import javax.faces.UISelectOne;
import javax.faces.RenderContext;
import javax.faces.ObjectManager;
import javax.faces.FormatValidator;
import javax.faces.RangeValidator;
import javax.faces.LengthValidator;
import javax.faces.RequiredValidator;
import javax.faces.UIForm;

import com.sun.faces.util.Util;

import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import javax.servlet.ServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import org.xml.sax.Attributes;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Iterator;

import java.io.PrintStream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**

 *  This gets called for each tag.  We use it to build the component
 *  tree.  This implementation is optimized only to be fast to
 *  implement.  This implemenentation is tightly bound to the html_basic
 *  custom tag library.  A better architecture would be to have this
 *  class be given a reference to a class that encapsulates the
 *  specifics of this particularly taglib.  This current implementation
 *  does not allow tags to be added without modifying this class as
 *  well.<P>

 * <B>Lifetime And Scope</B> <P>

 * There is one instance of TreeBuilder for each time you want to parse
 * a tree.  Instances are not intended to be reused. </P>

 *
 *
 * @version $Id: TreeBuilder.java,v 1.4 2002/03/15 20:58:03 jvisvanathan Exp $
 * 
 * @see	com.sun.faces.treebuilder.TreeEngine#getTreeForURI
 *
 */

public class TreeBuilder extends Object implements JspParseListener
{
//
// Protected Constants
//

protected static String PARENT = "parent:";

protected static String PARENT_SELECTONE = PARENT + "javax.faces.UISelectOne";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables
// stores model value of the form temporarily to support implicit mapping
// of components to properties in the bean.
private String formModelRef = null;

// Relationship Instance Variables

private RenderContext renderContext;
private String requestURI;
private UIComponent root = null;

/**

* Store the mapping between shortTagName and component class

*/

protected Hashtable classMap;
private Stack tagStack;

private int curDepth = 0;

//
// Constructors and Initializers    
//

public TreeBuilder(RenderContext newRenderContext, String newRequestURI)
{
    ParameterCheck.nonNull(newRenderContext);
    ParameterCheck.nonNull(newRequestURI);
    renderContext = newRenderContext;
    requestURI = newRequestURI;
    root = null;
    curDepth = 0;
    initializeClassMap();
}

//
// Class methods
//

//
// General Methods
//

public UIComponent getRoot() {
    return root;
}

public void printTree(UIComponent root, PrintStream out) {
    if (null == root) {
	return;
    }
    int i = 0;

    // handle indentation
    for (i = 0; i < curDepth; i++) {
	out.print("  ");
    }
    out.print(root.getId());
    if (root instanceof UISelectOne) {
	Iterator it = ((UISelectOne)root).getItems(renderContext);
	out.print(" {\n");
	while (it.hasNext()) {
	    for (i = 0; i < curDepth; i++) {
		out.print("  ");
	    }
	    
	    UISelectOne.Item curItem = (UISelectOne.Item) it.next();
	    out.print("\t value=" + 
		      curItem.getValue() + 
		      " label=" + 
		      curItem.getLabel() +
		      " description=" + 
		      curItem.getDescription() + "\n");
	}
	for (i = 0; i < curDepth; i++) {
	    out.print("  ");
	}
	out.print(" }\n");
    }
    else {
	out.print(" value=" + root.getValue(renderContext) + "\n");
    }
    curDepth++;
    Iterator it = root.getChildren(renderContext);
    while (it.hasNext()) {
	printTree((UIComponent) it.next(), out);
    }
    curDepth--;
}

//
// classMap methods
//

    /**

    * This is a total RAD hack.

    */

private void initializeClassMap()
{
    classMap = new Hashtable(30);
    // PENDING(edburns): read this from a persistent store
    classMap.put("Form", "javax.faces.UIForm");
    classMap.put("Command_Button", "javax.faces.UICommand");
    classMap.put("Command_Hyperlink", "javax.faces.UICommand");
    classMap.put("SelectBoolean_Checkbox", "javax.faces.UISelectBoolean");
    classMap.put("RadioGroup", "javax.faces.UISelectOne");
    classMap.put("SelectOne_Radio", PARENT_SELECTONE);
    classMap.put("SelectOne_OptionList", "javax.faces.UISelectOne");
    classMap.put("SelectOne_Option", PARENT_SELECTONE);
    classMap.put("Output_Text", "javax.faces.UIOutput");
    classMap.put("TextEntry_Input", "javax.faces.UITextEntry");
    classMap.put("TextEntry_Secret", "javax.faces.UITextEntry");
    classMap.put("TextEntry_TextArea", "javax.faces.UITextEntry");
    classMap.put("Errors", "javax.faces.UIOutput");
    tagStack = new Stack();
}

private UIComponent createComponentForTag(String shortTagName) {
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


protected boolean isSupportedTag(String shortTagName) {
    return (null != classMap.get(shortTagName));
}

/**

* @return true if this tag has a component associated with it

*/

protected boolean tagHasComponent(String shortTagName) {
    boolean result = false;
    String value;

    // Return true if the classMap has an entry for shortTagName, and
    // the entry does not start with the string PARENT.

    if (null != (value = (String) classMap.get(shortTagName))) {
	result = 0 != value.indexOf(PARENT);
    }
    return result;
}

/**

* @return the property name in the UIComponent class that corresponds to
* the attrName.

*/

private String mapAttrNameToPropertyName(String attrName) {
    if (null == attrName) {
	return attrName;
    }

    if (attrName.equals("model")) {
        attrName = "modelReference";
    }

    if (attrName.equals("converter")) {
        attrName = "converterReference";
    }

    if (attrName.equals("selectedValueModel")) {
        attrName = "selectedModelReference";
    }
    return attrName;
}

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

    ObjectManager objectManager = renderContext.getObjectManager();
    Assert.assert_it(objectManager != null );

    ServletRequest request = renderContext.getRequest();
    Assert.assert_it(request != null );

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
    else if (attrName.equals("required")) {
        if ( attrValue.equals("true")) {
            // create required validator
            child.setAttribute("required", attrValue);

            RequiredValidator reqValidator = new RequiredValidator();
            String reqValidatorId = Util.generateId();
            objectManager.put( request, reqValidatorId, reqValidator);

            attrMethod = child.getClass().getMethod("addValidator", stringArg);
            attrValue = reqValidatorId;
        }
    }

    else if (attrName.equals("format")) {
        // create format validator
        child.setAttribute("format", attrValue);

        FormatValidator formatValidator = new FormatValidator();
        String formatValidatorId = Util.generateId();
        objectManager.put( request, formatValidatorId, formatValidator);

        attrMethod = child.getClass().getMethod("addValidator", stringArg);
        attrValue = formatValidatorId;
    }

    else if (attrName.equals("rangeMaximum")) {
        // create Range validator
        child.setAttribute("rangeMaximum", attrValue);

        RangeValidator rangeValidator = new RangeValidator();
        String rangeValidatorId = Util.generateId();
        objectManager.put( request, rangeValidatorId, rangeValidator);

        attrMethod = child.getClass().getMethod("addValidator", stringArg);
        attrValue = rangeValidatorId;
    }

    else if (attrName.equals("lengthMaximum")) {
        // create length validator
        child.setAttribute("lengthMaximum", attrValue);

        LengthValidator lengthValidator = new LengthValidator();
        String lengthValidatorId = Util.generateId();
        objectManager.put( request, lengthValidatorId, lengthValidator);

        attrMethod = child.getClass().getMethod("addValidator", stringArg);
        attrValue = lengthValidatorId;
    }

    Object [] args = { attrValue };
    if (null != attrMethod) {
	attrMethod.invoke(child, args);
    }
}

private void handleNestedComponentTag(UIComponent parent, String shortTagName,
				      Attributes attrs) {
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
    uiSelectOne.addItem(itemValue, itemLabel, itemDesc);
    // if it is checked, make sure the model knows about it.
    // we should update selectedValue only if it is null
    // in the model bean otherwise we would be overwriting
    // the value in model bean, losing any earlier updates. 
    if (checked && null == uiSelectOne.getSelectedValue(renderContext)) {
	uiSelectOne.setSelectedValue(itemValue);
    }
}


//
// Methods from JspParseListener
//

public void handleTagBegin(Attributes attrs, String prefix,
			   String shortTagName, TagLibraryInfo tli,
			   TagInfo ti, boolean hasBody, boolean isXml)
{
    if (!prefix.equalsIgnoreCase("faces")) {
	return;
    }
    UIComponent child = null, parent = null;
    String attrName, attrValue;

    if (!tagStack.empty()) {
	parent = (UIComponent) tagStack.peek();
    }
    int attrLen, i = 0;
    
    if (null == (child = createComponentForTag(shortTagName))) {
	// This isn't a tag that has a component.  See if it is a tag
	// that has a nested relationship to a component, such as
	// SelectOne
	handleNestedComponentTag(parent, shortTagName, attrs);
	return;
    }
    
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
        // save Form's modelReference so that the children can get the model
        // bean if it is not explicitly specified.
        if ( attrName.equals("modelReference") && child instanceof UIForm ) {
            formModelRef = attrValue;
        }
    }
    // If model attribute is not set for a component get it from the 
    // parent form if it exists.
    if (child.getModelReference() == null && formModelRef != null && 
            !(child instanceof UIForm) ) {
       child.setModelReference("$" + formModelRef + "." + child.getId());     
    }   
    
    // cleanup: make sure we have the necessary required attributes
    if (child.getId() == null) {
	String gId = Util.generateId();
	child.setId(gId);
    }
    
    Assert.assert_it(null != renderContext);
    if (tagHasComponent(shortTagName)) {
	if (null == root) {
	    root = child;
	}
	
	if (null != parent) {
	    parent.add(child);
	}
	
	tagStack.push(child);
    }
    else {
	// don't wire this tag up, just call setAttributes directly
    }
}

public void handleTagEnd(Attributes attrs, String prefix,
			 String shortTagName, TagLibraryInfo tli,
			 TagInfo ti, boolean hasBody)
{
    if (!prefix.equalsIgnoreCase("faces")) {
	return;
    }

    if (!tagHasComponent(shortTagName)) {
	// This isn't a tag that has a component
	return;
    }
    tagStack.pop();
    // null out formModelRef once we have finished processing a form.
    if ( shortTagName.equals("Form")) {
        formModelRef = null;
    }
}


// Delete this text and replace the below text with the name of the file
// containing the testcase covering this class.  If this text is here
// someone touching this file is poor software engineer.

// The testcase for this class is TestTreeBuilder.java 


} // end of class TreeBuilder
