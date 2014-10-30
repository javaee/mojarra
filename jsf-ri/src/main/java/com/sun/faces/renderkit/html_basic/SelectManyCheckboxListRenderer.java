/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/**
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// SelectManyCheckboxListRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;

/**
 * <B>SelectManyCheckboxListRenderer</B> is a class that renders the
 * current value of <code>UISelectMany<code> component as a list of checkboxes.
 */

public class SelectManyCheckboxListRenderer extends MenuRenderer {


    private static final Attribute[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.SELECTMANYCHECKBOX);

    // ---------------------------------------------------------- Public Methods


    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String alignStr;
        Object borderObj;
        boolean alignVertical = false;
        int border = 0;

        if (null !=
            (alignStr = (String) component.getAttributes().get("layout"))) {
            alignVertical = alignStr.equalsIgnoreCase("pageDirection");
        }
        if (null != (borderObj = component.getAttributes().get("border"))) {
            border = (Integer) borderObj;
        }

        Converter converter = null;
        if(component instanceof ValueHolder) {
            converter = ((ValueHolder)component).getConverter();
        }

        renderBeginText(component, border, alignVertical, context, true);

        Iterator<SelectItem> items =
              RenderKitUtils.getSelectItems(context, component);

        Object currentSelections = getCurrentSelectedValues(component);
        Object[] submittedValues = getSubmittedSelectedValues(component);
        Map<String,Object> attributes = component.getAttributes();
        OptionComponentInfo optionInfo =
              new OptionComponentInfo((String) attributes.get("disabledClass"),
                                      (String) attributes.get("enabledClass"),
                                      (String) attributes.get("unselectedClass"),
                                      (String) attributes.get("selectedClass"),
                                      Util.componentIsDisabled(component),
                                      isHideNoSelection(component));
        int idx = -1;
        while (items.hasNext()) {
            SelectItem curItem = items.next();
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
                    renderOption(context,
                                 component,
                                 converter,
                                 itemsArray[i],
                                 currentSelections,
                                 submittedValues,
                                 alignVertical,
                                 idx++,
                                 optionInfo);
                }
                renderEndText(component, alignVertical, context);
                writer.endElement("td");
                if (alignVertical) {
                    writer.endElement("tr");
                    writer.writeText("\n", component, null);
                }
            } else {
                renderOption(context,
                             component,
                             converter,
                             curItem,
                             currentSelections,
                             submittedValues,
                             alignVertical,
                             idx,
                             optionInfo);
            }
        }

        renderEndText(component, alignVertical, context);

    }

    // ------------------------------------------------------- Protected Methods


    /**
     * We override isBehaviorSource since the ID of the activated check box
     * will have been augmented with the option number.
     *
     * @see HtmlBasicRenderer#isBehaviorSource(FacesContext, String, String)
     */
    @Override
    protected boolean isBehaviorSource(FacesContext ctx,
                                       String behaviorSourceId,
                                       String componentClientId) {

        if (behaviorSourceId == null) {
            return false;
        }
        char sepChar = UINamingContainer.getSeparatorChar(ctx);
        String actualBehaviorId;
        if (behaviorSourceId.lastIndexOf(sepChar) != -1) { 
            actualBehaviorId = behaviorSourceId.substring(0, behaviorSourceId.lastIndexOf(sepChar));
        } else {
            actualBehaviorId = behaviorSourceId;
        }
        
        return (actualBehaviorId.equals(componentClientId));

    }


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


    protected void renderEndText(UIComponent component,
                                 boolean alignVertical,
                                 FacesContext context)
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


    protected void renderOption(FacesContext context,
                                UIComponent component,
                                Converter converter,
                                SelectItem curItem,
                                Object currentSelections,
                                Object[] submittedValues,
                                boolean alignVertical,
                                int itemNumber,
                                OptionComponentInfo optionInfo) throws IOException {


        String valueString = getFormattedValue(context, component,
                                               curItem.getValue(), converter);

        Object valuesArray;
        Object itemValue;
        if (submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = valueString;
        } else {
            valuesArray = currentSelections;
            itemValue = curItem.getValue();
        }

        RequestStateManager.set(context,
                                RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME,
                                component);

        boolean isSelected = isSelected(context, component, itemValue, valuesArray, converter);
        if (optionInfo.isHideNoSelection()
                && curItem.isNoSelectionOption()
                && currentSelections != null
                && !isSelected) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

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
        String idString = component.getClientId(context)
                          + UINamingContainer.getSeparatorChar(context)
                          + Integer.toString(itemNumber);
        writer.writeAttribute("id", idString, "id");

        writer.writeAttribute("value", valueString, "value");
        writer.writeAttribute("type", "checkbox", null);

        if (isSelected) {
            writer.writeAttribute(getSelectedTextString(), Boolean.TRUE, null);
        }

        // Don't render the disabled attribute twice if the 'parent'
        // component is already marked disabled.
        if (!optionInfo.isDisabled()) {
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
                                                ATTRIBUTES,
                                                getNonOnClickSelectBehaviors(component));

        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);
        
        RenderKitUtils.renderSelectOnclick(context, component, false);

        writer.endElement("input");
        writer.startElement("label", component);
        writer.writeAttribute("for", idString, "for");

        // Set up the label's class, if appropriate
        StringBuilder labelClass = new StringBuilder();
        String style;
        // If disabledClass or enabledClass set, add it to the label's class
        if (optionInfo.isDisabled() || curItem.isDisabled()) {
            style = optionInfo.getDisabledClass();
        } else {  // enabled
            style = optionInfo.getEnabledClass();
        }
        if (style != null) {
            labelClass.append(style);
        }
        // If selectedClass or unselectedClass set, add it to the label's class
        if (isSelected(context, component, itemValue, valuesArray, converter)) {
            style = optionInfo.getSelectedClass();
        } else { // not selected
            style = optionInfo.getUnselectedClass();
        }
        if (style != null) {
            if (labelClass.length() > 0) {
                labelClass.append(' ');
            }
            labelClass.append(style);
        }
        writer.writeAttribute("class", labelClass.toString(), "labelClass");
        String itemLabel = curItem.getLabel();
        if (itemLabel == null) {
            itemLabel = valueString;
        }
        writer.writeText(" ", component, null);
        if (!curItem.isEscape()) {
            // It seems the ResponseWriter API should
            // have a writeText() with a boolean property
            // to determine if it content written should
            // be escaped or not.
            writer.write(itemLabel);
        } else {
            writer.writeText(itemLabel, component, "label");
        }
//        if (isSelected(context, component, itemValue, valuesArray, converter)) {
//            
//        } else { // not selected
//            
//        }
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
