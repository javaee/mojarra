/*
 * $Id: UIInput.java,v 1.26 2003/07/26 17:54:37 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import javax.faces.validator.Validator;


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
 * <p>When the <code>validate()</code> method of this {@link UIInput}
 * detects that a value change has actually occurred, and that all validations
 * have been successfully passed, it will queue a
 * {@link ValueChangedEvent}.  Later on, the <code>broadcast()</code>
 * method will ensure that this event is broadcast to all interested
 * listeners.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Text</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public interface UIInput extends UIOutput {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.Message} to be created if
     * this validation fails.</p>
     */
    public static final String REQUIRED_MESSAGE_ID =
        "javax.faces.component.UIInput.REQUIRED";



    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the previous value of this {@link UIInput} component.
     * This method should only be utilized by the <code>decode()</code>
     * method of this component, or its corresponding
     * {@link javax.faces.render.Renderer}.</p>
     */
    public Object getPrevious();


    /**
     * <p>Set the previous value of this {@link UIInput} component.
     * This method should only be utilized by the <code>decode()</code>
     * method of this component, or its corresponding
     * {@link javax.faces.render.Renderer}.</p>
     *
     * @param previous The new previous value
     */
    public void setPrevious(Object previous);


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


    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>Perform the following algorithm to update the model data
     * associated with this {@link UIInput}, if any, as appropriate.</p>
     * <ul>
     * <li>If the <code>valid</code> property of this component is
     *     <code>false</code>, take no further action.</li>
     * <li>If the <code>valueRef</code> property of this component
     *     is <code>null</code>, take no further action.</li>
     * <li>Retrieve the {@link javax.faces.application.Application} instance
     *     for this web application.</ul>
     * <li>Ask it for a {@link javax.faces.el.ValueBinding} for the
     *     <code>valueRef</code> expression.</li>
     * <li>Use the <code>setValue()</code> method of the
     *     {@link javax.faces.el.ValueBinding} to update the value that the
     *     value reference expression points at.</li>
     * <li>If the <code>setValue()</code> method returns successfully:
     *     <ul>
     *     <li>Clear the local value of this {@link UIInput}.</li>
     *     <li>Set the <code>valid</code> property of this {@link UIInput}
     *         to <code>true</code>.</li>
     *     </ul></li>
     * <li>If the <code>setValue()</code> method call fails:
     *     <ul>
     *     <li>Enqueue error messages by calling <code>addMessage()</code>
     *         on the specified {@link FacesContext} instance.</li>
     *     <li>Set the <code>valid</code> property of this {@link UIInput}
     *         to <code>false</code>.</li>
     *     </ul></li>
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IllegalArgumentException if the <code>valueRef</code>
     *  property has invalid syntax for an expression
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void updateModel(FacesContext context);


    // ------------------------------------------------------ Validation Methods


    /**
     * <p>Return the valid state of this {@link UIInput} component.</p>
     */
    public boolean isValid();


    /**
     * <p>Set the valid state of this {@link UIInput} component.</p>
     *
     * @param valid The new valid state
     */
    public void setValid(boolean valid);


    /**
     * <p>Perform the following algorithm to validate the local value of
     * this {@link UIInput}.</p>
     * <ul>
     * <li>Save the current local value (if any) in the <code>previous</code>
     *     property.</li>
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
     *     registered for this {@link UIInput}.</li>
     * <li>If the <code>valid</code> property of this component is still
     *     <code>true</code>, and if the local value is different from
     *     the previous value of this component, fire a
     *     {@link ValueChangedEvent} to be broadcast to all interested
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
    public void validate(FacesContext context);


    /**
     * <p>Add a {@link Validator} instance to the set associated with
     * this {@link UIInput}.</p>
     *
     * @param validator The {@link Validator} to add
     *
     * @exception NullPointerException if <code>validator</code>
     *  is null
     */
    public void addValidator(Validator validator);


    /**
     * <p>Remove a {@link Validator} instance from the set associated with
     * this {@link UIInput}, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param validator The {@link Validator} to remove
     */
    public void removeValidator(Validator validator);


    // ------------------------------------------------ Event Processing Methods


    /**
     * <p>Add a new {@link ValueChangedListener} to the set of listeners
     * interested in being notified when {@link ValueChangedEvent}s occur.</p>
     *
     * @param listener The {@link ValueChangedListener} to be added
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void addValueChangedListener(ValueChangedListener listener);


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
        throws AbortProcessingException;


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
    public void removeValueChangedListener(ValueChangedListener listener);


}
