/*
 * $Id: Application.java,v 1.2 2003/03/13 01:11:52 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;


/**
 * <p><strong>Application</strong> represents a per-web-application
 * singleton object where applications based on JavaServer Faces (or
 * implementations wishing to provide extended functionality) can
 * register application-wide singletons that provide functionality
 * required by JavaServer Faces.  Default implementations of each
 * object are provided for cases where the application does not choose
 * to customize the behavior.</p>
 *
 * <p>The instance of {@link Application} is created by calling the
 * <code>getApplication()</code> method of {@link ApplicationFactory}.
 * Because this instance is shared, it must be implemented in a
 * thread-safe manner.</p>
 */

public abstract class Application {


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link ActionListener} that will be the default
     * {@link ActionListener} to be registered with relevant components
     * during the <em>Reconstitute Component Tree</em> phase of the
     * request processing lifecycle.  The default implementation will
     * perform the following functions:</p>
     * <ul>
     * <li>The <code>getPhaseId()</code> method of this listener instance
     *     must return <code>PhaseId.INVOKE_APPLICATION</code>.</li>
     * <li>If the source component has a non-null <code>action</code>
     *     property, return that value.</li>
     * <li>If the source component has a non-null <code>actionRef</code>
     *     property, evaluate this value reference to retrieve the
     *     corresponding object.</li>
     * <li>If there is no such corresponding object, or if this object
     *     does not implement {@link Action}, throw an
     *     <code>IllegalArgumentException</code>.</li>
     * <li>Call the <code>invoke()</code> method of the returned object,
     *     and return the return value from that method call.</li>
     * </ul>
     */
    public abstract ActionListener getActionListener();


    /**
     * <p>Replace the default {@link ActionListener} that will be registered
     * with relevant components during the <em>Reconstitute Component Tree</em>
     * phase of the requset processing lifecycle.  This
     * listener must return <code>PhaseId.INVOKE_APPLICATION</code> from its
     * <code>getPhaseId()</code> method.</p>
     *
     * @param listener The new {@link ActionListener}
     *
     * @exception IllegalArgumentException if the specified
     *  <code>listener</code> does not return
     *  <code>PhaseId.INVOKE_APPLICATION</code> from its
     *  <code>getPhaseId()</code> method
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public abstract void setActionListener(ActionListener listener);


    /**
     * <p>Return the {@link NavigationHandler} instance that will be passed
     * the outcome returned by any invoked {@link Action} for this
     * web application.  The default implementation must provide the behavior
     * described in the {@link NavigationHandler} class description.</p>
     */
    public abstract NavigationHandler getNavigationHandler();


    /**
     * <p>Set the {@link NavigationHandler} instance that will be passed
     * the outcome returned by any invoked {@link Action} for this
     * web application.</p>
     *
     * @param handler The new {@link NavigationHandler} instance
     *
     * @exception NullPointerException if <code>handler</code>
     *  is <code>null</code>
     */
    public abstract void setNavigationHandler(NavigationHandler handler);


    /**
     * <p>Return the {@link PropertyResolver} instance that will be utilized
     * to resolve action and valus references.  The default implementation
     * must provide the behavior described in the
     * {@link PropertyResolver} class description.</p>
     */
    public abstract PropertyResolver getPropertyResolver();


    /**
     * <p>Set the {@link PropertyResolver} instance that will be utilized
     * to resolve action and value references.</p>
     *
     * @param resolver The new {@link PropertyResolver} instance
     *
     * @exception NullPointerException if <code>resolver</code>
     *  is <code>null</code>
     */
    public abstract void setPropertyResolver(PropertyResolver resolver);


    /**
     * <p>Return a {@link ValueBinding} for the specified action or value
     * reference expression, which may be used to manipulate the corresponding
     * property value later.  The returned {@link ValueBinding} instance must
     * utilize the {@link PropertyResolver} and {@link VariableResolver}
     * instances registered with this {@link Application} instance at the
     * time that the {@link ValueBinding} instance was initially created.</p>
     *
     * <p>For maximum performance, implementations of {@link Application}
     * may, but are not required to, cache {@link ValueBinding} instances
     * in order to avoid repeated parsing of the reference expression.
     * However, under no circumstances may a particular {@link ValueBinding}
     * instance be shared across multiple web applications.</p>
     *
     * @param ref Reference expression for which to return a
     *  {@link ValueBinding} instance
     *
     * @exception NullPointerException if <code>ref</code>
     *  is <code>null</code>
     * @exception ReferenceSyntaxException if the specified <code>ref</code>
     *  has invalid syntax
     */
    public abstract ValueBinding getValueBinding(String ref)
        throws ReferenceSyntaxException;


    /**
     * <p>Return the {@link VariableResolver} instance that will be utilized
     * to resolve action and value references.  The default implementation
     * must provide the behavior described in the
     * {@link VariableResolver} class description.</p>
     */
    public abstract VariableResolver getVariableResolver();


    /**
     * <p>Set the {@link VariableResolver} instance that will be utilized
     * to resolve action and value references.</p>
     *
     * @param resolver The new {@link VariableResolver} instance
     *
     * @exception NullPointerException if <code>resolver</code>
     *  is <code>null</code>
     */
    public abstract void setVariableResolver(VariableResolver resolver);
    

}
