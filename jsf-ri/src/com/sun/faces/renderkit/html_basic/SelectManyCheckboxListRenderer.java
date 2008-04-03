/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
 * $Id: SelectManyCheckboxListRenderer.java,v 1.61 2007/12/17 21:46:10 rlubke Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// SelectManyCheckboxListRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.List;

import javax.el.ELException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;

/**
 * <B>SelectManyCheckboxListRenderer</B> is a class that renders the
 * current value of <code>UISelectMany<code> component as a list of checkboxes.
 */

public class SelectManyCheckboxListRenderer extends MenuRenderer {


    private static final String[] ATTRIBUTES =
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

        List<SelectItem> items = RenderKitUtils.getSelectItems(context, component);

        if (!items.isEmpty()) {
            int idx = -1;
            for (SelectItem curItem : items) {
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
                        renderOption(context, component, converter, itemsArray[i],
                                     alignVertical, i);
                    }
                    renderEndText(component, alignVertical, context);
                    writer.endElement("td");
                    if (alignVertical) {
                        writer.endElement("tr");
                        writer.writeText("\n", component, null);
                    }
                } else {
                    renderOption(context, component, converter, curItem, alignVertical, idx);
                }
            }
        }

        renderEndText(component, alignVertical, context);

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


    protected void renderOption(FacesContext context, UIComponent component,
	    			Converter converter, SelectItem curItem,
	    			boolean alignVertical, int itemNumber)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        // disable the check box if the attribute is set.
        boolean componentDisabled = Util.componentIsDisabled(component);

        String labelClass;
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
                                               curItem.getValue(), converter);
        writer.writeAttribute("value", valueString, "value");
        writer.writeAttribute("type", "checkbox", null);

        Object submittedValues[] = getSubmittedSelectedValues(
              component);
        boolean isSelected;

        Class type = String.class;
        Object valuesArray;
        Object itemValue;
        if (submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = valueString;
        } else {
            valuesArray = getCurrentSelectedValues(component);
            itemValue = curItem.getValue();
        }
        if (valuesArray != null) {
            type = valuesArray.getClass().getComponentType();
        }

        RequestStateManager.set(context,
                                RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME,
                                component);
        Object newValue;
        try {
            newValue = context.getApplication().getExpressionFactory().
                coerceToType(itemValue, type);
        } catch (ELException ele) {
            newValue = itemValue;
        } catch (IllegalArgumentException iae) {
            // If coerceToType fails, per the docs it should throw
            // an ELException, however, GF 9.0 and 9.0u1 will throw
            // an IllegalArgumentException instead (see GF issue 1527).
            newValue = itemValue;
        }

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
        RenderKitUtils.renderPassThruAttributes(writer,
                                                component,
                                                ATTRIBUTES);
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
            if (!curItem.isEscape()) {
                // It seems the ResponseWriter API should
                // have a writeText() with a boolean property
                // to determine if it content written should
                // be escaped or not.
                writer.write(itemLabel);
            } else {
                writer.writeText(itemLabel, component, "label");
            }
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
