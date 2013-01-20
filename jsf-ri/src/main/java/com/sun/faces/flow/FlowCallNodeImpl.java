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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowCallNode;
import javax.faces.flow.Parameter;

public class FlowCallNodeImpl extends FlowCallNode {
    
    private final String id;
    private final ValueExpression calledFlowIdVE;
    
    private final ValueExpression calledFlowDocumentIdVE;
    
    private Map<String, Parameter> outboundParameters;

    public FlowCallNodeImpl(String id, 
            String calledFlowDocumentId, 
            String calledFlowId, 
            List<Parameter> outboundParametersFromConfig) {
        FacesContext context = FacesContext.getCurrentInstance();
        this.id = id;
        
        if (null != calledFlowDocumentId) {
            this.calledFlowDocumentIdVE = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), calledFlowDocumentId, String.class);
        } else {
            this.calledFlowDocumentIdVE = null;
        }
        
        if (null != calledFlowId) {
            this.calledFlowIdVE = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), calledFlowId, String.class);
        } else {
            this.calledFlowIdVE = null;
        }
        
        outboundParameters = new ConcurrentHashMap<String, Parameter>();            
        if (null != outboundParametersFromConfig) {
            for (Parameter cur : outboundParametersFromConfig) {
                outboundParameters.put(cur.getName(), cur);
            }
        }
        
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlowCallNodeImpl other = (FlowCallNodeImpl) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if (this.calledFlowIdVE != other.calledFlowIdVE && (this.calledFlowIdVE == null || !this.calledFlowIdVE.equals(other.calledFlowIdVE))) {
            return false;
        }
        if (this.calledFlowDocumentIdVE != other.calledFlowDocumentIdVE && (this.calledFlowDocumentIdVE == null || !this.calledFlowDocumentIdVE.equals(other.calledFlowDocumentIdVE))) {
            return false;
        }
        if (this.outboundParameters != other.outboundParameters && (this.outboundParameters == null || !this.outboundParameters.equals(other.outboundParameters))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 59 * hash + (this.calledFlowIdVE != null ? this.calledFlowIdVE.hashCode() : 0);
        hash = 59 * hash + (this.calledFlowDocumentIdVE != null ? this.calledFlowDocumentIdVE.hashCode() : 0);
        hash = 59 * hash + (this.outboundParameters != null ? this.outboundParameters.hashCode() : 0);
        return hash;
    }
    
    
    

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCalledFlowDocumentId(FacesContext context) {
        String result = null;
        
        if (null != calledFlowDocumentIdVE) {
            result = (String) calledFlowDocumentIdVE.getValue(context.getELContext());
        }
        
        return result;
    }

    @Override
    public String getCalledFlowId(FacesContext context) {
        String result = null;
        
        if (null != calledFlowIdVE) {
            result = (String) calledFlowIdVE.getValue(context.getELContext());
        }
        
        return result;
    }

    @Override
    public Map<String, Parameter> getOutboundParameters() {
        return outboundParameters;
    }

}
