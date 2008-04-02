/*
 * $Id: StateHolderWithBackReference.java,v 1.1 2003/09/18 00:49:44 eburns Exp $
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
 * state between requests, and require a back reference to the
 * <code>UIComponent</code> to which they are attached.</p>
 *
 * <p>An implementor must have a public no-args constructor.</p>
 *
 */

public interface StateHolderWithBackReference extends StateHolder {

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
     * <p>If the class that implements this interface requires a "back
     * reference" to a <code>UIComponent</code>, the
     * <code>toAttachTo</code> parameter is used to convey it.</p>
     *
     * @param context the {@link FacesContext} for this request
     *
     * @param state the opaque state object we returned from {@link
     * #saveState}
     *
     * @param toAttachTo the UIComponent to which we are attached, if
     * any.
     *
     *
     */

    public void restoreState(FacesContext context, Object state, 
			     UIComponent toAttachTo) throws IOException;

    /**
     *
     * <p>If true, the Object implementing this interface must not
     * participate in state saving or restoring.</p>
     */

    public boolean isTransient();

    public void setTransient(boolean newTransientValue);

}
