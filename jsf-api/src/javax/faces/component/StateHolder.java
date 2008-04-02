/*
 * $Id: StateHolder.java,v 1.2 2003/07/28 22:18:41 eburns Exp $
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
 * component state between requests.</p>
 *
 * <p>An implementor <strong>must</strong> implement both {@link
 * #getState} and {@link #restoreState} methods in this class, since
 * these two methods have a tightly coupled contract between themselves.
 * In other words, if there is an ineritance hierarchy, it is not
 * permissable to have the {@link #getState} and {@link #restoreState}
 * methods reside at different levels of the hierarchy.</p>
 *
 */

public interface StateHolder {

    /**
     * <p> Gets the state of the component as a serializable Object.<p>
     *
     * <p>If the class that implements this interface may have
     * references to instances that may also implement StateHolder (such
     * as a UIComponent with child components, faces, event handlers,
     * etc.) this method must call the getState() method on all those
     * instances as well.</p>
     *
     */

    public Object getState(FacesContext context);

    /**
     *
     * <p> Perform any processing required to restore the state from the
     * entries in the state Object.</p>
     *
     * <p>If the class that implements this interface may have
     * references to instances that may also implement StateHolder (such
     * as a UIComponent with child components, faces, event handlers,
     * etc.) this method must call the restoreState() method on all
     * those instances as well. </p>
     *
     */

    public void restoreState(FacesContext context, Object state) throws IOException;

    /**
     *
     * <p>If true, the Object implementing this interface must not
     * participate in state saving or restoring.</p>
     */

    public boolean isTransient();

    public void setTransient(boolean newTransientValue);

}
