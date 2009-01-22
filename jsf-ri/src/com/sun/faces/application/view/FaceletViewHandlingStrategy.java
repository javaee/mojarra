/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.application.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.webapp.pdl.StateManagementStrategy;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.sun.faces.facelets.Facelet;
import com.sun.faces.facelets.FaceletFactory;
import com.sun.faces.facelets.compiler.Compiler;
import com.sun.faces.facelets.compiler.SAXCompiler;
import com.sun.faces.facelets.tag.ui.UIDebug;
import com.sun.faces.facelets.util.DevTools;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.facelets.el.VariableMapperWrapper;
import com.sun.faces.facelets.tag.composite.CompositeComponentBeanInfo;
import java.beans.BeanInfo;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.webapp.pdl.facelets.FaceletContext;

/**
 * This {@link ViewHandlingStrategy} handles Facelets/PDL-based views.
 */
public class FaceletViewHandlingStrategy extends ViewHandlingStrategy {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    // FaceletFactory singleton for this application
    private FaceletFactory faceletFactory;

    // Array of viewId extensions that should be handled by Facelets
    private String[] extensionsArray;

    // Array of viewId prefixes that should be handled by Facelets
    private String[] prefixesArray;
    
    public static final String IS_BUILDING_METADATA =
          FaceletViewHandlingStrategy.class.getName() + ".IS_BUILDING_METADATA";
    

    private StateManagementStrategy stateManagementStrategy;
    
    // ------------------------------------------------------------ Constructors


    public FaceletViewHandlingStrategy() {

        initialize();

    }

    // ------------------------------------ Methods from PageDeclarationLanguage

     @Override
     public StateManagementStrategy getStateManagementStrategy(FacesContext context, String viewId) {
         return stateManagementStrategy;
     }
    
    @Override
    public BeanInfo getComponentMetadata(FacesContext context, 
            Resource compositeComponentResource) {
        // PENDING this implementation is terribly wasteful.
        // Must find a better way.
        CompositeComponentBeanInfo result;
        FaceletContext ctx = (FaceletContext)
                context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        FaceletFactory factory = (FaceletFactory)
              RequestStateManager.get(context, RequestStateManager.FACELET_FACTORY);
        VariableMapper orig = ctx.getVariableMapper();
        UIComponent tmp = context.getApplication().createComponent("javax.faces.NamingContainer");
        UIPanel facetComponent = (UIPanel)
                context.getApplication().createComponent("javax.faces.Panel");
        facetComponent.setRendererType("javax.faces.Group");
        tmp.getFacets().put(UIComponent.COMPOSITE_FACET_NAME, facetComponent);
        // We have to put the resource in here just so the classes that eventually
        // get called by facelets have access to it.
        tmp.getAttributes().put(Resource.COMPONENT_RESOURCE_KEY, 
                compositeComponentResource);
        
        Facelet f;

        try {
            f = factory.getFacelet(compositeComponentResource.getURL());
            VariableMapper wrapper = new VariableMapperWrapper(orig) {

                @Override
                public ValueExpression resolveVariable(String variable) {
                    return super.resolveVariable(variable);
                }
                
            };
            ctx.setVariableMapper(wrapper);
            context.getAttributes().put(IS_BUILDING_METADATA, Boolean.TRUE);
            f.apply(context, facetComponent);
        } catch (Exception e) {
            if (e instanceof FacesException) {
                throw (FacesException) e;
            } else {
                throw new FacesException(e);
            }
        }
        finally {
            context.getAttributes().remove(IS_BUILDING_METADATA);
            ctx.setVariableMapper(orig);
        }
        result = (CompositeComponentBeanInfo) 
                tmp.getAttributes().get(UIComponent.BEANINFO_KEY);
        
        return result;
    }


    /**
     * @see javax.faces.webapp.pdl.PageDeclarationLanguage#getScriptComponentResource(javax.faces.context.FacesContext, javax.faces.application.Resource)
     */
    public Resource getScriptComponentResource(FacesContext context,
            Resource componentResource) {
        Resource result = null;
        
        String resourceName = componentResource.getResourceName();
        if (resourceName.endsWith(".xhtml")) {
            resourceName = resourceName.substring(0, 
                    resourceName.length() - 6) + ".groovy";
            ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
            result = resourceHandler.createResource(resourceName, 
                    componentResource.getLibraryName());
        }
        
        return result;
    }


    /**
     * @see javax.faces.webapp.pdl.PageDeclarationLanguage#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     */
    public void renderView(FacesContext ctx,
                           UIViewRoot viewToRender)
    throws IOException {

        // suppress rendering if "rendered" property on the component is
        // false
        if (!viewToRender.isRendered()) {
            return;
        }

        // log request
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Rendering View: " + viewToRender.getViewId());
        }

