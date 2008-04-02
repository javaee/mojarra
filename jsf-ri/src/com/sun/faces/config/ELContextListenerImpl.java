/*
 * $Id: ELContextListenerImpl.java,v 1.1 2005/05/05 20:51:20 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
