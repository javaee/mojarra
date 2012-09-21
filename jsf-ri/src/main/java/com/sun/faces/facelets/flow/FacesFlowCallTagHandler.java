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
import com.sun.faces.flow.FlowCallNodeImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.flow.FlowCallNode;
import javax.faces.flow.Parameter;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;

public class FacesFlowCallTagHandler extends TagHandlerImpl {

    public FacesFlowCallTagHandler(TagConfig config) {
        super(config);
    }
    
    static Map<String, FlowCallNode> getFacesFlowCalls(FaceletContext ctx) {
        Map<String, FlowCallNode> result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (Map<String, FlowCallNode>) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.FacesFlowCalls);
        if (null == result) {
            result = new HashMap<String, FlowCallNode>();
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.FacesFlowCalls, result);
        }
        
        return result;
    }
    
    public static List<Parameter> getOutboundParameters(FaceletContext ctx) {
        List<Parameter> result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (List<Parameter>) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.OutboundParameters);
        if (null == result) {
            result = Collections.synchronizedList(new ArrayList<Parameter>());
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.OutboundParameters, result);
        }
        
        return result;
    }
    
    public static void clearOutboundParameters(FaceletContext ctx) {
        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.OutboundParameters);
    }
    
        
        
    public static boolean isWithinFacesFlowCall(FaceletContext ctx) {
        boolean result = false;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = flowData.containsKey(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinFacesFlowCall) ? true : false;

        return result;
    }
    
    private static void setWithinFacesFlowCall(FaceletContext ctx, boolean state) {
        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);

        if (state) {
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinFacesFlowCall, Boolean.TRUE);
        } else {
            flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinFacesFlowCall);
        }
    }
    
    
    
    
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        try {
            setWithinFacesFlowCall(ctx, true);
            this.nextHandler.apply(ctx, parent);
            TagAttribute id = this.getRequiredAttribute("id");
            String idStr = id.getValue(ctx);
            
            FacesFlowReference struct = FacesFlowReferenceTagHandler.getCurrentFacesFlowReference(ctx);

            Map<String, FlowCallNode> calls = getFacesFlowCalls(ctx);
            List<Parameter> outboundParametersFromConfig = FacesFlowCallTagHandler.getOutboundParameters(ctx);
            FlowCallNodeImpl toAdd = new FlowCallNodeImpl(idStr, 
                    struct.getFlowId(),
                    struct.getFlowDocumentId(), 
                    outboundParametersFromConfig);
            calls.put(idStr, toAdd);
            
            
        } finally {
            FacesFlowCallTagHandler.clearOutboundParameters(ctx);
            setWithinFacesFlowCall(ctx, false);
        }
        
    }
    
}
