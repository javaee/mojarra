/*
 * $Id: UISelectBoolean.java,v 1.4 2002/01/17 22:25:20 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Iterator;

/**
 * Class for representing a user-interface component which allows
 * the user to select a boolean value.
 */
public class UISelectBoolean extends UIComponent {
    private static String TYPE = "SelectBoolean";

    private String modelReference = null;
    private String messageModelReference = null;
    private boolean selected = false;

    /** 
     * Returns a String representing the select-boolean type.  
     *
     * @return a String object containing &quot;SelectBoolean&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    /**
     * The model-reference property for this data-bound component.
     * This property contains a reference to the object which acts
     * as the data-source for this component.  The model-reference
     * must resolve to an object which implements one of the following types:
     * <ul>
     * <li><code>java.lang.Boolean</code>
     * </ul>  
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
     * Returns the current state for this component.
     * If this component's model property is non-null, it will
     * return the current value contained in the object
     * referenced by the model property. If the model property
     * is null, it will return a locally stored value.
     *
     * @see #getModel
     * @param rc the render context used to render this component
     * @return boolean containing the current state
     */
    public boolean isSelected(RenderContext rc) {

        boolean state = false;
        if ( modelReference == null )  {
            return selected;
        }
        else {
            try {
                String state_str = (String) rc.getObjectAccessor().
                        getObject(rc.getRequest(), (String) modelReference);
                state = (Boolean.valueOf(state_str)).booleanValue();
            } catch ( FacesException e ) {
                // PENDING (visvan) skip this exception ??
                return selected;
            }
            return state;
        }
    }

    /**
     * Sets the current state for this component.
     * If this component's model property is non-null, it will
     * store the new value in the object referenced by the
     * model property.  If the model property is null, it
     * will store the value locally.
     * @param rc the render context used to render this component
     * @param state boolean containing the new state for this component
     */
    public void setSelected(RenderContext rc, boolean state) {
        if ( modelReference == null ) {
            selected = state;
        } else {
            try {
                String state_str = String.valueOf( state );
                rc.getObjectAccessor().setObject(rc.getRequest(),
						 (String)modelReference,state_str);
            } catch ( FacesException e ) {
                // PENDING ( visvan ) skip this exception ??
                selected = state;
            }
        }

    }

    /**
     * Registers the specified listener id as a value-change listener
     * for this component.  The specified listener id must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>ValueChangeListener</code> interface, else an exception will
     * be thrown.
     * @see ValueChangeListener
     * @param listenerId the id of the value-change listener
     * @throws FacesException if listenerId is not registered in the
     *         scoped namespace or if the object referred to by listenerId
     *         does not implement the <code>ValueChangeListener</code> interface.
     */
    public void addValueChangeListener(String listenerId) throws FacesException {
    }

    /**
     * Removes the specified listener id as a value-change listener
     * for this component.  
     * @param listenerId the id of the value-change listener
     * @throws FacesException if listenerId is not registered as a
     *         value-change listener for this component.
     */
    public void removeValueChangeListener(String listenerId) throws FacesException {
    }

    /**
     * @return Iterator containing the ValueChangeListener instances registered
     *         for this component
     */
    public Iterator getValueChangeListeners() {
	return null;
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
