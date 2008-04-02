/*
 * $Id: Application.java,v 1.5 2003/06/24 16:52:13 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.MessageResources;
import javax.faces.convert.Converter;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;



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
 *
 * <p>The application also acts as a factory for several types of
 * Objects specified in the Faces Configuration file.  Please see {@link
 * Application#getComponent}, {@link Application#getConverter},
 * {@link Application#getMessageResources}, and {@link
 * Application#getValidator}. </p>
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
     * phase of the request processing lifecycle.  This
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


    // ------------------------------------------------------- Object Factories


    /**
     * <p>Register a new mapping of component type to the name of the
     * corresponding {@link UIComponent} class.  This allows subsequent calls
     * to <code>getComponent()</code> to serve as a factory for
     * {@link UIComponent} instances.</p>
     *
     * @param componentType The component type to be registered
     * @param componentClass The fully qualified class name of the
     *  corresponding {@link UIComponent} implementation
     *
     * @exception NullPointerException if <code>componentType</code> or
     *  <code>componentClass</code> is <code>null</code>
     */
    public abstract void addComponent(String componentType,
                                      String componentClass);


    /**
     * <p>Instantiate and return a new {@link UIComponent} instance of the
     * class specified by a previous call to <code>addComponent()</code> for
     * the specified component type.</p>
     *
     * @param componentType The component type for which to create and
     *  return a new {@link UIComponent} instance
     *
     * @exception FacesException if a {@link UIComponent} of the
     *  specified type cannot be created
     * @exception NullPointerException if <code>componentType</code>
     *  is <code>null</code>
     */ 
    public abstract UIComponent getComponent(String componentType)
        throws FacesException;


    /**
     * <p>Call the <code>getValue()</code> method on the specified
     * {@link ValueBinding}.  If it returns a {@link UIComponent} instance,
     * return it as the value of this method.  If it does not, instantiate
     * and return a new {@link UIComponent} instance of the specified
     * component type.</p>
     *
     * @param componentRef {@link ValueBinding} representing a component
     *  reference (typically specified by the <code>componentRef</code>
     *  attribute of a custom tag)
     * @param FacesContext {@link FacesContext} for the current request
     * @param componentType Component type to create if the {@link ValueBinding}
     *  does not return a component instance
     *
     * @exception FacesException if a {@link UIComponent} cannot be created
     * @exception NullPointerExcepton if any parameter is <code>null</code>
     */
    public abstract UIComponent getComponent(ValueBinding componentRef,
					     FacesContext context,
					     String componentType)
	throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of currently defined
     * component types for this <code>Application</code>.</p>
     */
    public abstract Iterator getComponentTypes();


    /**
     * <p>Register a new mapping of converter id to the name of the
     * corresponding {@link Converter} class.  This allows subsequent calls
     * to <code>getConverter()</code> to serve as a factory for
     * {@link Converter} instances.</p>
     *
     * @param converterId The converter id to be registered
     * @param converterClass The fully qualified class name of the
     *  corresponding {@link Converter} implementation
     *
     * @exception NullPointerException if <code>converterId</code>
     *  or <code>converterClass</code> is <code>null</code>
     */
    public abstract void addConverter(String converterId, 
				      String converterClass);


    /**
     * <p>Instantiate and return a new {@link Converter} instance of the
     * class specified by a previous call to <code>addConverter()</code>
     * for the specified converter id.</p>
     *
     * @param converterId The converter id for which to create and
     *  return a new {@link Converter} instance
     *
     * @exception FacesException if a {@link Converter} of the
     *  specified id cannot be created
     * @exception NullPointerException if <code>converterId</code>
     *  is <code>null</code>
     */ 
    public abstract Converter getConverter(String converterId)
        throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of currently registered
     * converter ids for this <code>Application</code>.</p>
     */
    public abstract Iterator getConverterIds();

    
    /**
    * <p>Register a new mapping of message resources id to the name of the
    * corresponding {@link MessageResources} class.  This allows subsequent
    * calls to <code>getMessageResources()</code> to serve as a factory for
    * {@link MessageResources} instances.</p>
    *
    * @param messageResourcesId The message resources id to be registered
    * @param messageResourcesClass The fully qualified class name of the
    *  corresponding {@link MessageResources} implementation
    *
    * @exception NullPointerException if <code>messageResourcesId</code>
    *  or <code>messageResourcesClass</code> is <code>null</code>
    */
    public abstract void addMessageResources(String messageResourcesId,
					     String messageResourcesClass);


    /**
     * <p>Instantiate (if necessary) and return a {@link MessageResources}
     * instance of the class specified by a previous call to
     * <code>addMessageResources</code>.</p>
     *
     * @param messageResourcesId The message resources id for which to
     *  create (if necessary) and return a {@link MessageResources}
     *  instance
     *
     * @exception FacesException if a {@link MessageResources} instance
     *  of the specified id cannot be created
     * @exception NullPointerException if <code>messageResourcesId</code>
     *  is <code>null</code>
     */ 
    public abstract MessageResources getMessageResources
        (String messageResourcesId) throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of currently registered
     * message resources ids for this <code>Application</code>.</p>
     */
    public abstract Iterator getMessageResourcesIds();


    /**
     * <p>Register a new mapping of validator id to the name of the
     * corresponding {@link Validator} class.  This allows subsequent calls
     * to <code>getValidator()</code> to serve as a factory for
     * {@link Validator} instances.</p>
     *
     * @param validatorId The validator id to be registered
     * @param validatorClass The fully qualified class name of the
     *  corresponding {@link Validator} implementation
     *
     * @exception NullPointerException if <code>validatorId</code>
     *  or <code>validatorClass</code> is <code>null</code>
     */
    public abstract void addValidator(String validatorId, 
				      String validatorClass);

    /**
     * <p>Instantiate and return a new {@link Validator} instance of the
     * class specified by a previous call to <code>addValidator()</code>
     * for the specified validator id.</p>
     *
     * @param validatorId The validator id for which to create and
     *  return a new {@link Validator} instance
     *
     * @exception FacesException if a {@link Validator} of the
     *  specified id cannot be created
     * @exception NullPointerException if <code>validatorId</code>
     *  is <code>null</code>
     */ 
    public abstract Validator getValidator(String validatorId)
        throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of currently registered
     * validator ids for this <code>Application</code>.</p>
     */
    public abstract Iterator getValidatorIds();


    // ---------------------------------------------------------- Static Methods


    /**
     * <p>Return the {@link Application} instance for the
     * current application.</p>
     */
    public static Application getCurrentInstance() {

	ApplicationFactory factory = (ApplicationFactory)
	    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	return (factory.getApplication());

    }


}
