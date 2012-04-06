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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    static Map<Object,Object> getFlowData(FaceletContext ctx) {
        Map<Object, Object> attrs = ctx.getFacesContext().getAttributes();
        Map<Object, Object> result = Collections.emptyMap();
        if (!attrs.containsKey(FLOW_DATA_MAP_ATTR_NAME)) {
            // Because Facelets is single threaded, this need not be concurrent.
            result = new HashMap<Object, Object>();
            attrs.put(FLOW_DATA_MAP_ATTR_NAME, result);
        } else {
            result = (Map<Object, Object>) attrs.get(FLOW_DATA_MAP_ATTR_NAME);
        }
        
        return result;
        
    }

    public FacesFlowDefinitionTagHandler(TagConfig config) {
        super(config);
    }
    
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        this.nextHandler.apply(ctx, parent);
        FacesContext context = ctx.getFacesContext();
        FlowHandler flowHandler = context.getApplication().getFlowHandler();
        
        Map<Object, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        if (flowData.isEmpty()) {
            // Create a simple flow like this
            
            // <faces-flow-definition id=this tag's id attribute>
            //   <default-node>the name of this page without any extension</default-node>
            //   <view id="the name of this page without any extension">
            //     <vdl-document>the name of this page with the extension</vdl-document>
            //   </view>
            // </faces-flow-definition>
            TagAttribute flowIdAttr = this.getRequiredAttribute("id");
            String flowId = flowIdAttr.getValue(ctx);
            Flow newFlow = flowHandler.getFlow(flowId);
            if (null == newFlow) {
                newFlow = new Flow();
                newFlow.setId(flowId);
                
                String myViewId = this.tag.getLocation().getPath();
                newFlow.setDefaultNodeId(myViewId);

                int dot = myViewId.indexOf(".");
                String id = myViewId.substring(0, dot);
                ViewNode viewNode = new ViewNode();
                viewNode.setVdlDocumentId(myViewId);
                viewNode.setId(id);
                List<ViewNode> viewsInFlow = Collections.synchronizedList(new ArrayList<ViewNode>());
                viewsInFlow.add(viewNode);
                newFlow.setViews(viewsInFlow);
                
                flowHandler.addFlow(newFlow);
            }

        } else {
            
        }
    }
    
    
    
}
