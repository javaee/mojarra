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

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.el.VariableResolver;

/**
 *
 * <p>Represents a name that can be used as the first element of a
 * value.
 * 
 * @author Nathan Abramson - Art Technology Group
 * @author Shawn Bayern
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: eburns $
 **/

public class NamedValue
    extends Expression {
    //-------------------------------------
    // Constants
    //-------------------------------------

    //-------------------------------------
    // Properties
    //-------------------------------------
    // property name

    String mName;

    public String getName() {
        return mName;
    }

    //-------------------------------------
    /**
     *
     * Constructor
     **/
    public NamedValue(String pName) {
        mName = pName;
    }

    //-------------------------------------
    // Expression methods
    //-------------------------------------
    /**
     *
     * Returns the expression in the expression language syntax
     **/
    public String getExpressionString() {
        return StringLiteral.toIdentifierToken(mName);
    }

    //-------------------------------------
    /**
     *
     * Evaluates by looking up the name in the VariableResolver
     **/
    public Object evaluate(ExpressionInfo exprInfo)
        throws ElException {
       
        VariableResolver resolver = exprInfo.getVariableResolver();
	FacesContext context = exprInfo.getFacesContext();
        if (resolver == null) {
            return null;
        } else {
            return resolver.resolveVariable(context, mName);
        }
    }

    public void setValue(ExpressionInfo exprInfo, Object newValue)
	throws ElException {
	// PENDING (hans): Is this the behavior we want?

	// Resolve the variable, to create it in the correct scope
	// if it's a managed bean that doesn't exist
	evaluate(exprInfo);

	// Look for the variable in all scopes and replace it where
	// found or add it to the request scope if not found
	ExternalContext ec = 
	    exprInfo.getFacesContext().getExternalContext();
        if (ec.getRequestMap().get(mName) != null) {
            ec.getRequestMap().put(mName, newValue);
        }
        else if (ec.getSessionMap() != null &&
		 ec.getSessionMap().get(mName) != null) {
            ec.getSessionMap().put(mName, newValue);
        }
        else if (ec.getApplicationMap().get(mName) != null) {
            ec.getApplicationMap().put(mName, newValue);
        }
	else {
            ec.getRequestMap().put(mName, newValue);
	}
    }

    public boolean isReadOnly(ExpressionInfo exprInfo)
        throws ElException {
	boolean isReadOnly = false;

	if ("param".equals(mName) ||
	    "paramValues".equals(mName) ||
	    "header".equals(mName) ||
	    "headerValues".equals(mName) ||
	    "cookie".equals(mName) ||
	    "initParam".equals(mName)) {
	    isReadOnly = true;
	}
	return isReadOnly;
    }

    public Class getType(ExpressionInfo exprInfo)
        throws ElException {

	Class type = null;
	if ("applicationScope".equals(mName) ||
	    "requestScope".equals(mName) ||
	    "sessionScope".equals(mName) ||
	    "param".equals(mName) ||
	    "paramValues".equals(mName) ||
	    "header".equals(mName) ||
	    "headerValues".equals(mName) ||
	    "cookie".equals(mName) ||
	    "initParam".equals(mName)) {
	    type = Map.class;
	}
	if (type == null) {
	    ExternalContext ec = 
		exprInfo.getFacesContext().getExternalContext();
	    if (ec.getRequestMap().get(mName) != null) {
		type = ec.getRequestMap().get(mName).getClass();
	    }
	    else if (ec.getSessionMap() != null &&
		     ec.getSessionMap().get(mName) != null) {
		type = ec.getSessionMap().get(mName).getClass();
	    }
	    else if (ec.getApplicationMap().get(mName) != null) {
		type = ec.getApplicationMap().get(mName).getClass();
	    }
	}
	return type;
    }
    //-------------------------------------
}
