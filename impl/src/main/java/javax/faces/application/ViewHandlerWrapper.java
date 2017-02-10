/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;

/**
 * <p class="changed_modified_2_2"><span class="changed_modified_2_3">Provides</span>
 * a simple implementation of {@link ViewHandler} that can
 * be subclassed by developers wishing to provide specialized behavior
 * to an existing {@link ViewHandler} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link ViewHandler}.</p>
 *
 * <p class="changed_added_2_3">Usage: extend this class and push the implementation being wrapped to the
 * constructor and use {@link #getWrapped} to access the instance being wrapped.</p>
 *
 * @since 1.2
 */
public abstract class ViewHandlerWrapper extends ViewHandler implements FacesWrapper<ViewHandler> {


    private ViewHandler wrapped;

    /**
     * @deprecated Use the other constructor taking the implementation being wrapped.
     */
    @Deprecated
    public ViewHandlerWrapper() {

    }

    /**
     * <p class="changed_added_2_3">If this view handler has been decorated,
     * the implementation doing the decorating should push the implementation being wrapped to this constructor.
     * The {@link #getWrapped()} will then return the implementation being wrapped.</p>
     *
     * @param wrapped The implementation being wrapped.
     * @since 2.3
     */
    public ViewHandlerWrapper(ViewHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ViewHandler getWrapped() {
        return wrapped;
    }


    // ------------------------ Methods from javax.faces.application.ViewHandler

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#initView}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#initView
     * @since 1.2
     */
    @Override
    public void initView(FacesContext context) throws FacesException {
        getWrapped().initView(context);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#restoreView(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)
     * @since 1.2
     */
    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return getWrapped().restoreView(context, viewId);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#createView(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#createView(javax.faces.context.FacesContext, String)
     * @since 1.2
     */
    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        return getWrapped().createView(context, viewId);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     * @since 1.2
     */
    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
        getWrapped().renderView(context, viewToRender);
    }


    /**
     *
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#calculateCharacterEncoding(javax.faces.context.FacesContext)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#calculateCharacterEncoding(javax.faces.context.FacesContext)
     * @since 1.2
     */
    @Override
    public String calculateCharacterEncoding(FacesContext context) {
        return getWrapped().calculateCharacterEncoding(context);
    }

    /**
     *
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#calculateLocale(javax.faces.context.FacesContext)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#calculateLocale(javax.faces.context.FacesContext)
     * @since 1.2
     */
    @Override
    public Locale calculateLocale(FacesContext context) {
        return getWrapped().calculateLocale(context);
    }

    /**
     *
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#deriveViewId(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#deriveViewId(javax.faces.context.FacesContext, String)
     * @since 2.0
     */
    @Override
    public String deriveViewId(FacesContext context, String requestViewId) {
        return getWrapped().deriveViewId(context, requestViewId);
    }

    /**
     *
     * <p class="changed_added_2_1">The default behavior of this method is to
     * call {@link ViewHandler#deriveLogicalViewId(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#deriveLogicalViewId(javax.faces.context.FacesContext, String)
     * @since 2.1
     */
    @Override
    public String deriveLogicalViewId(FacesContext context, String requestViewId) {
        return getWrapped().deriveLogicalViewId(context, requestViewId);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#calculateRenderKitId(javax.faces.context.FacesContext)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#calculateRenderKitId(javax.faces.context.FacesContext)
     * @since 1.2
     */
    @Override
    public String calculateRenderKitId(FacesContext context) {
        return getWrapped().calculateRenderKitId(context);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getActionURL(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#getActionURL(javax.faces.context.FacesContext, String)
     * @since 1.2
     */
    @Override
    public String getActionURL(FacesContext context, String viewId) {
        return getWrapped().getActionURL(context, viewId);
    }

    /**
     *
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getProtectedViewsUnmodifiable}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#getProtectedViewsUnmodifiable
     * @since 2.2
     */
    @Override
    public Set<String> getProtectedViewsUnmodifiable() {
        return getWrapped().getProtectedViewsUnmodifiable();
    }

    /**
     *
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#addProtectedView}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#addProtectedView
     * @since 2.2
     */
    @Override
    public void addProtectedView(String urlPattern) {
        getWrapped().addProtectedView(urlPattern);
    }

    /**
     *
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#removeProtectedView}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#removeProtectedView
     * @since 2.2
     */
    @Override
    public boolean removeProtectedView(String urlPattern) {
        return getWrapped().removeProtectedView(urlPattern);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getRedirectURL(javax.faces.context.FacesContext, String, java.util.Map, boolean)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#getRedirectURL(javax.faces.context.FacesContext, String, java.util.Map, boolean)
     * @since 2.0
     */
    @Override
    public String getRedirectURL(FacesContext context, String viewId, Map<String,List<String>> parameters,  boolean includeViewParams) {
        return getWrapped().getRedirectURL(context, viewId, parameters, includeViewParams);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getBookmarkableURL(javax.faces.context.FacesContext, String, java.util.Map, boolean)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#getBookmarkableURL(javax.faces.context.FacesContext, String, java.util.Map, boolean)
     * @since 2.0
     */
    @Override
    public String getBookmarkableURL(FacesContext context, String viewId, Map<String,List<String>> parameters, boolean includeViewParams) {
        return getWrapped().getBookmarkableURL(context, viewId, parameters, includeViewParams);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getResourceURL(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#getResourceURL(javax.faces.context.FacesContext, String)
     * @since 1.2
     */
    @Override
    public String getResourceURL(FacesContext context, String path) {
        return getWrapped().getResourceURL(context, path);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getWebsocketURL(FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#getWebsocketURL(FacesContext, String)
     * @since 2.3
     */
    @Override
    public String getWebsocketURL(FacesContext context, String channel) {
        return getWrapped().getWebsocketURL(context, channel);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getViewDeclarationLanguage}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @since 2.0
     */
    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context, String viewId) {
        return getWrapped().getViewDeclarationLanguage(context, viewId);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getViews(FacesContext, String, ViewVisitOption...)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @since 2.3
     */
    @Override
    public Stream<String> getViews(FacesContext context, String path, ViewVisitOption... options) {
        return getWrapped().getViews(context, path, options);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getViews(FacesContext, String, int, ViewVisitOption...)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @since 2.3
     */
    @Override
    public Stream<String> getViews(FacesContext context, String path, int maxDepth, ViewVisitOption... options) {
        return getWrapped().getViews(context, path, maxDepth, options);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#writeState(javax.faces.context.FacesContext)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#writeState(javax.faces.context.FacesContext)
     * @since 1.2
     */
    @Override
    public void writeState(FacesContext context) throws IOException {
        getWrapped().writeState(context);
    }

}
