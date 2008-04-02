/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.sun.faces.el.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;


/**
 *
 * <p>Represents a function call.</p>
 * 
 * @author Shawn Bayern (in the style of Nathan's other classes)
 **/

public class FunctionInvocation
    extends Expression {
    //-------------------------------------
    // Constants
    //-------------------------------------
    private static Log log = LogFactory.getLog(FunctionInvocation.class);
    
    //-------------------------------------
    // Properties
    //-------------------------------------
    // property index

    private String functionName;
    private List argumentList;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String f) {
        functionName = f;
    }

    public List getArgumentList() {
        return argumentList;
    }

    public void setArgumentList(List l) {
        argumentList = l;
    }

    //-------------------------------------
    /**
     * Constructor
     **/
    public FunctionInvocation(String functionName, List argumentList) {
        this.functionName = functionName;
        this.argumentList = argumentList;
    }

    //-------------------------------------
    // Expression methods
    //-------------------------------------
    /**
     * Returns the expression in the expression language syntax
     **/
    public String getExpressionString() {
        StringBuffer b = new StringBuffer();
        b.append(functionName);
        b.append("(");
        Iterator i = argumentList.iterator();
        while (i.hasNext()) {
            b.append(((Expression) i.next()).getExpressionString());
            if (i.hasNext())
                b.append(", ");
        }
        b.append(")");
        return b.toString();
    }


    //-------------------------------------
    /**
     *
     * Evaluates by looking up the name in the VariableResolver
     **/
    public Object evaluate(
        ExpressionInfo exprInfo)
        throws ElException {
        FunctionMapper functions = exprInfo.getFunctionMapper();
        // if the Map is null, then the function is invalid
        if (functions == null) {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.UNKNOWN_FUNCTION, functionName);
                log.error(message);
                throw new ElException(message);
            }
        }            

        // normalize function name
        String prefix = null;
        String localName = null;
        int index = functionName.indexOf(':');
        if (index == -1) {
            prefix = "";
            localName = functionName;
        } else {
            prefix = functionName.substring(0, index);
            localName = functionName.substring(index + 1);
        }

        // ensure that the function's name is mapped
        Method target = (Method) functions.resolve(prefix, localName);
        if (target == null) {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.UNKNOWN_FUNCTION, functionName);
                log.error(message);
                throw new ElException(message);
            }
        }      

        // ensure that the number of arguments matches the number of parameters
        Class[] params = target.getParameterTypes();
        if (params.length != argumentList.size()) {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.INAPPROPRIATE_FUNCTION_ARG_COUNT,
                    functionName, new Integer(params.length),
                    new Integer(argumentList.size()));
                log.error(message);
                throw new ElException(message);
            }
        }

        // now, walk through each parameter, evaluating and casting its argument
        Object[] arguments = new Object[argumentList.size()];
        for (int i = 0; i < params.length; i++) {
            // evaluate
            arguments[i] = ((Expression) argumentList.get(i)).evaluate(
                exprInfo);
            // coerce
            arguments[i] = Coercions.coerce(arguments[i], params[i]);
        }

        // finally, invoke the target method, which we know to be static
        try {
            return (target.invoke(null, arguments));
        } catch (InvocationTargetException ex) {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.FUNCTION_INVOCATION_ERROR,
                    functionName);
                Throwable t = ex.getTargetException();
                log.error(message, t);
                throw new ElException(message, t);
            }
            return null;
        } catch (Throwable t) {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.FUNCTION_INVOCATION_ERROR,
                    functionName);
                log.error(message, t);
                throw new ElException(message, t);
            }
            return null;
        }
    }

    //-------------------------------------
}
