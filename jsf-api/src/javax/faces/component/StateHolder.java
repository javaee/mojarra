/*
 * $Id: StateHolder.java,v 1.9 2003/11/03 22:00:54 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.io.IOException;

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
     * instances as well.</p>
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

    public void restoreState(FacesContext context, Object state) throws IOException;

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
