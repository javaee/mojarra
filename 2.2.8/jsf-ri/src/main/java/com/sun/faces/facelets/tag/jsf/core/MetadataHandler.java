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

package com.sun.faces.facelets.tag.jsf.core;

import com.sun.faces.facelets.tag.TagHandlerImpl;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * This is a specialized <code>FacetHandler</code> to enable
 * <code>f:metadata</code> support.
 *
 * </p>
 */
public class MetadataHandler extends TagHandlerImpl {

    private static final Logger LOGGER = FacesLogger.TAGLIB.getLogger();


    // ------------------------------------------------------------ Constructors


    public MetadataHandler(TagConfig config) {
        super(config);
    }


    // ------------------------------------------------- Methods from TagHandler

    public void apply(FaceletContext ctx, UIComponent parent)
          throws IOException {

        Util.notNull("parent", parent);
        UIViewRoot root;
        if (parent instanceof UIViewRoot) {
            root = (UIViewRoot) parent;
        } else {
            root = ctx.getFacesContext().getViewRoot();
        }
        if (root == null) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "jsf.metadata.uiviewroot.unavailable");
            }
            return;
        }

        UIComponent facetComponent = null;
        if (root.getFacetCount() > 0) {
              facetComponent = root.getFacets().get(UIViewRoot.METADATA_FACET_NAME);
        }
        if (facetComponent == null) {
            root.getAttributes().put(FacetHandler.KEY,
                                       UIViewRoot.METADATA_FACET_NAME);
            try {
                this.nextHandler.apply(ctx, root);
            } finally {
                root.getAttributes().remove(FacetHandler.KEY);
            }
            facetComponent = root.getFacets().get(UIViewRoot.METADATA_FACET_NAME);
            if (facetComponent != null && !(facetComponent instanceof UIPanel)) {
                Application app = ctx.getFacesContext().getApplication();
                UIComponent panelGroup = app.createComponent(UIPanel.COMPONENT_TYPE);
                panelGroup.getChildren().add(facetComponent);
                root.getFacets().put(UIViewRoot.METADATA_FACET_NAME, panelGroup);
                facetComponent = panelGroup;
            }
            if (null != facetComponent) {
                facetComponent.setId(UIViewRoot.METADATA_FACET_NAME);
            }
        }
    }
}
