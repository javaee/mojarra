/*
 * $Id: ActionSource.java,v 1.8 2004/01/26 20:48:53 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.el.MethodBinding;



/**
 * <p><strong>ActionSource</strong> is an interface that may be implemented
 * by any concrete {@link UIComponent} that wishes to be a source of
 * {@link ActionEvent}s, including the ability to invoke application
 * actions via the default {@link ActionListener} mechanim.</p>
 */

public interface ActionSource {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link MethodBinding}pointing at the application
     * action to be invoked, if this {@link UIComponent} is activated by
     * the user, during the <em>Apply Request Values</em> or <em>Invoke
     * Application</em> phase of the request processing lifecycle,
     * depending on the value of the <code>immediate</code>
     * property.</p>
     */
    public MethodBinding getAction();

    /**
     * <p>Set the {@link MethodBinding} pointing at the appication
     * action to be invoked, if this {@link UIComponent} is activated by
     * the user, during the <em>Apply Request Values</em> or <em>Invoke
     * Application</em> phase of the request processing lifecycle,
     * depending on the value of the <code>immediate</code>
     * property.</p>
     *
     * <p>Any method referenced by such an expression must be public, with
     * a return type of <code>String</code>, and accept no parameters.</p>
     *
     * @param action The new MethodBinding expression
     */
    public void setAction(MethodBinding action);


    /**
     * <p>Return the {@link MethodBinding} pointing at an action
     * listener method to be invoked, if this {@link UIComponent} is
     * activated by the user, during the <em>Apply Request Values</em>
     * or <em>Invoke Application</em> phase of the request processing
     * lifecycle, depending upon the value of the <code>immediate</code>
     * property.</p>
     */
    public MethodBinding getActionListener();


    /**
     * <p>Set the {@link MethodBinding} pointing at an action listener
     * method to be invoked, if this {@link UIComponent} is activated by
     * the user, during the <em>Apply Request Values</em> or <em>Invoke
     * Application</em> phase of the request processing lifecycle,
     * depending upon the value of the <code>immmediate</code>
     * property.</p>
     *
     * <p>Any method referenced by such an expression must be public, with
     * a return type of <code>void</code>, and accept a single parameter of
     * type <code>ActionEvent</code>.</p>
     *
     * @param actionListener The new method binding expression
     */
    public void setActionListener(MethodBinding actionListener);

    /**
     * <p>Return a flag indicating that the default {@link ActionListener}
     * provided by the JavaServer Faces implementation should be executed
     * immediately (that is, during <em>Apply Request Values</em> phase
     * of the request processing lifecycle), rather than waiting until the
     * <em>Invoke Application</em> phase.  The default value for this
     * property must be <code>false</code>.</p>
     */
    public boolean isImmediate();


    /**
     * <p>Set the "immediate execution" flag for this {@link UIComponent}.</p>
     *
     * @param immediate The new immediate execution flag
     */
    public void setImmediate(boolean immediate);


    // -------------------------------------------------- Event Listener Methods


    /**
     * <p>Add a new {@link ActionListener} to the set of listeners interested
     * in being notified when {@link ActionEvent}s occur.</p>
     *
     * @param listener The {@link ActionListener} to be added
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void addActionListener(ActionListener listener);


    /**
     * <p>Return the set of registered {@link ActionListener}s for this
     * {@link ActionSource} instance.  If there are no registered listeners,
     * a zero-length array is returned.</p>
     */
    public abstract ActionListener[] getActionListeners();


    /**
     * <p>Remove an existing {@link ActionListener} (if any) from the set of
     * listeners interested in being notified when {@link ActionEvent}s
     * occur.</p>
     *
     * @param listener The {@link ActionListener} to be removed
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void removeActionListener(ActionListener listener);


}
