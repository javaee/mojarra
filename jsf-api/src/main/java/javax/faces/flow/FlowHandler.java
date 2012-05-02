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
 * to make navigational decisions related to flows.  The implementation
 * must support two means of defining Faces Flows: 1. metadata within
 * VDL pages and 2. metadata in the Application Configuration Resources.
 * Additional means of defining flows may be provided by decorating the
 * {@link FlowHandlerFactory}.</p>
 
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
 * <p>During startup, flow definitions must be discoverd and inspected
 * and each such flow made known to the runtime with a call to {@link
 * #addFlow}.  The inspection must proceed in the following order.
 * 
 * <ul>
 
 <li><p>VDL files accessible to the application.</p></li>
 
 <li><p>The Application Configuration Resources.</p></li>
 * 
 * </ul>
 * 
 * <p>If the runtime discovers a conflict between a flow defined in a VDL view
 * and one defined in the Application Configuration Resources, the entry in
 * the Application Configuration Resources takes precedence.  This is
 * consistent with the behavior regarding annotations and XML as specified
 * in the section "Requirements for scanning of classes for annotations" of
 * the spec prose document.</p>

 * <p><strong>Defining Flows</strong></p>

 * <p>The runtime must support the set of XML elements described in the
 * "Faces Flows" facelet taglibrary as a means of defining the Faces
 * Flows for an application instance.  See <a
 * href="../../../overview-summary.html#overview_description">the API
 * Overview</a> for a link to the Facelet Taglibrarydocs, including the
 * Faces Flows taglibrary.  When used within VDL views, the elements
 * must reside within the view's {@code <f:metadata>} section, and must
 * be namespaced properly as with any other Facelet Taglibrary.  When
 * used in the Application Configuration Resources, the elements may
 * appear as defined in the Application Configuration Resources
 * schema.</p>

 * <p><strong>Managing Flows</strong></p>

 * <p>The singleton instance of this class must be thread safe, and
 * therefore must not store any per-user state.  Flows are, however,
 * traversed in a per-user manner, and must be associated with the
 * current {@link javax.faces.lifecycle.ClientWindow}.  Furthermore,
 * Flows may be nested.  These requirements strongly suggest managing
 * the flows with a stack-like runtime data structure, stored in a
 * per-user fashion and associated with the {@code ClientWindow}.</p>

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
