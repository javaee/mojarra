/*
 * $Id: ValueChangeListener.java,v 1.5 2005/12/05 16:42:54 edburns Exp $
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
     * @throws AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     */
    public void processValueChange(ValueChangeEvent event)
        throws AbortProcessingException;


}
