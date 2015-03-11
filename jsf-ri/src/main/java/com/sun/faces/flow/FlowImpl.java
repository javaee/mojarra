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

import com.sun.faces.util.Util;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.el.MethodExpression;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowCallNode;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.FlowNode;
import javax.faces.flow.MethodCallNode;
import javax.faces.flow.Parameter;
import javax.faces.flow.ReturnNode;
import javax.faces.flow.SwitchNode;
import javax.faces.flow.ViewNode;
import javax.faces.lifecycle.ClientWindow;

public class FlowImpl extends Flow implements Serializable {

    private static final long serialVersionUID = 5287030395068302998L;

    public static final Flow SYNTHESIZED_RETURN_CASE_FLOW = new FlowImpl(FlowHandler.NULL_FLOW);
    
    public static final Flow ABANDONED_FLOW = new FlowImpl(FlowHandlerImpl.ABANDONED_FLOW);

    // <editor-fold defaultstate="collapsed" desc="Instance variables">    
    
    private String id;
    private String definingDocumentId;
    private String startNodeId;
    private final ConcurrentHashMap<String, Set<NavigationCase>> _navigationCases;
    private final Map<String, Set<NavigationCase>> navigationCases;
    private final CopyOnWriteArrayList<ViewNode> _views;
    private final List<ViewNode> views;
    private final CopyOnWriteArrayList<MethodCallNode> _methodCalls;
    private final List<MethodCallNode> methodCalls;
    private final ConcurrentHashMap<String, Parameter> _inboundParameters;
    private final Map<String,Parameter> inboundParameters;
    private final ConcurrentHashMap<String, ReturnNode> _returns;
    private final Map<String, ReturnNode> returns;
    private final ConcurrentHashMap<String, SwitchNode> _switches;
    private final Map<String, SwitchNode> switches;
    private final ConcurrentHashMap<String, FlowCallNode> _facesFlowCalls;
    private final Map<String, FlowCallNode> facesFlowCalls;
    private final ConcurrentHashMap<String, FlowCallNode> _facesFlowCallsByTargetFlowId;
    private MethodExpression initializer;
    private MethodExpression finalizer;
    private boolean hasBeenInitialized = false;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">       

    public FlowImpl() {
        _inboundParameters = new ConcurrentHashMap<String, Parameter>();
        inboundParameters = Collections.unmodifiableMap(_inboundParameters);
        _returns = new ConcurrentHashMap<String, ReturnNode>();
        returns = Collections.unmodifiableMap(_returns);
        _switches = new ConcurrentHashMap<String, SwitchNode>();
        switches = Collections.unmodifiableMap(_switches);
        _facesFlowCalls = new ConcurrentHashMap<String, FlowCallNode>();
        facesFlowCalls = Collections.unmodifiableMap(_facesFlowCalls);
        _facesFlowCallsByTargetFlowId = new ConcurrentHashMap<String, FlowCallNode>();
        _views = new CopyOnWriteArrayList<ViewNode>();
        views = Collections.unmodifiableList(_views);
        _navigationCases = new ConcurrentHashMap<String, Set<NavigationCase>>();
        navigationCases = Collections.unmodifiableMap(_navigationCases);
        _methodCalls = new CopyOnWriteArrayList<MethodCallNode>();
        methodCalls = Collections.unmodifiableList(_methodCalls);
    }
    
