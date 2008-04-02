/*
 * $Id: EditableValueHolder.java,v 1.2 2004/01/09 01:50:24 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.validator.Validator;
import javax.faces.el.MethodBinding;
import javax.faces.render.Renderer;


/**
 * <p><strong>EditableValueHolder</strong> is an extension of ValueHolder
 * that describes additional features supported by editable components,
 * including {@link ValueChangeEvent}s and {@link Validator}s.
 */

public interface EditableValueHolder extends ValueHolder {

    /**
     * <p>Return the submittedValue value of this component.
     * This method should only be used by the <code>decode()</code> and
     * <code>validate()</code> method of this component, or
     *  its corresponding {@link Renderer}.</p>
     */
    public Object getSubmittedValue();

    /**
     * <p>Set the submittedValue value of this component.
     * This method should only be used by the <code>decode()</code> and
     * <code>validate()</code> method of this component, or
     *  its corresponding {@link Renderer}.</p>

     * @param submittedValue The new submitted value
     */
    public void setSubmittedValue(Object submittedValue);

    /**
     * Return the "local value set" state for this component.
     * Calls to <code>setValue()</code> automatically reset
     * this property to <code>true</code>.
     */
    public boolean isLocalValueSet();

    /**
     * Sets the "local value set" state for this component.
     */
    public void setLocalValueSet(boolean localValueSet);

    /**
     * <p>Return the "required field" state for this component.</p>
     */
    public boolean isRequired();

    /**
     * <p>Set the "required field" state for this component.</p>
     *
     * @param required The new "required field" state
     */
    public void setRequired(boolean required);

    /**
     * <p>Return a <code>MethodBinding</code> pointing at a
     * method that will be called during <em>Process Validations</em>
     * phase of the request processing lifecycle, to validate the current
     * value of this component.</p>
     */
    public MethodBinding getValidator();

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
    public void setValidator(MethodBinding validatorBinding);

    /**
     * <p>Return a <code>MethodBinding </code> instance 
     * method that will be called during <em>Process Validations</em>
     * phase of he request processing lifecycle, after any registered
     * {@link ValueChangeListener}s have been notified of a value change.</p>
     */
    public MethodBinding getValueChangeListener();

    /**
     * <p>Set a <code>MethodBinding</code> instance  a
     * that will be called during <em>Process Validations</em>
     * phase of he request processing lifecycle, after any registered
     * {@link ValueChangeListener}s have been notified of a value change.</p>
     *
     * @param valueChangeMethod The new method binding instance 
     */
    public void setValueChangeListener(MethodBinding valueChangeMethod);

    /**
     * <p>Add a {@link Validator} instance to the set associated with
     * this component.</p>
     *
     * @param validator The {@link Validator} to add
     *
     * @exception NullPointerException if <code>validator</code>
     *  is null
     */
    public void addValidator(Validator validator);

    /**
     * <p>Return the set of registered {@link Validator}s for this
     * component instance.  If there are no registered validators,
     * a zero-length array is returned.</p>
     */
    public Validator[] getValidators();

    /**
     * <p>Remove a {@link Validator} instance from the set associated with
     * this component, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param validator The {@link Validator} to remove
     */
    public void removeValidator(Validator validator);

    /**
     * <p>Add a new {@link ValueChangeListener} to the set of listeners
     * interested in being notified when {@link ValueChangeEvent}s occur.</p>
     *
     * @param listener The {@link ValueChangeListener} to be added
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void addValueChangeListener(ValueChangeListener listener);

    /**
     * <p>Return the set of registered {@link ValueChangeListener}s for this
     * component instance.  If there are no registered listeners,
     * a zero-length array is returned.</p>
     */
    public ValueChangeListener[] getValueChangeListeners();

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
    public void removeValueChangeListener(ValueChangeListener listener);
}
