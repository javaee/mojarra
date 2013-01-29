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
import com.sun.faces.flow.MethodCallNodeImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.faces.component.UIComponent;
import javax.faces.flow.MethodCallNode;
import javax.faces.flow.Parameter;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;

public class MethodCallTagHandler extends TagHandlerImpl {

    public MethodCallTagHandler(TagConfig config) {
        super(config);
    }
    
    static class MethodCallStruct {
        String expression;
        String defaultOutcome;
        String id;
    }
    
    
    public static List<Parameter> getParameters(FaceletContext ctx) {
        List<Parameter> result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (List<Parameter>) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.Parameters);
        if (null == result) {
            result = Collections.synchronizedList(new ArrayList<Parameter>());
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.Parameters, result);
        }
        
        return result;
    }
        
    private static void setWithinMethodCall(FaceletContext ctx, boolean state) {
        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);

        if (state) {
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinMethodCall, Boolean.TRUE);
        } else {
            flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinMethodCall);
            flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentMethodCall);
            flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.Parameters);
        }
    }
    
    public static boolean isWithinMethodCall(FaceletContext ctx) {
        boolean result = false;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = flowData.containsKey(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinMethodCall) ? true : false;

        return result;
    }
    
    private static MethodCallStruct getCurrentMethodCall(String id, FaceletContext ctx) {
        MethodCallStruct result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (MethodCallStruct) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentMethodCall);
        if (null == result) {
            result = new MethodCallStruct();
            result.id = id;
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentMethodCall, result);
        }
        
        return result;
    }
    
    static MethodCallStruct getCurrentMethodCall(FaceletContext ctx) {
        MethodCallStruct result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (MethodCallStruct) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentMethodCall);

        return result;
    }
    
    public static List<MethodCallNode> getMethodCalls(FaceletContext ctx) {
        List<MethodCallNode> result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (List<MethodCallNode>) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.MethodCalls);
        if (null == result) {
            result = new CopyOnWriteArrayList<MethodCallNode>();
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.MethodCalls, result);
        }
        
        return result;
    }
    
    
    
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        try {
            setWithinMethodCall(ctx, true);
            TagAttribute id = this.getRequiredAttribute("id");
            String idStr = id.getValue(ctx);
            MethodCallStruct methodCallStruct = MethodCallTagHandler.getCurrentMethodCall(idStr, ctx);
            this.nextHandler.apply(ctx, parent);

            List<MethodCallNode> methodCalls = getMethodCalls(ctx);
            List<Parameter> params = getParameters(ctx);
            MethodCallNodeImpl cur = new MethodCallNodeImpl(ctx.getFacesContext(), 
                    methodCallStruct.id, 
                    methodCallStruct.expression,
                    methodCallStruct.defaultOutcome,
                    params);
            
            methodCalls.add(cur);

        } finally {
            setWithinMethodCall(ctx, false);
        }

    }
    
    
    
}
