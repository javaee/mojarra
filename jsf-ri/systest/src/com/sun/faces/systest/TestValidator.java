/*
 * $Id: TestValidator.java,v 1.1 2003/05/20 17:00:01 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.util.Collections;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;


/**
 * <p>Test implementation of {@link Validator}.</p>
 */

public class TestValidator implements Validator {


    public void validate(FacesContext context, UIComponent component) {
        ;  // No action taken
    }


}
