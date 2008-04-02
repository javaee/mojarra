/*
 * $Id: StateHolderSaver.java,v 1.11 2005/05/19 13:26:56 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.io.Serializable;

import javax.faces.context.FacesContext;

/**
 * <p>Helper class for saving and restoring attached objects.</p>
 */
class StateHolderSaver extends Object implements Serializable {

    private static final long serialVersionUID = 6470180891722042701L;

    private String className = null;
    private Object savedState = null;

    public StateHolderSaver(FacesContext context, Object toSave) {
	className = toSave.getClass().getName();
	
        if (toSave instanceof StateHolder) {
            // do not save an attached object that is marked transient.
            if (!((StateHolder)toSave).isTransient()) {
                savedState = ((StateHolder)toSave).saveState(context);
            } else {
                className = null;
            }
        }
	else if (toSave instanceof Serializable) {
	    savedState = toSave;
	    className = null;
	}
    }

    /**
     *
     * @return the restored {@link StateHolder} instance.
     */

    public Object restore(FacesContext context) throws IllegalStateException {
        Object result = null;
        Class toRestoreClass = null;

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
        return loader.loadClass(name);
    }
}
