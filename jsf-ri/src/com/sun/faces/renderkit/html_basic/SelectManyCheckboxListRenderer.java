/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


/**
 * $Id: SelectManyCheckboxListRenderer.java,v 1.20 2003/12/17 15:13:57 rkitain Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// SelectManyCheckboxListRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import com.sun.faces.util.Util;

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
        Util.doAssert(writer != null );
       
        String alignStr = null;
	Object borderObj = null;
	boolean alignVertical = false;
	int border = 0;

        if (null != (alignStr = (String) component.getAttributes().get("layout"))) {
	    alignVertical = alignStr.equalsIgnoreCase("PAGE_DIRECTION") ? 
		true : false;
	}
	if (null != (borderObj = component.getAttributes().get("border"))){
	    if (borderObj instanceof Integer) {
		border = ((Integer)borderObj).intValue();
	    }
	    else {
		try {
		    border = Integer.valueOf(borderObj.toString()).intValue();
		}
		catch (Throwable e) {
		    border = 0;
		}
	    }
	}
        
	renderBeginText(component, border, alignVertical, context);
        
        Iterator items = Util.getSelectItems(context, component);
        SelectItem curItem = null;
        while (items.hasNext()) {
            curItem = (SelectItem) items.next();
            // If we come across a group of options, render them as a nested
            // table.
	    if ( curItem instanceof SelectItemGroup) {
                renderBeginText(component, border, alignVertical, 
                        context);
                // render options of this group.
                SelectItem[] itemsArray = 
                    ((SelectItemGroup)curItem).getSelectItems();
                for ( int i = 0; i < itemsArray.length; ++i ) {
                    renderOption(context, component, itemsArray[i], 
                            alignVertical);
                }
                renderEndText(alignVertical, context);
            } else {
                renderOption(context, component, curItem, alignVertical);
            }
        }
        renderEndText(alignVertical, context);
    }
    
    protected void renderOption(FacesContext context, UIComponent component,
            SelectItem curItem, boolean alignVertical ) throws IOException {
                
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null );
        
        Object selectedValues[] = getCurrentSelectedValues(context, component);
        
        // disable the radio button if the attribute is set.
        String labelClass = null;
        if ( curItem.isDisabled()){
            labelClass = (String) component.
                getAttributes().get("disabledClass");
        } else {
            labelClass = (String) component.
                getAttributes().get("enabledClass");
        }
        if (alignVertical) {
            writer.writeText("\t", null);
            writer.startElement("tr", component);
            writer.writeText("\n", null);
	}
        writer.startElement("td", component);
        writer.writeText("\n", null);
        writer.startElement("label", component);
        writer.writeAttribute("for", component.getClientId(context), 
                "clientId");
        writer.startElement("input", component);
        writer.writeAttribute("name", component.getClientId(context), 
               "clientId");
        writer.writeAttribute("id", component.getClientId(context), 
                "clientId");
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
        String itemLabel = curItem.getLabel();
        if (itemLabel != null) {
            writer.writeText(" ", null);
            writer.writeText(itemLabel, "label");
        }
	writer.endElement("input");
        // apply any styleClass specified on the label.
        if ( labelClass != null) {
            writer.startElement("span", component);
            writer.writeAttribute("class", labelClass, "labelClass");
        }
        if (null != labelClass) {
            writer.endElement("span");
        }
        writer.endElement("label");
        writer.endElement("td");
	writer.writeText("\n", null);
	if (alignVertical) {
	    writer.writeText("\t", null);
            writer.endElement("tr");
            writer.writeText("\n", null);
        }
    }
    
    String getSelectedTextString() {
        return " checked";
    }
    
    protected void renderBeginText (UIComponent component, int border, 
           boolean alignVertical, FacesContext context ) throws IOException {
            
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null );
        
	writer.startElement("table", component);
         if (border != Integer.MIN_VALUE) {
            writer.writeAttribute("border", new Integer(border), "border");
        }
        writer.writeText("\n", null);

	if (!alignVertical) {
            writer.writeText("\t", null);
	    writer.startElement("tr", component);
	    writer.writeText("\n", null);
	}
    }
    
    protected void renderEndText(boolean alignVertical,
            FacesContext context ) throws IOException {
                
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null );
        
        if (!alignVertical) {
	    writer.writeText("\t", null);
	    writer.endElement("tr");
	    writer.writeText("\n", null);
	}
        writer.endElement("table");
    }
    
} // end of class SelectManyCheckboxListRenderer
