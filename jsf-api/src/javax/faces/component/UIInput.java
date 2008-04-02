/*
 * $Id: UIInput.java,v 1.57 2004/01/06 23:02:41 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p><strong>UIInput</strong> is a {@link UIComponent} that represents
 * a component that both displays output to the user (like
 * {@link UIOutput} components do) and processes request parameters on the
 * subsequent request that need to be decoded.  There are no restrictions
 * on the data type of the local value, or the object referenced by the
 * value reference expression (if any); however, individual
 * {@link javax.faces.render.Renderer}s will generally impose restrictions
 * on the type of data they know how to display.</p>
 *
 * <p>During the <em>Apply Request Values</em> phase of the request
 * processing lifecycle, the decoded value of this component, usually
 * but not necessarily a String, must be stored - but not yet converted -
 * using <code>setSubmittedValue()</code>.  If the component wishes
 * to indicate that no particular value was submitted, it can either
 * do nothing, or set the submitted value to <code>null</code>.</p>
 * <p>
 * <p>During the <em>Process Validators</em> phase of the request processing
 * lifecycle, the submitted value will be converted to a typesafe object,
 * and, if validation succeeds, stored as a local value using
 * <code>setValue()</code>.
 * </p> 
 * <p>During the <em>Render Response</em> phase of the request processing
 * lifecycle, conversion for output occurs as for {@link UIOutput}.</p>
 *
 * <p>When the <code>validate()</code> method of this {@link UIInput}
 * detects that a value change has actually occurred, and that all validations
 * have been successfully passed, it will queue a
 * {@link ValueChangeEvent}.  Later on, the <code>broadcast()</code>
 * method will ensure that this event is broadcast to all interested
 * listeners.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Text</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIInput extends UIOutput {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.FacesMessage} to be created if
     * a conversion error occurs, and the {@link ConverterException}
     * does not provide a message.</p>
     */
    public static final String CONVERSION_MESSAGE_ID =
        "javax.faces.component.UIInput.CONVERSION";


    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.FacesMessage} to be created if
     * a required check fails.</p>
     */
    public static final String REQUIRED_MESSAGE_ID =
        "javax.faces.component.UIInput.REQUIRED";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIInput} instance with default property
     * values.</p>
     */
    public UIInput() {

        super();
        setRendererType("Text");

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The submittedValue value of this {@link UIInput} component.</p>
     */
    private Object submittedValue = null;


    /**
     * <p>Return the submittedValue value of this {@link UIInput} component.
     * This method should only be used by the <code>decode()</code> and
     * <code>validate()</code> method of this component, or
     *  its corresponding {@link Renderer}.</p>
     */
    public Object getSubmittedValue() {

        return (this.submittedValue);

    }


    /**
     * <p>Set the submittedValue value of this {@link UIInput} component.
     * This method should only be used by the <code>decode()</code> and
     * <code>validate()</code> method of this component, or
     *  its corresponding {@link Renderer}.</p>

     * @param submittedValue The new submitted value
     */
    public void setSubmittedValue(Object submittedValue) {

        this.submittedValue = submittedValue;

    }

    public void setValue(Object value) {
        super.setValue(value);
        // Mark the local value as set.
        setLocalValueSet(true);
    }

    /**
     * <p>The "localValueSet" state for this component. 
     */
    private boolean localValueSet;

    /**
     * Return the "local value set" state for this component.
     * Calls to <code>setValue()</code> automatically reset
     * this property to <code>true</code>.
     */
    public boolean isLocalValueSet() {
        return localValueSet;
    }

    /**
     * Sets the "local value set" state for this component.
     */
    public void setLocalValueSet(boolean localValueSet) {
        this.localValueSet = localValueSet;
    }


    /**
     * <p>The "required field" state for this component.</p>
     */
    private boolean required = false;
    private boolean requiredSet = false;


    /**
     * <p>Return the "required field" state for this component.</p>
     */
    public boolean isRequired() {

	if (this.requiredSet) {
	    return (this.required);
	}
	ValueBinding vb = getValueBinding("required");
	if (vb != null) {
	    return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
	} else {
	    return (this.required);
	}

    }


    /**
     * <p>Set the "required field" state for this component.</p>
     *
     * @param required The new "required field" state
     */
    public void setRequired(boolean required) {

        this.required = required;
	this.requiredSet = true;

    }


    private MethodBinding validatorBinding = null;


    /**
     * <p>Return a <code>MethodBinding</code> pointing at a
     * method that will be called during <em>Process Validations</em>
     * phase of the request processing lifecycle, to validate the current
     * value of this component.</p>
     */
    public MethodBinding getValidator() {

        return (this.validatorBinding);

    }


    /**
     * <p>Set a <code>MethodBinding</code> pointing at a
     * method that will be called during <em>Process Validations</em>
     * phase of the request processing lifecycle, to validate the current
     * value of this component.</p>
     *
     * <p>Any method referenced by such an expression must be public, with
     * a return type of <code>void</code>, and accept parameters of type
     * {@link FacesContext}, {@link UIComponent}, and <code>Object</code>.</p>
     *
     * @param validatorBinding The new <code>MethodBinding</code> instance
     */
    public void setValidator(MethodBinding validatorBinding) {

        this.validatorBinding = validatorBinding;

    }


   private MethodBinding valueChangeMethod = null;


    /**
     * <p>Return a <code>MethodBinding </code> instance 
     * method that will be called during <em>Process Validations</em>
     * phase of he request processing lifecycle, after any registered
     * {@link ValueChangeListener}s have been notified of a value change.</p>
     */
    public MethodBinding getValueChangeListener() {

        return (this.valueChangeMethod);

    }


    /**
     * <p>Set a <code>MethodBinding</code> instance  a
     * that will be called during <em>Process Validations</em>
     * phase of he request processing lifecycle, after any registered
     * {@link ValueChangeListener}s have been notified of a value change.</p>
     *
     * @param valueChangeMethod The new method binding instance 
     */
    public void setValueChangeListener(MethodBinding valueChangeMethod) {

        this.valueChangeMethod = valueChangeMethod;

    }


    // ----------------------------------------------------- UIComponent Methods
    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void decode(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Force validity back to "true"
        setValid(true);
        super.decode(context);
    }

    /**
     * <p>In addition to to the default {@link UIComponent#broadcast}
     * processing, pass the {@link ValueChangeEvent} being broadcast to the
     * method referenced by <code>valueChangeListener</code> (if any).</p>
     *
     * @param event {@link FacesEvent} to be broadcast
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @exception IllegalArgumentException if the implementation class
     *  of this {@link FacesEvent} is not supported by this component
     * @exception NullPointerException if <code>event</code> is
     * <code>null</code>
     */
    public void broadcast(FacesEvent event)
        throws AbortProcessingException {

        if (!(event instanceof ValueChangeEvent)) {
            throw new IllegalArgumentException();
        }
        // Perform standard superclass processing
        super.broadcast(event);

        // Notify the specified value change listener method (if any)
        MethodBinding valueChangeMethod = getValueChangeListener();
        if ((valueChangeMethod != null) &&
            event.getPhaseId().equals(PhaseId.PROCESS_VALIDATIONS)) {
            FacesContext context = getFacesContext();
            valueChangeMethod.invoke(context, new Object[] { event });
        }

    }


    /**
     * <p>Perform the following algorithm to update the model data
     * associated with this {@link UIInput}, if any, as appropriate.</p>
     * <ul>
     * <li>If the <code>valid</code> property of this component is
     *     <code>false</code>, take no further action.</li>
     * <li>If the <code>localValueSet</code> property of this component is
     *     <code>false</code>, take no further action.</li>
     * <li>If no {@link ValueBinding} for <code>value</code> exists,
     *     take no further action.</li>
     * <li>Call <code>setValue()</code> method of the {@link ValueBinding}
     *      to update the value that the {@link ValueBinding} points at.</li>
     * <li>If the <code>setValue()</code> method returns successfully:
     *     <ul>
     *     <li>Clear the local value of this {@link UIInput}.</li>
     *     <li>Set the <code>localValueSet</code> property of this
     *         {@link UIInput} to false.</li>
     *     </ul></li>
     * <li>If the <code>setValue()</code> method call fails:
     *     <ul>
     *     <li>Enqueue an error message by calling <code>addMessage()</code>
     *         on the specified {@link FacesContext} instance.</li>
     *     <li>Set the <code>valid</code> property of this {@link UIInput}
     *         to <code>false</code>.</li>
     *     </ul></li>
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (!isValid() || !isLocalValueSet()) {
            return;
        }

	ValueBinding vb = getValueBinding("value");
	if (vb != null) {
	    try {
		vb.setValue(context, getLocalValue());
		setValue(null);
		setLocalValueSet(false);
		return;
	    } catch (FacesException e) {
                FacesMessage message =
                    MessageFactory.getMessage(context, CONVERSION_MESSAGE_ID);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(getClientId(context), message);
		setValid(false);
	    } catch (IllegalArgumentException e) {
                FacesMessage message =
                    MessageFactory.getMessage(context, CONVERSION_MESSAGE_ID);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(getClientId(context), message);
		setValid(false);
	    } catch (Exception e) {
                FacesMessage message =
                    MessageFactory.getMessage(context, CONVERSION_MESSAGE_ID);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(getClientId(context), message);
		setValid(false);
	    }
	}
    }


    // ------------------------------------------------------ Validation Methods


    /**
     * <p>Perform the following algorithm to validate the local value of
     * this {@link UIInput}.</p>
     * <ul>
     * <li>Retrieve the submitted value with <code>getSubmittedValue()</code>.
     *   If this returns null, exit without further processing.  (This
     *   indicates that no value was submitted for this component.)</li>
     * <li> Convert the submitted value into a "local value" of the
     * appropriate data type, if necessary, according to the following
     * rules:
     * <ul>
     * <li>If a <code>Renderer</code> is present, call
     *     <code>getConvertedValue()</code> to convert the submitted
     *     value.</li>
     * <li>If no <code>Renderer</code> is present, and the submitted
     *     value is a String, locate a {@link Converter} as follows:
     *     <ul>
     *     <li>If <code>getConverter()</code> returns a non-null {@link Converter},
     *         use that instance.</li>
     *     <li>Otherwise, if a value binding for <code>value</code> exists,
     *         call <code>getType()</code> on it.  
     *         <ul>
     *         <li>If this call returns <code>null</code>, assume the output
     *             type is <code>String</code> and perform no conversion.</li>
     *         <li>Otherwise, call
     *             <code>Application.createConverter(Class)</code>
     *             to locate any registered {@link Converter} capable of
     *             converting data values of the specified type.</li>
     *       </ul>
     *     </li>
     *     </ul>
     * </li>
     * <li>If a {@link Converter} instance was located, call its
     *     <code>getAsObject()</code> method to perform the conversion.
     *     If conversion fails:
     *     <ul>
     *       <li>Enqueue an appropriate error message by calling the
     *         <code>addMessage()</code> method on the
     *         <code>FacesContext</code>.</li>
     *       <li>Set the <code>valid</code> property
     *       on this component to <code>false</code> </li>
     *     </ul></li>
     * <li>Otherwise, use the submitted value without any conversion</li>
     * </ul>
     * </li>
     * <li>If the <code>valid</code> property on this component is still
     *     <code>true</code>, and the <code>required</code> property is also
     *     true, ensure that the local value is not empty (where "empty" is
     *     defined as <code>null</code> or a zero-length String.  If the local
     *     value is empty:
     *     <ul>
     *     <li>Enqueue an appropriate error message by calling the
     *         <code>addMessage()</code> method on the <code>FacesContext</code>
     *         instance for the current request.</li>
     *     <li>Set the <code>valid</code> property on this component to
     *         <code>false</code>.</li>
     *     </ul></li>
     * <li>If the <code>valid</code> property on this component is still
     *     <code>true</code>, and the local value is not empty, call the
     *     <code>validate()</code> method of each {@link Validator}
     *     registered for this {@link UIInput}, followed by the method
     *     pointed at by the <code>validatorBinding</code> property (if any).
     *     If any of these validators or the method throws a
     *     {@link ValidatorException}, catch the exception, add
     *     its message (if any) to the {@link FacesContext}, and set
     *     the <code>valid</code> property of this component to false.</li>
     * <li>If the <code>valid</code> property of this component is still
     *     <code>true</code>, retrieve the previous value of the component
     *     (with <code>getValue()</code>), store the new local value using
     *     <code>setValue()</code>, and reset the submitted value to 
     *     null.  If the local value is different from
     *     the previous value of this component, fire a
     *     {@link ValueChangeEvent} to be broadcast to all interested
     *     listeners.</li>
     * </ul>
     *
     * <p>Application components implementing {@link UIInput} that wish to
     * perform validation with logic embedded in the component should perform
     * their own correctness checks, and then call the
     * <code>super.validate()</code> method to perform the standard
     * processing described above.</p>
     *
     * @param context The {@link FacesContext} for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is null
     */
    public void validate(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Submitted value == null means "the component was not submitted
        // at all";  validation should not continue
        Object submittedValue = getSubmittedValue();
        if (submittedValue == null) {
            return;
        }
            
        Renderer renderer = getRenderer(context);
        Object newValue = null;

        try {
            if (renderer != null) {
                newValue = renderer.getConvertedValue(context, this,
                                                      submittedValue);
            } else if (submittedValue instanceof String) {
                // If there's no Renderer, and we've got a String,
                // run it through the Converter (if any)
                Converter converter = getConverterWithType(context);
                if (converter != null) {
                    newValue = converter.getAsObject(context, this,
                                                     (String) submittedValue);
                }
                else {
                    newValue = submittedValue;
                }
            } else {
                newValue = submittedValue;
            }
            
        } catch (ConverterException ce) {
            addConversionErrorMessage(context, ce, submittedValue);
            setValid(false);
        }

	// If our value is valid, enforce the required property if present
	if (isValid() && isRequired() && isEmpty(newValue)) {
	    FacesMessage message =
		MessageFactory.getMessage(context, REQUIRED_MESSAGE_ID);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
	    context.addMessage(getClientId(context), message);
	    setValid(false);
	}

	// If our value is valid and not empty, call all validators
	if (isValid() && !isEmpty(newValue)) {
	    if (this.validators != null) {
		Iterator validators = this.validators.iterator();
		while (validators.hasNext()) {
		    Validator validator = (Validator) validators.next();
                    try { 
                        validator.validate(context, this, newValue);
                    }
                    catch (ValidatorException ve) {
                        // If the validator throws an exception, we're
                        // invalid, and we need to add a message
                        setValid(false);
                        FacesMessage message = ve.getFacesMessage();
                        if (message != null) {
			    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                            context.addMessage(getClientId(context), message);
                        }
                    }
		}
	    }
            if (validatorBinding != null) {
                try {
                    validatorBinding.invoke(context,
                              new Object[] { context, this, newValue});
                }
                catch (EvaluationException ee) {
                    if (ee.getCause() instanceof ValidatorException) {
                        ValidatorException ve =
                            (ValidatorException) ee.getCause();

                        // If the validator throws an exception, we're
                        // invalid, and we need to add a message
                        setValid(false);
                        FacesMessage message = ve.getFacesMessage();
                        if (message != null) {
			    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                            context.addMessage(getClientId(context), message);
                        }
                    } else {
                        // Otherwise, rethrow the EvaluationException
                        throw ee;
                    }
                }
            }
	}

	// If our value is valid, store the new value, erase the
        // "submitted" value, and emit a ValueChangeEvent if appropriate
	if (isValid()) {
	    Object previous = getValue();
            setValue(newValue);
            setSubmittedValue(null);
            if (compareValues(previous, newValue)) {
                queueEvent(new ValueChangeEvent(this, previous, newValue));
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

    private boolean isEmpty(Object value) {

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
    List validators = null;


    /**
     * <p>Add a {@link Validator} instance to the set associated with
     * this {@link UIInput}.</p>
     *
     * @param validator The {@link Validator} to add
     *
     * @exception NullPointerException if <code>validator</code>
     *  is null
     */
    public void addValidator(Validator validator) {

        if (validator == null) {
            throw new NullPointerException();
        }
        if (validators == null) {
            validators = new ArrayList();
        }
        validators.add(validator);

    }


    /**
     * <p>Return the set of registered {@link Validator}s for this
     * {@link UIInput} instance.  If there are no registered validators,
     * a zero-length array is returned.</p>
     */
    public Validator[] getValidators() {

        if (validators == null) {
            return (new Validator[0]);
        } else {
            return ((Validator[]) validators.toArray
                    (new Validator[validators.size()]));
        }

    }


    /**
     * <p>Remove a {@link Validator} instance from the set associated with
     * this {@link UIInput}, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param validator The {@link Validator} to remove
     */
    public void removeValidator(Validator validator) {

        if (validators != null) {
            validators.remove(validator);
        }

    }


    // ------------------------------------------------ Event Processing Methods


    /**
     * <p>Add a new {@link ValueChangeListener} to the set of listeners
     * interested in being notified when {@link ValueChangeEvent}s occur.</p>
     *
     * @param listener The {@link ValueChangeListener} to be added
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void addValueChangeListener(ValueChangeListener listener) {

        addFacesListener(listener);

    }


    /**
     * <p>Return the set of registered {@link ValueChangeListener}s for this
     * {@link UIInput} instance.  If there are no registered listeners,
     * a zero-length array is returned.</p>
     */
    public ValueChangeListener[] getValueChangeListeners() {

        ValueChangeListener vcl[] = (ValueChangeListener [])
	    getFacesListeners(ValueChangeListener.class);
        return (vcl);

    }


    /**
     * <p>Remove an existing {@link ValueChangeListener} (if any) from the
     * set of listeners interested in being notified when
     * {@link ValueChangeEvent}s occur.</p>
     *
     * @param listener The {@link ValueChangeListener} to be removed
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void removeValueChangeListener(ValueChangeListener listener) {

        removeFacesListener(listener);

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = localValueSet ? Boolean.TRUE : Boolean.FALSE;
        values[2] = required ? Boolean.TRUE : Boolean.FALSE;
	values[3] = requiredSet ? Boolean.TRUE : Boolean.FALSE;
        values[4] = saveAttachedState(context, validators);
        values[5] = saveAttachedState(context, validatorBinding);
        values[6] = saveAttachedState(context, valueChangeMethod);
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        localValueSet = ((Boolean) values[1]).booleanValue();
        required = ((Boolean) values[2]).booleanValue();
        requiredSet = ((Boolean) values[3]).booleanValue();
	List restoredValidators = null;
	Iterator iter = null;

	if (null != (restoredValidators = (List) 
		     restoreAttachedState(context, values[4]))) {
	    // if there were some validators registered prior to this
	    // method being invoked, merge them with the list to be
	    // restored.
	    if (null != validators) {
		iter = restoredValidators.iterator();
		while (iter.hasNext()) {
		    validators.add(iter.next());
		}
	    }
	    else {
		validators = restoredValidators;
	    }
	}

        validatorBinding = (MethodBinding) restoreAttachedState(context,
								values[5]);
        valueChangeMethod = (MethodBinding) restoreAttachedState(context,
								 values[6]);


    }

    private Converter getConverterWithType(FacesContext context) {
        Converter converter = getConverter();
        if (converter != null) {
            return converter;
        }

        ValueBinding valueBinding = getValueBinding("value");
        if (valueBinding == null) {
            return null;
        }

        Class converterType = valueBinding.getType(context);
        // if converterType is null, String, or Object, assume
        // no conversion is needed
        if (converterType == null || 
            converterType == String.class ||
            converterType == Object.class) {
            return null;
        }

        // if getType returns a type for which we support a default
        // conversion, acquire an appropriate converter instance.
        try {
            Application application = context.getApplication();
            return application.createConverter(converterType);
        } catch (Exception e) {
            return (null);
        }
    }

    private void addConversionErrorMessage(FacesContext context, 
            ConverterException ce, Object value) {
        FacesMessage message = ce.getFacesMessage();
        if (message == null) {
            message = MessageFactory.getMessage(context,
                                                CONVERSION_MESSAGE_ID);
            if (message.getDetail() == null) {
                message.setDetail(ce.getMessage());
            }
        }

        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(getClientId(context), message);
    }

}
