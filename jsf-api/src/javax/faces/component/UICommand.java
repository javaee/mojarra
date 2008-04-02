/*
 * $Id: UICommand.java,v 1.72 2005/05/05 20:51:02 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
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
 * <p>Listeners will be invoked in the following order:
 * <ol>
 *  <li>{@link ActionListener}s, in the order in which they were registered.
 *  <li>The "actionListener" {@link MethodExpression} (which will cover
 *  the "actionListener" that was set as a <code>MethodBinding</code>).
 *  <li>The default {@link ActionListener}, retrieved from the
 *      {@link Application} - and therefore, any attached "action"
 *      {@link MethodExpression}.
 * </ol>
 * </p>
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Button</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UICommand extends UIComponentBase
    implements ActionSource2 {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Command";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Command";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UICommand} instance with default property
     * values.</p>
     */
    public UICommand() {

        super();
        setRendererType("javax.faces.Button");

    }


    // ------------------------------------------------------ Instance Variables


    private Object value = null;


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    // ------------------------------------------------- ActionSource/ActionSource2 Properties


    /**
     * {@inheritDoc}
     * @deprecated This has been replaced by {@link #getActionExpression}.
     */
    public MethodBinding getAction() {
	MethodBinding result = null;
	MethodExpression me = null;

	if (null != (me = getActionExpression())) {
	    // if the MethodExpression is an instance of our private
	    // wrapper class.
	    if (me.getClass() == MethodExpressionMethodBindingAdapter.class) {
		result = ((MethodExpressionMethodBindingAdapter)me).getWrapped();
	    }
	    else {
		// otherwise, this is a real MethodExpression.  Wrap it
		// in a MethodBinding.
		result = new MethodBindingMethodExpressionAdapter(me);
	    }
	}
	return result;
	    
    }

    /**
     * {@inheritDoc}
     * @deprecated This has been replaced by {@link #setActionExpression(javax.el.MethodExpression)}.
     */
    public void setAction(MethodBinding action) {
	MethodExpressionMethodBindingAdapter adapter = null;
	if (null != action) {
	    adapter = new MethodExpressionMethodBindingAdapter(action);
	    setActionExpression(adapter);
	}
	else {
	    setActionExpression(null);
	}
    }
    
    /**
     * {@inheritDoc}
     * @deprecated Use {@link #getActionListeners} instead.
     */
    public MethodBinding getActionListener() {
	MethodBinding result = null;

	ActionListener [] curListeners = getActionListeners();
	// go through our lisetners list and find the one and only
	// MethodBindingActionListener instance, if present.
	if (null != curListeners) {
	    for (int i = 0; i < curListeners.length; i++) {
		// We are guaranteed to have at most one instance of
		// MethodBindingActionListener in the curListeners list.
		if (MethodBindingActionListener.class ==
		    curListeners[i].getClass()) {
		    result = ((MethodBindingActionListener)curListeners[i]).
			getWrapped();
		    break;
		}
	    }
	}
	return result;
    }

    /**
     * {@inheritDoc}
     * @deprecated This has been replaced by {@link #addActionListener(javax.faces.event.ActionListener)}.
     */
    public void setActionListener(MethodBinding actionListener) {

	ActionListener [] curListeners = getActionListeners();
	// see if we need to null-out, or replace an existing listener
	if (null != curListeners) {
	    for (int i = 0; i < curListeners.length; i++) {
		// if we want to remove the actionListener
		if (null == actionListener) {
		    // We are guaranteed to have at most one instance of
		    // MethodBindingActionListener in the curListeners
		    // list.
		    if (MethodBindingActionListener.class ==
			curListeners[i].getClass()) {
			removeFacesListener(curListeners[i]);
			return;
		    }
		}
		// if we want to replace the actionListener
		else if (actionListener == curListeners[i]) {
		    removeFacesListener(curListeners[i]);
		    break;
		}
	    }
	}
	addActionListener(new MethodBindingActionListener(actionListener));
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
	ValueExpression ve = getValueExpression("immediate");
	if (ve != null) {
	    try {
		return (Boolean.TRUE.equals(ve.getValue(getFacesContext().getELContext())));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	} else {
	    return (this.immediate);
	}

    }


    public void setImmediate(boolean immediate) {

	// if the immediate value is changing.
	if (immediate != this.immediate) {
	    this.immediate = immediate;
	}
	this.immediateSet = true;

    }



    /**
     * <p>Returns the <code>value</code> property of the
     * <code>UICommand</code>. This is most often rendered as a label.</p>
     */
    public Object getValue() {

	if (this.value != null) {
	    return (this.value);
	}
	ValueExpression ve = getValueExpression("value");
	if (ve != null) {
	    try {
		return (ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }

	} else {
	    return (null);
	}

    }


    /**
     * <p>Sets the <code>value</code> property of the <code>UICommand</code>.
     * This is most often rendered as a label.</p>
     *
     * @param value the new value
     */
    public void setValue(Object value) {

        this.value = value;

    }


    // ---------------------------------------------------- ActionSource / ActionSource2 Methods

    
    /**
     * <p>The {@link MethodExpression} that, when invoked, yields the
     * literal outcome value.</p>
     */
    private MethodExpression actionExpression = null;
    
    public MethodExpression getActionExpression() {
        return actionExpression;
    }
    
    public void setActionExpression(MethodExpression actionExpression) {
        this.actionExpression = actionExpression;    
    }
    
    /** 
     * @exception NullPointerException {@inheritDoc}
     */ 
    public void addActionListener(ActionListener listener) {

        addFacesListener(listener);

    }
    
    public ActionListener[] getActionListeners() {

        ActionListener al[] = (ActionListener [])
	    getFacesListeners(ActionListener.class);
        return (al);

    }



    /**
     * @exception NullPointerException {@inheritDoc}
     */ 
    public void removeActionListener(ActionListener listener) {

        removeFacesListener(listener);

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = saveAttachedState(context, actionExpression);
        values[2] = immediate ? Boolean.TRUE : Boolean.FALSE;
        values[3] = immediateSet ? Boolean.TRUE : Boolean.FALSE;
        values[4] = value;
        
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        actionExpression = 
	    (MethodExpression) restoreAttachedState(context, values[1]);
        immediate = ((Boolean) values[2]).booleanValue();
        immediateSet = ((Boolean) values[3]).booleanValue();
        value = values[4];
        
        //PENDING add restoring of "ActionExpression" MethodExpression
    }


    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>In addition to to the default {@link UIComponent#broadcast}
     * processing, pass the {@link ActionEvent} being broadcast to the
     * method referenced by <code>actionListener</code> (if any),
     * and to the default {@link ActionListener} registered on the
     * {@link javax.faces.application.Application}.</p>
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
    public void broadcast(FacesEvent event) throws AbortProcessingException {

        // Perform standard superclass processing (including calling our
        // ActionListeners)
        super.broadcast(event);

        if (event instanceof ActionEvent) {
            FacesContext context = getFacesContext();

	    // no need to call our method based ActionListener, since it
	    // is handled by the superclass processing.

            // Invoke the default ActionListener
            ActionListener listener =
              context.getApplication().getActionListener();
            if (listener != null) {
                listener.processAction((ActionEvent) event);
            }
        }
    }

    /**
     * <p>Intercept <code>queueEvent</code> and, for {@link ActionEvent}s,
     * mark the phaseId for the event to be
     * <code>PhaseId.APPLY_REQUEST_VALUES</code> if the
     * <code>immediate</code> flag is true,
     * <code>PhaseId.INVOKE_APPLICATION</code> otherwise.</p>
     */

    public void queueEvent(FacesEvent e) {
	if (e instanceof ActionEvent) {
	    if (isImmediate()) {
		e.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
	    }
	    else {
		e.setPhaseId(PhaseId.INVOKE_APPLICATION);
	    }
	}
	super.queueEvent(e);
    }
}
