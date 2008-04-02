/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


/**
 * $Id: SelectManyCheckboxListRenderer.java,v 1.25 2004/01/27 21:04:29 eburns Exp $
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

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
       
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }    
          
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null );
       
        String alignStr = null;
	Object borderObj = null;
	boolean alignVertical = false;
	int border = 0;

        if (null != (alignStr = (String) component.getAttributes().get("layout"))) {
	    alignVertical = alignStr.equalsIgnoreCase("pageDirection") ? 
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
        // if enabledClass or disabledClass attributes are specified, apply
        // it on the label.
        if ( labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }
        
        writer.startElement("input", component);
        writer.writeAttribute("name", component.getClientId(context), 
               "clientId");
        String valueString = getFormattedValue(context, component,
                                               curItem.getValue());
        writer.writeAttribute("value", valueString, "value");
        writer.writeAttribute("type", "checkbox", null);
        boolean isSelected;
        Object submittedValues[] = getSubmittedSelectedValues(context, component);
        if (submittedValues != null) {
            isSelected = isSelected(valueString, submittedValues);
        } else {
            Object selectedValues = getCurrentSelectedValues(context,
                                                             component);
            isSelected = isSelected(curItem.getValue(), selectedValues);
        }

        if (isSelected) {
            writer.writeAttribute(getSelectedTextString(), Boolean.TRUE, null);
        }
        if ( curItem.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }

        // Apply HTML 4.x attributes specified on selectone component to all 
        // items in the list except styleClass. styleClass has been rendered as
        // an attribute of outer most table already, so temporarily null out the 
        // attribute,so that it is not rendered again as a pass through attribute.
        Object styleClass = null;
        if ( component.getAttributes().containsKey("styleClass")) {
            styleClass = component.getAttributes().get("styleClass");
            component.getAttributes().remove("styleClass");
        }
        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);
        if (styleClass != null ) {
            component.getAttributes().put("styleClass", styleClass);
        }
        
        String itemLabel = curItem.getLabel();
        if (itemLabel != null) {
            writer.writeText(" ", null);
            writer.writeText(itemLabel, "label");
        }
	writer.endElement("input");
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
        
       // render styleclass attribute on the outer table instead of rendering it
       // as pass through attribute on every option in the list.
       String styleClass = (String) component.getAttributes().get("styleClass");
       if (styleClass != null) {
           writer.writeAttribute("class", styleClass, "class");  
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
