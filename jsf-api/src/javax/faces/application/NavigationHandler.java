/*
 * $Id: NavigationHandler.java,v 1.15 2007/04/27 22:00:02 ofung Exp $
 */

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

package javax.faces.application;


import javax.faces.context.FacesContext;


/**
 * <p>A <strong>NavigationHandler</strong> is passed the outcome string
 * returned by an application action invoked for this application, and will
 * use this (along with related state information) to choose the view to
 * be displayed next.</p>
 *
 * <p>A default implementation of <code>NavigationHandler</code> must be
 * provided by the JSF implementation, which will be utilized unless
 * <code>setNavigationHandler()</code> is called to establish a different one.
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
     * <p>Perform navigation processing based on the state information
     * in the specified {@link FacesContext}, plus the outcome string
     * returned by an executed application action.</p>
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


}
