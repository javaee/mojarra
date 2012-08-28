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
package com.sun.faces.facelets.flow;

import com.sun.faces.RIConstants;
import com.sun.faces.facelets.tag.TagHandlerImpl;
import com.sun.faces.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.NavigationCase;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.MethodCallNode;
import javax.faces.flow.SwitchNode;
import javax.faces.flow.ViewNode;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;

public class FacesFlowDefinitionTagHandler extends TagHandlerImpl {
    
    private static final String FLOW_DATA_MAP_ATTR_NAME = FacesFlowDefinitionTagHandler.class.getPackage().getName() + ".FLOW_DATA";
    
    enum FlowDataKeys {
        FlowReturnNavigationCase,
        NavigationCases,
        Views,
        VDLDocument,
        Initializer,
        Finalizer,
        StartNodeId,
        WithinFacesFlowReturn,
        WithinSwitch,
        WithinMethodCall,
        CurrentNavigationCase,
        CurrentMethodCall,
        MethodCalls,
        SwitchNavigationCases,
        SwitchDefaultCase,
        Switches,
        
    } 
    
    static Map<FlowDataKeys,Object> getFlowData(FaceletContext ctx) {
        Map<Object, Object> attrs = ctx.getFacesContext().getAttributes();
        Map<FlowDataKeys, Object> result = Collections.emptyMap();
        if (!attrs.containsKey(FLOW_DATA_MAP_ATTR_NAME)) {
            // Because Facelets is single threaded, this need not be concurrent.
            result = new EnumMap<FlowDataKeys, Object>(FlowDataKeys.class);
            attrs.put(FLOW_DATA_MAP_ATTR_NAME, result);
        } else {
            result = (Map<FlowDataKeys, Object>) attrs.get(FLOW_DATA_MAP_ATTR_NAME);
        }
        
        return result;
        
    }
    
    private String getFlowId(FaceletContext ctx) {
        String id = null;
        
        TagAttribute flowIdAttr = this.getAttribute("id");
        if (null != flowIdAttr) {
            id = flowIdAttr.getValue(ctx);
        } else {
            id = this.tag.getLocation().getPath();

            int i = id.indexOf(RIConstants.FLOW_IN_JAR_PREFIX);
            if (-1 != i) { 
                id = id.substring(i + RIConstants.FLOW_IN_JAR_PREFIX_LENGTH);
            }
            
            if (id.charAt(0) == '/') {
                id = id.substring(1);
            }
            
            if (id.startsWith("WEB-INF/")) {
                id = id.substring(8);
            }
            
            int slash = id.lastIndexOf("/");
            if (-1 != slash) {
                id = id.substring(0, slash);
            }
        }
        
        
        return id;
    }
    
    private void clearFlowData(FaceletContext ctx) {
        getFlowData(ctx).clear();
    }

    public FacesFlowDefinitionTagHandler(TagConfig config) {
        super(config);
    }
    
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        
        // PENDING(edburns): we should explicitly allow or disallow nested <j:faces-flow-definition> handlers.
        // I'm leaning toward disallow.
        
        clearFlowData(ctx);
        this.nextHandler.apply(ctx, parent);
        FacesContext context = ctx.getFacesContext();
        FlowHandler flowHandler = context.getApplication().getFlowHandler();
        
        Map<FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        String flowId = getFlowId(ctx);
        Flow newFlow = flowHandler.getFlow(context, null, flowId);
        boolean addFlow = false;
        
        if (null == newFlow) {
            
            // In the current implementation, this is the only place where a flow 
            // is instantiated.

            newFlow = new Flow();
            newFlow.setId(flowId);
            addFlow = true;

        } else {
            
            // Inspect the flow for a view corresponding to this page.
            if (null != newFlow.getNode(context, getMyNodeId())) {
                // If we have one, take no further action.
                return;
            } 
            // else we have a flow, but no view corresponding to this page.
        }
        
