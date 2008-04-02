/*
<<<<<<< variant A
 * $Id: ValueBindingImpl.java,v 1.24 2004/01/06 04:28:28 eburns Exp $
>>>>>>> variant B
 * $Id: ValueBindingImpl.java,v 1.24 2004/01/06 04:28:28 eburns Exp $
======= end
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.util.Map;
import java.util.Arrays;

import javax.faces.el.EvaluationException;
import javax.faces.el.ValueBinding;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.component.StateHolder;
import javax.faces.application.Application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.sun.faces.util.Util;

import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.Expression;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

public class ValueBindingImpl extends ValueBinding implements StateHolder
{
//
// Private/Protected Constants
//     
    // Array of faces implicit objects
    private static final String[] FACES_IMPLICIT_OBJECTS = {
        "applicationScope",
        "sessionScope",
        "requestScope",
        "facesContext",
        "cookies",
        "header",
        "headerValues",
        "initParam",
        "param",
        "paramValues",
        "view"
    };

    static {
        // Sort for binary searching
        Arrays.sort(FACES_IMPLICIT_OBJECTS);
    }
    
//
// Class Variables
//

    private static final Log log = LogFactory.getLog(ValueBindingImpl.class);

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

    protected String ref = null;

    protected Application application = null;

    // PENDING (hans) This variable can be removed if getScope() is moved
    protected static Map applicationMap = null;

//
// Constructors and Initializers    
//
    /**
     * <p>Necessary for {@link StateHolder} contract.</p>
     */
    public ValueBindingImpl() {
    }

    public ValueBindingImpl(Application application) { 
	Util.parameterNonNull(application);
	this.application = application;
	
	if (null == applicationMap) {
//PENDING(rogerk)getCurrentinstance() performance considerations.
	    applicationMap = 
		FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
	}
	Util.doAssert(null != applicationMap);
    }

//
// Class methods
//

//
// General Methods
//
 
    public void setRef(String newRef) {
	reset();
	Util.parameterNonEmpty(newRef);
	ref = newRef;
    }

    public void reset() {
	ref = null;
    }

