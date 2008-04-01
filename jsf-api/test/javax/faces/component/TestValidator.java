/*
 * $Id: TestValidator.java,v 1.3 2002/08/29 05:39:16 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Collections;
import java.util.Iterator;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;


/**
 * <p>Test implementation of {@link Validator}.</p>
 */

public class TestValidator implements Validator {


    public boolean validate(FacesContext context, UIComponent component) {
        return (true); // No action taken
    }


}
