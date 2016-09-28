/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package com.sun.faces.renderkit.html_basic;

import static com.sun.faces.renderkit.RenderKitUtils.getSelectItems;
import static com.sun.faces.renderkit.RenderKitUtils.renderPassThruAttributes;
import static com.sun.faces.renderkit.RenderKitUtils.renderSelectOnclick;
import static com.sun.faces.renderkit.RenderKitUtils.renderXHTMLStyleBooleanAttributes;
import static com.sun.faces.util.RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME;
import static java.lang.Integer.MIN_VALUE;
import static javax.faces.component.UINamingContainer.getSeparatorChar;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.util.RequestStateManager;

/**
 * <b>SelectManyCheckboxListRenderer</b> is a class that renders the current value of <code>UISelectMany<code> component
 * as a list of checkboxes.
 */
public class SelectManyCheckboxListRenderer extends MenuRenderer {

    private static final Attribute[] ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.SELECTMANYCHECKBOX);


    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String layout = (String) component.getAttributes().get("layout");
        boolean alignVertical = "pageDirection".equalsIgnoreCase(layout);
        Integer border = (Integer) component.getAttributes().get("border");
        Converter<?> converter = ((ValueHolder) component).getConverter();

        renderBeginText(component, border, alignVertical, context, true);

        Iterator<SelectItem> items = getSelectItems(context, component);

        Object currentSelections = getCurrentSelectedValues(component);
        Object[] submittedValues = getSubmittedSelectedValues(component);
        OptionComponentInfo optionInfo = new OptionComponentInfo(component);
        int index = -1;
        while (items.hasNext()) {
            SelectItem currentItem = items.next();
            index++;

            // If we come across a group of options, render them as a nested table.
            if (currentItem instanceof SelectItemGroup) {

                // Write out the label for the group.
                if (currentItem.getLabel() != null) {
                    if (alignVertical) {
                        writer.startElement("tr", component);
                    }

                    writer.startElement("td", component);
                    writer.writeText(currentItem.getLabel(), component, "label");
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
                renderBeginText(component, 0, alignVertical, context, false);

                // Render options of this group.
                SelectItem[] itemsArray = ((SelectItemGroup) currentItem).getSelectItems();

                for (SelectItem element : itemsArray) {
                    renderOption(context, component, converter, element, currentSelections, submittedValues, alignVertical, index++, optionInfo);
                }

                renderEndText(component, alignVertical, context);
                writer.endElement("td");

                if (alignVertical) {
                    writer.endElement("tr");
                    writer.writeText("\n", component, null);
                }
            } else {
                renderOption(context, component, converter, currentItem, currentSelections, submittedValues, alignVertical, index, optionInfo);
            }
        }

        renderEndText(component, alignVertical, context);
    }


    // ----------------------------------------------------------------------------------------------- Protected Methods

    /**
     * We override isBehaviorSource since the ID of the activated check box will have been augmented with the option number.
     * @see HtmlBasicRenderer#isBehaviorSource(FacesContext, String, String)
     */
    @Override
    protected boolean isBehaviorSource(FacesContext context, String behaviorSourceId, String componentClientId) {

        if (behaviorSourceId == null) {
            return false;
        }

        char sepChar = UINamingContainer.getSeparatorChar(context);
        String actualBehaviorId;

        if (behaviorSourceId.lastIndexOf(sepChar) != -1) {
            actualBehaviorId = behaviorSourceId.substring(0, behaviorSourceId.lastIndexOf(sepChar));
        } else {
            actualBehaviorId = behaviorSourceId;
        }

        return (actualBehaviorId.equals(componentClientId));

    }

    protected void renderBeginText(UIComponent component, Integer border, boolean alignVertical,
            FacesContext context, boolean outerTable) throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        writer.startElement("table", component);

        if ((border != null) && (border != MIN_VALUE)) {
            writer.writeAttribute("border", border, "border");
        }

        // Render style and styleclass attribute on the outer table instead of rendering it as pass through attribute on
        // every option in the list.
        if (outerTable) {

            // Render "id" only for outerTable.
            if (shouldWriteIdAttribute(component)) {
                writeIdAttributeIfNecessary(context, writer, component);
            }

            renderStyleAndClassAttributes(writer, component);
        }

        writer.writeText("\n", component, null);

        if (!alignVertical) {
            writer.writeText("\t", component, null);
            writer.startElement("tr", component);
            writer.writeText("\n", component, null);
        }
    }

    protected void renderStyleAndClassAttributes(ResponseWriter writer, UIComponent component) throws IOException {
        String styleClass = (String) component.getAttributes().get("styleClass");
        String style = (String) component.getAttributes().get("style");

        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "class");
        }

        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
    }

    protected void renderEndText(UIComponent component, boolean alignVertical, FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        if (!alignVertical) {
            writer.writeText("\t", component, null);
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }

        writer.endElement("table");
    }

    protected void renderOption(FacesContext context, UIComponent component, Converter<?> converter,
            SelectItem currentItem, Object currentSelections, Object[] submittedValues,
            boolean alignVertical, int itemNumber, OptionComponentInfo optionInfo) throws IOException
    {
        String value = getFormattedValue(context, component, currentItem.getValue(), converter);
        Object values;
        Object itemValue;

        if (submittedValues != null) {
            values = submittedValues;
            itemValue = value;
        } else {
            values = currentSelections;
            itemValue = currentItem.getValue();
        }

        RequestStateManager.set(context, TARGET_COMPONENT_ATTRIBUTE_NAME, component);

        boolean selected = isSelected(context, component, itemValue, values, converter);

        if (optionInfo.isHideNoSelection() && currentItem.isNoSelectionOption() && (currentSelections != null) && !selected) {
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

        String name = component.getClientId(context);
        String clientId = name + getSeparatorChar(context) + Integer.toString(itemNumber);

        writer.startElement("input", component);
        writer.writeAttribute("name", name, "clientId");
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("value", value, "value");
        writer.writeAttribute("type", "checkbox", null);

        if (selected) {
            writer.writeAttribute(getSelectedTextString(), true, null);
        }

        // Don't render the disabled attribute twice if the 'parent' component is already marked disabled.
        if (!optionInfo.isDisabled() && currentItem.isDisabled()) {
            writer.writeAttribute("disabled", true, "disabled");
        }

        // Apply HTML 4.x attributes specified on UISelectMany component to all items in the list except styleClass and
        // style which are rendered as attributes of outer most table.
        renderPassThruAttributes(context, writer, component, ATTRIBUTES, getNonOnClickSelectBehaviors(component));
        renderXHTMLStyleBooleanAttributes(writer, component);
        renderSelectOnclick(context, component, false);

        writer.endElement("input");
        writer.startElement("label", component);
        writer.writeAttribute("for", clientId, "for");

        // Set up the label's class, if appropriate.
        StringBuilder labelClass = new StringBuilder();
        String styleClass;

        // If disabledClass or enabledClass set, add it to the label's class.
        if (optionInfo.isDisabled() || currentItem.isDisabled()) {
            styleClass = optionInfo.getDisabledClass();
        } else {  // enabled
            styleClass = optionInfo.getEnabledClass();
        }

        if (styleClass != null) {
            labelClass.append(styleClass);
        }

        // If selectedClass or unselectedClass set, add it to the label's class.
        if (isSelected(context, component, itemValue, values, converter)) {
            styleClass = optionInfo.getSelectedClass();
        } else { // not selected
            styleClass = optionInfo.getUnselectedClass();
        }

        if (styleClass != null) {
            if (labelClass.length() > 0) {
                labelClass.append(' ');
            }
            labelClass.append(styleClass);
        }

        writer.writeAttribute("class", labelClass.toString(), "labelClass");
        String itemLabel = currentItem.getLabel();

        if (itemLabel == null) {
            itemLabel = value;
        }

        writer.writeText(" ", component, null);

        if (!currentItem.isEscape()) {
            // It seems the ResponseWriter API should have a writeText() with a boolean property to determine if
            // content written should be escaped or not.
            writer.write(itemLabel);
        } else {
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


    // ----------------------------------------------------------------------------------------- Package Private Methods

    String getSelectedTextString() {

        return "checked";
    }

}