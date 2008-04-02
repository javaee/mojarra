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

/*
 * $Id: MenuRenderer.java,v 1.69 2006/03/13 21:21:47 edburns Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// MenuRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

import java.util.logging.Level;

/**
 * <B>MenuRenderer</B> is a class that renders the current value of
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of
 * menu options.
 */

public class MenuRenderer extends HtmlBasicInputRenderer {

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

    public MenuRenderer() {
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

    public void decode(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Begin decoding component " + component.getId());
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
        assert (clientId != null);
        // currently we assume the model type to be of type string or 
        // convertible to string and localised by the application.
        if (component instanceof UISelectMany) {
            Map<String,String[]> requestParameterValuesMap = context.getExternalContext().
                getRequestParameterValuesMap();
            if (requestParameterValuesMap.containsKey(clientId)) {
                String newValues[] = requestParameterValuesMap.
                    get(clientId);
                setSubmittedValue(component, newValues);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("submitted values for UISelectMany component " +
                              component.getId() + " after decoding " + newValues);
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
            Map<String,String> requestParameterMap = context.getExternalContext().
                getRequestParameterMap();
            if (requestParameterMap.containsKey(clientId)) {
                String newValue = requestParameterMap.get(clientId);
                setSubmittedValue(component, newValue);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("submitted value for UISelectOne component " +
                              component.getId() + " after decoding " + newValue);
                }

            }
	    else {
		// there is no value, but this is different from a null
		// value.
		setSubmittedValue(component, RIConstants.NO_VALUE);
	    }
        }
        return;
    }


    public Object getConvertedValue(FacesContext context, UIComponent component,
                                    Object submittedValue)
        throws ConverterException {
        if (component instanceof UISelectMany) {
            return convertSelectManyValue(context,
                                          ((UISelectMany) component),
                                          (String[]) submittedValue);
        } else {
            return convertSelectOneValue(context,
                                         ((UISelectOne) component),
                                         (String) submittedValue);
        }
    }


    public Object convertSelectOneValue(FacesContext context,
                                        UISelectOne uiSelectOne,
                                        String newValue)
        throws ConverterException {
        Object convertedValue = null;
	if (newValue == RIConstants.NO_VALUE) {
	    return null;
	}
        if (newValue == null) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("No conversion necessary for SelectOne Component  "
                          + uiSelectOne.getId() + " since the new value is null ");
            }
            return null;
        }

