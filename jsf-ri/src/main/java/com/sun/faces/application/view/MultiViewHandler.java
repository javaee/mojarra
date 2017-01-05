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

package com.sun.faces.application.view;

import static com.sun.faces.RIConstants.FACELETS_ENCODING_KEY;
import static com.sun.faces.RIConstants.SAVESTATE_FIELD_MARKER;
import static com.sun.faces.renderkit.RenderKitUtils.PredefinedPostbackParameter.RENDER_KIT_ID_PARAM;
import static com.sun.faces.util.MessageUtils.ILLEGAL_VIEW_ID_ID;
import static com.sun.faces.util.MessageUtils.getExceptionMessageString;
import static com.sun.faces.util.Util.getFacesMapping;
import static com.sun.faces.util.Util.isPrefixMapped;
import static com.sun.faces.util.Util.notNull;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static javax.faces.FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.push.PushContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;
import javax.faces.view.ViewMetadata;
import javax.servlet.http.HttpServletResponse;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

/**
 * This {@link ViewHandler} implementation handles both JSP-based and
 * Facelets/PDL-based views.
 */
public class MultiViewHandler extends ViewHandler {

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private String[] configuredExtensions;
    private Set<String> protectedViews;
    private boolean extensionsSet; // For legacy JSF 1.2 support
    
    private ViewDeclarationLanguageFactory vdlFactory;


    // ------------------------------------------------------------ Constructors


    public MultiViewHandler() {
        WebConfiguration config = WebConfiguration.getInstance();
              
        configuredExtensions = config.getConfiguredExtensions();
        extensionsSet = config.isSet(WebConfiguration.WebContextInitParameter.DefaultSuffix);
        vdlFactory = (ViewDeclarationLanguageFactory) FactoryFinder.getFactory(VIEW_DECLARATION_LANGUAGE_FACTORY);
        protectedViews = new CopyOnWriteArraySet<>();
    }


    // ------------------------------------------------ Methods from ViewHandler


    /**
     * Call the default implementation of {@link javax.faces.application.ViewHandler#initView(javax.faces.context.FacesContext)}
     *
     * @see javax.faces.application.ViewHandler#initView(javax.faces.context.FacesContext)
     */
    @Override
    public void initView(FacesContext context) throws FacesException {
        super.initView(context);
    }

    /**
     * <p>
     * Call {@link ViewDeclarationLanguage#restoreView(javax.faces.context.FacesContext, String)}.
     * </p>
     *
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)   
     */
    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {

        notNull("context", context);
        
        String physicalViewId = derivePhysicalViewId(context, viewId, false);
        
        return vdlFactory.getViewDeclarationLanguage(physicalViewId)
                         .restoreView(context, physicalViewId);
    }
    
    /**
     * <p>
     * Derive the physical view ID (i.e. the physical resource) and call
     * call {@link ViewDeclarationLanguage#createView(javax.faces.context.FacesContext, String)}.
     * </p>
     *
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)
     */
    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {

        notNull("context", context);
        
        String physicalViewId = derivePhysicalViewId(context, viewId, false);
        
        return vdlFactory.getViewDeclarationLanguage(physicalViewId)
                         .createView(context, physicalViewId);
    }
    
