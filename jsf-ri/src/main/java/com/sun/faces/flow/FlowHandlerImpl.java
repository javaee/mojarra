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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowCallNode;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.Parameter;
import javax.faces.flow.ViewNode;

public class FlowHandlerImpl extends FlowHandler {

    public FlowHandlerImpl() {
        WebConfiguration config = WebConfiguration.getInstance();
        flowFeatureIsEnabled = config.isHasFlows();
        flows = new ConcurrentHashMap<String, Map<String, Flow>>();
        flowsByFlowId = new ConcurrentHashMap<String, List<Flow>>();
    }
    
    private boolean flowFeatureIsEnabled;

    // key: definingDocumentId, value: Map<flowId, Flow>
    private Map<String, Map<String, Flow>> flows;
    
    // key: flowId, List<Flow>
    private Map<String, List<Flow>> flowsByFlowId;

    @Override
    public Map<Object, Object> getCurrentFlowScope() {
        return FlowCDIContext.getCurrentFlowScope();
    }
    
    @Override
    public Flow getFlow(FacesContext context, String definingDocumentId, String id) {
        Flow result = null;
        Map<String, Flow> mapsForDefiningDocument = flows.get(definingDocumentId);
        
        if (null == mapsForDefiningDocument) {
            mapsForDefiningDocument = flows.get(id);
        }
        
        if (null != mapsForDefiningDocument) {
            result = mapsForDefiningDocument.get(id);
        }
        
        return result;
    }

    private Flow getFlowByNodeId(String id) {
        if (null == id || 0 == id.length()) {
            throw new IllegalStateException();
        }
        Flow result = null;
        List<Flow> flowsForFlowId = flowsByFlowId.get(id);
        if (null != flowsForFlowId) {
            for (Flow cur : flowsForFlowId) {
                for (ViewNode curView : cur.getViews()) {
                    if (id.equals(curView.getId())) {
                        result = cur;
                    }
                }
            }
        }

        return result;
    }
    
    

    @Override
    public void addFlow(FacesContext context, Flow toAdd) {
        String id = toAdd.getId();
        if (null == toAdd || null == id || 0 == id.length()) {
            throw new IllegalArgumentException();
        }
        Map<String, Flow> mapsForDefiningDocument = flows.get(toAdd.getDefiningDocumentId());
        if (null == mapsForDefiningDocument) {
            mapsForDefiningDocument = new ConcurrentHashMap<String, Flow>();
            flows.put(toAdd.getDefiningDocumentId(), mapsForDefiningDocument);
        }
        
        mapsForDefiningDocument.put(id, toAdd);

        // Make it possible for the "transition" method to map from view nodes
        // to flow instances.
        List<Flow> flowsWithId = flowsByFlowId.get(id);
        if (null == flowsWithId) {
            flowsWithId = new CopyOnWriteArrayList<Flow>();
            flowsByFlowId.put(id, flowsWithId);
        }
        flowsWithId.add(toAdd);
        
        NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();
        if (navigationHandler instanceof ConfigurableNavigationHandler) {
            ((ConfigurableNavigationHandler)navigationHandler).inspectFlow(context, toAdd);
        }
    }

    @Override
    public boolean isActive(FacesContext context, String definingDocumentId, 
                            String id) {
        boolean result = false;
        Deque<Flow> flowStack = getFlowStack(context);
        for (Flow cur : flowStack) {
            if (id.equals(cur.getId()) &&
                definingDocumentId.equals(cur.getDefiningDocumentId())) {
                result = true;
                break;
            }
        }
        
        return result;
    }
    
    
    
    
    @Override
    public Flow getCurrentFlow(FacesContext context) {
        if (!flowFeatureIsEnabled) {
            return null;
        }
        return getFlowStack(context).peek();
    }
            
    // We need a method that takes a view id of a view that is in a flow
    // and makes the system "enter" the flow.
    
