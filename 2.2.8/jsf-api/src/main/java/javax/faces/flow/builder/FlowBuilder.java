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
package javax.faces.flow.builder;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.flow.Flow;

/**
 * <p class="changed_added_2_2">A Java language API for building {@link Flow}s. 
 * This API is semantically identical to the
 * <code>&lt;flow-definition&gt;</code> element in the
 * <a target="_"
 * href="../../../../web-facesconfig.html#type_faces-config-flow-definitionType">Application
 * Configuration Resources XML Schema Definition</a>.</p>
 * 
 * <div class="changed_added_2_2">
 * 
 * <p>Usage example:</p><pre><code>public class FlowA implements Serializable {
    
    &#x40;Produces {@link FlowDefinition}
    public {@link Flow} buildMyFlow(&#x40;{@link FlowBuilderParameter} {@link FlowBuilder} flowBuilder) {
        String flowId = "flow-a";
        flowBuilder.id("unique", flowId);
        flowBuilder.returnNode("taskFlowReturn1").
                fromOutcome("#{flow_a_Bean.returnValue}");
        flowBuilder.inboundParameter("param1FromFlowB", "#{flowScope.param1Value}");
        flowBuilder.inboundParameter("param2FromFlowB", "#{flowScope.param2Value}");
        flowBuilder.flowCallNode("callB").flowReference("", "flow-b").
                outboundParameter("param1FromFlowA", "param1Value").
                outboundParameter("param2FromFlowA", "param2Value");
        
        return flowBuilder.getFlow();
    }
}
</code></pre>
 * 
 * <p>The runtime must discover all such methods at startup time and ensure that
 * the returned flows are added to the {@link javax.faces.flow.FlowHandler} using
 * the {@link javax.faces.flow.FlowHandler#addFlow(javax.faces.context.FacesContext, javax.faces.flow.Flow)} method.</p>
 * 
 * </div>
 *
 * @since 2.2
 */

public abstract class FlowBuilder  {
    
    /**
     * <p class="changed_added_2_2">Set the defining document id and flow id
     * of this flow.</p>
     * 
     * @param definingDocumentId The defining document id of this flow, or the 
     * empty string if this flow does not need a defining document id.
     * @param id the id of the flow
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */
    public abstract FlowBuilder id(String definingDocumentId, String id);
    
    /**
     * <p class="changed_added_2_2">Define a view node in a flow graph.</p>
     * 
     * @param viewNodeId Within the flow graph, the id of this view node
     * @param vdlDocumentId The fully qualified path to the view node within this flow.
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */
    
    /**
     * <p class="changed_added_2_2">Define a view node in a flow graph.</p>
     * 
     * @param viewNodeId Within the flow graph, the id of this view node.  Must
     * be unique among all nodes in this flow graph.
     * @param vdlDocumentId The fully qualified path to the view node within this flow.
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */

    public abstract ViewBuilder viewNode(String viewNodeId, String vdlDocumentId);

    /**
     * <p class="changed_added_2_2">Define a particular combination of 
     * conditions that must match for this case to be executed, 
     * and the view id of the component tree that should be selected next.</p>
     * 
     * @since 2.2
     */
    public abstract NavigationCaseBuilder navigationCase();

    /**
     * <p class="changed_added_2_2">Define a particular list of cases that 
     * will be inspected in the order they are defined to determine where to
     * go next in the flow graph.  If none of the cases match, the outcome
     * from the default case is chosen.</p>
     * 
     * @param switchNodeId Within the flow graph, the id of this switch node.  Must
     * be unique among all nodes in this flow graph.
     *
     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     * 
     */
    public abstract SwitchBuilder switchNode(String switchNodeId);
    
    /**
     * <p class="changed_added_2_2">Define a return node.  This node will cause
     * the specified outcome to be returned to the calling flow.</p>
     * 
     * @param returnNodeId Within the flow graph, the id of this return node.  Must
     * be unique among all nodes in this flow graph.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract ReturnBuilder returnNode(String returnNodeId);
    
    /**
     * <p class="changed_added_2_2">Define a method call node.  This node will
     * cause the specified method to be invoked, passing parameters if necessary.
     * The return from the method is used as the outcome for where to go next in the
     * flow.  If the method is a void method, the default outcome is used.</p>
     * 
     * @param methodCallNodeId Within the flow graph, the id of this method call node.  Must
     * be unique among all nodes in this flow graph.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract MethodCallBuilder methodCallNode(String methodCallNodeId);
    
    /**
     * <p class="changed_added_2_2">Define a flow call node.  This node will
     * cause the specified flow to be called, passing parameters if necessary.</p>
     * 
     * @param flowCallNodeId Within the flow graph, the id of this return node.  Must
     * be unique among all nodes in this flow graph.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract FlowCallBuilder flowCallNode(String flowCallNodeId);
    
    /**
     * <p class="changed_added_2_2">A MethodExpression that will be invoked when the flow is entered.</p>
     * 
     * @param methodExpression the expression to invoke, must reference a zero-argument method.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract FlowBuilder initializer(MethodExpression methodExpression);
    
    /**
     * <p class="changed_added_2_2">A MethodExpression that will be invoked when the flow is entered.</p>
     * 
     * @param methodExpression the expression to invoke, must reference a zero-argument method.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract FlowBuilder initializer(String methodExpression);
    
    /**
     * <p class="changed_added_2_2">A MethodExpression that will be invoked when the flow is exited.</p>
     * 
     * @param methodExpression the expression to invoke, must reference a zero-argument method.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract FlowBuilder finalizer(MethodExpression methodExpression);
    
    /**
     * <p class="changed_added_2_2">A MethodExpression that will be invoked when the flow is exited.</p>
     * 
     * @param methodExpression the expression to invoke, must reference a zero-argument method.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract FlowBuilder finalizer(String methodExpression);

    /**
     * <p class="changed_added_2_2">A parameter that will be populated with 
     * the value from a correspondingly named outbound parameter from another
     * flow when this flow is entered from that flow.</p>
     * 
     * @param name the parameter name
     * 
     * @param expression the {@code ValueExpression} to populate with the inbound
     * value when the flow is called.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract FlowBuilder inboundParameter(String name, ValueExpression expression);
        
    /**
     * <p class="changed_added_2_2">A parameter that will be populated with 
     * the value from a correspondingly named outbound parameter from another
     * flow when this flow is entered from that flow.</p>
     * 
     * @param name the parameter name
     * 
     * @param expression the {@code ValueExpression} String to populate with the inbound
     * value when the flow is called.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract FlowBuilder inboundParameter(String name, String expression);

    /**
     * <p class="changed_added_2_2">Called as the last step in flow definition, 
     * this method must perform any implementation specific initialization
     * and return the built {@link Flow}. If called more than one time during a 
     * given flow building process, the second and subsequent invocations must
     * take no action and return the built flow.</p>
     * 
     * @throws IllegalStateException if the {@link #id} method had not been
     * called prior to this method being called.
     * 
     * @since 2.2
     */
    public abstract Flow getFlow();
    
}
    

