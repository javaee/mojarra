/*
 * $Id: MethodBindingImpl.java,v 1.2 2003/12/17 15:13:37 rkitain Exp $
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
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

import com.sun.faces.util.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Implementation of {@link MethodBinding}.</p>
 */

public class MethodBindingImpl extends MethodBinding implements StateHolder {


    private static final Log log = LogFactory.getLog(MethodBindingImpl.class);

    // ------------------------------------------------------------ Constructors

    public MethodBindingImpl() {
    }

    public MethodBindingImpl(Application application, String ref,
                             Class args[]) {
 
        if ((application == null) || (ref == null)) {
            throw new NullPointerException();
        }
	if (!(ref.startsWith("#{") && ref.endsWith("}"))) {
	    if (log.isErrorEnabled()) {
		log.error(" Expression " + ref + 
			  " does not follow the syntax #{...}");
	    }
	    throw new ReferenceSyntaxException(ref);
	}
	rawRef = ref;
	ref = Util.stripBracketsIfNecessary(ref);
	
        this.args = args;
        String vbRef = null;
        
        if (ref.endsWith("]")) {
            int left = ref.lastIndexOf("[");
            if (left < 0) {
                if (log.isDebugEnabled()) {
                    log.debug("Expression syntax error: Missing '[' in " + ref);
                }
                throw new ReferenceSyntaxException(ref);
            }
            // createValueBinding expects the expression in the VBL syntax,
            // which is of the form #{....}". So make ref conform to that.
            vbRef = "#{" + (ref.substring(0, left)) + "}";
            this.vb = application.createValueBinding(vbRef);
            this.name = ref.substring(left + 1);
            this.name = this.name.substring(0, this.name.length() - 1);
        } else {
            int period = ref.lastIndexOf(".");
            if (period < 0) {
                if (log.isDebugEnabled()) {
                    log.debug("Expression syntax error: Missing '.' in " + ref);
                }
                throw new ReferenceSyntaxException(ref);
            }
            // createValueBinding expects the expression in the VBL syntax,
            // which is of the form #{....}". So make ref conform to that.
            vbRef = "#{" + (ref.substring(0, period)) + "}";
            this.vb = application.createValueBinding(vbRef);
            this.name = ref.substring(period + 1);
        }
        if (this.name.length() < 1) {
            if (log.isDebugEnabled()) {
                log.debug("Expression syntax error: Missing name after period in:" + ref);
            }
            throw new ReferenceSyntaxException(ref);
        }

    }


    // ------------------------------------------------------ Instance Variables


    private Class args[];
    private String name;
    private String rawRef;
    private ValueBinding vb;


    // --------------------------------------------------- MethodBinding Methods


    public Object invoke(FacesContext context, Object params[])
        throws EvaluationException, MethodNotFoundException {

        if (context == null) {
            throw new NullPointerException();
        }
        Object base = vb.getValue(context);
        Method method = method(base);
        try {
            return (method.invoke(base, params));
        } catch (IllegalAccessException e) {
            throw new EvaluationException(e);
        } catch (InvocationTargetException ite) {
	    throw new EvaluationException(ite.getTargetException());
	}

    }


    public Class getType(FacesContext context) {

        Object base = vb.getValue(context);
        Method method = method(base);
        Class returnType = method.getReturnType();
        if (log.isDebugEnabled()) {
            log.debug("Method return type:" + returnType.getName());
        }
        if ("void".equals(returnType.getName())) {
            return (null);
        } else {
            return (returnType);
        }

    }

    public String getExpressionString() {
	return rawRef;
    }


    // ----------------------------------------------------- StateHolder Methods

    public Object saveState(FacesContext context) {
	Object values[] = new Object[4];
	values[0] = name;
	values[1] = UIComponentBase.saveAttachedState(context, vb);
	values[2] = args;
	values[3] = rawRef;
	return (values);
    }


    public void restoreState(FacesContext context, Object state) {
	Object values[] = (Object[]) state;
	name = (String) values[0];
	vb = (ValueBinding) UIComponentBase.restoreAttachedState(context, 
								 values[1]);
	args = (Class []) values[2];
	rawRef = (String) values[3];
    }


    private boolean transientFlag = false;


    public boolean isTransient() {
	return (this.transientFlag);
    }


    public void setTransient(boolean transientFlag) {
	this.transientFlag = transientFlag;
    }



    // --------------------------------------------------------- Private Methods


    private Method method(Object base) {
	if (null == base) {
	    throw new MethodNotFoundException(name);
	}

        Class clazz = base.getClass();
        try {
            return (clazz.getMethod(name, args));
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(name + ": " + e.getMessage());
        }

    }



}
