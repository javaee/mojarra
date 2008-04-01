/*
 * $Id: TestValidator.java,v 1.2 2002/07/26 21:53:33 craigmcc Exp $
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


    public AttributeDescriptor getAttributeDescriptor(String name) {
        return (null);
    }

    public Iterator getAttributeNames() {
        return (Collections.EMPTY_LIST.iterator());
    }

    public void validate(FacesContext context, UIComponent component) {
        ; // No action taken
    }


}
