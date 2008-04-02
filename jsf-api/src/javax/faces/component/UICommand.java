/*
 * $Id: UICommand.java,v 1.39 2003/07/27 00:48:20 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.Renderer;


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

public interface UICommand extends UIOutput {


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
     * {@link javax.faces.application.Action} to be invoked, if this component
     * is activated by the user, during <em>Invoke Application</em> phase
     * of the request processing lifecycle.</p>
     */
    public String getActionRef();


    /**
     * <p>Set the <em>action reference expression</em> pointing at the
     * {@link javax.faces.application.Action} to be invoked, if this component
     * is activated by the user, during <em>Invoke Application</em> phase
     * of the request processing lifecycle.</p>
     *
     * @param actionRef The new action reference
     */
    public void setActionRef(String actionRef);


    // ------------------------------------------------ Event Processing Methods


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