    /**
     * <p>
     * Call {@link ViewDeclarationLanguage#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)}
     * if the view can be rendered.
     * </p>
     *
     * @see ViewHandler#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     */
    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {

        notNull("context", context);
        notNull("viewToRender", viewToRender);

        vdlFactory.getViewDeclarationLanguage(viewToRender.getViewId())
                  .renderView(context, viewToRender);
    }

    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#calculateLocale(javax.faces.context.FacesContext)
     */
    @Override
    public Locale calculateLocale(FacesContext context) {

        notNull("context", context);

        Locale result = null;
        // determine the locales that are acceptable to the client based on the
        // Accept-Language header and the find the best match among the
        // supported locales specified by the client.
        Iterator<Locale> locales = context.getExternalContext().getRequestLocales();
        while (locales.hasNext()) {
            Locale perf = locales.next();
            result = findMatch(context, perf);
            if (result != null) {
                break;
            }
        }
        
        // no match is found.
        if (result == null) {
            if (context.getApplication().getDefaultLocale() == null) {
                result = Locale.getDefault();
            } else {
                result = context.getApplication().getDefaultLocale();
            }
        }
        
        return result;
    }

    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#calculateRenderKitId(javax.faces.context.FacesContext)
     */
    @Override
    public String calculateRenderKitId(FacesContext context) {

        notNull("context", context);

        String result = RENDER_KIT_ID_PARAM.getValue(context);

        if (result == null) {
            if (null ==
                (result = context.getApplication().getDefaultRenderKitId())) {
                result = RenderKitFactory.HTML_BASIC_RENDER_KIT;
            }
        }
        
        return result;
    }


    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#writeState(javax.faces.context.FacesContext)
     */
    @Override
    public void writeState(FacesContext context) throws IOException {

        notNull("context", context);
        
        if (!context.getPartialViewContext().isAjaxRequest()) {
            if (LOGGER.isLoggable(FINE)) {
                LOGGER.fine(
                    "Begin writing marker for viewId " +
                    context.getViewRoot().getViewId());
            }

            WriteBehindStateWriter writer = WriteBehindStateWriter.getCurrentInstance();
            if (writer != null) {
                writer.writingState();
            }
            
            context.getResponseWriter().write(SAVESTATE_FIELD_MARKER);
            
            if (LOGGER.isLoggable(FINE)) {
                LOGGER.fine(
                    "End writing marker for viewId " +
                    context.getViewRoot().getViewId());
            }
        }
    }

    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#getActionURL(javax.faces.context.FacesContext, String)
     */
    @Override
    public String getActionURL(FacesContext context, String viewId) {
        String result = getActionURLWithoutViewProtection(context, viewId);
        // http://java.net/jira/browse/JAVASERVERFACES-2204
        // PENDING: this code is optimized to be fast to write.
        // It must be optimized to be fast to run.
        
        // See git clone ssh://edburns@git.java.net/grizzly~git 1_9_36 for
        // how grizzly does this.
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        Set<String> urlPatterns = viewHandler.getProtectedViewsUnmodifiable();
        
        // Implement section 12.1 of the Servlet spec.
        boolean viewIdIsProtected = false;
        for (String urlPattern : urlPatterns) {
            if (urlPattern.equals(viewId)) {
                viewIdIsProtected = true;
            }
            if (viewIdIsProtected) {
                break;
            }
        }
        
        if (viewIdIsProtected) {
            StringBuilder builder = new StringBuilder(result);
            // If the result already has a query string...
            if (result.contains("?")) {
                // ...assume it also has one or more parameters, and 
                // append an additional parameter.
                builder.append("&");
            } else {
                // Otherwise, this is the first parameter in the result.
                builder.append("?");
            }
            
            String rkId = viewHandler.calculateRenderKitId(context);
            ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context, rkId);
            String tokenValue = rsm.getCryptographicallyStrongTokenFromSession(context);
            builder.append(ResponseStateManager.NON_POSTBACK_VIEW_TOKEN_PARAM)
                   . append("=").append(tokenValue);
            result = builder.toString();

        }
        
