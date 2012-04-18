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

import com.sun.faces.facelets.tag.TagHandlerImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.NavigationCase;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.ViewNode;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;

public class FacesFlowDefinitionTagHandler extends TagHandlerImpl {
    
    private static final String FLOW_DATA_MAP_ATTR_NAME = FacesFlowDefinitionTagHandler.class.getPackage().getName() + ".FLOW_DATA";
    
    enum FlowDataKeys {
        FlowReturnNavigationCase,
        Views
        
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
            int slash = id.lastIndexOf("/");
            if (0 == slash) {
                id = "";
            } else {
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
        Flow newFlow = flowHandler.getFlow(flowId);
        
        if (null == newFlow) {

            newFlow = new Flow();
            newFlow.setId(flowId);
            flowHandler.addFlow(newFlow);

        } else {
            
            // Inspect the flow for a view corresponding to this page.
            if (null != newFlow.getView(getMyNodeId())) {
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
            if (null == newFlow.getDefaultNodeId()) {
                newFlow.setDefaultNodeId(getMyNodeId());
            } 
            
            //
            // <view>s
            //
            if (null == newFlow.getViews()) {
                viewsInFlow = synthesizeViews();
                newFlow.setViews(viewsInFlow);
            } else if (null == newFlow.getView(getMyNodeId())) {
                ViewNode viewNode = new ViewNode();
                viewNode.setId(getMyNodeId());
                viewNode.setVdlDocumentId(this.tag.getLocation().getPath());
                newFlow.getViews().add(viewNode);
            }
            
            // </editor-fold>
 
        } else {
            
            // <editor-fold defaultstate="collapsed" desc="Some flow data, create the flow now with that data, using conventions for gaps">       

            if (null == newFlow.getDefaultNodeId()) {
                //
                // <default-node>
                //
                // If we have some flow data, we must have a default-node.
                
                newFlow.setDefaultNodeId(getMyNodeId());
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
            FlowNavigationCase facesFlowReturn = FacesFlowReturnTagHandler.getNavigationCase(ctx);
            if (null != facesFlowReturn) {
                Map<String, NavigationCase> returns = newFlow.getReturns(context);
                String returnId = facesFlowReturn.getReturnId();
                if (!returns.containsKey(returnId)) {
                    returns.put(returnId, facesFlowReturn);
                }
            }
            
            
            // </editor-fold>
            
        }
        
        
    }
    
    private List<ViewNode> synthesizeViews() {
        List<ViewNode> viewsInFlow = null;
        ViewNode viewNode = new ViewNode();
        viewNode.setId(getMyNodeId());
        viewNode.setVdlDocumentId(this.tag.getLocation().getPath());
        viewsInFlow = Collections.synchronizedList(new ArrayList<ViewNode>());
        viewsInFlow.add(viewNode);
        
        return viewsInFlow;
    }
    
    private String getMyNodeId() {
        String myViewId = this.tag.getLocation().getPath();
        int dot = myViewId.indexOf(".");
        String id = myViewId.substring(0, dot);
        return id;
        
    }
    
}