    private FlowImpl(String id) {
        this.id = id;
        definingDocumentId = null;
        startNodeId = null;
        _navigationCases = null;
        navigationCases = null;
        _views = null;
        views = null;
        _methodCalls = null;
        methodCalls = null;
        _inboundParameters = null;
        inboundParameters = null;
        _returns = null;
        returns = null;
        _switches = null;
        switches = null;
        _facesFlowCalls = null;
        facesFlowCalls = null;
        _facesFlowCallsByTargetFlowId = null;
        initializer = null;
        finalizer = null;
        hasBeenInitialized = true;
    }    

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Object helpers">       
        
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Flow other = (Flow) obj;
        if ((this.id == null) ? (other.getId() != null) : !this.id.equals(other.getId())) {
            return false;
        }
        if ((this.startNodeId == null) ? (other.getStartNodeId() != null) : !this.startNodeId.equals(other.getStartNodeId())) {
            return false;
        }
        if (this._views != other.getViews() && (this._views == null || !this._views.equals(other.getViews()))) {
            return false;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            if (this._returns != other.getReturns() && (this._returns == null || !this._returns.equals(other.getReturns()))) {
                return false;
            }
            if (this.initializer != other.getInitializer() && (this.initializer == null || !this.initializer.equals(other.getInitializer()))) {
                return false;
            }
            if (this.finalizer != other.getFinalizer() && (this.finalizer == null || !this.finalizer.equals(other.getFinalizer()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 59 * hash + (this.startNodeId != null ? this.startNodeId.hashCode() : 0);
        hash = 59 * hash + (this._views != null ? this._views.hashCode() : 0);
        hash = 59 * hash + (this._returns != null ? this._returns.hashCode() : 0);
        hash = 59 * hash + (this.initializer != null ? this.initializer.hashCode() : 0);
        hash = 59 * hash + (this.finalizer != null ? this.finalizer.hashCode() : 0);
        return hash;
    }

    // </editor-fold>
    
   
    // <editor-fold defaultstate="collapsed" desc="Simple properties">       


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDefiningDocumentId() {
        return definingDocumentId;
    }

    public void setId(String definingDocumentId, String id) {
        Util.notNull("definingDocumentId", definingDocumentId);
        Util.notNull("flowId", id);
        this.id = id;
        this.definingDocumentId = definingDocumentId;
    }

    @Override
    public String getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(String defaultNodeId) {
        this.startNodeId = defaultNodeId;
    }

    @Override
    public MethodExpression getFinalizer() {
        return finalizer;
    }

    public void setFinalizer(MethodExpression finalizer) {
        this.finalizer = finalizer;
    }

    @Override
    public MethodExpression getInitializer() {
        return initializer;
    }

    public void setInitializer(MethodExpression initializer) {
        this.initializer = initializer;
    }
    
    @Override
    public Map<String, Parameter> getInboundParameters() {
        return inboundParameters;
    }    
    
    public Map<String, Parameter> _getInboundParameters() {
        return _inboundParameters;
    }    
    
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="Graph properties">       

    @Override
    public List<ViewNode> getViews() {
        return views;
    }

    public List<ViewNode> _getViews() {
        return _views;
    }

    @Override
    public Map<String,ReturnNode> getReturns() {
        return returns;
    }
    
    public Map<String,ReturnNode> _getReturns() {
        return _returns;
    }

    @Override
    public Map<String,SwitchNode> getSwitches() {
        return switches;
    }
    
    public Map<String,SwitchNode> _getSwitches() {
        return _switches;
    }

    @Override
    public Map<String,FlowCallNode> getFlowCalls() {
        return facesFlowCalls;
    }

    public Map<String,FlowCallNode> _getFlowCalls() {
        return _facesFlowCalls;
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
        return navigationCases;
    }
    
    public Map<String, Set<NavigationCase>> _getNavigationCases() {
        return _navigationCases;
    }
    
    @Override
    public FlowCallNode getFlowCall(Flow targetFlow) {
        String targetFlowId = targetFlow.getId();
        if (!hasBeenInitialized) {
            FacesContext context = FacesContext.getCurrentInstance();
            this.init(context);
        }
        FlowCallNode result = _facesFlowCallsByTargetFlowId.get(targetFlowId);
        
        return result;
    }

    @Override
    public List<MethodCallNode> getMethodCalls() {
        return methodCalls;
    }

    public List<MethodCallNode> _getMethodCalls() {
        return _methodCalls;
    }

    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="Graph navigation">       
    
    @Override
    public FlowNode getNode(String nodeId) {
        List<ViewNode> myViews = getViews();
        FlowNode result = null;
        
        if (null != myViews) {
            for (ViewNode cur : myViews) {
                if (nodeId.equals(cur.getId())) {
                    result = cur;
                    break;
                }
            }
        }
        if (null == result) {
            Map<String, SwitchNode> mySwitches = getSwitches();
            result = mySwitches.get(nodeId);
        }
        if (null == result) {
            List<MethodCallNode> myMethods = getMethodCalls();
            for (MethodCallNode cur : myMethods) {
                if (nodeId.equals(cur.getId())) {
                    result = cur;
                    break;
                }
            }
        }
        if (null == result) {
            Map<String, FlowCallNode> myCalls = getFlowCalls();
            result = myCalls.get(nodeId);
        }
        
        if (null == result) {
            Map<String, ReturnNode> myReturns = getReturns();
            result = myReturns.get(nodeId);
        }
        
        return result;
        
    }
    
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="Outside interaction">       
    
    
    @Override
    public String getClientWindowFlowId(ClientWindow curWindow) {
        String result = null;

        result = curWindow.getId() + "_" + getId();
        
        return result;
    }
    
    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="Helpers">
    
    public void init(FacesContext context) {
        if (hasBeenInitialized) {
            return;
        }
        hasBeenInitialized = true;
        
        // Populate lookup data structures.
        FlowCallNode curNode = null;
        String curTargetFlowId = null;
        for (Map.Entry<String,FlowCallNode> cur : _facesFlowCalls.entrySet()) {
            curNode = cur.getValue();
            curTargetFlowId = curNode.getCalledFlowId(context);
            _facesFlowCallsByTargetFlowId.put(curTargetFlowId, curNode);
        }
    }
    
    // </editor-fold>
    
}
