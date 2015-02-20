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
package com.sun.faces.application;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.flow.FlowDiscoveryCDIExtension;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Producer;
import javax.faces.context.FacesContext;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.builder.FlowDefinition;

class JavaFlowLoaderHelper {
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    
    synchronized void loadFlows(FacesContext context, FlowHandler flowHandler) throws IOException {
        BeanManager beanManager = (BeanManager) 
                Util.getCdiBeanManager(context);
        Bean<?> extensionImpl = beanManager.resolve(beanManager.getBeans(FlowDiscoveryCDIExtension.class));
        if (null == extensionImpl) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to obtain {0} from CDI implementation.  Flows described with {1} are unavailable.", 
                        new String [] {
                            FlowDiscoveryCDIExtension.class.getName(),
                            FlowDefinition.class.getName()
                        });
            }
            return;
        }
        javax.enterprise.context.spi.CreationalContext<?> creationalContext = beanManager.createCreationalContext(extensionImpl);
        FlowDiscoveryCDIExtension myExtension = 
                (FlowDiscoveryCDIExtension) beanManager.getReference(extensionImpl, 
                FlowDiscoveryCDIExtension.class, creationalContext);
        
        List<Producer<Flow>> flowProducers = myExtension.getFlowProducers();
        WebConfiguration config = WebConfiguration.getInstance();
        if (!flowProducers.isEmpty()) {
            enableClientWindowModeIfNecessary(context);
        }
        
        for (Producer<Flow> cur : flowProducers) {
            Flow toAdd = cur.produce(beanManager.<Flow>createCreationalContext(null));
            if (null == toAdd) {
                LOGGER.log(Level.SEVERE, "Flow producer method {0}() returned null.  Ignoring.", cur.toString());
            } else {
                flowHandler.addFlow(context, toAdd);
                config.setHasFlows(true);
            }
        }
        
    }
    
    private void enableClientWindowModeIfNecessary(FacesContext context) {
        
        WebConfiguration config = WebConfiguration.getInstance(context.getExternalContext());
        
        String optionValue = config.getOptionValue(WebConfiguration.WebContextInitParameter.ClientWindowMode);
        boolean clientWindowNeedsEnabling = false;
        if ("none".equals(optionValue)) {
            clientWindowNeedsEnabling = true;
            String featureName = 
                    WebConfiguration.WebContextInitParameter.ClientWindowMode.getQualifiedName();
            LOGGER.log(Level.WARNING, 
                    "{0} was set to none, but Faces Flows requires {0} is enabled.  Setting to ''url''.", new Object[]{featureName});
        } else if (null == optionValue) {
            clientWindowNeedsEnabling = true;
        }
        if (clientWindowNeedsEnabling) {
            config.setOptionValue(WebConfiguration.WebContextInitParameter.ClientWindowMode, "url");
        }
    }
}
