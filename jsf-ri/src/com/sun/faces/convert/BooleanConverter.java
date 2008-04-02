/*
 * $Id: BooleanConverter.java,v 1.1 2002/09/23 20:31:21 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.convert;


import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.FormatPool;
import com.sun.faces.util.Util;

import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.mozilla.util.Assert;

/**
 * Implement converter support so that <code>&lt;input_text&gt;</code> can
 * be used to implement the equivalent of <code>&lt;input_date&gt;</code>
 */
public class BooleanConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component,
        String value) throws ConverterException {

        if (value == null) {
            return (value);
        }

//PENDING(rogerk) should we specifically check to make sure the string value
// is either "true" or "false" and if not throw an exception?
// If string value is neither, then converted Boolean will be "false"
//
        return Boolean.valueOf(value);
    }

    public String getAsString(FacesContext context, UIComponent component,
        Object value) throws ConverterException {

        if ((value == null) || (value instanceof String)) {
            return ((String) value);
        }
        Boolean bool = null;
        try {
            bool = (Boolean) value;
        } catch (Exception e) {
            throw new ConverterException(Util.getExceptionMessage(
                Util.CONVERSION_ERROR_MESSAGE_ID));
        }
        return bool.toString();
    }
}