        convertedValue =
            super.getConvertedValue(context, uiSelectOne, newValue);
        if (logger.isLoggable(Level.FINE)) {
                 logger.fine("SelectOne Component  " + uiSelectOne.getId() +
                      " convertedValue " + convertedValue);
        }
        return convertedValue;
    }


    public Object convertSelectManyValue(FacesContext context,
                                         UISelectMany uiSelectMany,
                                         String[] newValues)
        throws ConverterException {
        // if we have no local value, try to get the valueExpression.
        ValueExpression valueExpression = uiSelectMany.getValueExpression("value");

        Object result = newValues; // default case, set local value
        Class modelType = null;
	boolean throwException = false;

        // If we have a ValueExpression
        if (null != valueExpression) {
            modelType = valueExpression.getType(context.getELContext());
            // Does the valueExpression resolve properly to something with
            // a type?
            if (null != modelType) {
                if (modelType.isArray()) {
                    result = handleArrayCase(context, uiSelectMany,
                                             modelType, newValues);
                } else if (List.class.isAssignableFrom(modelType)) {
                    result = handleListCase(context, newValues);
                } else {
		    throwException = true;
                }
            } else {
		throwException = true;
            }
        } else {
            // No ValueExpression, just use Object array.
            Object[] convertedValues = new Object[1];
            result = handleArrayCase(context, uiSelectMany,
                                     convertedValues.getClass(),
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
	    Object [] params = {
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


    protected Object handleArrayCase(FacesContext context,
                                     UISelectMany uiSelectMany,
                                     Class arrayClass,
                                     String[] newValues)
        throws ConverterException {
        Object result = null;
        Class elementType = null;
        Converter converter = null;
        int len = (null != newValues ? newValues.length : 0);

        elementType = arrayClass.getComponentType();

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
		Object [] params = {
		    valueStr.toString(),
		    "null Converter"
		};

		throw new ConverterException(MessageUtils.getExceptionMessage(
                  MessageUtils.CONVERSION_ERROR_MESSAGE_ID, params));
            }
        }

        assert (null != result);
        if (elementType.isPrimitive()) {
            for (int i = 0; i < len; i++) {
                if (elementType.equals(Boolean.TYPE)) {
                    Array.setBoolean(result, i,
                                     ((Boolean) converter.getAsObject(context,
                                                                      uiSelectMany,
                                                                      newValues[i])).booleanValue());
                } else if (elementType.equals(Byte.TYPE)) {
                    Array.setByte(result, i,
                                  ((Byte) converter.getAsObject(context,
                                                                uiSelectMany,
                                                                newValues[i])).byteValue());
                } else if (elementType.equals(Double.TYPE)) {
                    Array.setDouble(result, i,
                                    ((Double) converter.getAsObject(context,
                                                                    uiSelectMany,
                                                                    newValues[i])).doubleValue());
                } else if (elementType.equals(Float.TYPE)) {
                    Array.setFloat(result, i,
                                   ((Float) converter.getAsObject(context,
                                                                  uiSelectMany,
                                                                  newValues[i])).floatValue());
                } else if (elementType.equals(Integer.TYPE)) {
                    Array.setInt(result, i,
                                 ((Integer) converter.getAsObject(context,
                                                                  uiSelectMany,
                                                                  newValues[i])).intValue());
                } else if (elementType.equals(Character.TYPE)) {
                    Array.setChar(result, i,
                                  ((Character) converter.getAsObject(context,
                                                                     uiSelectMany,
                                                                     newValues[i])).charValue());
                } else if (elementType.equals(Short.TYPE)) {
                    Array.setShort(result, i,
                                   ((Short) converter.getAsObject(context,
                                                                  uiSelectMany,
                                                                  newValues[i])).shortValue());
                } else if (elementType.equals(Long.TYPE)) {
                    Array.setLong(result, i,
                                  ((Long) converter.getAsObject(context,
                                                                uiSelectMany,
                                                                newValues[i])).longValue());
                }
            }
        } else {
            for (int i = 0; i < len; i++) {
                if (logger.isLoggable(Level.FINE)) {
                    Object converted = converter.getAsObject(context,
                                                             uiSelectMany,
                                                             newValues[i]);
                    logger.fine("String value: " + newValues[i] +
                              " converts to : " + converted.toString());
                }
                Array.set(result, i, converter.getAsObject(context,
                                                           uiSelectMany,
                                                           newValues[i]));
            }
        }
        return result;
    }


    protected ArrayList<String> handleListCase(FacesContext context,
                                    String[] newValues) {
        int
            i = 0,
            len = newValues.length;
        ArrayList<String> result = new ArrayList<String>(len);
        for (i = 0; i < len; i++) {
            result.add(newValues[i]);
        }

        return result;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Begin encoding component " + component.getId());
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
            logger.log(Level.FINER,"End encoding component " + component.getId());
        }
    }


    // Render the "select" portion..
    //
    void renderSelect(FacesContext context,
                      UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Rendering 'select'");
        }
        writer.startElement("select", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        // render styleClass attribute if present.
        String styleClass = null;
        if (null !=
            (styleClass = (String) component.getAttributes().get("styleClass"))) {
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
        Object size = component.getAttributes().get("size");
        if ((null == size) ||
            ((size instanceof Integer) &&
            ((Integer) size).intValue() == Integer.MIN_VALUE)) {
            writeDefaultSize(writer, itemCount);
        }

        RenderKitUtils.renderPassThruAttributes(context, 
                                                writer, 
                                                component);                
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer,
                                                         component);
        // Now, render the "options" portion...
        renderOptions(context, component);

        writer.endElement("select");
    }


    int getOptionNumber(FacesContext context, UIComponent component) {
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


    void renderOptions(FacesContext context, UIComponent component)
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        Iterator items = RenderKitUtils.getSelectItems(context, component);
        SelectItem curItem = null;
        while (items.hasNext()) {
            curItem = (SelectItem) items.next();
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


    protected void renderOption(FacesContext context, UIComponent component,
                                SelectItem curItem) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        writer.writeText("\t", null);
        writer.startElement("option", component);

        String valueString = getFormattedValue(context, component,
                                               curItem.getValue());
        writer.writeAttribute("value", valueString, "value");

        Object submittedValues[] = getSubmittedSelectedValues(context,
                                                              component);
        Class type = String.class;
        Object valuesArray = null;
        Object itemValue = null;
        
        boolean isSelected;
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
        
        Object newValue = context.getApplication().getExpressionFactory().
            coerceToType(itemValue, type);

        isSelected = isSelected(newValue, valuesArray);

        if (isSelected) {
            writer.writeAttribute("selected", true, "selected");
        }

	String labelClass = null;
	Boolean disabledAttr = (Boolean)component.getAttributes().get("disabled") ;
	boolean componentDisabled = false ;
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
            writer.writeText(curItem.getLabel(), "label");
        }
        else {
            writer.write(curItem.getLabel());
        }
        writer.endElement("option");
        writer.writeText("\n", null);

    }


    boolean isSelected(Object itemValue, Object valueArray) {
        if (null != valueArray) {
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


    boolean isSelected(String itemValue, Object[] values) {
        if (null != values) {
            int len = values.length;
            for (int i = 0; i < len; i++) {
                if (values[i].equals(itemValue)) {
                    return true;
                }
            }
        }
        return false;
    }


    protected void writeDefaultSize(ResponseWriter writer, int itemCount)
        throws IOException {
        // if size is not specified default to 1.
        writer.writeAttribute("size", "1", "size");
    }

    // To derive a selectOne type component from this, override
    // these methods.
    String getMultipleText(UIComponent component) {
        if (component instanceof UISelectMany) {
            return " multiple ";
        }
        return "";
    }


    Object[] getSubmittedSelectedValues(FacesContext context,
                                        UIComponent component) {
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


    Object getCurrentSelectedValues(FacesContext context,
                                    UIComponent component) {
        if (component instanceof UISelectMany) {
            UISelectMany select = (UISelectMany) component;
            Object value = select.getValue();
            if (value instanceof List)
                return ((List) value).toArray();

            return value;
        }

        UISelectOne select = (UISelectOne) component;
        Object returnObject;
        if (null != (returnObject = select.getValue())) {
            return new Object[]{returnObject};
        }
        return null;
    }

} // end of class MenuRenderer
