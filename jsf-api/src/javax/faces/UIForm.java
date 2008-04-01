/*
 * $Id: UIForm.java,v 1.3 2002/01/17 22:25:20 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;
import javax.servlet.ServletRequest;

/**
 * Class for representing a form user-interface component. 
 * A form encapsulates the process of taking a collection of
 * input from the user and submitting it to the application
 * in a single &quot;submit&quot; operation.  A &quot;form control&quot;
 * is a user-interface component added as a descendent to the form
 * for the purpose of:
 * <ol>
 * <li>collecting data from the user:
 * @see UISelectBoolean
 * @see UISelectOne
 * @see UITextEntry
 * <li>displaying form data to the user:
 * @see UIOutput
 * <li>executing a form command (submit, navigate, etc):
 * @see UICommand  
 * </ol>
 */
public class UIForm extends UIComponent {

    private static String TYPE = "Form";
    private String modelReference = null;
    private String messageModelReference = null;

    /** 
     * Returns a String representing the form's type.  
     *
     * @return a String object containing &quot;Form&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    /**
     * The model-reference property for this data-bound component.
     * This property contains a reference to the object which acts
     * as the data-source for this component.  If a property on the
     * associated model object matches the id of one of this form's
     * controls, the model-reference for that control will be implicitly
     * hooked up to that property on the form's model object.  If
     * the model-reference on this form is null, then model-references
     * on this form's controls must be set explicitly on each control.
     * @see #setModelReference  
     * @return String containing the model-reference for this component
     */
    public String getModelReference() {
        return modelReference;
    }

    /**
     * Sets the model-reference property on this data-bound component.
     * @see #getModelReference
     * @param modelReference the String which contains a reference to the
     *        object which acts as the data-source for this component
     */
    public void setModelReference(String modelReference) {
        this.modelReference = modelReference;
    }

    /**
     * Registers the specified listener id as a form listener
     * for this component.  The specified listener id must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>FormListener</code> interface, else an exception will
     * be thrown.
     * @see FormListener
     * @param listenerId the id of the form listener
     * @throws FacesException if listenerId is not registered in the
     *         scoped namespace or if the object referred to by listenerId
     *         does not implement the <code>FormListener</code> interface.
     */
    public void addFormListener(ServletRequest req, String listenerId) 
            throws FacesException {
    }

    /**
     * Removes the specified listener id as a form listener
     * for this component.  
     * @param listenerId the id of the form listener
     * @throws FacesException if listenerId is not registered as a
     *         form listener for this component.
     */
    public void removeFormListener(String listenerId) throws FacesException {
    }

    /**
     * @return Iterator containing the FormListener instances registered
     *         for this component
     */
    public Iterator getFormListeners() {
	return null; //compile
    }

    /**
     * Registers the specified validator id as a validator
     * for this validatible component.  The specified validator id must be registered
     * in the scoped namespace, else an exception will be thrown.
     * @see Validator
     * @see #removeValidator
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered in the
     *         scoped namespace or if the object referred to by validatorId
     *         does not implement the <code>Validator</code> interface.
     */
    public void addValidator(String validatorId) {
    }

    /**
     * Removes the specified validator id as a validator
     * for this validatible component.
     * @see #addValidator  
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered as a
     *         validator for this component.
     */
    public void removeValidator(String validatorId) {
    }

    /**
     * @return Iterator containing the Validator instances registered
     *         for this component
     */
    public Iterator getValidators() {
	return null; //compile
    }

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
    public String getMessageModelReference() {
	return messageModelReference;
    }

    /**
     * Sets the message model-reference property on this validatible component.
     * @see #getMessageModelReference
     * @param modelReference the String which contains a reference to the
     *        object which acts as the store for any validation error messages
     */
    public void setMessageModelReference(String modelReference) {
	this.messageModelReference = modelReference;
    }

    /**
     * The &quot;validState&quot; attribute which describes the current
     * valid state of this component.  Valid state may be one of the
     * following:
     * <ul>
     * <li><code>Validatible.UNVALIDATED</code>
     * <li><code>Validatible.VALID</code>
     * <li><code>Validatible.INVALID</code>
     * </ul>
     * @see #setValidState
     * @return integer containing the current valid state of this
     *         component
     */
    public int getValidState() {
	Integer valid = (Integer)getAttribute(null, "validState");
	return valid != null? valid.intValue() : Validatible.UNVALIDATED;
    }

    /**
     * Sets the &quot;validState&quot; attribute for this component.
     * @see #getValidState
     * @param validState integer containing the valid state of this
     *        component
     * @throws IllegalParameterException if validState is not
     *         UNVALIDATED, VALID, or INVALID
     */
    public void setValidState(int validState) {
    }

}
