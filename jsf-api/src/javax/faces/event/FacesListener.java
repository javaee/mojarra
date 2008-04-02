/*
 * $Id: FacesListener.java,v 1.9 2005/08/22 22:08:05 ofung Exp $
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


import java.util.EventListener;


/**
 * <p>A generic base interface for event listeners for various types of
 * {@link FacesEvent}s.  All listener interfaces for specific
 * {@link FacesEvent} event types must extend this interface.</p>
 *
 * <p>Implementations of this interface must have a zero-args public
 * constructor.  If the class that implements this interface has state
 * that needs to be saved and restored between requests, the class must
 * also implement {@link javax.faces.component.StateHolder}.</p>
 */

public interface FacesListener extends EventListener {

}
