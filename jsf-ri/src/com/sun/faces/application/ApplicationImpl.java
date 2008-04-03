/*
 * $Id: ApplicationImpl.java,v 1.100 2008/01/30 16:53:04 edburns Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.application;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.CompositeELResolver;
import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ResourceHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.application.ProjectStage;
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

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.el.ELUtils;
import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.VariableResolverImpl;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.Util;


/**
 * <p><strong>Application</strong> represents a per-web-application
 * singleton object where applications based on JavaServer Faces (or
 * implementations wishing to provide extended functionality) can
 * register application-wide singletons that provide functionality
 * required by JavaServer Faces.
 */
public class ApplicationImpl extends Application {

    // Log instance for this class
    private static final Logger logger = FacesLogger.APPLICATION.getLogger();

    private static final ELContextListener[] EMPTY_EL_CTX_LIST_ARRAY = { };

    private static final Map<String,Class<?>[]> STANDARD_CONV_ID_TO_TYPE_MAP =
         new HashMap<String,Class<?>[]>(8, 1.0f);
    private static final Map<Class<?>,String> STANDARD_TYPE_TO_CONV_ID_MAP =
         new HashMap<Class<?>,String>(16, 1.0f);

    static {
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Byte", new Class[] { Byte.TYPE, Byte.class});
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Boolean", new Class[] { Boolean.TYPE, Boolean.class});
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Character", new Class[] { Character.TYPE, Character.class});
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Short", new Class[] { Short.TYPE, Short.class });
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Integer", new Class[] { Integer.TYPE, Integer.class });
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Long", new Class[] { Long.TYPE, Long.class });
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Float", new Class[] { Float.TYPE, Float.class });
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Double", new Class[] { Double.TYPE, Double.class });
        for (Map.Entry<String,Class<?>[]> entry : STANDARD_CONV_ID_TO_TYPE_MAP.entrySet()) {
            Class<?>[] types = entry.getValue();
            String key = entry.getKey();
            for (Class<?> clazz : types) {
                STANDARD_TYPE_TO_CONV_ID_MAP.put(clazz, key);
            }
        }
    }

    // Relationship Instance Variables

    private ApplicationAssociate associate = null;
    private ProjectStage projectStage;

    private volatile ActionListener actionListener = null;
    private volatile NavigationHandler navigationHandler = null;
    private volatile PropertyResolverImpl propertyResolver = null;
    private volatile VariableResolverImpl variableResolver = null;
    private volatile ViewHandler viewHandler = null;
    private volatile ResourceHandler resourceHandler;
    private volatile StateManager stateManager = null;
    private volatile ArrayList<Locale> supportedLocales = null;
    private volatile Locale defaultLocale = null;
    //
    // This map stores reference expression | value binding instance
    // mappings.
    //
    
    //
    // These three maps store store "identifier" | "class name"
    // mappings.
    //
    private Map<String,Object> componentMap = null;
    private Map<String,Object> converterIdMap = null;
    private Map<Class,Object> converterTypeMap = null;
    private Map<String,Object> validatorMap = null;
    private volatile String messageBundle = null;

    private ArrayList<ELContextListener> elContextListeners = null;
    private ArrayList<ELResolver> elResolvers = null;
    private CompositeELResolver compositeELResolver = null;

