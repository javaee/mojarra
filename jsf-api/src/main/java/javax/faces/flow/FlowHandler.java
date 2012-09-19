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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_2"><strong>FlowHandler</strong> is the main
 * entry point that enables the runtime to interact with the faces flows
 * feature.  {@link
 * javax.faces.application.ConfigurableNavigationHandler} uses this
 * class when it needs to make navigational decisions related to flows.
 * The faces flow feature entirely depends on the {@link
 * javax.faces.lifecycle.ClientWindow} feature, which itself requires
 * explicit enabling.  Please see the specification for {@code
 * ClientWindow} for the requirements for enabling this feature.</p>
 
 * <div class="changed_added_2_2">

 * <p><strong>Defining Flows</strong></p>

 * <p>The implementation must support defining faces flows using the
 * <code>&lt;faces-flow-definition&gt;</code> element as specified in
 * the <a target="_"
 * href="../../../web-facesconfig.html#type_faces-config-faces-flow-definitionType">Application
 * Configuration Resources XML Schema Definition</a>.  Additional means
 * of defining flows may be provided by decorating the {@link
 * FlowHandlerFactory}.</p>

 * <p><strong>Where to Discover Flows</strong></p>

 * <ul>

 * <p>The system must support the following conventions within the web
 * application for discovery of flows.  In each of these cases, when the
 * flow is discovered (see below), it is added to the runtime with a
 * call to {@link #addFlow}.  In this discussion, let
 * <code>flowName</code> be the name of the flow.</p>

 *     <ul>

 * <li><p><strong>In a jar on the application's classpath</strong></p>

 * <p>When packaged in a jar, a flow definition and all of its contents
 * must reside in the path <code>META-INF/flows/&lt;flowName&gt;</code>
 * in the jar.  The flow definition must reside in the path
 * <code>META-INF/flows/&lt;flowName&gt;/&lt;flowName&gt;-flow.xml</code>
 * in the jar.</p>

 * <p>If <code>ProjectStage</code> is <code>Development</code>, the
 * runtime must try to detect the case of multiple flows defined with
 * the same <code>flowName</code> and fail to deploy in that case.  A
 * descriptive error including the <code>flowName</code> must be
 * printed.</p>

 * </li>

 * <li><p><strong>Within a directory in the web application root</strong></p>

 * <p>When packaged in a directory in the web application root, a flow
 * definition and all of its contents must reside in the path
 * <code>flowName</code> relative to the web application root.  The flow
 * definition must reside in
 * <code>&lt;flowName&gt;/&lt;flowName&gt;-flow.xml</code>. </p>

 * </li>

 * <li><p><strong>Directly in the web application
 * root</strong></p>

 * <p>When packaged directly in the web application root, a flow
 * definition must reside in <code>&lt;flowName&gt;-flow.xml</code>.</p>

 * </li>

 * <li><p><strong>In the Application Configuration
 * Resources</strong></p>

 * <p>Flow definitions may be included directly in the Application
 * Configuration Resources, such as the
 * <code>WEB-INF/faces-config.xml</code> file.</p>

 * </li>

 * </ul>

 * <p>In all of these cases, an entire flow is discoverable starting
 * with the root <code>&lt;faces-flow-definition&gt;</code> for that
 * flow.</p>

 * </ul>

 * <p><strong>When to Discover Flows</strong></p>

 * <ul>

 * <p>Because flows may be defined in several different locations, the
 * timing of when to discover them and add them to the runtime varies.
 * Flows defined in the Application Configuration Resources must be
 * loaded at the same time as the Application Configuration Resources
 * are processed, namely, deployment time.  Flows defined in any of the
 * other places mentioned previously may be discovered and loaded
 * lazily, when a user agent actually traverses a view in the flow.
 * Because the runtime must use the {@link #addFlow} method to make the
 * flow available to the application, it is possible to dynamically add
 * flows using this same API.  The runtime is not required to persist
 * the flow definition for such flows, but may do so.</p>

 * </ul>

 * <p><strong>Managing Flows</strong></p>

 * <ul>

 * <p>The singleton instance of this class must be thread safe, and
 * therefore must not store any per-user state.  Flows are, however,
 * traversed in a per-user manner, and must be associated with the
 * current {@link javax.faces.lifecycle.ClientWindow}.  Furthermore,
 * Flows may be nested.  These requirements strongly suggest managing
 * the flows with a stack-like runtime data structure, stored in a
 * per-user fashion and associated with the {@code ClientWindow}.</p>

 * <p><strong>The Flow Graph</strong></p>

 * <p>Prior versions of the specification defined a flow graph but the
 * only kind of node in the graph was a VDL view.  The Faces Flow
 * feature currently defines the following node types.</p>

 * <ul>

 * <li><p>View</p>

 * <p>This is the regular JSF VDL View that has been in the
 * specification since the beginning.</p>

 * </li>

 * <li><p>Switch</p>

 * <p>This is a list of EL expressions.  When control is passed to a
 * switch node, each expression in the list is evaluated and the first
 * one that returns {@code true} is used to define the id of the next
 * node to which control must be passed.  If none of the expressions
 * evaluates to {@code true}, control passes to the specified default
 * id.</p>

 * </li>

 * <li><p>Return</p>

 * <p>This node type specifies an outcome that is returned to the
 * calling flow.</p>

 * </li>

 * <li><p>Method Call</p>

 * <p>This node type allows invocation of arbitrary application logic at
 * any point in the executiong of the flow.  An outcome can be specified
 * that will cause a navigation case to be navigated to after the method
 * has been invoked.</p>

 * </li>

 * <li><p>Faces Flow Call</p>

 * <p>This node type allows one flow to call another flow.  The calling
 * flow remains active and is not exited until control returns from the
 * called flow.</p>

 * </li>

 * </ul>

 * <p>This UML diagram shows the relationship of the flow node types to
 * the {@code FlowHandler}.  Non-implemented classes are shaded in
 * gray.</p>

 * <img src="FlowHierarchy.jpg" style="display: block; margin-left: auto; margin-right: auto"></img>

 * </ul>

 * <div class="changed_added_2_2">

 * <p><strong>Flows and Model Objects</strong></p>

 * <ul>

 * <p>Managed beans annotated with the CDI annotation
 * {@link FlowScoped} must be instantiated upon a user agent's entry
 * into the named scope, and must be made available for garbage
 * collection when the user agent leaves the flow.</p>

 * <p>The <code>facesFlowScoped</code> EL implicit object is also
 * available to store values in the "current" slope.  Values stored in
 * this scope must be made available for garbage collection when the
 * user agent leaves the flow.</p>

 * </ul>

 * </div>

 * @since 2.2
     
 */ 

public abstract class FlowHandler {

    /**
     * <p class="changed_added_2_2">Return the {@code Map} that backs
     * the {@code #{facesFlowScope}} EL implicit object or {@code null}
     * if no flow is currently active. </p>
     *
     * @since 2.2
     */ 
    
    public abstract Map<Object, Object> getCurrentFlowScope();
    
    /**
     * <p class="changed_added_2_2">Return the {@link Flow} whose {@code
     * id} is equivalent to the argument {@code id}, within the scope of
     * the argument {@code definingDocument}. </p>
     *
     * @param definingDocument An application unique identifier
     * for the document in which the returned flow is defined.

     * @param id the id of a {@link Flow}, unique within the
     * scope of the {@code definingDocument}.

     * @throws NullPointerException if any of the parameters are {@code null}
     *
     * @since 2.2
     */ 
    
    public abstract Flow getFlow(FacesContext context, Object definingDocument, String id);
    
    /**
     * <p class="changed_added_2_2">Add the argument {@link Flow} to the
     * collection of {@code Flow}s known to the current
     * application.  The implementation must be thread safe.</p>
     *
     * @param definingDocument An application unique identifier
     * for the document in which the argument flow is defined.

     * @param toAdd the {@code Flow} to add.

     * @throws NullPointerException if any of the parameters are {@code null}
     *
     * @throws IllegalStateException if there is already a flow with the
     * same {@code id} as the argument {@code Flow} within the scope of
     * the {@code definingDocument}.
     *
     * @since 2.2
     */ 
    public abstract void addFlow(FacesContext context, Object definingDocument, Flow toAdd);
    
    /**
     * <p class="changed_added_2_2">Return the currently active {@link
     * Flow} for the argument {@code FacesContext}, or {@code null} if
     * no flow is active.  A {@code Flow} must always be associated with
     * exactly one {@link javax.faces.lifecycle.ClientWindow}, but a
     * {@code ClientWindow} may have multiple {@code Flow}s.</p>
     *
     * @param context the {@code FacesContext} for the current request.
     *
     * @since 2.2
     */
    public abstract Flow getCurrentFlow(FacesContext context);
    
    public Flow getCurrentFlow() {
        return getCurrentFlow(FacesContext.getCurrentInstance());
    }

    /**
     * <p class="changed_added_2_2">Perform a transition in the flow
     * graph for the current user's {@link
     * javax.faces.lifecycle.ClientWindow}.  Obtain references to the
     * {@code Flow} instances corresponding to the {@code origin} and
     * {@code destination} arguments.  If the {@code origin Flow} is
     * equal to the {@code destination Flow}, take no action and return
     * {@code null}.  Otherwise, if the {@code outboundCallNode}
     * argument is non-{@code null} save aside the outbound parameters.
     * For discussion <strong>evaluatedParams</strong> is a data
     * structure that stores the evaluated values of any outbound
     * parameters.  It is necessary to evaluate these values before
     * popping any flow scopes because the values may refer to scoped
     * instances that need to be passed to the target flow, but will not
     * be available once the new scope is activated.  Save aside the
     * outbound parameters using the following algorithm.</p>

     * <div class="changed_added_2_2">

     * <ul>

     * <p>If the {@code outboundParameters} property of {@code
     * outboundCallNode} is non-{@code null} and not empty, and the
     * {@code inboundParameters} property of the target flow is
     * non-{@code null} and not empty, for each entry in the outbound
     * parameters whose name matches an entry in the inbound parameters,
     * evaluate the value of the parameter, and put the evaluated value
     * into <strong>evaluatedParams</strong> under the corresponding
     * name.  Otherwise, consider <strong>evaluatedParams</strong> to be
     * empty.</p>

     * </ul>

     * <p>If the {@code destination Flow} is a sub-flow of the {@code
     * origin Flow} push the {@code destination Flow} onto the flow data
     * structure and return {@code the destination Flow}. Otherwise, pop
     * the current {code Flow} from the flow data structure.  If the
     * {@code destination Flow} is non-{@code null}, make the {@code
     * destination Flow} the current flow, pushing it onto the data
     * structure.  If <strong>evaluatedParams</strong> is not empty, for
     * each entry, find the corresponding parameter in the target flow's
     * inbound parameters and call its {@code setValue} method, passing
     * the value from <strong>evaluatedParams</strong>.</p>
     * 
     * </div>
     * @since 2.2
     */
            
    public abstract Flow transition(FacesContext context, UIComponent origin, 
            UIComponent destination, FlowCallNode outboundCallNode);
    

    /**
     * <p class="changed_added_2_2">Return {@code true} if and only if
     * the flow referenced by the argument {@code definingDocument} and
     * {@code id} is currently active.</p>

     * @param definingDocument An application unique identifier
     * for the document in which the returned flow is defined.

     * @param id the id of a {@link Flow}, unique within the
     * scope of the {@code definingDocument}.

     * @since 2.2
     */
    public abstract boolean isActive(FacesContext context, Object definingDocument, String id);
        
}
