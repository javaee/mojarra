/*
 * $Id: UIInputBase.java,v 1.4 2003/07/28 22:18:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.ArrayList;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.Message;
import javax.faces.application.MessageResources;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import javax.faces.validator.Validator;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;



/**
 * <p><strong>UIInputBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIInput}.</p>
 */

public class UIInputBase extends UIOutputBase implements UIInput {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIInputBase} instance with default property
     * values.</p>
     */
    public UIInputBase() {

        super();
        setRendererType("Text");

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The previous value of this {@link UIInput} component.</p>
     */
    private Object previous = null;


    public Object getPrevious() {

        return (this.previous);

    }


    public void setPrevious(Object previous) {

        this.previous = previous;

    }


    /**
     * <p>The "required field" state for this component.</p>
     */
    private boolean required = false;


    public boolean isRequired() {

	return (this.required);

    }


    public void setRequired(boolean required) {

	this.required = required;

    }


    // ----------------------------------------------------- UIComponent Methods


    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isValid()) {
            return;
        }
        String valueRef = getValueRef();
        if (valueRef == null) {
            return;
        }
        try {
	    Application application = context.getApplication();
            ValueBinding binding = application.getValueBinding(valueRef);
            binding.setValue(context, getValue());
            setValue(null);
            return;
        } catch (FacesException e) {
            setValid(false);
            throw e;
        } catch (IllegalArgumentException e) {
            setValid(false);
            throw e;
        } catch (Exception e) {
            setValid(false);
            throw new FacesException(e);
        }

    }


    // ------------------------------------------------------ Validation Methods


    /**
     * <p>The valid state of this {@link UIInput} component.</p>
     */
    protected boolean valid = true;


    public boolean isValid() {

	return (this.valid);

    }


    public void setValid(boolean valid) {

	this.valid = valid;

    }


    public void validate(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Save and reset the previous value for this component
        Object previous = getPrevious();
        setPrevious(null);

	// If our value is valid, enforce the required property if present
	if (isValid() && isRequired() && isEmpty()) {
	    Message message =
		context.getApplication().
		getMessageResources(MessageResources.FACES_API_MESSAGES).
		getMessage(context, REQUIRED_MESSAGE_ID);
	    context.addMessage(this, message);
	    setValid(false);
	}

	// If our value is valid and not empty, call all external validators
	if (isValid() && !isEmpty() && (this.validators != null)) {
	    Iterator validators = this.validators.iterator();
	    while (validators.hasNext()) {
		Validator validator = (Validator) validators.next();
		validator.validate(context, this);
	    }
	}

	// If our value is valid, emit a ValueChangedEvent if appropriate
	if (isValid()) {
	    Object value = getValue();
            if (compareValues(previous, value)) {
                context.addFacesEvent
                    (new ValueChangedEvent(this, previous, value));
            }
        }

    }


    /**
     * <p>Return <code>true</code> if the new value is different from the
     * previous value.</p>
     *
     * @param previous old value of this component (if any)
     * @param value new value of this component (if any)
     */
    protected boolean compareValues(Object previous, Object value) {

        if (previous == null) {
            return (value != null);
        } else if (value == null) {
            return (true);
        } else {
            return (!(previous.equals(value)));
        }

    }


    private boolean isEmpty() {

	Object value = getValue();
	if (value == null) {
	    return (true);
	} else if ((value instanceof String) &&
		   (((String) value).length() < 1)) {
	    return (true);
	} else {
	    return (false);
	}

    }
    

    /**
     * <p>The set of {@link Validator}s associated with this
     * <code>UIComponent</code>.</p>
     */
    protected ArrayList validators = null;


    public void addValidator(Validator validator) {

        if (validator == null) {
            throw new NullPointerException();
        }
        if (validators == null) {
            validators = new ArrayList();
        }
        validators.add(validator);

    }


    public void removeValidator(Validator validator) {

        if (validators != null) {
            validators.remove(validator);
        }

    }


    // -------------------------------------------------- Event Listener Methods


    public void addValueChangedListener(ValueChangedListener listener) {

        addFacesListener(listener);

    }


    public void removeValueChangedListener(ValueChangedListener listener) {

        removeFacesListener(listener);

    }


    // ---------------------------------------------- methods from StateHolder

    public void restoreState(FacesContext context, 
			     Object stateObj) throws IOException {
	Object [] state = (Object []) stateObj;
	Object [] thisState = (Object []) state[THIS_INDEX];

	// restore the attributes
	String stateStr = (String) thisState[ATTRS_INDEX];
	int i = stateStr.indexOf(STATE_SEP);
	required = Boolean.valueOf(stateStr.substring(0, i)).booleanValue();
	valid = Boolean.valueOf(stateStr.substring(i + STATE_SEP_LEN)).booleanValue();
	// restore the listeners
	listeners = context.getApplication().getViewHandler().
	    getStateManager().restoreAttachedObjectState(context, 
							 thisState[LISTENERS_INDEX]);
	// restore the validators
	List [] validatorsList = context.getApplication().getViewHandler().
	    getStateManager().restoreAttachedObjectState(context, 
							 thisState[VALIDATORS_INDEX]);
	if (null != validatorsList) {
	    validators = (ArrayList) validatorsList[0];
	}
	// restore the value
	previous = thisState[PREVIOUS_INDEX];
	
	super.restoreState(context, state[SUPER_INDEX]);
    }

    private static final int ATTRS_INDEX = 0;
    private static final int LISTENERS_INDEX = 1;
    private static final int PREVIOUS_INDEX = 2;
    private static final int VALIDATORS_INDEX = 3;


    public Object getState(FacesContext context) {
	// get the state of our superclasses.
	Object superState = super.getState(context);
	Object [] result = new Object[2];
	Object [] thisState = new Object[4];
	// save the attributes
	thisState[ATTRS_INDEX] = required + STATE_SEP + valid;
	// save the listeners
	thisState[LISTENERS_INDEX] = context.getApplication().getViewHandler().getStateManager().getAttachedObjectState(context, this, "listeners", listeners);
	if (null != validators) {
	    List [] validatorsList = new List[1];
	    validatorsList[0] = validators;
	    thisState[VALIDATORS_INDEX] = context.getApplication().getViewHandler().getStateManager().getAttachedObjectState(context, this, "validators", validatorsList);
	}
	// save the value
	if (previous instanceof Serializable) {
	    thisState[PREVIOUS_INDEX] = previous;
	}
	result[THIS_INDEX] = thisState;
	// save the state of our superclass
	result[SUPER_INDEX] = superState;
	return result;
    }    


}
