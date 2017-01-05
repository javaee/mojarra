/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package javax.faces.application;

import java.io.IOException;
import java.util.stream.Stream;

import javax.faces.FacesWrapper;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0"><span class="changed_modified_2_0_rev_a
 * changed_modified_2_2">Provides</span> a simple implementation of
 * {@link ResourceHandler} that can be subclassed by developers wishing
 * to provide specialized behavior to an existing {@link
 * ResourceHandler} instance.  The default implementation of all methods
 * is to call through to the wrapped {@link ResourceHandler}.</p>
 *
 * <p class="changed_modified_2_3">Usage: extend this class and push the implementation being wrapped to the
 * constructor and use {@link #getWrapped} to access the instance being wrapped.</p>
 *
 * @since 2.0
 */
public abstract class ResourceHandlerWrapper extends ResourceHandler implements FacesWrapper<ResourceHandler> {

    private ResourceHandler wrapped;

    /**
     * @deprecated Use the other constructor taking the implementation being wrapped.
     */
    @Deprecated
    public ResourceHandlerWrapper() {

    }

    /**
     * <p class="changed_added_2_3">If this resource handler has been decorated, 
     * the implementation doing the decorating should push the implementation being wrapped to this constructor.
     * The {@link #getWrapped()} will then return the implementation being wrapped.</p>
     * 
     * @param wrapped The implementation being wrapped.
     * @since 2.3
     */
    public ResourceHandlerWrapper(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }
    
    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }


    // -------------------------------------------- Methods from ResourceHandler


    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link ResourceHandler#createResource(String)} on the
     * wrapped {@link ResourceHandler} object.</p>
     */
    @Override
    public Resource createResource(String resourceName) {
        return getWrapped().createResource(resourceName);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link ResourceHandler#createResourceFromId(String)} on the
     * wrapped {@link ResourceHandler} object.</p>
     *
     * @since 2.2
     */
    @Override
    public Resource createResourceFromId(String resourceId) {
        return getWrapped().createResourceFromId(resourceId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link ResourceHandler#createResource(String, String)} on the wrapped
     * {@link ResourceHandler} object.</p>
     */
    @Override
    public Resource createResource(String resourceName, String libraryName) {
        return getWrapped().createResource(resourceName, libraryName);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link ResourceHandler#createViewResource} on the wrapped
     * {@link ResourceHandler} object.</p>
     */
    @Override
    public ViewResource createViewResource(FacesContext context, String resourceName) {
        return getWrapped().createViewResource(context, resourceName);
    }
    
    /**
     * <p class="changed_added_2_3">The default behavior of this method
     * is to call {@link ResourceHandler#getViewResources(FacesContext, String, int, ResourceVisitOption...)} on the wrapped
     * {@link ResourceHandler} object.</p>
     * 
     * @since 2.3
     */
    @Override
    public Stream<String> getViewResources(FacesContext facesContext, String path, int maxDepth, ResourceVisitOption... options) {
        return getWrapped().getViewResources(facesContext, path, maxDepth, options);
    }
    
    /**
     * <p class="changed_added_2_3">The default behavior of this method
     * is to call {@link ResourceHandler#getViewResources(FacesContext, String, ResourceVisitOption...)} on the wrapped
     * {@link ResourceHandler} object.</p>
     * 
     * @since 2.3
     */
    public Stream<String> getViewResources(FacesContext facesContext, String path, ResourceVisitOption... options) {
        return getWrapped().getViewResources(facesContext, path, options);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link ResourceHandler#createResource(String, String,
     * String)} on the wrapped {@link ResourceHandler} object.</p>
     */
    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        return getWrapped().createResource(resourceName, libraryName, contentType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link
     * ResourceHandler#handleResourceRequest(javax.faces.context.FacesContext)}
     * on the wrapped {@link ResourceHandler} object.</p>
     */
    @Override
    public void handleResourceRequest(FacesContext context) throws IOException {
        getWrapped().handleResourceRequest(context);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link ResourceHandler#isResourceRequest(javax.faces.context.FacesContext)} on the
     * wrapped {@link ResourceHandler} object.</p>
     */
    @Override
    public boolean isResourceRequest(FacesContext context) {
        return getWrapped().isResourceRequest(context);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link ResourceHandler#isResourceURL} on the
     * wrapped {@link ResourceHandler} object.</p>
     */
    @Override
    public boolean isResourceURL(String url) {
        return getWrapped().isResourceURL(url);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link ResourceHandler#libraryExists(String)} on the
     * wrapped {@link ResourceHandler} object.</p>
     */
    @Override
    public boolean libraryExists(String libraryName) {
        return getWrapped().libraryExists(libraryName);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link ResourceHandler#getRendererTypeForResourceName(String)} on the
     * wrapped {@link ResourceHandler} object.</p>
     */
    @Override
    public String getRendererTypeForResourceName(String resourceName) {
        return getWrapped().getRendererTypeForResourceName(resourceName);
    }

    /**
     * <p class="changed_added_2_3">The default behavior of this method
     * is to call {@link ResourceHandler#markResourceRendered(FacesContext, String, String)} on the
     * wrapped {@link ResourceHandler} object.</p>
     * 
     * @since 2.3
     */
    @Override
    public void markResourceRendered(FacesContext context, String resourceName, String libraryName) {
        getWrapped().markResourceRendered(context, resourceName, libraryName);
    }

    /**
     * <p class="changed_added_2_3">The default behavior of this method
     * is to call {@link ResourceHandler#isResourceRendered(FacesContext, String, String)} on the
     * wrapped {@link ResourceHandler} object.</p>
     * 
     * @since 2.3
     */
    @Override
    public boolean isResourceRendered(FacesContext context, String resourceName, String libraryName) {
        return getWrapped().isResourceRendered(context, resourceName, libraryName);
    }

}
