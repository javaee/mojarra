/*
 * $Id: AppConfig.java,v 1.1 2003/05/01 06:20:37 eburns Exp $
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
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.el.PropertyNotFoundException;
import com.sun.faces.config.ManagedBeanFactory;
import com.sun.faces.config.ConfigBase;
import com.sun.faces.config.ConfigComponent;
import com.sun.faces.util.Util;

/**
 *
 *  <p>AppConfig is a helper class to the ApplicationImpl that serves as
 *  a shim between it and the config system.</p>
 *
 * @version $Id: AppConfig.java,v 1.1 2003/05/01 06:20:37 eburns Exp $
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
    private static final String APPLICATION = "application";
    private static final String SESSION = "session";
    private static final String REQUEST = "request";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

    protected ConfigBase yourBase; // are belong to us
    protected HashMap managedBeanFactories;

//
// Constructors and Initializers    
//

public AppConfig()
{
    super();
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

        if (scope.equalsIgnoreCase(APPLICATION)) {
            context.getExternalContext().
                getApplicationMap().put(managedBeanName, bean);
        }
        else if (scope.equalsIgnoreCase(SESSION)) {
            context.getExternalContext().
                getSessionMap().put(managedBeanName, bean);
        }
        else if (scope.equalsIgnoreCase(REQUEST)) {
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
     * Clear Hashmap when ConfigFile is reparsed.
     */
    public void clearManagedBeanFactories() {
        managedBeanFactories = new HashMap();
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

// The testcase for this class is com.sun.faces.application.TestAppConfig.java 
// The testcase for this class is com.sun.faces.application.TestApplicationImpl_Config.java 


} // end of class AppConfig
