/*
 * $Id: ActionListener.java,v 1.7 2005/08/22 22:08:05 ofung Exp $
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
