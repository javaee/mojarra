/*
 * $Id: ManagedBeanFactoryImpl.java,v 1.8 2006/03/06 16:40:27 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;

import com.sun.faces.RIConstants;
import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.ListEntriesBean;
import com.sun.faces.config.beans.ManagedBeanBean;
import com.sun.faces.config.beans.ManagedPropertyBean;
import com.sun.faces.config.beans.MapEntriesBean;
import com.sun.faces.config.beans.MapEntryBean;
import com.sun.faces.spi.ManagedBeanFactory;
import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

import com.sun.org.apache.commons.beanutils.PropertyUtils;

/**
 * <p>This class creates a managed bean instance. It has a contract with
 * the ManagedBeanBean class which is populated from the config file.
 * The bean instance is created lazily so a deep copy of the
 * ManagedBeanBean is required.</p>
 *
 * <p>The Application implementation instantiated the beans as required and
 * stores them in the appropriate scope.</p>
 */
public class ManagedBeanFactoryImpl extends ManagedBeanFactory {

    //
    // class variables
    //

    //
    // Protected Constants
    //
    /**
     * Managed bean type is unknown
     */
    private static final byte TYPE_IS_UNKNOWN = -1;
    
    /**
     * This managed-bean or managed-property is a List
     */
    private final static byte TYPE_IS_LIST = 0;

    /**
     * This managed-bean or managed-property is a Map
     */
    private final static byte TYPE_IS_MAP = 1;

    /**
     * This managed-bean is a bean
     */
    private final static byte TYPE_IS_BEAN = 2;

    /**
     * This managed-bean is a UIComponent
     */
    private final static byte TYPE_IS_UICOMPONENT = 3;

    /**
     * This managed-property is a simple property
     */
    private final static byte TYPE_IS_SIMPLE = 3;
    
    private static final Method[] EMPTY_METHODS = new Method[0];
    
    private static final String MANAGED_BEAN_CREATED_STACK = 
        RIConstants.FACES_PREFIX + "managedBeanStack";

    private static enum Annotations {
        POST_CONSTRUCT("javax.annotation.PostConstruct"),
        PRE_DESTROY("javax.annotation.PreDestroy");
        
