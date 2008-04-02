/*
 * $Id: PropertyResolverImpl.java,v 1.15 2004/11/09 04:23:10 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import com.sun.faces.el.impl.ELSupport;

import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Concrete implementation of <code>PropertyResolver</code>.
 * </p>
 */

public class PropertyResolverImpl extends PropertyResolver
{
    // ------------------------------------------------------- Static Variables

    protected static final Object[] EMPTY_ARRAY = new Object[0];

    // ------------------------------------------------------- Static Methods

    public static Object coerceToType(Object obj, Class type)
            throws EvaluationException
    {
        try
        {
            return ELSupport.coerceToType(obj, type);
        }
        catch (IllegalArgumentException iae)
        {
            throw new EvaluationException(iae.getMessage(), iae);
        }
    }

    public static PropertyDescriptor getPropertyDescriptor(Class baseType,
            String name) throws EvaluationException, PropertyNotFoundException
    {
        try
        {
            PropertyDescriptor desc = null;
            BeanInfo beanInfo = Introspector.getBeanInfo(baseType);
            PropertyDescriptor[] pda = beanInfo.getPropertyDescriptors();
            int i = 0;
            while (i < pda.length && desc == null)
            {
                if (pda[i].getName().equals(name))
                {
                    desc = pda[i];
                }
                i++;
            }
            if (desc == null)
                    throw new PropertyNotFoundException(ELSupport.msg(
                            "el.error.property.notfound", baseType, name));
            return desc;
        }
        catch (IntrospectionException ie)
        {
            throw new EvaluationException(ELSupport.msg("el.error.property",
                    baseType), ie);
        }
    }

    protected static void assertInput(Object base, Object property)
            throws PropertyNotFoundException
    {
        if (base == null || property == null)
        {
            throw new PropertyNotFoundException(ELSupport.msg(
                    "el.error.property.input.both", base, property));
        }
    }

    protected static void assertInput(Object base)
    {
        if (base == null)
        {
            throw new PropertyNotFoundException(ELSupport.msg(
                    "el.error.property.input", base));
        }
    }

    protected static void assertInput(Object base, int index)
            throws PropertyNotFoundException
    {
        if (base == null)
        {
            throw new PropertyNotFoundException(ELSupport.msg(
                    "el.error.property.array.outofbounds", base, "" + index));
        }
        if (index < 0)
        {
            throw new PropertyNotFoundException(ELSupport.msg(
                    "el.error.property.array.outofbounds", base, "" + index));
        }
    }

    // Specified by javax.faces.el.PropertyResolver.getType(Object,int)
    public Class getType(Object base, int index)
    {
        // validates base != null and index >= 0
        assertInput(base, index);

        Class type = base.getClass();
        try
        {
            if (type.isArray())
            {
                Array.get(base, index);
                return type.getComponentType();
            }
            else if (base instanceof List)
            {
                Object value = ((List) base).get(index);
                return (value != null) ? value.getClass() : null;
            }
            else
            {
                throw new EvaluationException(ELSupport.msg(
                        "el.error.property.array.type", base));
            }
        }
        catch (ArrayIndexOutOfBoundsException aioobe)
        {
            throw new PropertyNotFoundException(ELSupport.msg(
                    "el.error.property.array.outofbounds.size", base,
                    "" + index, "" + Array.getLength(base)));
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            throw new PropertyNotFoundException(ELSupport.msg(
                    "el.error.property.array.outofbounds.size", base,
                    "" + index, "" + ((List) base).size()));
        }
    }

    // Specified by javax.faces.el.PropertyResolver.getType(Object,String)
    public Class getType(Object base, Object property)
    {
        // validates base and property
        assertInput(base, property);

        if (base instanceof Map)
        {
            Object value = ((Map) base).get(property);
            return (value != null) ? value.getClass() : null;
        }
        else if (base instanceof List || base.getClass().isArray())
        {
            int index = ELSupport.coerceToNumber(property, Integer.class).intValue();
            return this.getType(base, index);
        }
        else
        {
            PropertyDescriptor desc = getPropertyDescriptor(base.getClass(),
                    property.toString());
            return desc.getPropertyType();
        }
    }

    // Specified by javax.faces.el.PropertyResolver.getValue(Object,int)
    public Object getValue(Object base, int index)
    {
        // validates base and index
        if (base == null) return null;

        Class type = base.getClass();
        if (base.getClass().isArray())
        {
            try
            {
                return Array.get(base, index);
            }
            catch (ArrayIndexOutOfBoundsException aioobe)
            {
                return null;
            }
        }
        else if (base instanceof List)
        {
            try
            {
                return ((List) base).get(index);
            }
            catch (IndexOutOfBoundsException ioobe)
            {
                return null;
            }
        }
        else
        {
            throw new EvaluationException(ELSupport.msg(
                    "el.error.property.array.type", base));
        }
    }

