/*
 * $Id: RadioRenderer.java,v 1.72 2005/08/22 22:10:21 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// RadioRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import javax.faces.component.NamingContainer;
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
                                SelectItem curItem, boolean alignVertical, int itemNumber)
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

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
	boolean componentDisabled = false;
	if (component.getAttributes().get("disabled") != null) {
            if ((component.getAttributes().get("disabled")).equals(Boolean.TRUE)) {
	        componentDisabled = true;
	    }
	}
        if (componentDisabled || curItem.isDisabled()) {
            labelClass = (String) component.
                getAttributes().get("disabledClass");
        } else {
            labelClass = (String) component.
                getAttributes().get("enabledClass");
        }
        writer.startElement("td", component);
        writer.writeText("\n", null);

        writer.startElement("input", component);
        writer.writeAttribute("type", "radio", "type");
        if (null != curItem.getValue() &&
            curItem.getValue().equals(curValue)) {
            writer.writeAttribute("checked", Boolean.TRUE, null);
        }
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        String idString = component.getClientId(context) + NamingContainer.SEPARATOR_CHAR +
            new Integer(itemNumber).toString();
        writer.writeAttribute("id", idString, "id");

        writer.writeAttribute("value", (getFormattedValue(context, component,
                                                          curItem.getValue())), "value");

        if (curItem.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }
        // Apply HTML 4.x attributes specified on UISelectMany component to all 
        // items in the list except styleClass and style which are rendered as
        // attributes of outer most table.
        Util.renderPassThruAttributes(context, writer, component,
                                      new String[]{"style", "border"});

        Util.renderBooleanPassThruAttributes(writer, component);

        writer.endElement("input");
        writer.startElement("label", component);
        writer.writeAttribute("for", idString, "for");
        // if enabledClass or disabledClass attributes are specified, apply
        // it on the label.
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }
        String itemLabel = curItem.getLabel();
        if (itemLabel != null) {
            writer.writeText(" ", null);
            writer.writeText(itemLabel, "label");
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

} // end of class RadioRenderer
