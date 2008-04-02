/*
 * $Id: DateConverter.java,v 1.1 2002/09/23 20:31:22 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.convert;


import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.FormatPool;
import com.sun.faces.util.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.mozilla.util.Assert;

/**
 * Implement converter support so that <code>&lt;input_text&gt;</code> can
 * be used to implement the equivalent of <code>&lt;input_date&gt;</code>
 */
public class DateConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component,
        String newValue) throws ConverterException {

        Object convertedValue = null;
        Class modelType = null;
        String modelRef = null;
        Date newDateValue = null;

        if (null == newValue) {
            return (newValue);
        }

        modelRef = component.getModelReference();

        // Try to get the newValue as a Date
        try {
            newDateValue = parseDate(context, component, newValue);
        }
        catch (ParseException pe) {
            throw new ConverterException(Util.getExceptionMessage(
                Util.CONVERSION_ERROR_MESSAGE_ID));
        }

        if (null != modelRef) {
            try {
                modelType = context.getModelType(modelRef);
            } catch (FacesException fe ) {
                throw new ConverterException(Util.getExceptionMessage(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
            }
            Assert.assert_it(modelType != null );

            // Verify the modelType is one of the supported types
            if (modelType.isAssignableFrom(Date.class)) {
                convertedValue = newDateValue;
            }
            else if (modelType.isAssignableFrom(Long.class)) {
                convertedValue = (new Long(newDateValue.getTime()));
            }
            else {
                throw new ConverterException(Util.getExceptionMessage(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
            }
        } else {
            convertedValue = newDateValue;
        }
        return convertedValue;
    }

    public String getAsString(FacesContext context, UIComponent component,
        Object value) throws ConverterException {
        if ((value == null) || (value instanceof String)) {
            return ((String) value);
        }
        Date date = null;
        try {
            date = (Date) value;
        } catch (Exception e) {
            throw new ConverterException(Util.getExceptionMessage(
                Util.CONVERSION_ERROR_MESSAGE_ID));
        }
        return formatDate(context, component, date);
    }

    protected Date parseDate(FacesContext context, UIComponent component, 
        String newValue) throws ParseException {
        FormatPool formatPool = null;
        formatPool = (FormatPool)
            context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
        Assert.assert_it(null != formatPool);
        return formatPool.dateFormat_parse(context, component, newValue);
    }

    protected String formatDate(FacesContext context, UIComponent component, 
        Date dateValue) {
        FormatPool formatPool = null;
        formatPool = (FormatPool)
            context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
        Assert.assert_it(null != formatPool);
        return formatPool.dateFormat_format(context, component, dateValue);
    }
}
