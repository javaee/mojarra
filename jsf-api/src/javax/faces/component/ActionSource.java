/*
 * $Id: ActionSource.java,v 1.1 2003/08/30 00:31:30 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.application.Action;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;



/**
 * <p><strong>ActionSource</strong> is an interface that may be implemented
 * by any concrete {@link UIComponent} that wishes to be a source of
 * {@link ActionEvent}s, including the ability to invoke application
 * {@link Action}s via the default {@link ActionListener} mechanim.</p>
 */

public interface ActionSource extends UIComponent {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the literal action outcome value to be returned to the
     * {@link ActionListener} processing application level events for this
     * application.</p>
     */
    public String getAction();


    /**
     * <p>Set the literal action outcome value for this component.</p>
     *
     * @param action The new outcome value
     */
    public void setAction(String action);


    /**
     * <p>Return the <em>action reference expression</em> pointing at the
     * {@link Action} to be invoked, if this {@link UIComponent}
     * is activated by the user, during the <em>Apply Request Values</em>
     * or <em>Invoke Application</em> phase of the request processing
     * lifecycle, depending on the value of the <code>immediate</code>
     * property.</p>
     */
    public String getActionRef();


    /**
     * <p>Set the <em>action reference expression</em> pointing at the
     * {@link Action} to be invoked, if this {@link UIComponent}
     * is activated by the user, during the <em>Apply Request Values</em>
     * or <em>Invoke Application</em> phase of the request processing
     * lifecycle, depending on the value of the <code>immediate</code>
     * property.</p>
     *
     * @param actionRef The new action reference
     */
    public void setActionRef(String actionRef);


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
     * <p>Set the "immediate execution" flag for this {@link UICommand}.</p>
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
