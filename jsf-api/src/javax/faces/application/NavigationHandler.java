/*
 * $Id: NavigationHandler.java,v 1.2 2003/03/13 01:11:54 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


import javax.faces.context.FacesContext;


/**
 * <p>A <strong>NavigationHandler</strong> is passed the outcome string
 * returned by an {@link Action} invoked for this application, and will use
 * this (along with related state information) to choose the component tree
 * to be displayed next.</p>
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
     * @param outcome The logical outcome returned by a previous {@link Action}
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>outcome</code> is <code>null</code>
     */
    public abstract void handleNavigation(FacesContext context,
                                          String outcome);


}