    @Override
    @SuppressWarnings(value="")
    public void transition(FacesContext context, Flow sourceFlow, 
                           Flow targetFlow, FlowCallNode outboundCallNode) {
        if (!flowFeatureIsEnabled) {
           return;
        }
        
        // there has to be a better way to structure this logic
        if (!flowsEqual(sourceFlow, targetFlow)) {
            // Do we have an outboundCallNode?
            Map<String, Object> evaluatedParams = null;
            if (null != outboundCallNode) {
                Map<String, Parameter> outboundParameters = outboundCallNode.getOutboundParameters();
                Map<String, Parameter> inboundParameters = targetFlow.getInboundParameters();
                // Are we passing parameters?
                if (null != outboundParameters && !outboundParameters.isEmpty() &&
                        null != inboundParameters && !inboundParameters.isEmpty()) {
                    
                    ELContext elContext = context.getELContext();
                    String curName;
                    // for each outbound parameter...
                    for (Map.Entry<String, Parameter> curOutbound : outboundParameters.entrySet()) {
                        curName = curOutbound.getKey();
                        if (inboundParameters.containsKey(curName)) {
                            if (null == evaluatedParams) {
                                evaluatedParams = new HashMap<String, Object>();
                            }
                            // Evaluate it and put it in the temporary map.
                            // It is necessary to do this before the flow
                            // transition because EL expressions may refer to
                            // things in the current flow scope.
                            evaluatedParams.put(curName, curOutbound.getValue().getValue().getValue(elContext));
                        }
                    }
                }
            }
            
            performPops(context, sourceFlow, targetFlow);
            if (null != targetFlow) {
                pushFlow(context, targetFlow);
            }
            // Now the new flow is active, it's time to evaluate the inbound
            // parameters.
            if (null != evaluatedParams) {
                Map<String, Parameter> inboundParameters = targetFlow.getInboundParameters();
                ELContext elContext = context.getELContext();
                String curName;
                ValueExpression toSet;
                for (Map.Entry<String, Object> curOutbound : evaluatedParams.entrySet()) {
                    curName = curOutbound.getKey();
                    assert(inboundParameters.containsKey(curName));
                    toSet = inboundParameters.get(curName).getValue();
                    toSet.setValue(elContext, curOutbound.getValue());
                }
            }
        } 
    }
    
    private void performPops(FacesContext context, Flow sourceFlow, Flow targetFlow) {
        // case 0: sourceFlow is null.  There must be nothing to pop.
        if (null == sourceFlow) {
            assert(null == peekFlow(context));
            return;
        }
                
        // case 1: target is null
        if (null == targetFlow) {
            popFlow(context);
            return;
        } 
        
        // case 2: neither source nor target are null.  If source does not
        // have a call that calls target, we must pop source.
        if (null == sourceFlow.getFlowCall(targetFlow)) {
            popFlow(context);            
        }

    }
    
    /*
     * The Flow.equals() method alone is insufficient because we need to account
     * for the case where one or the other or both operands may be null.
     * 
     */
    private boolean flowsEqual(Flow flow1, Flow flow2) {
        boolean result = false;
        if (flow1 == flow2) {
            result = true;
        } else if ((null == flow1) || (null == flow2)) {
            result = false;
        } else {
            result = flow1.equals(flow2);
        }
        return result;
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Helper Methods">
    
    private void pushFlow(FacesContext context, Flow toPush) {
        Deque<Flow> flowStack = getFlowStack(context);
        flowStack.push(toPush);
        FlowCDIContext.flowEntered();
        MethodExpression me  = toPush.getInitializer();
        if (null != me) {
            me.invoke(context.getELContext(), null);
        }
    }
    
    private Flow peekFlow(FacesContext context) {
        Deque<Flow> flowStack = getFlowStack(context);
        return flowStack.peek();
    }
    
    private Flow popFlow(FacesContext context) {
        Deque<Flow> flowStack = getFlowStack(context);
        Flow currentFlow = peekFlow(context);
        if (null != currentFlow) {
            callFinalizer(context, currentFlow);
        }
        return flowStack.pollFirst();
        
    }
    
    private void callFinalizer(FacesContext context, Flow currentFlow) {
        MethodExpression me  = currentFlow.getFinalizer();
        if (null != me) {
            me.invoke(context.getELContext(), null);
        }
        FlowCDIContext.flowExited();
    }
    
    private void popAll(FacesContext context) {
        Deque<Flow> flowStack = getFlowStack(context);
        Flow currentFlow = peekFlow(context);
        while  (!flowStack.isEmpty()) {
            callFinalizer(context, currentFlow);
            currentFlow = flowStack.pollFirst();
        }
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
        
    // </editor-fold>
    
}
