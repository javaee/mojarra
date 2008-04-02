/*
 * $Id: EditableValueHolder.java,v 1.12 2005/12/05 16:42:42 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.component;


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
     * <p>Return the submittedValue value of this component.  This
     * method should only be used by the <code>encodeBegin()</code>
     * and/or <code>encodeEnd()</code> methods of this component, or its
     * corresponding {@link Renderer}.</p>
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
     * <p>Return a flag indicating whether the local value of this component
     * is valid (no conversion error has occurred).</p>
     */
    public boolean isValid();


    /**
     * <p>Set a flag indicating whether the local value of this component
     * is valid (no conversion error has occurred).</p>
     *
     * @param valid The new valid flag
     */
    public void setValid(boolean valid);


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
     * <p>Return the "immediate" state for this component.</p>
     */
    public boolean isImmediate();

    /**
     * <p>Set the "immediate" state for this component.  When
     * set to true, the component's value will be converted
     * and validated immediately in the <em>Apply Request Values</em>
     * phase, and {@link ValueChangeEvent}s will be delivered
     * in that phase as well.  The default value for this
     * property must be <code>false</code>.</p>
     *
     * @param immediate The new "immediate" state
     */
    public void setImmediate(boolean immediate);

    /**
     * <p>If {@link #setValidator} was not previously called for this
     * instance, this method must return <code>null</code>.  If it was
     * called, this method must return the exact
     * <code>MethodBinding</code> instance that was passed to {@link
     * #setValidator}.</p>
     *
     * <p>This method will be called during the <em>Process
     * Validations</em> or <em>Apply Request Values</em> phases
     * (depending on the value of the <code>immediate</code>
     * property). </p>
     *
     * @deprecated {@link #getValidators} should be used instead.
     */
    public MethodBinding getValidator();

    /**
     * <p>Wrap the argument <code>validatorBinding</code> in an
     * implementation of {@link javax.faces.validator.Validator} and
     * store it in the internal data structure that backs the {@link
     * #getValidators} method, taking care to over-write any instance
     * that was stored by a previous call to <code>setValidator</code>.
     * </p>
     *
     * <p>The argument method will be called during the <em>Process
     * Validations</em> or <em>Apply Request Values</em> phases
     * (depending on the value of the <code>immediate</code>
     * property). </p>
     *
     * <p>Any method referenced by such an expression must be public,
     * with a return type of <code>void</code>, and accept parameters of
     * type {@link javax.faces.context.FacesContext}, {@link
     * UIComponent}, and <code>Object</code>.</p>
     *
     * @param validatorBinding The new <code>MethodBinding</code> instance
     * 
     * @deprecated Use {@link #addValidator} instead, obtaining the
     * argument {@link Validator} by creating an instance of {@link
     * javax.faces.validator.MethodExpressionValidator}.
     */
    public void setValidator(MethodBinding validatorBinding);

    /**
     * <p>If {@link #setValueChangeListener} was not previously called
     * for this instance, this method must return <code>null</code>.  If
     * it was called, this method must return the exact
     * <code>MethodBinding</code> instance that was passed to {@link
     * #setValueChangeListener}.</p>
     *
     * @deprecated Use {@link #getValueChangeListeners} instead.
     */
    public MethodBinding getValueChangeListener();

    /**
     * <p>Wrap the argument <code>valueChangeMethod</code> in an
     * implementation of {@link ValueChangeListener}
     * and store it in the internal data structure that backs the {@link
     * #getValueChangeListeners} method, taking care to over-write any
     * instance that was stored by a previous call to
     * <code>setValueChangeListener</code>.</p>
     *
     * <p>This argument method will be called during the <em>Process
     * Validations</em> or <em>Apply Request Values</em> phases
     * (depending on the value of the <code>immediate</code>
     * property). </p>
     *
     * <p>Any method referenced by such an expression must be public,
     * with a return type of <code>void</code>, and accept a parameter
     * of type {@link javax.faces.event.ValueChangeEvent}.</p>
     *
     * @param valueChangeMethod The new method binding instance 
     *
     * @deprecated Use {@link #addValueChangeListener} instead, obtaining the
     * argument {@link ValueChangeListener} by creating an instance of {@link
     * javax.faces.event.MethodExpressionValueChangeListener}.
     */
    public void setValueChangeListener(MethodBinding valueChangeMethod);

    /**
     * <p>Add a {@link Validator} instance to the set associated with
     * this component.</p>
     *
     * @param validator The {@link Validator} to add
     *
     * @throws NullPointerException if <code>validator</code>
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
     * @throws NullPointerException if <code>listener</code>
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
     * @throws NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void removeValueChangeListener(ValueChangeListener listener);
}
