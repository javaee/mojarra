/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.sandbox.handler;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/*******************************************************************************
 * <p>
 * This navigation handler will defer to the default navigation handler unless
 * the action outcome starts with a forward slash ("/"). If the outcome does
 * start with a forward slash, then the resulting view-id that is forwarded to
 * will just be the outcome suffixed with the JSF default suffix. E.g. - An
 * outcome of "/home" would resolve to "/home.jsp" or "/home.xhtml" (for
 * facelets). An outcome of "home" would use the navigation rules defined in
 * faces-confix.xml to resolve.
 * </p>
 * 
 * <p>
 * Configuration:
 * </p>
 * 
 * <pre>
 *  &lt;application&gt;
 *     ...
 *     &lt;navigation-handler&gt;com.iecokc.faces.handlers.navigation.ViewIdIsOutcomeNavigationHandler&lt;/navigation-handler&gt;
 *  &lt;/application&gt;
 * </pre>
 * 
 * @author Blevins
 * 
 */
public class ViewIdIsOutcomeNavigationHandler extends NavigationHandler {
    private NavigationHandler base;

    private String defaultSuffix;

    public ViewIdIsOutcomeNavigationHandler(NavigationHandler base) {
        super();
        this.base = base;
    }

    /***************************************************************************
     * <p>
     * Handle the navigation request implied by the specified parameters.
     * </p>
     * <p>
     * Outcomes starting with a forward slash are directly converted into a
     * view-id.
     * </p>
     * 
     * @param context
     *            <code>FacesContext</code> for the current request
     * @param fromAction
     *            The action binding expression that was evaluated to retrieve
     *            the specified outcome (if any)
     * @param outcome
     *            The logical outcome returned by the specified action
     * 
     * @exception IllegalArgumentException
     *                if the configuration information for a previously saved
     *                position cannot be found
     * @exception IllegalArgumentException
     *                if an unknown State type is found
     */
    @Override
    public void handleNavigation(FacesContext context, String fromAction,
            String outcome) {

        // Get the default prefix
        if (defaultSuffix == null) {
            defaultSuffix = context.getExternalContext().getInitParameter(
                    ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
        }

        // Handle outcomes that start with a forward slash
        if (outcome != null && outcome.startsWith("/")) {
            String viewId = outcome + defaultSuffix;
            UIViewRoot view = context.getApplication().getViewHandler()
                    .createView(context, viewId);
            view.setViewId(viewId);
            context.setViewRoot(view);
            return;
        }

        // Handle using default mechanisms
        this.base.handleNavigation(context, fromAction, outcome);
    }

}