    /**
     * Constructor
     */
    public ApplicationImpl() {
        super();
        associate = new ApplicationAssociate(this);
        componentMap = new ConcurrentHashMap<String, Object>();
        converterIdMap = new ConcurrentHashMap<String, Object>();
        converterTypeMap = new ConcurrentHashMap<Class, Object>();
        validatorMap = new ConcurrentHashMap<String, Object>();
        navigationHandler = new NavigationHandlerImpl(associate);
        propertyResolver = new PropertyResolverImpl();
        variableResolver = new VariableResolverImpl();

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Created Application instance ");
        }
    }

    public void addELContextListener(ELContextListener listener) {
        if (listener != null) {
            if (elContextListeners == null) {
                //noinspection CollectionWithoutInitialCapacity
                elContextListeners = new ArrayList<ELContextListener>();
            }
            elContextListeners.add(listener);
        }
    }
    
    public void removeELContextListener(ELContextListener listener) {
        if (listener != null && elContextListeners != null) {
            elContextListeners.remove(listener);
        }
    }
    
    public ELContextListener [] getELContextListeners() {
        if (elContextListeners != null ) {
            return (elContextListeners.toArray(
                       new ELContextListener[elContextListeners.size()]));
        } else {
            return (EMPTY_EL_CTX_LIST_ARRAY);
        }
    }
   
    public ExpressionFactory getExpressionFactory() {
        return associate.getExpressionFactory();
    }

    public Object evaluateExpressionGet(FacesContext context, 
        String expression, Class expectedType) throws ELException {
        ValueExpression ve = 
          getExpressionFactory().createValueExpression(context.getELContext(), 
                expression,expectedType);     
        return (ve.getValue(context.getELContext()));
    }
    
    public UIComponent createComponent(ValueExpression componentExpression,
        FacesContext context, String componentType) throws FacesException {
    	if (null == componentExpression) {
    		String message = MessageUtils.getExceptionMessageString
    		(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "componentExpression");
    		throw new NullPointerException(message);
    	}
    	if (null == context) {
    		String message = MessageUtils.getExceptionMessageString
    		(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
    		throw new NullPointerException(message);
    	}
    	if (null == componentType) {
    		String message = MessageUtils.getExceptionMessageString
    		(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "componentType");
    		throw new NullPointerException(message);
    	}

        Object result;
        boolean createOne = false;

        try {
            if (null != (result = 
                componentExpression.getValue(context.getELContext()))) {
                // if the result is not an instance of UIComponent
                createOne = (!(result instanceof UIComponent));
                // we have to create one.
            }
            if (null == result || createOne) {
                result = this.createComponent(componentType);
                componentExpression.setValue((context.getELContext()), result);
            }
        } catch (Exception ex) {
            throw new FacesException(ex);
        }

        return (UIComponent) result;    
    }

    public ELResolver getELResolver() {

        if (compositeELResolver == null) {
            compositeELResolver =
                 new FacesCompositeELResolver(
                      FacesCompositeELResolver.ELResolverChainType.Faces);
            ELUtils.buildFacesResolver(compositeELResolver, associate);
        }

        return compositeELResolver;

    }
    
    public void addELResolver(ELResolver resolver) {

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "ELResolver"));
        }

        if (resolver != null) {
            if (elResolvers == null) {
                //noinspection CollectionWithoutInitialCapacity
                elResolvers = new ArrayList<ELResolver>();
            }
            elResolvers.add(resolver);
        }

    }


    /**
     * @see javax.faces.application.Application#getProjectStage() 
     */
    @Override
    public ProjectStage getProjectStage() {
        
        if (projectStage == null) {
            WebConfiguration webConfig =
                  WebConfiguration.getInstance(
                        FacesContext.getCurrentInstance().getExternalContext());
            String value =
                  webConfig.getOptionValue(WebContextInitParameter.JavaxFacesProjectStage);
            if (value != null) {
                try {
                    projectStage = ProjectStage.valueOf(value);
                } catch (IllegalArgumentException iae) {
                    projectStage = ProjectStage.getExtension(value);
                }
            }
            if (projectStage == null) {
                projectStage = ProjectStage.Production;
            }
        }
        return projectStage;

    }

    public List<ELResolver> getApplicationELResolvers() {
        return elResolvers;
    }
    
    public ActionListener getActionListener() {
        return actionListener;
    }


    public ViewHandler getViewHandler() {
        return viewHandler;
    }


    public synchronized void setViewHandler(ViewHandler handler) {

        if (handler == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "handler");
            throw new NullPointerException(message);
        }

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "ViewHandler"));
        }

        viewHandler = handler;
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, MessageFormat.format("set ViewHandler Instance to ''{0}''", viewHandler.getClass().getName()));
        }

    }


    /**
     * @see javax.faces.application.Application#getResourceHandler()
     * @return
     * @since 2.0
     */
    @Override
    public ResourceHandler getResourceHandler() {

        return resourceHandler;

    }


    /**
     * @see javax.faces.application.Application#setResourceHandler(javax.faces.application.ResourceHandler)
     * @since 2.0
     */
    @Override
    public synchronized void setResourceHandler(ResourceHandler resourceHandler) {

        if (resourceHandler == null) {
            String message = MessageUtils.getExceptionMessageString
                  (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "resourceHandler");
            throw new NullPointerException(message);
        }

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "ResourceHandler"));
        }

        this.resourceHandler = resourceHandler;
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE,
                       "set ResourceHandler Instance to ''{0}''",
                       resourceHandler.getClass().getName());
        }

    }


    public StateManager getStateManager() {
        return stateManager;
    }


    public synchronized void setStateManager(StateManager manager) {

        if (manager == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "manager");
            throw new NullPointerException(message);
        }

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "StateManager"));
        }

        stateManager = manager;
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, MessageFormat.format("set StateManager Instance to ''{0}''",
                                                        stateManager.getClass().getName()));
        }

    }


    public synchronized void setActionListener(ActionListener listener) {
        if (listener == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "listener");
            throw new NullPointerException(message);
        }

        this.actionListener = listener;

        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("set ActionListener Instance to ''{0}''",
                                             actionListener.getClass().getName()));
        }
    }


    /**
     * Return the <code>NavigationHandler</code> instance
     * installed present in this application instance.  If
     * an instance does not exist, it will be created.
     */
    public NavigationHandler getNavigationHandler() {
        return navigationHandler;
    }


    /**
     * Set a <code>NavigationHandler</code> instance for this
     * application instance.
     *
     * @param handler The <code>NavigationHandler</code> instance.
     */
    public synchronized void setNavigationHandler(NavigationHandler handler) {
        if (handler == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "handler");
            throw new NullPointerException(message);
        }

        this.navigationHandler = handler;

        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("set NavigationHandler Instance to ''{0}''",
                                             navigationHandler.getClass().getName()));
        }
    }

    @SuppressWarnings("deprecation")
    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }
    
    public ResourceBundle getResourceBundle(FacesContext context, String var) {
        if (null == context || null == var) {
            throw new FacesException("context or var is null.");
        }
        return associate.getResourceBundle(context, var);
    }

    @SuppressWarnings("deprecation")
    public void setPropertyResolver(PropertyResolver resolver) {

        if (resolver == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "resolver");
            throw new NullPointerException(message);
        }

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "PropertyResolver"));
        }

        propertyResolver.setDelegate(ELUtils.getDelegatePR(associate, true));
        associate.setLegacyPropertyResolver(resolver);
        propertyResolver = new PropertyResolverImpl();


        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("set PropertyResolver Instance to ''{0}''", resolver.getClass().getName()));
        }
    }

    @SuppressWarnings("deprecation")
    public MethodBinding createMethodBinding(String ref, Class params[]) {
        MethodExpression result;
        if (ref == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "ref");
            throw new NullPointerException(message);
        }
        if (!(ref.startsWith("#{") && ref.endsWith("}"))) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(MessageFormat.format("Expression ''{0}'' does not follow the syntax #{...}", ref));
            }
            throw new ReferenceSyntaxException(ref);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // return a MethodBinding that wraps a MethodExpression.
	    if (null == params) {
		params = RIConstants.EMPTY_CLASS_ARGS;
	    }
            result = 
                getExpressionFactory().
                    createMethodExpression(context.getELContext(), ref, null,
                    params);
        } catch (ELException elex) {
            throw new ReferenceSyntaxException(elex);
        }
        return (new MethodBindingMethodExpressionAdapter(result));
    }

    @SuppressWarnings("deprecation")
    public ValueBinding createValueBinding(String ref)
        throws ReferenceSyntaxException {
        if (ref == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "ref");
            throw new NullPointerException(message);
        }
        ValueExpression result;
        FacesContext context = FacesContext.getCurrentInstance();
         // return a ValueBinding that wraps a ValueExpression.
         try {
             result= getExpressionFactory().
                     createValueExpression(context.getELContext(),ref,
                     Object.class);     
         } catch (ELException elex) {
            throw new ReferenceSyntaxException(elex);
         } 
         return (new ValueBindingValueExpressionAdapter(result));
    }

    @SuppressWarnings("deprecation")
    public VariableResolver getVariableResolver() {       
        return variableResolver;
    }

    @SuppressWarnings("deprecation")
    public void setVariableResolver(VariableResolver resolver) {

        if (resolver == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "resolver");
            throw new NullPointerException(message);
        }

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "VariableResolver"));
        }

        variableResolver.setDelegate(ELUtils.getDelegateVR(associate, true));
        associate.setLegacyVariableResolver(resolver);
        variableResolver = new VariableResolverImpl();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("set VariableResolver Instance to ''{0}''",
                                             variableResolver.getClass().getName()));
        }
    }


    public void addComponent(String componentType, String componentClass) {
        if (componentType == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "componentType");
            throw new NullPointerException(message);
        }
        if (componentClass == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "componentClass");
            throw new NullPointerException(message);
        }
        
        componentMap.put(componentType, componentClass);
        
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("added component of type ''{0}'' and class ''{1}''",
                                             componentType,
                                             componentClass));
        }
    }


    public UIComponent createComponent(String componentType)
        throws FacesException {
        if (componentType == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "componentType");
            throw new NullPointerException(message);
        }
        UIComponent returnVal;
        try {
            returnVal = (UIComponent) newThing(componentType, componentMap);
        } catch (Exception ex) {     
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, 
                        "jsf.cannot_instantiate_component_error", componentType);                
            }
            throw new FacesException(ex);
        }
        if (returnVal == null) {
            Object[] params = {componentType};
            if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, 
                            "jsf.cannot_instantiate_component_error", params);
            }
            throw new FacesException(MessageUtils.getExceptionMessageString(
                    MessageUtils.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, MessageFormat.format("Created component with component type of ''{0}''",
                                                        componentType));
        }
        return returnVal;
    }

    @SuppressWarnings("deprecation")
    public UIComponent createComponent(ValueBinding componentBinding,
                                       FacesContext context,
                                       String componentType)
        throws FacesException {
        if (null == componentBinding) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "componentBinding");
            throw new NullPointerException(message);
        }
        if (null == context) {
                String message = MessageUtils.getExceptionMessageString
                    (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
                throw new NullPointerException(message);
            }
        if (null == componentType) {
                String message = MessageUtils.getExceptionMessageString
                    (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "componentType");
                throw new NullPointerException(message);
            }

        Object result;
        boolean createOne = false;
        try {
            if (null != (result = componentBinding.getValue(context))) {
                // if the result is not an instance of UIComponent
                createOne = (!(result instanceof UIComponent));
                // we have to create one.
            }
          
            if (null == result || createOne) {
                result = this.createComponent(componentType);
                componentBinding.setValue(context, result);
            }
        } catch (Exception ex) {
            throw new FacesException(ex);
        }
        return (UIComponent) result;
    }


    public Iterator<String> getComponentTypes() {

        return componentMap.keySet().iterator();

    }


    public void addConverter(String converterId, String converterClass) {
        if (converterId == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "converterId");
            throw new NullPointerException(message);
        }
        if (converterClass == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "converterClass");
            throw new NullPointerException(message);
        }
        
        converterIdMap.put(converterId, converterClass);

        Class<?>[] types = STANDARD_CONV_ID_TO_TYPE_MAP.get(converterId);
        if (types != null) {
            for (Class<?> clazz : types) {
                // go directly against map to prevent cyclic method calls
                converterTypeMap.put(clazz, converterClass);
                addPropertyEditorIfNecessary(clazz);
            }
        }
        
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("added converter of type ''{0}'' and class ''{1}''",
                                             converterId,
                                             converterClass));
        }
    }


    public void addConverter(Class targetClass, String converterClass) {
        if (targetClass == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "targetClass");
            throw new NullPointerException(message);
        }
        if (converterClass == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "converterClass");
            throw new NullPointerException(message);
        }

        String converterId = STANDARD_TYPE_TO_CONV_ID_MAP.get(targetClass);
        if (converterId != null) {
            addConverter(converterId, converterClass);
        } else {
            converterTypeMap.put(targetClass, converterClass);
            addPropertyEditorIfNecessary(targetClass);
        }                
        
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("added converter of class type ''{0}''", converterClass));
        }
    }
    
    private final String [] STANDARD_BY_TYPE_CONVERTER_CLASSES = {
      "java.math.BigDecimal",
      "java.lang.Boolean",
      "java.lang.Byte",
      "java.lang.Character",
      "java.lang.Double",
      "java.lang.Float",
      "java.lang.Integer",
      "java.lang.Long",
      "java.lang.Short",
      "java.lang.Enum"
    };
    
    /**
     * <p>To enable EL Coercion to use JSF Custom converters, this 
     * method will call <code>PropertyEditorManager.registerEditor()</code>,
     * passing the <code>ConverterPropertyEditor</code> class for the
     * <code>targetClass</code> if the target class is not one of the standard
     * by-type converter target classes.
     * @param targetClass the target class for which a PropertyEditory may or
     *  may not be created
     */
    
    private void addPropertyEditorIfNecessary(Class targetClass) {
        PropertyEditor editor = PropertyEditorManager.findEditor(targetClass);
        if (null != editor) {
            return;
        }
        String className = targetClass.getName();
        // Don't add a PropertyEditor for the standard by-type converters.
        if (targetClass.isPrimitive()) {
            return;
        }
        for (String standardClass : STANDARD_BY_TYPE_CONVERTER_CLASSES) {
            if (-1 != standardClass.indexOf(className)) {
                return;
            }
        }
        Class editorClass = ConverterPropertyEditorFactory.getDefaultInstance().definePropertyEditorClassFor(targetClass);
        if (editorClass != null) {
            PropertyEditorManager.registerEditor(targetClass, editorClass);
        } else {
            logger.warning(MessageFormat.format("definePropertyEditorClassFor({0}) returned null.", targetClass.getName()));
        }
    }


    public Converter createConverter(String converterId) {
        if (converterId == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "convertedId");
            throw new NullPointerException(message);
        }
        Converter returnVal = (Converter) newThing(converterId, converterIdMap);
        if (returnVal == null) {
            Object[] params = {converterId};
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, 
                        "jsf.cannot_instantiate_converter_error", converterId);
            }
            throw new FacesException(MessageUtils.getExceptionMessageString(
                MessageUtils.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("created converter of type ''{0}''", converterId));
        }
        return returnVal;
    }


    public Converter createConverter(Class targetClass) {
        if (targetClass == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "targetClass");
            throw new NullPointerException(message);
        }
        Converter returnVal = (Converter) newConverter(targetClass,
                                                   converterTypeMap,targetClass);
        if (returnVal != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                 returnVal.getClass().getName()));
            }
            return returnVal;
        } 

        //Search for converters registered to interfaces implemented by
        //targetClass
        Class[] interfaces = targetClass.getInterfaces();
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                returnVal = createConverterBasedOnClass(interfaces[i], targetClass);
                if (returnVal != null) {
                   if (logger.isLoggable(Level.FINE)) {
                       logger.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                        returnVal.getClass().getName()));
                    }
                    return returnVal;
                }
            }
        }

        //Search for converters registered to superclasses of targetClass
        Class superclass = targetClass.getSuperclass();
        if (superclass != null) {
            returnVal = createConverterBasedOnClass(superclass, targetClass);
            if (returnVal != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                     returnVal.getClass().getName()));
                }
                return returnVal;
            }
        } 
        return returnVal;
    }

    protected Converter createConverterBasedOnClass(Class targetClass, 
            Class baseClass) {
        
        Converter returnVal = (Converter) newConverter(targetClass,
                converterTypeMap, baseClass);
        if (returnVal != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                 returnVal.getClass().getName()));
            }
            return returnVal;
        } 

        //Search for converters registered to interfaces implemented by
        //targetClass
        Class[] interfaces = targetClass.getInterfaces();
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                returnVal = createConverterBasedOnClass(interfaces[i], null);
                if (returnVal != null) {
                   if (logger.isLoggable(Level.FINE)) {
                       logger.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                        returnVal.getClass().getName()));
                    }
                    return returnVal;
                }
            }
        }

        //Search for converters registered to superclasses of targetClass
        Class superclass = targetClass.getSuperclass();
        if (superclass != null) {
            returnVal = createConverterBasedOnClass(superclass, null);
            if (returnVal != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                     returnVal.getClass().getName()));
                }
                return returnVal;
            }
        } 
        return returnVal;
    }

    public Iterator<String> getConverterIds() {
       
        return converterIdMap.keySet().iterator();
        
    }


    public Iterator<Class> getConverterTypes() {
                
        return converterTypeMap.keySet().iterator();
        
    }


    public Iterator<Locale> getSupportedLocales() {

            if (null != supportedLocales) {
                return supportedLocales.iterator();
            } else {
                return Collections.<Locale>emptyList().iterator();
            }

    }


    public synchronized void setSupportedLocales(Collection<Locale> newLocales) {
        if (null == newLocales) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "newLocales");
            throw new NullPointerException(message);
        }

        supportedLocales = new ArrayList<Locale>(newLocales);

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, MessageFormat.format("set Supported Locales ''{0}''",
                                                        supportedLocales.toString()));
        }
    }


    public Locale getDefaultLocale() {
        return defaultLocale;
    }


    public synchronized void setDefaultLocale(Locale locale) {

        if (locale == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "locale");
            throw new NullPointerException(message);
        }

        defaultLocale = locale;

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, (MessageFormat.format("set defaultLocale ''{0}''",
                                                         defaultLocale.getClass().getName())));
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
        if (validatorId == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "validatorId");
            throw new NullPointerException(message);
        }
        if (validatorClass == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "validatorClass");
            throw new NullPointerException(message);
        }
        
        validatorMap.put(validatorId, validatorClass);
        
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("added validator of type ''{0}'' class ''{1}''",
                                             validatorId,
                                             validatorClass));
        }
    }


    public Validator createValidator(String validatorId) throws FacesException {
        if (validatorId == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "validatorId");
            throw new NullPointerException(message);
        }
        Validator returnVal = (Validator) newThing(validatorId, validatorMap);
        if (returnVal == null) {
            Object[] params = {validatorId};
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, 
                        "jsf.cannot_instantiate_validator_error", params);
            }
            throw new FacesException(MessageUtils.getExceptionMessageString(
                MessageUtils.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(MessageFormat.format("created validator of type ''{0}''",
                                             validatorId));
        }
        return returnVal;
    }


    public Iterator<String> getValidatorIds() {        
        
        return validatorMap.keySet().iterator();
               
    }


    public synchronized void setMessageBundle(String messageBundle) {

        this.messageBundle = messageBundle;

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, MessageFormat.format("set messageBundle ''{0}''",
                                                        messageBundle));
        }
    }


    public String getMessageBundle() {
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
    protected Object newThing(String key, Map<String, Object> map) {
        assert (key != null && map != null);

        Object result;
        Class clazz;
        Object value;

        value = map.get(key);
        if (value == null) {
            return null;
        }
        assert (value instanceof String || value instanceof Class);
        if (value instanceof String) {
            try {
                clazz = Util.loadClass((String) value, value);
                assert (clazz != null);
                map.put(key, clazz);
            } catch (Throwable t) {
                throw new FacesException(t.getMessage(), t);
            }
        } else {
            clazz = (Class) value;
        }
        
        try {
            result = clazz.newInstance();
        } catch (Throwable t) {
            throw new FacesException((MessageUtils.getExceptionMessageString(
                  MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,
                  clazz.getName())), t);
        }
        return result;
    }
    
    /**
     * <p>The same as newThing except that a single argument constructor
     * that accepts a Class is looked for before calling the no-arg version.</p>
     *
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
     * @param targetClass the target class for the single argument ctor
     * @return The new object instance.
     */
    protected Object newConverter(Class key, Map<Class,Object> map, Class targetClass) {
        assert (key != null && map != null);

        Object result = null;
        Class clazz;
        Object value;

        value = map.get(key);
        if (value == null) {
            return null;
        }
        assert (value instanceof String || value instanceof Class);
        if (value instanceof String) {
            try {
                clazz = Util.loadClass((String) value, value);
                assert (clazz != null);
                map.put(key, clazz);
            } catch (Throwable t) {
                throw new FacesException(t.getMessage(), t);
            }
        } else {
            clazz = (Class) value;
        }
        
        Constructor ctor = 
              ReflectionUtils.lookupConstructor(clazz, Class.class);
        Throwable cause = null;
        if (ctor != null) {
            try {
                result = ctor.newInstance(targetClass);
            } catch (Exception e) {
                cause = e;
            }
        } else {
            try {
                result = clazz.newInstance();
            } catch (Exception e) {
                cause = e;
            }
        }       
        
        if (null != cause) {           
            throw new FacesException((MessageUtils.getExceptionMessageString(
                    MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, 
                    clazz.getName())), cause);
            
        }
        return result;
    }
    
    
    ApplicationAssociate getAssociate() {
        return associate;
    }


    // The testcase for this class is
    // com.sun.faces.application.TestApplicationImpl.java

    // The testcase for this class is
    // com.sun.faces.application.TestApplicationImpl_Config.java
}
