/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.component.StateHolder;

import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.Expression;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.util.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * PENDING (hans) The main reason for extending ValueBindingImpl
 * instead of ValueBinding is that the ManagedBeanFactory class
 * casts to ValueBindingImpl and calls getScope(), see the
 * comment about this method in ValueBindingImpl for details.
 */
public class MixedELValueBinding extends ValueBindingImpl {
    private static final Log log = 
	LogFactory.getLog(MixedELValueBinding.class);

    private String ref = null;
    private List expressions = null;

    public void setRef(String ref) {
	Util.parameterNonEmpty(ref);
	this.ref = ref;
	expressions = null;
    }

    public Object getValue(FacesContext context)
        throws EvaluationException, PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(Util.NULL_CONTEXT_ERROR_MESSAGE_ID)
            );
        }
	if (expressions == null) {
	    MixedELValueParser parser = new MixedELValueParser();
	    try {
		expressions = parser.parse(context, ref);
	    }
            catch (ElException e) {
		Throwable t = e;
                Throwable cause =  e.getCause();
                if (cause != null) {
                    t = cause;
                }
                if (log.isDebugEnabled()) {
                    log.debug("getValue Evaluation threw exception:", t);
                }
	        throw new EvaluationException(t);
	    }
	}

	StringBuffer sb = new StringBuffer();
	Iterator i = expressions.iterator();
	Application application = context.getApplication();
	ExpressionInfo info = new ExpressionInfo();
	info.setExpectedType(String.class); 
	info.setFacesContext(context); 
	info.setVariableResolver(application.getVariableResolver());
	info.setPropertyResolver(application.getPropertyResolver());
	while (i.hasNext()) {
	    Object o = i.next();
	    if (o instanceof Expression) {
		try {
		    Object value = ((Expression) o).evaluate(info);
		    if (value != null) {
			sb.append(value);
		    } // null values are effectively zero length strings
		}
		catch (ElException e) {
		    Throwable t = e;
		    Throwable cause =  e.getCause();
		    if (cause != null) {
			t = cause;
		    }
		    if (log.isDebugEnabled()) {
			log.debug("getValue Evaluation threw exception:", t);
		    }
		    throw new EvaluationException(t);
		}
	    }
	    else {
		sb.append(o);
	    }
	}
	return sb.toString();
    }	

    public void setValue(FacesContext context, Object value)
        throws EvaluationException, PropertyNotFoundException {
	Object [] params = {ref};        
	throw new PropertyNotFoundException(Util.getExceptionMessage(
	    Util.ILLEGAL_MODEL_REFERENCE_ID, params));
    }

    public boolean isReadOnly(FacesContext context)
        throws PropertyNotFoundException {
	return true;
    }

    public Class getType(FacesContext context)
        throws PropertyNotFoundException {
	return String.class;
    }

    public String getExpressionString() {
	return ref;
    }

    public String getScope(String valueBinding) {
	return null;
    }

    // StateHolder methods

    public Object saveState(FacesContext context) {
	return ref;
    }

    public void restoreState(FacesContext context, Object state) {
	ref = state.toString();
    }

    private boolean isTransient = false;
    
    public boolean isTransient() {
	return isTransient;
    }

    public void setTransient(boolean isTransient) {
	this.isTransient = isTransient;
    }
}
