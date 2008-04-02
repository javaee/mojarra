/*
 * $Id: MethodBindingImpl.java,v 1.1 2003/10/26 04:44:57 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;


/**
 * <p>Implementation of {@link MethodBinding}.</p>
 */

public class MethodBindingImpl extends MethodBinding {


    // ------------------------------------------------------------ Constructors


    public MethodBindingImpl(Application application, String ref,
                             Class args[]) {

        if ((application == null) || (ref == null)) {
            throw new NullPointerException();
        }
        this.args = args;
        if (ref.endsWith("]")) {
            int left = ref.lastIndexOf("[");
            if (left < 0) {
                throw new ReferenceSyntaxException(ref);
            }
            this.vb = application.getValueBinding(ref.substring(0, left));
            this.name = ref.substring(left + 1);
            this.name = this.name.substring(0, this.name.length() - 1);
        } else {
            int period = ref.lastIndexOf(".");
            if (period < 0) {
                throw new ReferenceSyntaxException(ref);
            }
            this.vb = application.getValueBinding(ref.substring(0, period));
            this.name = ref.substring(period + 1);
        }
        if (this.name.length() < 1) {
            throw new ReferenceSyntaxException(ref);
        }

    }


    // ------------------------------------------------------ Instance Variables


    private Class args[];
    private String name;
    private ValueBinding vb;


    // --------------------------------------------------- MethodBinding Methods


    public Object invoke(FacesContext context, Object params[])
        throws InvocationTargetException {

        if (context == null) {
            throw new NullPointerException();
        }
        Object base = vb.getValue(context);
        Method method = method(base);
        try {
            return (method.invoke(base, params));
        } catch (IllegalAccessException e) {
            throw new FacesException(e);
        }

    }


    public Class getType(FacesContext context) {

        Object base = vb.getValue(context);
        Method method = method(base);
        Class returnType = method.getReturnType();
        if ("void".equals(returnType.getName())) {
            return (null);
        } else {
            return (returnType);
        }

    }


    // --------------------------------------------------------- Private Methods


    private Method method(Object base) {

        Class clazz = base.getClass();
        try {
            return (clazz.getMethod(name, args));
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(name + ": " + e.getMessage());
        }

    }



}
