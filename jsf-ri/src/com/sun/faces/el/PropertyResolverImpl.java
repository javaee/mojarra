/*
 * $Id: PropertyResolverImpl.java,v 1.14 2004/02/26 20:32:40 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;


import com.sun.faces.RIConstants;
import com.sun.faces.el.impl.BeanInfoManager;
import com.sun.faces.el.impl.BeanInfoProperty;
import com.sun.faces.el.impl.Coercions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


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

    // Zero-argument array
    private static final Object[] sNoArgs = new Object[0];

    // ----------------------------------------------- PropertyResolver Methods


    // Specified by javax.faces.el.PropertyResolver.getValue(Object,String)
    public Object getValue(Object base, Object property) {

        if ((base == null) || (property == null)) {
            return null;
        }
        if (base instanceof Map) {
            return (((Map) base).get(property));
        } else {
            String name = null;
            BeanInfoProperty bip = null;
            try {
                name = Coercions.coerceToString(property);
                bip =
                    BeanInfoManager.getBeanInfoProperty(base.getClass(), name);
            } catch (Throwable t) {
                // PENDING (hans) Align with std message handling
                String message = "Error finding property '" +
                    name + "' from bean of type " +
                    base.getClass().getName() + ": " + t;
                if (log.isDebugEnabled()) {
                    log.debug(message, t);
                }
                throw new PropertyNotFoundException(message, t);
            }
            if (bip != null && bip.getReadMethod() != null) {
                try {
                    return bip.getReadMethod().invoke(base, sNoArgs);
                } catch (InvocationTargetException exc) {
                    // PENDING (hans) Align with std message handling
                    Throwable t = exc.getTargetException();
                    String message = "Error getting property '" +
                        name + "' from bean of type " +
                        base.getClass().getName() + ": " + t;
                    if (log.isDebugEnabled()) {
                        log.debug(message, t);
                    }
                    throw new EvaluationException(message, t);
                } catch (IllegalAccessException t) {
                    // PENDING (hans) Align with std message handling
                    String message = "Error getting property '" +
                        name + "' from bean of type " +
                        base.getClass().getName() + ": " + t;
                    if (log.isDebugEnabled()) {
                        log.debug(message, t);
                    }
                    throw new EvaluationException(message, t);
                }
            } else {
                // No readable property with this name
                String message = "Error getting property '" +
                    name + "' from bean of type " + base.getClass().getName();
                if (log.isDebugEnabled()) {
                    log.debug(message);
                }
                throw new PropertyNotFoundException(message);
            }
        }

    }


    // Specified by javax.faces.el.PropertyResolver.getValue(Object,int)
    public Object getValue(Object base, int index) {

        if (base == null) {
            return null;
        }

        Object result = null;
        try {
            if (base.getClass().isArray()) {
                result = (Array.get(base, index));
            } else if (base instanceof List) {
                result = (((List) base).get(index));
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("getValue:Property not found at index:" + index);
                }
                throw new EvaluationException("Bean of type " +
                                              base.getClass().getName() +
                                              " doesn't have indexed properties");
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore according to spec
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("getValue:Property not found at index:" + index);
            }
            throw new EvaluationException("Error getting index " + index, t);
        }
        return result;
    }


    // Specified by javax.faces.el.PropertyResolver.setValue(Object,String,Object)
    public void setValue(Object base, Object property, Object value) {

        if ((base == null) || (property == null)) {
            String className = base == null ?
                "null" : base.getClass().getName();
            throw new PropertyNotFoundException("Error setting property '" +
                                                property + "' in bean of type " + className);
        }

        if (base instanceof Map) {
            ((Map) base).put(property, value);
        } else {
            String name = null;
            BeanInfoProperty bip = null;
            try {
                name = Coercions.coerceToString(property);
                bip =
                    BeanInfoManager.getBeanInfoProperty(base.getClass(), name);
            } catch (Throwable t) {
                // PENDING (hans) Align with std message handling
                String message = "Error finding property '" +
                    name + "' in bean of type " +
                    base.getClass().getName() + ": " + t;
                if (log.isDebugEnabled()) {
                    log.debug(message, t);
                }
                throw new PropertyNotFoundException(message, t);
            }
            if (bip != null && bip.getWriteMethod() != null) {
                try {
                    bip.getWriteMethod().invoke(base, new Object[]{value});
                } catch (InvocationTargetException exc) {
                    // PENDING (hans) Align with std message handling
                    Throwable t = exc.getTargetException();
                    String message = "Error setting property '" +
                        name + "' in bean of type " +
                        base.getClass().getName() + ": " + t;
                    if (log.isDebugEnabled()) {
                        log.debug(message, t);
                    }
                    throw new EvaluationException(message, t);
                } catch (IllegalAccessException t) {
                    // PENDING (hans) Align with std message handling
                    String message = "Error setting property '" +
                        name + "' in bean of type " +
                        base.getClass().getName() + ": " + t;
                    if (log.isDebugEnabled()) {
                        log.debug(message, t);
                    }
                    throw new EvaluationException(message, t);
                }
            } else {
                // No write property with this name
                String message = "Error setting property '" +
                    name + "' in bean of type " + base.getClass().getName();
                if (log.isDebugEnabled()) {
                    log.debug(message);
                }
                throw new PropertyNotFoundException(message);
            }
        }

    }


    // Specified by javax.faces.el.PropertyResolver.setValue(Object,int,Object)
    public void setValue(Object base, int index, Object value) {

        if (base == null) {
            throw new PropertyNotFoundException("Error setting index '" +
                                                index + "' in bean of type null");
        }

        try {
            if (base.getClass().isArray()) {
                Array.set(base, index, value);
            } else if (base instanceof List) {
                ((List) base).set(index, value);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("setValue:Error setting index:" + index);
                }
                throw new EvaluationException("Bean of type " +
                                              base.getClass().getName() +
                                              " doesn't have indexed properties");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new PropertyNotFoundException("Error setting index " +
                                                index, e);
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("setValue:Error setting index:" + index);
            }
            throw new EvaluationException("Error setting index " + index, t);
        }

    }


    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,String)
    public boolean isReadOnly(Object base, Object property) {
        boolean result = false;

        if ((base == null) || (property == null)) {
            String className = base == null ?
                "null" : base.getClass().getName();
            throw new PropertyNotFoundException("Error testing property '" +
                                                property + "' in bean of type " + className);
        }

        if (base instanceof Map) {
            // this marker is set in ExternalContextImpl when the Map is
            // created.
            // PENDING (hans) Isn't there a better way to handle this?
            result = RIConstants.IMMUTABLE_MARKER ==
                ((Map) base).get(RIConstants.IMMUTABLE_MARKER);
        } else {
            String name = null;
            BeanInfoProperty bip = null;
            try {
                name = Coercions.coerceToString(property);
                bip =
                    BeanInfoManager.getBeanInfoProperty(base.getClass(), name);
            } catch (Throwable t) {
                // PENDING (hans) Align with std message handling
                String message = "Error finding property '" +
                    name + "' in bean of type " +
                    base.getClass().getName() + ": " + t;
                if (log.isDebugEnabled()) {
                    log.debug(message, t);
                }
                throw new PropertyNotFoundException(message, t);
            }
            if (bip != null && bip.getWriteMethod() == null) {
                result = true;
            } else if (bip == null) {
                // PENDING (hans) Align with std message handling
                String message = "Error finding property '" +
                    name + "' in bean of type " +
                    base.getClass().getName();
                if (log.isDebugEnabled()) {
                    log.debug(message);
                }
                throw new PropertyNotFoundException(message);
            }
        }
        return result;
    }


    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,int)
    public boolean isReadOnly(Object base, int index) {
        boolean result = false;

        if (base == null) {
            throw new PropertyNotFoundException("Error setting index '" +
                                                index + "' in bean of type null");
        }

        try {
            // Try to read the index, to trigger exceptions if any
            if (base.getClass().isArray()) {
                Array.get(base, index);
                result = false; // No way to know for sure
            } else if (base instanceof List) {
                ((List) base).get(index);
                result = false; // No way to know for sure
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("getValue:Property not found at index:" + index);
                }
                throw new EvaluationException("Bean of type " +
                                              base.getClass().getName() +
                                              " doesn't have indexed properties");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new PropertyNotFoundException("Error setting index " +
                                                index, e);
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("setValue:Error setting index:" + index);
            }
            throw new EvaluationException("Error setting index " + index, t);
        }
        return result;
    }


    // Specified by javax.faces.el.PropertyResolver.getType(Object,String)
    public Class getType(Object base, Object property) {

        if ((base == null) || (property == null)) {
            String className = base == null ?
                "null" : base.getClass().getName();
            throw new PropertyNotFoundException("Error testing property '" +
                                                property + "' in bean of type " + className);
        }

        if (base instanceof Map) {
            Object value = ((Map) base).get(property);
            if (value != null) {
                return (value.getClass());
            } else {
                return (null);
            }
        } else {
            String name = null;
            BeanInfoProperty bip = null;
            try {
                name = Coercions.coerceToString(property);
                bip =
                    BeanInfoManager.getBeanInfoProperty(base.getClass(), name);
            } catch (Throwable t) {
                // PENDING (hans) Align with std message handling
                String message = "Error finding property '" +
                    name + "' in bean of type " +
                    base.getClass().getName() + ": " + t;
                if (log.isDebugEnabled()) {
                    log.debug(message, t);
                }
                throw new PropertyNotFoundException(message, t);
            }
            if (bip != null) {
                return bip.getPropertyDescriptor().getPropertyType();
            } else {
                // PENDING (hans) Align with std message handling
                String message = "Error finding property '" +
                    name + "' in bean of type " +
                    base.getClass().getName();
                if (log.isDebugEnabled()) {
                    log.debug(message);
                }
                throw new PropertyNotFoundException(message);
            }
        }
    }


    // Specified by javax.faces.el.PropertyResolver.getType(Object,int)
    public Class getType(Object base, int index) {
        Class result = null;

        if (base == null) {
            throw new PropertyNotFoundException("Error setting index '" +
                                                index + "' in bean of type null");
        }

        try {
            // Try to read the index, to trigger exceptions if any
            if (base.getClass().isArray()) {
                Array.get(base, index);
                result = base.getClass().getComponentType();
            } else if (base instanceof List) {
                Object o = ((List) base).get(index);
                if (o != null) {
                    result = o.getClass();
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("getValue:Property not found at index:" + index);
                }
                throw new EvaluationException("Bean of type " +
                                              base.getClass().getName() +
                                              " doesn't have indexed properties");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new PropertyNotFoundException("Error getting index " +
                                                index, e);
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("getType:Error getting index:" + index);
            }
            throw new EvaluationException("Error getting index " + index, t);
        }
        return result;
    }

}
