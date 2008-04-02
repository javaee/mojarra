/*
 * $Id: Application.java,v 1.25 2004/01/21 03:50:23 eburns Exp $
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
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
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
 * {@link Application#createMethodBinding},
 * {@link Application#createValidator}, and
 * {@link Application#createValueBinding}. </p>
 */

public abstract class Application {


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the default {@link ActionListener} to be registered for all
     * {@link ActionSource} components in this appication.  If not explicitly
     * set, a default implementation must be provided that performs the
     * following functions:</p>
     * <ul>
     * <li>The <code>processAction()</code> method must first call
     *     <code>FacesContext.renderResponse()</code> in order to bypass
     *     any intervening lifecycle phases, once the method returns.</li>
     * <li>The <code>processAction()</code> method must next determine
     *     the logical outcome of this event, as follows:
     *     <ul>
     *     <li>If the originating component has a non-<code>null</code>
     *     <code>action</code> property, retrieve the {@link
     *     MethodBinding} from the property, and call
     *     <code>invoke()</code> on it.  Convert the returned value (if
     *     any) to a String, and use it as the logical outcome.</li>

     *     <li>Otherwise, the logical outcome is <code>null</code>.</li>
     *     </ul></li>

     * <li>The <code>processAction()</code> method must finally retrieve
     *     the <code>NavigationHandler</code> instance for this
     *     application and call {@link
     *     NavigationHandler#handleNavigation} passing: 
     *
     *     <ul>

     *     <li>the {@link FacesContext} for the current request</li>

     *     <li>If there is a <code>MethodBinding</code> instance for the
     *     <code>action</code> property of this component, the result of
     *     calling {@link MethodBinding#getExpressionString} on it, null
     *     otherwise</li>
     *
     *     <li>the logical outcome as determined above</li>
     *
     *     </ul>
     *
     *     </li>
     * </ul>
     */
    public abstract ActionListener getActionListener();


    /**
     * <p>Set the default {@link ActionListener} to be registered for all
     * {@link ActionSource} components.</p>
     * </p>
     *
     * @param listener The new default {@link ActionListener}
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public abstract void setActionListener(ActionListener listener);


    /**
     * <p>Return the default <code>Locale</code> for this application.  If
     * not explicitly set, <code>null</code> is returned.</p>
     */ 
    public abstract Locale getDefaultLocale();


    /**
     * <p>Set the default <code>Locale</code> for this application.</p>
     *
     * @param locale The new default <code>Locale</code>
     *
     * @exception NullPointerException if <code>locale</code>
     *  is <code>null</code>
     */
    public abstract void setDefaultLocale(Locale locale);

    /**
     * <p>Return the <code>render-kit-id</code> to be used for rendering
     * this application, or <code>null</code> if {@link
     * javax.faces.render.RenderKitFactory#HTML_BASIC_RENDER_KIT} should
     * be used.</p>
     */
    public abstract String getDefaultRenderKitId();

    /**
     * <p>Set the <code>render-kit-id</code> to be used to render this
     * application.  Unless the client has provided a custom {@link
     * ViewHandler} that is aware of allowing multiple {@link
     * javax.faces.render.RenderKit} instances to be used in the same
     * application, this method must only be called by the configuration
     * system during application startup.  It must not be called
     * dynamically during the runtime of the application.  This is a
     * limitation of the current specification and may be lifted in a
     * future release.</p>
     */
    // PENDING(edburns): remove limitation
    public abstract void setDefaultRenderKitId(String renderKitId);
	


    /**
     * <p>Return the fully qualified class name of the
     * <code>ResourceBundle</code> to be used for JavaServer Faces messages
     * for this application.  If not explicitly set, <code>null</code>
     * is returned.</p>
     */
    public abstract String getMessageBundle();


    /**
     * <p>Set the fully qualified class name of the <code>ResourceBundle</code>
     * to be used for JavaServer Faces messages for this application.  See the
     * JavaDocs for the <code>java.util.ResourceBundle</code> class for more
     * information about the syntax for resource bundle names.</p>
     *
     * @param bundle Base name of the resource bundle to be used
     *
     * @exception NullPointerException if <code>bundle</code>
     *  is <code>null</code>
     */
    public abstract void setMessageBundle(String bundle);


    /**
     * <p>Return the {@link NavigationHandler} instance that will be passed
     * the outcome returned by any invoked application action for this
     * web application.  If not explicitly set, a default implementation
     * must be provided that performs the functions described in the
     * {@link NavigationHandler} class description.</p>
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
     * to resolve method and value references.  If not explicitly set, a default
     * implementation must be provided that performs the functions described in
     * the {@link PropertyResolver} class description.</p>
     */
    public abstract PropertyResolver getPropertyResolver();


    /**
     * <p>Set the {@link PropertyResolver} instance that will be utilized
     * to resolve method and value references.</p>
     *
     * @param resolver The new {@link PropertyResolver} instance
     *
     * @exception NullPointerException if <code>resolver</code>
     *  is <code>null</code>
     */
    public abstract void setPropertyResolver(PropertyResolver resolver);


    /**
     * <p>Return the {@link VariableResolver} instance that will be utilized
     * to resolve method and value references.  If not explicitly set, a default
     * implementation must be provided that performs the functions described in
     * the {@link VariableResolver} class description.</p>
     */
    public abstract VariableResolver getVariableResolver();


    /**
     * <p>Set the {@link VariableResolver} instance that will be utilized
     * to resolve method and value references.</p>
     *
     * @param resolver The new {@link VariableResolver} instance
     *
     * @exception NullPointerException if <code>resolver</code>
     *  is <code>null</code>
     */
    public abstract void setVariableResolver(VariableResolver resolver);


    /**
     * <p>Return the {@link ViewHandler} instance that will be utilized
     * during the <em>Restore View</em> and <em>Render Response</em>
     * phases of the request processing lifecycle.  If not explicitly set,
     * a default implementation must be provided that performs the functions
     * described in the {@link ViewHandler} description in the
     * JavaServer Faces Specification.</p>
     */
    public abstract ViewHandler getViewHandler();


    /**
     * <p>Set the {@link ViewHandler} instance that will be utilized
     * during the <em>Restore View</em> and <em>Render Response</em>
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



    /**
     * <p>Return the {@link StateManager} instance that will be utilized
     * during the <em>Restore View</em> and <em>Render Response</em>
     * phases of the request processing lifecycle.  If not explicitly set,
     * a default implementation must be provided that performs the functions
     * described in the {@link StateManager} description
     * in the JavaServer Faces Specification.</p>
     */
    public abstract StateManager getStateManager();


    /**
     * <p>Set the {@link StateManager} instance that will be utilized
     * during the <em>Restore View</em> and <em>Render Response</em>
     * phases of the request processing lifecycle.</p>
     *
     * @param manager The new {@link StateManager} instance
     *
     * @exception IllegalStateException if this method is called after
     * at least one request has been processed by the
     * <code>Lifecycle</code> instance for this application.
     * @exception NullPointerException if <code>manager</code>
     *  is <code>null</code>
     */
    public abstract void setStateManager(StateManager manager);


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
     * @param componentBinding {@link ValueBinding} representing a
     * component value binding expression (typically specified by the
     * <code>component</code> attribute of a custom tag)
     * @param context {@link FacesContext} for the current request
     * @param componentType Component type to create if the {@link ValueBinding}
     *  does not return a component instance
     *
     * @exception FacesException if a {@link UIComponent} cannot be created
     * @exception NullPointerException if any parameter is <code>null</code>
     */
    public abstract UIComponent createComponent(ValueBinding componentBinding,
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
     * <p>Instantiate and return a new {@link MethodBinding} for the specified
     * method reference expression, which may be used to call the corresponding
     * method later.</p>
     *
     * @param ref Reference expression for which to return a
     *  {@link MethodBinding} instance
     * @param params Parameter signatures that must be compatible with those
     *  of the method to be invoked, or a zero-length array or <code>null</code>
     *  for a method that takes no parameters
     *
     * @exception NullPointerException if <code>ref</code>
     *  is <code>null</code>
     * @exception ReferenceSyntaxException if the specified <code>ref</code>
     *  has invalid syntax
     */
    public abstract MethodBinding createMethodBinding(String ref,
                                                      Class params[])
        throws ReferenceSyntaxException;


    /**
     * <p>Return an <code>Iterator</code> over the supported
     * <code>Locale</code>s for this appication.</p>
     */ 
    public abstract Iterator getSupportedLocales();


    /**
     * <p>Set the <code>Locale</code> instances representing the supported
     * <code>Locale</code>s for this application.</p>
     *
     * @param locales The set of supported <code>Locale</code>s
     *  for this application
     *
     * @exception NullPointerException if the argument
     * <code>newLocales</code> is <code>null</code>.
     *
     */ 
    public abstract void setSupportedLocales(Collection locales);


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
     * <p>Instantiate and return a new {@link ValueBinding} for the specified
     * value reference expression, which may be used to manipulate the
     * corresponding property value later.</p>
     *
     * @param ref Reference expression for which to return a
     *  {@link ValueBinding} instance
     *
     * @exception NullPointerException if <code>ref</code>
     *  is <code>null</code>
     * @exception ReferenceSyntaxException if the specified <code>ref</code>
     *  has invalid syntax
     */
    public abstract ValueBinding createValueBinding(String ref)
        throws ReferenceSyntaxException;


}
