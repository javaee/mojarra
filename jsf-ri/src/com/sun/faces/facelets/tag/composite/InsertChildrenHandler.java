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


package com.sun.faces.facelets.tag.composite;

import com.sun.faces.facelets.tag.TagHandlerImpl;
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
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This <code>TagHandler</code> is responsible for relocating children
 * defined within a composite component to a component within the
 * composite component's <code>composite:implementation</code> section.
 */
public class InsertChildrenHandler extends TagHandlerImpl {

    private final Logger LOGGER = FacesLogger.TAGLIB.getLogger();
    private static final String REQUIRED_ATTRIBUTE = "required";

    // This attribute is not required.  If not defined, then assume the facet
    // isn't necessary.
    private TagAttribute required;

    
    // ------------------------------------------------------------ Constructors


    public InsertChildrenHandler(TagConfig config) {

        super(config);
        required = getAttribute(REQUIRED_ATTRIBUTE);

    }


    // ------------------------------------------------- Methods from TagHandler


    public void apply(FaceletContext ctx, UIComponent parent)
          throws IOException {

        UIComponent compositeParent =
              UIComponent.getCurrentCompositeComponent(ctx.getFacesContext());
        if (compositeParent != null) {
            int count = parent.getChildCount();
            compositeParent.subscribeToEvent(PostAddToViewEvent.class,
                                             new RelocateChildrenListener(ctx,
                                                                          parent,
                                                                          count,
                                                                          this.tag.getLocation()));
        }

    }



    // ----------------------------------------------------------- Inner Classes


    private class RelocateChildrenListener extends RelocateListener {


        private FaceletContext ctx;
        private UIComponent component;
        private int idx;
        private Location location;


        // -------------------------------------------------------- Constructors


        RelocateChildrenListener(FaceletContext ctx,
                                 UIComponent component,
                                 int idx,
                                 Location location) {

            this.ctx = ctx;
            this.component = component;
            this.idx = idx;
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
                               "jsf.composite.component.insertchildren.missing.template",
                               location.toString());
                }
                return;
            }

            if (compositeParent.getChildCount() == 0 && isRequired()) {
                throwRequiredException(ctx, compositeParent);
            }

            List<UIComponent> compositeChildren = compositeParent.getChildren();
            List<UIComponent> parentChildren = component.getChildren();
            parentChildren.addAll(idx, compositeChildren);
            
        }


        // ----------------------------------------------------- Private Methods


        private void throwRequiredException(FaceletContext ctx,
                                        UIComponent compositeParent) {

            throw new TagException(tag,
                                   "Unable to find any children components "
                                     + "nested within parent composite component with id '"
                                     + compositeParent .getClientId(ctx.getFacesContext())
                                     + '\'');

        }


        private boolean isRequired() {

            return ((required != null) && required.getBoolean(ctx));

        }


    } // END RelocateChildrenListener

}
