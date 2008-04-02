/*
 * $Id: UIInput.java,v 1.17 2003/03/13 01:11:58 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;

/**
 * <p><strong>UIInput</strong> is a {@link UIComponent} that represents
 * a component that both displays output to the user (like
 * {@link UIOutput} components do) and includes request parameters on the
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
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>Text</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIInput extends UIOutput {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIInput";


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UIInput} instance with default property
     * values.</p>
     */
    public UIInput() {

        super();
        setRendererType("Text");

    }


    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>The previous value of this {@link UIInput} component.</p>
     */
    private Object previous = null;


    /**
     * <p>Return the previous value of this {@link UIInput} component.
     * This method should only be utilized by the <code>decode()</code>
     * method of this component, or its corresponding
     * {@link javax.faces.render.Renderer}.</p>
     */
    public Object getPrevious() {

        return (this.previous);

    }


    /**
     * <p>Set the previous value of this {@link UIInput} component.
     * This method should only be utilized by the <code>decode()</code>
     * method of this component, or its corresponding
     * {@link javax.faces.render.Renderer}.</p>
     *
     * @param previous The new previous value
     */
    public void setPrevious(Object previous) {

        this.previous = previous;

    }


    // ---------------------------------------------------- UIComponent Methods


    /**
     * <p>Perform the following algorithm to update the model data
     * associated with this {@link UIInput}, if any, as appropriate.</p>
     * <ul>
     * <li>If the <code>valid</code> property of this component is
     *     <code>false</code>, take no further action.</li>
     * <li>If the <code>valueRef</code> property of this component
     *     is <code>null</code>, take no further action.</li>
     * <li>Retrieve the {@link Application} instance for this web application
     *     from {@link ApplicationFactory}.</li>
     * <li>Ask it for a {@link ValueBinding} for the <code>valueRef</code>
     *     expression.</li>
     * <li>Use the <code>setValue()</code> method of the
     *     {@link ValueBinding} to update the value that the
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
            ApplicationFactory factory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            Application application = factory.getApplication();
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


    /**
     * <p>Perform validations and, if validation is successful, and the
     * local value is different from the previous value, queue
     * a {@link ValueChangedEvent} to be processed later.</p>
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

        // Save and reset the previous value for this component
        Object previous = getPrevious();
        setPrevious(null);

        // Determine whether a value change has actually occurred
        Object value = getValue();
        boolean changed;
        if (previous != null && value != null &&
            previous.equals(value)) {
            // no change has occurred
            changed = false;
        } else if (previous == null) {
            // if the value is going from null to non-null, a change 
            // has occurred
            changed = (value != null);
        } else /* if (previous != null) */ {
            // if the value is going from non-null
            if (value == null) {
                // to null, no change has occurred.
                changed = false;
            // if previous and current values are not null, compare values.
            } else if (compareValues(previous, value)) {
                // value change has occurred
                changed = true;
            } else {
                // value change has not occurred
                changed = false;
            }
        }

        // Queue a ValueChangedEvent if appropriate
        if (changed) {
            fireValueChangedEvent(context, previous, value);
        }

    }
    

    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Return <code>true</code> if the new value is different from the
     * previous value.</p>
     *
     * @param previous old value of this component
     * @param value new value of this component
     */
    protected boolean compareValues(Object previous, Object value) {

        return (!(previous.equals(value)));

    }


    // ----------------------------------------------- Event Processing Methods


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
     * <p>Queue an {@link ValueChangedEvent} for processing during the next
     * event processing cycle.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param oldValue The original value of this component
     * @param newValue The new value of this component
     */
    protected void fireValueChangedEvent(FacesContext context,
                                         Object oldValue,
                                         Object newValue) {

        context.addFacesEvent
            (new ValueChangedEvent(this, oldValue, newValue));

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
