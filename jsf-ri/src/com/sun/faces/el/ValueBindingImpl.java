/*
 * $Id: ValueBindingImpl.java,v 1.4 2003/04/01 15:26:54 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.util.Map;
import java.util.List;

import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.el.PropertyResolver;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import org.mozilla.util.ParameterCheck;
import org.mozilla.util.Assert;

import com.sun.faces.el.impl.jstl.ELEvaluator;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

public class ValueBindingImpl extends ValueBinding
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

    protected String ref = null;

    protected VariableResolver variableResolver = null;
    protected PropertyResolver propertyResolver = null;
    protected static Map applicationMap = null;

//
// Constructors and Initializers    
//

    public ValueBindingImpl(VariableResolverImpl newVar, 
			    PropertyResolver newProp) {
	ParameterCheck.nonNull(newVar);
	ParameterCheck.nonNull(newProp);
	variableResolver = newVar;
	propertyResolver = newProp;
	newVar.setPropertyResolver(propertyResolver);
	
	if (null == applicationMap) {
//PENDING(rogerk)getCurrentinstance() performance considerations.
	    applicationMap = 
		FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
	}
	Assert.assert_it(null != applicationMap);
    }

//
// Class methods
//

//
// General Methods
//

    public ELEvaluator getELEvaluator() {
        ELEvaluator elEvaluator = (ELEvaluator)
	    applicationMap.get(RIConstants.ELEVALUATOR);
	if (null == elEvaluator) {
            applicationMap.put(RIConstants.ELEVALUATOR, 
			       elEvaluator= new ELEvaluator(variableResolver));
	}
	Assert.assert_it(null != elEvaluator);
	return elEvaluator;
    }
    
    
    public void setRef(String newRef) {
	reset();
	ParameterCheck.nonNull(newRef);
	ParameterCheck.notEmpty(newRef);
	ref = newRef;
    }

    public void reset() {
	ref = null;
    }

    String addBracketsIfNecessary(String modelReference) {
        if (modelReference == null) {
            throw new NullPointerException();
        }
        String result = modelReference;
        if (!(result.startsWith("${") &&
	      result.endsWith("}"))) {
            result = "${" + modelReference + "}";
        }
        return (result);
    }

    String stripQuotesIfNecessary(String modelReference) {
        if (modelReference == null) {
            throw new NullPointerException();
        }
        String result = modelReference;
        if ((result.startsWith("\"") || result.startsWith("\'")) &&
	    (result.endsWith("\"") || result.endsWith("\'"))) {
            result = modelReference.substring(1, modelReference.length() - 1);
        }
        return (result);
    }

    /**

    * <p>Return the "last" part of the expression.</p>

    * <p>For an expression like <code>a.b.c</code> the last part is
    * <code>"c"</code>.  For an expression like <code>a[1].b.c</code>,
    * the last part is still <code>"c"</code>.  For an expession like
    * "a[1]", the last part is "[1]".  For an expression like
    * <code>"a"</code> the last part is <code>"a"</code>.</p>

    * @return the "last" part of our ref.

    */

    String getLastSegment() {
	String result = null;
	char [] str = ref.toCharArray();
	int lastLeftBracketIndex, i, len = str.length;
	Object [] params = { ref };

	// iterate backwards through the String, searching for the first
	// ']' or '.' from the end.  If we encounter '.', consider
	// everything after '.' the "last segment".  If we encounter
	// ']', look for the matching '[' and consider from '[' to the
	// end, inclusive, as the "last segment".  If no matching '[' is
	// found, it's an invalid expression.

	for (i = len-1; 0 <= i; i--) {
	    // this is the "square bracket" case
	    if (str[i] == ']') {
		// if we have ']' without '['
		if (-1 == (lastLeftBracketIndex = ref.lastIndexOf('['))) {
		    throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID, params));
		}
		// We have ']' with '['.  

		// Verify that there is something between the '[' and
		// ']' chars.
		if (lastLeftBracketIndex + 1 == i) {
		    throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID, params));
		}

		// We have content in between '[' and ']'
		// PENDING(edburns): perhaps validate the expression
		// here?
		result = ref.substring(lastLeftBracketIndex);
		break;
	    }
	    // We have a '[' with no ']' following it.
	    if (str[i] == '[') {
		throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID, params));
	    }
	    // this is the "dot" case
	    if (str[i] == '.') {
		// guard against strings ending in '.'.
		if (len <= i + 1) {
		    throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID, params));
		}
		
		result = ref.substring(i+1);
		break;
	    }
	}
	// degenerate case, ref contains only a single segment
	if (null == result) {
	    result = ref;
	}
	return result;
    }

