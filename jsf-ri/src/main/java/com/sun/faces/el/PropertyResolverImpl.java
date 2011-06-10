/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

        Class<?> type = base.getClass();
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

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            return context.getApplication().getELResolver().getType(context.getELContext(), base,property);
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
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

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            return context.getApplication().getELResolver().getValue(context.getELContext(), base,property);
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
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

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            return context.getApplication().getELResolver().isReadOnly(context.getELContext(), base,property);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
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
        Class<?> type = base.getClass();
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
                //noinspection unchecked
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
                        base,
                        index));
        }
    }
}
