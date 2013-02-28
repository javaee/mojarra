/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

// RestoreViewPhase.java

package com.sun.faces.lifecycle;

import java.util.EnumSet;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.el.MethodExpression;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.faces.application.ProtectedViewException;
import javax.faces.component.visit.VisitCallback;
import javax.faces.context.ExternalContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostRestoreStateEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.flow.FlowHandler;
import javax.faces.render.ResponseStateManager;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewMetadata;

/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 */

public class RestoreViewPhase extends Phase {

    private static final String WEBAPP_ERROR_PAGE_MARKER =
            "javax.servlet.error.message";

    private static final Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();

    private WebConfiguration webConfig;

    private static String SKIP_ITERATION_HINT =
        "javax.faces.visit.SKIP_ITERATION";


    // ---------------------------------------------------------- Public Methods


    public PhaseId getId() {

        return PhaseId.RESTORE_VIEW;

    }


    public void doPhase(FacesContext context,
                        Lifecycle lifecycle,
                        ListIterator<PhaseListener> listeners) {

        Util.getViewHandler(context).initView(context);
        super.doPhase(context, lifecycle, listeners);

        // Notify View Root after phase listener (if registered)
        notifyAfter(context, lifecycle);
    }

    /**
     * PRECONDITION: the necessary factories have been installed in the
     * ServletContext attr set. <P>
     * <p/>
     * POSTCONDITION: The facesContext has been initialized with a tree.
     */

    public void execute(FacesContext facesContext) throws FacesException {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Entering RestoreViewPhase");
        }
        if (null == facesContext) {
            throw new FacesException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        
        // If an app had explicitely set the tree in the context, use that;
        //
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Found a pre created view in FacesContext");
            }
            facesContext.getViewRoot().setLocale(
                 facesContext.getExternalContext().getRequestLocale());

            // do per-component actions
            deliverPostRestoreStateEvent(facesContext);

