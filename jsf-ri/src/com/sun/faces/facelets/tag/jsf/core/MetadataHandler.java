/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.facelets.tag.jsf.core;

import java.io.IOException;

import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIPanel;
import javax.faces.FacesException;
import javax.el.ELException;

import com.sun.faces.util.Util;
import com.sun.faces.facelets.tag.TagHandlerImpl;

/**
 * <p>
 * This is a specialized <code>FacetHandler</code> to enable
 * <code>f:metadata</code> support.
 *
 * </p>
 */
public class MetadataHandler extends TagHandlerImpl {

    /**
     * <p>The name of the attribute whose presence hints to the next handler that
     * the facet contains multiple children. The handler then knows to
     * implicitly wrap the children in a UIPanel.</p>
     */
    public static final String FACET_IS_COMPOSITE = "facelets.FACET_IS_COMPOSITE";

    // ------------------------------------------------------------ Constructors


    public MetadataHandler(TagConfig config) {
        super(config);
    }


    // ------------------------------------------------- Methods from TagHandler

    public void apply(FaceletContext ctx, UIComponent parent)
          throws IOException, FacesException, FaceletException, ELException {

        Util.notNull("parent", parent);
        UIComponent facetComponent =
              parent.getFacets().get(UIViewRoot.METADATA_FACET_NAME);
        if (facetComponent == null) {
            parent.getAttributes().put(FacetHandler.KEY,
                                       UIViewRoot.METADATA_FACET_NAME);
            try {
                this.nextHandler.apply(ctx, parent);
            } finally {
                parent.getAttributes().remove(FacetHandler.KEY);
            }
            facetComponent = parent.getFacets().get(UIViewRoot.METADATA_FACET_NAME);
            if (!(facetComponent instanceof UIPanel)) {
                UIComponent panelGroup = ctx.getFacesContext().getApplication()
                      .createComponent(UIPanel.COMPONENT_TYPE);
                panelGroup.getAttributes().put(UIComponent.ADDED_BY_PDL_KEY, Boolean.TRUE);
                panelGroup.getChildren().add(facetComponent);
                parent.getFacets()
                      .put(UIViewRoot.METADATA_FACET_NAME, panelGroup);
                facetComponent = panelGroup;

                facetComponent.setId(UIViewRoot.METADATA_FACET_NAME);

            }
        }
    }
}
