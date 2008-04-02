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

package com.sun.faces.el.impl.jstl;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.faces.el.VariableResolver;
import javax.faces.el.PropertyResolver;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;

import org.mozilla.util.Assert;

/**
 *
 * <p>Represents an operator that obtains a Map entry, an indexed
 * value, a property value, or an indexed property value of an object.
 * The following are the rules for evaluating this operator:
 *
 * <ul><pre>
 * Evaluating a[b] (assuming a.b == a["b"])
 *   a is null
 *     return null
 *   b is null
 *     return null
 *   a is Map
 *     !a.containsKey (b)
 *       return null
 *     a.get(b) == null
 *       return null
 *     otherwise
 *       return a.get(b)
 *   a is List or array
 *     coerce b to int (using coercion rules)
 *     coercion couldn't be performed
 *       error
 *     a.get(b) or Array.get(a, b) throws ArrayIndexOutOfBoundsException or IndexOutOfBoundsException
 *       return null
 *     a.get(b) or Array.get(a, b) throws other exception
 *       error
 *     return a.get(b) or Array.get(a, b)
 * 
 *   coerce b to String
 *   b is a readable property of a
 *     getter throws an exception
 *       error
 *     otherwise
 *       return result of getter call
 *
 *   otherwise
 *     error
 * </pre></ul>
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: eburns $
 **/

public class ArraySuffix
  extends ValueSuffix
{
  //-------------------------------------
  // Constants
  //-------------------------------------

  // Zero-argument array
  static Object [] sNoArgs = new Object [0];

  static Object [] sOneArg = new Object [1];

  //-------------------------------------
  // Properties
  //-------------------------------------
  // property index

  Expression mIndex;
  public Expression getIndex ()
  { return mIndex; }
  public void setIndex (Expression pIndex)
  { mIndex = pIndex; }

  //-------------------------------------
  /**
   *
   * Constructor
   **/
  public ArraySuffix (Expression pIndex)
  {
    mIndex = pIndex;
  }

  //-------------------------------------
  /**
   *
   * Gets the value of the index
   **/
  Object evaluateIndex (Object pContext,
			VariableResolver vResolver,
			Logger pLogger)
    throws ELException
  {
    return mIndex.evaluate (pContext, vResolver, pLogger);
  }

  //-------------------------------------
  /**
   *
   * Returns the operator symbol
   **/
  String getOperatorSymbol ()
  {
    return "[]";
  }

  //-------------------------------------
  // ValueSuffix methods
  //-------------------------------------
  /**
   *
   * Returns the expression in the expression language syntax
   **/
  public String getExpressionString ()
  {
    return "[" + mIndex.getExpressionString () + "]";
  }

  public Object evaluate (Object pValue,
			  Object pContext,
			  VariableResolver vResolver,
			  PropertyResolver pResolver,
			  Logger pLogger)
      throws ELException {
      return this.evaluate(pValue, null, pContext, vResolver, pResolver, 
			   pLogger);
  }


  //-------------------------------------
  /**
   *
   * Evaluates the expression in the given context, operating on the
   * given value.
   **/
  public Object evaluate (Object pValue,
			  Object rValue, 
			  Object pContext,
			  VariableResolver vResolver,
			  PropertyResolver pResolver,
			  Logger pLogger)
    throws ELException
  {
    Object indexVal;
    Object result = null;
    String indexStr;
    int index = -1;
    boolean isIndexCase = false;
    BeanInfoProperty property;
    BeanInfoIndexedProperty ixproperty;
    
    // Check for null value
    if (pValue == null) {
	if (pLogger.isLoggingWarning ()) {
	    pLogger.logWarning 
		(Constants.CANT_GET_INDEXED_VALUE_OF_NULL,
		 getOperatorSymbol ());
	}
	// PENDING(edburns): Add an exception throwing mechanism to the
	// interface VariableResolver
	vResolver.resolveVariable((FacesContext) pContext, 
				  "ELEvaluator.throwException");
	return null;
    }
    
    // Evaluate the index
    else if ((indexVal = evaluateIndex (pContext, vResolver, pLogger)) == 
	     null) {
	if (pLogger.isLoggingWarning ()) {
	    pLogger.logWarning
		(Constants.CANT_GET_NULL_INDEX,
		 getOperatorSymbol ());
	}
	return null;
    }
    
    //
    // determine whether this is the index case, or the property case
    //
    if (isIndexCase = (pValue instanceof List || pValue.getClass().isArray())){
	Integer indexObj = Coercions.coerceToInteger (indexVal, pLogger);
	if (indexObj == null) {
	    if (pLogger.isLoggingError ()) {
		pLogger.logError
		    (Constants.BAD_INDEX_VALUE,
		     getOperatorSymbol (),
		     indexVal.getClass ().getName ());
	    }
	    return null;
	}
	index = indexObj.intValue();
    }
	
    try {
	//
	// deal with the "get" and "put" cases separately
	//
	
	// this is the "put" case
	if (null != rValue) {
	    if (isIndexCase) {
		pResolver.setValue(pValue, index, rValue);
	    }
	    else {
		pResolver.setValue(pValue, (String) indexVal, rValue);
	    }
	}
	else {
	    // this is the "get" case
	    if (isIndexCase) {
		result = pResolver.getValue(pValue, index);
	    }
	    else {
		result = pResolver.getValue(pValue, (String) indexVal);
	    }
	}
    }
    catch (PropertyNotFoundException e) {
	// PENDING(edburns): Add an exception throwing mechanism to the
	// interface VariableResolver
	vResolver.resolveVariable((FacesContext) pContext, 
				  "ELEvaluator.throwException");
    }
    return result;
  }

  //-------------------------------------
}
