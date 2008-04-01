/*
 * $Id: Validator.java,v 1.4 2002/04/05 19:40:20 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Iterator;

/**
 * Interface for implementing objects which can perform
 * validation on value objects and configure appropriate error
 * messages.
 */
public interface Validator {

    /**
     * @return String identifying the type of validator
     */
    public String getType();

    /**
     * Returns an iterator containing the names of the validator's
     * supported attributes. Validator attributes are set on a UI
     * component to control how the validator performs validation.
     * @return an iterator containing the Strings representing supported
     *          attribute names
     */
    public Iterator getSupportedAttributeNames();

    /**
     * @param fc FacesContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public void validate(FacesContext fc, UIComponent component, Object value) throws 
            ValidationException;

}
