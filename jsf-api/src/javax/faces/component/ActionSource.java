/*
 * $Id: ActionSource.java,v 1.13 2005/08/22 22:07:52 ofung Exp $
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


import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.el.MethodBinding;



/**
 * <p><strong>ActionSource</strong> is an interface that may be implemented
 * by any concrete {@link UIComponent} that wishes to be a source of
 * {@link ActionEvent}s, including the ability to invoke application
 * actions via the default {@link ActionListener} mechanism.</p>
 */

public interface ActionSource {


    // -------------------------------------------------------------- Properties


    /**
     * <p>If the implementing class also implements {@link
     * ActionSource2}, the implementation of this method must call
     * through to {@link ActionSource2#getActionExpression} and examine
     * the result.  If the result came from a previous call to {@link
     * #setAction}, extract the <code>MethodBinding</code> from it and
     * return it.  Otherwise, wrap the returned {@link
     * javax.el.MethodExpression} in a <code>MethodBinding</code>
     * implementation, and return it.</p>
     * 
     * <p>If the implementing class does not implement
     * <code>ActionSource2</code>, return the {@link
     * MethodBinding}pointing at the application action to be invoked,
     * if this {@link UIComponent} is activated by the user, during the
     * <em>Apply Request Values</em> or <em>Invoke Application</em>
     * phase of the request processing lifecycle, depending on the value
     * of the <code>immediate</code> property.</p>
     *
     * @deprecated This has been replaced by {@link
     * ActionSource2#getActionExpression}.
     */
    public MethodBinding getAction();

    /**
     * <p>If the implementing class also implements {@link
     * ActionSource2}, the implementation of this method must wrap the
     * argument <code>action</code> in a class that implements {@link
     * javax.el.MethodExpression} and call through to {@link
     * ActionSource2#setActionExpression}, passing the wrapped
     * <code>action</code>.</p>
     *
     * <p>If the implementing class does not implement
     * <code>ActionSource2</code>, set the {@link MethodBinding}
     * pointing at the appication action to be invoked, if this {@link
     * UIComponent} is activated by the user, during the <em>Apply
     * Request Values</em> or <em>Invoke Application</em> phase of the
     * request processing lifecycle, depending on the value of the
     * <code>immediate</code> property.</p>
     *
     * <p>Any method referenced by such an expression must be public, with
     * a return type of <code>String</code>, and accept no parameters.</p>
     *
     * @param action The new MethodBinding expression
     *
     * @deprecated This has been replaced by {@link
     * ActionSource2#setActionExpression(javax.el.MethodExpression)}.
     */
    public void setAction(MethodBinding action);


    /**
     * <p>If {@link #setActionListener} was not previously called
     * for this instance, this method must return <code>null</code>.  If
     * it was called, this method must return the exact
     * <code>MethodBinding</code> instance that was passed to {@link
     * #setActionListener}.</p>
     *
     * <p> The method to be invoked, if this {@link UIComponent} is
     * activated by the user, will be called during the <em>Apply
     * Request Values</em> or <em>Invoke Application</em> phase of the
     * request processing lifecycle, depending upon the value of the
     * <code>immediate</code> property.</p>
     *
     * @deprecated Use {@link #getActionListeners} instead.
     */
    public MethodBinding getActionListener();


    /**
     * <p>Wrap the argument <code>actionListener</code> in an
     * implementation of {@link ActionListener}
     * and store it in the internal data structure that backs the {@link
     * #getActionListeners} method, taking care to over-write any
     * instance that was stored by a previous call to
     * <code>setActionListener</code>.</p>
     *
     * <p>Any method referenced by such an expression must be public, with
     * a return type of <code>void</code>, and accept a single parameter of
     * type <code>ActionEvent</code>.</p>
     *
     * @param actionListener The new method binding expression
     *
     * @deprecated This has been replaced by {@link
     * #addActionListener(javax.faces.event.ActionListener)}.
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
