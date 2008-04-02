/*
 * $Id: RadioRenderer.java,v 1.76 2006/03/29 22:38:38 rlubke Exp $
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

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import java.io.IOException;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.Util;

/**
 * <B>ReadoRenderer</B> is a class that renders the current value of
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of
 * radio buttons
 */

public class RadioRenderer extends SelectManyCheckboxListRenderer {

    // ------------------------------------------------------------ Constructors


    public RadioRenderer() {

        super();

    }

    // ------------------------------------------------------- Protected Methods


    protected void renderOption(FacesContext context, UIComponent component,
                                SelectItem curItem, boolean alignVertical,
                                int itemNumber)
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

        Class type = String.class;
        if (curValue != null) {
            type = curValue.getClass();
        }
        Object itemValue = curItem.getValue();
        Object newValue = context.getApplication().getExpressionFactory().
              coerceToType(itemValue, type);

        // disable the radio button if the attribute is set.
        String labelClass = null;
        boolean componentDisabled = Util.componentIsDisabled(component);

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

        if (newValue.equals(curValue)) {
            writer.writeAttribute("checked", Boolean.TRUE, null);
        }
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        String idString =
              component.getClientId(context) + NamingContainer.SEPARATOR_CHAR +
              Integer.toString(itemNumber);
        writer.writeAttribute("id", idString, "id");

        writer.writeAttribute("value",
                              (getFormattedValue(context, component,
                                                 curItem.getValue())),
                              "value");

        // Don't render the disabled attribute twice if the 'parent'
        // component is already marked disabled.
        if (!Util.componentIsDisabled(component)) {
            if (curItem.isDisabled()) {
                writer.writeAttribute("disabled", true, "disabled");
            }
        }
        // Apply HTML 4.x attributes specified on UISelectMany component to all 
        // items in the list except styleClass and style which are rendered as
        // attributes of outer most table.
        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                component,
                                                new String[]{"border",
                                                             "style"});
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer,
                                                         component);


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
