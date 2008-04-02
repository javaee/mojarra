/*
 * $Id: MethodBindingMethodExpressionAdapter.java,v 1.1 2005/05/05 21:55:41 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package @package@;

import java.io.Serializable;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.MethodBinding;

import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ELException;

/**
 * <p>Wrap a MethodExpression instance and expose it as a MethodBinding</p>
 *
 */
@protection@ class MethodBindingMethodExpressionAdapter extends MethodBinding implements StateHolder, 
    Serializable {
   
    private MethodExpression methodExpression= null;
    private boolean tranzient;

    public MethodBindingMethodExpressionAdapter() {} // for StateHolder
    
    @protection@ MethodBindingMethodExpressionAdapter(MethodExpression methodExpression) {
        this.methodExpression = methodExpression;    
    }

    public Object invoke(FacesContext context, Object params[])
        throws javax.faces.el.EvaluationException, javax.faces.el.MethodNotFoundException {
	assert(null != methodExpression);
        if ( context == null ) {
            throw new NullPointerException();
        }
        
	Object result = null;
	try {
	    result = methodExpression.invoke(context.getELContext(),
					     params);
	}
	catch (javax.el.MethodNotFoundException e) {
	    throw new javax.faces.el.MethodNotFoundException(e);
	}
	catch (javax.el.PropertyNotFoundException e) {
	    throw new EvaluationException(e);
	}
	catch (ELException e) {
        // look for the root cause and pass that to the
        // ctor of EvaluationException
        Throwable cause = e.getCause();
        if (cause != null) {
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
        } else {
            cause = e;
        }
	    throw new EvaluationException(cause);
	} catch (NullPointerException e) {
	    throw new javax.faces.el.MethodNotFoundException(e);
	}
	return result;
    }

    public Class getType(FacesContext context) throws javax.faces.el.MethodNotFoundException {
	assert(null != methodExpression);
	Class result = null;
        if ( context == null ) {
            throw new NullPointerException();
        }
        
	try {
	    MethodInfo mi = 
		methodExpression.getMethodInfo(context.getELContext());
	    result = mi.getReturnType();
	}
	catch (javax.el.PropertyNotFoundException e) {
	    throw new javax.faces.el.MethodNotFoundException(e);
	}
	catch (javax.el.MethodNotFoundException e) {
	    throw new javax.faces.el.MethodNotFoundException(e);
	}
	catch (ELException e) {
	    throw new javax.faces.el.MethodNotFoundException(e);
	}
	return result;
    }
    
   
    public String getExpressionString() {
	assert(null != methodExpression);
        return methodExpression.getExpressionString();
    }

    public boolean equals(Object other) {
	assert(null != methodExpression);
	boolean result = false;
	
	// don't bother even trying to compare, if we're not assignment
	// compatabile with "other"
	if (MethodExpression.class.isAssignableFrom(other.getClass())) {
	    MethodExpression otherVE = (MethodExpression) other;
	    result = this.getExpressionString().equals(otherVE.getExpressionString());
	}
	return result;
    }

    public int hashCode() {
	assert(null != methodExpression);

	return methodExpression.hashCode();
    }


    public boolean isTransient() {
        return this.tranzient;
    }
    
    public void setTransient(boolean tranzient) {
        this.tranzient = tranzient;
    }
    
    public Object saveState(FacesContext context){
	Object result = null;
	if (!tranzient) {
	    if (methodExpression instanceof StateHolder) {
		Object [] stateStruct = new Object[2];
		
		// save the actual state of our wrapped methodExpression
		stateStruct[0] = ((StateHolder)methodExpression).saveState(context);
		// save the class name of the methodExpression impl
		stateStruct[1] = methodExpression.getClass().getName();

		result = stateStruct;
	    }
	    else {
		result = methodExpression;
	    }
	}

	return result;
	
    }

    public void restoreState(FacesContext context, Object state) {
	// if we have state
	if (null == state) {
	    return;
	}
	
	if (!(state instanceof MethodExpression)) {
	    Object [] stateStruct = (Object []) state;
	    Object savedState = stateStruct[0];
	    String className = stateStruct[1].toString();
	    MethodExpression result = null;
	    
	    Class toRestoreClass = null;
	    if (null != className) {
		try {
		    toRestoreClass = loadClass(className, this);
		}
		catch (ClassNotFoundException e) {
		    throw new IllegalStateException(e.getMessage());
		}
		
		if (null != toRestoreClass) {
		    try {
			result = 
			    (MethodExpression) toRestoreClass.newInstance();
		    }
		    catch (InstantiationException e) {
			throw new IllegalStateException(e.getMessage());
		    }
		    catch (IllegalAccessException a) {
			throw new IllegalStateException(a.getMessage());
		    }
		}
		
		if (null != result && null != savedState) {
		    // don't need to check transient, since that was
		    // done on the saving side.
		    ((StateHolder)result).restoreState(context, savedState);
		}
		methodExpression = result;
	    }
	}
	else {
	    methodExpression = (MethodExpression) state;
	}
    }

    //
    // Helper methods for StateHolder
    //

    private static Class loadClass(String name, 
            Object fallbackClass) throws ClassNotFoundException {
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader.loadClass(name);
    }
 


}
