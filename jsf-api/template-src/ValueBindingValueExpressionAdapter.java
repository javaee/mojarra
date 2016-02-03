/*
 * $Id: ValueBindingValueExpressionAdapter.java,v 1.6 2007/04/27 22:00:12 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
            
        if (context == null) {
	        throw new NullPointerException("FacesContext -> null");
        }
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
        if (context == null) {
	        throw new NullPointerException("FacesContext -> null");
        }
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
            
        if (context == null) {
	        throw new NullPointerException("FacesContext -> null");
        }
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
            
        if (context == null) {
	        throw new NullPointerException("FacesContext -> null");
        }
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
    
    public boolean equals(Object other) {
    
        if (other == this) {
            return true;
        }
        
        if (other instanceof ValueBindingValueExpressionAdapter) {
            ValueExpression expr = 
                ((ValueBindingValueExpressionAdapter) other).getWrapped();
            return (valueExpression.equals(expr));
        } else if (other instanceof ValueBinding) {
            FacesContext context = FacesContext.getCurrentInstance();
            ValueBinding otherVB = (ValueBinding) other;
            Class type = otherVB.getType(context);
            if (type != null) {
                return type.equals(valueExpression.getType(context.getELContext()));               
            }            
        }
        return false;
        
    }

    public int hashCode() {    
        assert(null != valueExpression);
        return valueExpression.hashCode();
    }
    
    public ValueExpression getWrapped() {
        return valueExpression;
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
        return Class.forName(name, true, loader);
    }
 


}
