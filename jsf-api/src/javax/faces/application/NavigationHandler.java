/*
 * $Id: NavigationHandler.java,v 1.9 2004/01/26 20:48:52 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
