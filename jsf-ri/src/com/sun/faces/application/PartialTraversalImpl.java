/*
 * $Id: PartialTraversalImpl.java,v 1.84 2007/08/28 06:06:14 rlubke Exp $
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

package com.sun.faces.application;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.PartialTraversal;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;

import javax.servlet.http.HttpServletResponse;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;


/**
 * <p><b>PartialTraversalImpl</b> is a 
 * {@link javax.faces.application.PartialTraversal} implementation that
 * performs partial view processing and partial view rendering.</p> 
 */

public class PartialTraversalImpl implements PartialTraversal {


    // -------------------------------------------------------- Static Variables


    // Log instance for this class
    private static Logger LOGGER = FacesLogger.APPLICATION.getLogger();


    // ------------------------------------------------------ Instance Variables


    private static final String RENDER_ALL_MARKER = "javax.faces.ViewRoot";
    private static final String ORIGINAL_WRITER = "javax.faces.originalWriter";
    private static final String VIEW_STATE_MARKER = "javax.faces.ViewState";


    public void traverse(FacesContext context, PhaseId phaseId, UIViewRoot viewRoot) {

        PartialViewContext partialViewContext = context.getPartialViewContext();
        List <String> executePhaseClientIds = partialViewContext.getExecutePhaseClientIds(); 
        List <String> renderPhaseClientIds = partialViewContext.getRenderPhaseClientIds(); 

        if (phaseId == PhaseId.APPLY_REQUEST_VALUES || 
            phaseId == PhaseId.PROCESS_VALIDATIONS ||
            phaseId == PhaseId.UPDATE_MODEL_VALUES) {

            // Skip this processing if "none" is specified in the render list, 
            // or there were no execute phase client ids. 

            if (executePhaseClientIds == null || executePhaseClientIds.isEmpty() ||
                partialViewContext.isExecuteNone()) {
                // PENDING LOG ERROR OR WARNING
                return;
            }

            try {
                processComponents(viewRoot, phaseId, executePhaseClientIds, context);
            } catch (Exception e) {
                // PENDING LOG EXCEPTION
            }


            // If we have just finished APPLY_REQUEST_VALUES phase, install the
            // partial response writer.  We want to make sure that any content
            // or errors generated in the other phases are written using the
            // partial response writer.
            // 
            if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                ResponseWriter writer = partialViewContext.getPartialResponseWriter();
                context.setResponseWriter(writer);
            }

        } else if (phaseId == PhaseId.RENDER_RESPONSE) {

            try {
                partialViewContext.enableResponseWriting(true);
                ResponseWriter writer = partialViewContext.getPartialResponseWriter();
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

                if (partialViewContext.isRenderAll()) {
                    renderAll(context, viewRoot);
                    renderState(context, viewRoot);
                    writer.endElement("changes");
                    writer.endElement("partial-response");
                    return;
                }

                // Skip this processing if "none" is specified in the render list,
                // or there were no render phase client ids.
                if (renderPhaseClientIds == null || renderPhaseClientIds.isEmpty() ||
                    partialViewContext.isRenderNone()) {
                } else { 
                    processComponents(viewRoot, phaseId, renderPhaseClientIds, context);
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

    private void processComponents(UIComponent component, PhaseId phaseId, 
        List phaseClientIds, FacesContext context) throws IOException {
        PartialViewContext partialViewContext = context.getPartialViewContext();
        if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
            if (phaseClientIds.contains(component.getClientId()) ||
                partialViewContext.isExecuteAll()) {
                component.processDecodes(context);
            }
        } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
            if (phaseClientIds.contains(component.getClientId()) ||
                partialViewContext.isExecuteAll()) {
                component.processValidators(context);
            }
        } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
            if (phaseClientIds.contains(component.getClientId()) ||
                partialViewContext.isExecuteAll()) {
                component.processUpdates(context);
            }
        } else if (phaseId == PhaseId.RENDER_RESPONSE) {
            if (phaseClientIds.contains(component.getClientId())) {
                if (component.isRendered()) {
                    ResponseWriter writer = context.getResponseWriter();
                    writer.startElement("update", component);
                    writer.writeAttribute("id", component.getClientId(context), "id");
                    try {
                        writer.write("<![CDATA[");

                        // do the default behavior...
                        component.encodeAll(context);

                        writer.write("]]>");
                    } catch (Exception ce) {
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
            }
        }
        Iterator<UIComponent> itr = component.getFacetsAndChildren();
        while (itr.hasNext()) {
            UIComponent kid = (UIComponent)itr.next();
            processComponents(kid, phaseId, phaseClientIds, context);
        }
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


    private void cleanupAfterView(FacesContext context) {
        ResponseWriter orig = (ResponseWriter) context.getAttributes().
            get(ORIGINAL_WRITER);
        assert(null != orig);
        // move aside the PartialResponseWriter
        context.setResponseWriter(orig);
    }
}
