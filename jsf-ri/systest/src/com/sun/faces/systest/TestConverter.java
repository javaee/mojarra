/*
 * $Id: TestConverter.java,v 1.4 2004/02/26 20:33:40 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * <p>Test implementation of {@link Converter}.</p>
 */
public class TestConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component,
                              String newValue) throws ConverterException {
        // No action taken
        return newValue;
    }


    public String getAsString(FacesContext context, UIComponent component,
                              Object value) throws ConverterException {
        // No action taken
        return (value.toString());
    }
}
