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

package com.sun.faces.application.view;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.view.ViewDeclarationLanguage;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

/**
 * <p>
 * This represents how a particular page description language
 * is to be rendered/restored.
 * <p>
 */
public abstract class ViewHandlingStrategy extends ViewDeclarationLanguage {

    private static final Logger logger = FacesLogger.APPLICATION.getLogger();

    protected ApplicationAssociate associate;
    protected WebConfiguration webConfig;


    // ------------------------------------------------------------ Constructors


    public ViewHandlingStrategy() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        webConfig = WebConfiguration.getInstance(ctx.getExternalContext());
        associate = ApplicationAssociate.getInstance(ctx.getExternalContext());

    }

    // ---------------------------------------------------------- Public Methods


    /**
     * @see ViewDeclarationLanguage#restoreView(javax.faces.context.FacesContext, String)
     */
    @Override
    public UIViewRoot restoreView(FacesContext ctx,
                                  String viewId) {

        ExternalContext extContext = ctx.getExternalContext();

        String mapping = Util.getFacesMapping(ctx);
        UIViewRoot viewRoot = null;

        // maping could be null if a non-faces request triggered
        // this response.
        if (extContext.getRequestPathInfo() == null && mapping != null &&
            Util.isPrefixMapped(mapping)) {
            // this was probably an initial request
            // send them off to the root of the web application
            try {
                ctx.responseComplete();
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "Response Complete for" + viewId);
                }
                if (!extContext.isResponseCommitted()) {
                    extContext.redirect(extContext.getRequestContextPath());
                }
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }
        } else {
            // this is necessary to allow decorated impls.
            ViewHandler outerViewHandler =
                  ctx.getApplication().getViewHandler();
            String renderKitId =
                  outerViewHandler.calculateRenderKitId(ctx);
            viewRoot = Util.getStateManager(ctx).restoreView(ctx,
                                                             viewId,
                                                             renderKitId);
        }

        return viewRoot;
        
    }

    
    /**
     * @see ViewDeclarationLanguage#createView(javax.faces.context.FacesContext, String)
     */
    @Override
    public UIViewRoot createView(FacesContext ctx,
                                 String viewId) {

        Util.notNull("context", ctx);

        UIViewRoot result = (UIViewRoot)
              ctx.getApplication()
                    .createComponent(UIViewRoot.COMPONENT_TYPE);

        Locale locale = null;
        String renderKitId = null;

        // use the locale from the previous view if is was one which will be
        // the case if this is called from NavigationHandler. There wouldn't be
        // one for the initial case.
        if (ctx.getViewRoot() != null) {
            locale = ctx.getViewRoot().getLocale();
            renderKitId = ctx.getViewRoot().getRenderKitId();
        }

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Created new view for " + viewId);
        }
        // PENDING(): not sure if we should set the RenderKitId here.
        // The UIViewRoot ctor sets the renderKitId to the default
        // one.
        // if there was no locale from the previous view, calculate the locale
        // for this view.
        if (locale == null) {
            locale = ctx.getApplication().getViewHandler().calculateLocale(ctx);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(
                      "Locale for this view as determined by calculateLocale "
                      + locale.toString());
            }
        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Using locale from previous view "
                            + locale.toString());
            }
        }

        if (renderKitId == null) {
            renderKitId =
                  ctx.getApplication().getViewHandler()
                        .calculateRenderKitId(ctx);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(
                      "RenderKitId for this view as determined by calculateRenderKitId "
                      + renderKitId);
            }
        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Using renderKitId from previous view "
                            + renderKitId);
            }
        }

        result.setLocale(locale);
        result.setRenderKitId(renderKitId);
        result.setViewId(viewId);

        return result;
    }

    /**
     *
     * @param viewId the view ID
     * @return <code>true</code> if this <code>ViewHandlingStrategy</code>
     *  handles the the view type represented by <code>viewId</code>
     */
    
    public abstract boolean handlesViewId(String viewId);


}