        if (flowData.isEmpty()) {
            List<ViewNode> viewsInFlow = null;
        
            // <editor-fold defaultstate="collapsed" desc="No flow data, create the entire flow now using conventions">       
            
            // Create a simple flow like this
            
            // <faces-flow-definition id=this tag's id attribute>
            //   <default-node>the name of this page without any extension</default-node>
            //   <view id="the name of this page without any extension">
            //     <vdl-document>the name of this page with the extension</vdl-document>
            //   </view>
            // </faces-flow-definition>
            
            //
            // <default-node>
            //
            if (null == newFlow.getStartNodeId()) {
                newFlow.setStartNodeId(getMyNodeId());
            } 
            
            //
            // <view>s
            //
            if (null == newFlow.getViews()) {
                viewsInFlow = synthesizeViews();
                newFlow.setViews(viewsInFlow);
            } else if (null == newFlow.getNode(context, getMyNodeId())) {
                ViewNode viewNode = new ViewNode();
                viewNode.setId(getMyNodeId());
                viewNode.setVdlDocumentId(this.tag.getLocation().getPath());
                newFlow.getViews().add(viewNode);
            }
            
            // </editor-fold>
 
        } else {
            
            // <editor-fold defaultstate="collapsed" desc="Some flow data, create the flow now with that data, using conventions for gaps">       

            if (null == newFlow.getStartNodeId()) {
                //
                // <default-node>
                //
                // If we have some flow data, we must have a start-node.
                String startNodeId = (String) flowData.get(FlowDataKeys.StartNodeId);
                if (null != startNodeId) {
                    newFlow.setStartNodeId(startNodeId);
                } else {
                    newFlow.setStartNodeId(getMyNodeId());
                }
            }
            
            //
            // <view>s
            //
            // We may or may not have views.
            List<ViewNode> viewsFromTag = (List<ViewNode>) flowData.get(FlowDataKeys.Views);
            if (null == viewsFromTag) {
                // If not, make one from this view.
                viewsFromTag = synthesizeViews();
            }
            if (null == newFlow.getViews()) {            
                newFlow.setViews(viewsFromTag);
            } else {
                newFlow.getViews().addAll(viewsFromTag);
            }
            
            //
            // <faces-flow-return>
            //
            List<FlowNavigationCase> facesFlowReturns = FacesFlowReturnTagHandler.getNavigationCases(ctx);
            if (null != facesFlowReturns) {
                Map<String, NavigationCase> returns = newFlow.getReturns(context);
                for (FlowNavigationCase cur : facesFlowReturns) {
                    String returnId = cur.getEnclosingId();
                    if (!returns.containsKey(returnId)) {
                        returns.put(returnId, cur);
                    }
                }
            }
            
            //
            // <switch>
            //
            Map<String, SwitchNode> switchElement = SwitchNodeTagHandler.getSwitches(ctx);
            if (null != switchElement) {
                Map<String, SwitchNode> switches = newFlow.getSwitches(context);
                for (Map.Entry<String, SwitchNode> cur : switchElement.entrySet()) {
                    switches.put(cur.getKey(), cur.getValue());
                }
            }
            
            //
            // <method-call>
            //
            List<MethodCallNode> methodCalls = MethodCallTagHandler.getMethodCalls(ctx);
            newFlow.setMethodCalls(methodCalls);
            
            //
            // <initializer>
            //
            String meStr = (String) flowData.get(FlowDataKeys.Initializer);
            MethodExpression me = null;
            ExpressionFactory ef = context.getApplication().getExpressionFactory();
            final Class argTypes[] = new Class [0]; // PENDING(edburns): arguments must be supported.
            if (null != meStr) {
                me = ef.createMethodExpression(context.getELContext(), meStr, null, argTypes);
                newFlow.setInitializer(me);
            }

            //
            // <finalizer>
            //
            meStr = (String) flowData.get(FlowDataKeys.Finalizer);
            me = null;
            if (null != meStr) {
                me = ef.createMethodExpression(context.getELContext(), meStr, null, argTypes);
                newFlow.setFinalizer(me);
            }

            
            
            
            // </editor-fold>
            
        }

        // This needs to be done at the end so that the flow is fully populated.
        if (addFlow) {
            flowHandler.addFlow(context, null, newFlow);

        }
        
    }
    
    private List<ViewNode> synthesizeViews() {
        List<ViewNode> viewsInFlow = null;
        ViewNode viewNode = new ViewNode();
        viewNode.setId(getMyNodeId());
        
        String path = this.tag.getLocation().getPath();
        if (path.endsWith(RIConstants.FLOW_DEFINITION_ID_SUFFIX)) {
            int i = path.indexOf(RIConstants.FLOW_DEFINITION_ID_SUFFIX);
            path = path.substring(0, i) + ".xhtml";
        }
        
        
        viewNode.setVdlDocumentId(path);
        viewsInFlow = Collections.synchronizedList(new ArrayList<ViewNode>());
        viewsInFlow.add(viewNode);
        
        return viewsInFlow;
    }
    
    private String getMyNodeId() {
        String myViewId = Util.removeAllButLastSlashPathSegment(this.tag.getLocation().getPath());
        if (myViewId.endsWith(RIConstants.FLOW_DEFINITION_ID_SUFFIX)) {
            myViewId = myViewId.substring(0, myViewId.length() - RIConstants.FLOW_DEFINITION_ID_SUFFIX_LENGTH);
        } else {

            int dot = myViewId.indexOf(".");
            myViewId = myViewId.substring(0, dot);
        }
        return myViewId;
        
    }
    
}
