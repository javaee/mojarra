/*
 * $Id: ValueChangeListener.java,v 1.1 2003/10/27 04:10:03 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UIComponent;


/**
 * <p>A listener interface for receiving {@link ValueChangeEvent}s.  A class
 * that is interested in receiving such events implements this interface, and
 * then registers itself with the source {@link UIComponent} of interest, by
 * calling <code>addValueChangeListener()</code>.</p>
 */

public interface ValueChangeListener extends FacesListener {


    /**
     * <p>Invoked when the value change described by the specified
     * {@link ValueChangeEvent} occurs.</p>
     *
     * @param event The {@link ValueChangeEvent} that has occurred
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     */
    public void processValueChange(ValueChangeEvent event)
        throws AbortProcessingException;


}
