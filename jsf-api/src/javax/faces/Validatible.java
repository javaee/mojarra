/*
 * $Id: Validatible.java,v 1.5 2002/04/05 19:40:19 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Iterator;

/**
 * The interface implemented by user-interface components which
 * require validation.
 * @see Validator
 */
public interface Validatible {

    /**
     * valid state indicating the validatible component's value
     * has not been validated.
     */
    public static final int UNVALIDATED = 0;

    /**
     * valid state indicating the validatible component's value
     * has been validated and is considered valid.
     */
    public static final int VALID = 1;

    /**
     * valid state indicating the validatible component's value
     * has been validated and is considered invalid.
     */
    public static final int INVALID = 2;

    /**
     * Registers the specified validator id as a validator
     * for this validatible component.  The specified validator id must be registered
     * in the scoped namespace, else an exception will be thrown.
     * PENDING(aim): this should take a Validator instance, not an ID
     * @see Validator
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered in the
     *         scoped namespace or if the object referred to by validatorId
     *         does not implement the <code>Validator</code> interface.
     */
    public void addValidator(String validatorId);

    /**
     * Removes the specified validator id as a validator
     * for this validatible component.
     * PENDING(aim): this should take a Validator instance, not an ID  
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered as a
     *         validator for this component.
     */
    public void removeValidator(String validatorId);

    /**
     * @return Iterator containing the Validator instances registered
     *         for this component
     */
    public Iterator getValidators();

    /**
     * The &quot;validState&quot; attribute which describes the current
     * valid state of this component.  Valid state may be one of the
     * following:
     * <ul>
     * <li><code>UNVALIDATED</code>
     * <li><code>VALID</code>
     * <li><code>INVALID</code>
     * </ul>
     * @see #setValidState
     * @return integer containing the current valid state of this
     *         component
     */
    public int getValidState();

    /**
     * Sets the &quot;validState&quot; attribute for this component.
     * @see #getValidState
     * @param validState integer containing the valid state of this
     *        component
     * @throws IllegalParameterException if validState is not
     *         UNVALIDATED, VALID, or INVALID
     */
    public void setValidState(int validState);

    /**
     * Performs validation on the component's current value.
     * @param fc FacesContext object representing the event-processing 
     *           phase of this request
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public void doValidate(FacesContext fc);

}

