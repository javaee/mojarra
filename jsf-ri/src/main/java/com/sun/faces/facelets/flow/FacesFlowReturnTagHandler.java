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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;

public class FacesFlowReturnTagHandler extends TagHandlerImpl {

    public FacesFlowReturnTagHandler(TagConfig config) {
        super(config);
    }
    
    public static SwitchCaseImpl getNavigationCase(FaceletContext ctx) {
        SwitchCaseImpl result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (SwitchCaseImpl) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.FlowReturnNavigationCase);
        if (null == result) {
            result = new SwitchCaseImpl();
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.FlowReturnNavigationCase, result);
        }
        
        return result;
    }
    
    private static void clearNavigationCase(FaceletContext ctx) {
        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.FlowReturnNavigationCase);
    }
    
    public static List<SwitchCaseImpl> getNavigationCases(FaceletContext ctx) {
        List<SwitchCaseImpl> cases = null;
        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        cases = (List<SwitchCaseImpl>) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.NavigationCases);
        if (null == cases) {
            cases = new ArrayList<SwitchCaseImpl>();
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.NavigationCases, cases);
        }
        
        return cases;
    }
    
    public static boolean isWithinFacesFlowReturn(FaceletContext ctx) {
        boolean result = false;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = flowData.containsKey(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinFacesFlowReturn) ? true : false;

        return result;
    }
    
    private static void setWithinFacesFlowReturn(FaceletContext ctx, boolean state) {
        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);

        if (state) {
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinFacesFlowReturn, Boolean.TRUE);
        } else {
            flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.WithinFacesFlowReturn);
        }
    }
    
    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        try {
            setWithinFacesFlowReturn(ctx, true);
            this.nextHandler.apply(ctx, parent);
            TagAttribute id = this.getRequiredAttribute("id");
            List<SwitchCaseImpl> taskFlowReturns = getNavigationCases(ctx);        
            SwitchCaseImpl taskFlowReturn = getNavigationCase(ctx);
            if (null == taskFlowReturns) {
                throw new TagException(tag, "Unable to determine from-outcome for task flow return id " + id.getValue(ctx));
            }
            taskFlowReturn.setEnclosingId(id.getValue(ctx));
            taskFlowReturns.add(taskFlowReturn);
            clearNavigationCase(ctx);

        }
        finally {
            setWithinFacesFlowReturn(ctx, false);
        }
    }
    
    
    
}
