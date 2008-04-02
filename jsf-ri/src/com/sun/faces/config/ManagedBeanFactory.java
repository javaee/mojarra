/*
 * $Id: ManagedBeanFactory.java,v 1.2 2003/04/29 20:51:35 eburns Exp $
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
	//need to instantiate bean
        Object bean = null;

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = FacesContext.getCurrentInstance().
                    getClass().getClassLoader();
            }
	    bean = java.beans.Beans.instantiate(
                loader, managedBean.getManagedBeanClass());
        } catch (Exception ex) {
            //FIX_ME: add a better exception message
            throw new FacesException(ex.getMessage(), ex);
        }

	Map props = managedBean.getProperties();
	Iterator iter = props.keySet().iterator();
	ConfigManagedBeanProperty cmp = null;
	ConfigManagedBeanPropertyValue cmpv = null;
	while (iter.hasNext()) {
            cmp = (ConfigManagedBeanProperty)props.get((String)iter.next());
            if (cmp.hasValuesArray()) {
		List list = cmp.getValues();
		for (int i=0; i < list.size(); i++) {
                    cmpv = (ConfigManagedBeanPropertyValue)list.get(i);

                    //FIX_ME: To do: set Mapped properties once they're available from the ConfigManagedBean
                    //set the indexed property on the bean
                    try {
	                PropertyUtils.setIndexedProperty(bean, 
                            cmp.getPropertyName(), i, cmpv.getValue());
                    } catch (Exception ex) {
                        //FIX_ME: add a better exception message
                        throw new FacesException(ex.getMessage(), ex);
                    }
                }
            } else {
                cmpv = (ConfigManagedBeanPropertyValue)cmp.getValue();

                //find properties and set them on the bean
                try {
	            PropertyUtils.setSimpleProperty(bean, 
                        cmp.getPropertyName(), cmpv.getValue());
                } catch (Exception ex) {
                    //FIX_ME: add a better exception message
                    throw new FacesException(ex.getMessage(), ex);
                }
            }

	}

        //set the scope
        scope = managedBean.getManagedBeanScope();

        return bean;
    }

    public String getScope() {
        return scope;
    }

}
