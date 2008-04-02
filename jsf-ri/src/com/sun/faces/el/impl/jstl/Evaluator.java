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

import java.text.MessageFormat;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import com.sun.faces.el.impl.support.ExpressionEvaluator;

import com.sun.faces.el.VariableResolverImpl;

/**
 *
 * <p>This is the expression evaluator "adapter" that customizes it
 * for use with the JSP Standard Tag Library.  It uses a
 * VariableResolver implementation that looks up variables from the
 * PageContext and also implements its implicit objects.  It also
 * wraps ELExceptions in JspExceptions that describe the attribute
 * name and value causing the error.
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: eburns $
 **/

public class Evaluator
  implements ExpressionEvaluator
{
  //-------------------------------------
  // Properties
  //-------------------------------------

  //-------------------------------------
  // Member variables
  //-------------------------------------

  /** The singleton instance of the evaluator **/
  static ELEvaluator sEvaluator =
    new ELEvaluator
    (new VariableResolverImpl ());

  //-------------------------------------
  // ExpressionEvaluator methods
  //-------------------------------------
  /** 
   *
   * Translation time validation of an attribute value.  This method
   * will return a null String if the attribute value is valid;
   * otherwise an error message.
   **/ 
  public String validate (String pAttributeName,
			  String pAttributeValue)
  {
    try {
      sEvaluator.parseExpressionString (pAttributeValue);
      return null;
    }
    catch (ELException exc) {
      return
	MessageFormat.format
	(Constants.ATTRIBUTE_PARSE_EXCEPTION,
	 new Object [] {
	   "" + pAttributeName,
	   "" + pAttributeValue,
	   exc.getMessage ()
	 });
    }
  }

  //-------------------------------------
  /**
   *
   * Evaluates the expression at request time
   **/
  public Object evaluate (String pAttributeName,
			  String pAttributeValue,
			  Class pExpectedType,
			  Tag pTag,
			  PageContext pPageContext)
    throws JspException
  {
    try {
      return sEvaluator.evaluate
	(pAttributeValue,
	 pPageContext,
	 pExpectedType);
    }
    catch (ELException exc) {
      throw new JspException
	(MessageFormat.format
	 (Constants.ATTRIBUTE_EVALUATION_EXCEPTION,
	  new Object [] {
	    "" + pAttributeName,
	    "" + pAttributeValue,
	    exc.getMessage ()
	  }));
    }
  }

  //-------------------------------------
  // Testing methods
  //-------------------------------------
  /**
   *
   * Parses the given attribute value, then converts it back to a
   * String in its canonical form.
   **/
  public static String parseAndRender (String pAttributeValue)
    throws JspException
  {
    try {
      return sEvaluator.parseAndRender (pAttributeValue);
    }
    catch (ELException exc) {
      throw new JspException
	(MessageFormat.format
	 (Constants.ATTRIBUTE_PARSE_EXCEPTION,
	  new Object [] {
	    "test",
	    "" + pAttributeValue,
	    exc.getMessage ()
	  }));
    }
  }

  //-------------------------------------

}
