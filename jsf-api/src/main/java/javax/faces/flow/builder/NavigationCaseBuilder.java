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

import javax.el.ValueExpression;

/**
 * <p class="changed_added_2_2">Create a navigation case in the current {@link javax.faces.flow.Flow}.</p>
 * 
 * @since 2.2
 */
public abstract class NavigationCaseBuilder {

    /**
     * 
     * <p class="changed_added_2_2">Set the from-view-id of the current navigation case.</p>
     * 
     * @param fromViewId the from-view-id
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */

    public abstract NavigationCaseBuilder fromViewId(String fromViewId);
    
    /**
     * 
     * <p class="changed_added_2_2">Set the from-action of the current navigation case.</p>
     * 
     * @param fromAction the from-action
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */

    public abstract NavigationCaseBuilder fromAction(String fromAction);
    
    /**
     * 
     * <p class="changed_added_2_2">Set the from-outcome of the current navigation case.</p>
     * 
     * @param fromOutcome the from-outcome
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */

    public abstract NavigationCaseBuilder fromOutcome(String fromOutcome);
    
    /**
     * 
     * <p class="changed_added_2_2">Set the to-view-id of the current navigation case.</p>
     * 
     * @param toViewId the to-view-id
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */

    public abstract NavigationCaseBuilder toViewId(String toViewId);

    /**
     * 
     * <p class="changed_added_2_2">Set the to-flow-document-id of the current navigation case.</p>
     * 
     * @param toFlowDocumentId the to-flow-document-id
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */

    public abstract NavigationCaseBuilder toFlowDocumentId(String toFlowDocumentId);
    
    /**
     * 
     * <p class="changed_added_2_2">Set the if of the current navigation case.</p>
     * 
     * @param condition the <if>
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */

    public abstract NavigationCaseBuilder condition(String condition);
    
    /**
     * 
     * <p class="changed_added_2_2">Set the if of the current navigation case.</p>
     * 
     * @param condition the <if>
     * @throws NullPointerException if any of the parameters are {@code null}
     * @since 2.2
     */

    public abstract NavigationCaseBuilder condition(ValueExpression condition);
    
    /**
     * 
     * <p class="changed_added_2_2">Create a redirect within this navigation case.</p>
     * 
     * @since 2.2
     */

    public abstract RedirectBuilder redirect();
    
    /**
     * 
     * <p class="changed_added_2_2">Allows populating the redirect with parameters 
     * and setting the includeViewParams option.</p>
     * 
     * @since 2.2
     */
    
    public abstract class RedirectBuilder {
        
        /**
         * <p class="changed_added_2_2">Add a parameter to the redirect.</p>
         * 
         * @param name the name of the redirect parameter
         * @value the value of the redirect parameter.  May not be a {@code ValueExpression}.
         * @throws NullPointerException if any of the parameters are {@code null}
         * @since 2.2
         */

        public abstract RedirectBuilder parameter(String name, String value);
        
        /**
         * <p class="changed_added_2_2">Indicates the current redirect should include view parameters.</p>
         * 
         * @since 2.2
         */

        public abstract RedirectBuilder includeViewParams();
    
    }
    
}
