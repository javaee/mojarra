/*
 * $Id: RadioRenderer.java,v 1.62 2004/02/06 18:55:21 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RadioRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import java.io.IOException;

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
                                SelectItem curItem, boolean alignVertical)
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null);

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
        if (curItem.isDisabled()) {
            labelClass = (String) component.
                getAttributes().get("disabledClass");
        } else {
            labelClass = (String) component.
                getAttributes().get("enabledClass");
        }
        writer.startElement("td", component);
        writer.writeText("\n", null);

        writer.startElement("label", component);
        writer.writeAttribute("for", component.getClientId(context),
                              "clientId");
        // if enabledClass or disabledClass attributes are specified, apply
        // it on the label.
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }

        writer.startElement("input", component);
        writer.writeAttribute("type", "radio", "type");
        if (null != curItem.getValue() &&
            curItem.getValue().equals(curValue)) {
            writer.writeAttribute("checked", new Boolean("true"), null);
        }
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        writer.writeAttribute("value", (getFormattedValue(context, component,
                                                          curItem.getValue())), "value");

        if (curItem.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }
        // Apply HTML 4.x attributes specified on selectone component to all 
        // items in the list except styleClass. styleClass has been rendered as
        // an attribute of outer most table already, so temporarily null out the 
        // attribute,so that it is not rendered again as a pass through attribute.
        Object styleClass = null;
        if (component.getAttributes().containsKey("styleClass")) {
            styleClass = component.getAttributes().get("styleClass");
            component.getAttributes().remove("styleClass");
        }
        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);
        if (styleClass != null) {
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

} // end of class RadioRenderer
