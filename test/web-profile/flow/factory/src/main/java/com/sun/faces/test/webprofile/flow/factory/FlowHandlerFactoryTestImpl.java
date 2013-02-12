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
package com.sun.faces.test.webprofile.flow.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.FlowHandlerFactory;
import javax.faces.flow.FlowHandlerFactoryWrapper;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * A simple wrapped flow handler factory.
 */
@Named
@ApplicationScoped
public class FlowHandlerFactoryTestImpl extends FlowHandlerFactoryWrapper {

    public FlowHandlerFactoryTestImpl() {
    }
    
    private FlowHandlerFactory wrapped;
    
    @Inject
    private AppBean appBean;

    /**
     * Constructor.
     *
     * @param wrapped the wrapped flow handler factory.
     */
    public FlowHandlerFactoryTestImpl(FlowHandlerFactory wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public FlowHandlerFactory getWrapped() {
        return this.wrapped;
    }

    /**
     * Add a message to the context every time the createFlowHandler method is
     * called so we can verify later that the factory is actually being used.
     *
     * @param context the Faces context.
     * @return the flow handler.
     */
    @Override
    public FlowHandler createFlowHandler(FacesContext context) {
        System.out.println("createFlowHandler");
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("flowHandlerFactoryWrapped", true);
        String id = (null != appBean) ? appBean.getId() : "null";
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("appBean", id);
        return getWrapped().createFlowHandler(context);
    }
}
