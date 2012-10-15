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

import javax.el.MethodExpression;
import javax.el.ValueExpression;

public abstract class FlowBuilder {
    
    // <editor-fold defaultstate="collapsed" desc="Create Flow Nodes">       
    
    public abstract FlowBuilder viewNode(String viewNodeId, String vdlDocumentId);

    public abstract FlowBuilder switchNode(String switchNodeId);
    
    public abstract FlowBuilder returnNode(String returnNodeId);
    
    public abstract FlowBuilder methodCallNode(String methodCallNodeId);
    
    public abstract FlowBuilder flowCallNode(String flowCallNodeId);
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Operate on a Flow Node">       
    
    public abstract FlowBuilder condition(ValueExpression valueExpression);
    
    public abstract FlowBuilder fromOutcome(String outcome);
    
    public abstract FlowBuilder fromOutcome(ValueExpression outcome);
        
    public abstract FlowBuilder defaultOutcome(String outcome);
    
    public abstract FlowBuilder defaultOutcome(ValueExpression outcome);
    
    public abstract FlowBuilder navigationCase();
    
    public abstract FlowBuilder flowReference(String flowId);
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Flow-wide Settings">     
    
    public abstract FlowBuilder id(String flowId);
    
    public abstract FlowBuilder markAsStartNode(String startNodeId);
    
    public abstract FlowBuilder initializer(MethodExpression valueExpression);
    
    public abstract FlowBuilder finalizer(MethodExpression valueExpression);
    
    public abstract FlowBuilder inboundParameter(String name, ValueExpression value);
        
    public abstract FlowBuilder inboundParameter(String name, String value);

    public abstract FlowBuilder outboundParameter(String name, ValueExpression value);
        
    public abstract FlowBuilder outboundParameter(String name, String value);

    // </editor-fold>
        
    public abstract Flow getFlow();
    
}
    