        WriteBehindStateWriter stateWriter = null;
        try {
            // Only build the view if this view has not yet been built.
            if (!isViewPopulated(ctx, viewToRender)) {
                this.buildView(ctx, viewToRender);
            }

            // setup writer and assign it to the ctx
            ResponseWriter origWriter = ctx.getResponseWriter();
            if (origWriter == null) {
                origWriter = createResponseWriter(ctx);
            }

            stateWriter = new WriteBehindStateWriter(origWriter,
                                                     ctx,
                                                     responseBufferSize);

            ResponseWriter writer = origWriter.cloneWithWriter(stateWriter);
            ctx.setResponseWriter(writer);

            // render the view to the response
            writer.startDocument();
            viewToRender.encodeAll(ctx);
            writer.endDocument();

            // finish writing
            writer.close();


            boolean writtenState = stateWriter.stateWritten();
            // flush to origWriter
            if (writtenState) {
                stateWriter.flushToWriter();
            }

        } catch (FileNotFoundException fnfe) {
            this.handleFaceletNotFound(ctx,
                                       multiViewHandler,
                                       viewToRender.getViewId(),
                                       fnfe.getMessage());
        } catch (Exception e) {
            this.handleRenderException(ctx, e);
        } finally {
            if (stateWriter != null)
                stateWriter.release();
        }

    }


    /**
     * <p>
     * If {@link UIDebug#debugRequest(javax.faces.context.FacesContext)}} is <code>true</code>,
     * simply return a new UIViewRoot(), otherwise, call the default logic.
     * </p>
     * @see {@link javax.faces.webapp.pdl.PageDeclarationLanguage#restoreView(javax.faces.context.FacesContext, String)}
     */
    @Override
    public UIViewRoot restoreView(FacesContext ctx,
                                  String viewId) {

        if (UIDebug.debugRequest(ctx)) {
            ctx.getApplication().createComponent(UIViewRoot.COMPONENT_TYPE);
        }

        return super.restoreView(ctx, viewId);

    }


    /**
     * @see javax.faces.webapp.pdl.PageDeclarationLanguage#createView(javax.faces.context.FacesContext, String)
     * @return
     */
    @Override
    public UIViewRoot createView(FacesContext ctx,
                                 String viewId) {

        if (UIDebug.debugRequest(ctx)) {
            ctx.getApplication().createComponent(UIViewRoot.COMPONENT_TYPE);
        }
        UIViewRoot root = super.createView(ctx, viewId);
        ctx.setViewRoot(root);
        root.getClientId(ctx);

        if (root != null) {
            try {
                buildView(ctx, root);
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }
        }

        return root;

    }
    

    // --------------------------------------- Methods from ViewHandlingStrategy


    /**
     * @param viewId the view ID to check
     * @return <code>true</code> if assuming a default configuration and the
     *  view ID's extension is <code>.xhtml</code>  Otherwise try to match
     *  the view ID based on the configured extendsion and prefixes.
     *
     * @see com.sun.faces.config.WebConfiguration.WebContextInitParameter#FaceletsViewMappings
     */
    @Override
    public boolean handlesViewId(String viewId) {
         if (viewId != null) {
            // If there's no extensions array or prefixes array, then
            // assume defaults.  .xhtml extension is handled by
            // the FaceletViewHandler and .jsp will be handled by
            // the JSP view handler
            if ((extensionsArray == null) && (prefixesArray == null)) {
                return (viewId.endsWith(ViewHandler.DEFAULT_FACELETS_SUFFIX));
            }

            if (extensionsArray != null) {
                for (int i = 0; i < extensionsArray.length; i++) {
                    String extension = extensionsArray[i];
                    if (viewId.endsWith(extension)) {
                        return true;
                    }
                }
            }

            if (prefixesArray != null) {
                for (int i = 0; i < prefixesArray.length; i++) {
                    String prefix = prefixesArray[i];
                    if (viewId.startsWith(prefix)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    // ------------------------------------------------------- Protected Methods


    /**
     * @param ctx the {@link FacesContext} for the current request
     * @param viewToRender the {@link UIViewRoot} to check
     * @return <code>true</code> if the {@link FacesContext} attributes map
     *  contains a reference to the {@link UIViewRoot}'s view ID
     */
    protected static boolean isViewPopulated(FacesContext ctx, UIViewRoot viewToRender) {

        return ctx.getAttributes().containsKey(viewToRender.getViewId());

    }


    /**
     * <p>
     * Flag the specified {@link UIViewRoot} as populated.
     * </p>
     * @param ctx the {@link FacesContext} for the current request
     * @param viewToRender the {@link UIViewRoot} to mark as populated
     */
    protected static void setViewPopulated(FacesContext ctx,
                                           UIViewRoot viewToRender) {

        ctx.getAttributes().put(viewToRender.getViewId(), Boolean.TRUE);

    }


    /**
     * Initialize the core Facelets runtime.
     */
    protected void initialize() {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Initializing FaceletViewHandlingStrategy");
        }

        this.initializeMappings();
        this.stateManagementStrategy = new StateManagementStrategyImpl();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Initialization Successful");
        }

    }


    /**
     * Initialize mappings, during the first request.
     */
    protected void initializeMappings() {

        String viewMappings = webConfig
              .getOptionValue(WebContextInitParameter.FaceletsViewMappings);
        if ((viewMappings != null) && (viewMappings.length() > 0)) {
            String[] mappingsArray = Util.split(viewMappings, ";");

            List<String> extensionsList = new ArrayList<String>(mappingsArray.length);
            List<String> prefixesList = new ArrayList<String>(mappingsArray.length);

            for (int i = 0; i < mappingsArray.length; i++) {
                String mapping = mappingsArray[i].trim();
                int mappingLength = mapping.length();
                if (mappingLength <= 1) {
                    continue;
                }

                if (mapping.charAt(0) == '*') {
                    extensionsList.add(mapping.substring(1));
                } else if (mapping.charAt(mappingLength - 1) == '*') {
                    prefixesList.add(mapping.substring(0, mappingLength - 1));
                }
            }

            extensionsArray = new String[extensionsList.size()];
            extensionsList.toArray(extensionsArray);

            prefixesArray = new String[prefixesList.size()];
            prefixesList.toArray(prefixesArray);
        }
    }

    /**
     * @return a new Compiler for Facelet processing.
     */
    protected Compiler createCompiler() {
        return new SAXCompiler();
    }


    /**
     * Build the view.
     * @param ctx the {@link FacesContext} for the current request
     * @param viewToRender the {@link UIViewRoot} to populate based
     *  of the Facelet template
     * @throws IOException if an error occurs building the view.
     */
    protected void buildView(FacesContext ctx, UIViewRoot viewToRender)
    throws IOException {

        viewToRender.setViewId(viewToRender.getViewId());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Building View: " + viewToRender.getViewId());
        }
        if (faceletFactory == null) {
            ApplicationAssociate associate = ApplicationAssociate.getInstance(ctx.getExternalContext());
            faceletFactory = associate.getFaceletFactory();
            assert (faceletFactory != null);
        }
        RequestStateManager.set(ctx,
                                RequestStateManager.FACELET_FACTORY,
                                faceletFactory);
        Facelet f = faceletFactory.getFacelet(viewToRender.getViewId());

        // populate UIViewRoot
        f.apply(ctx, viewToRender);
        setViewPopulated(ctx, viewToRender);

    }


    /**
     * @param context the {@link FacesContext} for the current request
     * @return a {@link ResponseWriter} for processing the request
     * @throws IOException if the writer cannot be created
     */
    protected ResponseWriter createResponseWriter(FacesContext context)
    throws IOException {

        ExternalContext extContext = context.getExternalContext();
        RenderKit renderKit = context.getRenderKit();
        // Avoid a cryptic NullPointerException when the renderkit ID
        // is incorrectly set
        if (renderKit == null) {
            String id = context.getViewRoot().getRenderKitId();
            throw new IllegalStateException(
                  "No render kit was available for id \"" + id + "\"");
        }

        ServletResponse response = (ServletResponse) extContext.getResponse();

        if (responseBufferSizeSet) {
            // set the buffer for content
            response.setBufferSize(responseBufferSize);
        }


        // get our content type
        String contentType =
              (String) extContext.getRequestMap().get("facelets.ContentType");

        // get the encoding
        String encoding =
              (String) extContext.getRequestMap().get("facelets.Encoding");

        // Create a dummy ResponseWriter with a bogus writer,
        // so we can figure out what content type the ReponseWriter
        // is really going to ask for
        ResponseWriter writer = renderKit.createResponseWriter(NullWriter.Instance,
                                                               contentType,
                                                               encoding);

        contentType = getResponseContentType(context, writer.getContentType());
        encoding = getResponseEncoding(context, writer.getCharacterEncoding());

        // apply them to the response
        response.setContentType(contentType);
        response.setCharacterEncoding(encoding);

        // Now, clone with the real writer
        writer = writer.cloneWithWriter(response.getWriter());

        return writer;

    }


    /**
     * Handles the case where rendering throws an Exception.
     *
     * @param context the {@link FacesContext} for the current request
     * @param e the caught Exception
     * @throws IOException if the custom debug content cannot be written
     */
    protected void handleRenderException(FacesContext context, Exception e)
    throws IOException {

        Object resp = context.getExternalContext().getResponse();

        // always log
        if (LOGGER.isLoggable(Level.SEVERE)) {
            UIViewRoot root = context.getViewRoot();
            StringBuffer sb = new StringBuffer(64);
            sb.append("Error Rendering View");
            if (root != null) {
                sb.append('[');
                sb.append(root.getViewId());
                sb.append(']');
            }
            LOGGER.log(Level.SEVERE, sb.toString(), e);
        }

        // handle dev response
        if (associate.isDevModeEnabled() && !context.getResponseComplete()
            && resp instanceof HttpServletResponse) {
            HttpServletResponse httpResp = (HttpServletResponse) resp;
            if (!httpResp.isCommitted()) {
                httpResp.reset();
                httpResp.setContentType("text/html; charset=UTF-8");
                Writer w = httpResp.getWriter();
                DevTools.debugHtml(w, context, e);
                w.flush();
                context.responseComplete();
            }
        } else if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else if (e instanceof IOException) {
            throw (IOException) e;
        } else {
            throw new FacesException(e.getMessage(), e);
        }

    }


    /**
     * Handles the case where a Facelet cannot be found.
     *
     * @param context the {@link FacesContext} for the current request
     * @param vh the {@link ViewHandler} handling this view
     * @param viewId the view ID that was to be mapped to a Facelet
     * @param message optional message to include in the 404
     * @throws IOException if an error occurs sending the 404 to the client
     */
    protected void handleFaceletNotFound(FacesContext context,
                                         ViewHandler vh,
                                         String viewId,
                                         String message)
    throws IOException {

        String actualId = vh.getActionURL(context, viewId);
        Object respObj = context.getExternalContext().getResponse();
        if (respObj instanceof HttpServletResponse) {
            HttpServletResponse respHttp = (HttpServletResponse) respObj;
            respHttp.sendError(HttpServletResponse.SC_NOT_FOUND, ((message != null)
                                                                  ? (actualId + ": " + message)
                                                                  : actualId));
            context.responseComplete();
        }

    }


    /**
     * @param context the {@link FacesContext} for the current request
     * @param orig the original encoding
     * @return the encoding to be used for this response
     */
    protected String getResponseEncoding(FacesContext context, String orig) {

        String encoding = orig;

        // see if we need to override the encoding
        Map<Object,Object> ctxAttributes = context.getAttributes();
        Map<String,Object> sessionMap =
              context.getExternalContext().getSessionMap();

        // 1. check the request attribute
        if (ctxAttributes.containsKey("facelets.Encoding")) {
            encoding = (String) ctxAttributes.get("facelets.Encoding");
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST,
                           "Facelet specified alternate encoding {0}",
                           encoding);
            }
            sessionMap.put(ViewHandler.CHARACTER_ENCODING_KEY, encoding);
        }

        // 2. get it from request
        Object request = context.getExternalContext().getRequest();
        if (encoding == null && request instanceof ServletRequest) {
            encoding = ((ServletRequest) request).getCharacterEncoding();
        }

        // 3. get it from the session
        if (encoding == null) {
            encoding = (String) sessionMap.get(ViewHandler.CHARACTER_ENCODING_KEY);
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST,
                           "Session specified alternate encoding {0}",
                           encoding);
            }
        }

        // 4. default it
        if (encoding == null) {
            encoding = "UTF-8";
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("ResponseWriter created had a null CharacterEncoding, defaulting to UTF-8");
            }
        }

        return encoding;

    }


    /**
     * @param context the {@link FacesContext} for the current request
     * @param orig the original contentType
     * @return the content type to be used for this response
     */
    protected String getResponseContentType(FacesContext context, String orig) {

        String contentType = orig;

        // see if we need to override the contentType
        Map<Object,Object> m = context.getAttributes();
        if (m.containsKey("facelets.ContentType")) {
            contentType = (String) m.get("facelets.ContentType");
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("Facelet specified alternate contentType '"
                        + contentType + "'");
            }
        }

        // safety check
        if (contentType == null) {
            contentType = "text/html";
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("ResponseWriter created had a null ContentType, defaulting to text/html");
            }
        }

        return contentType;

    }


    // ---------------------------------------------------------- Nested Classes


    /**
     * Simple no-op writer.
     */
    protected static final class NullWriter extends Writer {

        static final NullWriter Instance = new NullWriter();

        public void write(char[] buffer) {
        }

        public void write(char[] buffer, int off, int len) {
        }

        public void write(String str) {
        }

        public void write(int c) {
        }

        public void write(String str, int off, int len) {
        }

        public void close() {
        }

        public void flush() {
        }

    } // END NullWriter

}
