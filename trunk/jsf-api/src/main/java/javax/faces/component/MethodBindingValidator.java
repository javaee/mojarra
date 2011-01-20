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

package javax.faces.component;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import javax.faces.el.MethodBinding;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;

/**
 * <p><strong>MethodBindingValidator</strong> is an {@link
 * ValidatorListener} that wraps a {@link MethodBinding}. When it
 * receives a {@link ActionEvent}, it executes a method on an
 * object identified by the {@link MethodBinding}.</p>
 */

class MethodBindingValidator extends MethodBindingAdapterBase implements Validator, StateHolder {


    // ------------------------------------------------------ Instance Variables
    
    private MethodBinding methodBinding = null;

    public MethodBindingValidator() {}

    
    /**
     * <p>Construct a {@link Validator} that contains a {@link
     * MethodBinding}.</p>
     */
    public MethodBindingValidator(MethodBinding methodBinding) {

        super();
        this.methodBinding = methodBinding;

    }

    public MethodBinding getWrapped() {
	return methodBinding;
    }


    // ------------------------------------------------------- Validator

    public void validate(FacesContext context,
                         UIComponent  component,
                         Object       value) throws ValidatorException {
	if (null == context || null == component) {
	    throw new NullPointerException();
	}
        try {
            methodBinding.invoke(context, new Object[] {context, component, 
							value});
        } 
	catch (EvaluationException ee) {
	    Throwable cause = this.getExpectedCause(ValidatorException.class,
						    ee);
	    if (cause instanceof ValidatorException) {
		throw ((ValidatorException) cause);
	    }
	    if (cause instanceof RuntimeException) {
		throw ((RuntimeException) cause);
	    }
	    throw new IllegalStateException(ee);
        }
	
	
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
	    if (methodBinding instanceof StateHolder) {
		Object [] stateStruct = new Object[2];
		
		// save the actual state of our wrapped methodBinding
		stateStruct[0] = ((StateHolder)methodBinding).saveState(context);
		// save the class name of the methodBinding impl
		stateStruct[1] = methodBinding.getClass().getName();

		result = stateStruct;
	    }
	    else {
		result = methodBinding;
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
	    
	    Class toRestoreClass;
	    if (null != className) {
		try {
		    toRestoreClass = loadClass(className, this);
		}
		catch (ClassNotFoundException e) {
		    throw new IllegalStateException(e);
		}
		
		if (null != toRestoreClass) {
		    try {
			result = 
			    (MethodBinding) toRestoreClass.newInstance();
		    }
		    catch (InstantiationException e) {
			throw new IllegalStateException(e);
		    }
		    catch (IllegalAccessException a) {
			throw new IllegalStateException(a);
		    }
		}
		
		if (null != result && null != savedState) {
		    // don't need to check transient, since that was
		    // done on the saving side.
		    ((StateHolder)result).restoreState(context, savedState);
		}
		methodBinding = result;
	    }
	}
	else {
	    methodBinding = (MethodBinding) state;
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
        return Class.forName(name, false, loader);
    }
}
