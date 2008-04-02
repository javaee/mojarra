/*
 * $Id: PropertyResolverImpl.java,v 1.31 2007/02/28 21:28:19 rlubke Exp $
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
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

import java.lang.reflect.Array;
import java.util.List;

import com.sun.faces.util.MessageUtils;

/**
 * <p>
 * Concrete implementation of <code>PropertyResolver</code>.
 * </p>
 */
@SuppressWarnings("deprecation")
public class PropertyResolverImpl extends PropertyResolver {


    private PropertyResolver delegate;


    // ------------------------------------------- Methods from PropertyResolver


    // Specified by javax.faces.el.PropertyResolver.getType(Object,int)
    public Class getType(Object base, int index)
        throws EvaluationException, PropertyNotFoundException{

        // validates base != null and index >= 0
        assertInput(base, index);

        if (delegate != null) {
            return delegate.getType(base, index);
        }

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
                        base));
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                        MessageUtils.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                        base,
                        index,
                        Array.getLength(base)));
        } catch (IndexOutOfBoundsException ioobe) {
           throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                        MessageUtils.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                        base,
                        index,
                        ((List)base).size()));
        }
    }

    // Specified by javax.faces.el.PropertyResolver.getType(Object,String)
    public Class getType(Object base, Object property) {

        assertInput(base, property);

        if (delegate != null) {
            return delegate.getType(base, property);
        }

        Class result = null;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            result = context.getApplication().getELResolver().getType(context.getELContext(), base,property);
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

        if (delegate != null) {
            return delegate.getValue(base, index);
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
                        base));
        }

    }

    // Specified by javax.faces.el.PropertyResolver.getValue(Object,String)
    public Object getValue(Object base, Object property) {

        if (delegate != null) {
            return delegate.getValue(base, property);
        }

        Object result = null;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            result = context.getApplication().getELResolver().getValue(context.getELContext(), base,property);
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }

    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,int)
    public boolean isReadOnly(Object base, int index) {

        // validate input
        assertInput(base, index);

        if (delegate != null) {
            return delegate.isReadOnly(base, index);
        }

        if (base instanceof List || base.getClass().isArray()) {
            return false;
        } else {
            throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                        MessageUtils.EL_PROPERTY_TYPE_ERROR_ID,
                        base));
        }
    }

    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,String)
    public boolean isReadOnly(Object base, Object property) {

        if (delegate != null) {
            return delegate.isReadOnly(base, property);
        }

        boolean result = false;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            result = context.getApplication().getELResolver().isReadOnly(context.getELContext(), base,property);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }

    // Specified by javax.faces.el.PropertyResolver.setValue(Object,int,Object)
    public void setValue(Object base, int index, Object value) {

        // validate input
        assertInput(base, index);

        if (delegate != null) {
            delegate.setValue(base, index, value);
            return;
        }
        
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
                        base,
                        index,
                        Array.getLength(base)));
            }
        } else if (base instanceof List) {
            try {
            	// Inherently not type safe, but nothing can be done about it.
                ((List) base).set(index, value);
            } catch (IndexOutOfBoundsException ioobe) {
                throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                        MessageUtils.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                        base,
                        index,
                        ((List)base).size()));
            }
        } else {
           throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                        MessageUtils.EL_PROPERTY_TYPE_ERROR_ID,
                        base));
        }
    }

    // Specified by
    // javax.faces.el.PropertyResolver.setValue(Object,String,Object)
    public void setValue(Object base, Object property, Object value) {

        if (delegate != null) {
            delegate.setValue(base, property, value);
            return;
        }

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getApplication().getELResolver().setValue(context.getELContext(), base,property, value);
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (javax.el.PropertyNotWritableException pnwe) {
            throw new PropertyNotFoundException(pnwe);
        }
        catch (ELException elex) {
            throw new EvaluationException(elex);
        }
    }


    // ---------------------------------------------------------- Public Methods


    public void setDelegate(PropertyResolver delegate) {

        this.delegate = delegate;

    }

    protected static void assertInput(Object base, Object property)
            throws PropertyNotFoundException {
        if (base == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base");
            throw new PropertyNotFoundException(message);
        }
        if (property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "property");
            throw new PropertyNotFoundException(message);
        }
    }

    protected static void assertInput(Object base, int index)
            throws PropertyNotFoundException {
        if (base == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base");
            throw new PropertyNotFoundException(message);
        }
        if (index < 0) {
            throw new PropertyNotFoundException(MessageUtils.getExceptionMessageString(
                        MessageUtils.EL_OUT_OF_BOUNDS_ERROR_ID,
                        new Object[]{base, new Integer(index)}));
        }
    }
}
