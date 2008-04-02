/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


/**
 * $Id: SelectManyCheckboxListRenderer.java,v 1.17 2003/10/30 22:15:36 jvisvanathan Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// SelectManyCheckboxListRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.SelectItemWrapper;
import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.mozilla.util.Assert;

/**
 * <B>SelectManyCheckboxListRenderer</B> is a class that renders the 
 * current value of <code>UISelectMany<code> component as a list of checkboxes.
 */

public class SelectManyCheckboxListRenderer extends MenuRenderer {
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

    public SelectManyCheckboxListRenderer() {
        super();
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

    void renderSelect (FacesContext context, UIComponent component) 
        throws IOException {
	    
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        String layoutStr;
        boolean layoutVertical = false;

        String classStr;

        if (null != (layoutStr = (String) component.getAttributes().get("layout"))) {
            layoutVertical = layoutStr.equalsIgnoreCase("PAGE_DIRECTION") ? true : false;
        }

        Iterator items = Util.getSelectItemWrappers(context, component);
        SelectItem curItem = null;
        SelectItemWrapper curItemWrapper = null;
        UIComponent curComponent;
        Object selectedValues[] = getCurrentSelectedValues(context, component);

        while (items.hasNext()) {
            curItemWrapper = (SelectItemWrapper) items.next();
            curItem = curItemWrapper.getSelectItem();
            curComponent = curItemWrapper.getUISelectItem();

            // disable the radio button if the attribute is set.
            String labelClass = null;
            if ( curItem.isDisabled()){
                labelClass = (String) component.
                    getAttributes().get("disabledClass");
            } else {
                labelClass = (String) component.
                    getAttributes().get("enabledClass");
            }
            
	    writer.writeText("\n", null);
            writer.startElement("label", curComponent);
	    writer.writeAttribute("for", component.getClientId(context), "clientId");
	    writer.startElement("input", component);
	    writer.writeAttribute("name", component.getClientId(context), "clientId");
	    writer.writeAttribute("id", component.getClientId(context), "clientId");
	    writer.writeAttribute("value",
	        getFormattedValue(context, component, curItem.getValue()), "value");
	    writer.writeAttribute("type", "checkbox", "type");
	    String selectText = getSelectedText(curItem, selectedValues);
	    if (!selectText.equals("")) {
	        writer.writeAttribute(selectText, new Boolean("true"), null);
	    }
            if ( curItem.isDisabled()) {
                writer.writeAttribute("disabled", "disabled", "disabled");
            }
            
            // PENDING (visvan) Apply HTML 4.x attributes specified on selectone 
            // component to all items in the list. This might need to be changed
            // later.
            Util.renderPassThruAttributes(writer, component);
            Util.renderBooleanPassThruAttributes(writer, component);
            
            // apply any styleClass specified on the label.
            if ( labelClass != null) {
                writer.startElement("span", component);
	        writer.writeAttribute("class", labelClass, "labelClass");
            }
            writer.writeText(curItem.getLabel(), "label");
            if (null != labelClass) {
	        writer.endElement("span");
	    }
	    writer.endElement("label");
            if (layoutVertical) {
                writer.startElement("br", curComponent);
                writer.endElement("br");
	    }
        }
    }
	
    String getSelectedTextString() {
        return " checked";
    }
	

} // end of class SelectManyCheckboxListRenderer
