/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * $Id: MenuRenderer.java,v 1.27 2003/09/23 17:23:11 eburns Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// MenuRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.mozilla.util.Assert;

import com.sun.faces.util.SelectItemWrapper;
import com.sun.faces.util.Util;
import java.lang.reflect.Array;

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

    public void decode(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
            return;
        } 

        setPreviousValue(component, ((UIInput)component).currentValue(context));

        String clientId = component.getClientId(context);
        Assert.assert_it(clientId != null);
        // currently we assume the model type to be of type string or 
        // convertible to string and localised by the application.
        if (component instanceof UISelectMany) {
            Map requestParameterValuesMap = context.getExternalContext().
                getRequestParameterValuesMap();
	    if (requestParameterValuesMap.containsKey(clientId)) {
		String newValues[] = (String[])requestParameterValuesMap.
		    get(clientId);
		setSelectManyValue(context, ((UISelectMany)component), newValues);
	    }
        } else {
            Map requestParameterMap = context.getExternalContext().
                getRequestParameterMap();
	    if (requestParameterMap.containsKey(clientId)) {
		String newValue = (String)requestParameterMap.get(clientId);
		setSelectOneValue(context, ((UISelectOne)component), newValue);
	    }
        }    
        return;
    }

    public void setSelectOneValue(FacesContext context, UISelectOne uiSelectOne,
            String newValue) throws ConverterException {
        Object convertedValue = null;
        if ( newValue == null ) {
            uiSelectOne.setValue(null);
            return;
        }
        try {
            convertedValue = getConvertedValue(context, uiSelectOne, newValue);   
            uiSelectOne.setValue(convertedValue);
        } catch (ConverterException ce) {
            uiSelectOne.setValue(newValue);
            addConversionErrorMessage(context, uiSelectOne, ce.getMessage());
            uiSelectOne.setValid(false);
            return;
        }
    }
    
    public void setSelectManyValue(FacesContext context, UISelectMany uiSelectMany,
            String[] newValues) throws ConverterException {
        String valueRef = uiSelectMany.getValueRef();
	Object result = newValues; // default case, set local value
	Class modelType = null;
	
	// If we have a valueRef
	if (null != valueRef) {
            modelType = (Util.getValueBinding(valueRef)).getType(context);
	    // Does the valueRef resolve properly to something with a type?
	    if (null != modelType) {
		if (modelType.isArray()) {
		    try {
			result = handleArrayCase(context, uiSelectMany, 
						 modelType, newValues);
			uiSelectMany.setValid(true);
		    }
		    catch (ConverterException e) {
			addConversionErrorMessage(context, uiSelectMany,
						  e.getMessage());
			uiSelectMany.setValid(false);
		    }
		}
		else if (List.class.isAssignableFrom(modelType)) {
		    result = handleListCase(context, newValues);
		}
		else {
		    // the model type must be an array.
		    addConversionErrorMessage(context, uiSelectMany,
					      Util.getExceptionMessage(Util.CONVERSION_ERROR_MESSAGE_ID));
		    uiSelectMany.setValid(false);
		}
	    } else {
		// We have a valueRef, but no modelType.  Error.
		addConversionErrorMessage(context, uiSelectMany,
					  Util.getExceptionMessage(Util.CONVERSION_ERROR_MESSAGE_ID));
		uiSelectMany.setValid(false);
	    }
	}
	else {
	    // No valueRef, just use Object array.
	    Object [] convertedValues = new Object[1];
	    try {
		result = handleArrayCase(context, uiSelectMany, 
					 convertedValues.getClass(), 
					 newValues);
	    }
	    catch (ConverterException e) {
		addConversionErrorMessage(context, uiSelectMany,
					  e.getMessage());
		uiSelectMany.setValid(false);
	    }
	}
	
	// At this point, result is ready to be set as the value
	uiSelectMany.setValue(result);
    }
    
    protected Object handleArrayCase(FacesContext context, 
				     UISelectMany uiSelectMany,
				     Class arrayClass,
				     String [] newValues) throws ConverterException {
	Object result = null;
	Class elementType = null;
	Converter converter = null;
	int 
	    i = 0, 
	    len = (null != newValues ? newValues.length : 0);
	
	elementType = arrayClass.getComponentType();

	// Optimization: If the elemntType is String, we don't need
	// conversion.  Just return newValues.
	if (elementType.equals(String.class)) {
	    return newValues;
	}
	
	try {
	    result = Array.newInstance(elementType, len);
	}
	catch (Exception e) {
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
	    // if that fails, look for a by-type converter
	    if (null == (converter = Util.getConverterForClass(elementType))) {
		throw new ConverterException("null Converter");
	    }
	}
	
	Assert.assert_it(null != result);
	if (elementType.isPrimitive()) {
	    for (i = 0; i < len; i++) {
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
	}
	else {
	    for (i = 0; i < len; i++) {
		Array.set(result, i, converter.getAsObject(context,
							   uiSelectMany,
							   newValues[i]));
	    }
	}
	return result;
    }

    protected Object handleListCase(FacesContext context, 
				    String [] newValues) {
	int 
	    i = 0, 
	    len = newValues.length;
	ArrayList result = new ArrayList(len);
	for (i = 0; i < len; i++) {
	    result.add(newValues[i]);
	}

	return result;
    }
	
    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }
    
    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
                
        String styleString = null;
        ResponseWriter writer = null;
	
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }    
          
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        
	String styleClass = null;
        if (null != (styleClass = (String) component.getAttribute("styleClass"))) {
	    writer.writeAttribute("class", styleClass, "styleClass");
	    writer.startElement("span", component);
	} else if (null != (styleClass = 
	    (String) component.getAttribute("styleClass"))) {
	    writer.writeAttribute("class", styleClass, "styleClass");
	    writer.startElement("span", component);
	}
	
        renderSelect(context, component);
        if (null != styleString) {
	    writer.endElement("span");
	}
    }

    // Render the "select" portion..
    //
    void renderSelect ( FacesContext context, 
        UIComponent component) throws IOException {
        
	ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

	writer.startElement("select", component);
	writer.writeAttribute("name", component.getClientId(context), "clientId");
	if (!getMultipleText(component).equals("")) {
	    writer.writeAttribute("multiple", new Boolean("true"), null);
	}

	// Determine how many option(s) we need to render, and update
	// the component's "size" attribute accordingly;  The "size"
	// attribute will be rendered as one of the "pass thru" attributes
	//
	int itemCount = getOptionNumber(context, component);
        itemCount = getDisplaySize(itemCount, component);

        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);

	// Now, render the "options" portion...
	//
	renderOptions(context, component);

	writer.endElement("select");
    }

    int getOptionNumber(FacesContext context, UIComponent component) {
        Iterator items = Util.getSelectItemWrappers(context, component);
	int itemCount = 0;
	while(items.hasNext()) {
	    itemCount++;
	    items.next();
	}
	return itemCount;
    }

    void renderOptions (FacesContext context, UIComponent component) 
        throws IOException {
        Object selectedValues[] = getCurrentSelectedValues(context, component);
        Iterator items = Util.getSelectItemWrappers(context, component);

        UIComponent curComponent;
        SelectItem curItem = null;
        SelectItemWrapper curItemWrapper = null;
	ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        while (items.hasNext()) {
            curItemWrapper = (SelectItemWrapper) items.next();
            curItem = curItemWrapper.getSelectItem();
            curComponent = curItemWrapper.getUISelectItem();
	    writer.writeText("\t", null);
	    writer.startElement("option", curComponent);
	    writer.writeAttribute("value", 
                getFormattedValue(context, component, curItem.getValue()), "value");
	    String selectText = getSelectedText(curItem, selectedValues);
	    if (!selectText.equals("")) {
	        writer.writeAttribute(selectText, new Boolean("true"), null);
	    }

            Util.renderPassThruAttributes(writer, curComponent);
            Util.renderBooleanPassThruAttributes(writer, curComponent);

	    writer.writeText(curItem.getLabel(), "label");
	    writer.endElement("option");
	    writer.writeText("\n", null);
        }
    }

    String getSelectedText(SelectItem item, Object[] values) {
        if (null != values) {
            int len = values.length;
            for (int i = 0; i < len; i++) {
                if (values[i].equals(item.getValue())) {
                    return getSelectedTextString();
                }
            }
        }
        return "";
    }

    protected int getDisplaySize(int itemCount, UIComponent component) {
        // if size is not specified default to 1.
        itemCount = 1;
        if (null != component.getAttribute("size")) {
            Integer size = Integer.valueOf((String)component.getAttribute("size"));
            itemCount = size.intValue();
        } else {
             component.setAttribute("size", String.valueOf(itemCount));
        }     
        return itemCount;
    }
    
    String getSelectedTextString() {
        return " selected";
    }
	
    // To derive a selectOne type component from this, override
    // these methods.
    String getMultipleText(UIComponent component) {
        // PENDING (visvan) not sure if this is the best way to check for
        // component type.
        if (component instanceof UISelectMany) {
            return " multiple ";
        } 
        return "";
    }

    Object[] getCurrentSelectedValues(FacesContext context,
				      UIComponent component) {
        if (component instanceof UISelectMany) {
            UISelectMany select = (UISelectMany) component;
            return (Object []) select.currentValue(context);
        } 
        UISelectOne select = (UISelectOne) component;
	Object returnObjects[] = new Object[1];
	if (null != (returnObjects[0] = select.currentValue(context))) {
            return returnObjects;
        }    
	return null;
    }
    
 } // end of class MenuRenderer
