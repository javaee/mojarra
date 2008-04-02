/*
 * $Id: TestActionListenerImpl.java,v 1.20 2004/02/06 18:56:28 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestActionListenerImpl.java

package com.sun.faces.application;

import com.sun.faces.ServletFacesTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.event.ActionEvent;


/**
 *
 *  <B>TestActionListenerImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestActionListenerImpl.java,v 1.20 2004/02/06 18:56:28 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

/**
 * This class tests the <code>ActionListenerImpl</code> class
 * functionality.  It uses the xml configuration file:
 * <code>web/test/WEB-INF/faces-navigation.xml</code>.
 */
public class TestActionListenerImpl extends ServletFacesTestCase {

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

    public TestActionListenerImpl() {
        super("TestActionListenerImpl");
    }


    public TestActionListenerImpl(String name) {
        super(name);
    }
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

    public void testProcessAction() {
        loadFromInitParam("/WEB-INF/faces-navigation.xml");
        FacesContext context = getFacesContext();

        System.out.println("Testing With Action Literal Set...");

        UICommand command = new UICommand();
        command.setAction(
            context.getApplication().createMethodBinding(
                "#{newCustomer.loginRequired}", null));
        UIViewRoot page = new UIViewRoot();
        page.setViewId("/login.jsp");
        context.setViewRoot(page);

        ActionListenerImpl actionListener = new ActionListenerImpl();
        ActionEvent actionEvent = new ActionEvent(command);

        actionListener.processAction(actionEvent);

        String newViewId = context.getViewRoot().getViewId();
        assertTrue(newViewId.equals("/must-login-first.jsp"));

        System.out.println("Testing With Action Set...");

        command = new UICommand();
        MethodBinding binding =
            context.getApplication().createMethodBinding("#{userBean.login}",
                                                         null);
        command.setAction(binding);

        UserBean user = new UserBean();
        context.getExternalContext().getSessionMap().put("userBean", user);
        assertTrue(
            user ==
            context.getExternalContext().getSessionMap().get("userBean"));

        page = new UIViewRoot();
        page.setViewId("/login.jsp");
        context.setViewRoot(page);

        actionEvent = new ActionEvent(command);
        actionListener.processAction(actionEvent);

        newViewId = context.getViewRoot().getViewId();
        // expected outcome should be view id corresponding to "page/outcome" search..

        assertTrue(newViewId.equals("/home.jsp"));
    }


    public void testIllegalArgException() {
        boolean exceptionThrown = false;

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot page = new UIViewRoot();
        page.setViewId("/login.jsp");
        context.setViewRoot(page);
        UserBean user = new UserBean();
        context.getExternalContext().getApplicationMap().put("UserBean", user);

        assertTrue(
            user ==
            context.getExternalContext().getApplicationMap().get("UserBean"));

        UICommand command = new UICommand();
        MethodBinding binding =
            context.getApplication().createMethodBinding("#{UserBean.noMeth}",
                                                         null);
        command.setAction(binding);
        ActionEvent actionEvent = new ActionEvent(command);

        ActionListenerImpl actionListener = new ActionListenerImpl();
        try {
            actionListener.processAction(actionEvent);
        } catch (FacesException e) {
            assertTrue(e.getCause() instanceof MethodNotFoundException);
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


    public static class UserBean extends Object {

        public String login() {
            return ("success");
        }

    }

} // end of class TestActionListenerImpl

