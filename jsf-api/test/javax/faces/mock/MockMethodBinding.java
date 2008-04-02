/*
 * $Id: MockMethodBinding.java,v 1.2 2003/10/25 22:08:49 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


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
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;


/**
 * <p>Mock implementation of {@link MethodBinding} that supports a limited
 * subset of expression evaluation functionality:</p>
 * <ul>
 * <li>The portion of the method reference expression before the final
 *     "." must conform to the limitations of {@link MockValueBinding}.</li>
 * <li>The name of the method to be executed cannot be delimited by "[]".</li>
 * </ul>
 */

public class MockMethodBinding extends MethodBinding {


    // ------------------------------------------------------------ Constructors


    public MockMethodBinding(Application application, String ref,
                             Class args[]) {

        if ((application == null) || (ref == null)) {
            throw new NullPointerException();
        }
        this.application = application;
        this.args = args;
        this.ref = ref;
        int period = ref.lastIndexOf(".");
        if (period < 0) {
            throw new ReferenceSyntaxException(ref);
        }
        vb = application.getValueBinding(ref.substring(0, period));
        name = ref.substring(period + 1);
        if (name.length() < 1) {
            throw new ReferenceSyntaxException(ref);
        }

    }


    // ------------------------------------------------------ Instance Variables


    private Application application;
    private Class args[];
    private String name;
    private String ref;
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


    // Package private so that unit tests can call this
    Method method(Object base) {

        Class clazz = base.getClass();
        try {
            return (clazz.getMethod(name, args));
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(ref + ": " + e.getMessage());
        }

    }



}
