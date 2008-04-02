/* 
 * $Id: ViewHandlerImpl.java,v 1.89 2006/10/18 16:29:10 edburns Exp $ 
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
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.util.DebugUtil;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler.
 *
 * @version $Id: ViewHandlerImpl.java,v 1.89 2006/10/18 16:29:10 edburns Exp $
 * @see javax.faces.application.ViewHandler
 */
public class ViewHandlerImpl extends ViewHandler {

    // Log instance for this class
    private static final Logger logger = Util.getLogger(Util.FACES_LOGGER
                                                  + Util.APPLICATION_LOGGER);

    private static final String AFTER_VIEW_CONTENT = RIConstants.FACES_PREFIX+
                                                     "AFTER_VIEW_CONTENT";    

    //
    // Relationship Instance Variables
    //

    /**
     * <p>Store the value of <code>DEFAULT_SUFFIX_PARAM_NAME</code>
     * or, if that isn't defined, the value of <code>DEFAULT_SUFFIX</code>
     */
    private String contextDefaultSuffix;
    private int bufSize = -1;

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
            logger.log(Level.FINEST, "+=+=+=+=+=+= Printout for " + viewToRender.getViewId() + " about to render.");
            DebugUtil.printTree(viewToRender, logger, Level.FINEST);
        }
        
        // set up the ResponseWriter
        
        RenderKitFactory renderFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit =
                renderFactory.getRenderKit(context, viewToRender.getRenderKitId());
                
        ResponseWriter oldWriter = context.getResponseWriter();

        if (bufSize == -1) {
            WebConfiguration webConfig =
                  WebConfiguration
                        .getInstance(context.getExternalContext());
            try {
                bufSize = Integer
                      .parseInt(webConfig.getContextInitParameter(
                            WebContextInitParameter.ResponseBufferSize));
            } catch (NumberFormatException nfe) {
                bufSize = Integer
                      .parseInt(WebContextInitParameter.ResponseBufferSize.getDefaultValue());
            }
        }


        WriteBehindStringWriter strWriter = 
              new WriteBehindStringWriter(context, bufSize);
        ResponseWriter newWriter;
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

        ResponseWriter responseWriter;
        if (null != oldWriter) {
            responseWriter = oldWriter.cloneWithWriter(response.getWriter());
        } else {
            responseWriter = newWriter.cloneWithWriter(response.getWriter());
        }
        context.setResponseWriter(responseWriter);
        
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Size of response for view '" + viewToRender + "':" 
                        + strWriter.length());
        }
        
        // flush directly to the response
        strWriter.flushToWriter(response.getWriter());
                        
        if (null != oldWriter) {
            context.setResponseWriter(oldWriter);
        }
        
        // write any AFTER_VIEW_CONTENT to the response
        // side effect: AFTER_VIEW_CONTENT removed
        ViewHandlerResponseWrapper wrapper = (ViewHandlerResponseWrapper)
              extContext.getRequestMap().remove(AFTER_VIEW_CONTENT);
        if (null != wrapper) {
            wrapper.flushToWriter(response.getWriter(),
                    response.getCharacterEncoding());
        }
        
        response.flushBuffer();                
               
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
     * 
     * @param context the <code>FacesContext</code> for the current request
     * @param viewToRender the view to render
     * @throws IOException if an error occurs rendering the view to the client
     */

    private void doRenderView(FacesContext context,
                              UIViewRoot viewToRender) 
    throws IOException, FacesException {   

    ApplicationAssociate associate =
        ApplicationAssociate.getInstance(context.getExternalContext());

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
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
            throw new NullPointerException(message);
        }

        ExternalContext extContext = context.getExternalContext();

        String mapping = Util.getFacesMapping(context);
        UIViewRoot viewRoot = null;

        if (mapping != null && !Util.isPrefixMapped(mapping)) {
            viewId = convertViewId(context, viewId);
        }

        // maping could be null if a non-faces request triggered
        // this response.
        if (extContext.getRequestPathInfo() == null && mapping != null &&
            Util.isPrefixMapped(mapping)) {
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
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "+=+=+=+=+=+= Restored View Printout for " + viewId);
                DebugUtil.printTree(viewRoot, logger, Level.FINEST);
            }

        }

        return viewRoot;
    }


    public UIViewRoot createView(FacesContext context, String viewId) {
        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                    (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
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
     * @param context the <code>FacesContext</code> for the current request
     * @param viewToExecute the view to build
     * @return <code>true</code> if the response should be immediately flushed 
     *  to the client, otherwise <code>false</code> 
     * @throws IOException if an error occurs executing the page    
     */
    private boolean executePageToBuildView(FacesContext context,
                                        UIViewRoot viewToExecute) 
    throws IOException {

        if (null == context) {
            String message = MessageUtils.getExceptionMessageString
                    (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
            throw new NullPointerException(message);
        }
        if (null == viewToExecute) {
            String message = MessageUtils.getExceptionMessageString
                    (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "viewToExecute");
            throw new NullPointerException(message);
        }

        String mapping = Util.getFacesMapping(context);
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
        if (!Util.isPrefixMapped(mapping)) {
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
        ViewHandlerResponseWrapper wrapped = 
              new ViewHandlerResponseWrapper(
                    (HttpServletResponse)extContext.getResponse());
        extContext.setResponse(wrapped);

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
            // flush the contents of the wrapper to the response
            // this is necessary as the user may be using a custom 
            // error page - this content should be propagated
            wrapped.flushContentToWrappedResponse();
            return true;            
        }               
        
        // Put the AFTER_VIEW_CONTENT into request scope
        // temporarily
        extContext.getRequestMap().put(AFTER_VIEW_CONTENT, wrapped);

        return false;
        
    }


    public Locale calculateLocale(FacesContext context) {

        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
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
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
            throw new NullPointerException(message);
        }       

        Map<String,String> requestParamMap = context.getExternalContext()
            .getRequestParameterMap();
        String result = requestParamMap.get(
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
                    supportedLocale.getCountry().equals("")) {
                    result = supportedLocale;
                }
            }
        }
        // if it's not in the supported locales,
        if (null == result) {
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
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
            throw new NullPointerException(message);
        }

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Begin writing market for viewId " +
                        context.getViewRoot().getViewId());
        }

        context.getResponseWriter().write(RIConstants.SAVESTATE_FIELD_MARKER);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("End writing marker for viewId " +
                        context.getViewRoot().getViewId());
        }

    }


    public String getActionURL(FacesContext context, String viewId) {

        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
            throw new NullPointerException(message);
        }
        if (viewId == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "viewId");
            throw new NullPointerException(message);
        }

        if (viewId.charAt(0) != '/') {
            String message = 
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_VIEW_ID_ID,
                        viewId);
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "jsf.illegal_view_id_error", viewId);
            }
        throw new IllegalArgumentException(message);
        }

        // Acquire the context path, which we will prefix on all results
        ExternalContext extContext = context.getExternalContext();
        String contextPath = extContext.getRequestContextPath();

        // Acquire the mapping used to execute this request (if any)
        String mapping = Util.getFacesMapping(context);

        // If no mapping can be identified, just return a server-relative path
        if (mapping == null) {
            return extContext.encodeActionURL(contextPath + viewId);
        }

        // Deal with prefix mapping
        if (Util.isPrefixMapped(mapping)) {
            if (mapping.equals("/*")) {
                return extContext.encodeActionURL(contextPath + viewId);
            } else {
                return extContext
                      .encodeActionURL(contextPath + mapping + viewId);
            }
        }

        // Deal with extension mapping
        int period = viewId.lastIndexOf(".");
        if (period < 0) {
            return extContext.encodeActionURL(contextPath + viewId + mapping);
        } else if (!viewId.endsWith(mapping)) {
            return extContext.encodeActionURL(contextPath
                                                + viewId.substring(0, period)
                                                + mapping);
        } else {
            return extContext.encodeActionURL(contextPath + viewId);
        }

    }


    public String getResourceURL(FacesContext context, String path) {
        ExternalContext extContext = context.getExternalContext();
        if (path.startsWith("/")) {
            return extContext
                  .encodeResourceURL(extContext.getRequestContextPath() + path);
        } else {
            return extContext.encodeResourceURL(path);
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
        
        if (!Util.isPrefixMapped(mapping)) {
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
     * <p>Adjust the viewID per the requirements of {@link #renderView}.</p>
     *
     * @param context current {@link FacesContext}
     * @param viewId  incoming view ID
     * @return the view ID with an altered suffix mapping (if necessary)
     */
    private String convertViewId(FacesContext context, String viewId) {

        if (contextDefaultSuffix == null) {
            contextDefaultSuffix =
                  WebConfiguration
                        .getInstance(context.getExternalContext())
                        .getContextInitParameter(WebContextInitParameter.JspDefaultSuffix);
            if (contextDefaultSuffix == null) {
                contextDefaultSuffix = ViewHandler.DEFAULT_SUFFIX;
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("contextDefaultSuffix "
                            + contextDefaultSuffix);
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
    
    // ----------------------------------------------------------- Inner Classes

    /**
     * <p>Handles writing the response from the dispatched request
     * and replaces any state markers with the actual
     * state supplied by the <code>StateManager</code>.
     */
    private static final class WriteBehindStringWriter extends FastStringWriter {
                        
        // length of the state marker
        private static final int STATE_MARKER_LEN = 
              RIConstants.SAVESTATE_FIELD_MARKER.length();
                        
        // the context for the current request
        private final FacesContext context;
        
        // char buffer
        private final char[] buf;
        
        // buffer length
        private final int bufSize;        
        

        /**
         * <p>Create a new <code>WriteBehindStringWriter</code> for the current
         * request with an initial capacity.</p>
         * @param context the <code>FacesContext</code> for the current request
         * @param initialCapcity the StringBuilder's initial capacity
         */
        public WriteBehindStringWriter(FacesContext context, int initialCapcity) {
            super(initialCapcity);         
            this.context = context;      
            bufSize = initialCapcity;
            buf = new char[bufSize];
        }

        /**
         * <p> Write directly from our FastStringWriter to the provided
         * writer.</p>
         * @param writer where to write
         * @throws IOException if an error occurs
         */
        public void flushToWriter(Writer writer) throws IOException {
            // Save the state to a new instance of StringWriter to
            // avoid multiple serialization steps if the view contains
            // multiple forms.
            StateManager stateManager = Util.getStateManager(context);            
            ResponseWriter origWriter = context.getResponseWriter();
            FastStringWriter state =
                  new FastStringWriter((stateManager.isSavingStateInClient(
                        context)) ? bufSize : 128);
            context.setResponseWriter(origWriter.cloneWithWriter(state));
            stateManager.writeState(context, stateManager.saveView(context));
            context.setResponseWriter(origWriter);
            
            // begin writing...
            int totalLen = builder.length();
            StringBuilder stateBuilder = state.getBuffer();
            int stateLen = stateBuilder.length();
            int pos = 0;
            int tildeIdx = getNextDelimiterIndex(pos);
            while (pos < totalLen) {
                if (tildeIdx != -1) {
                    if (tildeIdx > pos && (tildeIdx - pos) > bufSize) {
                        // theres enough content before the first ~ 
                        // to fill the entire buffer
                        builder.getChars(pos, (pos + bufSize), buf, 0);
                        writer.write(buf);
                        pos += bufSize;
                    } else {
                        // write all content up to the first '~'
                        builder.getChars(pos, tildeIdx, buf, 0);
                        int len = (tildeIdx - pos);
                        writer.write(buf, 0, len);                       
                        // now check to see if the state saving string is
                        // at the begining of pos, if so, write our 
                        // state out.                               
                        if (builder.indexOf(
                              RIConstants.SAVESTATE_FIELD_MARKER, 
                              pos) == tildeIdx) {
                            // buf is effectively zero'd out at this point
                            int statePos = 0;
                            while (statePos < stateLen) {
                                if ((stateLen - statePos) > bufSize) {
                                    // enough state to fill the buffer
                                    stateBuilder.getChars(statePos,
                                                          (statePos + bufSize),
                                                          buf,
                                                          0);
                                    writer.write(buf);
                                    statePos += bufSize;
                                } else {
                                    int slen = (stateLen - statePos);
                                    stateBuilder.getChars(statePos,
                                                          stateLen,
                                                          buf,
                                                          0); 
                                    writer.write(buf, 0, slen);
                                    statePos += slen;
                                }
                                
                            }                            
                        }
                        
                        // push us past the last '~' at the end of the marker
                        pos += (len + STATE_MARKER_LEN);
                        tildeIdx = getNextDelimiterIndex(pos);
                    }
                } else {
                    // we've written all of the state field markers.
                    // finish writing content
                    if (totalLen - pos > bufSize) {
                        // there's enough content to fill the buffer
                        builder.getChars(pos, (pos + bufSize), buf, 0);
                        writer.write(buf);
                        pos += bufSize;
                    } else {
                        // we're near the end of the response
                        builder.getChars(pos, totalLen, buf, 0);
                        int len = (totalLen - pos);
                        writer.write(buf, 0, len);
                        pos += (len + 1);                      
                    }
                }
            }
        }

        /**         
         * @return return the length of the underlying 
         * <code>StringBuilder</code>.
         */
        public int length() {
            return builder.length();
        }

        /**
         * <p>Get the next `~' from the StringBuilder.</p>
         * @param offset the offset from where to search from
         * @return the index of the first '~' from the specified
         *  offset
         */
        private int getNextDelimiterIndex(int offset) {
            return builder.indexOf(RIConstants.SAVESTATE_FIELD_DELIMITER, 
                                   offset);            
        }
        
       
    }
}
