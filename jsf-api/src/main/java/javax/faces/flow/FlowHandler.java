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
 * <p class="changed_added_2_2">This class is the main entry point that
 * enables the runtime to interact with the Faces Flows feature.  {@code
 * FlowHandler} is used by the {@link
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

 * </div>

 * @since 2.2
     
 */ 

public abstract class FlowHandler {

    /**
     * <p class="changed_added_2_2">Return the {@code Map} that is backs
     * the {@code #{facesFlowScope}} EL implicit object or {@code null}
     * if no flow is currently active. </p>
     *
     * @since 2.2
     */ 
    
    public abstract Map<Object, Object> getCurrentFlowScope();
    
    /**
     * <p class="changed_added_2_2">Return the {@link Flow} </p>
     *
     * @since 2.2
     */ 
    
    public abstract Flow getFlow(String id);
    
    public abstract Flow getFlowByNodeId(String id);
    
    public abstract void addFlow(FacesContext context, Flow toAdd);
    
    public abstract Flow getCurrentFlow(FacesContext context);
            
    public abstract Flow transition(FacesContext context, UIComponent src, UIComponent target);
    
    public abstract boolean isActive(FacesContext context, String id);
        
}
