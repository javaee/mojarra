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
 * entry point that enables the runtime to interact with the Faces Flows
 * feature.  {@code FlowHandler} is used by the {@link
 * javax.faces.application.ConfigurableNavigationHandler} when it needs
 * to make decisions related to flows.  The implementation must support
 * an XML based means of defining flows, expressed using elements within
 * the Application Configuration Resources.  Additional means of
 * defining flows may be provided by decorating the {@link
 * FlowHandlerFactory}.</p>
 
 * <div class="changed_added_2_2">

 * <p>Regardless of the means of defining a flow, the runtime must scan
 * for and discover flows at application startup time, as well as
 * support the capability to add flows dynamically during the
 * application's lifetime.  In the case of dynamically added flows, the
 * runtime is not required to persist the flow definition for such
 * flows, but may do so.  The faces flow feature entirely depends on the
 * {@link javax.faces.lifecycle.ClientWindow} feature, which itself
 * requires explicit enabling.  Please see the specification for {@code
 * ClientWindow} for the requirements for enabling this feature.</p>
 * 
 * <p><strong>Startup Time Requirements</strong></p>
 * 
 * <p>During startup, flow definitions must be discovered and inspected
 * and each such flow made known to the runtime with a call to {@link
 * #addFlow}.  The discovery process is documented in the section titled
 * "Application Startup Behavior" in the spec prose document.</p>

 * <p><strong>Defining Flows</strong></p>

 * <p>The runtime must support the set of XML elements described in the
 * section titled "XML Schema Definition for Application Configuration
 * Resources".  Flows may be defined in any Application Configuration
 * Resources file accessible to the application.  Particularly useful is
 * the pattern where the flow definition resides in the same directory
 * (in the filesystem or jar file) as the views that comprise the
 * flow.</p>

 * <p><strong>Managing Flows</strong></p>

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

 *
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
     * application. </p>
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

    /**
     * <p class="changed_added_2_2">Perform a transition in the flow
     * graph for the current user's {@link
     * javax.faces.lifecycle.ClientWindow}.  Obtain references to the
     * {@code Flow} instances corresponding to the {@code origin} and
     * {@code destination} arguments.  If the {@code origin Flow} is
     * equal to the {@code destination Flow}, take no action and return
     * {@code null}.  If the {@code destination Flow} is a sub-flow of
     * the {@code origin Flow} push the {@code destination Flow} onto
     * the flow data structure and return {@code the destination
     * Flow}. Otherwise, pop the current {code Flow} from the flow data
     * structure.  If the {@code destination Flow} is non-{@code null},
     * make the {@code destination Flow} the current flow, pushing it
     * onto the data structure.</p>
     * 
     *
     * @since 2.2
     */
            
    public abstract Flow transition(FacesContext context, UIComponent origin, UIComponent destination);
    

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
