/*
 * $Id: TestValidator01.java,v 1.1 2004/12/02 18:42:26 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;


/**
 * <p>Test implementation of {@link Validator}.</p>
 */

public class TestValidator01 implements Validator {


    public void validate(FacesContext context, UIComponent component, Object value) {
        context.addMessage(component.getClientId(context),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                component.getId() + " was validated", null));
    }


}
