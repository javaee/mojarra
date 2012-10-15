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

import com.sun.faces.facelets.flow.FlowNavigationCase;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowBuilder;
import javax.faces.flow.FlowCallNode;
import javax.faces.flow.ViewNode;

public class FlowBuilderImpl extends FlowBuilder {
    
    private FlowImpl flow;
    private ExpressionFactory expressionFactory;
    private ELContext elContext;
    
    private String returnNodeId;
    private String flowCallNodeId;
    private String flowReference;

    public FlowBuilderImpl(FacesContext context) {
        flow = new FlowImpl();
        this.expressionFactory = context.getApplication().getExpressionFactory();
        this.elContext = context.getELContext();

    }
    
    // <editor-fold defaultstate="collapsed" desc="Create Flow Nodes">       

    @Override
    public FlowBuilder viewNode(String viewNodeId, String vdlDocumentId) {
        List<ViewNode> viewNodes = flow.getViews();
        ViewNodeImpl viewNode = new ViewNodeImpl(viewNodeId, vdlDocumentId);
        viewNodes.add(viewNode);
        return this;
    }

    @Override
    public FlowBuilder switchNode(String switchNodeId) {
        return this;
    }
    
    @Override
    public FlowBuilder returnNode(String returnNodeId) {
        this.returnNodeId = returnNodeId;
        return this;
    }
    
    @Override
    public FlowBuilder methodCallNode(String methodCallNodeId) {
        return this;
    }
    
    @Override
    public FlowBuilder flowCallNode(String flowCallNodeId) {
        this.flowCallNodeId = flowCallNodeId;
        this.flowReference = null;
        return this;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Operate on a Flow Node">       
    
    @Override
    public FlowBuilder condition(ValueExpression valueExpression) {
        return this;
    }
    
    @Override
    public FlowBuilder fromOutcome(String outcome) {
        if (null == returnNodeId) {
            throw new IllegalStateException("Attempt to define navigation case without required context.");
        }
        FlowNavigationCase navCase = new FlowNavigationCase();
        navCase.setFromOutcome(outcome);
        flow.getReturns().put(returnNodeId, navCase);
        returnNodeId = null;

        return this;
    }

    @Override
    public FlowBuilder fromOutcome(ValueExpression outcome) {
        if (null == returnNodeId) {
            throw new IllegalStateException("Attempt to define navigation case without required context.");
        }
        FlowNavigationCase navCase = new FlowNavigationCase();
        navCase.setFromOutcome(outcome.getExpressionString());
        flow.getReturns().put(returnNodeId, navCase);
        returnNodeId = null;

        return this;
    }
            
    @Override
    public FlowBuilder defaultOutcome(ValueExpression outcome) {
        return this;
    }

    @Override
    public FlowBuilder defaultOutcome(String outcome) {
        return this;
    }
    
    @Override
    public FlowBuilder navigationCase() {
        if (null == returnNodeId) {
            throw new IllegalStateException("Attempt to define navigation case without required context: return node or...");
        }
        
        return this;
    }
    
    @Override
    public FlowBuilder flowReference(String flowReference) {
        this.flowReference = flowReference;
        
        return this;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Flow-wide Settings">     
    
    @Override
    public FlowBuilder id(String flowId) {
        flow.setId(flowId);
        flow.setStartNodeId(flowId);
        viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml");
        return this;
    }
    
    @Override
    public FlowBuilder markAsStartNode(String startNodeId) {
        flow.setStartNodeId(startNodeId);
        return this;
    }

    @Override
    public FlowBuilder initializer(MethodExpression methodExpression) {
        flow.setInitializer(methodExpression);
        return this;
    }
    
    @Override
    public FlowBuilder finalizer(MethodExpression methodExpression) {
        flow.setFinalizer(methodExpression);
        return this;
    }
    
    @Override
    public FlowBuilder inboundParameter(String name, ValueExpression value) {
        ParameterImpl param = new ParameterImpl(name, value);
        flow.getInboundParameters().put(name, param);
        
        return this;
    }

    @Override
    public FlowBuilder inboundParameter(String name, String value) {
        ValueExpression ve = expressionFactory.createValueExpression(elContext, value, Object.class);
        inboundParameter(name, ve);
        return this;
    }

    @Override
    public FlowBuilder outboundParameter(String name, ValueExpression value) {
        if (null == flowCallNodeId) {
            throw new IllegalStateException("Attempt to define outbound parameter without required context: flow call node.");
        }
        if (null == flowReference) {
            throw new IllegalStateException("Attempt to define outbound parameter without required context: id of called flow.");
        }
        ParameterImpl param = new ParameterImpl(name, value);
        Map<String, FlowCallNode> flowCalls = flow.getFlowCalls();
        FlowCallNodeImpl flowCall = (FlowCallNodeImpl) flowCalls.get(flowCallNodeId);
        if (null == flowCall) {
            flowCall = new FlowCallNodeImpl(flowCallNodeId, flowReference, null, null);
            flowCalls.put(flowCallNodeId, flowCall);
        }
        flowCall.getOutboundParameters().put(name, param);
        
        return this;
    }

    @Override
    public FlowBuilder outboundParameter(String name, String value) {
        ValueExpression ve = expressionFactory.createValueExpression(elContext, value, Object.class);
        outboundParameter(name, ve);
        return this;
    }
    
    
    
        
    // </editor-fold>
        
    @Override
    public Flow getFlow() {
        return flow;
    }
    
}
