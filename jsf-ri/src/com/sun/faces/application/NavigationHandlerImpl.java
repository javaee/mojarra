/*
 * $Id: NavigationHandlerImpl.java,v 1.4 2003/04/08 18:08:46 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import org.mozilla.util.Assert;

import javax.faces.FactoryFinder;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.tree.TreeFactory;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;



public class NavigationHandlerImpl extends NavigationHandler {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    /**
     * Constructor 
     */
    public NavigationHandlerImpl () {
        super();
    }

    public void handleNavigation(FacesContext context, String actionRef, String outcome) {

        String treeId = context.getTree().getTreeId();

        NavigationConfig navConfig = (NavigationConfig)context.getExternalContext().getApplicationMap().
            get(RIConstants.NAVIGATION_CONFIG_ATTR);
        if (null == navConfig) {
            throw new RuntimeException(Util.getExceptionMessage(Util.NULL_CONFIGURATION_ERROR_MESSAGE_ID));
        }

        String newTreeId = null;

        newTreeId = navConfig.getTreeId(treeId, actionRef, outcome);

        if (newTreeId != null) {
            TreeFactory treeFactory = (TreeFactory)
                FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
            Assert.assert_it(null != treeFactory);
            context.setTree(treeFactory.getTree(context, newTreeId)); 
        }
    }
}
