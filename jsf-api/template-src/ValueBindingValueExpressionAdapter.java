/*
 * $Id: ValueBindingValueExpressionAdapter.java,v 1.2 2005/05/19 13:26:58 rlubke Exp $
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
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

import javax.el.ValueExpression;
import javax.el.ELException;

/**
 * <p>Wrap a ValueExpression instance and expose it as a ValueBinding</p>
 *
 * @author Jacob Hookom
 */
@protection@ class ValueBindingValueExpressionAdapter extends ValueBinding implements StateHolder, 
    Serializable {

    private static final long serialVersionUID = @serialVersionUID@;

    private ValueExpression valueExpression= null;
    private boolean tranzient;

    public ValueBindingValueExpressionAdapter() {} // for StateHolder
    
    @protection@ ValueBindingValueExpressionAdapter(ValueExpression valueExpression) {
        this.valueExpression = valueExpression;    
    }
    
   
    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#getExpressionString()
     */
    public String getExpressionString() {
	assert(null != valueExpression);
        return valueExpression.getExpressionString();
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#getType(javax.faces.context.FacesContext)
     */
    public Class getType(FacesContext context) throws EvaluationException,
            PropertyNotFoundException {
       Class result = null;
        try {
            result = valueExpression.getType(context.getELContext());
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        } 
        return result;
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#getValue(javax.faces.context.FacesContext)
     */
    public Object getValue(FacesContext context) throws EvaluationException,
            PropertyNotFoundException {
        Object result = null;
        try {
            result = valueExpression.getValue(context.getELContext());
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#isReadOnly(javax.faces.context.FacesContext)
     */
    public boolean isReadOnly(FacesContext context) throws EvaluationException,
            PropertyNotFoundException {
        boolean result = false;
        try {
            result = valueExpression.isReadOnly(context.getELContext());
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }
    
    

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#setValue(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void setValue(FacesContext context, Object value)
            throws EvaluationException, PropertyNotFoundException {
        try {
            valueExpression.setValue(context.getELContext(), value);
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (javax.el.PropertyNotWritableException pnwe) {
            throw new PropertyNotFoundException(pnwe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
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
	    if (valueExpression instanceof StateHolder) {
		Object [] stateStruct = new Object[2];
		
		// save the actual state of our wrapped valueExpression
		stateStruct[0] = ((StateHolder)valueExpression).saveState(context);
		// save the class name of the valueExpression impl
		stateStruct[1] = valueExpression.getClass().getName();

		result = stateStruct;
	    }
	    else {
		result = valueExpression;
	    }
	}

	return result;
	
    }

    public void restoreState(FacesContext context, Object state) {
	// if we have state
	if (null == state) {
	    return;
	}
	
	if (!(state instanceof ValueExpression)) {
	    Object [] stateStruct = (Object []) state;
	    Object savedState = stateStruct[0];
	    String className = stateStruct[1].toString();
	    ValueExpression result = null;
	    
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
			    (ValueExpression) toRestoreClass.newInstance();
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
		valueExpression = result;
	    }
	}
	else {
	    valueExpression = (ValueExpression) state;
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
