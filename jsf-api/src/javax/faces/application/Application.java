/*
 * $Id: Application.java,v 1.20 2003/11/13 04:42:35 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
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
 * Application#createComponent}, {@link Application#createConverter},
 * and {@link Application#createValidator}. </p>
 */

public abstract class Application {


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the default {@link ActionListener} to be registered for all
     * {@link UICommand} components.  The default implementation must perform
     * the following functions:</p>
     * <ul>
     * <li>The <code>getPhaseId()</code> method of this listener instance
     *     must return <code>PhaseId.INVOKE_APPLICATION</code>.</li>
     * <li>The <code>processAction()</code> method must first call
     *     <code>FacesContext.renderResponse()</code> in order to bypass
     *     any intervening lifecycle phases, once the method returns.</li>
     * <li>The <code>processAction()</code> method must next determine
     *     the logical outcome of this event, as follows:
     *     <ul>
     *     <li>If the originating component has a non-<code>null</code>
     *         <code>action</code> property, its value is used as the
     *         logical outcome.</li>
     *     <li>If the originating component has a non-<code>null</code>
     *         <code>actionRef</code> property, create a {@link ValueBinding}
     *         for this reference expression, call <code>getValue()</code>
     *         to retrieve an <code>Action</code> instance.  Call the
     *         <code>invoke()</code> method on this instance, and use the
     *         returned value as the logical outcome.</li>
     *     <li>Otherwise, the logical outcome is <code>null</code>.</li>
     *     </ul></li>
     * <li>The <code>processAction()</code> method must finally retrieve
     *     the <code>NavigationHandler()</code> instance for this
     *     application, and pass the {@link FacesContext} for the
     *     current request, the <code>actionRef</code> value of the
     *     originating component (if any), and the logical outcome
     *     as determined above to the <code>handleNavigation()</code>
     *     method.</li>
     * </ul>
     */
    public abstract ActionListener getActionListener();


    /**
     * <p>Replace the default {@link ActionListener} to be registered for all
     * {@link UICommand} components.</p>
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
     * <p>Return the default <code>Locale</code> that was given in the
     * application configuration resources.</p>
     *
     */ 
    public abstract Locale getDefaultLocale();


    /**
     *
     * <p>Make it so the argument <code>newLocale</code> is returned the
     * next time {@link #getDefaultLocale} is called.</p>     
     */
    public abstract void setDefaultLocale(Locale newLocale);

    /**
     * <p>Set the name of the <code>ResourceBundle</code> to be used for
     * faces messages.  The argument can either designate a
     * <code>ResourceBundle</code> class, in which case the value of the
     * argument is a regular fully qualified classname. Alternatively,
     * it can designate a properties file in which case the name of the
     * properties file should be specified as the basename without the
     * .properties extension relative to the package and using "." as
     * the separator. That is, "foo/bar/messages.properties" should be
     * specified as "foo.bar.messages".</p>
     */ 
    public abstract void setMessageBundle(String messageBundle);

    /**
     * <p>Return the value set by the previous call to {@link
     * #setMessageBundle}.</p>
     */
    public abstract String getMessageBundle();

    /**
     * <p>Return the {@link NavigationHandler} instance that will be passed
     * the outcome returned by any invoked application action for this
     * web application.  The default implementation must provide the behavior
     * described in the {@link NavigationHandler} class description.</p>
     */
    public abstract NavigationHandler getNavigationHandler();


    /**
     * <p>Set the {@link NavigationHandler} instance that will be passed
     * the outcome returned by any invoked application action for this
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


    /**
     * <p>Return the {@link ViewHandler} instance that will be utilized
     * during the <em>Render Response</em> and <em>Restore View</em>
     * phases of the request processing lifecycle.</p>
     */
    public abstract ViewHandler getViewHandler();


    /**
     * <p>Set the {@link ViewHandler} instance that will be utilized
     * during the <em>Render Response</em> and <em>Restore View</em>
     * phases of the request processing lifecycle.</p>
     *
     * @param handler The new {@link ViewHandler} instance
     *
     * @exception IllegalStateException if this method is called after
     * at least one request has been processed by the
     * <code>Lifecycle</code> instance for this application.
     * @exception NullPointerException if <code>handler</code>
     *  is <code>null</code>
     */
    public abstract void setViewHandler(ViewHandler handler);


    // ------------------------------------------------------- Object Factories


