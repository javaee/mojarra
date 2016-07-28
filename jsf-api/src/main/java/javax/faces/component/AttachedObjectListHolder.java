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

import javax.faces.context.FacesContext;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * <p>
 * Utility class to enable partial state saving of Lists of attached objects
 * such as <code>FacesListener</code>s or <code>Validator</code>s.
 * </p>
 */
@SuppressWarnings({"unchecked"})
class AttachedObjectListHolder<T> implements PartialStateHolder {

    private boolean initialState;
    private List<T> attachedObjects = new ArrayList<T>(2);


    // ------------------------------------- Methods from PartialStateHolder


    public void markInitialState() {

        if (!attachedObjects.isEmpty()) {
            for (T t : attachedObjects) {
                if (t instanceof PartialStateHolder) {
                    ((PartialStateHolder) t).markInitialState();
                }
            }
        }
        initialState = true;

    }


    public boolean initialStateMarked() {

        return initialState;

    }


    public void clearInitialState() {

        if (!attachedObjects.isEmpty()) {
            for (T t : attachedObjects) {
                if (t instanceof PartialStateHolder) {
                    ((PartialStateHolder) t).clearInitialState();
                }
            }
        }
        initialState = false;

    }


    // -------------------------------------------- Methods from StateHolder


    public Object saveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (attachedObjects == null) {
            return null;
        }
        if (initialState) {
            Object[] attachedObjects = new Object[this.attachedObjects.size()];
            boolean stateWritten = false;
            for (int i = 0, len = attachedObjects.length; i < len; i++) {
                T attachedObject = this.attachedObjects.get(i);
                if (attachedObject instanceof StateHolder) {
                    StateHolder sh = (StateHolder) attachedObject;
                    if (!sh.isTransient()) {
                        attachedObjects[i] = sh.saveState(context);
                    }
                    if (attachedObjects[i] != null) {
                        stateWritten = true;
                    }
                }
            }
            return ((stateWritten) ? attachedObjects : null);
        } else {

            Object[] attachedObjects = new Object[this.attachedObjects.size()];
            for (int i = 0, len = attachedObjects.length; i < len; i++) {
                attachedObjects[i] = UIComponentBase.saveAttachedState(context, this.attachedObjects.get(i));
            }
            return (attachedObjects);
        }

    }


    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }

        Object[] attachedObjects = (Object[]) state;
        if (attachedObjects.length > 0 && attachedObjects[0] instanceof StateHolderSaver) {
            // overwrite the existing attachedObjects with those included
            // in the full state.
            if (this.attachedObjects != null) {
                this.attachedObjects.clear();
            } else {
                this.attachedObjects = new ArrayList<T>(2);
            }
            for (int i = 0, len = attachedObjects.length; i < len; i++) {
                T restored = (T) ((StateHolderSaver) attachedObjects[i]).restore(context);
                if (restored != null) {
                    add(restored);
                }
            }
        } else {
            // assume 1:1 relation between existing attachedObjects and state
            for (int i = 0, len = attachedObjects.length; i < len; i++) {
                T l = this.attachedObjects.get(i);
                if (l instanceof StateHolder) {
                    ((StateHolder) l).restoreState(context, attachedObjects[i]);
                }
            }
        }

    }


    public boolean isTransient() {

        return false;

    }


    public void setTransient(boolean newTransientValue) {

        // no-op

    }


    // ------------------------------------------------------ Public Methods


    void add(T attachedObject) {

        clearInitialState();
        attachedObjects.add(attachedObject);

    }

    void remove(T attachedObject) {

        clearInitialState();
        attachedObjects.remove(attachedObject);

    }

    T[] asArray(Class<T> type) {

        return new ArrayList<T>(attachedObjects).toArray((T[])Array.newInstance(type, attachedObjects.size()));
        
    }
    
    Iterator<T> iterator() {
        return attachedObjects.iterator();
    }
    
}
