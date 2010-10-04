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

package com.sun.faces.facelets.tag.composite;

import com.sun.faces.facelets.tag.TagHandlerImpl;
import com.sun.faces.facelets.tag.jsf.ComponentSupport;
import com.sun.faces.util.FacesLogger;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.Location;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.application.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This <code>TagHandler</code> is responsible for relocating Facets
 * defined within a composite component to a component within the
 * composite component's <code>composite:implementation</code> section.
 */
public class InsertFacetHandler extends TagHandlerImpl {

    private final Logger LOGGER = FacesLogger.TAGLIB.getLogger();
    
    // Supported attribute names
    private static final String NAME_ATTRIBUTE = "name";

    private static final String REQUIRED_ATTRIBUTE = "required";

    // Attributes

    // This attribute is required.
    private TagAttribute name;

    // This attribute is not required.  If it's not defined or false,
    // then the facet associated with name need not be present in the
    // using page.
    private TagAttribute required;


    // ------------------------------------------------------------ Constructors


    public InsertFacetHandler(TagConfig config) {

        super(config);
        name = getRequiredAttribute(NAME_ATTRIBUTE);
        required = getAttribute(REQUIRED_ATTRIBUTE);

    }


    // ------------------------------------------------- Methods from TagHandler


    public void apply(FaceletContext ctx, UIComponent parent)
          throws IOException {

        UIComponent compositeParent =
              UIComponent.getCurrentCompositeComponent(ctx.getFacesContext());


        if (compositeParent != null) {
            compositeParent.subscribeToEvent(PostAddToViewEvent.class,
                                             new RelocateFacetListener(ctx,
                                                                       parent,
                                                                       this.tag.getLocation()));
        }

    }


    // ----------------------------------------------------------- Inner Classes


    private class RelocateFacetListener extends RelocateListener {


        private FaceletContext ctx;
        private UIComponent component;
        private Location location;


        // -------------------------------------------------------- Constructors


        RelocateFacetListener(FaceletContext ctx,
                              UIComponent component,
                              Location location) {

            this.ctx = ctx;
            this.component = component;
            this.location = location;

        }

        // --------------------------- Methods from ComponentSystemEventListener


        public void processEvent(ComponentSystemEvent event)
        throws AbortProcessingException {

            UIComponent compositeParent = event.getComponent();
            if (compositeParent == null) {
                return;
            }

            // ensure we're working with the expected composite component as
            // nesting levels may mask this.
            Resource resource = getBackingResource(compositeParent);
            while (compositeParent != null && !resourcesMatch(resource, location)) {
                compositeParent = UIComponent.getCompositeComponentParent(compositeParent);
                if (compositeParent != null) {
                    resource = getBackingResource(compositeParent);
                }
            }

            if (compositeParent == null) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                               "jsf.composite.component.insertfacet.missing.template",
                               location.toString());
                }
                return;
            }
            boolean req = isRequired();
            String facetName = name.getValue(ctx);
            if (compositeParent.getFacetCount() == 0 && req) {
                throwRequiredException(ctx, facetName, compositeParent);
            }

            Map<String, UIComponent> facets = compositeParent.getFacets();
            UIComponent facet = facets.remove(facetName);
            if (facet == null) {
                facet = compositeParent.getParent().getFacets().remove(facetName);
            }
            if (facet != null) {
                component.getFacets().put(facetName, facet);

                String key = (String)facet.getAttributes().get(ComponentSupport.MARK_CREATED);
               
                String value = component.getId();
                if (key != null && value != null) {
                    //store the new parent's info per child in the old parent's attr map
                    compositeParent.getAttributes().put(key, value);
                }

            } else {
                // In the case of full state saving, the compositeParent won't
                // have the facet to be relocated as its own - it will have already
                // been made a facet of the target component, so we need
                // to only throw the Exception if required, and the target component
                // doesn't have the facet defined
                if (req && component.getFacets().get(facetName) == null) {
                    throwRequiredException(ctx, facetName, compositeParent);
                }
            }
         
        }


        // ----------------------------------------------------- Private Methods


        private void throwRequiredException(FaceletContext ctx,
                                            String facetName,
                                            UIComponent compositeParent) {

            throw new TagException(tag,
                                   "Unable to find facet named '"
                                   + facetName
                                   + "' in parent composite component with id '"
                                   + compositeParent .getClientId(ctx.getFacesContext())
                                   + '\'');

        }


        private boolean isRequired() {

            return ((required != null) && required.getBoolean(ctx));

        }

    } // END RelocateFacetListener

}
