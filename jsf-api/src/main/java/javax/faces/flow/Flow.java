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

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.el.MethodExpression;
import javax.faces.application.NavigationCase;
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

public abstract class Flow {
    
    // <editor-fold defaultstate="collapsed" desc="Simple properties">       


    /**
     * <p class="changed_added_2_2">Return the immutable id for this
     * Flow.  This must be unique within the defining document (such as
     * an Application Configuration Resources file), but need not be
     * unique within the entire application.</p>

     * @since 2.2
     */

    public abstract String getId();

    /**
     * <p class="changed_added_2_2">Return the immutable application unique 
     * identifier for the document in which the argument flow is defined.</p>

     * @since 2.2
     */

    public abstract String getDefiningDocumentId();

    /**
     * <p class="changed_added_2_2">Return the immutable id for the
     * default node that should be activated when this flow is
     * entered.</p>
     *
     * @since 2.2
     */
    
    public abstract String getStartNodeId();

    /**
     * <p class="changed_added_2_2">Return the {@code MethodExpression}
     * that must be called by the runtime as the last thing that happens
     * before exiting this flow.  Any {@link FlowScoped} beans declared
     * for this flow must remain in scope until after control returns
     * from the method referenced by this {@code MethodExpression}.</p>

     * <div class="changed_added_2_2">

     * </div>

     * @since 2.2
     */

    public abstract MethodExpression getFinalizer();

    /**
     * <p class="changed_added_2_2">Return the {@code MethodExpression}
     * that must be called by the runtime immediately after activating
     * any {@link FlowScoped} beans declared for this flow.</p>

     * <div class="changed_added_2_2">

     * </div>

     * @since 2.2
     */
    public abstract MethodExpression getInitializer();
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Graph properties">       

    /**
     * <p class="changed_added_2_2">Return an immutable data structure
     * containing the inbound parameters that have been declared for
     * this flow.  See {@link FlowHandler#transition} for the
     * specification of how these parameters are used.  Inbound
     * parameters are associated with a specific flow instance, while
     * outbound parameters are associated with a {@link FlowCallNode}
     * that causes the transition to a new flow.</p>

     * <div class="changed_added_2_2">

     * </div>

     * @since 2.2
     */

    public abstract Map<String, Parameter> getInboundParameters();

    /**
     * <p class="changed_added_2_2">Return an immutable data structure
     * containing all of the view nodes declared for this flow.</p>

     * <div class="changed_added_2_2">

     * </div>

     * @since 2.2
     */
    public abstract List<ViewNode> getViews();
    
    /**
     * <p class="changed_added_2_2">Return an immutable data structure
     * containing all of the return nodes declared for this flow.</p>

     * <div class="changed_added_2_2">

     * </div>

     * @since 2.2
     */
    public abstract Map<String,ReturnNode> getReturns();
    
    /**
     * <p class="changed_added_2_2">Return an immutable data structure
     * containing all of the switch nodes declared for this flow.</p>

     * <div class="changed_added_2_2">

     * </div>

     * @since 2.2
     */
    public abstract Map<String,SwitchNode> getSwitches();
    
    /**
     * <p class="changed_added_2_2">Return an immutable data structure
     * containing all the flow call nodes declared for this flow.</p>

     * <div class="changed_added_2_2">

     * </div>

     * @since 2.2
     */
    public abstract Map<String,FlowCallNode> getFlowCalls();
    
    /**
     * <p class="changed_added_2_2">Return the {@link FlowCallNode} that
     * represents calling the {@code targetFlow} from this flow, or
     * {@code null} if {@code targetFlow} cannot be reached from this
     * flow.</p>

     * <div class="changed_added_2_2">

     * </div>

     * @since 2.2
     */
    public abstract FlowCallNode getFlowCall(Flow targetFlow);

    /**
     * <p class="changed_added_2_2">Return an immutable data structure
     * containing all the method call nodes declared for this flow.</p>

     * <div class="changed_added_2_2">

     * </div>

     * @since 2.2
     */
    public abstract List<MethodCallNode> getMethodCalls();

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Graph navigation">
    
    public abstract FlowNode getNode(String nodeId);
    
    public abstract Map<String, Set<NavigationCase>> getNavigationCases();
    
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
    
    public abstract String getClientWindowFlowId(ClientWindow curWindow);
    
    // </editor-fold>

}
