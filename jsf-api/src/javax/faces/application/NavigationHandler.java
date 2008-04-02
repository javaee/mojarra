/*
 * $Id: NavigationHandler.java,v 1.13 2005/08/22 22:07:51 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void handleNavigation(FacesContext context,
                                          String fromAction,
                                          String outcome);


}
