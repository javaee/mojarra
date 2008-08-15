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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.faces.facelets.compiler.Compiler;
import com.sun.faces.facelets.compiler.SAXCompiler;
import com.sun.faces.facelets.compiler.TagLibraryConfig;
import com.sun.faces.facelets.impl.DefaultFaceletFactory;
import com.sun.faces.facelets.impl.DefaultResourceResolver;
import com.sun.faces.facelets.impl.PageDeclarationLanguageImpl;
import com.sun.faces.facelets.impl.ResourceResolver;
import com.sun.faces.facelets.tag.TagDecorator;
import com.sun.faces.facelets.tag.TagLibrary;
import com.sun.faces.facelets.tag.jsf.ComponentSupport;
import com.sun.faces.facelets.tag.ui.UIDebug;
import com.sun.faces.facelets.util.DevTools;
import com.sun.faces.facelets.util.FacesAPI;
import com.sun.faces.facelets.util.ReflectionUtil;
import javax.faces.webapp.pdl.PageDeclarationLanguage;

/**
 * ViewHandler implementation for Facelets
 *
 * @author Jacob Hookom
 * @version $Id: FaceletViewHandler.java,v 1.49.2.6 2006/03/20 07:22:00 jhook
 *          Exp $
 */
public class FaceletViewHandler extends ViewHandler {

    protected final static Logger log = Logger
            .getLogger("facelets.viewhandler");

    public final static long DEFAULT_REFRESH_PERIOD = 2;

    public final static String PARAM_REFRESH_PERIOD = "facelets.REFRESH_PERIOD";

    /**
     * Spelling error, We'll remove this in a future release.
     * @deprecated
     */
    public final static String PARAM_REFRESH_PERIO = PARAM_REFRESH_PERIOD;

    public final static String PARAM_SKIP_COMMENTS = "facelets.SKIP_COMMENTS";

    /**
     * Context initialization parameter for defining what viewIds should be
     * handled by Facelets, and what should not. When left unset, all URLs will
     * be handled by Facelets. When set, it must be a semicolon separated list
     * of either extension mappings or prefix mappings. For example:
     *
     * <pre>
     *
     *
     *
     *        &lt;context-param&gt;
     *          &lt;param-name&gt;facelets.VIEW_MAPPINGS&lt;/param-name&gt;
     *          &lt;param-value&gt;/demos/*; *.xhtml&lt;/param-value&gt;
     *        &lt;/context-param&gt;
     *
     *
     *
     * </pre>
     *
     * would use Facelets for processing all viewIds in the "/demos" directory
     * or that end in .xhtml, and use the standard JSP engine for all other
     * viewIds.
     * <p>
     * <strong>NOTE</strong>: when using this parameter, you need to use
     * prefix-mapping for the <code>FacesServlet</code> (that is,
     * <code>/faces/*</code>, not <code>*.jsf</code>).
     * </p>
     */
    public final static String PARAM_VIEW_MAPPINGS = "facelets.VIEW_MAPPINGS";

    public final static String PARAM_LIBRARIES = "facelets.LIBRARIES";

    public final static String PARAM_DECORATORS = "facelets.DECORATORS";

    public final static String PARAM_DEVELOPMENT = "facelets.DEVELOPMENT";

    public final static String PARAM_RESOURCE_RESOLVER = "facelets.RESOURCE_RESOLVER";

    public final static String PARAM_BUILD_BEFORE_RESTORE = "facelets.BUILD_BEFORE_RESTORE";

    public final static String PARAM_BUFFER_SIZE = "facelets.BUFFER_SIZE";

    private final static String STATE_KEY = "~facelets.VIEW_STATE~";

    private final static int STATE_KEY_LEN = STATE_KEY.length();

    private final static Object STATE_NULL = new Object();

    private final ViewHandler parent;

    private boolean developmentMode = false;

    private boolean buildBeforeRestore = false;

    private int bufferSize;

    private String defaultSuffix;

    private FaceletFactory faceletFactory;

    // Array of viewId extensions that should be handled by Facelets
    private String[] extensionsArray;

    // Array of viewId prefixes that should be handled by Facelets
    private String[] prefixesArray;

