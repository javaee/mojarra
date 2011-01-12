/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

import java.io.Serializable;

/**
 * <p>Helper class for saving and restoring attached objects.</p>
 */
class StateHolderSaver implements Serializable {

    private static final long serialVersionUID = @serialVersionUID@;

    private String className = null;
    private Serializable savedState = null;

    public StateHolderSaver(FacesContext context, Object toSave) {
        className = toSave.getClass().getName();

        if (toSave instanceof StateHolder) {
            // do not save an attached object that is marked transient.
            if (!((StateHolder) toSave).isTransient()) {
                savedState =
                      (Serializable) ((StateHolder) toSave).saveState(context);
            } else {
                className = null;
            }
        } else if (toSave instanceof Serializable) {
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
        if (className == null) {
            return null;
        }

        // else the object to save did implement StateHolder

        try {
            toRestoreClass = loadClass(className, this);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        if (null != toRestoreClass) {
            try {
                result = toRestoreClass.newInstance();
            }
            catch (InstantiationException e) {
                throw new IllegalStateException(e);
            }
            catch (IllegalAccessException a) {
                throw new IllegalStateException(a);
            }
        }

        if (null != result && null != savedState &&
            result instanceof StateHolder) {
            // don't need to check transient, since that was done on
            // the saving side.
            ((StateHolder) result).restoreState(context, savedState);
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
