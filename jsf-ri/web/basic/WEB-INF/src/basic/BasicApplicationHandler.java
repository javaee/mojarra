/*
 * $Id: BasicApplicationHandler.java,v 1.11 2002/10/10 17:27:45 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;

import java.util.SortedMap;
import javax.faces.FacesException;         // FIXME - subpackage?
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.faces.FactoryFinder;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.event.FormEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.CommandEvent;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.RIConstants;

public class BasicApplicationHandler implements ApplicationHandler{

    public boolean processEvent(FacesContext context, FacesEvent facesEvent) {

	if (!(facesEvent instanceof FormEvent) &&
            !(facesEvent instanceof CommandEvent)) {
	    return true;
	}
  
        boolean returnValue = true;
        String treeId = null;

        if (facesEvent instanceof FormEvent) {
	    FormEvent formEvent = (FormEvent) facesEvent;
	    if (formEvent.getCommandName().equals("login")) {
	        treeId = "/welcome.jsp"; 
	    } else if (formEvent.getCommandName().equals("back")) {
	        treeId = "/Faces_Basic.jsp";
	    } else if (formEvent.getCommandName().equals("standardRenderKitSubmit")) {
		treeId = "/StandardRenderKit.html";
	    }
	    else {
		return true;
	    }

            returnValue = true;
        } else if (facesEvent instanceof CommandEvent) {
            CommandEvent commandEvent = (CommandEvent)facesEvent;
            UIComponent c = commandEvent.getComponent();
            if (c.getAttribute("target") != null) {
                treeId = (String)c.getAttribute("target");
                returnValue = true;
            }
        } 

        if (null != treeId) {
            TreeFactory treeFactory = (TreeFactory)
            FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
            Assert.assert_it(null != treeFactory);
            context.setResponseTree(treeFactory.getTree(context,treeId));
        }

        return returnValue;
    }    
}
