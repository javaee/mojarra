/*
 * $Id: MethodExpressionMethodBindingAdapter.java,v 1.3 2005/06/01 14:03:28 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package @package@;

import javax.el.MethodExpression;
import javax.el.ELException;
import javax.el.ELContext;
import javax.el.MethodInfo;
import javax.faces.el.MethodBinding;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

import java.io.Serializable;

/**
 * <p>Wrap a MethodBinding instance and expose it as a
 * MethodExpression.</p>
 */

@protection@ class MethodExpressionMethodBindingAdapter extends MethodExpression implements Serializable, StateHolder {

    private static final long serialVersionUID = @serialVersionUID@;

    public MethodExpressionMethodBindingAdapter() {} // for StateHolder

    private MethodBinding binding = null;

    @protection@ MethodExpressionMethodBindingAdapter(MethodBinding binding) {
	assert(null != binding);
	this.binding = binding;
    }

    //
    // Methods from MethodExpression
    //

    private transient MethodInfo info = null;

    public MethodInfo getMethodInfo(ELContext context) throws ELException {
	assert(null != binding);

	if (null == info) {
	    FacesContext facesContext = (FacesContext) 
		context.getContext(FacesContext.class);
	    if (null != facesContext) {
		try {
		    info = new MethodInfo(null, binding.getType(facesContext), 
					  null);
		}
		catch (Exception e) {
		    throw new ELException(e);
		}
	    }
	}
		
	return info;
    }
    
    public Object invoke(ELContext context, Object[] params) throws ELException {
	assert(null != binding);

	Object result = null;
	FacesContext facesContext = (FacesContext) 
	    context.getContext(FacesContext.class);
	if (null != facesContext) {
	    try {
		result = binding.invoke(facesContext, params);
	    }
	    catch (Exception e) {
		throw new ELException(e);
	    }
	}
	return result;
    }

    public String getExpressionString() {
	assert(null != binding);
	return binding.getExpressionString();
	
    }

    public boolean isLiteralText() {
        assert (binding != null);
        String expr = binding.getExpressionString();
        return (!(expr.startsWith("#{")
            && expr.endsWith("}")));    
    }

    public boolean equals(Object other) {
	assert(null != binding);
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
	assert(null != binding);

	return binding.hashCode();
    }
    
    public String getDelimiterSyntax() {
       // PENDING (visvan) Implementation
        return "";
    }
    
    // 
    // Methods from StateHolder
    //

    

    public Object saveState(FacesContext context) {
	Object result = null;
	if (!tranzient) {
	    if (binding instanceof StateHolder) {
		Object [] stateStruct = new Object[2];
		
		// save the actual state of our wrapped binding
		stateStruct[0] = ((StateHolder)binding).saveState(context);
		// save the class name of the binding impl
		stateStruct[1] = binding.getClass().getName();

		result = stateStruct;
	    }
	    else {
		result = binding;
	    }
	}

	return result;
    }

    public void restoreState(FacesContext context, Object state) {
	// if we have state
	if (null == state) {
	    return;
	}
	
	if (!(state instanceof MethodBinding)) {
	    Object [] stateStruct = (Object []) state;
	    Object savedState = stateStruct[0];
	    String className = stateStruct[1].toString();
	    MethodBinding result = null;
	    
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
			    (MethodBinding) toRestoreClass.newInstance();
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
		binding = result;
	    }
	}
	else {
	    binding = (MethodBinding) state;
	}
    }

    private boolean tranzient = false;

    public boolean isTransient() {
	return tranzient;
    }

    public void setTransient(boolean newTransientMethod) {
	tranzient = newTransientMethod;
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
 

    // 
    // methods used by classes aware of this class's wrapper nature
    //

    public MethodBinding getWrapped() {
	return binding;
    }

}
