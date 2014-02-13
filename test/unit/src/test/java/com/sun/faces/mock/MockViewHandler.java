/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.mock;

import java.util.Locale;
import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.render.RenderKitFactory;

public class MockViewHandler extends ViewHandler {

    protected StateManager stateManager = null;

    public void renderView(FacesContext context, UIViewRoot viewToRender)
            throws IOException, FacesException {
    }

    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return null;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        UIViewRoot result = new UIViewRoot();
        result.setViewId(viewId);
        result.setRenderKitId(calculateRenderKitId(context));
        return result;
    }

    public void writeState(FacesContext context) {
    }

    public StateManager getStateManager() {
        if (null == stateManager) {
            stateManager = new StateManager() {
                protected Object getTreeStructureToSave(FacesContext context) {
                    return null;
                }

                protected Object getComponentStateToSave(FacesContext context) {
                    return null;
                }

                public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {
                    return null;
                }

                public SerializedView saveSerializedView(FacesContext context) {
                    return null;
                }

                public void writeState(FacesContext context,
                        SerializedView state) throws IOException {
                }

                protected UIViewRoot restoreTreeStructure(FacesContext context,
                        String viewId, String renderKitId) {
                    return null;
                }

                protected void restoreComponentState(FacesContext context, UIViewRoot root, String renderKitId) {
                }
            };
        }
        return stateManager;
    }

    public String getActionURL(FacesContext context, String viewId) {
        throw new UnsupportedOperationException();
    }

    public String getResourceURL(FacesContext context, String path) {
        if (path.startsWith("/")) {
            return context.getExternalContext().getRequestContextPath() + path;
        } else {
            return (path);
        }
    }

    public Locale calculateLocale(FacesContext context) {
        return Locale.getDefault();
    }

    public String calculateRenderKitId(FacesContext context) {
        return RenderKitFactory.HTML_BASIC_RENDER_KIT;
    }

}
