/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.application.NavigationHandlerImpl;
import com.sun.faces.flow.FlowHandlerImpl;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.application.*;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutcomeTarget;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionListener;
import javax.faces.flow.FlowHandler;
import javax.faces.lifecycle.ClientWindow;

public abstract class OutcomeTargetRenderer extends HtmlBasicRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {}

    // ------------------------------------------------------- Protected Methods
    

    protected void renderPassThruAttributes(FacesContext ctx,
                                            ResponseWriter writer,
                                            UIComponent component,
                                            Attribute[] attributes,
                                            List excludedAttributes)
    throws IOException {
        RenderKitUtils.renderPassThruAttributes(ctx, writer, component, attributes);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component, excludedAttributes);

        
    }

    protected String getLabel(UIComponent component) {

        Object value = ((UIOutcomeTarget) component).getValue();
        return value != null ? value.toString() : "";
        
    }

    protected String getFragment(UIComponent component) {

        String fragment = (String) component.getAttributes().get("fragment");
        fragment = (fragment != null ? fragment.trim() : "");
        if (fragment.length() > 0) {
            fragment = "#" + fragment;
        }
        return fragment;

    }

    @Override
    protected Object getValue(UIComponent component) {

        return ((UIOutcomeTarget) component).getValue();

    }

    protected boolean isIncludeViewParams(UIComponent component, NavigationCase navcase) {

        return (((UIOutcomeTarget) component).isIncludeViewParams() || navcase.isIncludeViewParams());

    }

    /**
     * Invoke the {@link NavigationHandler} preemptively to resolve a {@link NavigationCase}
     * for the outcome declared on the {@link UIOutcomeTarget} component. The current view id
     * is used as the from-view-id when matching navigation cases and the from-action is
     * assumed to be null.
     *
     * @param context the {@link FacesContext} for the current request
     * @param component the target {@link UIComponent}
     *
     * @return the NavigationCase represeting the outcome target
     */
    protected NavigationCase getNavigationCase(FacesContext context, UIComponent component) {
        NavigationHandler navHandler = context.getApplication().getNavigationHandler();
        if (!(navHandler instanceof ConfigurableNavigationHandler)) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING,
                    "jsf.outcome.target.invalid.navigationhandler.type",
                    component.getId());
            }
            return null;
        }

        String outcome = ((UIOutcomeTarget) component).getOutcome();
        if (outcome == null) {
            outcome = context.getViewRoot().getViewId();
            // QUESTION should we avoid the call to getNavigationCase() and instead instantiate one explicitly?
            //String viewId = context.getViewRoot().getViewId();
            //return new NavigationCase(viewId, null, null, null, viewId, false, false);
        }
        String toFlowDocumentId = (String) component.getAttributes().get(ActionListener.TO_FLOW_DOCUMENT_ID_ATTR_NAME);
        NavigationCase navCase = null;
        NavigationHandlerImpl.setResetFlowHandlerStateIfUnset(context, false);
        try {
            if (null == toFlowDocumentId) {
                navCase = ((ConfigurableNavigationHandler) navHandler).getNavigationCase(context, null, outcome);            
            } else {
                navCase = ((ConfigurableNavigationHandler) navHandler).getNavigationCase(context, null, outcome, toFlowDocumentId);            
            }
        }
        finally {
            NavigationHandlerImpl.unsetResetFlowHandlerState(context);
        }

        if (navCase == null && logger.isLoggable(Level.WARNING)) {
            logger.log(Level.WARNING,
                    "jsf.outcometarget.navigation.case.not.resolved",
                    component.getId());
        }
        return navCase;
    }

    /**
     * <p>Resolve the target view id and then delegate to
     * {@link ViewHandler#getBookmarkableURL(javax.faces.context.FacesContext, String, java.util.Map, boolean)}
     * to produce a redirect URL, which will add the page parameters if necessary
     * and properly prioritizing the parameter overrides.</p>
     *
     * @param context the {@link FacesContext} for the current request
     * @param component the target {@link UIComponent}
     * @param navCase the target navigation case
     *
     * @return an encoded URL for the provided navigation case
     */
    protected String getEncodedTargetURL(FacesContext context, UIComponent component, NavigationCase navCase) {
        // FIXME getNavigationCase doesn't resolve the target viewId (it is part of CaseStruct)
        String toViewId = navCase.getToViewId(context);
        Map<String,List<String>> params = getParamOverrides(component);
        addNavigationParams(navCase, params);
        String result = null;
        boolean didDisableClientWindowRendering = false;
        ClientWindow cw = null;

        
        try {
            Map<String, Object> attrs = component.getAttributes();
            Object val = attrs.get("disableClientWindow");
            if (null != val) {
                didDisableClientWindowRendering = "true".equalsIgnoreCase(val.toString());
            }
            if (didDisableClientWindowRendering) {
                cw = context.getExternalContext().getClientWindow();
                if (null != cw) {
                    cw.disableClientWindowRenderMode(context);
                }
            }
            
            result = Util.getViewHandler(context).getBookmarkableURL(context,
                                                               toViewId,
                                                               params,
                                                               isIncludeViewParams(component, navCase));
        } finally {
            if (didDisableClientWindowRendering && null != cw) {
                cw.enableClientWindowRenderMode(context);
            }
        }
        
        return result;
    }

    protected void addNavigationParams(NavigationCase navCase,
                                       Map<String,List<String>> existingParams) {

        Map<String,List<String>> navParams = navCase.getParameters();
        if (navParams != null && !navParams.isEmpty()) {
            for (Map.Entry<String,List<String>> entry : navParams.entrySet()) {
                String navParamName = entry.getKey();
                // only add the navigation params to the existing params collection
                // if the parameter name isn't already present within the existing
                // collection
                if (!existingParams.containsKey(navParamName)) {
                    if (entry.getValue().size() == 1) {
                        String value = entry.getValue().get(0);
                        String sanitized = null != value && 2 < value.length() ? value.trim() : "";
                        if (sanitized.contains("#{") || sanitized.contains("${")) {
                            FacesContext fc = FacesContext.getCurrentInstance();
                            value = fc.getApplication().evaluateExpressionGet(fc, value, String.class);
                            List<String> values = new ArrayList<String>();
                            values.add(value);
                            existingParams.put(navParamName, values);
                        } else {
                            existingParams.put(navParamName, entry.getValue());
                        }
                    } else {
                        existingParams.put(navParamName, entry.getValue());
                    }
                }
            }
        }
        
        String toFlowDocumentId = navCase.getToFlowDocumentId();
        if (null != toFlowDocumentId) {
            if (FlowHandler.NULL_FLOW.equals(toFlowDocumentId)) {
                List<String> flowDocumentIdValues = new ArrayList<String>();
                flowDocumentIdValues.add(FlowHandler.NULL_FLOW);
                existingParams.put(FlowHandler.TO_FLOW_DOCUMENT_ID_REQUEST_PARAM_NAME, flowDocumentIdValues);
                
                FacesContext context = FacesContext.getCurrentInstance();
                FlowHandler fh = context.getApplication().getFlowHandler();
                if (fh instanceof FlowHandlerImpl) {
                    FlowHandlerImpl fhi = (FlowHandlerImpl) fh;
                    List<String> flowReturnDepthValues = new ArrayList<String>();
                    flowReturnDepthValues.add("" + fhi.getAndClearReturnModeDepth(context));
                    existingParams.put(FlowHandlerImpl.FLOW_RETURN_DEPTH_PARAM_NAME, flowReturnDepthValues);
                }
                
            } else {
                String flowId = navCase.getFromOutcome();
                List<String> flowDocumentIdValues = new ArrayList<String>();
                flowDocumentIdValues.add(toFlowDocumentId);
                existingParams.put(FlowHandler.TO_FLOW_DOCUMENT_ID_REQUEST_PARAM_NAME, flowDocumentIdValues);
                
                List<String> flowIdValues = new ArrayList<String>();
                flowIdValues.add(flowId);
                existingParams.put(FlowHandler.FLOW_ID_REQUEST_PARAM_NAME, flowIdValues);
            }
        }

    }

    protected Map<String, List<String>> getParamOverrides(UIComponent component) {
        Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
        Param[] declaredParams = getParamList(component);
        for (Param candidate : declaredParams) {
            // QUESTION shouldn't the trimming of name should be done elsewhere?
            // null value is allowed as a way to suppress page parameter
            if (candidate.name != null && candidate.name.trim().length() > 0) {
                candidate.name = candidate.name.trim();
                List<String> values = params.get(candidate.name);
                if (values == null) {
                    values = new ArrayList<String>();
                    params.put(candidate.name, values);
                }
                values.add(candidate.value);
            }
        }

        return params;
    }

}
