/*
 * $Id: BasicApplicationHandler.java,v 1.1 2002/06/18 18:23:31 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;

import java.util.SortedMap;
import javax.faces.FacesException;         // FIXME - subpackage?
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.faces.FactoryFinder;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.event.FormEvent;
import javax.faces.event.CommandEvent;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

public class BasicApplicationHandler implements ApplicationHandler{

    
    /**
     * <p>Process a command event that has been queued for the application.
     * <strong>FIXME</strong> - does the application need to provide any
     * feedback to the lifecycle state machine?</p>
     *
     * @param context FacesContext for the current request
     * @param event CommandEvent to be processed
     */
    public void commandEvent(FacesContext context, CommandEvent event) {
    }    


    /**
     * <p>Process a form event that has been queued for the application.
     * <strong>FIXME</strong> - does the application need to provide any
     * feedback to the lifecycle state machine?</p>
     *
     * @param context FacesContext for the current request
     * @param event FormEvent to be processed
     */
    public void formEvent(FacesContext context, FormEvent event) {
        
        String treeId = "/welcome.xul";
        TreeFactory treeFactory = (TreeFactory)
	FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
        Assert.assert_it(null != treeFactory);
        
        ServletContext sc = context.getServletContext();
        context.setResponseTree(treeFactory.createTree(sc,treeId));  
    }    
}
