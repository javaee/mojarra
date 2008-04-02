/*
 * $Id: ManagedBeanFactory.java,v 1.4 2003/05/08 23:13:04 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.faces.el.ValueBinding;
import javax.faces.el.PropertyNotFoundException;

import com.sun.faces.el.ValueBindingImpl;
import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;

import org.apache.commons.beanutils.PropertyUtils;


/**
 * This class clreates a managed bean instance. It has a contract with
 * the ConfigManagedBean class which is populated from the config file.
 * The bean instance is created lazily so a deep copy of the 
 * ConfigManagedBean is required.
 *
 * The Application implementation instantiated the beans as required and
 * stores them in the appropriate scope.
 *
 */
public class ManagedBeanFactory extends Object {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //
    ConfigManagedBean managedBean;
    String scope;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //
    /**
     * Constructor 
     */
    public ManagedBeanFactory(ConfigManagedBean managedBean) {
        //ConfigManagedBean clone method implemented to return deep copy
        this.managedBean = (ConfigManagedBean) managedBean.clone();
        scope = null;
    }

    public void setConfigManagedBean(ConfigManagedBean newBean) {
        //ConfigManagedBean clone method implemented to return deep copy
        this.managedBean = (ConfigManagedBean) newBean.clone();
        scope = null;
    }

