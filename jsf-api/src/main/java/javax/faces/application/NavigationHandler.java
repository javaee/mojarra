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


import javax.faces.context.FacesContext;


/**
 * <p><span class="changed_modified_2_0 changed_modified_2_2">A</a> 
 * <strong>NavigationHandler</strong> is passed the outcome string
 * returned by an application action invoked for this application, and will
 * use this (along with related state information) to choose the view to
 * be displayed next.</p>
 *
 * <p>A default implementation of <code>NavigationHandler</code> must be
 * provided by the JSF implementation, which will be utilized unless
 * <code>setNavigationHandler()</code> is called to establish a different one.
 * <span class="changed_added_2_0">An implementation
 * of this class must be thread-safe.</span>
 * This default instance will compare the view identifier of the current
 * view, the specified action binding, and the specified outcome against
 * any navigation rules provided in <code>faces-config.xml</code> file(s).
 * If a navigation case matches, the current view will be changed by a call
 * to <code>FacesContext.setViewRoot()</code>.  Note that a <code>null</code>
 * outcome value will never match any navigation rule, so it can be used as an
 * indicator that the current view should be redisplayed.</p>
 */

public abstract class NavigationHandler {


    /**
     * <p><span class="changed_modified_2_0">Perform</span> navigation
     * processing based on the state information in the specified {@link
     * FacesContext}, plus the outcome string returned by an executed
     * application action.</p>
     *
     * <p class="changed_added_2_0">If the implementation class also
     * extends {@link ConfigurableNavigationHandler}, the implementation
     * must guarantee that the logic used in a call to {@link
     * ConfigurableNavigationHandler#getNavigationCase} is used in this
     * method to determine the correct navigation.</p>
     *
     * <p class="changed_added_2_0">This method must set the render targets
     * (used in partial rendering) to <code>render all </code>
     * invoking {@link javax.faces.context.PartialViewContext#setRenderAll})
     * if the view identifier has changed as the result of an application
     * action (to take into account <code>Ajax requests</code>).</p> 
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
     */
    public abstract void handleNavigation(FacesContext context,
                                          String fromAction,
                                          String outcome);
    
    /**
     * <p class="changed_added_2_2">Overloaded variant of {@link #handleNavigation(javax.faces.context.FacesContext, java.lang.String, java.lang.String)}
     * that allows the caller to provide the defining document id for a flow
     * to be entered by this navigation.  For backward compatibility with 
     * decorated {@code NavigationHandler} implementations that conform to an 
     * earlier version of the specification, an implementation is provided that
     * calls through to {@link #handleNavigation(javax.faces.context.FacesContext, java.lang.String, java.lang.String)},
     * ignoring the {@code toFlowDocumentId} parameter.</p>
     * 
     * @param context The {@link FacesContext} for the current request
     * @param fromAction The action binding expression that was evaluated
     *  to retrieve the specified outcome, or <code>null</code> if the
     *  outcome was acquired by some other means
     * @param outcome The logical outcome returned by a previous invoked
     *  application action (which may be <code>null</code>)
     * @param toFlowDocumentId The defining document id of the flow into which
     * this navigation will cause entry.
     *
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void handleNavigation(FacesContext context,
            String fromAction,
            String outcome,
            String toFlowDocumentId) {
        this.handleNavigation(context, fromAction, outcome);
    }


}
