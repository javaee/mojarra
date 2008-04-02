/*
 * $Id: ValueChangedListener.java,v 1.2 2003/01/16 20:24:21 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UIComponent;


/**
 * <p>A listener interface for receiving {@link ValueChangedEvent}s.  A class
 * that is interested in receiving such events implements this interface, and
 * then registers itself with the source {@link UIComponent} of interest, by
 * calling <code>addValueChangedListener()</code>.</p>
 */

public interface ValueChangedListener extends FacesListener {


    /**
     * <p>Invoked when the value change described by the specified
     * {@link ValueChangedEvent} occurs.</p>
     *
     * @param event The {@link ValueChangedEvent} that has occurred
     */
    public void processValueChanged(ValueChangedEvent event);


}
