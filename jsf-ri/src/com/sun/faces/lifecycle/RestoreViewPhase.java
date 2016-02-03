/*
 * $Id: RestoreViewPhase.java,v 1.16.30.3 2007/04/27 21:27:43 ofung Exp $
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

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.util.Util;


/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RestoreViewPhase.java,v 1.16.30.3 2007/04/27 21:27:43 ofung Exp $
 */

public class RestoreViewPhase extends Phase {

    // Log instance for this class
    protected static Log log = LogFactory.getLog(RestoreViewPhase.class);

    /**
     * <p>Store the value of <code>DEFAULT_SUFFIX_PARAM_NAME</code>
     * or, if that isn't defined, the value of <code>DEFAULT_SUFFIX</code>
     */
    private String contextDefaultSuffix;
   

    public PhaseId getId() {
        return PhaseId.RESTORE_VIEW;
    }


    /**
     * PRECONDITION: the necessary factories have been installed in the
     * ServletContext attr set. <P>
     * <p/>
     * POSTCONDITION: The facesContext has been initialized with a tree.
     */

    public void execute(FacesContext facesContext) throws FacesException {
        if (log.isDebugEnabled()) {
            log.debug("Entering RestoreViewPhase");
        }
        if (null == facesContext) {
            throw new FacesException(Util.getExceptionMessageString(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot != null) {
            if (log.isDebugEnabled()) {
                log.debug("Found a pre created view in FacesContext");
            }
            Locale locale = facesContext.getExternalContext().getRequestLocale();
            facesContext.getViewRoot().setLocale(locale);
            doPerComponentActions(facesContext, viewRoot);
            return;
        }

        // Reconstitute or create the request tree
        String viewId = getViewId(facesContext);
        if (viewId == null) {
            if (log.isDebugEnabled()) {
                log.debug("requestPath is null");
            }
            throw new FacesException(Util.getExceptionMessageString(
                Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
        }

        // try to restore the view
        if (null == (viewRoot = (Util.getViewHandler(facesContext)).
            restoreView(facesContext, viewId))) {

            if (log.isDebugEnabled()) {
                log.debug("New request: creating a view for " + viewId);
            }
            // if that fails, create one
            viewRoot = (Util.getViewHandler(facesContext)).
                createView(facesContext, viewId);
            facesContext.renderResponse();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Postback: Restored view for " + viewId);
            }
        }
        Util.doAssert(null != viewRoot);

        facesContext.setViewRoot(viewRoot);
        doPerComponentActions(facesContext, viewRoot);

        if (log.isDebugEnabled()) {
            log.debug("Exiting RestoreViewPhase");
        }
    }


    /**
     * <p>Do any per-component actions necessary during reconstitute</p>
     * @param context the <code>FacesContext</code> for the current request
     * @param uic top level <code>UIComponent</code> 
     */
    protected void doPerComponentActions(FacesContext context, UIComponent uic) {
        Iterator kids = uic.getFacetsAndChildren();
        while (kids.hasNext()) {
            doPerComponentActions(context, (UIComponent) kids.next());
        }
      
        // if this component has a component value reference expression,
        // make sure to populate the ValueBinding for it.
        ValueBinding valueBinding;
        if (null != (valueBinding = uic.getValueBinding("binding"))) {
            valueBinding.setValue(context, uic);
        }
    }


    /**
     * <p>Adjust the viewID per the requirements of Section 2.2.1.</p>
     *
     * @param context current {@link FacesContext}
     * @param viewId  incoming view ID
     * @return the view ID with an altered suffix mapping (if necessary)
     */
    private String convertViewId(FacesContext context, String viewId) {

        if (viewId == null) {
            return null;
        }
                
        initDefaultSuffix(context);
               
        String mapping = Util.getFacesMapping(context);
        boolean isPrefixMapped = Util.isPrefixMapped(mapping);
        ExternalContext extContext = context.getExternalContext();
        
         // maping could be null if a non-faces request triggered
        // this response.
        if (extContext.getRequestPathInfo() == null && mapping != null &&
            Util.isPrefixMapped(mapping)) {
            // this was probably an initial request
            // send them off to the root of the web application
            try {
                context.responseComplete();
                if (log.isTraceEnabled()) {
                    log.trace("Response Complete for" + viewId);
                }
                extContext.redirect(extContext.getRequestContextPath());
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }
        }
        
        String convertedViewId = null;
        if (mapping != null && !isPrefixMapped) {
            // if the viewId doesn't already use the above suffix,
            // replace or append.
            if (!viewId.endsWith(contextDefaultSuffix)) {
                StringBuffer buffer = new StringBuffer(viewId);
                int extIdx = viewId.lastIndexOf('.');
                if (extIdx != -1) {
                    buffer.replace(extIdx, viewId.length(),
                                   contextDefaultSuffix);
                } else {
                    // no extension in the provided viewId, append the suffix
                    buffer.append(contextDefaultSuffix);
                }
                convertedViewId = buffer.toString();
                if (log.isTraceEnabled()) {
                    log.trace("viewId after appending the context suffix " +
                    		convertedViewId);
                }

            }
        } else {
            convertedViewId = viewId.substring((viewId.indexOf(mapping) + 1));
        }
                      
        return convertedViewId;
    }

    /**
     * <p>Initialize the default extension suffix that will be used
     * in view ID adjustments.</p>
     * @param context the <code>FacesContext</code> for the current request
     */
    private void initDefaultSuffix(FacesContext context) {
        if (contextDefaultSuffix == null) {
            synchronized (this) {
                if (contextDefaultSuffix == null) {
                    contextDefaultSuffix =
                          context.getExternalContext().
                                getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
                    if (contextDefaultSuffix == null) {
                        contextDefaultSuffix = ViewHandler.DEFAULT_SUFFIX;
                    }
                    if (log.isTraceEnabled()) {
                        log.trace("contextDefaultSuffix "
                        		+ contextDefaultSuffix);
                    }
                }
            }
        }
    }


    /**
     * <p>Obtain the View ID for this request per section 2.2.1.</p>
     * @param context the <code>FacesContext</code> for the current request
     * @return the view ID for this request
     */
   private String getViewId(FacesContext context) {
        // Reconstitute or create the request tree
        Map requestMap = context.getExternalContext().getRequestMap();
        String requestPath = (String)
            requestMap.get("javax.servlet.include.path_info");
        if (requestPath == null) {
            requestPath = context.getExternalContext().getRequestPathInfo();
        }
        
        // It could be that this request was mapped using
        // a prefix mapping in which case there would be no
        // path_info.  Query the servlet path.
        if (requestPath == null) {
            requestPath = (String)
                requestMap.get("javax.servlet.include.servlet_path");
        }

        if (requestPath == null) {
            Object request = context.getExternalContext().getRequest();
            if (request instanceof HttpServletRequest) {
                requestPath = ((HttpServletRequest) request).getServletPath();
            }
        }
        
        return convertViewId(context, requestPath);
    }
    

} // end of class RestoreViewPhase
