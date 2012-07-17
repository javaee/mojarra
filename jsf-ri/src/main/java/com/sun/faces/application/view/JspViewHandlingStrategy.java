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

package com.sun.faces.application.view;

import java.beans.BeanInfo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.view.ViewMetadata;
import javax.faces.application.Resource;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;

import com.sun.faces.application.ViewHandlerResponseWrapper;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.Util;

import javax.faces.view.StateManagementStrategy;

import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.ResponseBufferSize;

/**
 * This {@link ViewHandlingStrategy} handles JSP-based views.
 */
public class JspViewHandlingStrategy extends ViewHandlingStrategy {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private int responseBufferSize;


    // ------------------------------------------------------------ Constructors


    public JspViewHandlingStrategy() {

        try {
            responseBufferSize =
                  Integer.parseInt(webConfig.getOptionValue(ResponseBufferSize));
        } catch (NumberFormatException nfe) {
            responseBufferSize = Integer
                  .parseInt(ResponseBufferSize.getDefaultValue());
        }

    }



    // ------------------------------------ Methods from ViewDeclarationLanguage
    

    /**
     * <p>
     * Not supported in JSP-based views.
     * </p>
     *
     * @see javax.faces.view.ViewDeclarationLanguage#getComponentMetadata(javax.faces.context.FacesContext, javax.faces.application.Resource)
     */
    @Override
    public BeanInfo getComponentMetadata(FacesContext context, Resource componentResource) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>
     * Not supported in JSP-based views.
     * </p>
     *
     * @see javax.faces.view.ViewDeclarationLanguage#getViewMetadata(javax.faces.context.FacesContext, String)
     */
    @Override
    public ViewMetadata getViewMetadata(FacesContext context, String viewId) {
        return null;
    }

    
    /**
     * <p>
     * Not supported in JSP-based views.
     * </p>
     *
     * @see javax.faces.view.ViewDeclarationLanguage#getScriptComponentResource(javax.faces.context.FacesContext, javax.faces.application.Resource)
     */
    @Override
    public Resource getScriptComponentResource(FacesContext context, Resource componentResource) {
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.faces.view.ViewDeclarationLanguage#buildView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     * @param context
     * @param view
     * @throws IOException
     */
    public void buildView(FacesContext context, UIViewRoot view)
    throws IOException {

        if (Util.isViewPopulated(context, view)) {
            return;
        }
        try {
            if (executePageToBuildView(context, view)) {
                context.getExternalContext().responseFlushBuffer();
                if (associate != null) {
                    associate.responseRendered();
                }
                context.responseComplete();
                return;
            }
        } catch (IOException e) {
            throw new FacesException(e);
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Completed building view for : \n" +
                                   view.getViewId());
        }
        context.getApplication().publishEvent(context,
                                              PostAddToViewEvent.class,
                                              UIViewRoot.class,
                                              view);
        Util.setViewPopulated(context, view);

    }

