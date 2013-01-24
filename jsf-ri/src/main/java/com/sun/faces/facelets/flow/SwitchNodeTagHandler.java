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
import com.sun.faces.flow.SwitchCaseImpl;
import com.sun.faces.flow.SwitchNodeImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.flow.SwitchCase;
import javax.faces.flow.SwitchNode;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;

public class SwitchNodeTagHandler extends TagHandlerImpl {

    public SwitchNodeTagHandler(TagConfig config) {
        super(config);
    }
    
    public static List<SwitchCaseImpl> getSwitchCases(FaceletContext ctx) {
        List<SwitchCaseImpl> result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (List<SwitchCaseImpl>) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.SwitchNavigationCases);
        if (null == result) {
            result = Collections.synchronizedList(new ArrayList<SwitchCaseImpl>());
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.SwitchNavigationCases, result);
        }
        
        return result;
    }
    
    static Map<String, SwitchNode> getSwitches(FaceletContext ctx) {
        Map<String, SwitchNode> result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (Map<String, SwitchNode>) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.Switches);
        if (null == result) {
            result = new HashMap<String, SwitchNode>();
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.Switches, result);
        }
        
        return result;
    }
    
    public static SwitchCaseImpl getDefaultSwitchCase(FaceletContext ctx, boolean create) {
        SwitchCaseImpl result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (SwitchCaseImpl) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.SwitchDefaultCase);
        if (null == result && create) {
            result = new SwitchCaseImpl();
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.SwitchDefaultCase, result);
        }
        
        return result;
    }
    
    public static SwitchCaseImpl getDefaultSwitchCase(FaceletContext ctx) {
        SwitchCaseImpl result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (SwitchCaseImpl) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.SwitchDefaultCase);
        
        return result;
    }
    
    
    public static boolean isWithinSwitch(FaceletContext ctx) {
        boolean result = false;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = flowData.containsKey(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinSwitch) ? true : false;

        return result;
    }
    
    private static void setWithinSwitch(FaceletContext ctx, boolean state) {
        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);

        if (state) {
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinSwitch, Boolean.TRUE);
        } else {
            flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinSwitch);
            flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.SwitchNavigationCases);
            flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.SwitchDefaultCase);
        }
    }
    
    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        try {
            setWithinSwitch(ctx, true);
            this.nextHandler.apply(ctx, parent);
            TagAttribute id = this.getRequiredAttribute("id");
            
            List<SwitchCaseImpl> casesFromConfig = SwitchNodeTagHandler.getSwitchCases(ctx);
            String idStr = id.getValue(ctx);

            SwitchCaseImpl defaultSwitchCase = SwitchNodeTagHandler.getDefaultSwitchCase(ctx);
            if (null != defaultSwitchCase) {
                defaultSwitchCase.setEnclosingId(idStr);
            }
            
            SwitchNodeImpl toAdd = new SwitchNodeImpl(idStr, defaultSwitchCase);
            List<SwitchCase> cases = toAdd._getCases();
            for (SwitchCaseImpl cur : casesFromConfig) {
                cur.setEnclosingId(idStr);
                cases.add(cur);
            }
            
            Map<String, SwitchNode> switches = getSwitches(ctx);
            switches.put(idStr, toAdd);
            
        } finally {
            setWithinSwitch(ctx, false);
        }
        
    }
    
}
