/*
 * $Id: ValueBindingImpl.java,v 1.2 2003/03/27 21:21:16 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.util.Map;

import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.el.PropertyResolver;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;

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

    public ValueBindingImpl(FacesContext facesContext, 
			    VariableResolverImpl newVar, 
			    PropertyResolver newProp) {
	ParameterCheck.nonNull(newVar);
	ParameterCheck.nonNull(newProp);
	variableResolver = newVar;
	propertyResolver = newProp;
	newVar.setPropertyResolver(propertyResolver);
	
	if (null == applicationMap) {
	    applicationMap = 
		facesContext.getExternalContext().getApplicationMap();
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
	ref = newRef;
    }

    public void reset() {
	ref = null;
    }

    private String addBracketsIfNecessary(String modelReference) {
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

//
// Methods from ValueBinding
//

    public Object getValue(FacesContext context)
        throws PropertyNotFoundException {
	Object result = null;

	try {
	    result = getELEvaluator().evaluate(addBracketsIfNecessary(ref), 
					       context, Object.class);
	}
	catch (Throwable e) {
	    throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID), e);
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
	    throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID), e);
	}
    }

    public boolean isReadOnly(FacesContext context)
        throws PropertyNotFoundException {
	return false;
    }

    public Class getType(FacesContext context)
        throws PropertyNotFoundException {
	return null;
    }

} // end of class ValueBindingImpl
