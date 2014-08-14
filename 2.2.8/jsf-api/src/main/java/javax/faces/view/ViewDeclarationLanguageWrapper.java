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

package javax.faces.view;


import java.beans.BeanInfo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.faces.FacesWrapper;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_2">Provides a simple implementation of 
 * {@link ViewDeclarationLanguage} that can
 * be subclassed by developers wishing to provide specialized behavior
 * to an existing {@link ViewDeclarationLanguage} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link ViewDeclarationLanguage} instance.</p>
 *
 * <p class="changed_added_2_2">Usage: extend this class and override 
 * {@link #getWrapped} to
 * return the instance being wrapping.</p>
 *
 * @since 2.2
 */
public abstract class ViewDeclarationLanguageWrapper extends ViewDeclarationLanguage implements FacesWrapper<ViewDeclarationLanguage> {


    // ----------------------------------------------- Methods from FacesWrapper


    /**
     * @return the wrapped {@link ViewDeclarationLanguage} instance
     * @see javax.faces.FacesWrapper#getWrapped()
     */
    @Override
    public abstract ViewDeclarationLanguage getWrapped();

    // ----------------------------------------------- Methods from ViewDeclarationLanguage


    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void retargetAttachedObjects(FacesContext context, UIComponent topLevelComponent, List<AttachedObjectHandler> handlers) {
        getWrapped().retargetAttachedObjects(context, topLevelComponent, handlers);
    }

    @Override
    public void retargetMethodExpressions(FacesContext context, UIComponent topLevelComponent) {
        getWrapped().retargetMethodExpressions(context, topLevelComponent);
    }

    @Override
    public boolean viewExists(FacesContext context, String viewId) {
        return getWrapped().viewExists(context, viewId);
    }

    @Override
    public void buildView(FacesContext context, UIViewRoot root) throws IOException {
        getWrapped().buildView(context, root);
    }

    @Override
    public List<String> calculateResourceLibraryContracts(FacesContext context, String viewId) {
        return getWrapped().calculateResourceLibraryContracts(context, viewId);
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        return getWrapped().createView(context, viewId);
    }

    @Override
    public UIComponent createComponent(FacesContext context, String taglibURI, String tagName, Map<String, Object> attributes) {
        return getWrapped().createComponent(context, taglibURI, tagName, attributes);
    }

    @Override
    public BeanInfo getComponentMetadata(FacesContext context, Resource componentResource) {
        return getWrapped().getComponentMetadata(context, componentResource);
    }

    @Override
    public Resource getScriptComponentResource(FacesContext context, Resource componentResource) {
        return getWrapped().getScriptComponentResource(context, componentResource);
    }

    @Override
    public StateManagementStrategy getStateManagementStrategy(FacesContext context, String viewId) {
        return getWrapped().getStateManagementStrategy(context, viewId);
    }

    @Override
    public ViewMetadata getViewMetadata(FacesContext context, String viewId) {
        return getWrapped().getViewMetadata(context, viewId);
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot view) throws IOException {
        getWrapped().renderView(context, view);
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return getWrapped().restoreView(context, viewId);
    }




    
}
