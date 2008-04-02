/*
 * $Id: NavigationHandlerImpl.java,v 1.1 2003/04/01 17:39:06 rkitain Exp $
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
        context = FacesContext.getCurrentInstance();

        NavigationConfig navConfig = (NavigationConfig)context.getExternalContext().getApplicationMap().
            get("navConfig");
        if (null == navConfig) {
            throw new RuntimeException(Util.getExceptionMessage(Util.NULL_CONFIGURATION_ERROR_MESSAGE_ID));
        }

//PENDING(rogerk) This is only one case;  Thee will be more when the spec is fleshed out;

        String newTreeId = navConfig.getTreeIdByPageActionOutcome(treeId, actionRef, outcome);

        if (newTreeId != null) {
            TreeFactory treeFactory = (TreeFactory)
                FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
            Assert.assert_it(null != treeFactory);
            context.setTree(treeFactory.getTree(context, newTreeId)); 
        }
    }
}
