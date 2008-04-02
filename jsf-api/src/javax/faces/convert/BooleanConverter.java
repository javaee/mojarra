/*
 * $Id: BooleanConverter.java,v 1.1 2003/08/12 03:51:42 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p>{@link Converter} implementation for <code>java.lang.Boolean</code>
 * (and boolean primitive) values.</p>
 */

public class BooleanConverter implements Converter {


    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) throws ConverterException {

        try {
            return (Boolean.valueOf(value));
        } catch (Exception e) {
            throw new ConverterException(e);
        }


    }


    public String getAsString(FacesContext context, UIComponent component,
                              Object value) throws ConverterException {

        try {
            return (Boolean.toString(((Boolean) value).booleanValue()));
        } catch (ConverterException e) {
            throw new ConverterException(e);
        }

    }


}
