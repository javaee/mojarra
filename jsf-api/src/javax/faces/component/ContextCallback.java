/*
 * ComponentCallback.java
 *
 * Created on February 9, 2006, 12:58 PM
 * $Id: ContextCallback.java,v 1.1 2006/02/10 16:02:07 edburns Exp $
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

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 *
 * <p>A simple callback interace that enables taking action on a
 * specific UIComponent (either facet or child) in the view while
 * preserving any contextual state for that component instance in the
 * view.</p>
 *
 */
public interface ContextCallback {
    
    /**
     * <p>This method will be called by an implementation of 
     * {@link UIComponent#invokeOnComponent}.  At the point in time when this 
     * method is called, the argument <code>target</code> is guaranteed to be
     * in the proper state with respect to its ancestors in the View.
     */
    
    public void invokeContextCallback(FacesContext context, UIComponent target) throws FacesException;
    
}
