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

/*
 * $Id: MenuRenderer.java,v 1.93 2007/07/27 19:59:08 rlubke Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.
 */

// MenuRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ConverterPropertyEditorBase;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * <B>MenuRenderer</B> is a class that renders the current value of
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of
 * menu options.
 */

public class MenuRenderer extends HtmlBasicInputRenderer {


    private static final String[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.SELECTMANYMENU);


    // ---------------------------------------------------------- Public Methods


    public Object convertSelectManyValue(FacesContext context,
                                         UISelectMany uiSelectMany,
                                         String[] newValues)
          throws ConverterException {

        // if we have no local value, try to get the valueExpression.
        ValueExpression valueExpression =
              uiSelectMany.getValueExpression("value");

        Object result = newValues; // default case, set local value
        boolean throwException = false;

        // If we have a ValueExpression
        if (null != valueExpression) {
            Class modelType = valueExpression.getType(context.getELContext());
            // Does the valueExpression resolve properly to something with
            // a type?
            if(modelType != null) {
                result = convertSelectManyValuesForModel(context,
                                                         uiSelectMany,
                                                         modelType,
                                                         newValues);
            }
            // If it could not be converted, as a fall back try the type of
            // the valueExpression's current value covering some edge cases such
            // as where the current value came from a Map.
            if(result == null) {
                Object value = valueExpression.getValue(context.getELContext());
                if(value != null) {
                    result = convertSelectManyValuesForModel(context,
                                                             uiSelectMany,
                                                             value.getClass(),
                                                             newValues);
                }
            }
            if(result == null) {
                throwException = true;
            }
        } else {
            // No ValueExpression, just use Object array.
            result = convertSelectManyValues(context, uiSelectMany,
                                             Object[].class,
                                             newValues);
        }
        if (throwException) {
            StringBuffer values = new StringBuffer();
            if (null != newValues) {
                for (int i = 0; i < newValues.length; i++) {
                    if (i == 0) {
                        values.append(newValues[i]);
                    } else {
                        values.append(' ').append(newValues[i]);
                    }
                }
            }
            Object[] params = {
                  values.toString(),
                  valueExpression.getExpressionString()
            };
            throw new ConverterException
                  (MessageUtils.getExceptionMessage(MessageUtils.CONVERSION_ERROR_MESSAGE_ID,
                                                    params));
        }

        // At this point, result is ready to be set as the value
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("SelectMany Component  " + uiSelectMany.getId() +
                        " convertedValues " + result);
        }
        return result;

    }


    public Object convertSelectOneValue(FacesContext context,
                                        UISelectOne uiSelectOne,
                                        String newValue)
          throws ConverterException {

        if (RIConstants.NO_VALUE.equals(newValue)) {
            return null;
        }
        if (newValue == null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("No conversion necessary for SelectOne Component  "
                            + uiSelectOne.getId()
                            + " since the new value is null ");
            }
            return null;
        }

        Object convertedValue =
              super.getConvertedValue(context, uiSelectOne, newValue);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("SelectOne Component  " + uiSelectOne.getId() +
                        " convertedValue " + convertedValue);
        }
        return convertedValue;

    }

    public void decode(FacesContext context, UIComponent component) {

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
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin decoding component " + component.getId());
        }

        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOrReadonly(component)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("No decoding necessary since the component " +
                            component.getId() + " is disabled");
            }
            return;
        }

        String clientId = component.getClientId(context);
        assert(clientId != null);
        // currently we assume the model type to be of type string or
        // convertible to string and localized by the application.
        if (component instanceof UISelectMany) {
            Map<String, String[]> requestParameterValuesMap =
                  context.getExternalContext().
                        getRequestParameterValuesMap();
            if (requestParameterValuesMap.containsKey(clientId)) {
                String newValues[] = requestParameterValuesMap.
                      get(clientId);
                setSubmittedValue(component, newValues);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("submitted values for UISelectMany component "
                                +
                                component.getId()
                                + " after decoding "
                                + Arrays.toString(newValues));
                }
            } else {
                // Use the empty array, not null, to distinguish
                // between an deselected UISelectMany and a disabled one
                setSubmittedValue(component, new String[0]);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Set empty array for UISelectMany component " +
                                component.getId() + " after decoding ");
                }
            }
        } else {
            // this is a UISelectOne
            Map<String, String> requestParameterMap =
                  context.getExternalContext().
                        getRequestParameterMap();
            if (requestParameterMap.containsKey(clientId)) {
                String newValue = requestParameterMap.get(clientId);
                setSubmittedValue(component, newValue);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("submitted value for UISelectOne component "
                                +
                                component.getId()
                                + " after decoding "
                                + newValue);
                }

            } else {
                // there is no value, but this is different from a null
                // value.
                setSubmittedValue(component, RIConstants.NO_VALUE);
            }
        }

    }


    public void encodeBegin(FacesContext context, UIComponent component)
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

    }


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

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding component " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " +
                            component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }

        renderSelect(context, component);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding component " + component.getId());
        }

    }


    public Object getConvertedValue(FacesContext context, UIComponent component,
                                    Object submittedValue)
          throws ConverterException {

        if (component instanceof UISelectMany) {
            // need to set the 'TARGET_COMPONENT_ATTRIBUTE_NAME' request attr so the
            // coerce-value call in the jsf-api UISelectMany.matchValue will work
            // (need a better way to determine the currently processing UIComponent ...)
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.put(ConverterPropertyEditorBase.TARGET_COMPONENT_ATTRIBUTE_NAME,
                    component);
            return convertSelectManyValue(context,
                                          ((UISelectMany) component),
                                          (String[]) submittedValue);
        } else {
            return convertSelectOneValue(context,
                                         ((UISelectOne) component),
                                         (String) submittedValue);
        }

    }

    // ------------------------------------------------------- Protected Methods


    /*
     * Converts the provided string array and places them into the correct provided model type.
     */
    protected Object convertSelectManyValuesForModel(FacesContext context,
                                                     UISelectMany uiSelectMany,
                                                     Class modelType,
                                                     String[] newValues) {
        Object result = null;
        if (modelType.isArray()) {
            result = convertSelectManyValues(context,
                                             uiSelectMany,
                                             modelType,
                                             newValues);
        } else if (List.class.isAssignableFrom(modelType)) {
            Object[] values = (Object[]) convertSelectManyValues(context,
                                                                 uiSelectMany,
                                                                 Object[].class,
                                                                 newValues);
            // perform a manual copy as the Array returned from
            // Arrays.asList() isn't mutable.  It seems a waste
            // to also call Collections.addAll(Arrays.asList())
            List<Object> l = new ArrayList<Object>(values.length);
            //noinspection ManualArrayToCollectionCopy
            for (Object v : values) {
                l.add(v);
            }
            result = l;
        }
        return result;
    }
    

    protected Object convertSelectManyValues(FacesContext context,
                                             UISelectMany uiSelectMany,
                                             Class arrayClass,
                                             String[] newValues)
          throws ConverterException {

        Object result;
        Converter converter;
        int len = (null != newValues ? newValues.length : 0);

        Class elementType = arrayClass.getComponentType();

        // Optimization: If the elementType is String, we don't need
        // conversion.  Just return newValues.
        if (elementType.equals(String.class)) {
            return newValues;
        }

        try {
            result = Array.newInstance(elementType, len);
        } catch (Exception e) {
            throw new ConverterException(e);
        }

        // bail out now if we have no new values, returning our
        // oh-so-useful zero-length array.
        if (null == newValues) {
            return result;
        }

        // obtain a converter.

        // attached converter takes priority
        if (null == (converter = uiSelectMany.getConverter())) {
            // Otherwise, look for a by-type converter
            if (null == (converter = Util.getConverterForClass(elementType,
                                                               context))) {
                // if that fails, and the attached values are of Object type,
                // we don't need conversion.
                if (elementType.equals(Object.class)) {
                    return newValues;
                }
                StringBuffer valueStr = new StringBuffer();
                for (int i = 0; i < len; i++) {
                    if (i == 0) {
                        valueStr.append(newValues[i]);
                    } else {
                        valueStr.append(' ').append(newValues[i]);
                    }
                }
                Object[] params = {
                      valueStr.toString(),
                      "null Converter"
                };

                throw new ConverterException(MessageUtils.getExceptionMessage(
                      MessageUtils.CONVERSION_ERROR_MESSAGE_ID, params));
            }
        }

        assert(null != result);
        if (elementType.isPrimitive()) {
            for (int i = 0; i < len; i++) {
                if (elementType.equals(Boolean.TYPE)) {
                    Array.setBoolean(result, i,
                                     ((Boolean) converter.getAsObject(context,
                                                                      uiSelectMany,
                                                                      newValues[i])));
                } else if (elementType.equals(Byte.TYPE)) {
                    Array.setByte(result, i,
                                  ((Byte) converter.getAsObject(context,
                                                                uiSelectMany,
                                                                newValues[i])));
                } else if (elementType.equals(Double.TYPE)) {
                    Array.setDouble(result, i,
                                    ((Double) converter.getAsObject(context,
                                                                    uiSelectMany,
                                                                    newValues[i])));
                } else if (elementType.equals(Float.TYPE)) {
                    Array.setFloat(result, i,
                                   ((Float) converter.getAsObject(context,
                                                                  uiSelectMany,
                                                                  newValues[i])));
                } else if (elementType.equals(Integer.TYPE)) {
                    Array.setInt(result, i,
                                 ((Integer) converter.getAsObject(context,
                                                                  uiSelectMany,
                                                                  newValues[i])));
                } else if (elementType.equals(Character.TYPE)) {
                    Array.setChar(result, i,
                                  ((Character) converter.getAsObject(context,
                                                                     uiSelectMany,
                                                                     newValues[i])));
                } else if (elementType.equals(Short.TYPE)) {
                    Array.setShort(result, i,
                                   ((Short) converter.getAsObject(context,
                                                                  uiSelectMany,
                                                                  newValues[i])));
                } else if (elementType.equals(Long.TYPE)) {
                    Array.setLong(result, i,
                                  ((Long) converter.getAsObject(context,
                                                                uiSelectMany,
                                                                newValues[i])));
                }
            }
        } else {
            for (int i = 0; i < len; i++) {
                if (logger.isLoggable(Level.FINE)) {
                    Object converted = converter.getAsObject(context,
                                                             uiSelectMany,
                                                             newValues[i]);
                    logger.fine("String value: " + newValues[i] +
                                " converts to : " + converted);
                }
                Array.set(result, i, converter.getAsObject(context,
                                                           uiSelectMany,
                                                           newValues[i]));
            }
        }
        return result;

    }


    protected void renderOption(FacesContext context, UIComponent component,
                                SelectItem curItem) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        writer.writeText("\t", component, null);
        writer.startElement("option", component);

        String valueString = getFormattedValue(context, component,
                                               curItem.getValue());
        writer.writeAttribute("value", valueString, "value");

        Object submittedValues[] = getSubmittedSelectedValues(
              component);
        Class type = String.class;
        Object valuesArray;
        Object itemValue;

        boolean isSelected;
        boolean containsValue;
        if (submittedValues != null) {
            containsValue = containsaValue(submittedValues);
            if (containsValue) {
                valuesArray = submittedValues;
                itemValue = valueString;
            } else {
                valuesArray = getCurrentSelectedValues(component);
                itemValue = curItem.getValue();
            }
        } else {
            valuesArray = getCurrentSelectedValues(component);
            itemValue = curItem.getValue();
        }
        if (valuesArray != null) {
            type = valuesArray.getClass().getComponentType();
        }

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(ConverterPropertyEditorBase.TARGET_COMPONENT_ATTRIBUTE_NAME,
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
            writer.writeAttribute("selected", true, "selected");
        }

        Boolean disabledAttr =
              (Boolean) component.getAttributes().get("disabled");
        boolean componentDisabled = false;
        if (disabledAttr != null) {
            if (disabledAttr.equals(Boolean.TRUE)) {
                componentDisabled = true;
            }
        }

        // if the component is disabled, "disabled" attribute would be rendered
        // on "select" tag, so don't render "disabled" on every option.
        if ((!componentDisabled) && curItem.isDisabled()) {
            writer.writeAttribute("disabled", true, "disabled");
        }

        String labelClass;
        if (componentDisabled || curItem.isDisabled()) {
            labelClass = (String) component.
                  getAttributes().get("disabledClass");
        } else {
            labelClass = (String) component.
                  getAttributes().get("enabledClass");
        }
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }

        if (curItem.isEscape()) {
            String label = curItem.getLabel();
            if (label == null) {
                label = curItem.getValue().toString();
            }
            writer.writeText(label, component, "label");
        } else {
            writer.write(curItem.getLabel());
        }
        writer.endElement("option");
        writer.writeText("\n", component, null);

    }


    protected void writeDefaultSize(ResponseWriter writer, int itemCount)
          throws IOException {

        // if size is not specified default to 1.
        writer.writeAttribute("size", "1", "size");

    }


    protected boolean containsaValue(Object valueArray) {

        if (null != valueArray) {
            int len = Array.getLength(valueArray);
            for (int i = 0; i < len; i++) {
                Object value = Array.get(valueArray, i);
                if (value != null && !(value.equals(RIConstants.NO_VALUE))) {
                    return true;
                }
            }
        }
        return false;

    }


    protected Object getCurrentSelectedValues(UIComponent component) {

        if (component instanceof UISelectMany) {
            UISelectMany select = (UISelectMany) component;
            Object value = select.getValue();
            if (value instanceof Collection) {

                Collection<?> list = (Collection) value;
                int size = list.size();
                if (size > 0) {
                    // get the type of the first element - Should
                    // we assume that all elements of the List are
                    // the same type?
                    return list.toArray((Object[]) Array
                          .newInstance(list.iterator().next().getClass(),
                                       size));
                } else {
                    return ((Collection) value).toArray();
                }
            }
            else if (value != null && !value.getClass().isArray()) {
                logger.warning(
                    "The UISelectMany value should be an array or a collection type, the actual type is " +
                    value.getClass().getName());
            }

            return value;
        }

        UISelectOne select = (UISelectOne) component;
        Object returnObject;
        if (null != (returnObject = select.getValue())) {
            Object ret = Array.newInstance(returnObject.getClass(), 1);
            Array.set(ret, 0, returnObject);
            return ret;
        }
        return null;

    }


    // To derive a selectOne type component from this, override
    // these methods.
    protected String getMultipleText(UIComponent component) {

        if (component instanceof UISelectMany) {
            return " multiple ";
        }
        return "";

    }


    protected int getOptionNumber(FacesContext context, UIComponent component) {

        Iterator items = RenderKitUtils.getSelectItems(context, component);
        int itemCount = 0;
        while (items.hasNext()) {
            itemCount++;
            SelectItem item = (SelectItem) items.next();
            if (item instanceof SelectItemGroup) {
                int optionsLength =
                      ((SelectItemGroup) item).getSelectItems().length;
                itemCount = itemCount + optionsLength;
            }
        }
        return itemCount;

    }


    protected Object[] getSubmittedSelectedValues(UIComponent component) {

        if (component instanceof UISelectMany) {
            UISelectMany select = (UISelectMany) component;
            return (Object[]) select.getSubmittedValue();
        }

        UISelectOne select = (UISelectOne) component;
        Object returnObject;
        if (null != (returnObject = select.getSubmittedValue())) {
            return new Object[]{returnObject};
        }
        return null;

    }


    protected boolean isSelected(Object itemValue, Object valueArray) {

        if (null != valueArray) {
            if (!valueArray.getClass().isArray()) {
                logger.warning("valueArray is not an array, the actual type is " +
                    valueArray.getClass());
                return valueArray.equals(itemValue);
            }
            int len = Array.getLength(valueArray);
            for (int i = 0; i < len; i++) {
                Object value = Array.get(valueArray, i);
                if (value == null) {
                    if (itemValue == null) {
                        return true;
                    }
                } else if (value.equals(itemValue)) {
                    return true;
                }
            }
        }
        return false;

    }


    protected void renderOptions(FacesContext context, UIComponent component)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        Iterator items = RenderKitUtils.getSelectItems(context, component);
        while (items.hasNext()) {
            SelectItem curItem = (SelectItem) items.next();
            if (curItem instanceof SelectItemGroup) {
                // render OPTGROUP
                writer.startElement("optgroup", component);
                writer.writeAttribute("label", curItem.getLabel(), "label");

                // render options of this group.
                SelectItem[] itemsArray =
                      ((SelectItemGroup) curItem).getSelectItems();
                for (int i = 0; i < itemsArray.length; ++i) {
                    renderOption(context, component, itemsArray[i]);
                }
                writer.endElement("optgroup");
            } else {
                renderOption(context, component, curItem);
            }
        }

    }


    // Render the "select" portion..
    //
    protected void renderSelect(FacesContext context,
                      UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Rendering 'select'");
        }
        writer.startElement("select", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        // render styleClass attribute if present.
        String styleClass;
        if (null !=
            (styleClass =
                  (String) component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        if (!getMultipleText(component).equals("")) {
            writer.writeAttribute("multiple", true, "multiple");
        }

        // Determine how many option(s) we need to render, and update
        // the component's "size" attribute accordingly;  The "size"
        // attribute will be rendered as one of the "pass thru" attributes
        int itemCount = getOptionNumber(context, component);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Rendering " + itemCount + " options");
        }
        // If "size" is *not* set explicitly, we have to default it correctly
        Integer size = (Integer) component.getAttributes().get("size");
        if (size == null || size == Integer.MIN_VALUE) {
            size = itemCount;
        }
        writeDefaultSize(writer, size);

        RenderKitUtils.renderPassThruAttributes(writer,
                                                component,
                                                ATTRIBUTES);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer,
                                                         component);
        // Now, render the "options" portion...
        renderOptions(context, component);

        writer.endElement("select");

    }

} // end of class MenuRenderer