    private PageDeclarationLanguage pdl = null;
    /**
     *
     */
    public FaceletViewHandler(ViewHandler parent) {
        this.parent = parent;
        this.pdl = new PageDeclarationLanguageImpl();
        FacesContext.getCurrentInstance().getApplication().setPageDeclarationLanguage(pdl);
    }
    
    public FaceletFactory getFaceletFactory() {
        return this.faceletFactory;
    }

    /**
     * Initialize the ViewHandler during its first request.
     */
    protected void initialize(FacesContext context) {
        synchronized (this) {
            if (this.faceletFactory == null) {
                log.fine("Initializing");
                Compiler c = this.createCompiler();
                this.initializeCompiler(c);
                this.faceletFactory = this.createFaceletFactory(c);

                this.initializeMappings(context);
                this.initializeMode(context);
                this.initializeBuffer(context);

                log.fine("Initialization Successful");
            }
        }
    }

    private void initializeMode(FacesContext context) {
        ExternalContext external = context.getExternalContext();
        String param = external.getInitParameter(PARAM_DEVELOPMENT);
        this.developmentMode = "true".equals(param);

        String restoreMode = external
                .getInitParameter(PARAM_BUILD_BEFORE_RESTORE);
        this.buildBeforeRestore = "true".equals(restoreMode);
    }

    private void initializeBuffer(FacesContext context) {
        ExternalContext external = context.getExternalContext();
        String param = external.getInitParameter(PARAM_BUFFER_SIZE);
        this.bufferSize = (param != null && !"".equals(param)) ? Integer
                .parseInt(param) : -1;
    }

    /**
     * Initialize mappings, during the first request.
     */
    private void initializeMappings(FacesContext context) {
        ExternalContext external = context.getExternalContext();
        String viewMappings = external.getInitParameter(PARAM_VIEW_MAPPINGS);
        if ((viewMappings != null) && (viewMappings.length() > 0)) {
            String[] mappingsArray = viewMappings.split(";");

            List extensionsList = new ArrayList(mappingsArray.length);
            List prefixesList = new ArrayList(mappingsArray.length);

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

    protected FaceletFactory createFaceletFactory(Compiler c) {

        // refresh period
        long refreshPeriod = DEFAULT_REFRESH_PERIOD;
        FacesContext ctx = FacesContext.getCurrentInstance();
        String userPeriod = ctx.getExternalContext().getInitParameter(
                PARAM_REFRESH_PERIOD);
        if (userPeriod != null && userPeriod.length() > 0) {
            refreshPeriod = Long.parseLong(userPeriod);
        }

        // resource resolver
        ResourceResolver resolver = new DefaultResourceResolver();
        String resolverName = ctx.getExternalContext().getInitParameter(
                PARAM_RESOURCE_RESOLVER);
        if (resolverName != null && resolverName.length() > 0) {
            try {
                resolver = (ResourceResolver) ReflectionUtil.forName(resolverName)
                        .newInstance();
            } catch (Exception e) {
                throw new FacesException("Error Initializing ResourceResolver["
                        + resolverName + "]", e);
            }
        }

        // Resource.getResourceUrl(ctx,"/")
        return new DefaultFaceletFactory(c, resolver, refreshPeriod);
    }

    protected Compiler createCompiler() {
        return new SAXCompiler();
    }

    protected void initializeCompiler(Compiler c) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext ext = ctx.getExternalContext();

        // load libraries
        String libParam = ext.getInitParameter(PARAM_LIBRARIES);
        if (libParam != null) {
            libParam = libParam.trim();
            String[] libs = libParam.split(";");
            URL src;
            TagLibrary libObj;
            for (int i = 0; i < libs.length; i++) {
                try {
                    src = ext.getResource(libs[i].trim());
                    if (src == null) {
                        throw new FileNotFoundException(libs[i]);
                    }
                    libObj = TagLibraryConfig.create(src);
                    c.addTagLibrary(libObj);
                    log.fine("Successfully Loaded Library: " + libs[i]);
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Error Loading Library: " + libs[i],
                            e);
                }
            }
        }

        // load decorators
        String decParam = ext.getInitParameter(PARAM_DECORATORS);
        if (decParam != null) {
            decParam = decParam.trim();
            String[] decs = decParam.split(";");
            TagDecorator decObj;
            for (int i = 0; i < decs.length; i++) {
                try {
                    decObj = (TagDecorator) ReflectionUtil.forName(decs[i])
                            .newInstance();
                    c.addTagDecorator(decObj);
                    log.fine("Successfully Loaded Decorator: " + decs[i]);
                } catch (Exception e) {
                    log.log(Level.SEVERE,
                            "Error Loading Decorator: " + decs[i], e);
                }
            }
        }

        // skip params?
        String skipParam = ext.getInitParameter(PARAM_SKIP_COMMENTS);
        if (skipParam != null && "true".equals(skipParam)) {
            c.setTrimmingComments(true);
        }
    }

