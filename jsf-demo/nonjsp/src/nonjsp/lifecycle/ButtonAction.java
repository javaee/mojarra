/*
 * $Id: ButtonAction.java,v 1.1 2003/02/13 23:34:29 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ButtonAction.java

package nonjsp.lifecycle;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.tree.TreeFactory;
import javax.faces.FactoryFinder;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.mozilla.util.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * <B>ButtonAction</B> is a class that implements an ActionListener.
 * When a specific component fires an event the listener grabs the
 * event and handles it appropriately. In this case, a new xul page
 * is loaded when a button is pressed.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ButtonAction.java,v 1.1 2003/02/13 23:34:29 horwat Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ButtonAction implements ActionListener {
    //
    // Protected Constants
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ButtonAction.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ButtonAction() {
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    
    // This listener will process events after the phase specified.
    
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    public void processAction(ActionEvent event) {
        trace("Action.processAction : actionCommand : "+
            event.getActionCommand());

        //set the component tree to next xul page
        String treeId = null;

	FacesContext context = FacesContext.getCurrentInstance();
	if (event.getActionCommand().equals("Hello")) {
		treeId = "/hi.xul";
            }
        trace("Action: TreeId: " + treeId);

        if (null != treeId) {
            TreeFactory treeFactory = (TreeFactory)
                FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
            Assert.assert_it(null != treeFactory);
            context.setTree(treeFactory.getTree(context,treeId));

            HttpServletRequest request = (HttpServletRequest)
                context.getServletRequest();
            request.setAttribute("javax.servlet.include.path_info", treeId);

	    context.renderResponse();
        }

    }

    //Logging
    private void trace(String msg) {
	if (log.isTraceEnabled()) {
            log.trace(msg);
	}
    }

} // end of class ButtonAction
