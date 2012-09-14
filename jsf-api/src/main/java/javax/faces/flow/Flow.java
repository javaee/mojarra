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
package javax.faces.flow;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.el.MethodExpression;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ClientWindow;

/**
 * <p class="changed_added_2_2"><strong>Flow</strong> is the runtime
 * representation of a Faces Flow.  Once placed into service by the
 * runtime, an instance of this class is immutable.  The implementation
 * must be thread-safe because instances will be shared across all
 * usages of the flow within the application.</p>
 *
 *
 * @since 2.2
 */

public class Flow implements Serializable {
    
    public Flow() {
    }
    
    // <editor-fold defaultstate="collapsed" desc="Instance variables">       

    private String id;
    private String startNodeId;
    private List<ViewNode> views;
    private List<MethodCallNode> methodCalls;
    private ConcurrentHashMap<String, Parameter> inboundParameters = new ConcurrentHashMap<String, Parameter>();
    private ConcurrentHashMap<String,NavigationCase> returns = new ConcurrentHashMap<String, NavigationCase>();
    private ConcurrentHashMap<String,SwitchNode> switches = new ConcurrentHashMap<String, SwitchNode>();
    private ConcurrentHashMap<String,FacesFlowCallNode> facesFlowCalls = new ConcurrentHashMap<String, FacesFlowCallNode>();
    private ConcurrentHashMap<String,FacesFlowCallNode> facesFlowCallsByTargetFlowId = new ConcurrentHashMap<String, FacesFlowCallNode>();
    private MethodExpression initializer;
    private MethodExpression finalizer;
    private boolean hasBeenInitialized = false;
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Object helpers">       
    
    private static final long serialVersionUID = -7506626306507232154L;
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Flow other = (Flow) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.startNodeId == null) ? (other.startNodeId != null) : !this.startNodeId.equals(other.startNodeId)) {
            return false;
        }
        if (this.views != other.views && (this.views == null || !this.views.equals(other.views))) {
            return false;
        }
        if (this.returns != other.returns && (this.returns == null || !this.returns.equals(other.returns))) {
            return false;
        }
        if (this.initializer != other.initializer && (this.initializer == null || !this.initializer.equals(other.initializer))) {
            return false;
        }
        if (this.finalizer != other.finalizer && (this.finalizer == null || !this.finalizer.equals(other.finalizer))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 59 * hash + (this.startNodeId != null ? this.startNodeId.hashCode() : 0);
        hash = 59 * hash + (this.views != null ? this.views.hashCode() : 0);
        hash = 59 * hash + (this.returns != null ? this.returns.hashCode() : 0);
        hash = 59 * hash + (this.initializer != null ? this.initializer.hashCode() : 0);
        hash = 59 * hash + (this.finalizer != null ? this.finalizer.hashCode() : 0);
        return hash;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Simple properties">       


    /**
     * <p class="changed_added_2_2">Return the immutable id for this
     * Flow.  This must be unique within a flow definition, but need not
     * be unique within the entire application.</p>

     * @since 2.2
     */

    public String getId() {
        return id;
    }

    /**
     * <p class="changed_added_2_2">This setter will likely be moved
     * from the public API into the implementation.</p>
     * @since 2.2
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * <p class="changed_added_2_2">Return the immutable id for the
     * default node that should be activated when this flow is
     * entered.</p>
     *
     * @since 2.2
     */
    
    public String getStartNodeId() {
        return startNodeId;
    }

    /**
     * <p class="changed_added_2_2">This setter will likely be moved
     * from the public API into the implementation.</p>
     * @since 2.2
     */
    public void setStartNodeId(String defaultNodeId) {
        this.startNodeId = defaultNodeId;
    }

    public MethodExpression getFinalizer(FacesContext context) {
        return finalizer;
    }

    public void setFinalizer(MethodExpression finalizer) {
        this.finalizer = finalizer;
    }

    public MethodExpression getInitializer(FacesContext context) {
        init(context);
        return initializer;
    }

    public void setInitializer(MethodExpression initializer) {
        this.initializer = initializer;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Graph properties">       

    public Map<String, Parameter> getInboundParameters() {
        return inboundParameters;
    }

    public List<ViewNode> getViews() {
        return views;
    }

    public void setViews(List<ViewNode> views) {
        this.views = views;
    }
    
    public Map<String,NavigationCase> getReturns(FacesContext context) {
        return returns;
    }
    
    public Map<String,SwitchNode> getSwitches(FacesContext context) {
        return switches;
    }
    
    public Map<String,FacesFlowCallNode> getFacesFlowCalls(FacesContext context) {
        return facesFlowCalls;
    }
    
    public FacesFlowCallNode getFacesFlowCallByTargetFlowId(FacesContext context, String targetFlowId) {
        FacesFlowCallNode result = facesFlowCallsByTargetFlowId.get(targetFlowId);
        
        return result;
    }

    public List<MethodCallNode> getMethodCalls(FacesContext context) {
        return methodCalls;
    }

    public void setMethodCalls(List<MethodCallNode> methodCalls) {
        this.methodCalls = methodCalls;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Graph navigation">       
    
    public FlowNode getNode(FacesContext context, String nodeId) {
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
            Map<String, SwitchNode> mySwitches = getSwitches(context);
            result = mySwitches.get(nodeId);
        }
        if (null == result) {
            List<MethodCallNode> myMethods = getMethodCalls(context);
            for (MethodCallNode cur : myMethods) {
                if (nodeId.equals(cur.getId())) {
                    result = cur;
                    break;
                }
            }
        }
        if (null == result) {
            Map<String, FacesFlowCallNode> myCalls = getFacesFlowCalls(context);
            result = myCalls.get(nodeId);
        }
        
        return result;
        
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Outside interaction">       
    
    
    /**
     * <p class="changed_added_2_2">Obtain the current {@link
     * javax.faces.lifecycle.ClientWindow} from the {@link
     * javax.faces.context.ExternalContext}.  Get the window's id and 
     * append "_" and the return from {@link #getId}.  Return the result.</p>
     *
     * @since 2.2
     */
    
    public String getClientWindowFlowId(ClientWindow curWindow) {
        String result = null;

        result = curWindow.getId() + "_" + getId();
        
        return result;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Helpers">
    
    private void init(FacesContext context) {
        if (hasBeenInitialized) {
            return;
        }
        hasBeenInitialized = true;
        
        // Populate lookup data structures.
        FacesFlowCallNode curNode = null;
        String curTargetFlowId = null;
        for (Map.Entry<String,FacesFlowCallNode> cur : facesFlowCalls.entrySet()) {
            curNode = cur.getValue();
            curTargetFlowId = curNode.getCalledFlowId(context);
            facesFlowCallsByTargetFlowId.put(curTargetFlowId, curNode);
        }
    }
    
    // </editor-fold>

}