    public UIViewRoot restoreView(FacesContext context, String viewId) {
        if (UIDebug.debugRequest(context)) {
            return new UIViewRoot();
        }

        if (!handledByFacelets(viewId)) {
            return this.parent.restoreView(context, viewId);
        }

        if (this.faceletFactory == null) {
            this.initialize(context);
        }

        // In JSF 1.2, restoreView() will only be called on postback.
        // But in JSF 1.1, it will be called for an initial request too,
        // in which case we must return null in order to fall through
        // to createView()
        if (FacesAPI.getVersion() < 12) {
            if (!isPostback11(context)) {
                return null;
            }
        }

        ViewHandler outerViewHandler = context.getApplication()
                .getViewHandler();
        String renderKitId = outerViewHandler.calculateRenderKitId(context);

        UIViewRoot viewRoot = this.parent.restoreView(context, viewId);
        context.setViewRoot(viewRoot);
        return viewRoot;
    }

    protected ViewHandler getWrapped() {
        return this.parent;
    }

    protected ResponseWriter createResponseWriter(FacesContext context)
            throws IOException, FacesException {
        ExternalContext extContext = context.getExternalContext();
        RenderKit renderKit = context.getRenderKit();
	ResponseWriter writer = null;
        // Avoid a cryptic NullPointerException when the renderkit ID
        // is incorrectly set
        if (renderKit == null) {
            String id = context.getViewRoot().getRenderKitId();
            throw new IllegalStateException(
                    "No render kit was available for id \"" + id + "\"");
        }

        ServletResponse response = (ServletResponse) extContext.getResponse();

        // set the buffer for content
        if (this.bufferSize != -1) {
            response.setBufferSize(this.bufferSize);
        }

        // get our content type
        String contentType = (String)extContext.getRequestMap().get("facelets.ContentType");

        // get the encoding
        String encoding = (String) extContext.getRequestMap().get("facelets.Encoding");

        // Create a dummy ResponseWriter with a bogus writer,
        // so we can figure out what content type the ReponseWriter
        // is really going to ask for
        try {
	        writer = renderKit.createResponseWriter(
	                NullWriter.Instance, contentType, encoding);
        } catch(IllegalArgumentException e) {
        	//Added because of an RI bug prior to 1.2_05-b3.  Might as well leave it in case other
        	//impls have the same problem.  https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=613
        	log.fine("The impl didn't correctly handled '*/*' in the content type list.  Trying '*/*' directly.");
	        writer = renderKit.createResponseWriter(
	                NullWriter.Instance, "*/*", encoding);
        }

        contentType = getResponseContentType(context, writer.getContentType());
        encoding = getResponseEncoding(context, writer.getCharacterEncoding());

        // apply them to the response
        response.setContentType(contentType + "; charset=" + encoding);

        // removed 2005.8.23 to comply with J2EE 1.3
        // response.setCharacterEncoding(encoding);

        // Now, clone with the real writer
        writer = writer.cloneWithWriter(response.getWriter());

        return writer;
    }

