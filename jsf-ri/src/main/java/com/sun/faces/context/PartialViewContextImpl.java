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

package com.sun.faces.context;

import com.sun.faces.component.visit.PartialVisitContext;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;

import java.io.Writer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import javax.faces.FactoryFinder;
import static javax.faces.FactoryFinder.VISIT_CONTEXT_FACTORY;
import javax.faces.component.visit.VisitContextFactory;
import javax.faces.component.visit.VisitContextWrapper;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

 public class PartialViewContextImpl extends PartialViewContext {

    // Log instance for this class
    private static Logger LOGGER = FacesLogger.CONTEXT.getLogger();

    private boolean released;

    // BE SURE TO ADD NEW IVARS TO THE RELEASE METHOD
    private PartialResponseWriter partialResponseWriter;
    private List<String> executeIds;
    private Collection<String> renderIds;
    private Boolean ajaxRequest;
    private Boolean partialRequest;
    private Boolean renderAll;
    private FacesContext ctx;
    private boolean processingPhases = false;

    private static final String ORIGINAL_WRITER = "com.sun.faces.ORIGINAL_WRITER";


    // ----------------------------------------------------------- Constructors


    public PartialViewContextImpl(FacesContext ctx) {
        this.ctx = ctx;
    }


    // ---------------------------------------------- Methods from PartialViewContext

    /**
     * @see javax.faces.context.PartialViewContext#isAjaxRequest()
     */
    @Override
    public boolean isAjaxRequest() {

        assertNotReleased();
        if (ajaxRequest == null) {
            ajaxRequest = "partial/ajax".equals(ctx.
                getExternalContext().getRequestHeaderMap().get("Faces-Request"));
            if (!ajaxRequest) {
                ajaxRequest = "partial/ajax".equals(ctx.getExternalContext().getRequestParameterMap().
                    get("Faces-Request"));
            }
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
                    "partial/process".equals(ctx.
                    getExternalContext().getRequestHeaderMap().get("Faces-Request"));
        }
        return partialRequest;

    }


    /**
     * @see javax.faces.context.PartialViewContext#isExecuteAll()
     */
    @Override
    public boolean isExecuteAll() {

        assertNotReleased();
        String execute = ctx.
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
        if (renderAll == null) {
            String render = ctx.
                getExternalContext().getRequestParameterMap()
                    .get(PARTIAL_RENDER_PARAM_NAME);
            renderAll = (ALL_PARTIAL_PHASE_CLIENT_IDS.equals(render));
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

    @Override
    public boolean isResetValues() {
        Object value = ctx.getExternalContext().getRequestParameterMap().get(RESET_VALUES_PARAM_NAME);
        return (null != value && "true".equals(value)) ? true : false;
    }

    @Override
    public void setPartialRequest(boolean isPartialRequest) {
        this.partialRequest = isPartialRequest;
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

        // include the view parameter facet ID if there are other execute IDs
        // to process
        if (!executeIds.isEmpty()) {
            UIViewRoot root = ctx.getViewRoot();
            if (root.getFacetCount() > 0) {
                if (root.getFacet(UIViewRoot.METADATA_FACET_NAME) != null) {
                    executeIds.add(0, UIViewRoot.METADATA_FACET_NAME);   
                }
            }
        }
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
     * @see PartialViewContext#processPartial(javax.faces.event.PhaseId) 
     */
    @Override
    public void processPartial(PhaseId phaseId) {
        PartialViewContext pvc = ctx.getPartialViewContext();
        Collection <String> myExecuteIds = pvc.getExecuteIds();
        Collection <String> myRenderIds = pvc.getRenderIds();
        UIViewRoot viewRoot = ctx.getViewRoot();

        if (phaseId == PhaseId.APPLY_REQUEST_VALUES ||
            phaseId == PhaseId.PROCESS_VALIDATIONS ||
            phaseId == PhaseId.UPDATE_MODEL_VALUES) {

            // Skip this processing if "none" is specified in the render list,
            // or there were no execute phase client ids.

            if (myExecuteIds == null || myExecuteIds.isEmpty()) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                        "No execute and render identifiers specified.  Skipping component processing.");
                }
                return;
            }

            try {
                processComponents(viewRoot, phaseId, myExecuteIds, ctx);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO,
                           e.toString(),
                           e);
                }
                throw new FacesException(e);
            }

            // If we have just finished APPLY_REQUEST_VALUES phase, install the
            // partial response writer.  We want to make sure that any content
            // or errors generated in the other phases are written using the
            // partial response writer.
            //
            if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                PartialResponseWriter writer = pvc.getPartialResponseWriter();
                ctx.setResponseWriter(writer);
            }

        } else if (phaseId == PhaseId.RENDER_RESPONSE) {

            try {
                //
                // We re-enable response writing.
                //
                PartialResponseWriter writer = pvc.getPartialResponseWriter();
                ResponseWriter orig = ctx.getResponseWriter();
                ctx.getAttributes().put(ORIGINAL_WRITER, orig);
                ctx.setResponseWriter(writer);

                ExternalContext exContext = ctx.getExternalContext();
                exContext.setResponseContentType("text/xml");
                exContext.addResponseHeader("Cache-Control", "no-cache");
                
//                String encoding = writer.getCharacterEncoding( );
//                if( encoding == null ) {
//                    encoding = "UTF-8";
//                }
//                writer.writePreamble("<?xml version='1.0' encoding='" + encoding + "'?>\n");
                writer.startDocument();
                
                if (isResetValues()) {
                    viewRoot.resetValues(ctx, myRenderIds);
                }
                
                if (isRenderAll()) {
                    renderAll(ctx, viewRoot);
                    renderState(ctx);
                    writer.endDocument();
                    return;
                }

                // Skip this processing if "none" is specified in the render list,
                // or there were no render phase client ids.
                if (myRenderIds != null && !myRenderIds.isEmpty()) {
                    processComponents(viewRoot, phaseId, myRenderIds, ctx);
                }

                renderState(ctx);

                writer.endDocument();
            } catch (IOException ex) {
                this.cleanupAfterView();
            } catch (RuntimeException ex) {
                this.cleanupAfterView();
                // Throw the exception
                throw ex;
            }
        }
    }

    /**
     * @see javax.faces.context.PartialViewContext#getPartialResponseWriter()
     */
    @Override
    public PartialResponseWriter getPartialResponseWriter() {
        assertNotReleased();
        if (partialResponseWriter == null) {
            partialResponseWriter = new DelayedInitPartialResponseWriter(this);
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
        ctx = null;
        partialRequest = null;

    }

    // -------------------------------------------------------- Private Methods



    private List<String> populatePhaseClientIds(String parameterName) {

        Map<String,String> requestParamMap =
              ctx.getExternalContext().getRequestParameterMap();

        String param = requestParamMap.get(parameterName);
        if (param == null) {
            return new ArrayList<String>();
        } else {
            Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
            String[] pcs = Util.split(appMap, param, "[ \t]+");
            return ((pcs != null && pcs.length != 0)
                    ? new ArrayList<String>(Arrays.asList(pcs))
                    : new ArrayList<String>());
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
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED, VisitHint.EXECUTE_LIFECYCLE);
        VisitContextFactory visitContextFactory = (VisitContextFactory) 
                FactoryFinder.getFactory(VISIT_CONTEXT_FACTORY);
        VisitContext visitContext = visitContextFactory.getVisitContext(context, phaseClientIds, hints);
        PhaseAwareVisitCallback visitCallback =
            new PhaseAwareVisitCallback(ctx, phaseId);
        component.visitTree(visitContext, visitCallback);

        PartialVisitContext partialVisitContext = unwrapPartialVisitContext(visitContext);
        if (partialVisitContext != null) {
            if (LOGGER.isLoggable(Level.FINER) && !partialVisitContext.getUnvisitedClientIds().isEmpty()) {
                Collection<String> unvisitedClientIds = partialVisitContext.getUnvisitedClientIds();
                String message;
                StringBuilder builder = new StringBuilder();
                for (String cur : unvisitedClientIds) {
                    builder.append(cur).append(" ");
                }
                LOGGER.log(Level.FINER,
                        "jsf.context.partial_visit_context_unvisited_children",
                        new Object[]{builder.toString()});
            }
        }    
    }

    /**
     * Unwraps {@link PartialVisitContext} from a chain of {@link VisitContextWrapper}s.
     *
     * If no {@link PartialVisitContext} is found in the chain, null is returned instead.
     * 
     * @param visitContext the visit context.
     * @return the (unwrapped) partial visit context.
     */
    private static PartialVisitContext unwrapPartialVisitContext(VisitContext visitContext) {
        if (visitContext == null) {
            return null;
        }
        if (visitContext instanceof PartialVisitContext) {
            return (PartialVisitContext) visitContext;
        }
        if (visitContext instanceof VisitContextWrapper) {
            return unwrapPartialVisitContext(((VisitContextWrapper) visitContext).getWrapped());
        }
        return null;
    }
    
    private void renderAll(FacesContext context, UIViewRoot viewRoot) throws IOException {
        // If this is a "render all via ajax" request,
        // make sure to wrap the entire page in a <render> elemnt
        // with the special viewStateId of VIEW_ROOT_ID.  This is how the client
        // JavaScript knows how to replace the entire document with
        // this response.
        PartialViewContext pvc = context.getPartialViewContext();
        PartialResponseWriter writer = pvc.getPartialResponseWriter();
        
        if (!Util.isPortletRequest(context)) {
            writer.startUpdate(PartialResponseWriter.RENDER_ALL_MARKER);
            if (viewRoot.getChildCount() > 0) {
                for (UIComponent uiComponent : viewRoot.getChildren()) {
                    uiComponent.encodeAll(context);
                }
            }
            writer.endUpdate();
        }
        else {
            /*
             * If we have a portlet request, start rendering at the view root.
             */
            writer.startUpdate(viewRoot.getClientId(ctx));
            viewRoot.encodeBegin(context);
            if (viewRoot.getChildCount() > 0) {
                for (UIComponent uiComponent : viewRoot.getChildren()) {
                    uiComponent.encodeAll(context);
                }
            }
            viewRoot.encodeEnd(context);
            writer.endUpdate();
        }
    }

    private void renderState(FacesContext context) throws IOException {
        // Get the view state and write it to the response..
        PartialViewContext pvc = context.getPartialViewContext();
        PartialResponseWriter writer = pvc.getPartialResponseWriter();
        String viewStateId = Util.getViewStateId(context);

        writer.startUpdate(viewStateId);
        String state = context.getApplication().getStateManager().getViewState(context);
        writer.write(state);
        writer.endUpdate();

        ClientWindow window = context.getExternalContext().getClientWindow();
        if (null != window) {
            String clientWindowId = Util.getClientWindowId(context);
            writer.startUpdate(clientWindowId);
            writer.write(window.getId());
            writer.endUpdate();
        }
    }

    private PartialResponseWriter createPartialResponseWriter() {

        ExternalContext extContext = ctx.getExternalContext();
        String encoding = extContext.getRequestCharacterEncoding();
        extContext.setResponseCharacterEncoding(encoding);
        ResponseWriter responseWriter = null;
        Writer out = null;
        try {
            out = extContext.getResponseOutputWriter();
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           ioe.toString(),
                           ioe);
            }
        }

        if (out != null) {
            UIViewRoot viewRoot = ctx.getViewRoot();
            if (viewRoot != null) {
                responseWriter =
                    ctx.getRenderKit().createResponseWriter(out,
                    "text/xml", encoding);
            } else {
                RenderKitFactory factory = (RenderKitFactory)
                    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
                RenderKit renderKit = factory.getRenderKit(ctx, RenderKitFactory.HTML_BASIC_RENDER_KIT);
                responseWriter = renderKit.createResponseWriter(out, "text/xml", encoding);
            }
        }
        if (responseWriter instanceof PartialResponseWriter)  {
            return (PartialResponseWriter) responseWriter;
        } else {
            return new PartialResponseWriter(responseWriter);
        }

    }

    private void cleanupAfterView() {
        ResponseWriter orig = (ResponseWriter) ctx.getAttributes().
            get(ORIGINAL_WRITER);
        assert(null != orig);
        // move aside the PartialResponseWriter
        ctx.setResponseWriter(orig);
    }


    @SuppressWarnings({"FinalPrivateMethod"})
    private final void assertNotReleased() {
        if (released) {
            throw new IllegalStateException();
        }
    }

    // ----------------------------------------------------------- Inner Classes


    private static class PhaseAwareVisitCallback implements VisitCallback {

        private PhaseId curPhase;
        private FacesContext ctx;

        private PhaseAwareVisitCallback(FacesContext ctx, PhaseId curPhase) {
            this.ctx = ctx;
            this.curPhase = curPhase;
        }  


        public VisitResult visit(VisitContext context, UIComponent comp) {
            try {

                if (curPhase == PhaseId.APPLY_REQUEST_VALUES) {

                    // RELEASE_PENDING handle immediate request(s)
                    // If the user requested an immediate request
                    // Make sure to set the immediate flag here.

                    comp.processDecodes(ctx);
                } else if (curPhase == PhaseId.PROCESS_VALIDATIONS) {
                    comp.processValidators(ctx);
                } else if (curPhase == PhaseId.UPDATE_MODEL_VALUES) {
                    comp.processUpdates(ctx);
                } else if (curPhase == PhaseId.RENDER_RESPONSE) {
                    PartialResponseWriter writer = ctx.getPartialViewContext().getPartialResponseWriter();
                    writer.startUpdate(comp.getClientId(ctx));
                    // do the default behavior...
                    comp.encodeAll(ctx);
                    writer.endUpdate();
                } else {
                    throw new IllegalStateException("I18N: Unexpected " +
                                                    "PhaseId passed to " +
                                              " PhaseAwareContextCallback: " +
                                                    curPhase.toString());
                }
            }
            catch (IOException ex) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.severe(ex.toString());
                }
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                    ex.toString(),
                    ex);
                }
                throw new FacesException(ex);
            }

            // Once we visit a component, there is no need to visit
            // its children, since processDecodes/Validators/Updates and
            // encodeAll() already traverse the subtree.  We return
            // VisitResult.REJECT to supress the subtree visit.
            return VisitResult.REJECT;
        }
    }


     /**
      * Delays the actual construction of the PartialResponseWriter <em>until</em>
      * content is going to actually be written.
      */
    private static final class DelayedInitPartialResponseWriter extends PartialResponseWriter {

        private ResponseWriter writer;
        private PartialViewContextImpl ctx;

        // -------------------------------------------------------- Constructors


        public DelayedInitPartialResponseWriter(PartialViewContextImpl ctx) {

            super(null);
            this.ctx = ctx;
            ExternalContext extCtx = ctx.ctx.getExternalContext();
            extCtx.setResponseContentType("text/xml");
            extCtx.setResponseCharacterEncoding(extCtx.getRequestCharacterEncoding());

        }


        // ---------------------------------- Methods from PartialResponseWriter


        @Override
        public ResponseWriter getWrapped() {

            if (writer == null) {
                writer = ctx.createPartialResponseWriter();
            }
            return writer;

        }
         
    } // END DelayedInitPartialResponseWriter

} 
