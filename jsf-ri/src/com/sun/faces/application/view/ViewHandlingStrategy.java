/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.application.ApplicationAssociate;

/**
 * <p>
 * This represents how a particular page description language
 * is to be rendered/restored.
 * <p>
 */
public abstract class ViewHandlingStrategy {

    protected boolean responseBufferSizeSet;
    protected int responseBufferSize;
    protected ApplicationAssociate associate;
    protected WebConfiguration webConfig;


    // ------------------------------------------------------------ Constructors


    public ViewHandlingStrategy() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        webConfig = WebConfiguration.getInstance(ctx.getExternalContext());
        associate = ApplicationAssociate.getInstance(ctx.getExternalContext());
         try {
            responseBufferSizeSet = webConfig
                  .isSet(WebConfiguration.WebContextInitParameter.ResponseBufferSize);
            responseBufferSize = Integer
                  .parseInt(webConfig.getOptionValue(
                        WebConfiguration.WebContextInitParameter.ResponseBufferSize));
        } catch (NumberFormatException nfe) {
            responseBufferSize = Integer
                  .parseInt(WebConfiguration.WebContextInitParameter.ResponseBufferSize.getDefaultValue());
        }

    }


    // ---------------------------------------------------------- Public Methods


    /**
     *
     * @param viewId the view ID
     * @return <code>true</code> if this <code>ViewHandlingStrategy</code>
     *  handles the the view type represented by <code>viewId</code>
     */
    public abstract boolean handlesViewId(String viewId);


    /**
     * <p>
     * Render the specified view in an implementation dependent manner.
     * </p>
     * @param ctx the {@link FacesContext} for the current request
     * @param vh the {@link MultiViewHandler} that is calling this
     *  <code>ViewHandlingStrategy</code>
     * @param view the {@link UIViewRoot} to render
     * @throws IOException if an error occurs rendering the view
     */
    public abstract void renderView(FacesContext ctx,
                                    MultiViewHandler vh,
                                    UIViewRoot view)
    throws IOException;


    /**
     * <p>
     * Restore the specified viewId in an implementation specific manner.
     * <p>
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param vh the {@link MultiViewHandler} that is calling this
     *  <code>ViewHandlingStrategy</code>
     * @param viewId the view ID to restore
     * @return thew restored {@link UIViewRoot}
     */
    public UIViewRoot restoreView(FacesContext ctx,
                                  MultiViewHandler vh,
                                  String viewId) {

        return vh.restoreViewPrivate(ctx, viewId);
        
    }

    
    /**
     * Create a new UIViewRoot using the specified viewId.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param vh the {@link MultiViewHandler} that is calling this
     *  <code>ViewHandlingStrategy</code>
     * @param viewId the view ID to restore
     * @return a new {@link UIViewRoot}
     */
    public UIViewRoot createView(FacesContext ctx,
                                 MultiViewHandler vh,
                                 String viewId) {

        return vh.createViewPrivate(ctx, viewId);

    }

}
