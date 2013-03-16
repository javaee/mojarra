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

import com.sun.faces.flow.FlowCallNodeImpl;
import com.sun.faces.flow.ParameterImpl;
import com.sun.faces.util.Util;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.flow.FlowCallNode;
import javax.faces.flow.builder.FlowCallBuilder;

public class FlowCallBuilderImpl extends FlowCallBuilder {
    
    private FlowBuilderImpl root;
    private String flowCallNodeId;
    private String flowDocumentId;
    private String flowId;
    

    public FlowCallBuilderImpl(FlowBuilderImpl root, String id) {
        this.root = root;
        this.flowCallNodeId = id;
    }

    @Override
    public FlowCallBuilder flowReference(String flowDocumentId, String flowId) {
        Util.notNull("flowDocumentId", flowDocumentId);
        Util.notNull("flowId", flowId);
        this.flowDocumentId = flowDocumentId;
        this.flowId = flowId;
        getFlowCall();
        return this;
    }
    
    private FlowCallNodeImpl getFlowCall() {
        Util.notNull("flowCallNodeId", flowCallNodeId);
        Util.notNull("flowwDocumentId", flowDocumentId);
        Util.notNull("flowId", flowId);
        
        Map<String, FlowCallNode> flowCalls = root._getFlow()._getFlowCalls();
        FlowCallNodeImpl flowCall = (FlowCallNodeImpl) flowCalls.get(flowCallNodeId);
        if (null == flowCall) {
            flowCall = new FlowCallNodeImpl(flowCallNodeId, flowDocumentId, flowId, null);
            flowCalls.put(flowCallNodeId, flowCall);
        }
        return flowCall;
    }

    @Override
    public FlowCallBuilder outboundParameter(String name, ValueExpression value) {
        Util.notNull("name", name);
        Util.notNull("value", value);
        ParameterImpl param = new ParameterImpl(name, value);
        FlowCallNodeImpl flowCall = getFlowCall();
        flowCall._getOutboundParameters().put(name, param);
        return this;
    }

    @Override
    public FlowCallBuilder outboundParameter(String name, String value) {
        Util.notNull("name", name);
        Util.notNull("value", value);
        ValueExpression ve = root.getExpressionFactory().createValueExpression(root.getELContext(), value, Object.class);
        outboundParameter(name, ve);
        return this;
    }

    @Override
    public FlowCallBuilder markAsStartNode() {
        root._getFlow().setStartNodeId(flowCallNodeId);
        return this;
    }
    
    
    
}
