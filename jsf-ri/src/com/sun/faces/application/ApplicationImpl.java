/*
 * $Id: ApplicationImpl.java,v 1.52 2004/07/15 18:08:31 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.el.MethodBindingImpl;
import com.sun.faces.el.MixedELValueBinding;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.ValueBindingImpl;
import com.sun.faces.el.VariableResolverImpl;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.ExpressionEvaluator;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


/**
 * <p><strong>Application</strong> represents a per-web-application
 * singleton object where applications based on JavaServer Faces (or
 * implementations wishing to provide extended functionality) can
 * register application-wide singletons that provide functionality
 * required by JavaServer Faces.
 */
public class ApplicationImpl extends Application {

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ApplicationImpl.class);

    // Relationship Instance Variables

    private ApplicationAssociate associate = null;

    private ActionListener actionListener = null;
    private NavigationHandler navigationHandler = null;
    private PropertyResolver propertyResolver = null;
    private VariableResolver variableResolver = null;
    private ViewHandler viewHandler = null;
    private StateManager stateManager = null;
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
    private String messageBundle = null;

//
// Constructors and Initializers
//

    /**
     * Constructor
     */
    public ApplicationImpl() {
        super();
	associate = new ApplicationAssociate(this);
        valueBindingMap = new HashMap();
        componentMap = new HashMap();
        converterIdMap = new HashMap();
        converterTypeMap = new HashMap();
        validatorMap = new HashMap();

        if (log.isDebugEnabled()) {
            log.debug("Created Application instance ");
        }
    }


    public ActionListener getActionListener() {
        return actionListener;
    }


    public ViewHandler getViewHandler() {
        return viewHandler;
    }


    public void setViewHandler(ViewHandler handler) {
        if (handler == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" ViewHandler " + handler;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            if (associate.isResponseRendered()) {
                // at least one response has been rendered.
                if (log.isErrorEnabled()) {
                    log.error(
                        "Response for this request has been rendered already ");
                }
                throw new IllegalStateException(Util.getExceptionMessageString(
                    Util.ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER_ID));
            }
            viewHandler = handler;
            if (log.isDebugEnabled()) {
                log.debug("set ViewHandler Instance to " + viewHandler);
            }
        }
    }


    public StateManager getStateManager() {
        return stateManager;
    }


    public void setStateManager(StateManager manager) {
        if (manager == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " StateManager " + manager;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            if (associate.isResponseRendered()) {
                // at least one response has been rendered.
                if (log.isErrorEnabled()) {
                    log.error(
                        "Response for this request has been rendered already ");
                }
                throw new IllegalStateException(Util.getExceptionMessageString(
                    Util.ILLEGAL_ATTEMPT_SETTING_STATEMANAGER_ID));
            }
            stateManager = manager;
            if (log.isDebugEnabled()) {
                log.debug("set StateManager Instance to " + stateManager);
            }
        }
    }


    public void setActionListener(ActionListener listener) {
        if (listener == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" ActionListener " + listener;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            this.actionListener = listener;
        }
        if (log.isDebugEnabled()) {
            log.debug("set ActionListener Instance to " + actionListener);
        }
    }


    /**
     * Return the <code>NavigationHandler</code> instance
     * installed present in this application instance.  If
     * an instance does not exist, it will be created.
     */
    public NavigationHandler getNavigationHandler() {
        synchronized (this) {
            if (null == navigationHandler) {
                navigationHandler = new NavigationHandlerImpl();
            }
        }
        return navigationHandler;
    }


    /**
     * Set a <code>NavigationHandler</code> instance for this
     * application instance.
     *
     * @param handler The <code>NavigationHandler</code> instance.
     */
    public void setNavigationHandler(NavigationHandler handler) {
        if (handler == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" NavigationHandler " + handler;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            this.navigationHandler = handler;
        }
        if (log.isDebugEnabled()) {
            log.debug("set NavigationHandler Instance to " + navigationHandler);
        }
    }


    public PropertyResolver getPropertyResolver() {
        synchronized (this) {
            if (null == propertyResolver) {
                propertyResolver = new PropertyResolverImpl();
            }
        }
        return propertyResolver;
    }


    public void setPropertyResolver(PropertyResolver resolver) {
        if (resolver == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" PropertyResolver " + resolver;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            this.propertyResolver = resolver;
        }
        if (log.isDebugEnabled()) {
            log.debug("set PropertyResolver Instance to " + propertyResolver);
        }
    }


    public MethodBinding createMethodBinding(String ref, Class params[]) {

        if (ref == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" ref " + ref;
            throw new NullPointerException(message);

        } else {
            return (new MethodBindingImpl(this, ref, params));
        }

    }


    public ValueBinding createValueBinding(String ref)
        throws ReferenceSyntaxException {
        ValueBinding valueBinding = null;
        if (ref == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" ref " + ref;
            throw new NullPointerException(message);
        } else {
            if (!Util.isVBExpression(ref)) {
                if (log.isErrorEnabled()) {
                    log.error(" Expression " + ref +
                              " does not follow the JSF EL syntax ");
                }
                throw new ReferenceSyntaxException(ref);
            }

            // is this a Mixed expression?
            if (Util.isMixedVBExpression(ref)) {
                valueBinding = new MixedELValueBinding();
            } else {
                // PENDING: Need to impelement the performance enhancement
                // suggested by Hans in the EG on 17 November 2003.
                ref = Util.stripBracketsIfNecessary(ref);
                checkSyntax(ref);
                valueBinding = new ValueBindingImpl(this);
            }
            ((ValueBindingImpl) valueBinding).setRef(ref);
        }
        return valueBinding;
    }


    public VariableResolver getVariableResolver() {
        synchronized (this) {
            if (null == variableResolver) {
                variableResolver = new VariableResolverImpl();
            }
        }
        return variableResolver;
    }


    public void setVariableResolver(VariableResolver resolver) {
        if (resolver == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" VariableResolver " + resolver;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            this.variableResolver = resolver;
        }
        if (log.isDebugEnabled()) {
            log.debug("set VariableResolver Instance to " + variableResolver);
        }
    }


    public void addComponent(String componentType, String componentClass) {
        if (componentType == null || componentClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" componentType " + componentType +
                " componentClass " + componentClass;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            componentMap.put(componentType, componentClass);
        }
        if (log.isTraceEnabled()) {
            log.trace("added component of type " + componentType +
                      " class " + componentClass);
        }
    }


    public UIComponent createComponent(String componentType)
        throws FacesException {
        if (componentType == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" componentType " + componentType;
            throw new NullPointerException(message);
        }
        UIComponent returnVal = (UIComponent) newThing(componentType,
                                                       componentMap);
        if (returnVal == null) {
            if (log.isErrorEnabled()) {
                log.error(
                    "Could not instantiate component of type " + componentType);
            }
            Object[] params = {componentType};
            throw new FacesException(Util.getExceptionMessageString(
                Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (log.isTraceEnabled()) {
            log.trace("Created component " + componentType);
        }
        return returnVal;
    }


    public UIComponent createComponent(ValueBinding componentBinding,
                                       FacesContext context,
                                       String componentType)
        throws FacesException {
        if (null == componentBinding || null == context ||
            null == componentType) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" componentBinding " + componentBinding +
                " context " + context + " componentType " + componentType;
            throw new NullPointerException(message);
        }

        Object result = null;
        boolean createOne = false;

        if (null != (result = componentBinding.getValue(context))) {
            // if the result is not an instance of UIComponent
            createOne = (!(result instanceof UIComponent));
            // we have to create one.
        }
        if (null == result || createOne) {
            result = this.createComponent(componentType);
            componentBinding.setValue(context, result);
        }

        return (UIComponent) result;
    }


    public Iterator getComponentTypes() {
        Iterator result = Collections.EMPTY_LIST.iterator();
        synchronized (this) {
            result = componentMap.keySet().iterator();
        }

        return result;
    }


    public void addConverter(String converterId, String converterClass) {
        if (converterId == null || converterClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " converterId " + converterId +
                " converterClass " + converterClass;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            converterIdMap.put(converterId, converterClass);
        }
        if (log.isTraceEnabled()) {
            log.trace("added converter of type " + converterId +
                      " and class " + converterClass);
        }
    }


    public void addConverter(Class targetClass, String converterClass) {
        if (targetClass == null || converterClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" targetClass " + targetClass +
                " converterClass " + converterClass;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            converterTypeMap.put(targetClass, converterClass);
        }
        if (log.isTraceEnabled()) {
            log.trace("added converter of class type " + converterClass);
        }
    }


    public Converter createConverter(String converterId) {
        if (converterId == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" converterId " + converterId;
            throw new NullPointerException(message);
        }
        Converter returnVal = (Converter) newThing(converterId, converterIdMap);
        if (returnVal == null) {
            if (log.isErrorEnabled()) {
                log.error("Couldn't instantiate converter of the type " +
                          converterId);
            }
            Object[] params = {converterId};
            throw new FacesException(Util.getExceptionMessageString(
                Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (log.isTraceEnabled()) {
            log.trace("created converter of type " + converterId);
        }
        return returnVal;
    }


    public Converter createConverter(Class targetClass) {
        if (targetClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" targetClass " + targetClass;
            throw new NullPointerException(message);
        }
        Converter returnVal = (Converter) newThing(targetClass,
                                                   converterTypeMap);

        if (returnVal != null) {
            if (log.isTraceEnabled()) {
                log.trace("Created converter of type " +
                          returnVal.getClass().getName());
            }
            return returnVal;
        }

        //Search for converters registered to interfaces implemented by
        //targetClass
        Class[] interfaces = targetClass.getInterfaces();
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                returnVal = createConverter(interfaces[i]);
                if (returnVal != null) {
                    if (log.isTraceEnabled()) {
                        log.trace("Created converter of type " +
                                  returnVal.getClass().getName());
                    }
                    return returnVal;
                }
            }
        }

        //Search for converters registered to superclasses of targetClass
        Class superclass = targetClass.getSuperclass();
        if (superclass != null) {
            returnVal = createConverter(superclass);
            if (returnVal != null) {
                if (log.isTraceEnabled()) {
                    log.trace("Created converter of type " +
                              returnVal.getClass().getName());
                }
                return returnVal;
            }
        }

        return returnVal;
    }


    public Iterator getConverterIds() {
        Iterator result = Collections.EMPTY_LIST.iterator();
        synchronized (this) {
            result = converterIdMap.keySet().iterator();
        }

        return result;
    }


    public Iterator getConverterTypes() {
        Iterator result = Collections.EMPTY_LIST.iterator();
        synchronized (this) {
            result = converterTypeMap.keySet().iterator();
        }
        return result;
    }


    ArrayList supportedLocales = null;


    public Iterator getSupportedLocales() {
        Iterator result = Collections.EMPTY_LIST.iterator();

        synchronized (this) {
            if (null != supportedLocales) {
                result = supportedLocales.iterator();
            }
        }
        return result;
    }


    public void setSupportedLocales(Collection newLocales) {
        if (null == newLocales) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" newLocales " + newLocales;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            supportedLocales = new ArrayList(newLocales);
        }
        if (log.isTraceEnabled()) {
            log.trace("set Supported Locales");
        }
    }


    protected Locale defaultLocale = null;


    public Locale getDefaultLocale() {
        return defaultLocale;
    }


    public void setDefaultLocale(Locale locale) {

        if (locale == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " locale " + locale;
            throw new NullPointerException(message);
        }

        synchronized (this) {
            defaultLocale = locale;
        }
        if (log.isTraceEnabled()) {
            log.trace("set defaultLocale " + defaultLocale);
        }

    }


    protected String defaultRenderKitId = null;


    public String getDefaultRenderKitId() {
        return defaultRenderKitId;
    }


    public void setDefaultRenderKitId(String renderKitId) {
        defaultRenderKitId = renderKitId;
    }


    public void addValidator(String validatorId, String validatorClass) {
        if (validatorId == null || validatorClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " validatorId " + validatorId + 
                " validatorClass " + validatorClass;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            validatorMap.put(validatorId, validatorClass);
        }
        if (log.isTraceEnabled()) {
            log.trace("added validator of type " + validatorId +
                      " class " + validatorClass);
        }
    }


    public Validator createValidator(String validatorId) throws FacesException {
        if (validatorId == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" validatorId " + validatorId;
            throw new NullPointerException(message);
        }
        Validator returnVal = (Validator) newThing(validatorId, validatorMap);
        if (returnVal == null) {
            if (log.isErrorEnabled()) {
                log.error("Couldn't instantiate Validator of the type " +
                          validatorId);
            }
            Object[] params = {validatorId};
            throw new FacesException(Util.getExceptionMessageString(
                Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (log.isTraceEnabled()) {
            log.trace("created validator of type " + validatorId);
        }
        return returnVal;
    }


    public Iterator getValidatorIds() {
        Iterator result = Collections.EMPTY_LIST.iterator();
        synchronized (this) {
            result = validatorMap.keySet().iterator();
        }
        return result;
    }


    public void setMessageBundle(String messageBundle) {
        synchronized (this) {
            this.messageBundle = messageBundle;
        }
        if (log.isTraceEnabled()) {
            log.trace("set messageBundle " + messageBundle);
        }
    }


    synchronized public String getMessageBundle() {
        return messageBundle;
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
     * @return The new object instance.
     */
    protected Object newThing(Object key, Map map) {
        Util.doAssert(key != null);
        Util.doAssert(map != null);
        Util.doAssert(key instanceof String || key instanceof Class);

        Object result = null;
        Class clazz = null;
        Object value = null;

        synchronized (this) {
            value = map.get(key);

            if (value == null) {
                return null;
            }
            Util.doAssert(value instanceof String || value instanceof Class);
            if (value instanceof String) {
                try {
                    clazz = Util.loadClass((String) value, value);
                    Util.doAssert(clazz != null);
                    map.put(key, clazz);
                } catch (Throwable t) {
                    Object[] params = {t.getMessage()};
                    throw new FacesException(Util.getExceptionMessageString(
                        Util.CANT_LOAD_CLASS_ERROR_MESSAGE_ID, params));
                }
            } else {
                clazz = (Class) value;
            }
        }

        try {
            result = clazz.newInstance();
        } catch (Throwable t) {
            Object[] params = {clazz.getName()};
            throw new FacesException(Util.getExceptionMessageString(
                Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params));
        }
        return result;
    }
    private void checkSyntax(String ref) throws ReferenceSyntaxException {
        try {
            ExpressionInfo exprInfo = new ExpressionInfo();
            exprInfo.setExpressionString(ref);
            ExpressionEvaluator evaluator =
                Util.getExpressionEvaluator();
            // this will be cached so it won't have to be parsed again when
            // evaluated.
            evaluator.parseExpression(exprInfo);
            if (log.isTraceEnabled()) {
                log.trace("Expression " + ref + " passed syntax check");
            }
        } catch (ElException elex) {
            if (log.isErrorEnabled()) {
                log.trace("Expression " + ref + " failed syntax check");
            }
            throw new ReferenceSyntaxException(ref, elex);
        }
    }


    // The testcase for this class is
    // com.sun.faces.application.TestApplicationImpl.java

    // The testcase for this class is
    // com.sun.faces.application.TestApplicationImpl_Config.java
}
