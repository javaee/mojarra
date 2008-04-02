/*
 * $Id: NumberConverter.java,v 1.1 2002/09/23 20:31:22 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

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
        Class modelType = null;

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
        String modelRef = component.getModelReference();
        if ( modelRef == null ) {
             return parsedValue;
        }
        // convert the parsed value to model property type.
        try {
            modelType = context.getModelType(modelRef);
        } catch (FacesException fe ) {
            throw new ConverterException(Util.getExceptionMessage(
                Util.CONVERSION_ERROR_MESSAGE_ID));
        }
        Assert.assert_it(modelType != null);
        Assert.assert_it(parsedValue != null);

        if ( (modelType.getName()).equals("java.lang.Character") ||
                (modelType.getName()).equals("char")) {
            // 36 is the maximum value allowed for radix.
            char charvalue = Character.forDigit(parsedValue.intValue(),36);
            convertedValue = (new Character(charvalue));
        } else {
            convertedValue = convertToModelType(modelType, parsedValue);
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

    private Number convertToModelType(Class modelType, Number parsedValue) {
        Assert.assert_it(parsedValue != null);

        // PENDING (visvan) If it comes to rounding should we throw
        // an exception
        if ( (modelType.getName()).equals("java.lang.Byte") ||
                (modelType.getName()).equals("byte")) {
            return (new Byte(parsedValue.byteValue()));
        } else if ( (modelType.getName()).equals("java.lang.Double") ||
                (modelType.getName()).equals("double")) {
            return (new Double(parsedValue.doubleValue()));
        } else if ( (modelType.getName()).equals("java.lang.Float") ||
                (modelType.getName()).equals("float") ) {
            return (new Float(parsedValue.floatValue()));
        } else if ( (modelType.getName()).equals("java.lang.Integer") ||
               (modelType.getName()).equals("int")) {
            return (new Integer(parsedValue.intValue()));
        } else if ( (modelType.getName()).equals("java.lang.Short") ||
                (modelType.getName()).equals("short") ) {
            return (new Short(parsedValue.shortValue()));
        } else if ( (modelType.getName()).equals("java.lang.Long") ||
                (modelType.getName()).equals("long")) {
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
