/*
 * $Id: RadioRenderer.java,v 1.11 2002/03/13 18:04:24 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RadioRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.UIComponent;
import javax.faces.UISelectOne;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>RadioRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RadioRenderer.java,v 1.11 2002/03/13 18:04:24 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class RadioRenderer extends Object implements Renderer {
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public RadioRenderer() {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init() {
        // super.init();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public boolean supportsType(UIComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof UISelectOne ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_UISELECTONE)) {
            supports = true;
        }
        return supports;
    }

    public Iterator getSupportedAttributeNames(String componentType) throws FacesException {
        return null;
    }

    public Iterator getSupportedAttributes(String componentType) throws FacesException {
	return null;
    }

    public PropertyDescriptor getAttributeDescriptor(String attributeName)
	throws FacesException {
	return null;
    }


    public void renderStart(RenderContext rc, UIComponent c) 
        throws IOException, FacesException {
        return;
    }

    public void renderChildren(RenderContext rc, UIComponent c) 
        throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, UIComponent c) 
            throws IOException,FacesException {
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        UISelectOne uiSelectOne = null;
        if ( supportsType(c)) {
            uiSelectOne = (UISelectOne) c;
        } else {
            throw new FacesException("Invalid component type. " +
                                     "Expected UISelectOne");
        }
	Iterator itemsIter;
	String curValue;
        String radioId = uiSelectOne.getId();
        Assert.assert_it(null != radioId);

        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );

        StringBuffer output = new StringBuffer();

        String selectedValue = (String) uiSelectOne.getSelectedValue(rc);

	// We require a way to tell which item in the UISelectOne's
	// collection maps to this tag instance.  We do this by sticking
	// an Iterator in the component's attribute set.
	itemsIter = (Iterator) uiSelectOne.getAttribute(rc, "curValue");
	if (null == itemsIter) {
	    // this must be the first one.
	    itemsIter = uiSelectOne.getItems(rc);
	    // Store the item iter in the attr set, for the next RadioTag.
	    uiSelectOne.setAttribute("curValue", itemsIter);
	}
	Assert.assert_it(null != itemsIter);

	UISelectOne.Item item = (UISelectOne.Item)itemsIter.next();
	Assert.assert_it(null != item);
	if (!itemsIter.hasNext()) {
	    // if this is the last item in the collection, remove our
	    // temporary Iterator from the attr set.
	    uiSelectOne.setAttribute("curValue", null);
	}

	curValue = item.getValue();
	String itemLabel = item.getLabel();
	output.append("<INPUT TYPE=\"RADIO\"");
	if ((null != selectedValue) &&
	    selectedValue.equals(item.getValue())) {
	    output.append(" CHECKED");
	}
	output.append(" NAME=\"");
	output.append(radioId);
	output.append("\" VALUE=\"");
	output.append(item.getValue());
	output.append("\">");
	if (itemLabel != null) {
	    output.append(" ");
	    output.append(itemLabel);
	}
	
	outputMethod.writeText(output.toString());
	outputMethod.flush();
	
        return;
    }

    public boolean getCanRenderChildren(RenderContext rc, UIComponent c) {
        return false;
    }


} // end of class RadioRenderer
