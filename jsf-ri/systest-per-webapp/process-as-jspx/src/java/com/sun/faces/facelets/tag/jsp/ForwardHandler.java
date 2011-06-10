
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

package com.sun.faces.facelets.tag.jsp;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContextWrapper;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import javax.faces.webapp.FacesServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;




public class ForwardHandler extends TagHandler {

    private final TagAttribute page;
    private static final Logger LOGGER =
          Logger.getLogger("javax.faces.webapp", "javax.faces.LogStrings");


    public ForwardHandler(TagConfig config) {
        super(config);

        this.page = this.getRequiredAttribute("page");

    }

    public void apply(FaceletContext ctx, UIComponent component) throws IOException {
        nextHandler.apply(ctx, component);
        FacesContext context = ctx.getFacesContext();
        ExternalContext extContext = context.getExternalContext();
        // Test for portlet or servlet
        Object obj = extContext.getContext();
        if (obj instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) obj;
            String path = this.page.getValue(ctx);
            RequestDispatcher rd = servletContext.getRequestDispatcher(path);
            final Map<String, ValueExpression> params = ParamHandler.getParams(context, component);
            HttpServletRequest req = (HttpServletRequest) extContext.getRequest();
            if (!params.isEmpty()) {
                req = new WrapHttpServletRequestToAddParams(context, params, req);
            }
            FacesContextFactory facesContextFactory = null;
            Lifecycle lifecycle = null;

            // Acquire our FacesContextFactory instance
            try {
                facesContextFactory = (FacesContextFactory)
                    FactoryFinder.getFactory
                    (FactoryFinder.FACES_CONTEXT_FACTORY);
            } catch (FacesException e) {
                ResourceBundle rb = LOGGER.getResourceBundle();
                String msg = rb.getString("severe.webapp.facesservlet.init_failed");
                Throwable rootCause = (e.getCause() != null) ? e.getCause() : e;
                LOGGER.log(Level.SEVERE, msg, rootCause);
                throw new IOException(msg);
            }

            // Acquire our Lifecycle instance
            try {
                LifecycleFactory lifecycleFactory = (LifecycleFactory)
                      FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
                String lifecycleId;

                // This is a bug.  Custom lifecycles configured via a <init-parameter>
                // are not available at this point.  The correct solution
                // would be to have some way to get the currently active Lifecycle
                // instance.
                lifecycleId = servletContext.getInitParameter
                                 (FacesServlet.LIFECYCLE_ID_ATTR);

                if (lifecycleId == null) {
                    lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
                }
                lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
            } catch (FacesException e) {
                Throwable rootCause = e.getCause();
                if (rootCause == null) {
                    throw e;
                } else {
                    throw new IOException(e.getMessage(), rootCause);
                }
            }

            FacesContext newFacesContext = null;

            try {
                newFacesContext = facesContextFactory.
                        getFacesContext(servletContext, req,
                                        (ServletResponse) extContext.getResponse(),
                                        lifecycle);
                WrapFacesContextToAllowSetCurrentInstance.doSetCurrentInstance(newFacesContext);
                rd.forward(req, (ServletResponse) extContext.getResponse());
            } catch (ServletException ex) {
                throw new IOException(ex);
            } finally {
                if (null != newFacesContext) {
                    newFacesContext.release();
                }
                WrapFacesContextToAllowSetCurrentInstance.doSetCurrentInstance(context);
                context.responseComplete();
            }

        }

    }

    private static final class WrapFacesContextToAllowSetCurrentInstance extends FacesContextWrapper {

        private FacesContext wrapped;

        public WrapFacesContextToAllowSetCurrentInstance(FacesContext wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public FacesContext getWrapped() {
            return this.wrapped;
        }

        private static void doSetCurrentInstance(FacesContext currentInstance) {
            setCurrentInstance(currentInstance);
        }


    }

}