    /**
     * <p>Register a new mapping of component type to the name of the
     * corresponding {@link UIComponent} class.  This allows subsequent calls
     * to <code>createComponent()</code> to serve as a factory for
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
    public abstract UIComponent createComponent(String componentType)
        throws FacesException;


    /**
     * <p>Call the <code>getValue()</code> method on the specified
     * {@link ValueBinding}.  If it returns a {@link UIComponent} instance,
     * return it as the value of this method.  If it does not, instantiate
     * a new {@link UIComponent} instance of the specified component type,
     * pass the new component to the <code>setValue()</code> method of the
     * specified {@link ValueBinding}, and return it.</p>
     *
     * @param componentRef {@link ValueBinding} representing a component
     *  reference (typically specified by the <code>componentRef</code>
     *  attribute of a custom tag)
     * @param context {@link FacesContext} for the current request
     * @param componentType Component type to create if the {@link ValueBinding}
     *  does not return a component instance
     *
     * @exception FacesException if a {@link UIComponent} cannot be created
     * @exception NullPointerException if any parameter is <code>null</code>
     */
    public abstract UIComponent createComponent(ValueBinding componentRef,
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
     * to <code>createConverter()</code> to serve as a factory for
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
     * <p>Register a new converter class that is capable of performing
     * conversions for the specified target class.</p>
     *
     * @param targetClass The class for which this converter is registered
     * @param converterClass The fully qualified class name of the
     *  corresponding {@link Converter} implementation
     *
     * @exception NullPointerException if <code>targetClass</code>
     *  or <code>converterClass</code> is <code>null</code>
     */
    public abstract void addConverter(Class targetClass,
                                      String converterClass);


    /**
     * <p>Instantiate and return a new {@link Converter} instance of the
     * class specified by a previous call to <code>addConverter()</code>
     * for the specified converter id.  If there is no such registration
     * for this converter id, return <code>null</code>.</p>
     *
     * @param converterId The converter id for which to create and
     *  return a new {@link Converter} instance
     *
     * @exception FacesException if the {@link Converter} cannot be
     *  created
     * @exception NullPointerException if <code>converterId</code>
     *  is <code>null</code>
     */ 
    public abstract Converter createConverter(String converterId);


    /**
     * <p>Instantiate and return a new {@link Converter} instance of the
     * class that has registered itself as capable of performing conversions
     * for objects of the specified type.  If no such {@link Converter} class
     * can be identified, return <code>null</code>.</p>
     *
     * <p>To locate an appropriate {@link Converter} class, the following
     * algorithm is performed, stopping as soon as an appropriate {@link
     * Converter} class is found:</p>
     * <ul>
     * <li>Locate a {@link Converter} registered for the target class itself.
     *     </li>
     * <li>Locate a {@link Converter} registered for interfaces that are
     *     implemented by the target class (directly or indirectly).</li>
     * <li>Locate a {@link Converter} registered for the superclass (if any)
     *     of the target class, recursively working up the inheritance
     *     hierarchy.</li>
     * </ul>
     *
     * @param targetClass Target class for which to return a {@link Converter}
     *
     * @exception FacesException if the {@link Converter} cannot be
     *  created
     * @exception NullPointerException if <code>targetClass</code>
     *  is <code>null</code>
     */
    public abstract Converter createConverter(Class targetClass);


    /**
     * <p>Return an <code>Iterator</code> over the set of currently registered
     * converter ids for this <code>Application</code>.</p>
     */
    public abstract Iterator getConverterIds();

    
    /**
     * <p>Return an <code>Iterator</code> over the set of <code>Class</code>
     * instances for which {@link Converter} classes have been explicitly
     * registered.</p>
     */
    public abstract Iterator getConverterTypes();

    /**
     * <p>Return a {@link MethodBinding} for the specified method
     * reference expression, which may be used to call the corresponding
     * method later.  The returned {@link MethodBinding} instance must
     * utilize the {@link PropertyResolver} and {@link VariableResolver}
     * instances registered with this {@link Application} instance at the
     * time that the {@link MethodBinding} instance was initially created.</p>
     *
     * <p>For maximum performance, implementations of {@link Application}
     * may, but are not required to, cache {@link MethodBinding} instances
     * in order to avoid repeated parsing of the reference expression.
     * However, under no circumstances may a particular {@link MethodBinding}
     * instance be shared across multiple web applications.</p>
     *
     * @param ref Reference expression for which to return a
     *  {@link MethodBinding} instance
     * @param params Parameter signatures that must match exactly on the
     *  method to be invoked, or <code>null</code> for a method that takes
     *  no parameters
     *
     * @exception NullPointerException if <code>ref</code>
     *  is <code>null</code>
     * @exception ReferenceSyntaxException if the specified <code>ref</code>
     *  has invalid syntax
     */
    public abstract MethodBinding getMethodBinding(String ref, Class params[])
        throws ReferenceSyntaxException;


    /**
     * <p>Return an <code>Iterator</code> over the supported
     * <code>Locale</code> instances specified in the application
     * configuration resources.</p>
     *
     */ 
    public abstract Iterator getSupportedLocales();


    /**
     * <p>Make it so the <code>Locale</code> instances in the argument
     * <code>newLocales</code> are returned the next time {@link
     * #getSupportedLocales} is called.</p>
     *
     * @exception NullPointerException if the argument
     * <code>newLocales</code> is <code>null</code>.
     *
     */ 
    public abstract void setSupportedLocales(Collection newLocales);


    /**
     * <p>Register a new mapping of validator id to the name of the
     * corresponding {@link Validator} class.  This allows subsequent calls
     * to <code>createValidator()</code> to serve as a factory for
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
    public abstract Validator createValidator(String validatorId)
        throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of currently registered
     * validator ids for this <code>Application</code>.</p>
     */
    public abstract Iterator getValidatorIds();


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
