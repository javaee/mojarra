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

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;

import java.io.Writer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.component.visit.PartialVisitContext;
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
    private Collection<String> executeIds;
    private Collection<String> renderIds;
    private OnOffResponseWrapper onOffResponse = null;
    private Boolean ajaxRequest;
    private Boolean partialRequest;
    private Boolean renderAll = null; 

    private static final String RENDER_ALL_MARKER = "javax.faces.ViewRoot";
    private static final String ORIGINAL_WRITER = "javax.faces.originalWriter";
    private static final String VIEW_STATE_MARKER = "javax.faces.ViewState";


    // ----------------------------------------------------------- Constructors


    public PartialViewContextImpl() {
    }


    // ---------------------------------------------- Methods from PartialViewContext

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
     * @see javax.faces.context.PartialViewContext#isPartialRequest()
     */
    @Override
    public boolean isPartialRequest() {

        assertNotReleased();
        if (partialRequest == null) {
            partialRequest = isAjaxRequest() ||
                FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap()
                .containsKey("javax.faces.partial");
        }
        return partialRequest;

    }

    public boolean isExecuteNone() {

        assertNotReleased();
        String execute = FacesContext.getCurrentInstance().
            getExternalContext().getRequestParameterMap()
              .get(PARTIAL_EXECUTE_PARAM_NAME);
        return (NO_PARTIAL_PHASE_CLIENT_IDS.equals(execute));

    }

    /**
     * @see javax.faces.context.PartialViewContext#isExecuteAll()
     */
    @Override
    public boolean isExecuteAll() {

        assertNotReleased();
        String execute = FacesContext.getCurrentInstance().
            getExternalContext().getRequestParameterMap()
                .get(PARTIAL_EXECUTE_PARAM_NAME);
        return (ALL_PARTIAL_PHASE_CLIENT_IDS.equals(execute));

    }

    /**
     * @see javax.faces.context.PartialViewContext#isRenderAll()
     */
    @Override
    public boolean isRenderAll() {

        assertNotReleased();
        String render = FacesContext.getCurrentInstance().
            getExternalContext().getRequestParameterMap()
                .get(PARTIAL_RENDER_PARAM_NAME);
        renderAll = (ALL_PARTIAL_PHASE_CLIENT_IDS.equals(render));

        return renderAll;

    }

     
    /**
     * @see javax.faces.context.PartialViewContext#setRenderAll(boolean) 
     */
    @Override
    public void setRenderAll(boolean renderAll) {

        this.renderAll = renderAll;

    }


    public boolean isRenderNone() {

        assertNotReleased();
        String render = FacesContext.getCurrentInstance().
            getExternalContext().getRequestParameterMap()
            .get(PARTIAL_RENDER_PARAM_NAME);
        return (NO_PARTIAL_PHASE_CLIENT_IDS.equals(render));

    }

    /**
     * @see javax.faces.context.PartialViewContext#getExecuteIds()
     */
    @Override
    public Collection<String> getExecuteIds() {

        assertNotReleased();
        if (executeIds != null) {
            return executeIds;
        }
        executeIds = populatePhaseClientIds(PARTIAL_EXECUTE_PARAM_NAME);
        return executeIds;

    }

    /**
     * @see javax.faces.context.PartialViewContext#getRenderIds()
     */
    @Override
    public Collection<String> getRenderIds() {

        assertNotReleased();
        if (renderIds != null) {
            return renderIds;
        }
        renderIds = populatePhaseClientIds(PARTIAL_RENDER_PARAM_NAME);
        return renderIds;

    }

    /**
     * @see javax.faces.context.PartialViewContext#processPartial(
     *  javax.faces.context.FacesContext,javax.faces.event.PhaseId))
     */
    @Override
    public void processPartial(FacesContext context, PhaseId phaseId) {
        Collection <String> executeIds = getExecuteIds();
        Collection <String> renderIds = getRenderIds();
        UIViewRoot viewRoot = context.getViewRoot();

        if (phaseId == PhaseId.APPLY_REQUEST_VALUES ||
            phaseId == PhaseId.PROCESS_VALIDATIONS ||
            phaseId == PhaseId.UPDATE_MODEL_VALUES) {

            // Skip this processing if "none" is specified in the render list,
            // or there were no execute phase client ids.

            if (executeIds == null || executeIds.isEmpty()) {
                // PENDING LOG ERROR OR WARNING
                return;
            }

            try {
                processComponents(viewRoot, phaseId, executeIds, context);
            } catch (Exception e) {
                // PENDING LOG EXCEPTION
            }

            // If we have just finished APPLY_REQUEST_VALUES phase, install the
            // partial response writer.  We want to make sure that any content
            // or errors generated in the other phases are written using the
            // partial response writer.
            //
            if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                ResponseWriter writer = getPartialResponseWriter();
                context.setResponseWriter(writer);
            }

        } else if (phaseId == PhaseId.RENDER_RESPONSE) {

            try {
                //
                // We re-enable response writing.
                //
                OnOffResponseWrapper onOffResponse = new OnOffResponseWrapper(context);
                onOffResponse.setEnabled(true);
                ResponseWriter writer = getPartialResponseWriter();
                ResponseWriter orig = context.getResponseWriter();
                context.getAttributes().put(ORIGINAL_WRITER, orig);
                context.setResponseWriter(writer);

                ExternalContext exContext = context.getExternalContext();
                if (exContext.getResponse() instanceof HttpServletResponse) {
                    exContext.setResponseContentType("text/xml");
                    exContext.setResponseHeader("Cache-Control", "no-cache");
                    writer.startElement("partial-response", viewRoot);
                    writer.startElement("changes", viewRoot);
                }

                if (isRenderAll()) {
                    renderAll(context, viewRoot);
                    renderState(context, viewRoot);
                    writer.endElement("changes");
                    writer.endElement("partial-response");
                    return;
                }

                // Skip this processing if "none" is specified in the render list,
                // or there were no render phase client ids.
                if (renderIds == null || renderIds.isEmpty()) {
                } else {
                    processComponents(viewRoot, phaseId, renderIds, context);
                }

                renderState(context, viewRoot);

                writer.endElement("changes");
                writer.endElement("partial-response");
            } catch (IOException ex) {
                this.cleanupAfterView(context);
            } catch (RuntimeException ex) {
                this.cleanupAfterView(context);
                // Throw the exception
                throw ex;
            }
        }
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
        executeIds = null;
        renderIds = null;
        onOffResponse = null;
    }

    // -------------------------------------------------------- Private Methods



    private Collection<String> populatePhaseClientIds(String parameterName) {

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

    // Process the components specified in the phaseClientIds list
    private void processComponents(UIComponent component, PhaseId phaseId,
        Collection<String> phaseClientIds, FacesContext context) throws IOException {

        // We use the tree visitor mechanism to locate the components to
        // process.  Create our (partial) VisitContext and the
        // VisitCallback that will be invoked for each component that
        // is visited.  Note that we use the SKIP_UNRENDERED hint as we
        // only want to visit the rendered subtree.
        EnumSet hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        PartialVisitContext visitContext =
            new PartialVisitContext(context, phaseClientIds, hints);
        PhaseAwareVisitCallback visitCallback =
            new PhaseAwareVisitCallback(phaseId);
        component.visitTree(visitContext, visitCallback);
    }

    private void renderAll(FacesContext context, UIViewRoot viewRoot) throws IOException {
        // If this is a "render all via ajax" request,
        // make sure to wrap the entire page in a <render> elemnt
        // with the special id of VIEW_ROOT_ID.  This is how the client
        // JavaScript knows how to replace the entire document with
        // this response.
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("update", viewRoot);
        writer.writeAttribute("id", RENDER_ALL_MARKER, "id");

        writer.write("<![CDATA[");

        Iterator<UIComponent> itr = viewRoot.getFacetsAndChildren();
        while (itr.hasNext()) {
            UIComponent kid = (UIComponent)itr.next();
            kid.encodeAll(context);
        }

        writer.write("]]>");
        writer.endElement("update");
    }

    private void renderState(FacesContext context, UIViewRoot viewRoot) throws IOException {
        // Get the view state and write it to the response..
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("update", viewRoot);
        writer.writeAttribute("id", VIEW_STATE_MARKER, "id");
        String state = context.getApplication().getStateManager().getViewState(context);
        writer.write("<![CDATA[" + state + "]]>");
        writer.endElement("update");
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

    private void cleanupAfterView(FacesContext context) {
        ResponseWriter orig = (ResponseWriter) context.getAttributes().
            get(ORIGINAL_WRITER);
        assert(null != orig);
        // move aside the PartialResponseWriter
        context.setResponseWriter(orig);
    }


    @SuppressWarnings({"FinalPrivateMethod"})
    private final void assertNotReleased() {
        if (released) {
            throw new IllegalStateException();
        }
    }

    // ----------------------------------------------------------- Inner Classes

    private static class PhaseAwareVisitCallback implements VisitCallback {

        private PhaseId curPhase = null;
        private PhaseAwareVisitCallback(PhaseId curPhase) {
            this.curPhase = curPhase;
        }  


        public VisitResult visit(VisitContext context,
                                 UIComponent comp) {
            try {
                FacesContext facesContext = context.getFacesContext();

                if (curPhase == PhaseId.APPLY_REQUEST_VALUES) {

                    // RELEASE_PENDING handle immediate request(s)
                    // If the user requested an immediate request
                    // Make sure to set the immediate flag here.

                    comp.processDecodes(facesContext);
                } else if (curPhase == PhaseId.PROCESS_VALIDATIONS) {
                    comp.processValidators(facesContext);
                } else if (curPhase == PhaseId.UPDATE_MODEL_VALUES) {
                    comp.processUpdates(facesContext);
                } else if (curPhase == PhaseId.RENDER_RESPONSE) {

                    ResponseWriter writer = facesContext.getResponseWriter();

                    writer.startElement("update", comp);
                    writer.writeAttribute("id", comp.getClientId(facesContext), "id");
                    try {
                        writer.write("<![CDATA[");

                        // do the default behavior...
                        comp.encodeAll(facesContext);

                        writer.write("]]>");
                    }
                    catch (Exception ce) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.severe(ce.toString());
                        }
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE,
                            ce.toString(),
                            ce);
                        }
                    }
                    writer.endElement("update");
                }
                else {
                    throw new IllegalStateException("I18N: Unexpected " +
                                                    "PhaseId passed to " +
                                              " PhaseAwareContextCallback: " +
                                                    curPhase.toString());
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }

            // Once we visit a component, there is no need to visit
            // its children, since processDecodes/Validators/Updates and
            // encodeAll() already traverse the subtree.  We return
            // VisitResult.REJECT to supress the subtree visit.
            return VisitResult.REJECT;
        }
    }

} // end of class PartialViewContextImpl