    /**
     * Attempt to instantiate the JavaBean and set its properties.
     */
    public Object newInstance() throws FacesException {
        Object bean = null;

        if (managedBean.getManagedBeanCreate() != null) {
            if (managedBean.getManagedBeanCreate().equalsIgnoreCase("FALSE")) {
                return bean;
            }
        }

        //need to instantiate bean

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = FacesContext.getCurrentInstance().
                    getClass().getClassLoader();
            }
            bean = java.beans.Beans.instantiate(
                loader, managedBean.getManagedBeanClass());
        } catch (Exception ex) {
            Object[] obj = new Object[1];
            obj[0] = managedBean.getManagedBeanClass();
            throw new FacesException(Util.getExceptionMessage(Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj), ex);
        }

        //set the scope
        scope = managedBean.getManagedBeanScope();

        Map props = managedBean.getProperties();
        Iterator iter = props.keySet().iterator();
        ConfigManagedBeanProperty cmp = null;
        ConfigManagedBeanPropertyValue cmpv = null;
        Object value;
        
        while (iter.hasNext()) {
            value = null;
            cmp = (ConfigManagedBeanProperty)props.get((String)iter.next());
            if (cmp.hasValuesArray()) {
            List list = cmp.getValues();
            for (int i=0; i < list.size(); i++) {
                    cmpv = (ConfigManagedBeanPropertyValue)list.get(i);

                    //set the indexed property on the bean
                    if (cmpv.getValueCategory() == 
                        ConfigManagedBeanPropertyValue.VALUE_REF) {
                        value = getValueRef((String)cmpv.getValue());
                    } else {
                        value = cmpv.getValue();
                    }

                    try {
                        // if it's a class type do not set it
                        if (cmpv.getValueCategory() != 
                            ConfigManagedBeanPropertyValue.VALUE_CLASS) {
                            PropertyUtils.setIndexedProperty(
                                bean, 
                                cmp.getPropertyName(), 
                                i, 
                                value);
                        }
                    } catch (Exception ex) {
                        Object[] obj = new Object[1];
                        obj[0] = cmp.getPropertyName();
                        throw new FacesException(Util.getExceptionMessage(Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj), ex);
                    }
                }
            } else if (cmp.hasMapEntries()) {
                ConfigManagedPropertyMap cmpm = null;
                List list = cmp.getMapEntries();
                for (int i=0; i < list.size(); i++) {
                    cmpm = (ConfigManagedPropertyMap)list.get(i);

                    //set the mapped property on the bean
                    if (cmpm.getValueCategory() == 
                        ConfigManagedPropertyMap.VALUE_REF) {
                        value = getValueRef((String)cmpm.getValue());
                    } else {
                        value = cmpm.getValue();
                    }

                    try {
                        PropertyUtils.setMappedProperty(
                            bean, 
                             cmp.getPropertyName(), 
                            (String)cmpm.getKey(), 
                            value);
                    } catch (Exception ex) {
                        Object[] obj = new Object[1];
                        obj[0] = cmp.getPropertyName();
                        throw new FacesException(Util.getExceptionMessage(Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj), ex);
                    }
                }
            } else {
                cmpv = (ConfigManagedBeanPropertyValue)cmp.getValue();

                //find properties and set them on the bean
                if (cmpv.getValueCategory() == 
                    ConfigManagedBeanPropertyValue.VALUE_REF) {
                    value = getValueRef((String)cmpv.getValue());
                } else {
                    value = cmpv.getValue();
                }

                try {
                    // if it's a class type do not set it
                    if (cmpv.getValueCategory() != 
                        ConfigManagedBeanPropertyValue.VALUE_CLASS) {
                         PropertyUtils.setSimpleProperty(
                            bean, 
                            cmp.getPropertyName(), 
                            value);
                    }
                } catch (Exception ex) {
                    Object[] obj = new Object[1];
                    obj[0] = cmp.getPropertyName();
                    throw new FacesException(Util.getExceptionMessage(Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj), ex);
                }
            }

        }

        return bean;
    }

    public String getScope() {
        return scope;
    }

    private Object getValueRef(String value) throws FacesException {
        Object valueRef = null;

        if (!hasValidLifespan(value)) {
            Object[] obj = new Object[1];
            obj[0] = value;
            throw new FacesException(Util.getExceptionMessage(Util.INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID, obj));
        }

        ValueBinding binding = Util.getValueBinding(value);
        if (binding != null) {
            try {
                valueRef = binding.getValue(FacesContext.getCurrentInstance());
            } catch (PropertyNotFoundException ex) {  
                Object[] obj = new Object[1];
                obj[0] = value;
                throw new FacesException(Util.getExceptionMessage(Util.ERROR_GETTING_VALUEREF_VALUE_ERROR_MESSAGE_ID, obj));
            }
        } else {
            Object[] obj = new Object[1];
            obj[0] = value;
            throw new FacesException(Util.getExceptionMessage(Util.ERROR_GETTING_VALUE_BINDING_ERROR_MESSAGE_ID, obj));
        }
        return valueRef;
    }

    private boolean hasValidLifespan(String value) {
        ValueBindingImpl binding = (ValueBindingImpl)Util.getValueBinding(value);

        String valueScope = binding.getScope(value);

        //if the managed bean's scope is "none" but the scope of the
        //referenced object is not "none", scope is invalid
        if (scope == null) {
            if (valueScope != null) {
                return false;
            } 
            return true;
        }

        //if the managed bean's scope is "request" it is able to refer
        //to objects in any scope
        if (scope.equalsIgnoreCase(RIConstants.REQUEST)) {
            return true;
        }

        //if the managed bean's scope is "session" it is able to refer
        //to objects in other "session", "application", or "none" scopes
        if (scope.equalsIgnoreCase(RIConstants.SESSION)) {
            if (valueScope != null) {
                if (valueScope.equalsIgnoreCase(RIConstants.REQUEST)) {
                    return false;
                }
            }
            return true;
        }

        //if the managed bean's scope is "application" it is able to refer
        //to objects in other "application", or "none" scopes
        if (scope.equalsIgnoreCase(RIConstants.APPLICATION)) {
            if (valueScope != null) {
                if (valueScope.equalsIgnoreCase(RIConstants.REQUEST) ||
                    valueScope.equalsIgnoreCase(RIConstants.SESSION)) {
                    return false;
                }
            }
            return true;
        }

        //the managed bean is required to be in either "request", "session",
        //"application", or "none" scopes. One of the previous decision
        //statements must be true.
        org.mozilla.util.Assert.assert_it(false);
        return false;
    }

}
