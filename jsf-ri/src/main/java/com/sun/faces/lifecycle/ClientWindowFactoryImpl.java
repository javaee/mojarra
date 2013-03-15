/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.lifecycle;

import com.sun.faces.config.WebConfiguration;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.lifecycle.ClientWindowFactory;

public class ClientWindowFactoryImpl extends ClientWindowFactory {
    
    private boolean isClientWindowEnabled = false;
    private WebConfiguration config = null;

    public ClientWindowFactoryImpl() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getApplication().subscribeToEvent(PostConstructApplicationEvent.class,
                         Application.class, new PostConstructApplicationListener());
    }
    
    public ClientWindowFactoryImpl(boolean ignored) {
        isClientWindowEnabled = false;
    }
    
    private class PostConstructApplicationListener implements SystemEventListener {

        @Override
        public boolean isListenerForSource(Object source) {
            return source instanceof Application;
        }

        @Override
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            ClientWindowFactoryImpl.this.postConstructApplicationInitialization();
        }
        
    }
    
    private void postConstructApplicationInitialization() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        config = WebConfiguration.getInstance(extContext);
        String optionValue = config.getOptionValue(WebConfiguration.WebContextInitParameter.ClientWindowMode);
        
        isClientWindowEnabled = (null != optionValue) && "url".equals(optionValue);
    }
    
    
    @Override
    public ClientWindow getClientWindow(FacesContext context) {
        if (!isClientWindowEnabled) {
            return null;
        }
        
        return new ClientWindowImpl();
    }
}
