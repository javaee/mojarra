/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces.application;

import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

/**
 * <p
 * class="changed_added_2_0"><strong>ConfigurableNavigationHandler</strong>
 * extends the contract of {@link NavigationHandler} to allow runtime
 * inspection of the {@link NavigationCase}s that make up the rule-base
 * for navigation.  An implementation compliant with the version of the
 * specification in which this class was introduced (or a later version)
 * must make it so that its <code>NavigationHandler</code> is an
 * extension of this class.</p>
 *
 * @since 2.0
 */
public abstract class ConfigurableNavigationHandler extends NavigationHandler {
    
    /**
     * <p class="changed_added_2_0">Return the {@link NavigationCase}
     * representing the navigation that would be taken had {@link
     * NavigationHandler#handleNavigation} been called with the same
     * arguments or <code>null</code> if there is no such case.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param fromAction The action binding expression that was evaluated
     *  to retrieve the specified outcome, or <code>null</code> if the
     *  outcome was acquired by some other means
     * @param outcome The logical outcome returned by a previous invoked
     *  application action (which may be <code>null</code>)
     *
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     *
     * @since 2.0
     */ 

    public abstract NavigationCase getNavigationCase(FacesContext context,
            String fromAction, 
            String outcome);
    

    /**
     * <p class="changed_added_2_0">Return a <code>Map&lt;String,
     * Set&lt;NavigationCase&gt;&gt;</code> where the keys are
     * <code>&lt;from-view-id&gt;</code> values and the values are
     * <code>Set&lt;NavigationCase&gt;</code> where each element in the
     * Set is a <code>NavigationCase</code> that applies to that
     * <code>&lt;from-view-id&gt;</code>.  The implementation must
     * support live modifications to this <code>Map</code>.</p>
     *
     * @since 2.0
     */
    public abstract Map<String, Set<NavigationCase>> getNavigationCases();
    
    /**
     * <p class="changed_added_2_0">A convenience method to signal the
     * JavaServer Faces implementation to perform navigaton
     * with the provided outcome. When the NavigationHandler is invoked,
     * the current viewId is treated as the "from viewId" and the "from action"
     * is null.</p>
     * 
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     */
    public void performNavigation(String outcome) {
        this.handleNavigation(FacesContext.getCurrentInstance(), null, outcome);
    }

}
