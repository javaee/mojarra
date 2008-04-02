/*
 * $Id: ApplicationImpl.java,v 1.31 2003/10/26 04:44:56 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Collection;
import java.util.Locale;
import java.util.ArrayList;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.application.MessageResources;
import javax.faces.convert.Converter;
import javax.faces.event.ActionListener;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.el.ValueBinding;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.event.PhaseId;
import javax.faces.validator.Validator;
import javax.faces.FacesException;

import com.sun.faces.RIConstants;
import com.sun.faces.config.ManagedBeanFactory;
import com.sun.faces.el.MethodBindingImpl;
import com.sun.faces.el.ValueBindingImpl;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.VariableResolverImpl;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.ExpressionEvaluator;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.util.Util;

import org.mozilla.util.Assert;



/**
 * <p><strong>Application</strong> represents a per-web-application
 * singleton object where applications based on JavaServer Faces (or
 * implementations wishing to provide extended functionality) can
 * register application-wide singletons that provide functionality
 * required by JavaServer Faces.
 */
public class ApplicationImpl extends Application {

// Relationship Instance Variables

    private ActionListener actionListener = null;
    private NavigationHandler navigationHandler = null;
    private PropertyResolver propertyResolver = null;
    private VariableResolver variableResolver = null;
    private ViewHandler viewHandler = null;
    //
    // This map stores reference expression | value binding instance
    // mappings.
    //
    private Map valueBindingMap;
    //
    // These three maps store store "identifier" | "class name"
    // mappings.
    //
    private Map componentMap = null;
    private Map converterIdMap = null;
    private Map converterTypeMap = null;
    private Map validatorMap = null;
    //
    // This map stores "managed bean name" | "managed bean factory"
    // mappings.
    //
    private Map managedBeanFactoriesMap = null;
    //
    // This map stores "message resource identifier" | "class name"
    // mappings.  As information is requested using the 
    // "getMessageResources" method, the class name is used to create
    // the MessageRewources instance, and the class name in the map is replaced
    // with the instance.
    //
    private Map messageResourcesMap = null;        

    // Flag indicating that a response has been rendered.
    private boolean responseRendered = false;
   
//
// Constructors and Initializers
//

    /**
     * Constructor 
     */
    public ApplicationImpl () {
        super();
        valueBindingMap = new HashMap();
	componentMap = new HashMap();
	converterIdMap = new HashMap();
	converterTypeMap = new HashMap();
	validatorMap = new HashMap();
	managedBeanFactoriesMap = new HashMap();
	messageResourcesMap = new HashMap();   
    }

    /**
     * <p>Return the {@link ActionListener} that will be the default
     * {@link ActionListener} to be registered with relevant components
     * during the <em>Restore View</em> phase of the
     * request processing lifecycle.
     */
    public ActionListener getActionListener() {
        return actionListener;
    }

