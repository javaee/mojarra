/*
 * $Id: UICommand.java,v 1.67 2004/01/29 03:45:48 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


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
 * <p>Listeners will be invoked in the following order:
 * <ol>
 *  <li>{@link ActionListener}s, in the order in which they were registered.
 *  <li>The "actionListener" {@link MethodBinding}
 *  <li>The default {@link ActionListener}, retrieved from the
 *      {@link Application} - and therefore, any attached "action"
 *      {@link MethodBinding}.
 * </ol>
 * </p>
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Button</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UICommand extends UIComponentBase
    implements ActionSource {


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


    // ------------------------------------------------- ActionSource Properties


    /**
     * <p>The {@link MethodBinding} that, when invoked, yields the
     * literal outcome value.</p>
     */
    private MethodBinding action = null;


    public MethodBinding getAction() {
	return action;
    }


    public void setAction(MethodBinding action) {
        this.action = action;
    }


    /**
     * <p>The action listener {@link MethodBinding}.</p>
     */
    private MethodBinding actionListener = null;


    public MethodBinding getActionListener() {

        return (this.actionListener);

    }


    public void setActionListener(MethodBinding actionListener) {

        this.actionListener = actionListener;

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
	    return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
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
	ValueBinding vb = getValueBinding("value");
	if (vb != null) {
	    return (vb.getValue(getFacesContext()));
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


    // ---------------------------------------------------- ActionSource Methods


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

        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = saveAttachedState(context, action);
        values[2] = saveAttachedState(context, actionListener);
        values[3] = immediate ? Boolean.TRUE : Boolean.FALSE;
        values[4] = immediateSet ? Boolean.TRUE : Boolean.FALSE;
        values[5] = value;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        action = (MethodBinding) restoreAttachedState(context, values[1]);
        actionListener = (MethodBinding) restoreAttachedState(context, 
							      values[2]);
        immediate = ((Boolean) values[3]).booleanValue();
        immediateSet = ((Boolean) values[4]).booleanValue();
        value = values[5];
    }


    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>In addition to to the default {@link UIComponent#broadcast}
     * processing, pass the {@link ActionEvent} being broadcast to the
     * method referenced by <code>actionListener</code> (if any),
     * and to the default {@link ActionListener} registered on the
     * {@link Application}.</p>
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

        // Perform standard superclass processing
        super.broadcast(event);

        if (event instanceof ActionEvent) {
            FacesContext context = getFacesContext();

            // Notify the specified action listener method (if any)
            MethodBinding mb = getActionListener();
            if (mb != null) {
                mb.invoke(context, new Object[] { event });
            }

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
