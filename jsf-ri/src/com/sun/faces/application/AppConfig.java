/*
 * $Id: AppConfig.java,v 1.8 2003/05/15 22:25:45 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// AppConfig.java

package com.sun.faces.application;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import java.util.HashMap;
import java.util.Iterator;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.context.MessageResources;
import javax.faces.validator.Validator;
import javax.faces.el.PropertyNotFoundException;
import com.sun.faces.config.ManagedBeanFactory;
import com.sun.faces.config.ConfigBase;
import com.sun.faces.config.ConfigComponent;
import com.sun.faces.config.ConfigConverter;
import com.sun.faces.config.ConfigMessageResources;
import com.sun.faces.config.ConfigValidator;
import com.sun.faces.util.Util;
import com.sun.faces.context.MessageResourcesImpl;
import com.sun.faces.RIConstants;

/**
 *
 *  <p>AppConfig is a helper class to the ApplicationImpl that serves as
 *  a shim between it and the config system.</p>
 *
 * @version $Id: AppConfig.java,v 1.8 2003/05/15 22:25:45 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class AppConfig extends Object
{
//
// Protected Constants
//
    private static final String JSF_API_RESOURCE_FILENAME = "com/sun/faces/context/JSFMessages";

    private static final String JSF_RI_RESOURCE_FILENAME = "com/sun/faces/context/JSFImplMessages";

    private Application application= null;

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

    protected ConfigBase yourBase; // are belong to us
    protected HashMap managedBeanFactories = null;
    protected HashMap messageResourcesList = null;

//
// Constructors and Initializers    
//

public AppConfig(Application application)
{
    super();
    reset();
    this.application = application;
}

//
// Class methods
//

//
// General Methods
//

    public void setConfigBase(ConfigBase newBase) {
	ParameterCheck.nonNull(newBase);
	yourBase = newBase;
        NavigationHandlerImpl navHandler = (NavigationHandlerImpl)application.getNavigationHandler();
        if (navHandler instanceof NavigationHandlerImpl) {
            ((NavigationHandlerImpl)navHandler).initialize(newBase);
        }
    }

    public ConfigBase getConfigBase() {
	return yourBase;
    }


    /**
     * The ConfigFile managed has populated the managedBeanFactories
     * HashMap with ManagedBeanFactory object keyed by the bean name.
     * Find the ManagedBeanFactory object and if it exists instantiate
     * the bean and store it in the appropriate scope, if any.
     */
    public Object createAndMaybeStoreManagedBeans(FacesContext context,
        String managedBeanName) throws PropertyNotFoundException {

        ManagedBeanFactory managedBean = (ManagedBeanFactory) 
            managedBeanFactories.get(managedBeanName);
        if ( managedBean == null ) {
            return null;
        }
    
        Object bean;
        try {
            bean = managedBean.newInstance();
        } catch (Exception ex) {
            //FIX_ME: I18N error message
            throw new PropertyNotFoundException("Error instantiating bean", ex);
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

    /**
     * ConfigFiles manager populates the managedBeanFactories
     * HashMap with ManagedBeanFactory Objects.
     */
    public void addManagedBeanFactory(String managedBeanName,
                                      ManagedBeanFactory factory) {

        managedBeanFactories.put(managedBeanName, factory);
    }

    /**
     * Reset all stored state.
     */
    public void reset() {
        managedBeanFactories = new HashMap();
        messageResourcesList = new HashMap();
    }

    //
    // Package private methods
    // 

    void addComponent(String componentType, String componentClass) {
	ParameterCheck.nonNull(componentType);
	ParameterCheck.nonNull(componentClass);
	Assert.assert_it(null != yourBase);

	ConfigComponent configComponent = new ConfigComponent();
	configComponent.setComponentType(componentType);
	configComponent.setComponentClass(componentClass);
	yourBase.addComponent(configComponent);
    }

    UIComponent getComponent(String componentType) throws FacesException {
	ParameterCheck.nonNull(componentType);
	Assert.assert_it(null != yourBase);
	
	UIComponent result = null;
	ConfigComponent configComponent = null;
	if (null == (configComponent = (ConfigComponent)
		     yourBase.getComponents().get(componentType))) {
	    //PENDING(edburns): i18n
	    throw new FacesException();
	}
	result = (UIComponent) 
	    this.newThing(configComponent.getComponentClass());
	return result;
    }

    void addConverter(String converterId, String converterClass) {
	ParameterCheck.nonNull(converterId);
	ParameterCheck.nonNull(converterClass);
	Assert.assert_it(null != yourBase);

	ConfigConverter configConverter = new ConfigConverter();
	configConverter.setConverterId(converterId);
	configConverter.setConverterClass(converterClass);
	yourBase.addConverter(configConverter);
    }

    Converter getConverter(String converterId) throws FacesException {
	ParameterCheck.nonNull(converterId);
	Assert.assert_it(null != yourBase);
	
	Converter result = null;
	ConfigConverter configConverter = null;
	if (null == (configConverter = (ConfigConverter)
		     yourBase.getConverters().get(converterId))) {
	    //PENDING(edburns): i18n
	    throw new FacesException();
	}
	result = (Converter) 
	    this.newThing(configConverter.getConverterClass());
	return result;
    }
    
    private Object newThing(String thingClassName) throws FacesException {
	//PENDING(edburns): i18n
	Class thingClass = null;
	Object result = null;
	try {
	    if (null == (thingClass = Util.loadClass(thingClassName,
						     this))) {
		throw new FacesException();
	    }
	    result = thingClass.newInstance();
	}
	catch (Throwable e) {
	    throw new FacesException(e);
	}
	return result;
    }

    Iterator getComponentTypes() {
	Assert.assert_it(null != yourBase);
	return yourBase.getComponents().keySet().iterator();
    }

    Iterator getConverterIds() {
	Assert.assert_it(null != yourBase);
	return yourBase.getConverters().keySet().iterator();
    }

    void addMessageResources(String messageResourcesId, String messageResourcesClass) {
	ParameterCheck.nonNull(messageResourcesId);
	ParameterCheck.nonNull(messageResourcesClass);
	Assert.assert_it(null != yourBase);

	ConfigMessageResources configMessageResources = new ConfigMessageResources();
	configMessageResources.setMessageResourcesId(messageResourcesId);
	configMessageResources.setMessageResourcesClass(messageResourcesClass);
	yourBase.addMessageResources(configMessageResources);
    }

    MessageResources getMessageResources(String messageResourcesId) 
        throws FacesException {
	ParameterCheck.nonNull(messageResourcesId);
	Assert.assert_it(null != yourBase);

	// check our local store first.
        if ( messageResourcesList.containsKey(messageResourcesId)) {
            return ((MessageResources)
                    messageResourcesList.get(messageResourcesId));
        } 

	// if not found, we have to create one.
        MessageResources result = null;
	ConfigMessageResources configMessageResources = null;
	
	if (null == (configMessageResources = (ConfigMessageResources)
		     yourBase.getMessageResources().get(messageResourcesId))) {
	    //PENDING(edburns): i18n
	    throw new FacesException();
	}
	result = (MessageResources) 
	    this.newThing(configMessageResources.getMessageResourcesClass());

	if (messageResourcesId.equals(MessageResources.FACES_API_MESSAGES)) {
	    ((MessageResourcesImpl)result).init(messageResourcesId, 
                    JSF_API_RESOURCE_FILENAME);
	}
	else if (messageResourcesId.equals(MessageResources.FACES_IMPL_MESSAGES)) {
	    ((MessageResourcesImpl)result).init(messageResourcesId, 
                    JSF_RI_RESOURCE_FILENAME);
	}
	else {
	    // we don't initialize it, just return it un-initialized.
	}
	
        synchronized ( messageResourcesList ) { 
            messageResourcesList.put(messageResourcesId, result);
        }    
	
	return result;
    }

    Iterator getMessageResourcesIds() { 
	Assert.assert_it(null != yourBase);
	return yourBase.getMessageResources().keySet().iterator();
    }

    void addValidator(String validatorId, String validatorClass) {
	ParameterCheck.nonNull(validatorId);
	ParameterCheck.nonNull(validatorClass);
	Assert.assert_it(null != yourBase);

	ConfigValidator configValidator = new ConfigValidator();
	configValidator.setValidatorId(validatorId);
	configValidator.setValidatorClass(validatorClass);
	yourBase.addValidator(configValidator);
    }

    Validator getValidator(String validatorId) 
        throws FacesException {
	ParameterCheck.nonNull(validatorId);
	Assert.assert_it(null != yourBase);

	Validator result = null;
	ConfigValidator configValidator = null;
	if (null == (configValidator = (ConfigValidator)
		     yourBase.getValidators().get(validatorId))) {
	    //PENDING(edburns): i18n
	    throw new FacesException();
	}
	result = (Validator) 
	    this.newThing(configValidator.getValidatorClass());
	
	return result;
    }

    Iterator getValidatorIds() { 
	Assert.assert_it(null != yourBase);
	return yourBase.getValidators().keySet().iterator();
    }
    
    

// The testcase for this class is com.sun.faces.application.TestAppConfig.java 
// The testcase for this class is com.sun.faces.application.TestApplicationImpl_Config.java 


} // end of class AppConfig
