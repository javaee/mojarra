/*
 * $Id: TestActionListenerImpl.java,v 1.14 2003/10/02 06:50:07 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestActionListenerImpl.java

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.config.ConfigListener;
import com.sun.faces.config.ConfigNavigationCase;
import com.sun.faces.context.FacesContextImpl;

import com.sun.faces.util.DebugUtil;

import java.util.List;

import javax.faces.application.Action;
import javax.faces.component.UICommand;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.apache.cactus.WebRequest;
import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>TestActionListenerImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestActionListenerImpl.java,v 1.14 2003/10/02 06:50:07 jvisvanathan Exp $
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
public class TestActionListenerImpl extends ServletFacesTestCase
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

    public TestActionListenerImpl() {super("TestActionListenerImpl");}
    public TestActionListenerImpl(String name) {super(name);}
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
	loadFromInitParam("WEB-INF/faces-navigation.xml");
        FacesContext context = getFacesContext();

        System.out.println("Testing With Action Literal Set...");

        UICommand command = new UICommand();
        command.setAction("loginRequired");
        UIViewRoot page = new UIViewRoot();
        page.setViewId("/login.jsp");
        context.setViewRoot(page);

        ActionListenerImpl actionListener = new ActionListenerImpl();
        ActionEvent actionEvent = new ActionEvent(command);

        actionListener.processAction(actionEvent);

        String newViewId = context.getViewRoot().getViewId();
        assertTrue(newViewId.equals("/must-login-first.jsp"));

        System.out.println("Testing With ActionRef Set...");

        command = new UICommand();
        command.setActionRef("userBean.login");

        UserBean user = new UserBean();
        LoginAction login  = new LoginAction();
        user.setLogin(login);
        context.getExternalContext().getSessionMap().put("userBean", user);
        assertTrue(user == context.getExternalContext().getSessionMap().get("userBean"));

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

        assertTrue(user == context.getExternalContext().getApplicationMap().get("UserBean"));

        UICommand command = new UICommand();
        command.setActionRef("Foo");
        ActionEvent actionEvent = new ActionEvent(command);

        ActionListenerImpl actionListener = new ActionListenerImpl();
        try {
            actionListener.processAction(actionEvent);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    public static class LoginAction extends Action {
        public String invoke() {
            return "success";
        }
    }

    public static class UserBean extends Object {
        private Action login = null;

        public void setLogin(Action login) {
            this.login = login;
        }
        public Action getLogin() {
            return login;
        }
    }
} // end of class TestActionListenerImpl

