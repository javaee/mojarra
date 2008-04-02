/*
 * $Id: TestNavigation.java,v 1.1 2003/04/01 17:39:14 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestNavigation.java

package com.sun.faces.application;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.application.Action;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.tree.SimpleTreeImpl;


import com.sun.faces.ServletFacesTestCase;

/**
 *
 *  <B>TestNavigation</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestNavigation.java,v 1.1 2003/04/01 17:39:14 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestNavigation extends ServletFacesTestCase
{
//
// Protected Constants
//

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

    public TestNavigation() {super("TestNavigation");}
    public TestNavigation(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

    public void testNavigationHandler() {
        NavigationConfig navConfig = new NavigationConfig();

        FacesContext context = getFacesContext();
        context.getExternalContext().getApplicationMap().
            put("navConfig", navConfig);

        NavigationHandlerImpl navHandler = new NavigationHandlerImpl();
        
        context.setTree(new SimpleTreeImpl(context, "/login.jsp"));
        navHandler.handleNavigation(context, "UserBean", "success");
        String newTreeId = context.getTree().getTreeId();
        assertTrue(newTreeId.equals("/home.jsp"));
    }

    public void testFromActionListener() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.setTree(new SimpleTreeImpl(context, "/login.jsp"));
        UserBean user = new UserBean();
        context.getExternalContext().getApplicationMap().put("UserBean", user);

        assertTrue(user == context.getExternalContext().getApplicationMap().get("UserBean"));

        UICommand command = new UICommand();
        command.setActionRef("UserBean");
        ActionEvent actionEvent = new ActionEvent(command, "login");

        ActionListenerImpl actionListener = new ActionListenerImpl();
        actionListener.processAction(actionEvent); 
    }

    public void testIllegalArgException() {
        boolean exceptionThrown = false;

        FacesContext context = FacesContext.getCurrentInstance();
        context.setTree(new SimpleTreeImpl(context, "/login.jsp"));
        UserBean user = new UserBean();
        context.getExternalContext().getApplicationMap().put("UserBean", user);

        assertTrue(user == context.getExternalContext().getApplicationMap().get("UserBean"));

        UICommand command = new UICommand();
        command.setActionRef("Foo");
        ActionEvent actionEvent = new ActionEvent(command, "login");

        ActionListenerImpl actionListener = new ActionListenerImpl();
        try {
            actionListener.processAction(actionEvent);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

} // end of class TestNavigation


class UserBean extends Action {
    public String invoke() {
        return "success";
    }
}