    // Specified by javax.faces.el.PropertyResolver.getValue(Object,String)
    public Object getValue(Object base, Object property)
    {
        // validate input for null
        if (base == null || property == null) return null;

        if (base instanceof Map)
        {
            return ((Map) base).get(property);
        }
        else if (base instanceof List || base.getClass().isArray())
        {
            int index = ELSupport.coerceToNumber(property, Integer.class).intValue();
            return this.getValue(base, index);
        }
        else
        {
            PropertyDescriptor desc = getPropertyDescriptor(base.getClass(),
                    property.toString());

            try
            {
                Method method = desc.getReadMethod();
                if (method == null)
                        throw new PropertyNotFoundException(ELSupport.msg(
                                "el.error.property.noread", base, property));
                return method.invoke(base, EMPTY_ARRAY);
            }
            catch (IllegalAccessException iae)
            {
                throw new EvaluationException(ELSupport.msg(
                        "el.error.property.get.access", base, property), iae);
            }
            catch (InvocationTargetException ite)
            {
                throw new EvaluationException(ELSupport.msg(
                        "el.error.property.get.invoke", base, property), ite.getCause());
            }
        }
    }

    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,int)
    public boolean isReadOnly(Object base, int index)
    {
        // validate input
        assertInput(base, index);

        Class type = base.getClass();
        if (base instanceof List || base.getClass().isArray())
        {
            return false;
        }
        else
        {
            throw new EvaluationException(ELSupport.msg(
                    "el.error.property.array.type", base));
        }
    }

    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,String)
    public boolean isReadOnly(Object base, Object property)
    {
        // validate the input
        assertInput(base, property);

        if (base instanceof Map)
        {
            Object value = ((Map) base).get(property);
            try
            {
                ((Map) base).put(property, value);
                return false;
            }
            catch (Exception e)
            {
                return true;
            }
        }
        else if (base instanceof List || base.getClass().isArray())
        {
            int index = ELSupport.coerceToNumber(property, Integer.class).intValue();
            return this.isReadOnly(base, index);
        }
        else
        {
            PropertyDescriptor desc = getPropertyDescriptor(base.getClass(),
                    property.toString());

            Method method = desc.getWriteMethod();
            return (method == null);
        }
    }

    // Specified by javax.faces.el.PropertyResolver.setValue(Object,int,Object)
    public void setValue(Object base, int index, Object value)
    {
        // validate input
        assertInput(base, index);

        Class type = base.getClass();
        if (type.isArray())
        {
            try
            {
                Array.set(base, index, coerceToType(value, type
                        .getComponentType()));
            }
            catch (ArrayIndexOutOfBoundsException aioobe)
            {
                throw new PropertyNotFoundException(ELSupport.msg(
                        "el.error.property.array.outofbounds.size", base, ""
                                + index, "" + Array.getLength(base)));
            }
        }
        else if (base instanceof List)
        {
            try
            {
                ((List) base).set(index, value);
            }
            catch (IndexOutOfBoundsException ioobe)
            {
                throw new PropertyNotFoundException(ELSupport.msg(
                        "el.error.property.array.outofbounds.size", base, ""
                                + index, "" + ((List) base).size()));
            }
        }
        else
        {
            throw new EvaluationException(ELSupport.msg(
                    "el.error.property.array.type", base));
        }
    }

    // Specified by
    // javax.faces.el.PropertyResolver.setValue(Object,String,Object)
    public void setValue(Object base, Object property, Object value)
    {
        // validate input
        assertInput(base, property);

        if (base instanceof Map)
        {
            ((Map) base).put(property, value);
        }
        else if (base instanceof List || base.getClass().isArray())
        {
            int index = ELSupport.coerceToNumber(property, Integer.class).intValue();
            this.setValue(base, index, value);
        }
        else
        {
            PropertyDescriptor desc = getPropertyDescriptor(base.getClass(),
                    property.toString());

            try
            {
                Method method = desc.getWriteMethod();
                if (method != null)
                {
                    Object obj = coerceToType(value, desc.getPropertyType());
                    method.invoke(base, new Object[]
                    { obj });
                }
                else
                {
                    throw new PropertyNotFoundException(ELSupport.msg(
                            "el.error.property.readOnly", base, property
                                    .toString()));
                }
            }
            catch (IllegalAccessException iae)
            {
                throw new EvaluationException(ELSupport.msg(
                        "el.error.property.set.access", base, property
                                .toString()), iae);
            }
            catch (InvocationTargetException ite)
            {
                throw new EvaluationException(ELSupport.msg(
                        "el.error.property.set.invoke", base, property
                                .toString()), ite.getCause());
            }
        }
    }
}