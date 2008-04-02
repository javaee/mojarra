/*
 * $Id: ActionListener.java,v 1.3 2003/01/24 00:21:05 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UIComponent;


/**
 * <p>A listener interface for receiving {@link ActionEvent}s.  A class that
 * is interested in receiving such events implements this interface, and then
 * registers itself with the source {@link UIComponent} of interest, by
 * calling <code>addActionListener()</code>.</p>
 */

public interface ActionListener extends FacesListener  {


    /**
     * <p>Invoked when the action described by the specified
     * {@link ActionEvent} occurs.</p>
     *
     * @param event The {@link ActionEvent} that has occurred
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     */
    public void processAction(ActionEvent event)
        throws AbortProcessingException;


}
