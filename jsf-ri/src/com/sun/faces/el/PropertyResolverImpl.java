/*
 * $Id: PropertyResolverImpl.java,v 1.14.30.4 2007/04/27 21:27:40 ofung Exp $
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

package com.sun.faces.el;


import com.sun.faces.RIConstants;
import com.sun.faces.el.impl.BeanInfoManager;
import com.sun.faces.el.impl.BeanInfoProperty;
import com.sun.faces.el.impl.Coercions;
import com.sun.faces.el.impl.ElException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
                    Method writeMethod = bip.getWriteMethod();
                    Class argType = bip.getPropertyDescriptor().getPropertyType();
                    Object result = value;
                    if (value != null) {
                        result = Coercions.coerce(value, argType);
                    }
                    bip.getWriteMethod().invoke(base, new Object[]{result});
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
                } catch (ElException e) {
                    String message = "Error setting property '" +
                        name + "' in bean of type " +
                        base.getClass().getName() + ": " + e;
                    throw new EvaluationException(message, e);
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