    /**
     * @see javax.faces.view.ViewDeclarationLanguage#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     */
    public void renderView(FacesContext context,
                           UIViewRoot view)
    throws IOException {

        // suppress rendering if "rendered" property on the component is
        // false
        if (!view.isRendered() || context.getResponseComplete()) {
            return;
        }

        ExternalContext extContext = context.getExternalContext();

        if (!Util.isViewPopulated(context, view)) {
            buildView(context, view);
        }

        // set up the ResponseWriter

        RenderKitFactory renderFactory = (RenderKitFactory)
              FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit =
              renderFactory.getRenderKit(context, view.getRenderKitId());

        ResponseWriter oldWriter = context.getResponseWriter();

        WriteBehindStateWriter stateWriter =
              new WriteBehindStateWriter(extContext.getResponseOutputWriter(),
                                         context,
                                         responseBufferSize);
        ResponseWriter newWriter;
        if (null != oldWriter) {
            newWriter = oldWriter.cloneWithWriter(stateWriter);
        } else {
            newWriter = renderKit.createResponseWriter(stateWriter,
                                                       null,
                                                       extContext.getRequestCharacterEncoding());
        }
        context.setResponseWriter(newWriter);

        //  Don't call startDoc and endDoc on a partial response
        if (context.getPartialViewContext().isPartialRequest()) {
            doRenderView(context, view);
            try {
                extContext.getFlash().doPostPhaseActions(context);
            } catch (UnsupportedOperationException uoe) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("ExternalContext.getFlash() throw UnsupportedOperationException -> Flash unavailable");
                }
            }
        } else {
            // render the view to the response
            newWriter.startDocument();
            doRenderView(context, view);
            try {
                extContext.getFlash().doPostPhaseActions(context);
            } catch (UnsupportedOperationException uoe) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("ExternalContext.getFlash() throw UnsupportedOperationException -> Flash unavailable");
                }
            }
            newWriter.endDocument();
        }


        // replace markers in the body content and write it to response.

        // flush directly to the response
        if (stateWriter.stateWritten()) {
            stateWriter.flushToWriter();
        }

        // clear the ThreadLocal reference.
        stateWriter.release();

        if (null != oldWriter) {
            context.setResponseWriter(oldWriter);
        }

        // write any AFTER_VIEW_CONTENT to the response
        // side effect: AFTER_VIEW_CONTENT removed
        ViewHandlerResponseWrapper wrapper = (ViewHandlerResponseWrapper)
              RequestStateManager.remove(context, RequestStateManager.AFTER_VIEW_CONTENT);
        if (null != wrapper) {
            wrapper.flushToWriter(extContext.getResponseOutputWriter(),
                                  extContext.getResponseCharacterEncoding());
        }

        extContext.responseFlushBuffer();

    }

    @Override
    public StateManagementStrategy getStateManagementStrategy(FacesContext context, String viewId) {
        return null;
    }

    // --------------------------------------- Methods from ViewHandlingStrategy

    
    /**
     * This {@link ViewHandlingStrategy} <em>should</em> be the last one queried
     * and as such we return <code>true</code>.
     *
     * @see com.sun.faces.application.view.ViewHandlingStrategy#handlesViewId(String)
     */
    @Override
    public boolean handlesViewId(String viewId) {

        return true;

    }

    @Override 
    public String getId() {
        return JSP_VIEW_DECLARATION_LANGUAGE_ID;
    }

    // --------------------------------------------------------- Private Methods


    /**
     * Execute the target view.  If the HTTP status code range is
     * not 2xx, then return true to indicate the response should be
     * immediately flushed by the caller so that conditions such as 404
     * are properly handled.
     * @param context the <code>FacesContext</code> for the current request
     * @param viewToExecute the view to build
     * @return <code>true</code> if the response should be immediately flushed
     *  to the client, otherwise <code>false</code>
     * @throws java.io.IOException if an error occurs executing the page
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

        ExternalContext extContext = context.getExternalContext();

        if ("/*".equals(RequestStateManager.get(context, RequestStateManager.INVOCATION_PATH))) {
            throw new FacesException(MessageUtils.getExceptionMessageString(
                  MessageUtils.FACES_SERVLET_MAPPING_INCORRECT_ID));
        }

        String requestURI = viewToExecute.getViewId();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("About to execute view " + requestURI);
        }

        // update the JSTL locale attribute in request scope so that JSTL
        // picks up the locale from viewRoot. This attribute must be updated
        // before the JSTL setBundle tag is called because that is when the
        // new LocalizationContext object is created based on the locale.
        if (extContext.getRequest() instanceof ServletRequest) {
            Config.set((ServletRequest)
            extContext.getRequest(),
                       Config.FMT_LOCALE, context.getViewRoot().getLocale());
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Before dispacthMessage to viewId " + requestURI);
        }

        // save the original response
        Object originalResponse = extContext.getResponse();

        // replace the response with our wrapper
        ViewHandlerResponseWrapper wrapped = getWrapper(extContext);
        extContext.setResponse(wrapped);

        try
        {

          // build the view by executing the page
          extContext.dispatch(requestURI);

          if (LOGGER.isLoggable(Level.FINE)) {
              LOGGER.fine("After dispacthMessage to viewId " + requestURI);
          }
        }
        finally
        {
          // replace the original response
          extContext.setResponse(originalResponse);
        }

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
        RequestStateManager.set(context,
                                RequestStateManager.AFTER_VIEW_CONTENT,
                                wrapped);

        return false;

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
     * @throws java.io.IOException if an error occurs rendering the view to the client
     * @throws javax.faces.FacesException if some error occurs within the framework
     *  processing
     */
    private void doRenderView(FacesContext context,
                              UIViewRoot viewToRender)
    throws IOException {

        if (null != associate) {
            associate.responseRendered();
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "About to render view " + viewToRender.getViewId());
        }

        viewToRender.encodeAll(context);

    }


    /**
     * <p>
     * Simple utility method to wrap the current response with the
     * {@link ViewHandlerResponseWrapper}.
     * </p>
     * @param extContext the {@link ExternalContext} for this request
     * @return the current response wrapped with ViewHandlerResponseWrapper
     */
    private static ViewHandlerResponseWrapper getWrapper(ExternalContext extContext) {

        Object response = extContext.getResponse();
        if (response instanceof HttpServletResponse) {
            return new ViewHandlerResponseWrapper((HttpServletResponse) response);
        }
        throw new IllegalArgumentException();

    }

}

