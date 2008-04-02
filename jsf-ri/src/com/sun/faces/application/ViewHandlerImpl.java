/* 
 * $Id: ViewHandlerImpl.java,v 1.68 2006/03/22 20:49:59 edburns Exp $ 
 */ 


/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */


// ViewHandlerImpl.java 

package com.sun.faces.application;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.application.StateManager.SerializedView;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.RIConstants;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.util.DebugUtil;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler.
 *
 * @version $Id: ViewHandlerImpl.java,v 1.68 2006/03/22 20:49:59 edburns Exp $
 * @see javax.faces.application.ViewHandler
 */
public class ViewHandlerImpl extends ViewHandler {

    // Log instance for this class
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER
                                                  + Util.APPLICATION_LOGGER);

    private static final String AFTER_VIEW_CONTENT = RIConstants.FACES_PREFIX+
                                                     "AFTER_VIEW_CONTENT";

    /**
     * <p>The <code>request</code> scoped attribute to store the
     * {@link javax.faces.webapp.FacesServlet} path of the original
     * request.</p>
     */
    private static final String INVOCATION_PATH =
        RIConstants.FACES_PREFIX + "INVOCATION_PATH";

    //
    // Relationship Instance Variables
    //

    /**
     * <p>Store the value of <code>DEFAULT_SUFFIX_PARAM_NAME</code>
     * or, if that isn't defined, the value of <code>DEFAULT_SUFFIX</code>
     */
    private String contextDefaultSuffix;

    /**
     * If <code>true</code>, at least one method exists on 
     * the <code>StateManager</code> implementation that
     * replaces a deprecated method.  This will be used 
     * to determine if the non-deprecated methods should
     * be called.
     */
    private boolean nonDeprecatedMethodExists = false;

    /**
     * If <code>true</code>, then we've already performed
     * the check on the <code>StateManager</code> implementation
     * for at least one method that replace a deprecated method.
     */
    private boolean checkedForNonDeprecated = false;

    public ViewHandlerImpl() {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE,"Created ViewHandler instance ");
        }
    }


    public void renderView(FacesContext context,
            UIViewRoot viewToRender) throws IOException,
            FacesException {
        
        // suppress rendering if "rendered" property on the component is
        // false
        if (!viewToRender.isRendered()) {
            return;
        }
        
        ExternalContext extContext = context.getExternalContext();
        ServletRequest request = (ServletRequest) extContext.getRequest();
        ServletResponse response = (ServletResponse) extContext.getResponse();
        
        try {
            if (executePageToBuildView(context, viewToRender)) {
                response.flushBuffer(); 
                ApplicationAssociate.getInstance(extContext).responseRendered();
                return;
            }
        } catch (IOException e) {
            throw new FacesException(e);
        }
        
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Completed building view for : \n" +
                    viewToRender.getViewId());
        }
        if (logger.isLoggable(Level.FINEST)) {
            DebugUtil.printTree(viewToRender, logger, Level.FINEST);
        }
        
        // set up the ResponseWriter
        
        RenderKitFactory renderFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit =
                renderFactory.getRenderKit(context, viewToRender.getRenderKitId());
                
        ResponseWriter oldWriter = context.getResponseWriter();
        Writer strWriter = new FastStringWriter(2048);
        ResponseWriter newWriter = null;
        if (null != oldWriter) {
            newWriter = oldWriter.cloneWithWriter(strWriter);
        } else {
            newWriter = renderKit.createResponseWriter(strWriter, null,
                    request.getCharacterEncoding());            
        }
        context.setResponseWriter(newWriter);
        
        newWriter.startDocument();
        
        doRenderView(context, viewToRender);
        
        newWriter.endDocument();
        
        // replace markers in the body content and write it to response.

        ResponseWriter responseWriter = null;
        if (null != oldWriter) {
            responseWriter = oldWriter.cloneWithWriter(response.getWriter());
        } else {
            responseWriter = newWriter.cloneWithWriter(response.getWriter());
        }
        context.setResponseWriter(responseWriter);
        
        String bodyContent = strWriter.toString();
        replaceMarkers(bodyContent, context);
        
        if (null != oldWriter) {
            context.setResponseWriter(oldWriter);
        }
        
        // write any AFTER_VIEW_CONTENT to the response
        Object content = extContext.getRequestMap().get(AFTER_VIEW_CONTENT);
        assert(null != content);
        if (content instanceof byte []) {
            response.getWriter().write(new String((byte[]) content));
        } else if (content instanceof char []) {
            response.getWriter().write((char []) content);
        } else {
            assert(false);
        }
        
        response.flushBuffer(); // PENDING(edburns): necessary?
        
        // remove the AFTER_VIEW_CONTENT from the view root
        extContext.getRequestMap().remove(AFTER_VIEW_CONTENT);
        
        // PENDING (visvan) do we need this any more since we save the tree
        // after encode ??
       /* if (!context.getExternalContext().getRequestMap().containsKey(RIConstants.SAVED_STATE)) {
            // if we didn't serialize the state, or we didn't save it in
            // the client, we need to manually remove the transient
            // children and facets.
            removeTransientChildrenAndFacets(context, viewToRender,
                    new HashSet());
        } */
    }

    /**
     * <p>This is a separate method to account for handling the content
     * after the view tag.</p>
     *
     * <p>Create a new ResponseWriter around this response's Writer.
     * Set it into the FacesContext, saving the old one aside.</p>
     *
     * <p>call encodeBegin(), encodeChildren(), encodeEnd() on the
     * argument <code>UIViewRoot</code>.</p>
     *
     * <p>Restore the old ResponseWriter into the FacesContext.</p>
     *
     * <p>Write out the after view content to the response's writer.</p>
     *
     * <p>Flush the response buffer, and remove the after view content
     * from the request scope.</p>
     */

    private void doRenderView(FacesContext context,
                              UIViewRoot viewToRender) throws IOException,
            FacesException {
    ExternalContext extContext = context.getExternalContext();

    ApplicationAssociate associate =
        ApplicationAssociate.getInstance(extContext);

        if (null != associate) {
            associate.responseRendered();
        }

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "About to render view " + viewToRender.getViewId());
        }

        viewToRender.encodeAll(context);
    }


    public UIViewRoot restoreView(FacesContext context, String viewId) {
        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" context " + context;
            throw new NullPointerException(message);
        }

        ExternalContext extContext = context.getExternalContext();

        String mapping = getFacesMapping(context);
        UIViewRoot viewRoot = null;

        if (mapping != null && !isPrefixMapped(mapping)) {
            viewId = convertViewId(context, viewId);
        }

        // maping could be null if a non-faces request triggered
        // this response.
        if (extContext.getRequestPathInfo() == null && mapping != null &&
            isPrefixMapped(mapping)) {
            // this was probably an initial request
            // send them off to the root of the web application
            try {
                context.responseComplete();
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "Response Complete for" + viewId);
                }
                extContext.redirect(extContext.getRequestContextPath());
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }
        } else {
            // this is necessary to allow decorated impls.
            ViewHandler outerViewHandler =
                    context.getApplication().getViewHandler();
            String renderKitId =
                    outerViewHandler.calculateRenderKitId(context);
            viewRoot = Util.getStateManager(context).restoreView(context,
                                                                 viewId,
                                                                 renderKitId);
        }

        return viewRoot;
    }


    public UIViewRoot createView(FacesContext context, String viewId) {
        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                    (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +"context " + context;
            throw new NullPointerException(message);
        }

        Locale locale = null;
        String renderKitId = null;

        // use the locale from the previous view if is was one which will be
        // the case if this is called from NavigationHandler. There wouldn't be 
        // one for the initial case.
        if (context.getViewRoot() != null) {
            locale = context.getViewRoot().getLocale();
            renderKitId = context.getViewRoot().getRenderKitId();
        }
        UIViewRoot result = (UIViewRoot)
                context.getApplication().createComponent(UIViewRoot.COMPONENT_TYPE);
        result.setViewId(viewId);

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Created new view for " + viewId);
        }
        // PENDING(): not sure if we should set the RenderKitId here.
        // The UIViewRoot ctor sets the renderKitId to the default
        // one.
        // if there was no locale from the previous view, calculate the locale 
        // for this view.
        if (locale == null) {
            locale =
                context.getApplication().getViewHandler().calculateLocale(
                    context);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Locale for this view as determined by calculateLocale "
                            + locale.toString());
            }
        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Using locale from previous view "
                            + locale.toString());
            }
        }

        if (renderKitId == null) {
            renderKitId =
                context.getApplication().getViewHandler().calculateRenderKitId(
                    context);
           if (logger.isLoggable(Level.FINE)) {
               logger.fine(
               "RenderKitId for this view as determined by calculateRenderKitId "
               + renderKitId);
            }
        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Using renderKitId from previous view "
                            + renderKitId);
            }
        }

        result.setLocale(locale);
        result.setRenderKitId(renderKitId);

        return result;
    }

    /**
     * Execute the target view.  If the HTTP status code range is
     * not 2xx, then return true to indicate the response should be
     * immediately flushed by the caller so that conditions such as 404
     * are properly handled.
     * @return <code>true</code> if the response should be immediately flushed 
     *  to the client, otherwise <code>false</code>     
     */
    private boolean executePageToBuildView(FacesContext context,
                                        UIViewRoot viewToExecute) throws IOException, FacesException {

        if (null == context || null == viewToExecute) {
            String message = MessageUtils.getExceptionMessageString
                    (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " context " + context + " viewToExecute " +
                      viewToExecute;
            throw new NullPointerException(message);
        }

        String mapping = getFacesMapping(context);
        String requestURI = 
              updateRequestURI(viewToExecute.getViewId(), mapping);
        
        if (mapping.equals(requestURI)) {
            // The request was to the FacesServlet only - no path info
            // on some containers this causes a recursion in the 
            // RequestDispatcher and the request appears to hang.
            // If this is detected, return status 404
            HttpServletResponse response = (HttpServletResponse) 
                  context.getExternalContext().getResponse();
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return true;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("About to execute view " + requestURI);
        }
        
        String newViewId = requestURI;
        // If we have a valid mapping (meaning we were invoked via the
        // FacesServlet) and we're extension mapped, do the replacement.
        if (mapping != null && !isPrefixMapped(mapping)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine( "Found URL pattern mapping to FacesServlet "
                             + mapping);
            }
            newViewId = convertViewId(context, requestURI);
        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Found no URL patterns mapping to FacesServlet ");
            }
        }


        viewToExecute.setViewId(newViewId);
        ExternalContext extContext = context.getExternalContext();

        // update the JSTL locale attribute in request scope so that JSTL
        // picks up the locale from viewRoot. This attribute must be updated
        // before the JSTL setBundle tag is called because that is when the
        // new LocalizationContext object is created based on the locale.
        // PENDING: this only works for servlet based requests
        if (extContext.getRequest()
        instanceof ServletRequest) {
            Config.set((ServletRequest)
            extContext.getRequest(),
                       Config.FMT_LOCALE, context.getViewRoot().getLocale());
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Before dispacthMessage to newViewId " + newViewId);
        }

        // save the original response
        Object originalResponse = extContext.getResponse();

        // replace the response with our wrapper
        ViewHandlerResponseWrapper wrapped = null;
        extContext.setResponse(wrapped = new ViewHandlerResponseWrapper((HttpServletResponse)extContext.getResponse()));

        // build the view by executing the page
        extContext.dispatch(newViewId);        
        
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("After dispacthMessage to newViewId " + newViewId);
        }
        
        // replace the original response
        extContext.setResponse(originalResponse);
        
        // Follow the JSTL 1.2 spec, section 7.4,  
        // on handling status codes on a forward
        if (wrapped.getStatus() < 200 || wrapped.getStatus() > 299) {
            return true;            
        }
        
        // Put the AFTER_VIEW_CONTENT into request scope
        // temporarily
        if (wrapped.isBytes()) {
            extContext.getRequestMap().put(AFTER_VIEW_CONTENT,
                                           wrapped.getBytes());
        } else if (wrapped.isChars()) {
            extContext.getRequestMap().put(AFTER_VIEW_CONTENT,
                                           wrapped.getChars());
        }

        return false;
        
    }


    public Locale calculateLocale(FacesContext context) {

        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +"context " + context;
            throw new NullPointerException(message);
        }

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


    public String calculateRenderKitId(FacesContext context) {

        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +"context " + context;
            throw new NullPointerException(message);
        }
        String result = null;

        Map<String,String> requestParamMap = context.getExternalContext()
            .getRequestParameterMap();
        result = requestParamMap.get(
            ResponseStateManager.RENDER_KIT_ID_PARAM);

        if (result == null) {
            if (null ==
                (result = context.getApplication().getDefaultRenderKitId())) {
                result = RenderKitFactory.HTML_BASIC_RENDER_KIT;
            }
        }
        return result;
    }


    /**
     * Attempts to find a matching locale based on <code>perf</code> and
     * list of supported locales, using the matching algorithm
     * as described in JSTL 8.3.2.
     */
    protected Locale findMatch(FacesContext context, Locale perf) {
        Locale result = null;
        Iterator<Locale> it = context.getApplication().getSupportedLocales();
        while (it.hasNext()) {
            Locale supportedLocale = it.next();

            if (perf.equals(supportedLocale)) {
                // exact match
                result = supportedLocale;
                break;
            } else {
                // Make sure the preferred locale doesn't have country
                // set, when doing a language match, For ex., if the
                // preferred locale is "en-US", if one of supported
                // locales is "en-UK", even though its language matches
                // that of the preferred locale, we must ignore it.
                if (perf.getLanguage().equals(supportedLocale.getLanguage()) &&
                    supportedLocale.getCountry().equals("")) {
                    result = supportedLocale;
                }
            }
        }
        // if it's not in the supported locales,
        if (null == result) {
            Locale defaultLocale = context.getApplication().getDefaultLocale();
            if (defaultLocale != null) {
                if ( perf.equals(defaultLocale)) {
                    // exact match
                    result = defaultLocale;
                } else {
                    // Make sure the preferred locale doesn't have country
                    // set, when doing a language match, For ex., if the
                    // preferred locale is "en-US", if one of supported
                    // locales is "en-UK", even though its language matches
                    // that of the preferred locale, we must ignore it.
                    if (perf.getLanguage().equals(defaultLocale.getLanguage()) &&
                        defaultLocale.getCountry().equals("")) {
                        result = defaultLocale;
                    }
                }
            }
        }

        return result;
    }


    public void writeState(FacesContext context) throws IOException {
        if (context == null) {
           String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +"context " + context;
            throw new NullPointerException(message);
        }

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Begin writing market for viewId " +
                        context.getViewRoot().getViewId());
        }

        context.getResponseWriter().writeText(
                    RIConstants.SAVESTATE_FIELD_MARKER, null);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("End writing marker for viewId " +
                        context.getViewRoot().getViewId());
        }

    }


    public String getActionURL(FacesContext context, String viewId) {

        if (context == null || viewId == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +"context " + context + " viewId " + viewId;
            throw new NullPointerException(message);
        }

        if (viewId.charAt(0) != '/') {
            String message = MessageUtils.getExceptionMessageString(MessageUtils.ILLEGAL_VIEW_ID_ID,
                                                            new Object[]{viewId});
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "jsf.illegal_view_id_error", viewId);
            }
        throw new IllegalArgumentException(message);
        }

        // Acquire the context path, which we will prefix on all results
        String contextPath =
            context.getExternalContext().getRequestContextPath();

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
        int period = viewId.lastIndexOf(".");
        if (period < 0) {
            return contextPath + viewId + mapping;
        } else if (!viewId.endsWith(mapping)) {
            return contextPath + viewId.substring(0, period) + mapping;
        } else {
            return contextPath + viewId;
        }

    }


    public String getResourceURL(FacesContext context, String path) {

        if (path.startsWith("/")) {
            return context.getExternalContext().getRequestContextPath() + path;
        } else {
            return (path);
        }

    }


    /**
     * <p>Returns the URL pattern of the
     * {@link javax.faces.webapp.FacesServlet} that
     * is executing the current request.  If there are multiple
     * URL patterns, the value returned by
     * <code>HttpServletRequest.getServletPath()</code> and
     * <code>HttpServletRequest.getPathInfo()</code> is
     * used to determine which mapping to return.</p>
     * If no mapping can be determined, it most likely means
     * that this particular request wasn't dispatched through
     * the {@link javax.faces.webapp.FacesServlet}.
     *
     * @param context the {@link FacesContext} of the current request
     * @return the URL pattern of the {@link javax.faces.webapp.FacesServlet}
     *         or <code>null</code> if no mapping can be determined
     * @throws NullPointerException if <code>context</code> is null
     */
    private String getFacesMapping(FacesContext context) {
        // PENDING (rlubke) Need to handle the Portlet case

        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" context " + context;
            throw new NullPointerException(message);
        }

        // Check for a previously stored mapping   
        ExternalContext extContext = context.getExternalContext();
        String mapping =
            (String) extContext.getRequestMap().get(INVOCATION_PATH);

        if (mapping == null) {

            Object request = extContext.getRequest();
            String servletPath = null;
            String pathInfo = null;

            // first check for javax.servlet.forward.servlet_path
            // and javax.servlet.forward.path_info for non-null
            // values.  if either is non-null, use this
            // information to generate determine the mapping.

            if (request instanceof HttpServletRequest) {
                servletPath = extContext.getRequestServletPath();
                pathInfo = extContext.getRequestPathInfo();
            }


            mapping = getMappingForRequest(servletPath, pathInfo);
            if (mapping == null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE,
                               "jsf.faces_servlet_mapping_cannot_be_determined_error",
                               new Object[]{servletPath});
                }
            }
        }

        if (mapping != null) {
            extContext.getRequestMap().put(INVOCATION_PATH, mapping);
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE,
                       "URL pattern of the FacesServlet executing the current request "
                       + mapping);
        }
        return mapping;
    }


    /**
     * <p>Return the appropriate {@link javax.faces.webapp.FacesServlet} mapping
     * based on the servlet path of the current request.</p>
     *
     * @param servletPath the servlet path of the request
     * @param pathInfo    the path info of the request
     * @see HttpServletRequest#getServletPath()
     */
    private String getMappingForRequest(String servletPath, String pathInfo) {

        if (servletPath == null) {
            return null;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "servletPath " + servletPath);
            logger.log(Level.FINE, "pathInfo " + pathInfo);
        }
        // If the path returned by HttpServletRequest.getServletPath()
        // returns a zero-length String, then the FacesServlet has
        // been mapped to '/*'.
        if (servletPath.length() == 0) {
                return "/*";
        }

        // presence of path info means we were invoked
        // using a prefix path mapping
        if (pathInfo != null) {
            return servletPath;
        } else if (servletPath.indexOf('.') < 0) {
            // if pathInfo is null and no '.' is present, assume the
            // FacesServlet was invoked using prefix path but without
            // any pathInfo - i.e. GET /contextroot/faces or
            // GET /contextroot/faces/
            return servletPath;
        } else {
           // Servlet invoked using extension mapping
            return servletPath.substring(servletPath.lastIndexOf('.'));
        }
    }

    /**
     * <p>if the specified mapping is a prefix mapping, and the provided 
     * request URI (usually the value from <code>ExternalContext.getRequestServletPath()</code>)
     * starts with <code>mapping + '/'</code>, prune the mapping from the
     * URI and return it, otherwise, return the original URI. 
     * @param uri the servlet request path
     * @param mapping the FacesServlet mapping used for this request
     * @return the URI without additional FacesServlet mappings
     * @since 1.2
     */
    private String updateRequestURI(String uri, String mapping) {
        
        if (!isPrefixMapped(mapping)) {
            return uri;
        } else {
            int length = mapping.length() + 1;
            StringBuilder builder = new StringBuilder(length);
            builder.append(mapping).append('/');
            String mappingMod = builder.toString();
            boolean logged = false;
            while (uri.startsWith(mappingMod)) {
                if (!logged && logger.isLoggable(Level.WARNING)) {
                    logged = true;
                    logger.log(Level.WARNING,
                               "jsf.viewhandler.requestpath.recursion",
                               new Object[] {uri, mapping});
                }
                uri = uri.substring(length - 1);
            }
            return uri;
        }
    }


    /**
     * <p>Returns true if the provided <code>url-mapping</code> is
     * a prefix path mapping (starts with <code>/</code>).</p>
     *
     * @param mapping a <code>url-pattern</code>
     * @return true if the mapping starts with <code>/</code>
     */
    private static boolean isPrefixMapped(String mapping) {
        return (mapping.charAt(0) == '/');
    }


    /**
     * <p>Adjust the viewID per the requirements of {@link #renderView}.</p>
     *
     * @param context current {@link FacesContext}
     * @param viewId  incoming view ID
     * @return the view ID with an altered suffix mapping (if necessary)
     */
    private String convertViewId(FacesContext context, String viewId) {
        synchronized (this) {
            if (contextDefaultSuffix == null) {
                contextDefaultSuffix =
                    context.getExternalContext().
                    getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
                if (contextDefaultSuffix == null) {
                    contextDefaultSuffix = ViewHandler.DEFAULT_SUFFIX;
                }
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("contextDefaultSuffix " + contextDefaultSuffix);
                }
            }
        }
        String convertedViewId = viewId;
        // if the viewId doesn't already use the above suffix,
        // replace or append.
        if (!convertedViewId.endsWith(contextDefaultSuffix)) {
            StringBuffer buffer = new StringBuffer(convertedViewId);
            int extIdx = convertedViewId.lastIndexOf('.');
            if (extIdx != -1) {
                buffer.replace(extIdx, convertedViewId.length(),
                               contextDefaultSuffix);
            } else {
                // no extension in the provided viewId, append the suffix
                buffer.append(contextDefaultSuffix);
            }
            convertedViewId = buffer.toString();
            if (logger.isLoggable(Level.FINE)) {
                logger.fine( "viewId after appending the context suffix " +
                             convertedViewId);
            }

        }
        return convertedViewId;
    }

     public void replaceMarkers(String content, FacesContext context) {
         SerializedView view = null;
         Object stateArray = null;
         StateManager stateManager = Util.getStateManager(context);
         try {
             if (!checkedForNonDeprecated) {
                 nonDeprecatedMethodExists =
                     Util.hasDeclaredMethod(stateManager, "saveView");
                 checkedForNonDeprecated = true;
             }
             if (nonDeprecatedMethodExists) {
                 stateArray = stateManager.saveView(context);
             } else {
                 view = stateManager.saveSerializedView(context);
             }
        } catch (IllegalStateException ise) {
            throw new FacesException(ise);
        } catch (Exception ie) {
            // catch any exception thrown while saving the view           
            throw new FacesException(MessageUtils.getExceptionMessageString(
                MessageUtils.SAVING_STATE_ERROR_MESSAGE_ID), ie);
        }
        int
            beginIndex = 0,
            markerIndex = 0,
            markerLen = RIConstants.SAVESTATE_FIELD_MARKER.length(),
            contentLen = 0;

        try {
        contentLen = content.length();
        do {
        // if we have no more markers
        if (-1 == (markerIndex =
               content.indexOf(RIConstants.SAVESTATE_FIELD_MARKER,
                               beginIndex))) {
            // write out the rest of the content
                     context.getResponseWriter().write(content.substring(beginIndex));
        } else {
            // we have more markers, write out the current chunk

            context.getResponseWriter().write(content.substring(
                            beginIndex, markerIndex));
                    if (nonDeprecatedMethodExists) {
                        stateManager.writeState(context, stateArray);
                    } else {
                stateManager.writeState(context, view);
                    }
            beginIndex = markerIndex + markerLen;
        }
        } while (-1 != markerIndex && beginIndex < contentLen);
        } catch (Exception ex) {
            // catch any thrown while write state.
            throw new FacesException(ex);
        }
    }
}
