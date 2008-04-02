/*
 * $Id: ConfigBase.java,v 1.2 2003/04/29 20:51:31 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


import com.sun.faces.application.ApplicationImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;


/**
 * <p>Base bean for parsing configuration information.</p>
 */
public class ConfigBase {


    // ---------------------------------------------------------- <application>


    private String actionListener = null;
    public String getActionListener() {
        return (this.actionListener);
    }
    public void setActionListener(String actionListener) {
        this.actionListener = actionListener;
    }

    private String navigationHandler = null;
    public String getNavigationHandler() {
        return (this.navigationHandler);
    }
    public void setNavigationHandler(String navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    private String propertyResolver = null;
    public String getPropertyResolver() {
        return (this.propertyResolver);
    }
    public void setPropertyResolver(String propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    private String variableResolver = null;
    public String getVariableResolver() {
        return (this.variableResolver);
    }
    public void setVariableResolver(String variableResolver) {
        this.variableResolver = variableResolver;
    }


    // ------------------------------------------------------------ <component>


    private Map components = null;
    public void addComponent(ConfigComponent component) {
        if (components == null) {
            components = new HashMap();
        }
        components.put(component.getComponentType(), component);
    }
    public Map getComponents() {
        if (components == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.components);
        }
    }


    // ------------------------------------------------------------ <converter>


    private Map converters = null;
    public void addConverter(ConfigConverter converter) {
        if (converters == null) {
            converters = new HashMap();
        }
        converters.put(converter.getConverterId(), converter);
    }
    public Map getConverters() {
        if (converters == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.converters);
        }
    }


    // ------------------------------------------------------------ <validator>


    private Map validators = null;
    public void addValidator(ConfigValidator validator) {
        if (validators == null) {
            validators = new HashMap();
        }
        validators.put(validator.getValidatorId(), validator);
    }
    public Map getValidators() {
        if (validators == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.validators);
        }
    }

    // ------------------------------------------------------------ <object manager>

    private Map managedBeans = null;
    public void addManagedBean(ConfigManagedBean managedBean) {
        if (managedBeans == null) {
            managedBeans = new HashMap();
        }
        managedBeans.put(managedBean.getManagedBeanId(), managedBean);

        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
            FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
        ManagedBeanFactory mbf = new ManagedBeanFactory(managedBean);
        application.addManagedBeanFactory(managedBean.getManagedBeanId(), mbf);
    }
    public Map getManagedBeans() {
        if (managedBeans == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.managedBeans);
        }
    }    
}