            if (!facesContext.isPostback()) {
                facesContext.renderResponse();
            }
            return;
        }
        FacesException thrownException = null;

        try {

            // Reconstitute or create the request tree
            Map requestMap = facesContext.getExternalContext().getRequestMap();
            String viewId = (String)
              requestMap.get("javax.servlet.include.path_info");
            if (viewId == null) {
                viewId = facesContext.getExternalContext().getRequestPathInfo();
            }

            // It could be that this request was mapped using
            // a prefix mapping in which case there would be no
            // path_info.  Query the servlet path.
            if (viewId == null) {
                viewId = (String)
                  requestMap.get("javax.servlet.include.servlet_path");
            }

            if (viewId == null) {
                viewId = facesContext.getExternalContext().getRequestServletPath();
            }

            if (viewId == null) {
                throw new FacesException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
            }

            ViewHandler viewHandler = Util.getViewHandler(facesContext);

            boolean isPostBack = (facesContext.isPostback() && !isErrorPage(facesContext));
            if (isPostBack) {
                facesContext.setProcessingEvents(false);
                // try to restore the view
                viewRoot = viewHandler.restoreView(facesContext, viewId);
                if (viewRoot == null) {
                    if (is11CompatEnabled(facesContext)) {
                        // 1.1 -> create a new view and flag that the response should
                        //        be immediately rendered
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.fine("Postback: recreating a view for " + viewId);
                        }
                        viewRoot = viewHandler.createView(facesContext, viewId);
                        facesContext.renderResponse();

                    } else {
                        Object[] params = {viewId};
                        throw new ViewExpiredException(
                                MessageUtils.getExceptionMessageString(
                                MessageUtils.RESTORE_VIEW_ERROR_MESSAGE_ID,
                                params),
                                viewId);
                    }
                }

                facesContext.setViewRoot(viewRoot);
                facesContext.setProcessingEvents(true);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Postback: restored view for " + viewId);
                }
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("New request: creating a view for " + viewId);
                }
                
                String derivedViewId = viewHandler.deriveLogicalViewId(facesContext, viewId);
                ViewDeclarationLanguage vdl = viewHandler.getViewDeclarationLanguage(facesContext, derivedViewId);
                
                maybeTakeProtectedViewAction(facesContext, viewHandler, vdl, viewId);
                    
                ViewMetadata metadata  = null;
                if (vdl != null) {
                    // If we have one, get the ViewMetadata...
                    metadata = vdl.getViewMetadata(facesContext, viewId);
                    
                    if (metadata != null) { // perhaps it's not supported
                        // and use it to create the ViewRoot.  This will have, at most
                        // the UIViewRoot and its metadata facet.
                        viewRoot = metadata.createMetadataView(facesContext);
                        
                        // Only skip to render response if there is no metadata
                        if (!ViewMetadata.hasMetadata(viewRoot)) {
                            facesContext.renderResponse();
                        }
                    }
                } 
                if (null == vdl || null == metadata) {
                    facesContext.renderResponse();
                }

                if (null == viewRoot) {
                    viewRoot = (Util.getViewHandler(facesContext)).
                        createView(facesContext, viewId);
                }
                facesContext.setViewRoot(viewRoot);
                assert (null != viewRoot);
            }
        } catch (Throwable fe) {
            if (fe instanceof FacesException) {
              thrownException = (FacesException) fe;
            } else {
              thrownException = new FacesException(fe);
            }
        } finally {
            if (null == thrownException) {
                FlowHandler flowHandler = facesContext.getApplication().getFlowHandler();
                if (null != flowHandler) {
                    flowHandler.clientWindowTransition(facesContext);
                }
                
                deliverPostRestoreStateEvent(facesContext);
            } else {
                throw thrownException;
            }
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Exiting RestoreViewPhase");
        }

    }
    
    private void maybeTakeProtectedViewAction(FacesContext context, 
            ViewHandler viewHandler, 
            ViewDeclarationLanguage vdl, String viewId) {
        // http://java.net/jira/browse/JAVASERVERFACES-2204
        // PENDING: this code is optimized to be fast to write.
        // It must be optimized to be fast to run.
        
        // See git clone ssh://edburns@git.java.net/grizzly~git 1_9_36 for
        // how grizzly does this.
        
        Set<String> urlPatterns = viewHandler.getProtectedViewsUnmodifiable();
        // Implement section 12.1 of the Servlet spec.
        boolean currentViewIsProtected = false;
        for (String cur : urlPatterns) {
            if (cur.equals(viewId)) {
                currentViewIsProtected = true;
            }
            if (currentViewIsProtected) {
                break;
            }
        }
        
        if (currentViewIsProtected) {
            ExternalContext extContext = context.getExternalContext();
            Map<String, String> headers = extContext.getRequestHeaderMap();
            if (headers.containsKey("Referer")) {
                String referer = headers.get("Referer");
                boolean refererIsInProtectedSet = false;
                for (String cur : urlPatterns) {
                    if (cur.equals(referer)) {
                        refererIsInProtectedSet = true;
                    }
                    if (refererIsInProtectedSet) {
                        break;
                    }
                }
                if (!refererIsInProtectedSet) {
//                    boolean isAbsoluteURI = referer.matches("^[a-z]+://.*");
//                    if (!isAbsoluteURI) {
//                        // PENDING real implementation must make the value of
//                        // referer suitable for passing to the ctor for URI()
//                    }
                    URI uri = null;
                    boolean hostsMatch = false,
                            portsMatch = false,
                            contextPathsMatch = false,
                            refererOriginatesInThisWebapp = false;
                    try {
                        uri = new URI(referer);
                        hostsMatch = uri.getHost().equals(extContext.getRequestServerName());
                        portsMatch = uri.getPort() == extContext.getRequestServerPort();
                        contextPathsMatch = uri.getPath().contains(extContext.getApplicationContextPath());

                    } catch (URISyntaxException use) {
                        throw new ProtectedViewException(use);
                    }
                    refererOriginatesInThisWebapp = hostsMatch && portsMatch && contextPathsMatch;
                    if (!refererOriginatesInThisWebapp) {
                        String message = FacesLogger.LIFECYCLE.interpolateMessage(context, 
                                "jsf.lifecycle.invalid.referer", new String [] { referer, viewId });
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, message);
                        }
                        // PENDING() use vdl.viewExists() to see if the view originates 
                        // in this app. For now just throw the exception
                        throw new ProtectedViewException(message);
                    }

                }
            } else {
                // We do not have a Referer header.  Fall
                // back to inspecting the incoming request
                String rkId = viewHandler.calculateRenderKitId(context);
                ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context, rkId);
                String incomingSecretKeyValue = extContext.getRequestParameterMap().
                        get(ResponseStateManager.NON_POSTBACK_VIEW_TOKEN_PARAM);
                String correctSecretKeyValue = rsm.getCryptographicallyStrongTokenFromSession(context);
                if (null == incomingSecretKeyValue || 
                    !correctSecretKeyValue.equals(incomingSecretKeyValue)) {
                    throw new ProtectedViewException();
                }
                
            }

        }
        
        
    }
    
    private void deliverPostRestoreStateEvent(FacesContext facesContext) throws FacesException {
        UIViewRoot root = facesContext.getViewRoot();
        final PostRestoreStateEvent postRestoreStateEvent = new PostRestoreStateEvent(root);
        try {
            // PENDING: This is included for those component frameworks that don't utilize the
            // new VisitHint(s) yet - but still wish to know that they should be non-iterating
            // during state saving.  It should be removed at some point.
            facesContext.getAttributes().put(SKIP_ITERATION_HINT, true);
            facesContext.getApplication().publishEvent(facesContext, PostRestoreStateEvent.class, root);

            Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);
            VisitContext visitContext = VisitContext.createVisitContext(facesContext, null, hints);
            root.visitTree(visitContext, new VisitCallback() {

                        public VisitResult visit(VisitContext context, UIComponent target) {
                            postRestoreStateEvent.setComponent(target);
                            target.processEvent(postRestoreStateEvent);
                            //noinspection ReturnInsideFinallyBlock
                            return VisitResult.ACCEPT;
                        }
                    });
        } catch (AbortProcessingException e) {
            facesContext.getApplication().publishEvent(facesContext,
                    ExceptionQueuedEvent.class,
                    new ExceptionQueuedEventContext(facesContext,
                    e,
                    null,
                    PhaseId.RESTORE_VIEW));
        } finally {
            // PENDING: This is included for those component frameworks that don't utilize the
            // new VisitHint(s) yet - but still wish to know that they should be non-iterating
            // during state saving.  It should be removed at some point.
            facesContext.getAttributes().remove(SKIP_ITERATION_HINT);
        }
    }

    // --------------------------------------------------------- Private Methods

    /**
     * Notify afterPhase listener that is registered on the View Root.
     * @param context the FacesContext for the current request
     * @param lifecycle lifecycle instance
     */
    private void notifyAfter(FacesContext context, Lifecycle lifecycle) {
        UIViewRoot viewRoot = context.getViewRoot();
        if (null == viewRoot) {
            return;
        }
        MethodExpression afterPhase = viewRoot.getAfterPhaseListener();
        if (null != afterPhase) {
            try {
                PhaseEvent event = new PhaseEvent(context, PhaseId.RESTORE_VIEW, lifecycle);
                afterPhase.invoke(context.getELContext(), new Object[]{event});
            }
            catch (Exception e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "severe.component.unable_to_process_expression",
                               new Object[] { afterPhase.getExpressionString(),
                               ("afterPhase")});
                }
                return;
            }
        }
    }


    /**
     * The Servlet specification states that if an error occurs
     * in the application and there is a matching error-page declaration,
     * the that original request the cause the error is forwarded
     * to the error page.
     *
     * If the error occurred during a post-back and a matching
     * error-page definition was found, then an attempt to restore
     * the error view would be made as the javax.faces.ViewState
     * marker would still be in the request parameters.
     *
     * Use this method to determine if the current request is
     * an error page to avoid the above condition.
     *
     * @param context the FacesContext for the current request
     * @return <code>true</code> if <code>WEBAPP_ERROR_PAGE_MARKER</code>
     *  is found in the request, otherwise return <code>false</code>
     */
    private static boolean isErrorPage(FacesContext context) {

        return (context.getExternalContext().
                    getRequestMap().get(WEBAPP_ERROR_PAGE_MARKER) != null);

    }


    private WebConfiguration getWebConfig(FacesContext context) {

        if (webConfig == null) {
            webConfig = WebConfiguration.getInstance(context.getExternalContext());
        }
        return webConfig;

    }

    private boolean is11CompatEnabled(FacesContext context) {

        return (getWebConfig(context).isOptionEnabled(
              BooleanWebContextInitParameter.EnableRestoreView11Compatibility));
        
    }

    // The testcase for this class is TestRestoreViewPhase.java

} // end of class RestoreViewPhase
