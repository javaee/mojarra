 /*
 * $Id: PartialViewContextImpl.java,v 1.93.2.4 2008/04/09 08:59:06 edburns Exp $
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

package com.sun.faces.context;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletResponse;

import java.io.Writer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.OnOffResponseWrapper;
import com.sun.faces.util.Util;

public class PartialViewContextImpl extends PartialViewContext {

    // Log instance for this class
    private static Logger LOGGER = FacesLogger.CONTEXT.getLogger();

    private boolean released;

    // BE SURE TO ADD NEW IVARS TO THE RELEASE METHOD
    private ResponseWriter partialResponseWriter = null;
    private Map<Object,Object> attributes;
    private List<String> executePhaseClientIds;
    private List<String> renderPhaseClientIds;
    private OnOffResponseWrapper onOffResponse = null;
    private Boolean ajaxRequest;
    private Boolean renderAll;

    // ----------------------------------------------------------- Constructors


    public PartialViewContextImpl() {
    }


    // ---------------------------------------------- Methods from PartialViewContext

    /**
     * @see javax.faces.context.PartialViewContext#enableResponseWriting(boolean) 
     */
    @Override
    public void enableResponseWriting(boolean enable) {

        assertNotReleased();
        if (onOffResponse == null) {
            onOffResponse = new OnOffResponseWrapper(FacesContext.getCurrentInstance());
        }
        onOffResponse.setEnabled(enable);
        
    }


    /**
     * @see javax.faces.context.PartialViewContext#isAjaxRequest()
     */
    @Override
    public boolean isAjaxRequest() {

        assertNotReleased();
        if (ajaxRequest == null) {
            ajaxRequest = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap()
                  .containsKey("javax.faces.partial.ajax");
        }
        return ajaxRequest;

    }

    /**
     * @see javax.faces.context.PartialViewContext#isExecuteNone()
     */
    @Override
    public boolean isExecuteNone() {

        assertNotReleased();
        String execute = FacesContext.getCurrentInstance().
            getExternalContext().getRequestParameterMap()
              .get(PARTIAL_EXECUTE_PARAM_NAME);
        return (NO_PARTIAL_PHASE_CLIENT_IDS.equals(execute));

    }


    /**
     * @see javax.faces.context.PartialViewContext#isRenderAll()
     */
    @Override
    public boolean isRenderAll() {

        assertNotReleased();
        if (renderAll == null) {
            renderAll = (isAjaxRequest()
                           && !isRenderNone()
                           && getRenderPhaseClientIds().isEmpty());
        }

        return renderAll;

    }

     
    /**
     * @see javax.faces.context.PartialViewContext#setRenderAll(boolean) 
     */
    @Override
    public void setRenderAll(boolean renderAll) {

        this.renderAll = renderAll;

    }


    /**
     * @see javax.faces.context.PartialViewContext#isRenderNone()
     */
    @Override
    public boolean isRenderNone() {

        assertNotReleased();
        String render = FacesContext.getCurrentInstance().
            getExternalContext().getRequestParameterMap()
            .get(PARTIAL_RENDER_PARAM_NAME);
        return (NO_PARTIAL_PHASE_CLIENT_IDS.equals(render));

    }


    @Override
    public Map<Object,Object> getAttributes() {
        
        assertNotReleased();
        if (attributes == null) {
            attributes = new HashMap<Object,Object>();
        }
        return attributes;

    }

    /**
     * @see javax.faces.context.PartialViewContext#getExecutePhaseClientIds()
     */
    @Override
    public List<String> getExecutePhaseClientIds() {

        assertNotReleased();
        if (executePhaseClientIds != null) {
            return executePhaseClientIds;
        }
        executePhaseClientIds = populatePhaseClientIds(PARTIAL_EXECUTE_PARAM_NAME);
        return executePhaseClientIds;

    }


    /**
     * @see javax.faces.context.PartialViewContext#setExecutePhaseClientIds(java.util.List)
     */
    @Override
    public void setExecutePhaseClientIds(List<String>executePhaseClientIds) {

        assertNotReleased();
        this.executePhaseClientIds = executePhaseClientIds;

    }

    /**
     * @see javax.faces.context.PartialViewContext#getRenderPhaseClientIds()
     */
    @Override
    public List<String> getRenderPhaseClientIds() {

        assertNotReleased();
        if (renderPhaseClientIds != null) {
            return renderPhaseClientIds;
        }
        renderPhaseClientIds = populatePhaseClientIds(PARTIAL_RENDER_PARAM_NAME);
        return renderPhaseClientIds;

    }


    /**
     * @see javax.faces.context.PartialViewContext#setRenderPhaseClientIds(java.util.List)
     */
    @Override
    public void setRenderPhaseClientIds(List<String>renderPhaseClientIds) {

        assertNotReleased();
        this.renderPhaseClientIds = renderPhaseClientIds;

    }

    /**
     * @see javax.faces.context.PartialViewContext#getPartialResponseWriter()
     */
    @Override
    public ResponseWriter getPartialResponseWriter() {
        assertNotReleased();
        if (partialResponseWriter == null) {
            partialResponseWriter = createPartialResponseWriter();
        }
        return partialResponseWriter;
    }

    /**
     * @see javax.faces.context.PartialViewContext#release()
     */
    public void release() {
        
        released = true;
        ajaxRequest = null;
        renderAll = null;
        partialResponseWriter = null;
        executePhaseClientIds = null;
        renderPhaseClientIds = null;
        onOffResponse = null;
        if (attributes != null) {
            attributes.clear();
            attributes = null;
        }
         
    }


    // -------------------------------------------------------- Private Methods



    private List<String> populatePhaseClientIds(String parameterName) {

        Map<String,String> requestParamMap =
              FacesContext.getCurrentInstance().
              getExternalContext().getRequestParameterMap();

        String param = requestParamMap.get(parameterName);
        if (param == null || NO_PARTIAL_PHASE_CLIENT_IDS.equals(param)) {
            return Collections.emptyList();
        } else {
            String[] pcs = Util.split(param, ",[ \t]*");
            return ((pcs != null && pcs.length != 0)
                    ? new ArrayList<String>(Arrays.asList(pcs))
                    : Collections.<String>emptyList());
        }
        
    }


    private ResponseWriter createPartialResponseWriter() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        HttpServletResponse response = (HttpServletResponse)
              extContext.getResponse();
        String encoding = extContext.getRequestCharacterEncoding();
        response.setCharacterEncoding(encoding);
        ResponseWriter responseWriter = null;
        Writer out = null;
        try {
            out = response.getWriter();
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           ioe.toString(),
                           ioe);
            }
        }

        if (out != null) {
            responseWriter =
                ctx.getRenderKit().createResponseWriter(out,
                "text/xml", encoding);
        }
        return responseWriter;
    }

    @SuppressWarnings({"FinalPrivateMethod"})
    private final void assertNotReleased() {
        if (released) {
            throw new IllegalStateException();
        }
    }


} // end of class PartialViewContextImpl
