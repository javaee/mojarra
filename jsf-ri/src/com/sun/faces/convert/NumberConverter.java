/*
 * $Id: NumberConverter.java,v 1.3 2003/03/19 21:16:29 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.convert;


import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.FormatPool;
import com.sun.faces.renderkit.html_basic.NumberRenderer;
import com.sun.faces.util.Util;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.component.UIOutput;
import javax.faces.el.ValueBinding;

import org.mozilla.util.Assert;

/**
 * Implement converter support so that <code>&lt;input_text&gt;</code> can
 * be used to implement the equivalent of <code>&lt;input_number&gt;</code>
 */
public class NumberConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component,
        String newValue) throws ConverterException {

        Object convertedValue = null;
        Number parsedValue = null;
        Class valueType = null;
        String valueRef = null;
        
        if (null == newValue || newValue.length() == 0) {
            return (newValue);
        }

        newValue = newValue.trim();
        // get FormatPool Instance from ServletContext
        FormatPool formatPool = (FormatPool)
            context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
        Assert.assert_it(null != formatPool);

        try {
            parsedValue = formatPool.numberFormat_parse(context, component, 
                newValue);
        } catch (ParseException pe ) {
            throw new ConverterException(pe.getMessage());
        }

        // if modelReference is null, store value as Number.
       if ( component instanceof UIInput) {
            valueRef = ((UIInput)component).getValueRef();
        }
        if ( valueRef == null ) {
             return parsedValue;
        }
        // convert the parsed value to model property type.
        try {
            valueType = (Util.getValueBinding(valueRef)).getType(context);
        } catch (FacesException fe ) {
            throw new ConverterException(Util.getExceptionMessage(
                Util.CONVERSION_ERROR_MESSAGE_ID));
        }
        Assert.assert_it(valueType != null);
        Assert.assert_it(parsedValue != null);

        if ( (valueType.getName()).equals("java.lang.Character") ||
                (valueType.getName()).equals("char")) {
            // 36 is the maximum value allowed for radix.
            char charvalue = Character.forDigit(parsedValue.intValue(),36);
            convertedValue = (new Character(charvalue));
        } else {
            convertedValue = convertToModelType(valueType, parsedValue);
        }
        return convertedValue;
    }

    public String getAsString(FacesContext context, UIComponent component,
        Object value) throws ConverterException {

        if ((value == null) || (value instanceof String)) {
            return ((String) value);
        }
        Number number = null;
        try {
            number = (Number) value;
        } catch (Exception e) {
            throw new ConverterException(Util.getExceptionMessage(
                Util.CONVERSION_ERROR_MESSAGE_ID));
        }
        return formatNumber(context, component, number);
    }

    private Number convertToModelType(Class valueType, Number parsedValue) {
        Assert.assert_it(parsedValue != null);

        // PENDING (visvan) If it comes to rounding should we throw
        // an exception
        if ( (valueType.getName()).equals("java.lang.Byte") ||
                (valueType.getName()).equals("byte")) {
            return (new Byte(parsedValue.byteValue()));
        } else if ( (valueType.getName()).equals("java.lang.Double") ||
                (valueType.getName()).equals("double")) {
            return (new Double(parsedValue.doubleValue()));
        } else if ( (valueType.getName()).equals("java.lang.Float") ||
                (valueType.getName()).equals("float") ) {
            return (new Float(parsedValue.floatValue()));
        } else if ( (valueType.getName()).equals("java.lang.Integer") ||
               (valueType.getName()).equals("int")) {
            return (new Integer(parsedValue.intValue()));
        } else if ( (valueType.getName()).equals("java.lang.Short") ||
                (valueType.getName()).equals("short") ) {
            return (new Short(parsedValue.shortValue()));
        } else if ( (valueType.getName()).equals("java.lang.Long") ||
                (valueType.getName()).equals("long")) {
            return (new Long(parsedValue.longValue()));
        }
        return parsedValue;
    }

    private String formatNumber(FacesContext context, UIComponent component, 
        Number numberValue) {
        FormatPool formatPool = null;
        formatPool = (FormatPool)
            context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
        Assert.assert_it(null != formatPool);
        return formatPool.numberFormat_format(context, component, numberValue);
    }
}
