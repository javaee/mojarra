/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.facelets.tag.jsf.html;

import com.sun.faces.facelets.tag.jsf.ComponentSupport;
import com.sun.faces.facelets.tag.jsf.ComponentTagHandlerDelegateImpl;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributes;
import java.util.List;

import static com.sun.faces.facelets.tag.jsf.ComponentSupport.MARK_CREATED;

/**
 * This class overrides key methods from <code>ComponentTagHandlerDelegateImpl</code>
 * in order to properly find existing component resources as well as properly handling
 * the case when this concrete implementations of this class are applied more than
 * once for a particular request.
 */
public abstract class ComponentResourceDelegate extends ComponentTagHandlerDelegateImpl {

    private TagAttributes attributes;

    // ------------------------------------------------------------ Constructors


    public ComponentResourceDelegate(ComponentHandler owner) {

        super(owner);
        this.attributes = owner.getTag().getAttributes();

    }


    // ----------------------------------------- Methods from TagHandlerDelegate


    @Override
    protected UIComponent findChild(FaceletContext ctx,
                                    UIComponent parent,
                                    String tagId) {

        // If we have a target for this particular component, we need to
        // query the UIViewRoot's component resources, otherwise defer
        // to our super class.
        String target = getLocationTarget(ctx);
        if (target != null) {
            final UIViewRoot root = ctx.getFacesContext().getViewRoot();
            List<UIComponent> resources =
                  root.getComponentResources(ctx.getFacesContext(), target);
            for (UIComponent c : resources) {
                String cid = (String) c.getAttributes().get(MARK_CREATED);
                if (tagId.equals(cid)) {
                    return c;
                }
            }
            return null;
        } else {
            return super.findChild(ctx, parent, tagId);
        }

    }


    @Override protected void addComponentToView(FaceletContext ctx,
                                                UIComponent parent,
                                                UIComponent c,
                                                boolean componentFound) {

        if (!componentFound) {
            // default to the existing logic which will add the component
            // in-place.  An event will be fired to move the component
            // as a UIViewRoot component resource
            super.addComponentToView(ctx, parent, c, componentFound);
        } else {
            // when re-applying we supress events for existing components,
            // so if we simply relied on the default logic, the resources
            // wouldn't be be moved.  We'll do it manually instead.
            String target = getLocationTarget(ctx);
            if (target != null) {
                final UIViewRoot root = ctx.getFacesContext().getViewRoot();
                root.addComponentResource(ctx.getFacesContext(), c, target);
            } else {
                super.addComponentToView(ctx, parent, c, componentFound);
            }
        }

    }




    protected void doOrphanedChildCleanup(FaceletContext ctx,
                                          UIComponent parent,
                                          UIComponent c) {

        FacesContext context = ctx.getFacesContext();
        boolean suppressEvents =
              ComponentSupport.suppressViewModificationEvents(context);
        if (suppressEvents) {
            // if the component has already been found, it will be removed
            // and added back to the view.  We don't want to publish events
            // for this case.
            context.setProcessingEvents(false);
        }

        ComponentSupport.finalizeForDeletion(c);
        UIViewRoot root = context.getViewRoot();
        root.removeComponentResource(context, c, getLocationTarget(ctx));

        if (suppressEvents) {
            // restore the original state
            context.setProcessingEvents(true);
        }

    }


    // ------------------------------------------------------- Protected Methods


    protected abstract String getLocationTarget(FaceletContext ctx);


    protected TagAttribute getAttribute(String name) {

        return attributes.get(name);

    }

}
