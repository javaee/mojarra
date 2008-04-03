/*
 * $Id: ValueExpressionValueBindingAdapter.java,v 1.7 2007/04/27 22:00:12 ofung Exp $
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

import javax.el.ValueExpression;
import javax.el.ELException;
import javax.el.ELContext;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

import java.io.Serializable;

/**
 * <p>Wrap a ValueBinding instance and expose it as a
 * ValueExpression.</p>
 */

@protection@ class ValueExpressionValueBindingAdapter extends ValueExpression implements Serializable, StateHolder {

    private static final long serialVersionUID = @serialVersionUID@;

    public ValueExpressionValueBindingAdapter() {}

    private ValueBinding binding = null;

    @protection@ ValueExpressionValueBindingAdapter(ValueBinding binding) {
	assert(null != binding);
	this.binding = binding;
    }

    //
    // Methods from ValueExpression
    //

    public Object getValue(ELContext context) throws ELException {
	assert(null != binding);
	if (context == null) {
	    throw new NullPointerException("ELContext -> null");
    }
	Object result = null;
	FacesContext facesContext = (FacesContext) 
	    context.getContext(FacesContext.class);
	assert(null != facesContext);
	try {
	    result = binding.getValue(facesContext);
	}
	catch (Throwable e) {
	    throw new ELException(e);
	}
	return result;
    }

    public void setValue(ELContext context, Object value) throws ELException {
	assert(null != binding);
	if (context == null) {
	    throw new NullPointerException("ELContext -> null");
    }
	FacesContext facesContext = (FacesContext) 
	    context.getContext(FacesContext.class);
	assert(null != facesContext);
	try {
	    binding.setValue(facesContext, value);
	}
	catch (Throwable e) {
	    throw new ELException(e);
	}
    }


    public boolean isReadOnly(ELContext context) throws ELException {
	assert(null != binding);
	if (context == null) {
	    throw new NullPointerException("ELContext -> null");
    }
	boolean result = false;
	FacesContext facesContext = (FacesContext) 
	    context.getContext(FacesContext.class);
	assert(null != facesContext);
	try {
	    result = binding.isReadOnly(facesContext);
	}
	catch (Throwable e) {
	    throw new ELException(e);
	}
	return result;
    }

    public Class<?> getType(ELContext context) throws ELException {
	assert(null != binding);
	if (context == null) {
	    throw new NullPointerException("ELContext -> null");
    }
	Class result = null;
	FacesContext facesContext = (FacesContext) 
	    context.getContext(FacesContext.class);
	assert(null != facesContext);
	try {
	    result = binding.getType(facesContext);
	}
	catch (Throwable e) {
	    throw new ELException(e);
	}
	return result;
    }

    /**
     * <p>Always return <code>false</code> since we can't possibly know
     * if this is a literal text binding or not.</p>
     */

    public boolean isLiteralText() {
	return false;
    }
    
    public Class<?> getExpectedType() {
	assert(null != binding);
	Class result = null;
	FacesContext context = FacesContext.getCurrentInstance();
	try {
	    Object value = binding.getValue(context);
	    result = value.getClass();
	}
	catch (Throwable e) {
	    result = null;
	}
	return result;
    }

    public String getExpressionString() {
	assert(null != binding);
	return binding.getExpressionString();
	
    }

    public boolean equals(Object other) {
    
        if (other == this) {
            return true;
        }
        
        if (other instanceof ValueExpressionValueBindingAdapter) {
            ValueBinding vb = 
                ((ValueExpressionValueBindingAdapter) other).getWrapped();
            return (binding.equals(vb));
        } else if (other instanceof ValueExpression) {
            FacesContext context = FacesContext.getCurrentInstance();
            ValueExpression otherVE = (ValueExpression) other;
            Class type = binding.getType(context);
            if (type != null) {
                return type.equals(otherVE.getType(context.getELContext()));
            }            
        }
        return false;
        
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
	
	if (!(state instanceof ValueBinding)) {
	    Object [] stateStruct = (Object []) state;
	    Object savedState = stateStruct[0];
	    String className = stateStruct[1].toString();
	    ValueBinding result = null;
	    
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
			    (ValueBinding) toRestoreClass.newInstance();
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
	    binding = (ValueBinding) state;
	}
    }

    private boolean tranzient = false;

    public boolean isTransient() {
	return tranzient;
    }

    public void setTransient(boolean newTransientValue) {
	tranzient = newTransientValue;
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
 

    // 
    // methods used by classes aware of this class's wrapper nature
    //

    public ValueBinding getWrapped() {
	return binding;
    }

}