    public ViewHandler getViewHandler() {        
        return viewHandler;
    }

    
    public void setViewHandler(ViewHandler handler) {
        if (handler == null) {
            throw new NullPointerException(
                    Util.getExceptionMessage(
                            Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (responseRendered) {
            // at least one response has been rendered.
            throw new IllegalStateException(
                    Util.getExceptionMessage(
                            Util.ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER_ID));            
        }        
        viewHandler = handler;    
    }   

    
    /**
     * <p>Replace the default {@link ActionListener} that will be registered
     * with relevant components during the <em>Restore View</em>
     * phase of the requset processing lifecycle.  This
     * listener must return <code>PhaseId.INVOKE_APPLICATION</code> from its
     * <code>getPhaseId()</code> method.</p>
     *
     * @param listener The new {@link ActionListener}
     *
     * @exception IllegalArgumentException if the specified
     *	<code>listener</code> does not return
     *	<code>PhaseId.INVOKE_APPLICATION</code> from its
     *	<code>getPhaseId()</code> method
     * @exception NullPointerException if <code>listener</code>
     *	is <code>null</code>
     */
    public void setActionListener(ActionListener listener) {
        if (listener == null) {
            throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (listener.getPhaseId() != PhaseId.INVOKE_APPLICATION) {
            throw new IllegalArgumentException(listener.getPhaseId().toString());
        }

        this.actionListener = listener;
    }

    /**
     * <p>Return the {@link NavigationHandler} instance that will be passed
     * the outcome returned by any invoked {@link javax.faces.application.Action} for this
     * web application.	 The default implementation must provide the behavior
     * described in the {@link NavigationHandler} class description.</p>
     */
    public NavigationHandler getNavigationHandler() {
        if (null == navigationHandler) {
            navigationHandler = new NavigationHandlerImpl();
        }
        return navigationHandler;
    }

    /**
     * <p>Set the {@link NavigationHandler} instance that will be passed
     * the outcome returned by any invoked {@link javax.faces.application.Action} for this
     * web application.</p>
     *
     * @param handler The new {@link NavigationHandler} instance
     *
     * @exception NullPointerException if <code>handler</code>
     *	is <code>null</code>
     */
    public void setNavigationHandler(NavigationHandler handler) {
        if (handler == null) {
            throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        this.navigationHandler = handler;
    }

    /**
     * <p>Return the {@link PropertyResolver} instance that will be utilized
     * to resolve action and valus references.	The default implementation
     * must provide the behavior described in the
     * {@link PropertyResolver} class description.</p>
     */
    public PropertyResolver getPropertyResolver() {
        if (null == propertyResolver) {
            propertyResolver = new PropertyResolverImpl();
        }
        return propertyResolver;
    }

    /**
     * <p>Set the {@link PropertyResolver} instance that will be utilized
     * to resolve action and value references.</p>
     *
     * @param resolver The new {@link PropertyResolver} instance
     *
     * @exception NullPointerException if <code>resolver</code>
     *	is <code>null</code>
     */
    public void setPropertyResolver(PropertyResolver resolver) {
        if (resolver == null) {
            throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        this.propertyResolver = resolver;
    }


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
    public MethodBinding getMethodBinding(String ref, Class params[]) {

        if (ref == null) {
            throw new NullPointerException
                (Util.getExceptionMessage
                 (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
                                                                   
        } else {
            return (new MethodBindingImpl(this, ref, params));
        }

    }


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
     *	{@link ValueBinding} instance
     *
     * @exception NullPointerException if <code>ref</code>
     *	is <code>null</code>
     * @exception ReferenceSyntaxException if the specified <code>ref</code>
     *	has invalid syntax
     */
    public ValueBinding getValueBinding(String ref)
	throws ReferenceSyntaxException {
        if (ref == null) {
            throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // Check the ValueBinding cache for an existing binding
        ValueBinding valueBinding = (ValueBinding) valueBindingMap.get(ref);       
      
        if (valueBinding == null) {  
            // the binding doesn't exist.  Check the syntax of the reference
            // expression.  If the expression is valid, this has the side
            // effect of caching the expression in the ExpressionEvaluator.
            checkSyntax(ref);
            valueBinding = new ValueBindingImpl (this);
            ((ValueBindingImpl) valueBinding).setRef(ref);
            valueBindingMap.put(ref, valueBinding);
        }
        return valueBinding;
    }

    /**
     * <p>Return the {@link VariableResolver} instance that will be utilized
     * to resolve action and value references.	The default implementation
     * must provide the behavior described in the
     * {@link VariableResolver} class description.</p>
     */
    public VariableResolver getVariableResolver() {
        if (null == variableResolver) {
            variableResolver = new VariableResolverImpl();
        }
        return variableResolver;
    }

    /**
     * <p>Set the {@link VariableResolver} instance that will be utilized
     * to resolve action and value references.</p>
     *
     * @param resolver The new {@link VariableResolver} instance
     *
     * @exception NullPointerException if <code>resolver</code>
     *	is <code>null</code>
     */
    public void setVariableResolver(VariableResolver resolver) {
        if (resolver == null) {
            throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        this.variableResolver = resolver;
    }

    /**
     *<p>Register a new mapping of component type to the name of the 
     * {@link UIComponent} class.  Subsequent calls to
     * <code>createComponent()</code> will serve as a factory for {@link UIComponent}
     *  instances.
     *
     * @param componentType The component type to be registered
     * @param componentClass The fully qualified class name of the corresponding
     *  {@link UIComponent} implementation
     *
     * @exception NullPointerException if <code>componentType</code> or
     *  <code>componentClass</code> is <code>null</code>
     */
    public void addComponent(String componentType, String componentClass) {
	if (componentType == null || componentClass == null) {
	    throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	componentMap.put(componentType, componentClass);
    }

    /**
     * <p>Instantiate and return a new instance of {@link UIComponent} instance of the 
     * class specified by a previous call to <code>addComponent()</code> for the
     * specified component type.</p>
     * 
     * @param componentType The componentType that identifies the component instance
     *  that will be created and returned.
     *  
     * @return {@link UIComponent} instance
     *
     * @exception FacesException if a {@link UIComponent} of the specified type cannot
     *  be created.
     * @exception NullPointerException if <code>componentType</code> is <code>null</code>.
     */
    public UIComponent createComponent(String componentType) throws FacesException {
	if (componentType == null) {
	    throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	UIComponent returnVal = (UIComponent)newThing(componentType, componentMap);
	if (returnVal == null) {
            Object[] params = {componentType};
	    throw new FacesException(Util.getExceptionMessage(
                Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID,params));
	}
	return returnVal;
    }

    /**
     * <p>Instantiate and return a new instance of {@link UIComponent} instance
     * using either the component reference argument or the component type
     * argument.</p>
     *
     * @param componentRef The component reference that may identify the component
     *  instance that will be returned.
     * @param context The Faces context.
     * @param componentType The componentType that identifies the component instance
     *  that will be created and returned (if the componentref argument is not a
     *  {@link UIComponent} instance.
     *
     * @return {@link UIComponent} instance
     *
     * @exception FacesException if a {@link UIComponent} of the specified type cannot
     *  be created.
     * @exception NullPointerException if <code>componentType</code> is <code>null</code>.
     */
    public UIComponent createComponent(
            ValueBinding componentRef,
            FacesContext context,
            String componentType)
            throws FacesException {
        if (null == componentRef || null == context || null == componentType) {
            throw new NullPointerException(
                    Util.getExceptionMessage(
                            Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        Object result = null;
        boolean createOne = false;

        if (null != (result = componentRef.getValue(context))) {
            // if the result is not an instance of UIComponent
            createOne = (!(result instanceof UIComponent));
            // we have to create one.
        }
        if (null == result || createOne) {
            result = this.createComponent(componentType);
        }

        return (UIComponent) result;
    }

    /**
     * <p>Return an <code>Iterator</code> over the set of currently defined
     * component types for this <code>Application</code>.</p>
     *
     * @return Iteration of component types.
     */
    public Iterator getComponentTypes() {
	return componentMap.keySet().iterator();
    }

    /**
     *<p>Register a new mapping of converter id to the name of the 
     * {@link Converter} class.  Subsequent calls to
     * <code>createConverter()</code> will serve as a factory for {@link Converter}
     *  instances.
     *
     * @param converterId The converter identifier to be registered
     * @param converterClass The fully qualified class name of the corresponding
     *  {@link Converter} implementation
     *
     * @exception NullPointerException if <code>converterId</code> or
     *  <code>converterClass</code> is <code>null</code>
     */
    public void addConverter(String converterId, String converterClass) {
        if (converterId == null || converterClass == null) {
	    throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	converterIdMap.put(converterId, converterClass);
    }

    public void addConverter(Class targetClass, String converterClass) {
        if (targetClass == null || converterClass == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	converterTypeMap.put(targetClass, converterClass);
    }
    
    public Converter createConverter(String converterId) {
        if (converterId == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        Converter returnVal = (Converter)newThing(converterId, converterIdMap);	
        if (returnVal == null) {
            Object[] params = {converterId};
            throw new FacesException(Util.getExceptionMessage(
                Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID,params));
        }
        return returnVal;
    }
    
    public Converter createConverter(Class targetClass) {
        if (targetClass == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        Converter returnVal = (Converter)newThing
            (targetClass, converterTypeMap);

        if (returnVal != null) {
            return returnVal;
        }

        //Search for converters registered to interfaces implemented by
        //targetClass
        Class[] interfaces = targetClass.getInterfaces();
        if (interfaces != null ) {
            for (int i = 0; i < interfaces.length; i++) {
                returnVal = createConverter(interfaces[i]);
                if (returnVal != null) {
                    return returnVal;
                }
            }
        }

        //Search for converters registered to superclasses of targetClass
        Class superclass = targetClass.getSuperclass();
        if (superclass != null) {
            returnVal = createConverter(superclass);
            if (returnVal != null) {
                return returnVal;
            }
        }

        return returnVal;
    }

    /**
     * <p>Return an <code>Iterator</code> over the set of currently defined
     * converter identifiers for this <code>Application</code>.</p>
     *
     * @return Iteration of {@link Converter} identifiers.
     */
    public Iterator getConverterIds() {
        return converterIdMap.keySet().iterator();
    }

    public Iterator getConverterTypes() {
	return converterTypeMap.keySet().iterator();
    }

    ArrayList supportedLocales = null;
    public Iterator getSupportedLocales() {
	Iterator result = Collections.EMPTY_LIST.iterator();
	if (null != supportedLocales) {
	    result = supportedLocales.iterator();
	}
	return result;
    }

    public void setSupportedLocales(Collection newLocales) {
	if (null == newLocales) {
	    throw new NullPointerException();
	}
	supportedLocales = new ArrayList(newLocales);
    }

    protected Locale defaultLocale = null;
    public Locale getDefaultLocale(){
	Locale result = defaultLocale;
	if (null == defaultLocale) {
	    result = Locale.getDefault();
	}
	return result;
    }
    
    public void setDefaultLocale(Locale newLocale) {	
	    defaultLocale = newLocale;
    }
    
    /**
     *<p>Register a new mapping of validator id to the name of the 
     * {@link Validator} class.  Subsequent calls to
     * <code>createValidator()</code> will serve as a factory for {@link Validator}
     *  instances.
     *
     * @param validatorId The validator identifier to be registered
     * @param validatorClass The fully qualified class name of the corresponding
     *  {@link Validator} implementation
     *
     * @exception NullPointerException if <code>validatorId</code> or
     *  <code>validatorClass</code> is <code>null</code>
     */
    public void addValidator(String validatorId, String validatorClass) {
        if (validatorId == null || validatorClass == null) {
	    throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	validatorMap.put(validatorId, validatorClass);
    }

    /**
     * <p>Instantiate and return a new instance of {@link Validator} instance of the 
     * class specified by a previous call to <code>addValidator()</code> for the
     * specified validator identifier.</p>
     * 
     * @param validatorId The validatorId that identifies the validator instance
     *  that will be created and returned.
     *  
     * @return {@link Validator} instance.
     *
     * @exception FacesException if a {@link Validator} of the specified identifier
     * cannot be created.
     * @exception NullPointerException if <code>validatorId</code> is <code>null</code>.
     */
    public Validator createValidator(String validatorId) throws FacesException {
	if (validatorId == null) {
	    throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	Validator returnVal = (Validator)newThing(validatorId, validatorMap);
	if (returnVal == null) {
            Object[] params = {validatorId};
	    throw new FacesException(Util.getExceptionMessage(
                Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID,params));
	}
	return returnVal;
    }

    /**
     * <p>Return an <code>Iterator</code> over the set of currently defined
     * validator identifiers for this <code>Application</code>.</p>
     *
     * @return Iteration of {@link Validator} identifiers.
     */
    public Iterator getValidatorIds() {
        return validatorMap.keySet().iterator();
    }


    /**
     * This method adds a <code>messageResourcesId</code>, <code>messageResourcesClass</code>
     * mapping to the message resources map.
     *
     * @param messageResourcesId The message resources identifier.
     * @param messageResourcesClass The message resources class name.
     * @exception NullPointerException if either argument supplied to this
     *   method is null.
     */
    public void addMessageResources(String messageResourcesId, String messageResourcesClass) {
        if (messageResourcesId == null || messageResourcesClass == null) {
	    throw new NullPointerException(Util.getExceptionMessage(
	        Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	messageResourcesMap.put(messageResourcesId, messageResourcesClass);
    }

    /**
     * <p>This method returns a <code>MessageResources</code> instance given a 
     * <code>messageResourcesId</code>.  The value in the map must be either
     * a {@link MessageResources} instance, or a "class name" string.</p>
     *
     * @param messageResourcesId The identifier for the messages resource.
     * @return MessageResources The messages resource instance.
     * @exception FacesException
     * @exception NullPointerException if the identofier argument to this
     *    method is null.
     */
    public synchronized MessageResources getMessageResources(String messageResourcesId) 
        throws FacesException {
	if (messageResourcesId == null) {
	    throw new NullPointerException(Util.getExceptionMessage(
		Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}

	Object value = messageResourcesMap.get(messageResourcesId);

	//
	// If an instance already exists, return it..
	//
	if (value instanceof MessageResourcesImpl) { 
	    return ((MessageResourcesImpl)value);
	}
	
	String className = null;
	if (value == null) {
	    className = "com.sun.faces.application.MessageResourcesImpl";
	} else if (value instanceof String) {
	    className = (String)value;
	}
	    
	if (className != null) {
            Class clazz = null;
            Object result = null;
            try {
                clazz = Util.loadClass(className, className);
                Assert.assert_it(clazz != null);
	        result = clazz.newInstance();
            } catch (Throwable t) {
                Object[] params = {className};
                throw new FacesException(Util.getExceptionMessage(
		    Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params));
            }
	    messageResourcesMap.put(messageResourcesId, result);
	    return (MessageResources)result;
	}

	return null;
    }

    /**
     *<p>This method returns an iteration of message resource identifiers
     * contained in the message resources map.</p>
     *
     * @return Iterator the iteration of identifiers.
     */
    public Iterator getMessageResourcesIds() { 
        return messageResourcesMap.keySet().iterator();
    }

    // 
    // Methods leveraged by the RI only
    //

    /**
     *<p>Adds a new mapping of managed bean name to a managed bean
     * factory instance.</p>
     *
     * @param managedBeanName the name of the managed bean that will
     *  be created by the managed bean factory instance.
     * @param factory the managed bean factory instance.
     */
    public void addManagedBeanFactory(String managedBeanName,
        ManagedBeanFactory factory) {
        managedBeanFactoriesMap.put(managedBeanName, factory);
    }

    /**
     * <p>PRECONDITIONS: the values in the Map are either Strings
     * representing fully qualified java class names, or java.lang.Class
     * instances.</p>
     * <p>ALGORITHM: Look in the argument map for a value for the argument
     * key.  If found, if the value is instanceof String, assume the String
     * specifies a fully qualified java class name and obtain the
     * java.lang.Class instance for that String using Util.loadClass().
     * Replace the String instance in the argument map with the Class
     * instance.  If the value is instanceof Class, proceed.  Assert that the
     * value is either instanceof java.lang.Class or java.lang.String.</p>
     * <p>Now that you have a java.lang.class, call its newInstance and
     * return it as the result of this method.</p>
     *
     * @param key Used to look up the value in the <code>Map</code>.
     * @param map The <code>Map</code> that will be searched.
     *
     * @return The new object instance.
     */
    protected Object newThing(Object key, Map map) {
        Assert.assert_it(key != null);
        Assert.assert_it(map != null);
        Assert.assert_it(key instanceof String || key instanceof Class);

        Object result = null;
        Class clazz = null;
        Object value = map.get(key);

        if (value == null) {
	    return null;
        }
        Assert.assert_it(value instanceof String || value instanceof Class);
        if (value instanceof String) {
	    try {
                clazz = Util.loadClass((String)value, value);
                Assert.assert_it(clazz != null);
	        map.put(key, clazz);
	    } catch (Throwable t) {
                Object[] params = {t.getMessage()};
	        throw new FacesException(Util.getExceptionMessage(
		    Util.CANT_LOAD_CLASS_ERROR_MESSAGE_ID, params));
	    }
        } else {
            clazz = (Class)value;
        }

        try {
	    result = clazz.newInstance();
        } catch (Throwable t) {
            Object[] params = {clazz.getName()};
            throw new FacesException(Util.getExceptionMessage(
	        Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params));
        }
        return result;
    }

    /**
     *<p>The managedBeanFactories HashMap has been populated
     * with ManagedBeanFactory object keyed by the bean name.
     * Find the ManagedBeanFactory object and if it exists instantiate
     * the bean and store it in the appropriate scope, if any.</p>
     *
     * @param context The Faces context.
     * @param managedBeanName The name identifying the managed bean.
     *
     * @return The managed bean.
     *
     * @exception PropertyNotFoundException if the managed bean
     *  could not be created.
     */
    public Object createAndMaybeStoreManagedBeans(FacesContext context,
        String managedBeanName) throws PropertyNotFoundException {

        ManagedBeanFactory managedBean = (ManagedBeanFactory)
            managedBeanFactoriesMap.get(managedBeanName);
        if ( managedBean == null ) {
            return null;
        }

        Object bean;
        try {
            bean = managedBean.newInstance();
        } catch (Exception ex) {
            Object []params = {ex.getMessage()};
            throw new PropertyNotFoundException(Util.getExceptionMessage(
                Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params));
        }
        //add bean to appropriate scope
        String scope = managedBean.getScope();
        //scope cannot be null
        Assert.assert_it(null != scope);

        if (scope.equalsIgnoreCase(RIConstants.APPLICATION)) {
            context.getExternalContext().
                getApplicationMap().put(managedBeanName, bean);
        }
        else if (scope.equalsIgnoreCase(RIConstants.SESSION)) {
            Util.getSessionMap(context).put(managedBeanName, bean);
        }
        else if (scope.equalsIgnoreCase(RIConstants.REQUEST)) {
            context.getExternalContext().
                getRequestMap().put(managedBeanName, bean);
        }

        return bean;
    }
        
    private void checkSyntax(String ref) throws ReferenceSyntaxException {            
        try {                       
            ExpressionInfo exprInfo = new ExpressionInfo();
            exprInfo.setExpressionString(ref);            
            ExpressionEvaluator evaluator =
                Util.getExpressionEvaluator(RIConstants.FACES_RE_PARSER);
            // this will be cached so it won't have to be parsed again when
            // evaluated.
            evaluator.parseExpression(exprInfo);
        } catch (ElException elex) {
            // t will not be null if an error occurred.
            Throwable t = elex.getCause();
            if (t != null) {
                throw new ReferenceSyntaxException(t.getMessage(), t);
            }
            throw new ReferenceSyntaxException(elex.getMessage(), elex);
        }
    }
   
    // This is called by FacesContextImpl.responseRendered().
   void responseRendered() {
       responseRendered = true;
   }
   
    // The testcase for this class is com.sun.faces.application.TestApplicationImpl.java 
    // The testcase for this class is com.sun.faces.application.TestApplicationImpl_Config.java 

}
