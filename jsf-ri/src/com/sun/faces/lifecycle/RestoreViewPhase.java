/*
 * $Id: RestoreViewPhase.java,v 1.53.8.3 2008/04/12 16:32:30 edburns Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

// RestoreViewPhase.java

package com.sun.faces.lifecycle;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.DebugUtil;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import javax.faces.event.AfterAddToParentEvent;

/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RestoreViewPhase.java,v 1.53.8.3 2008/04/12 16:32:30 edburns Exp $
 */

public class RestoreViewPhase extends Phase {

    private static final String WEBAPP_ERROR_PAGE_MARKER =
            "javax.servlet.error.message";

    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();

    private WebConfiguration webConfig;


    // ---------------------------------------------------------- Public Methods


    public PhaseId getId() {

        return PhaseId.RESTORE_VIEW;

    }


    public void doPhase(FacesContext context,
                        Lifecycle lifecycle,
                        ListIterator<PhaseListener> listeners) {

        Util.getViewHandler(context).initView(context);
        super.doPhase(context, lifecycle, listeners);

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
            doPerComponentActions(facesContext, viewRoot);
            if (!isPostback(facesContext)) {
                facesContext.renderResponse();
            }
            return;
        }

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
            Object request = facesContext.getExternalContext().getRequest();
            if (request instanceof HttpServletRequest) {
                viewId = ((HttpServletRequest) request).getServletPath();
            }
        }

        if (viewId == null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("viewId is null");
            }
            throw new FacesException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
        }

        boolean isPostBack = (isPostback(facesContext) && !isErrorPage(facesContext));
        if (isPostBack) {
            // try to restore the view
            ViewHandler viewHandler = Util.getViewHandler(facesContext);
            viewRoot = viewHandler.restoreView(facesContext, viewId);

            if (viewRoot == null) {
                if (is11CompatEnabled(facesContext)) {
                    // 1.1 -> create a new view and flag that the response should
                    //        be immediately rendered
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
            doPerComponentActions(facesContext, viewRoot);

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Postback: Restored view for " + viewId);
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("New request: creating a view for " + viewId);
            }
            // if that fails, create one
            viewRoot = (Util.getViewHandler(facesContext)).
                  createView(facesContext, viewId);
            viewRoot.subscribeToEvent(AfterAddToParentEvent.class, viewRoot);
            facesContext.setViewRoot(viewRoot);
            facesContext.renderResponse();
            facesContext.getApplication().publishEvent(AfterAddToParentEvent.class,
                                                       viewRoot);
        }
        assert(null != viewRoot);
        

        if (isPostBack && LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "+=+=+=+=+=+= Restored View Printout for " + viewId);
            DebugUtil.printTree(viewRoot, LOGGER, Level.FINEST);
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Exiting RestoreViewPhase");
        }

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Do any per-component actions necessary during reconstitute</p>
     * @param context the <code>FacesContext</code> for the current request
     * @param uic the <code>UIComponent</code> to process the
     *  <code>binding</code> attribute
     */
    protected void doPerComponentActions(FacesContext context,
                                         UIComponent uic) {

        // if this component has a component value reference expression,
        // make sure to populate the ValueExpression for it.
        ValueExpression valueExpression;
        if (null != (valueExpression = uic.getValueExpression("binding"))) {
            valueExpression.setValue(context.getELContext(), uic);
        }

        for (Iterator<UIComponent> kids =  uic.getFacetsAndChildren();
             kids.hasNext(); ) {
            doPerComponentActions(context, kids.next());
        }

    }

    // --------------------------------------------------------- Private Methods


    /**
     * @param context the <code>FacesContext</code> for the current request
     * @return <code>true</code> if the {@link ResponseStateManager#isPostback(javax.faces.context.FacesContext)}
     *  returns <code>true</code> <em>and</em> the request doesn't contain the
     *  attribute <code>javax.servlet.error.message</code> which indicates we've been
     *  forwarded to an error page.
     */

    private boolean isPostback(FacesContext context) {

        // Get the renderKitId by calling viewHandler.calculateRenderKitId().
        String renderkitId =
              context.getApplication().getViewHandler().
                    calculateRenderKitId(context);
        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context,
                                                     renderkitId);
        return rsm.isPostback(context);

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
