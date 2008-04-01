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
     * @see Validator
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered in the
     *         scoped namespace or if the object referred to by validatorId
     *         does not implement the <code>Validator</code> interface.
     */
    void addValidator(String validatorId);

    /**
     * Removes the specified validator id as a validator
     * for this validatible component.  
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered as a
     *         validator for this component.
     */
    void removeValidator(String validatorId);

    /**
     * @return Iterator containing the Validator instances registered
     *         for this component
     */
    Iterator getValidators();

   /**
     * The message model-reference property for this validatible component.
     * This property contains a reference to the object which acts
     * as the store for any validation error messages.  The model-reference
     * must resolve to an object which implements one of the following types:
     * <ul>
     * <li><code>java.lang.String</code>
     * <li><code>java.util.Collection</code> of <code>String</code> objects
     * </ul>  
     * @see #setMessageModelReference  
     * @return String containing the message model-reference for this component
     */
    String getMessageModelReference();

    /**
     * Sets the message model-reference property on this validatible component.
     * @see #getMessageModelReference
     * @param modelReference the String which contains a reference to the
     *        object which acts as the store for any validation error messages
     */
    void setMessageModelReference(String modelReference);

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
    int getValidState();

    /**
     * Sets the &quot;validState&quot; attribute for this component.
     * @see #getValidState
     * @param validState integer containing the valid state of this
     *        component
     * @throws IllegalParameterException if validState is not
     *         UNVALIDATED, VALID, or INVALID
     */
    void setValidState(int validState);

    /**
     * Performs validation on the specified value object. 
     * Subclasses must override this method to perform appropriate
     * validation.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    void validate(EventContext ec);

}

