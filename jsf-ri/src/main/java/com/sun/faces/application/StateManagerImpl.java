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

import com.sun.faces.application.view.JspStateManagementStrategy;
import java.io.IOException;
import java.util.Map;
import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.ResponseStateManager;
import javax.faces.view.StateManagementStrategy;
import javax.faces.view.ViewDeclarationLanguage;

/**
 * <p> A
 * <code>StateManager</code> implementation to meet the requirements of the
 * specification. </p>
 */
public class StateManagerImpl extends StateManager {

    /**
     * Save the view.
     *
     * @param context the Faces context.
     * @return the saved view.
     */
    @Override
    public Object saveView(FacesContext context) {
        Object result = null;

        if (context != null && !context.getViewRoot().isTransient()) {
            UIViewRoot viewRoot = context.getViewRoot();
            StateManagementStrategy strategy = null;
            String viewId = viewRoot.getViewId();

            ViewDeclarationLanguage vdl =
                    context.getApplication().getViewHandler().
                    getViewDeclarationLanguage(context, viewId);

            if (vdl != null) {
                strategy = vdl.getStateManagementStrategy(context, viewId);
            }

            Map<Object, Object> contextAttributes = context.getAttributes();

            try {
                contextAttributes.put(StateManager.IS_SAVING_STATE, Boolean.TRUE);

                if (strategy != null) {
                    result = strategy.saveView(context);
                } else {
                    strategy = new JspStateManagementStrategy(context);
                    result = strategy.saveView(context);
                }
            } finally {
                contextAttributes.remove(StateManager.IS_SAVING_STATE);
            }
        }

        return result;
    }

    /**
     * Write out the state.
     *
     * @param context the Faces context.
     * @param state the state.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void writeState(FacesContext context, Object state) throws IOException {
        RenderKit rk = context.getRenderKit();
        ResponseStateManager rsm = rk.getResponseStateManager();
        rsm.writeState(context, state);
    }

    /**
     * Restores the view.
     * 
     * @param context the Faces context.
     * @param viewId the view id.
     * @param renderKitId the render kit id.
     * @return the view root.
     * @see StateManager#restoreView(javax.faces.context.FacesContext, java.lang.String, java.lang.String) 
     */
    public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {
        UIViewRoot result;
        StateManagementStrategy strategy = null;

        ViewDeclarationLanguage vdl =
                context.getApplication().getViewHandler().
                getViewDeclarationLanguage(context, viewId);

        if (vdl != null) {
            strategy = vdl.getStateManagementStrategy(context, viewId);
        }

        if (strategy != null) {
            result = strategy.restoreView(context, viewId, renderKitId);
        } else {
            strategy = new JspStateManagementStrategy(context);
            result = strategy.restoreView(context, viewId, renderKitId);
        }

        return result;
    }
}
