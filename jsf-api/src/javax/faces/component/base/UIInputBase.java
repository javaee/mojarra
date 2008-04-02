/*
 * $Id: UIInputBase.java,v 1.2 2003/07/26 17:54:49 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.Message;
import javax.faces.application.MessageResources;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
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


    // ------------------------------------------------ Event Processing Methods


    /**
     * <p>Array of {@link List}s of {@link ValueChangedListener}s registered
     * for particular phases.  The array, as well as the individual
     * elements, are lazily instantiated as necessary.</p>
     */
    protected List listeners[] = null;


    /**
     * <p>Add a new {@link ValueChangedListener} to the set of listeners
     * interested in being notified when {@link ValueChangedEvent}s occur.</p>
     *
     * @param listener The {@link ValueChangedListener} to be added
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void addValueChangedListener(ValueChangedListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            listeners = new List[PhaseId.VALUES.size()];
        }
        int ordinal = listener.getPhaseId().getOrdinal();
        if (listeners[ordinal] == null) {
            listeners[ordinal] = new ArrayList();
        }
        listeners[ordinal].add(listener);

    }


    /**
     * <p>Broadcast the specified {@link FacesEvent} to all registered
     * event listeners who have expressed an interest in events of this
     * type, for the specified {@link PhaseId}.  The order in which
     * registered listeners are notified is implementation dependent.</p>
     *
     * <p>After all interested listeners have been notified, return
     * <code>false</code> if this event does not have any listeners
     * interested in this event in future phases of the request processing
     * lifecycle.  Otherwise, return <code>true</code>.</p>
     *
     * @param event The {@link FacesEvent} to be broadcast
     * @param phaseId The {@link PhaseId} of the current phase of the
     *  request processing lifecycle
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @exception IllegalArgumentException if the implementation class
     *  of this {@link FacesEvent} is not supported by this component
     * @exception IllegalStateException if PhaseId.ANY_PHASE is passed
     *  for the phase identifier
     * @exception NullPointerException if <code>event</code> or
     *  <code>phaseId</code> is <code>null</code>
     */
    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException {

        if ((event == null) || (phaseId == null)) {
            throw new NullPointerException();
        }
        if (phaseId.equals(PhaseId.ANY_PHASE)) {
            throw new IllegalStateException();
        }
        if (event instanceof ValueChangedEvent) {
            if (listeners == null) {
                return (false);
            }
            ValueChangedEvent vcevent = (ValueChangedEvent) event;
            int ordinal = phaseId.getOrdinal();
            broadcast(vcevent, listeners[PhaseId.ANY_PHASE.getOrdinal()]);
            broadcast(vcevent, listeners[ordinal]);
            for (int i = ordinal + 1; i < listeners.length; i++) {
                if ((listeners[i] != null) && (listeners[i].size() > 0)) {
                    return (true);
                }
            }
            return (false);
        } else {
            throw new IllegalArgumentException();
        }

    }


    /**
     * <p>Broadcast the specified {@link ValueChangedEvent} to the
     * {@link ValueChangedListener}s on the specified list (if any)
     *
     * @param event The {@link ValueChangedEvent} to be broadcast
     * @param list The list of {@link ValueChangedListener}s, or
     *  <code>null</code> for no interested listeners
     */
    protected void broadcast(ValueChangedEvent event, List list) {

        if (list == null) {
            return;
        }
        Iterator listeners = list.iterator();
        while (listeners.hasNext()) {
            ValueChangedListener listener =
                (ValueChangedListener) listeners.next();
            listener.processValueChanged(event);
        }

    }


    /**
     * <p>Remove an existing {@link ValueChangedListener} (if any) from the
     * set of listeners interested in being notified when
     * {@link ValueChangedEvent}s occur.</p>
     *
     * @param listener The {@link ValueChangedListener} to be removed
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void removeValueChangedListener(ValueChangedListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            return;
        }
        int ordinal = listener.getPhaseId().getOrdinal();
        if (listeners[ordinal] == null) {
            return;
        }
        listeners[ordinal].remove(listener);

    }


}
