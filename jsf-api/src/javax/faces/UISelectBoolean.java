/*
 * $Id: UISelectBoolean.java,v 1.9 2002/04/05 19:40:19 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Iterator;
import javax.servlet.ServletRequest;
import java.util.Vector;
import java.util.EventObject;

/**
 * Class for representing a user-interface component which allows
 * the user to select a boolean value.
 */
public class UISelectBoolean extends UIComponent implements EventDispatcher {
    private static String TYPE = "SelectBoolean";

    private String messageModelReference = null;

    private Vector valueChangeListeners = null;

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
     * Returns the current state for this component.
     * If this component's model property is non-null, it will
     * return the current value contained in the object
     * referenced by the model property. If the model property
     * is null, it will return a locally stored value.
     *
     * @see #getModel
     * @param fc the faces context used to render this component
     * @return boolean containing the current state
     */
    public boolean isSelected(FacesContext fc) {
        boolean state;

	if (Boolean.valueOf((String) getValue(fc)).booleanValue()) {
	    state = true;
	}
	else {
	    state = false;
	}
	return state;
    }

    /**
     * Sets the current state for this component.
     * If this component's model property is non-null, it will
     * store the new value in the object referenced by the
     * model property.  If the model property is null, it
     * will store the value locally.
     * @param fc the faces context used to facesContext this component
     * @param state boolean containing the new state for this component
     */
    public void setSelected(FacesContext fc, boolean state) {
	if (state == true) {
	    setValue("true");
	}
	else {
	    setValue("false");
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
        if ( listenerId != null ) {
             if ( valueChangeListeners == null ) {
                 valueChangeListeners = new Vector();
             }
             valueChangeListeners.add(listenerId);
         }
    }

    /**
     * Removes the specified listener id as a value-change listener
     * for this component.  
     * @param listenerId the id of the value-change listener
     * @throws FacesException if listenerId is not registered as a
     *         value-change listener for this component.
     */
    public void removeValueChangeListener(String listenerId) throws FacesException {
        // Assert.assert_it( valueChangeListeners != null );
        if ( listenerId != null ) {
             valueChangeListeners.remove(listenerId);
        }
    }

    /**
     * @return Iterator containing the ValueChangeListener instances registered
     *         for this component
     */
    public Iterator getValueChangeListeners() {
        return valueChangeListeners == null? null : valueChangeListeners.iterator();
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

    public void doValidate(FacesContext ctx) {
    }

    /**
     * Dispatches the specified event to any registered listeners.
     * @param e the object describing the event
     */
    public void dispatch(EventObject e) throws FacesException {

        // ParameterCheck.nonNull(e);

        ValueChangeEvent value_event = null;
        if ( e instanceof ValueChangeEvent)  {
            value_event = (ValueChangeEvent) e;
        } else {
            throw new FacesException("Invalid event type. " +
                    "Expected ValueChangeEvent");
        }

        String new_value = (String) value_event.getNewValue();

        FacesContext facesContext = value_event.getFacesContext();
        // Assert.assert_it( facesContext != null );

        ObjectManager ot = facesContext.getObjectManager();
        // Assert.assert_it( ot != null );

        ServletRequest request = facesContext.getRequest();

        FacesContext fc = (FacesContext)ot.get(request,
                Constants.REF_FACESCONTEXT);
        // Assert.assert_it( fc != null );

	boolean state = (Boolean.valueOf(new_value)).booleanValue();
	setSelected(fc, state);
	pushValueToModel(fc);

        // dispatch value change listeners.
        if ( valueChangeListeners == null ) {
            return;
        }    
        Iterator listeners = getValueChangeListeners();
        // Assert.assert_it( listeners != null );
        
        while ( listeners.hasNext() ) {
            String listenerName = (String) listeners.next();
            // Assert.assert_it( listenerName != null );

            ValueChangeListener vcl = (ValueChangeListener)ot.get(request,
                    listenerName);
            // Assert.assert_it ( vcl != null );
            vcl.handleValueChange(value_event);
        }
    }

}
