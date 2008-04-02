/*
 * $Id: RadioRenderer.java,v 1.58 2004/01/06 14:53:21 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RadioRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.model.SelectItem;
import javax.faces.component.UISelectOne;
import javax.faces.context.ResponseWriter;

import com.sun.faces.util.Util;

import java.io.IOException;

import com.sun.faces.util.Util;

/**
 * <B>ReadoRenderer</B> is a class that renders the current value of 
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of 
 * radio buttons
 */

public class RadioRenderer extends SelectManyCheckboxListRenderer {
    
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
    protected void renderOption(FacesContext context, UIComponent component,
            SelectItem curItem, boolean alignVertical ) throws IOException {
                
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null );
        
        UISelectOne selectOne = (UISelectOne) component;
        Object curValue = selectOne.getSubmittedValue();
        if (curValue == null) {
            curValue = selectOne.getValue();
        }
        
        if (alignVertical) {
            writer.writeText("\t", null);
            writer.startElement("tr", component);
            writer.writeText("\n", null);
        }
        
        // disable the radio button if the attribute is set.
        String labelClass = null;
        if ( curItem.isDisabled() ){
            labelClass = (String) component.
                getAttributes().get("disabledClass");
        } else {
            labelClass = (String) component.
                getAttributes().get("enabledClass");
        }
        writer.startElement("td", component);
        writer.startElement("input", component);
        writer.writeAttribute("type", "radio", "type");
        if (null != curItem.getValue() &&
            curItem.getValue().equals(curValue)){
            writer.writeAttribute("checked", new Boolean("true"), null);
        }
        writer.writeAttribute("name", component.getClientId(context), 
                "clientId");
        writer.writeAttribute("value",(getFormattedValue(context, component,
            curItem.getValue())), "value");

        if (curItem.isDisabled()) {
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
            writer.writeText(itemLabel, null);
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
        writer.endElement("td");
        writer.writeText("\n", null);
        if (alignVertical) {
            writer.writeText("\t", null);
            writer.endElement("tr");
            writer.writeText("\n", null);
        }
    }

} // end of class RadioRenderer
