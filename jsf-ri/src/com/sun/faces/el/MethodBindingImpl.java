/*
 * $Id: MethodBindingImpl.java,v 1.6.30.2 2007/04/27 21:27:39 ofung Exp $
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


import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


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
                log.debug(
                    "Expression syntax error: Missing name after period in:" +
                    ref);
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
            return (Void.class);
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
        args = (Class[]) values[2];
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