        return result;
    }

    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#getResourceURL(javax.faces.context.FacesContext, String)
     */
    @Override
    public String getResourceURL(FacesContext context, String path) {

        if (path.charAt(0) == '/') {
            return context.getExternalContext().getRequestContextPath() + path;
        }
            
        return path;
    }

    @Override
    public String getWebsocketURL(FacesContext context, String channel) {
        requireNonNull(context, "context");
        requireNonNull(channel, "channel");

        ExternalContext externalContext = context.getExternalContext();
        String contextPath = externalContext.getRequestContextPath();
        return externalContext.encodeWebsocketURL(contextPath + PushContext.URI_PREFIX + "/" + channel);
    }

    @Override
    public String getBookmarkableURL(FacesContext context, String viewId, Map<String,List<String>> parameters, boolean includeViewParams) {
        Map<String,List<String>> params;
        if (includeViewParams) {
            params = getFullParameterList(context, viewId, parameters);
        } else {
            params = parameters;
        }
        
        ExternalContext ectx = context.getExternalContext();
        return ectx.encodeActionURL(ectx.encodeBookmarkableURL(Util.getViewHandler(context).getActionURL(context, viewId), params));
    }

    @Override
    public void addProtectedView(String urlPattern) {
        protectedViews.add(urlPattern);
    }

    @Override
    public Set<String> getProtectedViewsUnmodifiable() {
        return unmodifiableSet(protectedViews);
    }

    @Override
    public boolean removeProtectedView(String urlPattern) {
        return protectedViews.remove(urlPattern);
    }

    /**
     * @see ViewHandler#getRedirectURL(javax.faces.context.FacesContext, String, java.util.Map, boolean)
     */
    @Override
    public String getRedirectURL(FacesContext context, String viewId, Map<String, List<String>> parameters, boolean includeViewParams) {

        String encodingFromContext = (String) context.getAttributes().get(FACELETS_ENCODING_KEY);
        if (encodingFromContext == null) {
            encodingFromContext = (String) context.getViewRoot().getAttributes().get(FACELETS_ENCODING_KEY);
        }
        
        String responseEncoding;
        
        if (encodingFromContext == null) {
            try {
                responseEncoding = context.getExternalContext().getResponseCharacterEncoding();
            } catch (Exception e) {
                if (LOGGER.isLoggable(FINE)) {
                    String message = "Unable to obtain response character encoding from ExternalContext {0}.  Using UTF-8.";
                    message = MessageFormat.format(message, context.getExternalContext());
                    LOGGER.log(FINE, message, e);
                }
                responseEncoding = "UTF-8";
            }
        } else {
            responseEncoding = encodingFromContext;
        }

        if (parameters != null) {
            Map<String, List<String>> decodedParameters = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
                String string = entry.getKey();
                List<String> list = entry.getValue();
                List<String> values = new ArrayList<>();
                for (Iterator<String> it = list.iterator(); it.hasNext();) {
                    String value = it.next();
                    try {
                        value = URLDecoder.decode(value, responseEncoding);
                    } catch(UnsupportedEncodingException e) {
                        throw new RuntimeException("Unable to decode");
                    }
                    values.add(value);
                }
                decodedParameters.put(string, values);
            }
            parameters = decodedParameters;
        }
                
        Map<String, List<String>> params;
        if (includeViewParams) {
            params = getFullParameterList(context, viewId, parameters);
        } else {
            params = parameters;
        }
        
        ExternalContext ectx = context.getExternalContext();
        return ectx.encodeActionURL(ectx.encodeRedirectURL(Util.getViewHandler(context).getActionURL(context, viewId), params));
    }

    /**
     * @see ViewHandler#getViewDeclarationLanguage(javax.faces.context.FacesContext, String)
     */
    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context, String viewId) {
        return vdlFactory.getViewDeclarationLanguage(viewId);
    }

    @Override
    public String deriveViewId(FacesContext context, String requestViewId) {
        return derivePhysicalViewId(context, requestViewId, true);
    }

    @Override
    public String deriveLogicalViewId(FacesContext context, String requestViewId) {
        return derivePhysicalViewId(context, requestViewId, false);
    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>If the specified mapping is a prefix mapping, and the provided
     * request URI (usually the value from <code>ExternalContext.getRequestServletPath()</code>)
     * starts with <code>mapping + '/'</code>, prune the mapping from the
     * URI and return it, otherwise, return the original URI.
     * 
     * @param uri the servlet request path
     * @param mapping the FacesServlet mapping used for this request
     * @return the URI without additional FacesServlet mappings
     * 
     * @since 1.2
     */
    protected String normalizeRequestURI(String uri, String mapping) {

        if (mapping == null || !isPrefixMapped(mapping)) {
            return uri;
        }
        
        int length = mapping.length() + 1;
        StringBuilder builder = new StringBuilder(length);
        builder.append(mapping).append('/');
        String mappingMod = builder.toString();
        boolean logged = false;
        
        while (uri.startsWith(mappingMod)) {
            if (!logged && LOGGER.isLoggable(WARNING)) {
                logged = true;
                LOGGER.log(WARNING, "jsf.viewhandler.requestpath.recursion", new Object[] {uri, mapping});
            }
            uri = uri.substring(length - 1);
        }
        
        return uri;
    }

    /**
     * <p>Adjust the viewID per the requirements of {@link #renderView}.</p>
     *
     * @param context current {@link javax.faces.context.FacesContext}
     * @param viewId  incoming view ID
     * @return the view ID with an altered suffix mapping (if necessary)
     */
    protected String convertViewId(FacesContext context, String viewId) {

        // if the viewId doesn't already use the above suffix,
        // replace or append.
        int extIdx = viewId.lastIndexOf('.');
        int length = viewId.length();
        StringBuilder buffer = new StringBuilder(length);

        for (String ext : configuredExtensions) {
            if (viewId.endsWith(ext)) {
                return viewId;
            }

            appendOrReplaceExtension(viewId, ext, length, extIdx, buffer);

            String convertedViewId = buffer.toString();

            ViewDeclarationLanguage vdl = getViewDeclarationLanguage(context, convertedViewId);
            
            if (vdl.viewExists(context, convertedViewId)) {
                // RELEASE_PENDING (rlubke,driscoll) cache the lookup
                return convertedViewId;
            }
        }

        // unable to find any resource match that the default ViewHandler
        // can deal with.  Fall back to legacy (JSF 1.2) id conversion.
        return legacyConvertViewId(viewId, length, extIdx, buffer);
    }

    protected String derivePhysicalViewId(FacesContext ctx, String requestViewId, boolean checkPhysical) {
        
        if (requestViewId != null) {
            String mapping = getFacesMapping(ctx);
            if (mapping != null) {
                String physicalViewId;
                if (!isPrefixMapped(mapping)) {
                    
                    // Suffix mapping
                    
                    physicalViewId = convertViewId(ctx, requestViewId);
                } else {
                    
                    // Prefix mapping
                    
                    physicalViewId = normalizeRequestURI(requestViewId, mapping);
                    if (physicalViewId.equals(mapping)) {
                        // The request was to the FacesServlet only - no path info
                        // on some containers this causes a recursion in the
                        // RequestDispatcher and the request appears to hang.
                        // If this is detected, return status 404
                        send404Error(ctx);
                    }
                }

                if (checkPhysical) {
                    ViewDeclarationLanguage vdl = getViewDeclarationLanguage(ctx, physicalViewId);
                                                                                
                    return vdl.viewExists(ctx, physicalViewId) ? physicalViewId : null;
                } 
                
                return physicalViewId;
            }
        }
        
        return requestViewId;
    }

    protected Map<String,List<String>> getFullParameterList(FacesContext ctx,
                                                            String viewId,
                                                            Map<String,List<String>> existingParameters) {

        Map<String,List<String>> copy;
        if (existingParameters == null || existingParameters.isEmpty()) {
            copy = new LinkedHashMap<>(4);
        } else {
          copy = new LinkedHashMap<>(existingParameters);
        }
        addViewParameters(ctx, viewId, copy);
        
        return copy;
    }

    protected void addViewParameters(FacesContext ctx, String viewId, Map<String,List<String>> existingParameters) {

        UIViewRoot currentRoot = ctx.getViewRoot();
        String currentViewId = currentRoot.getViewId();
        Collection<UIViewParameter> toViewParams = Collections.emptyList();
        Collection<UIViewParameter> currentViewParams;
        boolean currentIsSameAsNew = false;
        currentViewParams = ViewMetadata.getViewParameters(currentRoot);

        if (currentViewId.equals(viewId)) {
            currentIsSameAsNew = true;
            toViewParams = currentViewParams;
        }
        else {
            ViewDeclarationLanguage pdl = getViewDeclarationLanguage(ctx, viewId);
            ViewMetadata viewMetadata = pdl.getViewMetadata(ctx, viewId);
            if (null != viewMetadata) {
                UIViewRoot root = viewMetadata.createMetadataView(ctx);
                toViewParams = ViewMetadata.getViewParameters(root);
            }
        }

        if (toViewParams.isEmpty()) {
            return;
        }

        for (UIViewParameter viewParam : toViewParams) {
            String value = null;
            // don't bother looking at view parameter if it's been overridden
            if (existingParameters.containsKey(viewParam.getName())) {
                continue;
            }
            
            if (paramHasValueExpression(viewParam)) {
                value = viewParam.getStringValueFromModel(ctx);
            }

            if (value == null) {
                if (currentIsSameAsNew) {
                    /*
                     * Anonymous view parameter: get string value from UIViewParameter instance stored in current view.
                     */
                    value = viewParam.getStringValue(ctx);
                }
                else {
                    /*
                     * Or transfer string value from matching UIViewParameter instance stored in current view.
                     */
                    value = getStringValueToTransfer(ctx, viewParam, currentViewParams);
                }
            }
            
            if (value != null) {
                List<String> existing = existingParameters.get(viewParam.getName());
                if (existing == null) {
                    existing = new ArrayList<>(4);
                    existingParameters.put(viewParam.getName(), existing);
                }
                existing.add(value);
            }
        }
    }

    /**
     * Attempts to find a matching locale based on <code>pref</code> and
     * list of supported locales, using the matching algorithm
     * as described in JSTL 8.3.2.
     * @param context the <code>FacesContext</code> for the current request
     * @param pref the preferred locale
     * @return the Locale based on pref and the matching alogritm specified
     *  in JSTL 8.3.2
     */
    protected Locale findMatch(FacesContext context, Locale pref) {

        Locale result = null;
        Iterator<Locale> it = context.getApplication().getSupportedLocales();
        while (it.hasNext()) {
            Locale supportedLocale = it.next();

            if (pref.equals(supportedLocale)) {
                // exact match
                result = supportedLocale;
                break;
            } else {
                // Make sure the preferred locale doesn't have country
                // set, when doing a language match, For ex., if the
                // preferred locale is "en-US", if one of supported
                // locales is "en-UK", even though its language matches
                // that of the preferred locale, we must ignore it.
                if (pref.getLanguage().equals(supportedLocale.getLanguage()) &&
                     supportedLocale.getCountry().length() == 0) {
                    result = supportedLocale;
                }
            }
        }
        
        // if it's not in the supported locales,
        if (result == null) {
            Locale defaultLocale = context.getApplication().getDefaultLocale();
            if (defaultLocale != null) {
                if ( pref.equals(defaultLocale)) {
                    // exact match
                    result = defaultLocale;
                } else {
                    // Make sure the preferred locale doesn't have country
                    // set, when doing a language match, For ex., if the
                    // preferred locale is "en-US", if one of supported
                    // locales is "en-UK", even though its language matches
                    // that of the preferred locale, we must ignore it.
                    if (pref.getLanguage().equals(defaultLocale.getLanguage()) &&
                         defaultLocale.getCountry().length() == 0) {
                        result = defaultLocale;
                    }
                }
            }
        }

        return result;
    }

    /**
     * <p>
     * Send {@link HttpServletResponse#SC_NOT_FOUND} (404) to the client.
     * </p>
     *
     * @param context the {@link FacesContext} for the current request
     */
    protected void send404Error(FacesContext context) {

        try {
            context.responseComplete();
            context.getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "");
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }
    }


    // --------------------------------------------------------- Private Methods
    
    private String getActionURLWithoutViewProtection(FacesContext context, String viewId) {

        notNull("context", context);
        notNull("viewId", viewId);

        if (viewId.length() == 0 || viewId.charAt(0) != '/') {
            String message = getExceptionMessageString(ILLEGAL_VIEW_ID_ID, viewId);
            if (LOGGER.isLoggable(SEVERE)) {
                LOGGER.log(SEVERE, "jsf.illegal_view_id_error", viewId);
            }
            throw new IllegalArgumentException(message);
        }

        // Acquire the context path, which we will prefix on all results
        ExternalContext extContext = context.getExternalContext();
        String contextPath = extContext.getRequestContextPath();

        // Acquire the mapping used to execute this request (if any)
        String mapping = getFacesMapping(context);

        // If no mapping can be identified, just return a server-relative path
        if (mapping == null) {
            return contextPath + viewId;
        }

        // Deal with prefix mapping
        if (isPrefixMapped(mapping)) {
            if (mapping.equals("/*")) {
                return contextPath + viewId;
            } else {
                return contextPath + mapping + viewId;
            }
        }

        // Deal with extension mapping
        int period = viewId.lastIndexOf('.');
        if (period < 0) {
            return contextPath + viewId + mapping;
        } else if (!viewId.endsWith(mapping)) {

            for (String extension : configuredExtensions) {
                if (viewId.endsWith(extension)) {
                    return contextPath + viewId.substring(0, viewId.indexOf(extension)) + mapping;
                }
            }

            return contextPath + viewId.substring(0, period) + mapping;
         
        } else {
            return contextPath + viewId;
        }
    }

    private static boolean paramHasValueExpression(UIViewParameter param) {
        return param.getValueExpression("value") != null;
    }

    private static String getStringValueToTransfer(FacesContext context, UIViewParameter param, Collection<UIViewParameter> viewParams) {

        if (viewParams != null && !viewParams.isEmpty()) {
            for (UIViewParameter candidate : viewParams) {
                if (candidate.getName() != null && param.getName() != null && candidate.getName().equals(param.getName())) {
                    return candidate.getStringValue(context);
                }
            }
        }

        return param.getStringValue(context);
    }

    // Utility method used by viewId conversion.  Appends the extension
    // if no extension is present.  Otherwise, replaces the extension.
    private void appendOrReplaceExtension(String viewId, String extension, int length, int extensionIndex, StringBuilder buffer) {

        buffer.setLength(0);
        buffer.append(viewId);

        if (extensionIndex != -1) {
            buffer.replace(extensionIndex, length, extension);
        } else {
            // no extension in the provided viewId, append the suffix
            buffer.append(extension);
        }
    }

    private String legacyConvertViewId(String viewId, int length, int extensionIndex, StringBuilder buffer) {

        // In 1.2, the viewId was converted by replacing the extension
        // with the single extension specified by javax.faces.DEFAULT_SUFFIX,
        // which defaulted to ".jsp".  In 2.0, javax.faces.DEFAULT_SUFFIX
        // may specify multiple extensions.  If javax.faces.DEFAULT_SUFFIX is
        // explicitly set, we honor it and pick off the first specified
        // extension.  If javax.faces.DEFAULT_SUFFIX is not explicitly set,
        // we honor the default 1.2 behavior and use ".jsp" as the suffix.

        String extension = (extensionsSet && 
                       !(configuredExtensions.length == 0)) ?
                          configuredExtensions[0] : ".jsp";

        if (viewId.endsWith(extension)) {
            return viewId;
        }

        appendOrReplaceExtension(viewId, extension, length, extensionIndex, buffer);

        return buffer.toString();
    }    
}
