/*
 * $Id: ELContextListenerImpl.java,v 1.4 2006/03/29 23:03:43 rlubke Exp $
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

package com.sun.faces.config;

import javax.faces.context.FacesContext;
import javax.el.ELContext;
import javax.el.ELContextListener;
import javax.el.ELContextEvent;

public class ELContextListenerImpl implements ELContextListener {
    
    public ELContextListenerImpl() {
    }
    
    /**
     * Invoked when a new <code>ELContext</code> has been created.
     *
     * @param ece the notification event.
     */
    public void contextCreated(ELContextEvent ece) {
        
        
        FacesContext context = FacesContext.getCurrentInstance();
        if ( context == null) {
            return;
        }
        ELContext source = (ELContext)ece.getSource();
        // Register FacesContext with JSP
        source.putContext(FacesContext.class, context);
        
        // dispatch the event to any JSF applications interested in
        // the event.
        ELContextListener[] listeners = 
            context.getApplication().getELContextListeners();
        if ( listeners == null) {
            return;
        }
        for (int i = 0; i < listeners.length; ++i) {
            ELContextListener elcl = listeners[i];
            elcl.contextCreated(new ELContextEvent(source));
        }
    }
    
}
