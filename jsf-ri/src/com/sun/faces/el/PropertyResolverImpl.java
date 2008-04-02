/*
 * $Id: PropertyResolverImpl.java,v 1.9 2003/12/17 15:13:37 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;


import com.sun.faces.RIConstants;

import javax.faces.component.UIComponent;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Concrete implementation of <code>PropertyResolver</code>.</p>
 */

public class PropertyResolverImpl extends PropertyResolver {


    // ------------------------------------------------------- Static Variables

    private static final Log log = LogFactory.getLog(PropertyResolver.class);

    /**
     * <p>Parameters passed to the property getter method of a JavaBean.</p>
     */
    private static final Object readParams[] = new Object[0];


    // ----------------------------------------------- PropertyResolver Methods


    // Specified by javax.faces.el.PropertyResolver.getValue(Object,String)
    public Object getValue(Object base, String name) {

        if ((base == null) || (name == null)) {
            throw new NullPointerException();
        }
        if (base instanceof Map) {
            return (((Map) base).get(name));
        } else if (base instanceof UIComponent) {
            Iterator kids = ((UIComponent) base).getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                if (name.equals(kid.getId())) {
                    return (kid);
                }
            }
            return (null);
        } else {
            PropertyDescriptor descriptor = descriptor(base, name);
            Method readMethod = descriptor.getReadMethod();
            if (readMethod != null) {
                try {
                    return (readMethod.invoke(base, readParams));
                } catch (IllegalAccessException e) {
                    throw new EvaluationException(e);
                } catch (InvocationTargetException ite) {
	            throw new EvaluationException(ite.getTargetException());
		}
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("getValue:Property not found:" + name);
                }
                throw new PropertyNotFoundException(name);
            }
        }

    }


    // Specified by javax.faces.el.PropertyResolver.getValue(Object,int)
    public Object getValue(Object base, int index) {

        if (base == null) {
            throw new NullPointerException();
        }
        if (base.getClass().isArray()) {
            return (Array.get(base, index));
        } else if (base instanceof List) {
            return (((List) base).get(index));
        } else if (base instanceof UIComponent) {
            return (((UIComponent) base).getChildren().get(index));
        } else {
            if (log.isDebugEnabled()) {
                log.debug("getValue:Property not found at index:" + index);
            }
            throw new PropertyNotFoundException("" + index);
        }

    }


    // Specified by javax.faces.el.PropertyResolver.setValue(Object,String,Object)
    public void setValue(Object base, String name, Object value) {

        if ((base == null) || (name == null)) {
            throw new NullPointerException();
        }
        if (base instanceof Map) {
            ((Map) base).put(name, value);
        } else if (base instanceof UIComponent) {
            if (log.isDebugEnabled()) {
                log.debug("setValue:Property not found:" + name);
            }
            throw new PropertyNotFoundException(name);
        } else {
            PropertyDescriptor descriptor = descriptor(base, name);
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod != null) {
                try {
                    writeMethod.invoke(base, new Object[] { value });
                } catch (Exception e) {
                    if (log.isDebugEnabled()) {
                        log.debug("setValue:Property not found error:", e);
                    }
                    throw new PropertyNotFoundException(e);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("setValue:Property not found:" + name);
                }
                throw new PropertyNotFoundException(name);
            }
        }

    }


    // Specified by javax.faces.el.PropertyResolver.setValue(Object,int,Object)
    public void setValue(Object base, int index, Object value) {

        if (base == null) {
            throw new NullPointerException();
        }
        if (base.getClass().isArray()) {
            Array.set(base, index, value);
        } else if (base instanceof List) {
            ((List) base).set(index, value);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("setValue:Property not found at index:" + index);
            }
            throw new PropertyNotFoundException("" + index);
        }

    }


    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,String)
    public boolean isReadOnly(Object base, String name) {
	boolean result = false;

        if ((base == null) || (name == null)) {
            throw new NullPointerException();
        }
        if (base instanceof Map) {
	    // this marker is set in ExternalContextImpl when the Map is
	    // created.
	    result = RIConstants.IMMUTABLE_MARKER == 
		((Map)base).get(RIConstants.IMMUTABLE_MARKER);
        } else if (base instanceof UIComponent) {
            result = true;
        } else {
            result = (descriptor(base, name).getWriteMethod() == null);
        }
	return result;
    }


    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,int)
    public boolean isReadOnly(Object base, int index) {

        if (base == null) {
            throw new NullPointerException();
        }
        if (base.getClass().isArray()) {
            return (false); // No way to know for sure
        } else if (base instanceof List) {
            return (false); // No way to know for sure
        } else if (base instanceof UIComponent) {
            return (true);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("isReadOnly:Property not found at index:" + index);
            }
            throw new PropertyNotFoundException("" + index);
        }

    }


    // Specified by javax.faces.el.PropertyResolver.getType(Object,String)
    public Class getType(Object base, String name) {

        if ((base == null) || (name == null)) {
            throw new NullPointerException();
        }
        if (base instanceof Map) {
            Object value = ((Map) base).get(name);
            if (value != null) {
                return (value.getClass());
            } else {
                return (null);
            }
        } else if (base instanceof UIComponent) {
            return (UIComponent.class);
        } else {
            return (descriptor(base, name).getPropertyType());
        }

    }


    // Specified by javax.faces.el.PropertyResolver.getType(Object,int)
    public Class getType(Object base, int index) {
        Class result = null;

        if (base == null) {
            throw new NullPointerException();
        }
        if (base instanceof UIComponent) {
            result = (UIComponent.class);
        }
        Class baseClass = base.getClass();
        if (baseClass.isArray()) {            
            if (index >= 0 && index <= Array.getLength(base) - 1) {
                return baseClass.getComponentType();
            } else {                
                if (log.isDebugEnabled()) {
                    log.debug("getType:index out of bounds:" + index);
                }
                throw new IndexOutOfBoundsException("" + index);
            }            
        } else if (base instanceof List) {
            result = ((List) base).get(index).getClass();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("getType:Property not found at index:" + index);
            }
            throw new PropertyNotFoundException("" + index);
        }
        return result;
    }

    // -------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>PropertyDescriptor</code> for the specified
     * property of the specified base object's class.</p>
     *
     * @param base Base object for which to retrieve a descriptor
     * @param name Name of the property to retrieve a descriptor for
     *
     * @exception PropertyNotFoundException if no PropertyDescriptor
     *  for the specified property name can be found
     */
    private PropertyDescriptor descriptor(Object base, String name)
        throws PropertyNotFoundException {

        // PENDING(craigmcc) - No caching; relying on the underlying
        // introspector to cache appropriately for performance
        try {
            PropertyDescriptor descriptors[] =
                Introspector.getBeanInfo(base.getClass()).
                getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++) {
                if (name.equals(descriptors[i].getName())) {
                    return (descriptors[i]);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Property not found while getting Descriptor for:" + name);
            }
            throw new PropertyNotFoundException(name);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Property not found exception while getting Descriptor:", e);
            }
            throw new PropertyNotFoundException(e);
        }

    }


    /**
     * <p>Convert the specified name into an int and return it.</p>
     *
     * @param name Name to be converted
     *
     * @exception PropertyNotFoundException if conversion cannot be
     *  performed
     */
    private int toInt(String name) throws PropertyNotFoundException {

        try {
            return (Integer.parseInt(name));
        } catch (NumberFormatException e) {
            if (log.isDebugEnabled()) {
                log.debug("Property not found while converting name:"+name+" to an 'int':", e);
            }
            throw new PropertyNotFoundException(name);
        }

    }


}
