/*
 * $Id: ManagedBeanFactory.java,v 1.11 2003/12/17 15:13:31 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

import java.lang.reflect.Array;

import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.faces.el.ValueBinding;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.component.UIComponent;

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
	synchronized (this) {
	    //ConfigManagedBean clone method implemented to return deep copy
	    this.managedBean = (ConfigManagedBean) newBean.clone();
	    scope = null;
	}
    }

    /**
     * Attempt to instantiate the JavaBean and set its properties.
     */
    public Object newInstance() throws FacesException {
        Object bean = null;
        boolean isUIComponent = false;
        
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

        if ( bean instanceof UIComponent) {
            isUIComponent = true;
        }
        //set the scope
        scope = managedBean.getManagedBeanScope();

        Map props = managedBean.getProperties();
        Iterator iter = props.keySet().iterator();
        ConfigManagedBeanProperty cmp = null;
        ConfigManagedBeanPropertyValue cmpv = null;
        Object value;

	if (null != (cmp = managedBean.getListOrMap())) {
	    // if a managed bean is a List or a Map, it may not have
	    // properties.
	    if (0 != props.size()) {
		Object[] obj = new Object[1];
		obj[0] = managedBean.getManagedBeanClass();
		throw new FacesException(Util.getExceptionMessage(Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj));
	    }

	    if (cmp.hasValuesArray()) {
		try {
		    copyListEntriesFromConfigToList(cmp, (List) bean);
		}
		catch (ClassNotFoundException cnfe) {
		    Object[] obj = new Object[1];
		    obj[0] = managedBean.getManagedBeanClass();
		    throw new FacesException(Util.getExceptionMessage(Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj), cnfe);
		}
	    } else if (cmp.hasMapEntries()) {
		copyMapEntriesFromConfigToMap(cmp, (Map) bean);
	    }
	}
        
        while (iter.hasNext()) {
            value = null;
            cmp = (ConfigManagedBeanProperty)props.get(iter.next());
            if (cmp.hasValuesArray()) {
		Object arrayOrList = null;

		try {
		    arrayOrList = getArrayOrListToSetIntoBean(bean, cmp);
		    PropertyUtils.setProperty(bean, cmp.getPropertyName(), 
					      arrayOrList);
		} 
		catch (FacesException fe) {
		    throw fe;
		}
		catch (Exception ex) {
		    // if the property happens to be attribute on UIComponent
		    // then bean introspection will fail.
		    if ( isUIComponent) {
			setComponentAttribute(bean, cmp.getPropertyName(), 
					      value);  
		    } else {
			Object[] obj = new Object[1];
			obj[0] = cmp.getPropertyName();
			throw new FacesException(
						 Util.getExceptionMessage(
									  Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj), 
						 ex);
		    }
		}
		
            } else if (cmp.hasMapEntries()) {
		Map map = null;

		try {
		    map = getMapToSetIntoBean(bean, cmp);
		    PropertyUtils.setProperty(bean, cmp.getPropertyName(), 
					      map);
		} 
		catch (FacesException fe) {
		    throw fe;
		}
		catch (Exception ex) {
		    // if the property happens to be attribute on UIComponent
		    // then bean introspection will fail.
		    if ( isUIComponent) {
			setComponentAttribute(bean, cmp.getPropertyName(), 
					      value);  
		    } else {
			Object[] obj = new Object[1];
			obj[0] = cmp.getPropertyName();
			throw new FacesException(
						 Util.getExceptionMessage(
									  Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj), 
						 ex);
		    }
		}

            } else {
		
                cmpv = cmp.getValue();
		
                //find properties and set them on the bean
                if (cmpv.getValueCategory() == 
                    ConfigManagedBeanPropertyValue.VALUE_BINDING) {
                    value = evaluateValueBindingGet((String)cmpv.getValue());
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
                    if ( isUIComponent) {
                        setComponentAttribute(bean, cmp.getPropertyName(), 
					      value);
                    } else {
                        Object[] obj = new Object[1];
                        obj[0] = cmp.getPropertyName();
                        throw new FacesException(Util.getExceptionMessage(
									  Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj), 
						 ex);
                    }
                }
            }
	    
        }
	
        return bean;
    }

    /**
     * 
     * <li><p> Call the property getter, if it exists.</p></li>
     *
     * <li><p>If the getter returns null or doesn't exist, create a
     * java.util.ArrayList(), otherwise use the returned Object (an array or
     * a java.util.List).</p></li>
     *
     * <li><p>If a List was returned or created in step 2), add all
     * elements defined by nested &lt;value&gt; elements in the order
     * they are listed, converting values defined by nested
     * &lt;value&gt; elements to the type defined by
     * &lt;value-class&gt;. If a &lt;value-class&gt; is not defined, use
     * the value as-is (i.e., as a java.lang.String). Add null for each
     * &lt;null-value&gt; element.</p></li>
     *
     * <li><p> If an array was returned in step 2), create a
     * java.util.ArrayList and copy all elements from the returned array to
     * the new List, auto-boxing elements of a primitive type. Add all
     * elements defined by nested &lt;value&gt; elements as described in step
     * 3).</p></li>
     *
     * <li><p> If a new java.util.List was created in step 2) and the
     * property is of type List, set the property by calling the setter
     * method, or log an error if there is no setter method.</p></li>
     *
     * <li><p> If a new java.util.List was created in step 4), convert
     * the * List to array of the same type as the property and set the
     * property by * calling the setter method, or log an error if there
     * is no setter * method.</p></li>
     *
     * @return the array or list to store into the bean.
     */

    private Object getArrayOrListToSetIntoBean(Object bean, 
					       ConfigManagedBeanProperty cmp) throws Exception {
	Object result = null;
	boolean 
	    getterIsNull = true,
	    getterIsArray = false;
	List
	    valuesForBean = null,
	    valuesFromConfig = null;
	Class 
	    valueType = java.lang.String.class,
	    propertyType = null;
	Object value = null;
	ConfigManagedBeanPropertyValue cmpv = null;
	
	try {
	    // see if there is a getter
	    result = PropertyUtils.getProperty(bean, cmp.getPropertyName());
	    getterIsNull = (null == result) ? true : false;

	    propertyType = 
		PropertyUtils.getPropertyType(bean, cmp.getPropertyName());
	    getterIsArray = propertyType.isArray();
	    
	}
	catch (NoSuchMethodException nsme) {
	    // it's valid to not have a getter.
	}

	// the property has to be either a List or Array
	if (!getterIsArray) {
	    if (null != propertyType && 
		!java.util.List.class.isAssignableFrom(propertyType)) {
		Object[] obj = new Object[1];
		obj[0] = cmp.getPropertyName();
		throw new FacesException(Util.getExceptionMessage(
								  Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, 
								  obj));
	    }
	}
	
	//
	// Deal with the possibility of the getter returning existing
	// values.
	// 

	// if the getter returned non-null
	if (!getterIsNull) {
	    // if what it returned was an array
	    if (getterIsArray) {
		valuesForBean = new ArrayList();
		for (int i = 0, len = Array.getLength(result); i < len; i++) {
		    valuesForBean.add(Array.get(result, i));
		}
	    }
	    else {
		// if what it returned was not a List
		if (!(result instanceof List)) {
		    // throw an exception
		    Object[] obj = new Object[1];
		    obj[0] = cmp.getPropertyName();
		    throw new FacesException(
					     Util.getExceptionMessage(
								      Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj));
		}
		valuesForBean = (List) result;
	    }
	}
	else {
	    
	    // getter returned null
	    result = valuesForBean = new ArrayList();
	}

	// at this point valuesForBean contains the existing values from
	// the bean, or no values if the bean had no values.  In any
	// case, we can proceed to add values from the config file.
	valueType = copyListEntriesFromConfigToList(cmp, valuesForBean);
	
	// at this point valuesForBean has the values to be set into the
	// bean. 

	if (getterIsArray) {
	    // convert back to Array
	    result = Array.newInstance(valueType, valuesForBean.size());
	    for (int i = 0, len = valuesForBean.size(); i < len; i++) {
		if (valueType == Boolean.TYPE) {
		    Array.setBoolean(result, i, ((Boolean)valuesForBean.get(i)).booleanValue());
		} else if (valueType == Byte.TYPE) {
		    Array.setByte(result, i, ((Byte)valuesForBean.get(i)).byteValue());
		} else if (valueType == Double.TYPE) {
		    Array.setDouble(result, i, ((Double)valuesForBean.get(i)).doubleValue());
		} else if (valueType == Float.TYPE) {
		    Array.setFloat(result, i, ((Float)valuesForBean.get(i)).floatValue());
		} else if (valueType == Integer.TYPE) {
		    Array.setInt(result, i, ((Integer)valuesForBean.get(i)).intValue());
		} else if (valueType == Character.TYPE) {
		    Array.setChar(result, i, ((Character)valuesForBean.get(i)).charValue());
		} else if (valueType == Short.TYPE) {
		    Array.setShort(result, i, ((Short)valuesForBean.get(i)).shortValue());
		} else if (valueType == Long.TYPE) {
		    Array.setLong(result, i, ((Long)valuesForBean.get(i)).longValue());
		} 
		else {
		    Array.set(result, i, valuesForBean.get(i));
		}
	    }
	}
	else {
	    result = valuesForBean;
	}

	return result;
    }

    protected Class copyListEntriesFromConfigToList(ConfigManagedBeanProperty cmp,
						    List valuesForBean) throws ClassNotFoundException {
	List valuesFromConfig = cmp.getValues();
	String valueClass = null;
	Class valueType = java.lang.String.class;
	Object value = null;
	ConfigManagedBeanPropertyValue cmpv = 
	    (ConfigManagedBeanPropertyValue)valuesFromConfig.get(0);
	int start = 0;
	
	// pull out the value-class
	if (cmpv.getValueCategory() == 
	    ConfigManagedBeanPropertyValue.VALUE_CLASS) {
	    valueClass = (String) cmpv.getValue();
	    start = 1;
	    valueType = getValueClassConsideringPrimitives(valueClass);
	}
	
	// go through the values from the config and copy them to the
	// valuesForBean.
	for (int i = start, size = valuesFromConfig.size(); 
	     i < size; i++) {
	    cmpv = (ConfigManagedBeanPropertyValue)valuesFromConfig.get(i);
	    if (cmpv.getValueCategory() == 
		ConfigManagedBeanPropertyValue.VALUE_BINDING) {
		value = evaluateValueBindingGet((String)cmpv.getValue());
	    } else if (cmpv.getValueCategory() == 
		       ConfigManagedBeanPropertyValue.NULL_VALUE) {
		value = null;
	    }
	    else {
		value = cmpv.getValue();
	    }
	    // convert the value if necessary
	    value = getConvertedValueConsideringPrimitives(value, valueType);
	    
	    valuesForBean.add(value);
	}
	return valueType;
    }

    /**
     *
     * <li><p>Call the property getter, if it exists.</p></li>
     *
     * <li><p>If the getter returns null or doesn't exist, create a
     * java.util.HashMap(), otherwise use the returned
     * java.util.Map.</p></li>
     *
     * <li><p>Add all entries defined by nested &lt;map-entry&gt;
     * elements in the order they are listed, converting key values
     * defined by nested &lt;key&gt; elements to the type defined by
     * &lt;key-class&gt; and entry values defined by nested
     * &lt;value&gt; elements to the type defined by
     * &lt;value-class&gt;. If &lt;key-class&gt; and/or
     * &lt;value-class&gt; are not defined, use the value as-is (i.e.,
     * as a java.lang.String). Add null for each &lt;null-value&gt;
     * element.</p></li>
     *
     * <li><p>If a new java.util.Map was created in step 2), set the
     * property by calling the setter method, or log an error if there
     * is no setter method.</p></li>
     *
     */
    private Map getMapToSetIntoBean(Object bean, 
				    ConfigManagedBeanProperty cmp) throws Exception {
	Map result = null;
	boolean getterIsNull = true;
	Class propertyType = null;
	ConfigManagedPropertyMap cmpm = null;
	List valuesFromConfig = null;
	Object value = null;
	
	try {
	    // see if there is a getter
	    result = (Map) PropertyUtils.getProperty(bean, 
						     cmp.getPropertyName());
	    getterIsNull = (null == result) ? true : false;
	    
	    propertyType = 
		PropertyUtils.getPropertyType(bean, cmp.getPropertyName());
	}
	catch (NoSuchMethodException nsme) {
	    // it's valid to not have a getter.
	}

	if (null != propertyType &&
	    !java.util.Map.class.isAssignableFrom(propertyType)) {
	    Object[] obj = new Object[1];
	    obj[0] = cmp.getPropertyName();
	    throw new FacesException(Util.getExceptionMessage(
							      Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, 
							      obj));
	}
	
	
	if (getterIsNull) {
	    result = new java.util.HashMap();
	}
	
	// at this point result contains the existing entries from the
	// bean, or no entries if the bean had no entries.  In any case,
	// we can proceed to add values from the config file.
	
	copyMapEntriesFromConfigToMap(cmp, result);
	
	return result;
    }
    
    void copyMapEntriesFromConfigToMap(ConfigManagedBeanProperty cmp, 
				       Map result) {
	Object value = null;
	List valuesFromConfig = cmp.getMapEntries();
	ConfigManagedPropertyMap cmpm = null;

	for (int i = 0, len = valuesFromConfig.size(); i < len; i++) {
	    cmpm = (ConfigManagedPropertyMap) valuesFromConfig.get(i);
	    if (cmpm.getValueCategory() == ConfigManagedPropertyMap.VALUE_BINDING){
		value = evaluateValueBindingGet((String)cmpm.getValue());
	    }
	    else if (cmpm.getValueCategory() == 
		     ConfigManagedPropertyMap.NULL_VALUE) {
		value = null;
	    }
	    else {
		value = cmpm.getValue();
	    }
	    result.put(cmpm.getKey(), value);
	}
    }

    private Class getValueClassConsideringPrimitives(String valueClass) throws ClassNotFoundException {
	Class valueType = null;
	if (null != valueClass && 0 < valueClass.length()) {
	    if (valueClass.equals(Boolean.TYPE.getName())) {
		valueType = Boolean.TYPE;
	    } else if (valueClass.equals(Byte.TYPE.getName())) {
		valueType = Byte.TYPE;
	    } else if (valueClass.equals(Double.TYPE.getName())) {
		valueType = Double.TYPE;
	    } else if (valueClass.equals(Float.TYPE.getName())) {
		valueType = Float.TYPE;
	    } else if (valueClass.equals(Integer.TYPE.getName())) {
		valueType = Integer.TYPE;
	    } else if (valueClass.equals(Character.TYPE.getName())) {
		valueType = Character.TYPE;
	    } else if (valueClass.equals(Short.TYPE.getName())) {
		valueType = Short.TYPE;
	    } else if (valueClass.equals(Long.TYPE.getName())) {
		valueType = Long.TYPE;
	    }
	    else {
		valueType = Util.loadClass(valueClass, this);
	    }
	}
	return valueType;
    }

    private Object getConvertedValueConsideringPrimitives(Object value,
							  Class valueType) throws FacesException {
	if (null != value && null != valueType) {
	    if (valueType == Boolean.TYPE || 
		valueType == java.lang.Boolean.class) {
		value = new Boolean(value.toString());
	    } else if (valueType == Byte.TYPE || 
		       valueType == java.lang.Byte.class) {
		value = new Byte(value.toString());
	    } else if (valueType == Double.TYPE || 
		       valueType == java.lang.Double.class) {
		value = new Double(value.toString());
	    } else if (valueType == Float.TYPE || 
		       valueType == java.lang.Float.class) {
		value = new Float(value.toString());
	    } else if (valueType == Integer.TYPE || 
		       valueType == java.lang.Integer.class) {
		value = new Integer(value.toString());
	    } else if (valueType == Character.TYPE || 
		       valueType == java.lang.Character.class) {
		value = new Character(value.toString().charAt(0));
	    } else if (valueType == Short.TYPE || 
		       valueType == java.lang.Short.class) {
		value = new Short(value.toString());
	    } else if (valueType == Long.TYPE || 
		       valueType == java.lang.Long.class) {
		value = new Long(value.toString());
	    } else if (valueType == String.class) {
	    }
	    else if (!valueType.isAssignableFrom(value.getClass())) {
		Object[] obj = new Object[1];
		obj[0] = value.toString();
		throw new FacesException(Util.getExceptionMessage(
								  Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, 
								  obj));
		
	    }
	}
	return value;
    }

    public String getScope() {
        return scope;
    }

    private Object evaluateValueBindingGet(String value) throws FacesException {
        Object valueBinding = null;

        if (!hasValidLifespan(value)) {
            Object[] obj = new Object[1];
            obj[0] = value;
            throw new FacesException(Util.getExceptionMessage(Util.INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID, obj));
        }

        ValueBinding binding = Util.getValueBinding(value);
        if (binding != null) {
            try {
                valueBinding = binding.getValue(FacesContext.getCurrentInstance());
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
        return valueBinding;
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
        Util.doAssert(false);
        return false;
    }
    /**
     * Sets the passed in property name and value as an attribute on 
     * <ocde>UIComponent</code> instance.
     */
    public void setComponentAttribute(Object component, String propName,
        Object propValue) {
        ((UIComponent)component).getAttributes().put(propName, propValue);
    }

}