    /**
     * Generate the encoding
     *
     * @param context
     * @param orig
     * @return
     */
    protected String getResponseEncoding(FacesContext context, String orig) {
        String encoding = orig;

        // see if we need to override the encoding
        Map m = context.getExternalContext().getRequestMap();
        Map sm = context.getExternalContext().getSessionMap();

        // 1. check the request attribute
        if (m.containsKey("facelets.Encoding")) {
            encoding = (String) m.get("facelets.Encoding");
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Facelet specified alternate encoding '" + encoding
                        + "'");
            }
            sm.put(CHARACTER_ENCODING_KEY, encoding);
        }

        // 2. get it from request
        Object request = context.getExternalContext().getRequest();
        if (encoding == null && request instanceof ServletRequest) {
            encoding = ((ServletRequest) request).getCharacterEncoding();
        }

        // 3. get it from the session
        if (encoding == null) {
            encoding = (String) sm.get(CHARACTER_ENCODING_KEY);
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Session specified alternate encoding '" + encoding
                        + "'");
            }
        }

        // 4. default it
        if (encoding == null) {
            encoding = "UTF-8";
            if (log.isLoggable(Level.FINEST)) {
                log
                        .finest("ResponseWriter created had a null CharacterEncoding, defaulting to UTF-8");
            }
        }

        return encoding;
    }

    /**
     * Generate the content type
     *
     * @param context
     * @param orig
     * @return
     */
    protected String getResponseContentType(FacesContext context, String orig) {
        String contentType = orig;

        // see if we need to override the contentType
        Map m = context.getExternalContext().getRequestMap();
        if (m.containsKey("facelets.ContentType")) {
            contentType = (String) m.get("facelets.ContentType");
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Facelet specified alternate contentType '"
                        + contentType + "'");
            }
        }

        // safety check
        if (contentType == null) {
            contentType = "text/html";
            if (log.isLoggable(Level.FINEST)) {
                log
                        .finest("ResponseWriter created had a null ContentType, defaulting to text/html");
            }
        }

        return contentType;
    }

    protected void buildView(FacesContext context, UIViewRoot viewToRender)
            throws IOException, FacesException {
        // setup our viewId
        String renderedViewId = this.getRenderedViewId(context, viewToRender
                .getViewId());
        viewToRender.setViewId(renderedViewId);
        // lazy initialize so we have a FacesContext to use
        if (this.faceletFactory == null) {
            this.initialize(context);
        }
        

        if (log.isLoggable(Level.FINE)) {
            log.fine("Building View: " + renderedViewId);
        }

        // grab our FaceletFactory and create a Facelet
        Facelet f = null;
        FaceletFactory.setInstance(this.faceletFactory);
        try {
            f = this.faceletFactory.getFacelet(viewToRender.getViewId());
        } finally {
            FaceletFactory.setInstance(null);
        }

        // populate UIViewRoot
        long time = System.currentTimeMillis();
        f.apply(context, viewToRender);
        setViewPopulated(context.getExternalContext(), viewToRender);
        time = System.currentTimeMillis() - time;
        if (log.isLoggable(Level.FINE)) {
            log.fine("Took " + time + "ms to build view: "
                    + viewToRender.getViewId());
        }
    }
    
    private boolean isViewPopulated(ExternalContext extContext, 
            UIViewRoot viewToRender) {
        boolean result = false;
        
        result = extContext.getRequestMap().containsKey(viewToRender.getViewId());
        
        return result;
    }
    
    private void setViewPopulated(ExternalContext extContext, UIViewRoot viewToRender) {
        extContext.getRequestMap().put(viewToRender.getViewId(), Boolean.TRUE);
        
    }

    public void renderView(FacesContext context, UIViewRoot viewToRender)
            throws IOException, FacesException {

        // lazy initialize so we have a FacesContext to use
        if (this.faceletFactory == null) {
            this.initialize(context);
        }

        // exit if the view is not to be rendered
        if (!viewToRender.isRendered()) {
            return;
        }

        // if facelets is not supposed to handle this request
        if (!handledByFacelets(viewToRender.getViewId())) {
            this.parent.renderView(context, viewToRender);
            return;
        }

        // log request
        if (log.isLoggable(Level.FINE)) {
            log.fine("Rendering View: " + viewToRender.getViewId());
        }

        StateWriter stateWriter = null;
        try {
            // Only build the view if this view has not yet been built.
            if (!this.isViewPopulated(context.getExternalContext(), viewToRender)) {
                this.buildView(context, viewToRender);
            }

            // setup writer and assign it to the context
            ResponseWriter origWriter = null;
            if (null == (origWriter = context.getResponseWriter())) {
                origWriter = this.createResponseWriter(context);
            }
            // QUESTION: should we use bufferSize? Or, since the
            // StateWriter usually only needs a small bit at the end,
            // should we always use a much smaller size?
            stateWriter = new StateWriter(origWriter,
                    this.bufferSize != -1 ? this.bufferSize : 1024);

            ResponseWriter writer = origWriter.cloneWithWriter(stateWriter);
            context.setResponseWriter(writer);

            // force creation of session if saving state there
            StateManager stateMgr = context.getApplication().getStateManager();
            if (!stateMgr.isSavingStateInClient(context)) {
                context.getExternalContext().getSession(true);
            }

            long time = System.currentTimeMillis();

            // render the view to the response
            writer.startDocument();
            if (FacesAPI.getVersion() >= 12) {
                viewToRender.encodeAll(context);
            } else {
                ComponentSupport.encodeRecursive(context, viewToRender);
            }
            writer.endDocument();

            // finish writing
            writer.close();

            // remove transients for older versions
            if (FacesAPI.getVersion() < 12) {
                ComponentSupport.removeTransient(viewToRender);
            }

            boolean writtenState = stateWriter.isStateWritten();
            // flush to origWriter
            if (writtenState) {
                String content = stateWriter.getAndResetBuffer();
                int end = content.indexOf(STATE_KEY);
                // See if we can find any trace of the saved state.
                // If so, we need to perform token replacement
                if (end >= 0) {
                    // save state
                    Object stateObj = stateMgr.saveSerializedView(context);
                    String stateStr;
                    if (stateObj == null) {
                        stateStr = null;
                    } else {
                        stateMgr.writeState(context,
                                       (StateManager.SerializedView) stateObj);
                        stateStr = stateWriter.getAndResetBuffer();
                    }

                    int start = 0;

                    while (end != -1) {
                        origWriter.write(content, start, end - start);
                        if (stateStr != null) {
                            origWriter.write(stateStr);
                        }
                        start = end + STATE_KEY_LEN;
                        end = content.indexOf(STATE_KEY, start);
                    }
                    origWriter.write(content, start, content.length() - start);
                // No trace of any saved state, so we just need to flush
                // the buffer
                } else {
                    origWriter.write(content);
                    // But for JSF 1.1 (actually, just 1.1_01 RI), if we didn't
                    // detect any saved state, force a call to
                    // saveSerializedView() in case we're using the old
                    // pure-server-side state saving
                    if ((FacesAPI.getVersion() < 12)
                        && !stateMgr.isSavingStateInClient(context)) {
                        stateMgr.saveSerializedView(context);
                    }
                }
            }

            time = System.currentTimeMillis() - time;
            if (log.isLoggable(Level.FINE)) {
                log.fine("Took " + time + "ms to render view: "
                        + viewToRender.getViewId());
            }

        } catch (FileNotFoundException fnfe) {
            this.handleFaceletNotFound(context, viewToRender.getViewId());
        } catch (Exception e) {
            this.handleRenderException(context, e);
        } finally {
            if (stateWriter != null)
                stateWriter.release();
        }
    }

    protected void handleRenderException(FacesContext context, Exception e)
            throws IOException, ELException, FacesException {
        Object resp = context.getExternalContext().getResponse();

        // always log
        if (log.isLoggable(Level.SEVERE)) {
            UIViewRoot root = context.getViewRoot();
            StringBuffer sb = new StringBuffer(64);
            sb.append("Error Rendering View");
            if (root != null) {
                sb.append('[');
                sb.append(root.getViewId());
                sb.append(']');
            }
            log.log(Level.SEVERE, sb.toString(), e);
        }

        // handle dev response
        if (this.developmentMode && !context.getResponseComplete()
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

    protected void handleFaceletNotFound(FacesContext context, String viewId)
            throws FacesException, IOException {
        String actualId = this.getActionURL(context, viewId);
        Object respObj = context.getExternalContext().getResponse();
        if (respObj instanceof HttpServletResponse) {
            HttpServletResponse respHttp = (HttpServletResponse) respObj;
            respHttp.sendError(HttpServletResponse.SC_NOT_FOUND, actualId);
            context.responseComplete();
        }
    }

    /**
     * Determine if Facelets needs to handle this request.
     */
    private boolean handledByFacelets(String viewId) {
        // If there's no extensions array or prefixes array, then
        // just make Facelets handle everything
        if ((extensionsArray == null) && (prefixesArray == null)) {
            return true;
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

        return false;
    }

    public String getDefaultSuffix(FacesContext context) throws FacesException {
        if (this.defaultSuffix == null) {
            ExternalContext extCtx = context.getExternalContext();
            String viewSuffix = extCtx
                    .getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
            this.defaultSuffix = (viewSuffix != null) ? viewSuffix
                    : ViewHandler.DEFAULT_SUFFIX;
        }
        return this.defaultSuffix;
    }

    protected String getRenderedViewId(FacesContext context, String actionId) {
        ExternalContext extCtx = context.getExternalContext();
        String viewId = actionId;
        if (extCtx.getRequestPathInfo() == null) {
            String viewSuffix = this.getDefaultSuffix(context);
            viewId = new StringBuffer(viewId).replace(viewId.lastIndexOf('.'),
                                                      viewId.length(),
                                                      viewSuffix).toString();
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine("ActionId -> ViewId: " + actionId + " -> " + viewId);
        }
        return viewId;
    }

    public void writeState(FacesContext context) throws IOException {
        if (handledByFacelets(context.getViewRoot().getViewId())) {
            // Tell the StateWriter that we're about to write state
            StateWriter.getCurrentInstance().writingState();
            // Write the STATE_KEY out.  Unfortunately, this will
            // be wasteful for pure server-side state managers where nothing
            // is actually written into the output, but this cannot
            // programatically be discovered
            context.getResponseWriter().write(STATE_KEY);
        } else {
            this.parent.writeState(context);
        }
    }

    public Locale calculateLocale(FacesContext context) {
        return this.parent.calculateLocale(context);
    }

    public String calculateRenderKitId(FacesContext context) {
        return this.parent.calculateRenderKitId(context);
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        UIViewRoot result = null;
        if (UIDebug.debugRequest(context)) {
            result = new UIViewRoot();
        }

        // lazy initialize so we have a FacesContext to use
        if (this.faceletFactory == null) {
            this.initialize(context);
        }

        result = this.parent.createView(context, viewId);
        
        try {
            context.setViewRoot(result);
            if (handledByFacelets(viewId)) {
                this.buildView(context, result);
            }
        }
        catch (IOException ioe) {
            if (log.isLoggable(Level.SEVERE)) {
                log
                        .log(Level.SEVERE,
                        "Unable to create View tree", ioe);
            }
            
        }
        
        return result;
    }

    public String getActionURL(FacesContext context, String viewId) {
        return this.parent.getActionURL(context, viewId);
    }

    public String getResourceURL(FacesContext context, String path) {
        return this.parent.getResourceURL(context, path);
    }

    /**
     * Try to guess if this is a postback request. In JSF 1.2, this method is
     * not needed, since ResponseStateManager can identify postbacks. We use a
     * simple heuristic: for HttpServletRequests, "POST" and "PUT" are
     * postbacks. For anything that isn't an HttpServletRequest, just guess that
     * if there's a request parameter, it's probably a postback.
     */
    static private boolean isPostback11(FacesContext context) {
        Object reqObject = context.getExternalContext().getRequest();
        if (reqObject instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) reqObject;

            String method = request.getMethod();

            // Is this a POST or PUT request?
            if ("POST".equals(method) || "PUT".equals(method)) {
                return true;
            }

            return false;
        } else {
            Map paramMap = context.getExternalContext()
                    .getRequestParameterMap();
            return !paramMap.isEmpty();
        }
    }

    protected static class NullWriter extends Writer {

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
    }
}
