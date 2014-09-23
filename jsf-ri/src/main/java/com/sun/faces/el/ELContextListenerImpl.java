/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.el;

import javax.faces.context.FacesContext;
import javax.el.ELContext;
import javax.el.ELContextListener;
import javax.el.ELContextEvent;
import javax.el.ExpressionFactory;

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
        ExpressionFactory exFactory = ELUtils.getDefaultExpressionFactory(context);
        if (null != exFactory) {
            source.putContext(ExpressionFactory.class, exFactory);
        }
        
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
