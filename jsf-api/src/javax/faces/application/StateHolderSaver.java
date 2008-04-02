/*
 * $Id: StateHolderSaver.java,v 1.3 2003/09/15 20:17:18 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;

import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.Serializable;

/**
 * <p>Helper class for saving and restoring attached objects.</p>
 *
 */
class StateHolderSaver extends Object implements Serializable {
    protected String className = null;
    protected Object savedState = null;

    StateHolderSaver(FacesContext context, Object toSave) {
        className = toSave.getClass().getName();

        if (toSave instanceof StateHolder) {
            if (!((StateHolder)toSave).isTransient()) {
                savedState = ((StateHolder)toSave).saveState(context);
            }
        }
    }

    /**
     *
     * @return the restored {@link StateHolder} instance.
     */

    Object restore(FacesContext context) throws IOException {
        Object result = null;
        Class toRestoreClass = null;
        try {
            toRestoreClass = loadClass(className, this);
        }
        catch (ClassNotFoundException e) {
            toRestoreClass = null;
        }

        if (null != toRestoreClass) {
            try {
                result = toRestoreClass.newInstance();
            }
            catch (InstantiationException e) {
                throw new IOException(e.getMessage());
            }
            catch (IllegalAccessException a) {
                throw new IOException(a.getMessage());
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
