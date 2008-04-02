/*
 * $Id: TestConverter01.java,v 1.1 2004/12/02 18:42:26 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * <p>Test implementation of {@link Converter}.</p>
 */
public class TestConverter01 implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component,
                              String newValue) throws ConverterException {
        context.addMessage(component.getClientId(context),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                component.getId() + " was converted to Object", null));

        return newValue;
    }


    public String getAsString(FacesContext context, UIComponent component,
                              Object value) throws ConverterException {
        context.addMessage(component.getClientId(context),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                component.getId() + " was converted to String", null));

        return (value.toString());
    }
}
