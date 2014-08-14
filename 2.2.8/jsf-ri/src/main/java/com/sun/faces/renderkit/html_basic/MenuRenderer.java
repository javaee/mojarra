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

/*
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.
 */

// MenuRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.logging.Level;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.FacesException;

import com.sun.faces.RIConstants;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.SelectItemsIterator;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.ReflectionUtils;

/**
 * <B>MenuRenderer</B> is a class that renders the current value of
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of
 * menu options.
 */

public class MenuRenderer extends HtmlBasicInputRenderer {


    private static final Attribute[] ATTRIBUTES =
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
            if (modelType != null) {
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

    @Override
    public void decode(FacesContext context, UIComponent component) {

        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (clientId == null) {
            clientId = component.getClientId(context);
        }
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


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        renderSelect(context, component);

    }


    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component,
                                    Object submittedValue)
          throws ConverterException {

        if (component instanceof UISelectMany) {
            // need to set the 'TARGET_COMPONENT_ATTRIBUTE_NAME' request attr so the
            // coerce-value call in the jsf-api UISelectMany.matchValue will work
            // (need a better way to determine the currently processing UIComponent ...)
            RequestStateManager.set(context,
                                    RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME,
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

        if (modelType.isArray()) {
            return convertSelectManyValues(context,
                                           uiSelectMany,
                                           modelType,
                                           newValues);
        } else if (Collection.class.isAssignableFrom(modelType)) {
            Object[] values = (Object[]) convertSelectManyValues(context,
                                                                 uiSelectMany,
                                                                 Object[].class,
                                                                 newValues);

            Collection targetCollection = null;

            // see if the collectionType hint is available, if so, use that
            Object collectionTypeHint = uiSelectMany.getAttributes().get("collectionType");
            if (collectionTypeHint != null) {
                targetCollection = createCollectionFromHint(collectionTypeHint);
            } else {
                // try to get a new Collection to store the values based
                // by trying to create a clone
                Collection currentValue = (Collection) uiSelectMany.getValue();
                if (currentValue != null) {
                    targetCollection = cloneValue(currentValue);
                }

                // No cloned instance so if the modelType happens to represent a
                // concrete type (probably not the norm) try to reflect a
                // no-argument constructor and invoke if available.
                if (targetCollection == null) {
                    //noinspection unchecked
                    targetCollection =
                          createCollection(currentValue, modelType);
                }

                // No suitable instance to work with, make our best guess
                // based on the type.
                if (targetCollection == null) {
                    //noinspection unchecked
                    targetCollection = bestGuess(modelType, values.length);
                }
            }

            //noinspection ManualArrayToCollectionCopy
            for (Object v : values) {
                //noinspection unchecked
                targetCollection.add(v);
            }

            return targetCollection;
        } else if (Object.class.equals(modelType)) {
            return convertSelectManyValues(context,
                                           uiSelectMany,
                                           Object[].class,
                                           newValues);
        } else {
            throw new FacesException("Target model Type is no a Collection or Array");
        }
        
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


    protected boolean renderOption(FacesContext context,
                                   UIComponent component,
                                   UIComponent selectComponent,
                                   Converter converter,
                                   SelectItem curItem,
                                   Object currentSelections,
                                   Object[] submittedValues,
                                   OptionComponentInfo optionInfo) throws IOException {

        Object valuesArray;
        Object itemValue;
        String valueString = getFormattedValue(context, component,
                                               curItem.getValue(), converter);
        boolean containsValue;
        if (submittedValues != null) {
            containsValue = containsaValue(submittedValues);
            if (containsValue) {
                valuesArray = submittedValues;
                itemValue = valueString;
            } else {
                valuesArray = currentSelections;
                itemValue = curItem.getValue();
            }
        } else {
            valuesArray = currentSelections;
            itemValue = curItem.getValue();
        }

        boolean isSelected = isSelected(context, component, itemValue, valuesArray, converter);
        if (optionInfo.isHideNoSelection()
                && curItem.isNoSelectionOption()
                && currentSelections != null
                && !isSelected) {
            return false;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        writer.writeText("\t", component, null);
        writer.startElement("option", (null != selectComponent) ? selectComponent : component);
        writer.writeAttribute("value", valueString, "value");

        if (isSelected) {
            writer.writeAttribute("selected", true, "selected");
        }

        // if the component is disabled, "disabled" attribute would be rendered
        // on "select" tag, so don't render "disabled" on every option.
        if ((!optionInfo.isDisabled()) && curItem.isDisabled()) {
            writer.writeAttribute("disabled", true, "disabled");
        }

        String labelClass;
        if (optionInfo.isDisabled() || curItem.isDisabled()) {
            labelClass = optionInfo.getDisabledClass();
        } else {
            labelClass = optionInfo.getEnabledClass();
        }
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }

        if (curItem.isEscape()) {
            String label = curItem.getLabel();
            if (label == null) {
                label = valueString;
            }
            writer.writeText(label, component, "label");
        } else {
            writer.write(curItem.getLabel());
        }
        writer.endElement("option");
        writer.writeText("\n", component, null);
        return true;
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
            if (value == null) {
                return null;
            } else if (value instanceof Collection) {
                return ((Collection) value).toArray();
            } else if (value.getClass().isArray()) {
                if (Array.getLength(value) == 0) {
                    return null;
                }
            } else if (!value.getClass().isArray()) {
                logger.warning(
                    "The UISelectMany value should be an array or a collection type, the actual type is " +
                    value.getClass().getName());
            }

            return value;
        }

        UISelectOne select = (UISelectOne) component;
        Object val = select.getValue();
        if (val != null) {
            return new Object[] { val };
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

    protected Object[] getSubmittedSelectedValues(UIComponent component) {

        if (component instanceof UISelectMany) {
            UISelectMany select = (UISelectMany) component;
            return (Object[]) select.getSubmittedValue();
        }

        UISelectOne select = (UISelectOne) component;
        Object val = select.getSubmittedValue();
        if (val != null) {
            return new Object[] { val };
        }
        return null;

    }


    protected boolean isSelected(FacesContext context,
                                 UIComponent component,
                                 Object itemValue,
                                 Object valueArray,
                                 Converter converter) {

        if (itemValue == null && valueArray == null) {
            return true;
        }
        if (null != valueArray) {
            if (!valueArray.getClass().isArray()) {
                logger.warning("valueArray is not an array, the actual type is " +
                    valueArray.getClass());
                return valueArray.equals(itemValue);
            }
            int len = Array.getLength(valueArray);
            for (int i = 0; i < len; i++) {
                Object value = Array.get(valueArray, i);
                if (value == null && itemValue == null) {
                    return true;
                } else {
                    if ((value == null) ^ (itemValue == null)) {
                        continue;
                    }
                    Object compareValue;
                    if (converter == null) {
                        compareValue = coerceToModelType(context,
                                                        itemValue,
                                                        value.getClass());
                    } else {
                        compareValue = itemValue;
                        if (compareValue instanceof String && !(value instanceof String)) {
                            // type mismatch between the time and the value we're
                            // comparing.  Invoke the Converter.
                            compareValue = converter.getAsObject(context,
                                                                component,
                                                                (String) compareValue);
                        }
                    }

                    if (value.equals(compareValue)) {
                        return (true);
                    }
                }
            }
        }
        return false;

    }


    protected int renderOptions(FacesContext context,
                                UIComponent component,
                                SelectItemsIterator<SelectItem> items)
    throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        Converter converter = null;
        if(component instanceof ValueHolder) {
            converter = ((ValueHolder)component).getConverter();
        }
        int count = 0;
        Object currentSelections = getCurrentSelectedValues(component);
        Object[] submittedValues = getSubmittedSelectedValues(component);
        Map<String,Object> attributes = component.getAttributes();
        boolean componentDisabled = Util.componentIsDisabled(component);

        OptionComponentInfo optionInfo =
              new OptionComponentInfo((String) attributes.get("disabledClass"),
                                      (String) attributes.get("enabledClass"),
                                      componentDisabled,
                                      isHideNoSelection(component));
        RequestStateManager.set(context,
                                RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME,
                                component);
        while (items.hasNext()) {
            SelectItem item = items.next();
            UIComponent selectComponent = items.currentSelectComponent();

            if (item instanceof SelectItemGroup) {
                // render OPTGROUP
                writer.startElement("optgroup", (null != selectComponent) ? selectComponent : component);
                writer.writeAttribute("label", item.getLabel(), "label");

                // if the component is disabled, "disabled" attribute would be rendered
                // on "select" tag, so don't render "disabled" on every option.
                if ((!componentDisabled) && item.isDisabled()) {
                    writer.writeAttribute("disabled", true, "disabled");
                }
                count++;
                // render options of this group.
                SelectItem[] itemsArray =
                      ((SelectItemGroup) item).getSelectItems();
                for (int i = 0; i < itemsArray.length; ++i) {
                    if (renderOption(context,
                                     component,
                                     selectComponent,
                                     converter,
                                     itemsArray[i],
                                     currentSelections,
                                     submittedValues,
                                     optionInfo)) {
                        count++;
                    }
                }
                writer.endElement("optgroup");
            } else {
                if (renderOption(context,
                                 component,
                                 selectComponent,
                                 converter,
                                 item,
                                 currentSelections,
                                 submittedValues,
                                 optionInfo)) {
                    count ++;
                }
            }
        }

        return count;

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
        SelectItemsIterator<SelectItem> items = RenderKitUtils.getSelectItems(context, component);

        // render the options to a buffer now so that we can determine
        // the size
        FastStringWriter bufferedWriter = new FastStringWriter(128);
        context.setResponseWriter(writer.cloneWithWriter(bufferedWriter));
        int count = renderOptions(context, component, items);
        context.setResponseWriter(writer);
        // If "size" is *not* set explicitly, we have to default it correctly
        Integer size = (Integer) component.getAttributes().get("size");
        if (size == null || size == Integer.MIN_VALUE) {
            size = count;
        }
        writeDefaultSize(writer, size);

        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                component,
                                                ATTRIBUTES,
                                                getNonOnChangeBehaviors(component));
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer,
                                                         component);

        RenderKitUtils.renderOnchange(context, component, false);

        // Now, write the buffered option content
        writer.write(bufferedWriter.toString());
        
        writer.endElement("select");

    }

    protected Object coerceToModelType(FacesContext ctx,
                                       Object value,
                                       Class itemValueType) {

        Object newValue;
        try {
            ExpressionFactory ef = ctx.getApplication().getExpressionFactory();
            newValue = ef.coerceToType(value, itemValueType);
        } catch (ELException ele) {
            newValue = value;
        } catch (IllegalArgumentException iae) {
            // If coerceToType fails, per the docs it should throw
            // an ELException, however, GF 9.0 and 9.0u1 will throw
            // an IllegalArgumentException instead (see GF issue 1527).
            newValue = value;
        }

        return newValue;

    }


    /**
     * @param collection a Collection instance
     *
     * @return a new <code>Collection</code> instance or null if the instance
     *         cannot be created
     */
    protected Collection createCollection(Collection collection,
                                          Class<? extends Collection> fallBackType) {

        Class<? extends Collection> lookupClass =
              ((collection != null) ? collection.getClass() : fallBackType);

        if (!lookupClass.isInterface()
             && !Modifier.isAbstract(lookupClass.getModifiers())) {
            try {
                return lookupClass.newInstance();
            } catch (Exception e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE,
                               "Unable to create new Collection instance for type "
                               + lookupClass.getName(),
                               e);
                }
            }
        }

