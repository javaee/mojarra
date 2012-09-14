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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public class FacesFlowCallNode extends FlowNode {
    
    private static final long serialVersionUID = -8867834379603617593L;
    
    private String id;
    
    private String calledFlowId;
    private ValueExpression calledFlowIdVE;
    
    private String calledFlowDocumentId;
    private ValueExpression calledFlowDocumentIdVE;
    
    private ConcurrentHashMap<String, Parameter> outboundParameters = new ConcurrentHashMap<String, Parameter>();
    
    // PENDING(edburns): move setters to impl, use proper el utils.

    public Map<String, Parameter> getOutboundParameters() {
        return outboundParameters;
    }

    public String getCalledFlowDocumentId(FacesContext context) {
        if (null != calledFlowDocumentIdVE) {
            return (String) calledFlowDocumentIdVE.getValue(context.getELContext());
        } else {
            return calledFlowDocumentId;
        }

    }

    public void setCalledFlowDocumentId(FacesContext context, String calledFlowDocumentId) {
        if (null != calledFlowDocumentId && calledFlowDocumentId.startsWith("#{")) {
            this.calledFlowDocumentIdVE = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), calledFlowDocumentId, String.class);
        } else {
            this.calledFlowDocumentId = calledFlowDocumentId;
        }
    }

    public String getCalledFlowId(FacesContext context) {
        if (null != calledFlowIdVE) {
            return (String) calledFlowIdVE.getValue(context.getELContext());
        } else {
            return calledFlowId;
        }
    }

    public void setCalledFlowId(FacesContext context, String calledFlowId) {
        if (null != calledFlowId && calledFlowId.startsWith("#{")) {
            this.calledFlowIdVE = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), calledFlowId, String.class);
        } else {
            this.calledFlowId = calledFlowId;
        }
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
}
