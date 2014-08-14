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
package com.sun.faces.flow.builder;

import com.sun.faces.flow.FlowImpl;
import com.sun.faces.flow.ParameterImpl;
import com.sun.faces.util.Util;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowCallBuilder;
import javax.faces.flow.builder.MethodCallBuilder;
import javax.faces.flow.builder.NavigationCaseBuilder;
import javax.faces.flow.builder.ReturnBuilder;
import javax.faces.flow.builder.SwitchBuilder;
import javax.faces.flow.builder.ViewBuilder;

public class FlowBuilderImpl extends FlowBuilder {
    
    private FlowImpl flow;
    private ExpressionFactory expressionFactory;
    private ELContext elContext;
    private FacesContext context;
    private boolean didInit;
    private boolean hasId;
    
    public FlowBuilderImpl(FacesContext context) {
        flow = new FlowImpl();
        this.context = context;
        this.expressionFactory = context.getApplication().getExpressionFactory();
        this.elContext = context.getELContext();
        this.didInit = false;
        this.hasId = false;

    }
    
    // <editor-fold defaultstate="collapsed" desc="Create Flow Nodes">   

    @Override
    public NavigationCaseBuilder navigationCase() {
        return new NavigationCaseBuilderImpl(this);
    }

    @Override
    public ViewBuilder viewNode(String viewNodeId, String vdlDocumentId) {
        Util.notNull("viewNodeId", viewNodeId);
        Util.notNull("vdlDocumentId", vdlDocumentId);
        ViewBuilder result = new ViewBuilderImpl(this, viewNodeId, vdlDocumentId);
        return result;
    }

    @Override
    public SwitchBuilder switchNode(String switchNodeId) {
        Util.notNull("switchNodeId", switchNodeId);
        return new SwitchBuilderImpl(this, switchNodeId);
    }
    
    @Override
    public ReturnBuilder returnNode(String returnNodeId) {
        Util.notNull("returnNodeId", returnNodeId);
        return new ReturnBuilderImpl(this, returnNodeId);
    }
    
    @Override
    public MethodCallBuilder methodCallNode(String methodCallNodeId) {
        Util.notNull("methodCallNodeId", methodCallNodeId);
        return new MethodCallBuilderImpl(this, methodCallNodeId);
    }
    
    @Override
    public FlowCallBuilder flowCallNode(String flowCallNodeId) {
        return new FlowCallBuilderImpl(this, flowCallNodeId);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Flow-wide Settings">     
    
    @Override
    public FlowBuilder id(String definingDocumentId, String flowId) {
        Util.notNull("definingDocumentId", definingDocumentId);
        Util.notNull("flowId", flowId);
        flow.setId(definingDocumentId, flowId);
        this.hasId = true;
        return this;
    }
    
    @Override
    public FlowBuilder initializer(MethodExpression methodExpression) {
        Util.notNull("methodExpression", methodExpression);
        flow.setInitializer(methodExpression);
        return this;
    }

    @Override
    public FlowBuilder initializer(String methodExpression) {
        Util.notNull("methodExpression", methodExpression);
        MethodExpression me = expressionFactory.createMethodExpression(elContext, methodExpression, null, new Class[] {});
        flow.setInitializer(me);
        return this;
    }
    
    @Override
    public FlowBuilder finalizer(MethodExpression methodExpression) {
        flow.setFinalizer(methodExpression);
        return this;
    }

    @Override
    public FlowBuilder finalizer(String methodExpression) {
        MethodExpression me = expressionFactory.createMethodExpression(elContext, methodExpression, null, new Class[] {});
        flow.setFinalizer(me);
        return this;
    }
    
    @Override
    public FlowBuilder inboundParameter(String name, ValueExpression value) {
        ParameterImpl param = new ParameterImpl(name, value);
        flow._getInboundParameters().put(name, param);
        
        return this;
    }

    @Override
    public FlowBuilder inboundParameter(String name, String value) {
        ValueExpression ve = expressionFactory.createValueExpression(elContext, value, Object.class);
        inboundParameter(name, ve);
        return this;
    }

    // </editor-fold>
        
    @Override
    public Flow getFlow() {
        if (!hasId) {
            throw new IllegalStateException("Flow must have a defining document id and flow id.");
        }
        if (!didInit) {
            flow.init(context);
            String startNodeId = flow.getStartNodeId();
            if (null == startNodeId) {
                String flowId = flow.getId();
                this.viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml").markAsStartNode();
            }
            didInit = true;
        }
        return flow;
    }
    
    public FlowImpl _getFlow() {
        return flow;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Package private helpers">
    
    ExpressionFactory getExpressionFactory() {
        return expressionFactory;
    }
    
    ELContext getELContext() {
        return elContext;
    }
    
    // </editor-fold>

    
}
