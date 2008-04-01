/*
 * $Id: Validator.java,v 1.3 2002/03/16 00:09:04 eburns Exp $
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
 * PENDING(aim): EventContext will become FacesContext in next rev
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
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public void validate(EventContext ec, UIComponent component, Object value) throws 
            ValidationException;

}
