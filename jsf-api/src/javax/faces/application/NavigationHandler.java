/*
 * $Id: NavigationHandler.java,v 1.5 2003/08/22 14:03:07 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


import javax.faces.context.FacesContext;


/**
 * <p>A <strong>NavigationHandler</strong> is passed the outcome string
 * returned by an {@link Action} invoked for this application, and will
 * use this (along with related state information) to choose the view to
 * be displayed next.</p>
 *
 * <p>The behavior requirements for the default implementation have not
 * yet been defined by the JSR-127 expert group, but will be documented here
 * when completed.</p>
 */

public abstract class NavigationHandler {


    /**
     * <p>Perform navigation processing based on the state information
     * in the specified {@link FacesContext}, plus the outcome string
     * returned by an executed {@link Action}.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param actionRef The action reference expression that was evaluated
     *  to retrieve the specified outcome, or <code>null</code> if the
     *  outcome was acquired by some other means
     * @param outcome The logical outcome returned by a previous {@link Action}
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>outcome</code> is <code>null</code>
     */
    public abstract void handleNavigation(FacesContext context,
                                          String actionRef,
                                          String outcome);


    // ---------------------------------------------------------- Static Methods


    /**
     * <p>Return the {@link NavigationHandler} instance for the
     * current application.</p>
     */
    public static NavigationHandler getCurrentInstance() {

	return (Application.getCurrentInstance().getNavigationHandler());

    }


}
