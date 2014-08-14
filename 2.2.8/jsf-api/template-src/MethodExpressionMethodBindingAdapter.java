/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

import javax.el.MethodExpression;
import javax.el.ELException;
import javax.el.ELContext;
import javax.el.MethodInfo;
import javax.faces.el.MethodBinding;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import java.util.Arrays;
import java.lang.reflect.Method;
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
	
	if (context == null) {
	    throw new NullPointerException("ELContext -> null");
    }

	if (null == info) {
	    FacesContext facesContext = (FacesContext) 
		context.getContext(FacesContext.class);
	    if (null != facesContext) {
		try {
		    //PENDING - we should find a way to provide more information
		    info = new MethodInfo(null, binding.getType(facesContext), null);
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
	
	if (context == null) {
	    throw new NullPointerException("ELContext -> null");
    }

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
        
        if (other == this) {
            return true;
        }
        
        if (other instanceof MethodExpressionMethodBindingAdapter) {
            MethodBinding ob = ((MethodExpressionMethodBindingAdapter) other).getWrapped();
            return (binding.equals(ob));
        } else if (other instanceof MethodExpression) {
            MethodExpression expression = (MethodExpression) other;
            
            // We'll need to do a little leg work to determine
            // if the MethodBinding is equivalent to the 
            // wrapped MethodExpression
            String expr = binding.getExpressionString();
            int idx = expr.indexOf('.');
            String target = expr.substring(0, idx).substring(2);
            String t = expr.substring(idx + 1);
            String method = t.substring(0, (t.length() - 1));
            
            FacesContext context = FacesContext.getCurrentInstance();
            ELContext elContext = context.getELContext();
            MethodInfo controlInfo = expression.getMethodInfo(elContext);
            
            // ensure the method names are the same
            if (!controlInfo.getName().equals(method)) {
                return false;
            }
            
            // Using the target, create an expression and evaluate
            // it.           
            ExpressionFactory factory = context.getApplication().getExpressionFactory();
            ValueExpression ve = factory.createValueExpression(elContext,
                                                               "#{" + target + '}',
                                                               Object.class);
            if (ve == null) {
                return false;
            }                
                                              
            Object result = ve.getValue(elContext);
            
            if (result == null) {
                return false;
            }
            
            
            // Get all of the methods with the matching name and try
            // to find a match based on controlInfo's return and parameter
            // types
            Method[] methods = result.getClass().getMethods();
            for (Method meth : methods) {
                if (meth.getName().equals(method)
                     && meth.getReturnType().equals(controlInfo.getReturnType())
                     && Arrays.equals(meth.getParameterTypes(), 
                                      controlInfo.getParamTypes())) {
                    return true;                      
                }
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
        if (context == null) {
            throw new NullPointerException();
        }
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
        if (context == null) {
            throw new NullPointerException();
        }
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
         return Class.forName(name, true, loader);
    }
 

    // 
    // methods used by classes aware of this class's wrapper nature
    //

    public MethodBinding getWrapped() {
	return binding;
    }

}