//
// Methods from ValueBinding
//

    public Object getValue(FacesContext context)
        throws EvaluationException, PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(Util.NULL_CONTEXT_ERROR_MESSAGE_ID)
            );
        }
	Object result = null;

        if (log.isDebugEnabled()) {
            log.debug("getValue(ref=" + ref + ")");
        }
	result = getValue(context, ref);
        if (log.isTraceEnabled()) {
            log.trace("-->Returning " + result);
        }
	return result;
    }

    protected Object getValue(FacesContext context, String toEvaluate)
        throws EvaluationException, PropertyNotFoundException {
	Object result = null;

	try {
            ExpressionInfo info = new ExpressionInfo();
            info.setExpressionString(toEvaluate);
            info.setExpectedType(Object.class); 
            info.setFacesContext(context); 
            info.setVariableResolver(application.getVariableResolver());
            info.setPropertyResolver(application.getPropertyResolver());
	    result = Util.getExpressionEvaluator().evaluate(info);
            if (log.isDebugEnabled()) {
                log.debug("getValue Result:" + result);
            }
        } catch (Throwable e) {        
	    Object [] params = { toEvaluate };        
            if (e instanceof ElException) {
                Throwable t = ((ElException) e).getCause();
                if (t != null) {
                    e = t;
                }
                if (log.isDebugEnabled()) {
                    log.debug("getValue Evaluation threw exception:", e);
                }
	        throw new EvaluationException(e);
            } else if (e instanceof PropertyNotFoundException) {
                Throwable t = ((PropertyNotFoundException) e).getCause();
                if (t != null) {
                    e = t;
                }
                if (log.isDebugEnabled()) {
                    log.debug("getValue Evaluation threw exception:", e);
                }
	        throw new PropertyNotFoundException(Util.getExceptionMessage(
		    Util.ILLEGAL_MODEL_REFERENCE_ID, params), e);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("getValue Evaluation threw exception:", e);
                }
	        throw new EvaluationException(e);
	    }
        }
	return result;
    }

    public void setValue(FacesContext context, Object value)
        throws EvaluationException, PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(Util.NULL_CONTEXT_ERROR_MESSAGE_ID)
            );
        }
        if (isReservedIdentifier(ref)) {
            throw new ReferenceSyntaxException(
                Util.getExceptionMessage(Util.ILLEGAL_IDENTIFIER_LVALUE_MODE_ID, new Object[]{ref}));
        }
        // PENDING(edburns): check for readOnly-ness        
        try {
            ExpressionInfo info = new ExpressionInfo();
            info.setExpressionString(ref);
            info.setFacesContext(context);
            info.setVariableResolver(application.getVariableResolver());
            info.setPropertyResolver(application.getPropertyResolver());
            Expression expr = 
		Util.getExpressionEvaluator().parseExpression(info);
	    expr.setValue(info, value);
            return;
        } catch (Throwable e) {
            Object[] params = {ref};            
            if (e instanceof ElException) {
                Throwable t = ((ElException) e).getCause();
                if (t != null) {
                    e = t;
                }
            } else if (e instanceof PropertyNotFoundException) {
                Throwable t = ((PropertyNotFoundException) e).getCause();
                if (t != null) {
                    e = t;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("setValue Evaluation threw exception:", e);
            }
            throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID, params), e);
        }
    }

    public boolean isReadOnly(FacesContext context)
        throws PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(Util.NULL_CONTEXT_ERROR_MESSAGE_ID)
            );
        }
        try {
            ExpressionInfo info = new ExpressionInfo();
            info.setExpressionString(ref);
            info.setFacesContext(context);
            info.setVariableResolver(application.getVariableResolver());
            info.setPropertyResolver(application.getPropertyResolver());
            Expression expr = 
		Util.getExpressionEvaluator().parseExpression(info);
	    return expr.isReadOnly(info);
        } catch (Throwable e) {
            Object[] params = {ref};            
            if (e instanceof ElException) {
                Throwable t = ((ElException) e).getCause();
                if (t != null) {
                    e = t;
                }
            } else if (e instanceof PropertyNotFoundException) {
                Throwable t = ((PropertyNotFoundException) e).getCause();
                if (t != null) {
                    e = t;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("setValue Evaluation threw exception:", e);
            }
            throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID, params), e);
        }
    }

    public Class getType(FacesContext context)
        throws PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(Util.NULL_CONTEXT_ERROR_MESSAGE_ID)
            );
        }
        try {
            ExpressionInfo info = new ExpressionInfo();
            info.setExpressionString(ref);
            info.setFacesContext(context);
            info.setVariableResolver(application.getVariableResolver());
            info.setPropertyResolver(application.getPropertyResolver());
            Expression expr = 
		Util.getExpressionEvaluator().parseExpression(info);
	    return expr.getType(info);
        } catch (Throwable e) {
            Object[] params = {ref};            
            if (e instanceof ElException) {
                Throwable t = ((ElException) e).getCause();
                if (t != null) {
                    e = t;
                }
            } else if (e instanceof PropertyNotFoundException) {
                Throwable t = ((PropertyNotFoundException) e).getCause();
                if (t != null) {
                    e = t;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("setValue Evaluation threw exception:", e);
            }
            throw new PropertyNotFoundException(Util.getExceptionMessage(Util.ILLEGAL_MODEL_REFERENCE_ID, params), e);
        }
    }

    /**
     * <p>Returns the expression string, without delimiters.</p>
     */
    public String getExpressionString() {
	return ref;
    }


    /**
     * <p>PENDING (hans) This method should probably be moved to some
     * other class or be declared in ValueBinding. It's used by the
     * ManagedBeanFactory to ensure that properties set by an expression
     * point to an object with an accepted lifespan.</p>
     *
     * <p>get the scope of the expression. Return <code>null</code> 
     * if it isn't scoped</p>
     *
     * <p>For example, the expression: <code>sessionScope.TestBean.one</code>
     * should return "session" as the scope.</p>
     *
     *
     * @return the scope of the expression
     */
    public String getScope(String valueBinding) {

        // PENDING (visvan) this method shouldn't accept any argument. 
        // This method should make use of "ref" which has already gone through
        // the verification process. 
        valueBinding = ref;
        if (valueBinding == null) {
            return null;
        }

        int segmentIndex = getFirstSegmentIndex(valueBinding);

        //examine first segment and see if it is a scope
        String identifier = valueBinding;
        String expression = null;

        if (segmentIndex > 0 ) {
            //get first segment designated by a "." or "["
            identifier = valueBinding.substring(0, segmentIndex);

            //get second segment designated by a "." or "["
            expression = valueBinding.substring(segmentIndex + 1);
            segmentIndex = getFirstSegmentIndex(expression);

            if (segmentIndex > 0) {
                expression = expression.substring(0, segmentIndex);
            }
        }

        //check to see if the identifier is a named scope. If it is check
        //for the expression in that scope. The expression is the
        //second segment.

	FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        if (identifier.equalsIgnoreCase(RIConstants.REQUEST_SCOPE)) {
            if ((expression != null) && 
                (ec.getRequestMap().get(expression) != null)) {
                return RIConstants.REQUEST;
            }
            else {
                return null;
            }
        }
        if (identifier.equalsIgnoreCase(RIConstants.SESSION_SCOPE)) {
            if ((expression != null) && 
                (Util.getSessionMap(context).get(expression) != null)) {
                return RIConstants.SESSION;
            }
            else {
                return null;
            }
        }
        if (identifier.equalsIgnoreCase(RIConstants.APPLICATION_SCOPE)) {
            if ((expression != null) && 
                (ec.getApplicationMap().get(expression) != null)) {
                return RIConstants.APPLICATION;
            }
            else {
                return null;
            }
        }

        //No scope was provided in the expression so check for the 
        //expression in all of the scopes. The expression is the first 
        //segment.

        if (ec.getRequestMap().get(identifier) != null) {
            return RIConstants.REQUEST;
        }
        if (Util.getSessionMap(context).get(identifier) != null) {
            return RIConstants.SESSION;
        }
        if (ec.getApplicationMap().get(identifier) != null) {
            return RIConstants.APPLICATION;
        }

        //not present in any scope
        return null;
    }

    /**
     * PENDING (hans) This method should move with the getScope()
     * method.
     *
     * The the first segment of a String tokenized by a "." or "["
     *
     * @return index of the first occurrence of . or [
     */
    private int getFirstSegmentIndex(String valueBinding) {
        int segmentIndex = valueBinding.indexOf(".");
        int bracketIndex = valueBinding.indexOf("[");

        //there is no "." in the valueBinding so take the bracket value
        if (segmentIndex < 0) {
            segmentIndex = bracketIndex;
        } else {
            //if there is a bracket proceed
            if (bracketIndex > 0) {
                //if the bracket index is before the "." then
                //get the bracket index
                if (segmentIndex > bracketIndex) {
                    segmentIndex = bracketIndex;
            	 }
            }
        }
        return segmentIndex;
    }
    
    /**
     * <p>Returns true if the profivided identifier is a reserved identifier,
     * otherwise false.</p>
     * @param identifier the identifier to check
     * @return returns true if the profivided identifier is a 
     *         reserved identifier, otherwisefalse
     */ 
    private boolean isReservedIdentifier(String identifier) {
        return (Arrays.binarySearch(FACES_IMPLICIT_OBJECTS, identifier) >= 0);
    }   

    // 
    // methods from StateHolder
    //

    public Object saveState(FacesContext context) {
	Object result = ref;
	return result;
    }

    public void restoreState(FacesContext context, Object state) {
	ref = state.toString();
	if (null == application) {
	    application = context.getApplication();
	}
	if (null == applicationMap) {
	    applicationMap = context.getExternalContext().getApplicationMap();
	}
    }

    private boolean isTransient = false;
    
    public boolean isTransient() {
	return isTransient;
    }

    public void setTransient(boolean newTransientValue) {
	isTransient = newTransientValue;
    }


    
    
} // end of class ValueBindingImpl
