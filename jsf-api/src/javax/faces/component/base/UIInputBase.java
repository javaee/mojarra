/*
 * $Id: UIInputBase.java,v 1.3 2003/07/27 00:48:25 craigmcc Exp $
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
    private ArrayList validators = null;


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


}
