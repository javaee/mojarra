/*
 * $Id: PropertyResolverImpl.java,v 1.22 2006/03/29 22:38:33 rlubke Exp $
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

package com.sun.faces.el;

import javax.el.ELException;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

import java.lang.reflect.Array;
import java.util.List;

import com.sun.faces.util.MessageUtils;

/**
 * <p/>
 * Concrete implementation of <code>PropertyResolver</code>.
 * </p>
 */

public class PropertyResolverImpl extends PropertyResolver {


    private ELResolver elResolver = null;

    // ------------------------------------------------------------ Constructors


    public PropertyResolverImpl(ELResolver resolver) {

        this.elResolver = resolver;

    }

    // ---------------------------------------------------------- Public Methods


    // Specified by javax.faces.el.PropertyResolver.getType(Object,String)
    public Class getType(Object base, Object property) {

        assertInput(base, property);
        Class result = null;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            result = elResolver.getType(context.getELContext(), base, property);
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;

    }


    // Specified by javax.faces.el.PropertyResolver.getType(Object,int)
    public Class getType(Object base, int index)
          throws EvaluationException, PropertyNotFoundException {

        // validates base != null and index >= 0
        assertInput(base, index);

        Class<? extends Object> type = base.getClass();
        try {
            if (type.isArray()) {
                Array.get(base, index);
                return type.getComponentType();
            } else if (base instanceof List) {
                Object value = ((List) base).get(index);
                return (value != null) ? value.getClass() : null;
            } else {
                throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                      MessageUtils.EL_PROPERTY_TYPE_ERROR_ID,
                      new Object[]{base}));
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                  MessageUtils.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                  new Object[]{base, new Integer(index),
                               new Integer(Array.getLength(base))}));
        } catch (IndexOutOfBoundsException ioobe) {
            throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                  MessageUtils.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                  new Object[]{base, new Integer(index),
                               new Integer(((List) base).size())}));
        }

    }


    // Specified by javax.faces.el.PropertyResolver.getValue(Object,String)
    public Object getValue(Object base, Object property) {

        Object result = null;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            result =
                  elResolver.getValue(context.getELContext(), base, property);
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;

    }


    // Specified by javax.faces.el.PropertyResolver.getValue(Object,int)
    public Object getValue(Object base, int index) {

        // validates base and index
        if (base == null) {
            return null;
        }

        if (base.getClass().isArray()) {
            try {
                return Array.get(base, index);
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                return null;
            }
        } else if (base instanceof List) {
            try {
                return ((List) base).get(index);
            } catch (IndexOutOfBoundsException ioobe) {
                return null;
            }
        } else {
            throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                  MessageUtils.EL_PROPERTY_TYPE_ERROR_ID,
                  new Object[]{base}));
        }

    }


    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,String)
    public boolean isReadOnly(Object base, Object property) {

        boolean result = false;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            result =
                  elResolver.isReadOnly(context.getELContext(), base, property);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;

    }


    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,int)
    public boolean isReadOnly(Object base, int index) {

        // validate input
        assertInput(base, index);

        if (base instanceof List || base.getClass().isArray()) {
            return false;
        } else {
            throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                  MessageUtils.EL_PROPERTY_TYPE_ERROR_ID,
                  new Object[]{base}));
        }

    }


    // Specified by
    // javax.faces.el.PropertyResolver.setValue(Object,String,Object)
    public void setValue(Object base, Object property, Object value) {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            elResolver.setValue(context.getELContext(), base, property, value);
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (javax.el.PropertyNotWritableException pnwe) {
            throw new PropertyNotFoundException(pnwe);
        }
        catch (ELException elex) {
            throw new EvaluationException(elex);
        }

    }


    // Specified by javax.faces.el.PropertyResolver.setValue(Object,int,Object)
    public void setValue(Object base, int index, Object value) {

        // validate input
        assertInput(base, index);
        FacesContext context = FacesContext.getCurrentInstance();
        Class<? extends Object> type = base.getClass();
        if (type.isArray()) {
            try {
                Array.set(base, index, (context.getApplication().
                      getExpressionFactory()).coerceToType(value, type
                      .getComponentType()));
            }
            catch (ArrayIndexOutOfBoundsException aioobe) {
                throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                      MessageUtils.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                      new Object[]{base, new Integer(index),
                                   new Integer(Array.getLength(base))}));
            }
        } else if (base instanceof List) {
            try {
                ((List) base).set(index, value);
            } catch (IndexOutOfBoundsException ioobe) {
                throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                      MessageUtils.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                      new Object[]{base, new Integer(index),
                                   new Integer(((List) base).size())}));
            }
        } else {
            throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                  MessageUtils.EL_PROPERTY_TYPE_ERROR_ID,
                  new Object[]{base}));
        }

    }

    // ------------------------------------------------------- Protected Methods


    protected static void assertInput(Object base, int index)
          throws PropertyNotFoundException {

        if (base == null) {
            String message = MessageUtils.getExceptionMessageString
                  (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base;
            throw new PropertyNotFoundException(message);
        }
        if (index < 0) {
            throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                  MessageUtils.EL_OUT_OF_BOUNDS_ERROR_ID,
                  new Object[]{base, new Integer(index)}));
        }

    }


    protected static void assertInput(Object base, Object property)
          throws PropertyNotFoundException {

        if (base == null || property == null) {
            String message = MessageUtils.getExceptionMessageString
                  (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }

    }

}
