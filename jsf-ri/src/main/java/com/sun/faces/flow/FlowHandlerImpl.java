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
package com.sun.faces.flow;

import com.sun.faces.config.WebConfiguration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowHandler;

public class FlowHandlerImpl extends FlowHandler {

    public FlowHandlerImpl() {
        WebConfiguration config = WebConfiguration.getInstance();
        hasFlows = config.isHasFlows();
    }
    
    private boolean hasFlows;
    
    
    
    @Override
    public Flow getCurrentFlow(FacesContext context) {
        if (!hasFlows) {
            return null;
        }
        return getFlowStack(context).peek();
    }
            
    // We need a method that takes a view id of a view that is in a flow
    // and makes the system "enter" the flow.
    
    @Override
    @SuppressWarnings(value="")
    public void transition(FacesContext context, UIComponent src, UIComponent target) {
        if (!hasFlows) {
            return;
        }
        
        String  sourceFlowId = getSourceFlowId(context, src),
                targetFlowId = getTargetFlowId(context, target);
        if (!sourceFlowId.equals(targetFlowId)) {
            popFlow(context);
        }
        
        if ("/start.xhtml".equals(targetFlowId)) {
            Flow currentFlow = new Flow();
            currentFlow.setId("flow");
            pushFlow(context, currentFlow);
        }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Helper Methods">
    
    private void pushFlow(FacesContext context, Flow toPush) {
        Deque<Flow> flowStack = getFlowStack(context);
        flowStack.push(toPush);
    }
    
    private Flow peekFlow(FacesContext context) {
        Deque<Flow> flowStack = getFlowStack(context);
        return flowStack.peek();
    }
    
    private Flow popFlow(FacesContext context) {
        Deque<Flow> flowStack = getFlowStack(context);
        Flow currentFlow = peekFlow(context);
        if (null != currentFlow) {
            FlowCDIContext.flowExited();
        }
        return flowStack.pollFirst();
        
    }
    
    private Deque<Flow> getFlowStack(FacesContext context) {
        Deque<Flow> result = null;
        ExternalContext extContext = context.getExternalContext();
        String sessionKey = extContext.getClientWindow().getId() + "_flowStack";
        Map<String, Object> sessionMap = extContext.getSessionMap();
        result = (Deque<Flow>) sessionMap.get(sessionKey);
        if (null == result) {
            result = new ArrayDeque<Flow>();
            sessionMap.put(sessionKey, result);
        }
        
        return result;
    }
    
    private String getSourceFlowId(FacesContext context, UIComponent source) {
        String result = "";
        if (source instanceof javax.faces.component.UIViewRoot) {
            result = ((javax.faces.component.UIViewRoot)source).getViewId();
        }
        
        return result;
    }
    
    private String getTargetFlowId(FacesContext context, UIComponent target) {
        String result = "";
        if (target instanceof javax.faces.component.UIViewRoot) {
            result = ((javax.faces.component.UIViewRoot)target).getViewId();
        }
        
        return result;
    }
    
    // </editor-fold>
    
}
