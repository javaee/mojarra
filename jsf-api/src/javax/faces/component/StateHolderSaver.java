/*
 * $Id: StateHolderSaver.java,v 1.15 2007/01/29 22:18:34 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.component;

import javax.faces.context.FacesContext;

import java.io.Serializable;

/**
 * <p>Helper class for saving and restoring attached objects.</p>
 */
class StateHolderSaver implements Serializable {

    private static final long serialVersionUID = 6470180891722042701L;

    private String className = null;
    private Serializable savedState = null;

    public StateHolderSaver(FacesContext context, Object toSave) {
	className = toSave.getClass().getName();
	
        if (toSave instanceof StateHolder) {
            // do not save an attached object that is marked transient.
            if (!((StateHolder)toSave).isTransient()) {
                savedState = (Serializable) ((StateHolder)toSave).saveState(context);
            } else {
                className = null;
            }
        }
	else if (toSave instanceof Serializable) {
	    savedState = (Serializable) toSave;
	    className = null;
	}
    }

    /**
     *
     * @return the restored {@link StateHolder} instance.
     */

    public Object restore(FacesContext context) throws IllegalStateException {
        Object result = null;
        Class toRestoreClass;

	// if the Object to save implemented Serializable but not
	// StateHolder
	if (null == className && null != savedState) {
	    return savedState;
	}

	// if the Object to save did not implement Serializable or
	// StateHolder
        if ( className == null) {
            return null;
        }

	// else the object to save did implement StateHolder
        
        try {
            toRestoreClass = loadClass(className, this);
        }
        catch (ClassNotFoundException e) {
	    throw new IllegalStateException(e.getMessage());
        }

        if (null != toRestoreClass) {
            try {
                result = toRestoreClass.newInstance();
            }
            catch (InstantiationException e) {
                throw new IllegalStateException(e.getMessage());
            }
            catch (IllegalAccessException a) {
                throw new IllegalStateException(a.getMessage());
            }
        }

        if (null != result && null != savedState &&
	    result instanceof StateHolder) {
	    // don't need to check transient, since that was done on
	    // the saving side.
	    ((StateHolder)result).restoreState(context, savedState);
        }
        return result;
    }


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
