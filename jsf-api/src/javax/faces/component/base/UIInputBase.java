/*
 * $Id: UIInputBase.java,v 1.7 2003/09/15 20:17:25 eburns Exp $
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
import javax.faces.component.Repeater;
import javax.faces.component.RepeaterSupport;
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

        Repeater repeater = RepeaterSupport.findParentRepeater(this);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                return (repeater.getChildPrevious(this));
            } else {
                return (this.previous);
            }
        } else {
            return (this.previous);
        }

    }


    public void setPrevious(Object previous) {

        this.previous = previous;
        Repeater repeater = RepeaterSupport.findParentRepeater(this);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                repeater.setChildPrevious(this, previous);
            } else {
                this.previous = previous;
            }
        } else {
            this.previous = previous;
        }

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

        Repeater repeater = RepeaterSupport.findParentRepeater(this);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                return (repeater.isChildValid(this));
            } else {
                return (this.valid);
            }
        } else {
            return (this.valid);
        }

    }


    public void setValid(boolean valid) {

        Repeater repeater = RepeaterSupport.findParentRepeater(this);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                repeater.setChildValid(this, valid);
            } else {
                this.valid = valid;
            }
        } else {
            this.valid = valid;
        }

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
    protected List validators = null;


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


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = previous;
        values[2] = required ? Boolean.TRUE : Boolean.FALSE;
        values[3] = valid ? Boolean.TRUE : Boolean.FALSE;
        List validatorsList[] = new List[1];
        validatorsList[0] = validators;
        values[4] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, this, null, validatorsList);
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        previous = values[1];
        required = ((Boolean) values[2]).booleanValue();
        valid = ((Boolean) values[3]).booleanValue();
        List validatorsList[] = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[4]);
        if (validatorsList != null) {
            validators = (List) validatorsList[0];
        }

    }


}
