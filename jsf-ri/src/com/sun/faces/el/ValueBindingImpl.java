/*
 * $Id: ValueBindingImpl.java,v 1.35 2004/07/17 01:37:12 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import com.sun.faces.RIConstants;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.Expression;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.util.Util;
import com.sun.faces.util.InstancePool;
import com.sun.faces.util.InstancePool.NewInstance;
import com.sun.faces.application.ApplicationAssociate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

import java.util.Arrays;
import java.util.Map;

public class ValueBindingImpl extends ValueBinding implements StateHolder {

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

    protected ApplicationAssociate appAssociate = null;

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
	//PENDING(rogerk)getCurrentinstance() performance considerations.
	FacesContext facesContext = FacesContext.getCurrentInstance();
        this.application = application;
	this.appAssociate = 
	    ApplicationAssociate.getInstance(facesContext.getExternalContext());
	
        if (null == applicationMap) {
            applicationMap =
                facesContext.getExternalContext()
                .getApplicationMap();
        }

	if (null != this.appAssociate) {
	    InstancePool pool = this.appAssociate.getExpressionInfoInstancePool();
	    if (!pool.isInitialized()) {
		pool.setInstantiator(new NewInstance() {
			public Object newInstance() {
			    return new ExpressionInfo();
			}
		    });
	    }
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
                Util.getExceptionMessageString(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
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

	ExpressionInfo info = checkoutExpressionInfo();
        try {
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
            if (e instanceof ElException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((ElException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("getValue Evaluation threw exception:", l);
                }
		checkinExpressionInfo(info);
                throw new EvaluationException(e);
            } else if (e instanceof PropertyNotFoundException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((PropertyNotFoundException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("getValue Evaluation threw exception:", l);
                }
                // Just rethrow it to keep detailed message
		checkinExpressionInfo(info);
                throw (PropertyNotFoundException) e;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("getValue Evaluation threw exception:", e);
                }
		checkinExpressionInfo(info);
                throw new EvaluationException(e);
            }
        }
	checkinExpressionInfo(info);
        return result;
    }


    public void setValue(FacesContext context, Object value)
        throws EvaluationException, PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        if (isReservedIdentifier(ref)) {
            throw new ReferenceSyntaxException(
                Util.getExceptionMessageString(
                    Util.ILLEGAL_IDENTIFIER_LVALUE_MODE_ID, new Object[]{ref}));
        }
        // PENDING(edburns): check for readOnly-ness        
	ExpressionInfo info = checkoutExpressionInfo();
        try {
            info.setExpressionString(ref);
            info.setFacesContext(context);
            info.setVariableResolver(application.getVariableResolver());
            info.setPropertyResolver(application.getPropertyResolver());
            Expression expr =
                Util.getExpressionEvaluator().parseExpression(info);
            expr.setValue(info, value);
	    checkinExpressionInfo(info);
            return;
        } catch (Throwable e) {
            if (e instanceof ElException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((ElException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("setValue Evaluation threw exception:", l);
                }
		checkinExpressionInfo(info);
                throw new EvaluationException(e);
            } else if (e instanceof PropertyNotFoundException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((PropertyNotFoundException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("setValue Evaluation threw exception:", l);
                }
                // Just rethrow it to keep detailed message
		checkinExpressionInfo(info);
                throw (PropertyNotFoundException) e;
            } else if (e instanceof EvaluationException) {
                if (log.isDebugEnabled()) {
                    log.debug("setValue Evaluation threw exception:", e);
                }
		checkinExpressionInfo(info);
		throw ((EvaluationException)e);
	    }
	    else {
                if (log.isDebugEnabled()) {
                    log.debug("setValue Evaluation threw exception:", e);
                }
		checkinExpressionInfo(info);
                throw new EvaluationException(e);
            }
        }
    }


    public boolean isReadOnly(FacesContext context)
        throws PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
	ExpressionInfo info = checkoutExpressionInfo();
        try {
            info.setExpressionString(ref);
            info.setFacesContext(context);
            info.setVariableResolver(application.getVariableResolver());
            info.setPropertyResolver(application.getPropertyResolver());
            Expression expr =
                Util.getExpressionEvaluator().parseExpression(info);
	    boolean result = expr.isReadOnly(info);
	    checkinExpressionInfo(info);
            return result;
        } catch (Throwable e) {
            if (e instanceof ElException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((ElException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("isReadOnly Evaluation threw exception:", l);
                }
		checkinExpressionInfo(info);
                throw new EvaluationException(e);
            } else if (e instanceof PropertyNotFoundException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((PropertyNotFoundException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("isReadOnly Evaluation threw exception:", l);
                }
                // Just rethrow it to keep detailed message
		checkinExpressionInfo(info);
                throw (PropertyNotFoundException) e;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("isReadOnly Evaluation threw exception:", e);
                }
		checkinExpressionInfo(info);
                throw new EvaluationException(e);
            }
        }
    }


    public Class getType(FacesContext context)
        throws PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
	ExpressionInfo info = checkoutExpressionInfo();
        try {
            info.setExpressionString(ref);
            info.setFacesContext(context);
            info.setVariableResolver(application.getVariableResolver());
            info.setPropertyResolver(application.getPropertyResolver());
            Expression expr =
                Util.getExpressionEvaluator().parseExpression(info);
	    Class result = expr.getType(info);
	    checkinExpressionInfo(info);
            return result;
        } catch (Throwable e) {
            if (e instanceof ElException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((ElException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("getType Evaluation threw exception:", l);
                }
		checkinExpressionInfo(info);
                throw new EvaluationException(e);
            } else if (e instanceof PropertyNotFoundException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((PropertyNotFoundException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("getType Evaluation threw exception:", l);
                }
                // Just rethrow it to keep detailed message
		checkinExpressionInfo(info);
                throw (PropertyNotFoundException) e;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("getType Evaluation threw exception:", e);
                }
		checkinExpressionInfo(info);
                throw new EvaluationException(e);
            }
        }
    }


    public String getExpressionString() {
        return "#{" + ref + "}";
    }

    /**
     * <p>Returns true if the profivided identifier is a reserved identifier,
     * otherwise false.</p>
     *
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

    // helper methods

    private ExpressionInfo checkoutExpressionInfo() {
	ExpressionInfo result = null;
	if (null != appAssociate) {
	    // PENDING(edburns): generic types would be nice here
	    result = (ExpressionInfo)
		appAssociate.getExpressionInfoInstancePool().checkout();
	    result.reset();
	}
	else {
	    result = new ExpressionInfo();
	}
	return result;
    }
    
    private void checkinExpressionInfo(ExpressionInfo toCheckin) {
	if (null != appAssociate) {
	    appAssociate.getExpressionInfoInstancePool().checkin(toCheckin);
	}
    }


} // end of class ValueBindingImpl
