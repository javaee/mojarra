/*
 * $Id: TestValidator.java,v 1.2 2003/07/26 17:55:20 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.Collections;
import java.util.Iterator;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;


/**
 * <p>Test implementation of {@link Validator}.</p>
 */

public class TestValidator implements Validator {


    public void validate(FacesContext context, UIInput component) {
        ;  // No action taken
    }


}
