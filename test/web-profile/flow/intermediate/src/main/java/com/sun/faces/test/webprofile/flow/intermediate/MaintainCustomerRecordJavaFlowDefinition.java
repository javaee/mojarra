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
package com.sun.faces.test.webprofile.flow.intermediate;

import java.io.Serializable;
import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowDefinition;
import javax.faces.flow.builder.FlowBuilderParameter;


public class MaintainCustomerRecordJavaFlowDefinition implements Serializable {
    
    private static final long serialVersionUID = -5610441904980215032L;

    public MaintainCustomerRecordJavaFlowDefinition() {
    }
    
    @Produces @FlowDefinition
    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
        String flowId = "maintain-customer-record-java";
        flowBuilder.id("", flowId);
        flowBuilder.viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml");

        flowBuilder.switchNode("router1").markAsStartNode().defaultOutcome("view-customer").
                switchCase().condition("#{flowScope.customerId == null}").
                fromOutcome("create-customer");
        flowBuilder.viewNode("create-customer", "/" + flowId + "/" + "create-customer.xhtml");
        flowBuilder.viewNode("view-customer", "/" + flowId + "/" + "view-customer.xhtml");
        flowBuilder.viewNode("maintain-customer-record", "/" + flowId + "/" + "maintain-customer-record");
        flowBuilder.methodCallNode("upgrade-customer").expression("#{maintainCustomerBeanJava.upgradeCustomer}").
                defaultOutcome("view-customer");
        flowBuilder.initializer("#{maintainCustomerBeanJava.initializeFlow}");
        flowBuilder.finalizer("#{maintainCustomerBeanJava.cleanUpFlow}");
        flowBuilder.returnNode("success").fromOutcome("/complete");
        flowBuilder.returnNode("errorOccurred").fromOutcome("error");
        flowBuilder.navigationCase().fromViewId("/" + flowId + "/pageA.xhtml").
                fromAction("#{maintainCustomerBeanJava.action01}").
                fromOutcome("pageB").
                toViewId("/" + flowId + "/pageB.xhtml");
        flowBuilder.navigationCase().fromViewId("/" + flowId + "/pageB.xhtml").
                fromOutcome("pageC").condition("#{param.gotoC != null}").toViewId("/" + flowId + "/pageC_true.xhtml");
        flowBuilder.navigationCase().fromViewId("/" + flowId + "/pageB.xhtml").
                fromOutcome("pageC").condition("#{param.gotoC == null}").toViewId("/" + flowId + "/pageC_false.xhtml");
        flowBuilder.navigationCase().fromViewId("/" + flowId + "/pageC*").
                toViewId("/" + flowId + "/pageB.xhtml");
                
        return flowBuilder.getFlow();
    }
    
}