        return null;

    }


    /**
     * <p>
     * Utility method to invoke the the <code>clone</code> method on the provided
     * value.
     * </p>
     *
     * @param value the value to clone
     * @return the result of invoking <code>clone()</code> or <code>null</code>
     *  if the value could not be cloned or does not implement the
     *  {@link Cloneable} interface
     */
    protected Collection cloneValue(Object value) {

        if (value instanceof Cloneable) {
            // even though Clonable marks an instance of a Class as being
            // safe to call .clone(), .clone() by default is protected.
            // The Collection classes that do implement Clonable do so at variable
            // locations within the class hierarchy, so we're stuck having to
            // use reflection.
            Method clone =
                  ReflectionUtils.lookupMethod(value.getClass(), "clone");
            if (clone != null) {
                try {
                    Collection c = (Collection) clone.invoke(value);
                    c.clear();
                    return c;
                } catch (Exception e) {
                    if (logger.isLoggable(Level.SEVERE)) {
                        logger.log(Level.SEVERE,
                                   "Unable to clone collection type: {0}",
                                   value.getClass().getName());
                        logger.log(Level.SEVERE, e.toString(), e);
                    }
                }
            } else {
                // no public clone method
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE,
                               "Type {0} implements Cloneable, but has no public clone method.",
                               value.getClass().getName());
                }
            }
        }

        return null;

    }


    /**
     * @param type the target model type
     * @param initialSize the initial size of the <code>Collection</code>
     * @return a <code>Collection</code> instance that best matches
     *  <code>type</code>
     */
    protected Collection bestGuess(Class<? extends Collection> type,
                                   int initialSize) {

        if (SortedSet.class.isAssignableFrom(type)) {
            return new TreeSet();
        } else if (Queue.class.isAssignableFrom(type)) {
           return new LinkedList(); 
        } else if (Set.class.isAssignableFrom(type)) {
            return new HashSet(initialSize);
        } else {
            // this covers the where type is List or Collection
            return new ArrayList(initialSize);
        }

    }


    /**
     * <p>
     * Create a collection from the provided hint.
     * @param collectionTypeHint the Collection type as either a String or Class
     * @return a new Collection instance
     */
    protected Collection createCollectionFromHint(Object collectionTypeHint) {

        Class<? extends Collection> collectionType;
        if (collectionTypeHint instanceof Class) {
            //noinspection unchecked
            collectionType = (Class<? extends Collection>) collectionTypeHint;
        } else if (collectionTypeHint instanceof String) {
            try {
                //noinspection unchecked
                collectionType = Util.loadClass((String) collectionTypeHint,
                                                this);
            } catch (ClassNotFoundException cnfe) {
                throw new FacesException(cnfe);
            }
        } else {
            // RELEASE_PENDING (i18n)
            throw new FacesException(
                  "'collectionType' should resolve to type String or Class.  Found: "
                  + collectionTypeHint.getClass().getName());
        }

        Collection c = createCollection(null, collectionType);
        if (c == null) {
            // RELEASE_PENDING (i18n)
            throw new FacesException("Unable to create collection type " + collectionType);
        }
        return c;

    }


    protected boolean isHideNoSelection(UIComponent component) {

        Object result = component.getAttributes().get("hideNoSelectionOption");
        return ((result != null) ? (Boolean) result : false);

    }

} // end of class MenuRenderer