//
// Methods from ValueBinding
//

    public Object getValue(FacesContext context)
        throws PropertyNotFoundException {
	Object result = null;

	result = getValue(context, ref);
	return result;
    }

    protected Object getValue(FacesContext context, String toEvaluate)
        throws PropertyNotFoundException {
	Object result = null;

	try {
	    result = getELEvaluator().evaluate(addBracketsIfNecessary(toEvaluate), 
					       context, Object.class);
	}
	catch (Throwable e) {
	    Object [] params = { toEvaluate };
	    throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID, params), e);
	}
	return result;
    }

    public void setValue(FacesContext context, Object value)
        throws PropertyNotFoundException {

	// PENDING(edburns): check for readOnly-ness
	try {
	    getELEvaluator().evaluate(addBracketsIfNecessary(ref), value, 
				    context, Object.class);
	    return;
	}
	catch (Throwable e) {
	    Object [] params = { ref };
	    throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID, params), e);
	}
    }

    /**

    * <p>ALGORITHM:</p>

    * <p>Leverage our knowledge that EL expression segments are
    * delimited by either "dot" or "square bracket" constructs to easily
    * determine the immutability of the "last" segment.  We do this by
    * separating the "last" segment from the "first part" of the EL
    * expression, evaluating the "first part" of the expression, then
    * using the appropriate <code>PropertyResolver.isReadOnly()</code>
    * method to determine immutability.</p>

    * <p>PENDING(edburns): this may not be safe in the world of
    * pluggable PropertyResolvers, since it may not be safe to assume
    * knowledge of the how to determine the "last" segment.</p>

    * @see javax.faces.el.ValueBinding#isReadOnly

    */  

    public boolean isReadOnly(FacesContext context)
        throws PropertyNotFoundException {
	boolean result = false;
	Object toTest = null;
	String 
	    last = getLastSegment(),
	    first = null;
	boolean isBracketedExpression = false;
	int i;
	// if our ref has only one segment
	if (0 == (i = ref.indexOf(last))) {
	    first = last;
	    last = RIConstants.IMMUTABLE_MARKER;
	}
	else {
	    // getLastSegment does different things depending on whether
	    // the last segment is a bracket expression or a dot
	    // expression.
	    if (last.charAt(0) == '[') {
		first = ref.substring(0, i);
		isBracketedExpression = true;
	    }
	    else {
		first = ref.substring(0, i-1);
	    }
	}
	// At this point, first is everything but the last segment in
	// the expression.  Last is the last segment in the expression.
	// If there is only one segment in the expression, last is
	// RIConstants.IMMUTABLE_MARKER.
	
	if (null != (toTest = getValue(context, first))) {
	    if (isBracketedExpression) {
		// Get the contents of the bracketed expression
		last = last.substring(1, last.length() - 1);
		last = stripQuotesIfNecessary(last);

		try {
		    i = Integer.valueOf(last).intValue();
		    result = propertyResolver.isReadOnly(toTest, i);
		}
		catch (NumberFormatException e) {
		    // unable to coerce to number, try the string version.
		    result = propertyResolver.isReadOnly(toTest, last);
		}
	    }
	    else {
		result = propertyResolver.isReadOnly(toTest, last);
	    }
	}

	return result;
    }

    public Class getType(FacesContext context)
        throws PropertyNotFoundException {
	return null;
    }

} // end of class ValueBindingImpl
