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

/**
 * $Id: SelectManyCheckboxListRenderer.java,v 1.51 2006/09/14 22:38:41 tony_robertson Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// SelectManyCheckboxListRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.sun.faces.application.ConverterPropertyEditorBase;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <B>SelectManyCheckboxListRenderer</B> is a class that renders the
 * current value of <code>UISelectMany<code> component as a list of checkboxes.
 */

public class SelectManyCheckboxListRenderer extends MenuRenderer {

    // ---------------------------------------------------------- Public Methods


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "component"));
        }

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String alignStr = null;
        Object borderObj = null;
        boolean alignVertical = false;
        int border = 0;

        if (null !=
            (alignStr = (String) component.getAttributes().get("layout"))) {
            alignVertical = alignStr.equalsIgnoreCase("pageDirection");
        }
        if (null != (borderObj = component.getAttributes().get("border"))) {
            border = (Integer) borderObj;
        }

        renderBeginText(component, border, alignVertical, context, true);

        Iterator items = RenderKitUtils.getSelectItems(context, component);
        SelectItem curItem = null;
        int idx = -1;
        while (items.hasNext()) {
            curItem = (SelectItem) items.next();
            idx++;
            // If we come across a group of options, render them as a nested
            // table.
            if (curItem instanceof SelectItemGroup) {
                // write out the label for the group.
                if (curItem.getLabel() != null) {
                    if (alignVertical) {
                        writer.startElement("tr", component);
                    }
                    writer.startElement("td", component);
                    writer.writeText(curItem.getLabel(), component, "label");
                    writer.endElement("td");
                    if (alignVertical) {
                        writer.endElement("tr");
                    }

                }
                if (alignVertical) {
                    writer.startElement("tr", component);
                }
                writer.startElement("td", component);
                writer.writeText("\n", component, null);
                renderBeginText(component, 0, alignVertical,
                                context, false);
                // render options of this group.
                SelectItem[] itemsArray =
                      ((SelectItemGroup) curItem).getSelectItems();
                for (int i = 0; i < itemsArray.length; ++i) {
                    renderOption(context, component, itemsArray[i],
                                 alignVertical, i);
                }
                renderEndText(component, alignVertical, context, false);
                writer.endElement("td");
                if (alignVertical) {
                    writer.endElement("tr");
                    writer.writeText("\n", component, null);
                }
            } else {
                renderOption(context, component, curItem, alignVertical, idx);
            }
        }

        renderEndText(component, alignVertical, context, true);

    }

    // ------------------------------------------------------- Protected Methods


    protected void renderBeginText(UIComponent component, int border,
                                   boolean alignVertical, FacesContext context,
                                   boolean outerTable)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        writer.startElement("table", component);
        if (border != Integer.MIN_VALUE) {
            writer.writeAttribute("border", border, "border");
        }

        // render style and styleclass attribute on the outer table instead of 
        // rendering it as pass through attribute on every option in the list.
        if (outerTable) {
            // render "id" only for outerTable.
            if (shouldWriteIdAttribute(component)) {
                writeIdAttributeIfNecessary(context, writer, component);
            }
            String styleClass = (String) component.getAttributes().get(
                  "styleClass");
            String style = (String) component.getAttributes().get("style");
            if (styleClass != null) {
                writer.writeAttribute("class", styleClass, "class");
            }
            if (style != null) {
                writer.writeAttribute("style", style, "style");
            }
        }
        writer.writeText("\n", component, null);

        if (!alignVertical) {
            writer.writeText("\t", component, null);
            writer.startElement("tr", component);
            writer.writeText("\n", component, null);
        }

    }


    protected void renderEndText(UIComponent component, boolean alignVertical,
                                 FacesContext context, boolean outerTable)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        if (!alignVertical) {
            writer.writeText("\t", component, null);
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }
        writer.endElement("table");

    }


    protected void renderOption(FacesContext context, UIComponent component,
                                SelectItem curItem, boolean alignVertical,
                                int itemNumber)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        // disable the check box if the attribute is set.
        String labelClass = null;
        boolean componentDisabled = Util.componentIsDisabled(component);

        if (componentDisabled || curItem.isDisabled()) {
            labelClass = (String) component.
                  getAttributes().get("disabledClass");
        } else {
            labelClass = (String) component.
                  getAttributes().get("enabledClass");
        }
        if (alignVertical) {
            writer.writeText("\t", component, null);
            writer.startElement("tr", component);
            writer.writeText("\n", component, null);
        }
        writer.startElement("td", component);
        writer.writeText("\n", component, null);

        writer.startElement("input", component);
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        String idString =
              component.getClientId(context) + NamingContainer.SEPARATOR_CHAR +
              Integer.toString(itemNumber);
        writer.writeAttribute("id", idString, "id");
        String valueString = getFormattedValue(context, component,
                                               curItem.getValue());
        writer.writeAttribute("value", valueString, "value");
        writer.writeAttribute("type", "checkbox", null);

        Object submittedValues[] = getSubmittedSelectedValues(context,
                                                              component);
        boolean isSelected;

        Class type = String.class;
        Object valuesArray = null;
        Object itemValue = null;
        if (submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = valueString;
        } else {
            valuesArray = getCurrentSelectedValues(context, component);
            itemValue = curItem.getValue();
        }
        if (valuesArray != null) {
            type = valuesArray.getClass().getComponentType();
        }

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(ConverterPropertyEditorBase.TARGET_COMPONENT_ATTRIBUTE_NAME, 
                component);
        Object newValue = context.getApplication().getExpressionFactory().
              coerceToType(itemValue, type);

        isSelected = isSelected(newValue, valuesArray);

        if (isSelected) {
            writer.writeAttribute(getSelectedTextString(), Boolean.TRUE, null);
        }

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
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

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
            writer.writeText(" ", component, null);
            writer.writeText(itemLabel, component, "label");
        }
        writer.endElement("label");
        writer.endElement("td");
        writer.writeText("\n", component, null);
        if (alignVertical) {
            writer.writeText("\t", component, null);
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }

    }

    // ------------------------------------------------- Package Private Methods


    String getSelectedTextString() {

        return "checked";

    }

} // end of class SelectManyCheckboxListRenderer