        Annotations(String annotationClassName) {                    
            ClassLoader loader = Thread.currentThread().getContextClassLoader();            
            if (loader != null) {
                try {
                    annotationClass = 
                          loader.loadClass(annotationClassName); 
                    if (!annotationClass.isAnnotation()) {
                        annotationClass = null;                        
                    }
                } catch (ClassNotFoundException cne) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO,
                                   "jsf.util_no_annotation_processed",
                                   annotationClassName);
                    }
                }
            }
        }
                
        private Class annotationClass;

        public Class getAnnotationClass() {
            return annotationClass;
        }                       
    }

    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private static final Logger LOGGER = Util.getLogger(Util.FACES_LOGGER 
            + Util.CONFIG_LOGGER);


    private Method[] postConstructMethods = null;
    private Method[] preDestroyMethods = null;
    
    // Attribute Instance Variables

    private ManagedBeanBean managedBean;
    private Scope scope = Scope.NONE;    

    // Relationship Instance Variables


    /**
     * <p>Setting this is part of the initialization contract for this
     * class.  Not setting this is a vialotation of that contract.</p>
     *
     */

    private Map<String,ManagedBeanFactory> managedBeanFactoryMap = null;

    //
    // Constructors and Initializers
    //
    /**
     * Constructor
     */
    public ManagedBeanFactoryImpl(ManagedBeanBean managedBean) {
        //ManagedBeanBean clone method implemented to return deep copy
        this.managedBean = managedBean; // (ManagedBeanBean) managedBean.clone();
        //set the scope
        scope = getScopeFromString(managedBean.getManagedBeanScope());
        
        // try to get the managedbean class
        Class<?> managedBeanClass = this.getManagedBeanClass();
        if (managedBeanClass != null) {
            postConstructMethods = 
                  getMethodsWithAnnotation(managedBeanClass,
                                           Annotations.POST_CONSTRUCT);
            preDestroyMethods = 
                  getMethodsWithAnnotation(managedBeanClass,
                                           Annotations.PRE_DESTROY);    
        }                
    }

    public Method[] getPostConstructMethods() {
        return postConstructMethods;
    }

    public Method[] getPreDestroyMethods() {
        return preDestroyMethods;
    }

    private Scope getScopeFromString(String scopeString) {
        Scope result = Scope.NONE;
	if (null != scopeString) {
            if (scopeString.equalsIgnoreCase(RIConstants.REQUEST)) {
                result = Scope.REQUEST;
            }
            else if (scopeString.equalsIgnoreCase(RIConstants.SESSION)) {
                result = Scope.SESSION;
            }
            else if (scopeString.equalsIgnoreCase(RIConstants.APPLICATION)) {
                result = Scope.APPLICATION;
            }
	}
        return result;
    }


    public void setManagedBeanBean(ManagedBeanBean newBean) {
        synchronized (this) {
            //ManagedBeanBean clone method implemented to return deep copy
            this.managedBean = newBean; // (ManagedBeanBean) newBean.clone();
            //set the scope
            scope = getScopeFromString(managedBean.getManagedBeanScope());
        }
    }
    
    public ManagedBeanBean getManagedBeanBean() {
        return this.managedBean;
    }

    public Map<String,ManagedBeanFactory> getManagedBeanFactoryMap() {
	if (null == managedBeanFactoryMap) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Contract violation: ManagedBeanFactory must be initialized with managedBeanFactoryMap after instantiation.");
            }
	}

	return managedBeanFactoryMap;
    }

    public void setManagedBeanFactoryMap(Map<String,ManagedBeanFactory> newManagedBeanFactoryMap) {
	managedBeanFactoryMap = newManagedBeanFactoryMap;
    }
    
    public String getBeanDescription(String lang) {
        DescriptionBean db = managedBean.getDescription(lang);
        if (db != null) {
            return db.getDescription();
        }
        return null;
    }
    
    public final Class getManagedBeanClass() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = this.getClass().getClassLoader();
            }
            return loader.loadClass(managedBean.getManagedBeanClass());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Attempt to instantiate the JavaBean and set its properties.
     */
    public Object newInstance(FacesContext context) throws FacesException {
        Object
            bean = null;
        int
            beanType = TYPE_IS_UNKNOWN;

        // before instantiating the bean, make sure there is no cyclic 
        // references.
        Map<String,Object> requestMap = 
            context.getExternalContext().getRequestMap();
        List<String> beanList = 
            (List<String>)requestMap.get(MANAGED_BEAN_CREATED_STACK);
        if (beanList == null) {
            beanList = new ArrayList<String>();
            requestMap.put(MANAGED_BEAN_CREATED_STACK, beanList);
        }
        
        if ( beanList.contains(managedBean.getManagedBeanName())) {
            if ( LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Possible cyclic reference to managedBean " + 
                   managedBean.getManagedBeanName() + " ");
            }
            Object[] obj = new Object[1];
            obj[0] = managedBean.getManagedBeanName();
            throw new FacesException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.CYCLIC_REFERENCE_ERROR_ID, obj)); 
        }
        
        //
        // instantiate the bean
        //
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = FacesContext.getCurrentInstance().
                    getClass().getClassLoader();
            }
            bean = java.beans.Beans.instantiate(loader,
                                                managedBean.getManagedBeanClass());
            FacesInjectionManager manager = 
                    FacesInjectionManager.getInjectionManager();
            if (manager != null) {
                manager.injectInstance(bean);    
            }
        } catch (Exception ex) {
            Object[] obj = new Object[2];
            obj[0] = managedBean.getManagedBeanClass();
            obj[1] = managedBean.getManagedBeanName();
            throw new FacesException(
                (MessageUtils.getExceptionMessageString(
                    MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,  obj) + ". " + 
                    ex.getMessage()), ex);
        }
        // add the bean to the managed bean stack.
        beanList.add(managedBean.getManagedBeanName());

        // populate the bean with its contents
        try {
            // what kind of bean is this?
            switch (beanType = getBeanType(bean)) {
                case TYPE_IS_LIST:
                    copyListEntriesFromConfigToList(
                        managedBean.getListEntries(),
                        (List<Object>) bean);
                    break;
                case TYPE_IS_MAP:
                    copyMapEntriesFromConfigToMap(managedBean.getMapEntries(),
                                                  (Map<Object,Object>) bean);
                    break;
                case TYPE_IS_UICOMPONENT:
                    // intentional fall-through
                case TYPE_IS_BEAN:
                    setPropertiesIntoBean(bean, beanType, managedBean);
                    break;
                default:
                    // notreached
                    assert (false);
                    break;
            }
        } catch (FacesException fe) {
            throw fe;
        } catch (ClassNotFoundException cnfe) {
            Object[] obj = new Object[1];
            obj[0] = managedBean.getManagedBeanClass();
            throw new FacesException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj),
                cnfe);
        }
        beanList.remove(managedBean.getManagedBeanName());
        return bean;
    }


    /**
     * determine the nature of the bean
     *
     * @return the appropriate TYPE_IS_* constant
     */

    protected int getBeanType(Object bean) {
        int result = TYPE_IS_UNKNOWN;
        ListEntriesBean listEntries = managedBean.getListEntries();
        MapEntriesBean mapEntries = managedBean.getMapEntries();
        ManagedPropertyBean[] managedProperties =
            managedBean.getManagedProperties();

        // is it a List?
        if (listEntries != null) {
            // managed-bean instances that are Lists, must not have
            // properties or map-entries.  It is a configuration error
            // if they do.
            if (mapEntries != null ||
                (managedProperties.length > 0)) {
                Object[] obj = new Object[1];
                obj[0] = managedBean.getManagedBeanClass();
                throw new FacesException(
                    MessageUtils.getExceptionMessageString(
                        MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj));
            }
            result = TYPE_IS_LIST;
        }

        // is it a Map?
        if (mapEntries != null) {
            assert (TYPE_IS_UNKNOWN == result);

            // managed-bean instances that are Maps, must not have
            // properties or list-entries.  It is a configuration error
            // if they do.
            if (managedBean.getManagedProperties().length > 0) {
                Object[] obj = new Object[1];
                obj[0] = managedBean.getManagedBeanClass();
                throw new FacesException(
                    MessageUtils.getExceptionMessageString(
                        MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj));
            }
            result = TYPE_IS_MAP;
        }

        if (TYPE_IS_LIST != result && TYPE_IS_MAP != result) {
            assert (TYPE_IS_UNKNOWN == result);

            // if it's not a List or a Map, it must be a Bean
            if (bean instanceof UIComponent) {
                result = TYPE_IS_UICOMPONENT;
            } else {
                result = TYPE_IS_BEAN;
            }
        }

        assert (TYPE_IS_UNKNOWN != result);
        return result;
    }


    /**
     * determine the nature of the property
     *
     * @return the appropriate TYPE_IS_* constant
     */

    protected int getPropertyType(ManagedPropertyBean bean) {
        int result = TYPE_IS_UNKNOWN;
        ListEntriesBean listEntries = bean.getListEntries();
        MapEntriesBean mapEntries = bean.getMapEntries();

        // is it a List?
        if (listEntries != null) {
            // managed-property instances that have list-entries must
            // not have value or map-entries.  It is a configuration
            // error if they do.
            if (mapEntries != null ||
                null != bean.getValue() || bean.isNullValue()) {
                Object[] obj = new Object[1];
                obj[0] = bean.getPropertyName();
                throw new FacesException(
                    MessageUtils.getExceptionMessageString(
                        MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj));
            }
            result = TYPE_IS_LIST;
        }

        // is it a Map?
        if (mapEntries != null) {
            assert (TYPE_IS_UNKNOWN == result);

            // managed-property instances that have map-entries, must
            // not have value or list-entries.  It is a configuration
            // error if they do.
            if (null != bean.getValue() || bean.isNullValue()) {
                Object[] obj = new Object[1];
                obj[0] = bean.getPropertyName();
                throw new FacesException(
                    MessageUtils.getExceptionMessageString(
                        MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj));
            }
            result = TYPE_IS_MAP;
        }

        if (TYPE_IS_LIST != result && TYPE_IS_MAP != result) {
            assert (TYPE_IS_UNKNOWN == result);

            if (null != bean.getValue() || bean.isNullValue()) {
                result = TYPE_IS_SIMPLE;
            }
        }

        // if the managed-property doesn't have list-entries,
        // map-entries, value, or null-value contents, this is a
        // configuration error.  The DTD doesn't allow this anyway.
        if (TYPE_IS_UNKNOWN == result && !bean.isNullValue()) {
            Object[] obj = new Object[1];
            obj[0] = bean.getPropertyName();
            throw new FacesException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj));
        }
        return result;
    }


    protected Class copyListEntriesFromConfigToList(ListEntriesBean listEntries,
                                                    List<Object> valuesForBean)
        throws ClassNotFoundException {
        String[] valuesFromConfig = listEntries.getValues();
        Class valueClass = java.lang.String.class;
        Object value = null;
        String strValue = null;
        int len = 0;

        if (0 == (len = valuesFromConfig.length)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("zero length array");
            }
            return null;
        }


        // pull out the value-class
        valueClass =
            getValueClassConsideringPrimitives(listEntries.getValueClass());

        // go through the values from the config and copy them to the
        // valuesForBean.
        for (int i = 0; i < len; i++) {
            strValue = valuesFromConfig[i];

            // set our value local variable
            if (Util.isVBExpression(strValue)) {
                value = evaluateValueExpressionGet(strValue);
            } else if (null == strValue) {
                value = null;
            } else {
                value = strValue;
            }
            // convert the value if necessary
            value = getConvertedValueConsideringPrimitives(value, valueClass);

            valuesForBean.add(value);
        }
        return valueClass;
    }


    void copyMapEntriesFromConfigToMap(MapEntriesBean mapEntries,
                                       Map<Object,Object> result)
        throws ClassNotFoundException {
        Object
            key = null,
            value = null;
        MapEntryBean[] valuesFromConfig = mapEntries.getMapEntries();
        MapEntryBean curEntry = null;
        Class
            keyClass = java.lang.String.class,
            valueClass = java.lang.String.class;
        String
            strKey = null,
            strValue = null;

        if (valuesFromConfig.length == 0) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("null or zero length array");
            }
            return;
        }

        // pull out the key-class and value-class
        keyClass =
            getValueClassConsideringPrimitives(mapEntries.getKeyClass());
        valueClass =
            getValueClassConsideringPrimitives(mapEntries.getValueClass());

        for (int i = 0, len = valuesFromConfig.length; i < len; i++) {
            curEntry = valuesFromConfig[i];

            strKey = curEntry.getKey();
            strValue = curEntry.getValue();

            if (Util.isVBExpression(strKey)) {
                key = evaluateValueExpressionGet(strKey);
            } else if (null == strKey) {
                key = null;
            } else {
                key = getConvertedValueConsideringPrimitives(strKey,
                                                             keyClass);
            }

            if (Util.isVBExpression(strValue)) {
                value = evaluateValueExpressionGet(strValue);
            } else if (null == strValue) {
                value = null;
            } else {
                value = getConvertedValueConsideringPrimitives(strValue,
                                                               valueClass);
            }
            result.put(key, value);
        }
    }


    protected void setPropertiesIntoBean(Object bean,
                                         int beanType,
                                         ManagedBeanBean managedBean) {
        Object
            value = null;
        String
            propertyClass = null,
            propertyName = null,
            strValue = null;
        
        Class valueClass = null;
        ManagedPropertyBean[] properties = managedBean.getManagedProperties();

        if (null == properties) {
            // a bean is allowed to have no properties
            return;
        }

        // iterate over the properties and load each into the bean
        for (int i = 0, len = properties.length; i < len; i++) {
            // skip null properties or properties without names
            if (null == properties[i] ||
                null == (propertyName = properties[i].getPropertyName())) {
                continue;
            }
            // determine what kind of property we have
            try {
                // this switch statement sets the "value" local variable
                // and tries to set it into the bean.
                switch (getPropertyType(properties[i])) {
                    case TYPE_IS_LIST:
                        setArrayOrListPropertiesIntoBean(bean, properties[i]);
                        break;
                    case TYPE_IS_MAP:
                        setMapPropertiesIntoBean(bean, properties[i]);                        
                        break;
                    case TYPE_IS_SIMPLE:
                        // if the config bean has no managed-property-class
                        // defined
                        if (null ==
                            (propertyClass = properties[i].getPropertyClass())) {
                            // look at the bean property
                            if (null ==
                                (valueClass =
                                PropertyUtils.getPropertyType(bean,
                                                              propertyName))) {
                                // if the bean property class can't be
                                // determined, use the fallback.
                                valueClass =
                                    getValueClassConsideringPrimitives(
                                        propertyClass);
                            }
                        } else {
                            // the config has a managed-property-class
                            // defined
                            valueClass =
                                getValueClassConsideringPrimitives(
                                    propertyClass);
                        }

                        strValue = properties[i].getValue();
                        if (Util.isVBExpression(strValue)) {
                            value = evaluateValueExpressionGet(strValue);
                        } else if (null == strValue &&
                            properties[i].isNullValue()) {
                            value = null;
                        } else {
                            value = strValue;
                        }
                        // convert the value if necessary
                        value =
                            getConvertedValueConsideringPrimitives(value,
                                                                   valueClass);
                        PropertyUtils.setSimpleProperty(bean,
                                                        propertyName,
                                                        value);
                        break;
                    default:
                        assert (false);
                }
            } catch (FacesException fe) {
                throw fe;
            } catch (Exception ex) {
                // if the property happens to be attribute on
                // UIComponent then bean introspection set will fail.
                if (TYPE_IS_UICOMPONENT == beanType) {
                    setComponentAttribute(bean, propertyName, value);
                } else {
                    // then this is a real exception to rethrow
                    Object[] obj = new Object[1];
                    obj[0] = propertyName;
                    throw new FacesException(
                        MessageUtils.getExceptionMessageString(
                            MessageUtils.ERROR_SETTING_BEAN_PROPERTY_ERROR_MESSAGE_ID,
                            obj),
                        ex);
                }
            }
        }
    }


    /**
     * <li><p> Call the property getter, if it exists.</p></li>
     * <p/>
     * <li><p>If the getter returns null or doesn't exist, create a
     * java.util.ArrayList(), otherwise use the returned Object (an array or
     * a java.util.List).</p></li>
     * <p/>
     * <li><p>If a List was returned or created in step 2), add all
     * elements defined by nested &lt;value&gt; elements in the order
     * they are listed, converting values defined by nested
     * &lt;value&gt; elements to the type defined by
     * &lt;value-class&gt;. If a &lt;value-class&gt; is not defined, use
     * the value as-is (i.e., as a java.lang.String). Add null for each
     * &lt;null-value&gt; element.</p></li>
     * <p/>
     * <li><p> If an array was returned in step 2), create a
     * java.util.ArrayList and copy all elements from the returned array to
     * the new List, auto-boxing elements of a primitive type. Add all
     * elements defined by nested &lt;value&gt; elements as described in step
     * 3).</p></li>
     * <p/>
     * <li><p> If a new java.util.List was created in step 2) and the
     * property is of type List, set the property by calling the setter
     * method, or log an error if there is no setter method.</p></li>
     * <p/>
     * <li><p> If a new java.util.List was created in step 4), convert
     * the * List to array of the same type as the property and set the
     * property by * calling the setter method, or log an error if there
     * is no setter * method.</p></li>
     */

    private void setArrayOrListPropertiesIntoBean(Object bean,
                                                  ManagedPropertyBean property)
        throws Exception {
        Object result = null;
        boolean
            getterIsNull = true,
            getterIsArray = false;
        List<Object>
            valuesForBean = null;
        Class
            valueType = java.lang.String.class,
            propertyType = null;

        String propertyName = property.getPropertyName();

        try {
            // see if there is a getter
            result = PropertyUtils.getProperty(bean, propertyName);
            getterIsNull = (null == result);

            propertyType =
                PropertyUtils.getPropertyType(bean, propertyName);
            getterIsArray = propertyType.isArray();

        } catch (NoSuchMethodException nsme) {
            // it's valid to not have a getter.
        }

        // the property has to be either a List or Array
        if (!getterIsArray) {
            if (null != propertyType &&
                !java.util.List.class.isAssignableFrom(propertyType)) {
                throw new FacesException(MessageUtils.getExceptionMessageString(
                    MessageUtils.MANAGED_BEAN_CANNOT_SET_LIST_ARRAY_PROPERTY_ID,
                    propertyName,
                    managedBean.getManagedBeanName()));
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
                valuesForBean = new ArrayList<Object>();
                for (int i = 0, len = Array.getLength(result); i < len; i++) {
                    // add the existing values
                    valuesForBean.add(Array.get(result, i));
                }
            } else {
                // if what it returned was not a List
                if (!(result instanceof List)) {
                    // throw an exception
                    throw new FacesException(MessageUtils.getExceptionMessageString(
                        MessageUtils.MANAGED_BEAN_EXISTING_VALUE_NOT_LIST_ID,
                        propertyName,
                        managedBean.getManagedBeanName()));
                }
                valuesForBean = (List<Object>) result;
            }
        } else {

            // getter returned null
            result = valuesForBean = new ArrayList<Object>();
        }

        // at this point valuesForBean contains the existing values from
        // the bean, or no values if the bean had no values.  In any
        // case, we can proceed to add values from the config file.
        valueType = copyListEntriesFromConfigToList(property.getListEntries(),
                                                    valuesForBean);

        // at this point valuesForBean has the values to be set into the
        // bean.

        if (getterIsArray) {
            // convert back to Array
            result = Array.newInstance(valueType, valuesForBean.size());
            for (int i = 0, len = valuesForBean.size(); i < len; i++) {
                if (valueType == Boolean.TYPE) {
                    Array.setBoolean(result, i,
                                     (Boolean) valuesForBean.get(i));
                } else if (valueType == Byte.TYPE) {
                    Array.setByte(result, i,
                                  (Byte) valuesForBean.get(i));
                } else if (valueType == Double.TYPE) {
                    Array.setDouble(result, i,
                                    (Double) valuesForBean.get(i));
                } else if (valueType == Float.TYPE) {
                    Array.setFloat(result, i,
                                   (Float) valuesForBean.get(i));
                } else if (valueType == Integer.TYPE) {
                    Array.setInt(result, i,
                                 (Integer) valuesForBean.get(i));
                } else if (valueType == Character.TYPE) {
                    Array.setChar(result, i,
                                  (Character) valuesForBean.get(i));
                } else if (valueType == Short.TYPE) {
                    Array.setShort(result, i,
                                   (Short) valuesForBean.get(i));
                } else if (valueType == Long.TYPE) {
                    Array.setLong(result, i,
                                  (Long) valuesForBean.get(i));
                } else {
                    Array.set(result, i, valuesForBean.get(i));
                }
            }
        } else {
            result = valuesForBean;
        }

        if (getterIsNull || getterIsArray) {
            PropertyUtils.setProperty(bean, propertyName, result);
        }

    }


    /**
     * <li><p>Call the property getter, if it exists.</p></li>
     * <p/>
     * <li><p>If the getter returns null or doesn't exist, create a
     * java.util.HashMap(), otherwise use the returned
     * java.util.Map.</p></li>
     * <p/>
     * <li><p>Add all entries defined by nested &lt;map-entry&gt;
     * elements in the order they are listed, converting key values
     * defined by nested &lt;key&gt; elements to the type defined by
     * &lt;key-class&gt; and entry values defined by nested
     * &lt;value&gt; elements to the type defined by
     * &lt;value-class&gt;. If &lt;key-class&gt; and/or
     * &lt;value-class&gt; are not defined, use the value as-is (i.e.,
     * as a java.lang.String). Add null for each &lt;null-value&gt;
     * element.</p></li>
     * <p/>
     * <li><p>If a new java.util.Map was created in step 2), set the
     * property by calling the setter method, or log an error if there
     * is no setter method.</p></li>
     */
    private void setMapPropertiesIntoBean(Object bean,
                                          ManagedPropertyBean property)
        throws Exception {
        Map<Object,Object> result = null;
        boolean getterIsNull = true;
        Class propertyType = null;
        String propertyName = property.getPropertyName();

        try {
            // see if there is a getter
            result = (Map<Object,Object>) 
                        PropertyUtils.getProperty(bean, propertyName);
            getterIsNull = (null == result);

            propertyType = PropertyUtils.getPropertyType(bean, propertyName);
        } catch (NoSuchMethodException nsme) {
            // it's valid to not have a getter.
        }

        if (null != propertyType &&
            !java.util.Map.class.isAssignableFrom(propertyType)) {
            throw new FacesException(MessageUtils.getExceptionMessageString(
                    MessageUtils.MANAGED_BEAN_CANNOT_SET_MAP_PROPERTY_ID,
                    propertyName,
                    managedBean.getManagedBeanName()));
        }


        if (getterIsNull) {
            result = new HashMap<Object,Object>();
        }

        // at this point result contains the existing entries from the
        // bean, or no entries if the bean had no entries.  In any case,
        // we can proceed to add values from the config file.

        copyMapEntriesFromConfigToMap(property.getMapEntries(), result);

        if (getterIsNull) {
            PropertyUtils.setProperty(bean, propertyName, result);
        }

    }


    private Class getValueClassConsideringPrimitives(String valueClass)
        throws ClassNotFoundException {
        Class valueType = java.lang.String.class;
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
            } else {
                valueType = Util.loadClass(valueClass, this);
            }
        }
        return valueType;
    }


    private Object getConvertedValueConsideringPrimitives(Object value,
                                                          Class valueType)
        throws FacesException {        
        if (null != value && null != valueType) {
            String valueString = value.toString();
            if (valueType.isEnum()) {
                value = Enum.valueOf(valueType, valueString);
            }
            else if (valueType == Boolean.TYPE ||
                valueType == java.lang.Boolean.class) {
                value = Boolean.valueOf(valueString);
            } else if (valueType == Byte.TYPE ||
                valueType == java.lang.Byte.class) {
                value = (valueString.length() == 0
                         ? 0
                         : Byte.valueOf(valueString));
            } else if (valueType == Double.TYPE ||
                valueType == java.lang.Double.class) {               
                value = (valueString.length() == 0
                         ? 0
                         : Double.valueOf(valueString));
            } else if (valueType == Float.TYPE ||
                valueType == java.lang.Float.class) {                
                value = (valueString.length() == 0
                         ? 0
                         : Float.valueOf(valueString));
            } else if (valueType == Integer.TYPE ||
                valueType == java.lang.Integer.class) {               
                value = (valueString.length() == 0
                         ? 0
                         : Integer.valueOf(valueString));
            } else if (valueType == Character.TYPE ||
                valueType == java.lang.Character.class) {               
                value = (valueString.length() == 0
                         ? 0
                         : valueString.charAt(0));
            } else if (valueType == Short.TYPE ||
                valueType == java.lang.Short.class) {               
                value = (valueString.length() == 0
                         ? 0
                         : Short.valueOf(valueString));
            } else if (valueType == Long.TYPE ||
                valueType == java.lang.Long.class) {               
                value = (valueString.length() == 0
                         ? 0
                         : Long.valueOf(valueString));
            } else if (valueType == String.class) {
            } else if (!valueType.isAssignableFrom(value.getClass())) {
                throw new FacesException(MessageUtils.getExceptionMessageString(
                      MessageUtils.MANAGED_BEAN_TYPE_CONVERSION_ERROR_ID,
                      value.toString(),
                      value.getClass(),
                      valueType,
                      managedBean.getManagedBeanName()));

            }
        }
        return value;
    }


    public Scope getScope() {
        return scope;
    }


    private Object evaluateValueExpressionGet(String value) throws FacesException {
        Object result = null;

        if (!hasValidLifespan(value)) {
            Object[] obj = new Object[1];
            obj[0] = value;
            throw new FacesException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID, obj));
        }

        ValueExpression ve = Util.getValueExpression(value);
        if (ve != null) {
            try {
                result = 
                  ve.getValue(FacesContext.getCurrentInstance().getELContext());
            } catch (PropertyNotFoundException ex) {
                Object[] obj = new Object[1];
                obj[0] = value;
                throw new FacesException(
                    MessageUtils.getExceptionMessageString(
                        MessageUtils.ERROR_GETTING_VALUEREF_VALUE_ERROR_MESSAGE_ID,
                        obj));
            }
        } else {
            Object[] obj = new Object[1];
            obj[0] = value;
            throw new FacesException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.ERROR_GETTING_VALUE_BINDING_ERROR_MESSAGE_ID, obj));
        }
        return result;
    }


    private boolean hasValidLifespan(String value) throws EvaluationException, ReferenceSyntaxException {
	Scope valueScope = null;

	if (Util.isMixedVBExpression(value)) {
	    valueScope = getNarrowestScopeFromExpression(value);
	}
	else {
	    valueScope = getScopeForSingleExpression(value);
	}

        //if the managed bean's scope is "none" but the scope of the
        //referenced object is not "none", scope is invalid
        if (scope == Scope.NONE) {
            return valueScope == Scope.NONE;
        }
       
        //if the managed bean's scope is "request" it is able to refer
        //to objects in any scope
        if (scope == Scope.REQUEST) {
            return true;
        }

        //if the managed bean's scope is "session" it is able to refer
        //to objects in other "session", "application", or "none" scopes
        if (scope == Scope.SESSION) {
            return valueScope != Scope.REQUEST;
        }

        //if the managed bean's scope is "application" it is able to refer
        //to objects in other "application", or "none" scopes
        if (scope == Scope.APPLICATION) {
            return !(valueScope == Scope.REQUEST
                     || valueScope == Scope.SESSION);
        }

        //the managed bean is required to be in either "request", "session",
        //"application", or "none" scopes. One of the previous decision
        //statements must be true.
        assert (false);
        return false;
    }

    private Scope getScopeForSingleExpression(String value) throws ReferenceSyntaxException, EvaluationException {
	String [] firstSegment = new String[1];
        Scope valueScope = Util.getScope(value, firstSegment);
	
	if (null == valueScope) {
	    // Perhaps the bean hasn't been created yet.  See what its
	    // scope would be when it is created.
	    ManagedBeanFactoryImpl otherFactory = null;
	    if (null != firstSegment[0] &&
		null != (otherFactory = (ManagedBeanFactoryImpl)
			 getManagedBeanFactoryMap().get(firstSegment[0]))) {
		valueScope = otherFactory.getScope();
	    }
	    else {
		// we are referring to a bean that doesn't exist in the
		// configuration file.  Give it a wide scope...
                valueScope = Scope.APPLICATION;
	    }
	}
	return valueScope;
    }

    private Scope getNarrowestScopeFromExpression(String expression) throws ReferenceSyntaxException {
	// break the argument expression up into its component
	// expressions, ignoring literals.
	List expressions = Util.getExpressionsFromString(expression);
	Iterator iter = expressions.iterator();
	int 
	    shortestScope = Scope.NONE.ordinal(),
	    currentScope = Scope.NONE.ordinal();
	Scope 
	    lScope = Scope.NONE,
	    result = Scope.NONE;
	
	// loop over the expressions 
	while (iter.hasNext()) {
	    lScope = getScopeForSingleExpression((String)iter.next());
	    // don't consider none
	    if (null == lScope || lScope == Scope.NONE) {
		continue;
	    }

	    currentScope = lScope.ordinal();
	    
	    // if we have no basis for comparison
	    if (Scope.NONE.ordinal() == shortestScope) {
		shortestScope = currentScope;
		result = lScope;
	    }
	    else {
		// we have a basis for comparison
		if (currentScope < shortestScope) {
		    shortestScope = currentScope;
		    result = lScope;
		}
	    }
	}
	return result;
    }
    
    /**
     * Returns a List of Methods on the instance referenced by argument
     * <code>obj</code> that are annotated with the annotation
     * referenced by argument <code>annoClass</code>.  If none are
     * found, returns the empty list.
     *
     * @param clazz the class for which to inspect for annotated methods
     *@param annotation the <code>Annotation</code> of interest
     */
    private static Method[] getMethodsWithAnnotation(Class clazz,
                                                     Annotations annotation) {
        List<Method> list = null;      
        if (null != clazz) {
            Class<? extends Annotation> annoClass = annotation.getAnnotationClass();
            if (annoClass == null) {
                return EMPTY_METHODS;
            }
           
            Method[] methods = clazz.getMethods();

            for (Method method : methods) {
                if (null != method.getAnnotation(annoClass)) {                   
                    if (null == list) {
                        list = new ArrayList<Method>();
                    }
                    list.add(method);
                }
            }
        }
        if (null == list) {
            return EMPTY_METHODS;
        }
        return list.toArray(new Method[list.size()]);
    }


    /**
     * Sets the passed in property name and value as an attribute on
     * <ocde>UIComponent</code> instance.
     */
    public void setComponentAttribute(Object component, String propName,
                                      Object propValue) {
        ((UIComponent) component).getAttributes().put(propName, propValue);
    }

}
