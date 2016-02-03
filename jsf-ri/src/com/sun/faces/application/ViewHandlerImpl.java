/* 
 * $Id: ViewHandlerImpl.java,v 1.45.12.7 2007/04/27 21:27:38 ofung Exp $ 
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


// ViewHandlerImpl.java 

package com.sun.faces.application;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.jstl.core.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

/**
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler.
 *
 * @version $Id: ViewHandlerImpl.java,v 1.45.12.7 2007/04/27 21:27:38 ofung Exp $
 * @see javax.faces.application.ViewHandler
 */
public class ViewHandlerImpl extends ViewHandler {

    //
    // Private/Protected Constants
    //
    private static final Log log = LogFactory.getLog(ViewHandlerImpl.class);


    public ViewHandlerImpl() {
        if (log.isDebugEnabled()) {
            log.debug("Created ViewHandler instance ");
        }
    }


    /**
     * @see ViewHandler#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)     
     */
    public void renderView(FacesContext context,
                           UIViewRoot viewToRender) throws IOException,
        FacesException {

        if (null == context || null == viewToRender) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " context " + context + " viewToRender " + 
                viewToRender;
            throw new NullPointerException(message);
        }
	
        ApplicationAssociate associate = ApplicationAssociate.getInstance(context.getExternalContext());
	
        if (null != associate) {
            associate.responseRendered();
        }
        String requestURI = viewToRender.getViewId();
        if (log.isDebugEnabled()) {
            log.debug("About to render view " + requestURI);
        }

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

        extContext.dispatch(requestURI);
    }

    /**
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)     
     */
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        if (context == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" context " + context;
            throw new NullPointerException(message);
        }

        ExternalContext extContext = context.getExternalContext();

        // set the request character encoding. NOTE! This MUST be done
        // before any request praameter is accessed.

        /*
        HttpServletRequest request =
            (HttpServletRequest) extContext.getRequest();
        */
        Map headerMap = extContext.getRequestHeaderMap();
        String
            contentType = null,
            charEnc = null;

        // look for a charset in the Content-Type header first.
        if (null != (contentType = (String) headerMap.get("Content-Type"))) {
            // see if this header had a charset
            String charsetStr = "charset=";
            int
                len = charsetStr.length(),
                i = 0;

            // if we have a charset in this Content-Type header AND it
            // has a non-zero length.
            if (-1 != (i = contentType.indexOf(charsetStr)) &&
                (i + len < contentType.length())) {
                charEnc = contentType.substring(i + len);
            }
        }
        // failing that, look in the session for a previously saved one
        if (null == charEnc) {
            if (null != extContext.getSession(false)) {
                charEnc = (String) extContext.getSessionMap().get
                    (CHARACTER_ENCODING_KEY);
            }
        }
        if (null != charEnc) {
            try {
                if (log.isTraceEnabled()) {
                    log.trace(
                        "set character encoding on request to " + charEnc);
                }
                Object request = extContext.getRequest();
                if (request instanceof ServletRequest) {
                    ((ServletRequest) request).setCharacterEncoding(charEnc);
                }
            } catch (java.io.UnsupportedEncodingException uee) {
                if (log.isErrorEnabled()) {
                    log.error(uee.getMessage(), uee);
                }
                throw new FacesException(uee);
            }
        }

        String renderKitId = getRenderkitId(context);
        UIViewRoot viewRoot = Util.getStateManager(context).restoreView(context,
        		viewId,
        		renderKitId);
        
        return viewRoot;
    }


    /**
     * @see ViewHandler#createView(javax.faces.context.FacesContext, String)     
     */
    public UIViewRoot createView(FacesContext context, String viewId) {
        if (context == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
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
            context.getApplication().
                createComponent(UIViewRoot.COMPONENT_TYPE);
        result.setViewId(viewId);
        if (log.isDebugEnabled()) {
            log.debug("Created new view for " + viewId);
        }

        // if there was no locale from the previous view, calculate the locale 
        // for this view.
        if (locale == null) {
            locale =
                context.getApplication().getViewHandler().calculateLocale(
                    context);
            if (log.isDebugEnabled()) {
                log.debug("Locale for this view as determined by calculateLocale "
                          + locale.toString());
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug(
                    "Using locale from previous view " + locale.toString());
            }
        }

        if (renderKitId == null) {
            renderKitId =
                context.getApplication().getViewHandler().calculateRenderKitId(
                    context);
            if (log.isDebugEnabled()) {
                log.debug("RenderKitId for this view as determined by calculateRenderKitId "
                          + renderKitId);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug(
                    "Using renderKitId from previous view " + renderKitId);
            }
        }

        result.setLocale(locale);
        result.setRenderKitId(renderKitId);

        return result;
    }


    public Locale calculateLocale(FacesContext context) {

        if (context == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +"context " + context;
            throw new NullPointerException(message);
        }

        Locale result = null;
        // determine the locales that are acceptable to the client based on the 
        // Accept-Language header and the find the best match among the 
        // supported locales specified by the client.
        Iterator locales = context.getExternalContext().getRequestLocales();
        while (locales.hasNext()) {
            Locale perf = (Locale) locales.next();
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
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +"context " + context;
            throw new NullPointerException(message);
        }
        String result = null;

        if (null ==
            (result = context.getApplication().getDefaultRenderKitId())) {
            result = RenderKitFactory.HTML_BASIC_RENDER_KIT;
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
        Iterator it = context.getApplication().getSupportedLocales();
        while (it.hasNext()) {
            Locale supportedLocale = (Locale) it.next();

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
            String message = Util.getExceptionMessageString
                    (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +"context " + context;
            throw new NullPointerException(message);
        }
        
        if (log.isTraceEnabled()) {
            log.trace("Begin writing state to response for viewId" +
                    context.getViewRoot().getViewId());
        }
        context.getResponseWriter().writeText(
                RIConstants.SAVESTATE_FIELD_MARKER, null);
        if (log.isTraceEnabled()) {
            log.trace("End writing state to response for viewId" +
                    context.getViewRoot().getViewId());
        }
        
    }


    public String getActionURL(FacesContext context, String viewId) {

        if (context == null || viewId == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +"context " + context + " viewId " + viewId;
            throw new NullPointerException(message);
        }

        if (viewId.charAt(0) != '/') {
            String message = Util.getExceptionMessageString(Util.ILLEGAL_VIEW_ID_ID,
                                                      new Object[]{viewId});
            if (log.isErrorEnabled()) {
                log.error(message + " " + viewId);
            }
            throw new IllegalArgumentException(message);
        }
        
        // Acquire the context path, which we will prefix on all results
        String contextPath =
            context.getExternalContext().getRequestContextPath();

        // Acquire the mapping used to execute this request (if any)
        String mapping = Util.getFacesMapping(context);

        // If no mapping can be identified, just return a server-relative path
        if (mapping == null) {
            return contextPath + viewId;
        }

        // Deal with prefix mapping
        if (Util.isPrefixMapped(mapping)) {
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
     * <p>Special handling of getRenderKitId for decorated
     * implementations.</p>
     * @param context the <code>FacesContext</code> for the current request
     * @return the calculated RenderKit ID
     */
    private String getRenderkitId(FacesContext context) {
        // this is necessary to allow decorated impls.        
        return context.getApplication().getViewHandler()
              .calculateRenderKitId(context);
    }
    
}
