/*
 * $Id: UICommand.java,v 1.53 2003/11/07 18:55:28 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.lang.reflect.InvocationTargetException;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


/**
 * <p><strong>UICommand</strong> is a {@link UIComponent} that represents
 * a user interface component which, when activated by the user, triggers
 * an application specific "command" or "action".  Such a component is
 * typically rendered as a push button, a menu item, or a hyperlink.</p>
 *
 * <p>When the <code>decode()</code> method of this {@link UICommand}, or
 * its corresponding {@link Renderer}, detects that this control has been
 * activated, it will queue an {@link ActionEvent}.
 * Later on, the <code>broadcast()</code> method will ensure that this
 * event is broadcast to all interested listeners.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Button</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UICommand extends UIComponentBase
    implements ActionSource, ValueHolder {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UICommand} instance with default property
     * values.</p>
     */
    public UICommand() {

        super();
        setRendererType("Button");
        // add the default action listener
        FacesContext context = FacesContext.getCurrentInstance();
	addDefaultActionListener(context);
    }


    // ------------------------------------------------------ Instance Variables


    private Object value = null;
    private String valueRef = null;


    // ------------------------------------------------- ActionSource Properties


    /**
     * <p>The literal outcome value.</p>
     */
    private String action = null;


    public String getAction() {

	if (this.action != null) {
	    return (this.action);
	}
	ValueBinding vb = getValueBinding("action");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}

    }


    public void setAction(String action) {

        this.action = action;

    }


    /**
     * <p>The action listener reference.</p>
     */
    private String actionListenerRef = null;


    public String getActionListenerRef() {

        return (this.actionListenerRef);

    }


    public void setActionListenerRef(String actionListenerRef) {

        this.actionListenerRef = actionListenerRef;

    }


    /**
     * <p>The action reference.</p>
     */
    private String actionRef = null;


    public String getActionRef() {

        return (this.actionRef);

    }


    public void setActionRef(String actionRef) {

        this.actionRef = actionRef;

    }


    /**
     * <p>The immediate flag.</p>
     */
    private boolean immediate = false;
    private boolean immediateSet = false;


    public boolean isImmediate() {

	if (this.immediateSet) {
	    return (this.immediate);
	}
	ValueBinding vb = getValueBinding("immediate");
	if (vb != null) {
	    Boolean value = (Boolean) vb.getValue(getFacesContext());
	    return (value.booleanValue());
	} else {
	    return (this.immediate);
	}

    }


    public void setImmediate(boolean immediate) {

	// if the immediate value is changing.
	if (immediate != this.immediate) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    // remove the current default action listener
	    removeDefaultActionListener(context);
	    this.immediate = immediate;
	    addDefaultActionListener(context);
	}
	this.immediateSet = true;

    }


    // -------------------------------------------------- ValueHolder Properties


    public Object getValue() {

        return (this.value);

    }


    public void setValue(Object value) {

        this.value = value;

    }


    public String getValueRef() {

        return (this.valueRef);

    }


    public void setValueRef(String valueRef) {

        this.valueRef = valueRef;

    }


    // ---------------------------------------------------- ActionSource Methods


    /** 
     * @exception NullPointerException {@inheritDoc}
     */ 
    public void addActionListener(ActionListener listener) {

        addFacesListener(listener);

    }


    public ActionListener[] getActionListeners() {

        FacesListener fl[] = getFacesListeners(ActionListener.class);
        ActionListener al[] = new ActionListener[fl.length];
        for (int i = 0; i < fl.length; i++) {
            al[i] = (ActionListener) fl[i];
        }
        return (al);

    }



    /**
     * @exception NullPointerException {@inheritDoc}
     */ 
    public void removeActionListener(ActionListener listener) {

        removeFacesListener(listener);

    }


    // ----------------------------------------------------- ValueHolder Methods

    /**
     * @exception EvaluationException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     */ 
    public Object currentValue(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = getValue();
        if (value != null) {
            return (value);
        }
        String valueRef = getValueRef();
        if (valueRef != null) {
            Application application = context.getApplication();
            ValueBinding binding = application.getValueBinding(valueRef);
            return (binding.getValue(context));
        }
        return (null);

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        removeDefaultActionListener(context);

        Object values[] = new Object[8];
        values[0] = super.saveState(context);
        values[1] = action;
        values[2] = actionListenerRef;
        values[3] = actionRef;
        values[4] = immediate ? Boolean.TRUE : Boolean.FALSE;
        values[5] = immediateSet ? Boolean.TRUE : Boolean.FALSE;
        values[6] = value;
        values[7] = valueRef;

        addDefaultActionListener(context);
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {
        removeDefaultActionListener(context);

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        action = (String) values[1];
        actionListenerRef = (String) values[2];
        actionRef = (String) values[3];
        immediate = ((Boolean) values[4]).booleanValue();
        immediateSet = ((Boolean) values[5]).booleanValue();
        value = values[6];
        valueRef = (String) values[7];

        addDefaultActionListener(context);

    }


    // ----------------------------------------------------- UIComponent Methods


    // Parameter signature for "actionListenerRef" method
    private static Class signature[] = { ActionEvent.class };


    /**
     * <p>In addition to to the default {@link UIComponent#broadcast}
     * processing, pass the {@link ActionEvent} being broadcast to the
     * method referenced by <code>actionListenerRef</code> (if any).</p>
     *
     * @param event {@link FacesEvent} to be broadcast
     * @param phaseId {@link PhaseId} of the current phase of the
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

        // Perform standard superclass processing
        boolean returnValue = super.broadcast(event, phaseId);

        // Notify the specified action listener method (if any)
        String actionListenerRef = getActionListenerRef();
        if (actionListenerRef != null) {
            if ((isImmediate() &&
                 phaseId.equals(PhaseId.APPLY_REQUEST_VALUES)) ||
                (!isImmediate() &&
                 phaseId.equals(PhaseId.INVOKE_APPLICATION))) {
                FacesContext context = FacesContext.getCurrentInstance();
                MethodBinding mb =
                    context.getApplication().getMethodBinding
                    (actionListenerRef, signature);
                try {
                    mb.invoke(context, new Object[] { event });
                } catch (InvocationTargetException e) {
                    throw new FacesException(e.getTargetException());
                }
            }
        }

        // Return the flag indicating future interest
        return (returnValue);

    }


    // --------------------------------------------------------- Private Methods


    // Add the default action listener
    private void addDefaultActionListener(FacesContext context) {

        ActionListener listener =
            context.getApplication().getActionListener();
        if (immediate) {
            addActionListener(new WrapperActionListener(listener));
        } else {
            addActionListener(listener);
        }

    }


    // Remove the default action listener
    private void removeDefaultActionListener(FacesContext context) {

        if (immediate) {
            if (listeners == null) {
                return;
            }
            List list = listeners[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()];
            if (list == null) {
                return;
            }
            Iterator items = list.iterator();
            while (items.hasNext()) {
                Object item = items.next();
                if (item instanceof WrapperActionListener) {
                    removeActionListener((ActionListener) item);
                    return;
                }
            }
        } else {
            removeActionListener(context.getApplication().getActionListener());
        }

    }



    // ------------------------------------------------ Private Wrapper Listener


    // Wrapper for the default ActionListener that overrides getPhaseId()
    // to return PhaseId.APPLY_REQUEST_VALUES, for "immediate" commands
    private class WrapperActionListener implements ActionListener {

        public WrapperActionListener(ActionListener wrapped) {
            this.wrapped = wrapped;
        }

        private ActionListener wrapped;

        public PhaseId getPhaseId() {
            return (PhaseId.APPLY_REQUEST_VALUES);
        }

        public void processAction(ActionEvent event) {
            wrapped.processAction(event);
        }

    }


}
