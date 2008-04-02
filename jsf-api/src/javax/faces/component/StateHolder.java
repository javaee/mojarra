/*
 * $Id: StateHolder.java,v 1.15 2005/08/22 22:07:54 ofung Exp $
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

/**
 *
 * <p>This interface is implemented by classes that need to save their
 * state between requests.</p>
 *
 * <p>An implementor <strong>must</strong> implement both {@link
 * #saveState} and {@link #restoreState} methods in this class, since
 * these two methods have a tightly coupled contract between themselves.
 * In other words, if there is an ineritance hierarchy, it is not
 * permissable to have the {@link #saveState} and {@link #restoreState}
 * methods reside at different levels of the hierarchy.</p>
 *
 * <p>An implementor must have a public no-args constructor.</p>
 *
 */

public interface StateHolder {

    /**
     * <p> Gets the state of the instance as a
     * <code>Serializable</code> Object.<p>
     *
     * <p>If the class that implements this interface has references to
     * instances that implement StateHolder (such as a
     * <code>UIComponent</code> with event handlers, validators, etc.)
     * this method must call the {@link #saveState} method on all those
     * instances as well.  <strong>This method must not save the state
     * of children and facets.</strong> That is done via the {@link
     * javax.faces.application.StateManager}</p>
     *
     * <p>This method must not alter the state of the implementing
     * object.  In other words, after executing this code:</p>
     *
     * <code><pre>
     * Object state = component.saveState(facesContext);
     * </pre></code>
     *
     * <p><code>component</code> should be the same as before executing
     * it.</p>
     *
     * <p>The return from this method must be <code>Serializable</code></p>
     * 
     * @exception NullPointerException if <code>context</code> is null
     */

    public Object saveState(FacesContext context);

    /**
     *
     * <p> Perform any processing required to restore the state from the
     * entries in the state Object.</p>
     *
     * <p>If the class that implements this interface has references to
     * instances that also implement StateHolder (such as a
     * <code>UIComponent</code> with event handlers, validators, etc.)
     * this method must call the {@link #restoreState} method on all those
     * instances as well. </p>
     * 
     * @exception NullPointerException if either <code>context</code> or
     *  <code>state</code> are <code>null</code>
     */

    public void restoreState(FacesContext context, Object state);

    /**
     *
     * <p>If true, the Object implementing this interface must not
     * participate in state saving or restoring.</p>
     */

    public boolean isTransient();

    /**
     * <p>Denotes whether or not the Object implementing this interface must
     * or must not participate in state saving or restoring.</p>
     * 
     * @param newTransientValue boolean pass <code>true</code> if this Object 
     *  will participate in state saving or restoring, otherwise 
     *  pass <code>false</code>.
     */ 
    public void setTransient(boolean newTransientValue);

}
