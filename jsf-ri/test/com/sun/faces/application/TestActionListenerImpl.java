/*
 * $Id: TestActionListenerImpl.java,v 1.5 2003/05/08 23:13:30 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestActionListenerImpl.java

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.config.ConfigBase;
import com.sun.faces.config.ConfigListener;
import com.sun.faces.config.ConfigNavigationCase;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.tree.SimpleTreeImpl;

import com.sun.faces.util.DebugUtil;

import java.util.List;

import javax.faces.application.Action;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.apache.cactus.WebRequest;
import org.apache.cactus.server.ServletContextWrapper;
import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>TestActionListenerImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestActionListenerImpl.java,v 1.5 2003/05/08 23:13:30 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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

    private void loadConfigFile() {
        config.getServletContext().removeAttribute(RIConstants.CONFIG_ATTR);
	// clear out the renderKit factory
	FactoryFinder.releaseFactories();

        final String paramVal = "WEB-INF/faces-navigation.xml";

        // work around a bug in cactus where calling
        // config.setInitParameter() doesn't cause
        // servletContext.getInitParameter() to relfect the call.

        ServletContextWrapper sc =
            new ServletContextWrapper(config.getServletContext()) {
                public String getInitParameter(String theName) {
                    if (null != theName &&
                        theName.equals(RIConstants.CONFIG_FILES_INITPARAM)) {
                        return paramVal;
                    }
                    return super.getInitParameter(theName);
                }
            };

        ConfigListener configListener = new ConfigListener();
        ServletContextEvent e =
            new ServletContextEvent(sc);
        configListener.contextInitialized(e);

        System.out.println("NAV CASES LOADED:");
        ConfigBase cbase = (ConfigBase)config.getServletContext().getAttribute(RIConstants.CONFIG_ATTR);
        List navs = cbase.getNavigationCases();
        for (int i=0; i<navs.size(); i++) {
            ConfigNavigationCase nc = (ConfigNavigationCase)navs.get(i);
            System.out.println("<from-tree-id>:"+nc.getFromTreeId());
            System.out.println(" : <from-action-ref>:"+nc.getFromActionRef());
            System.out.println(" : <from-outcome>:"+nc.getFromOutcome());
            System.out.println(" : <to-tree-id>:"+nc.getToTreeId());
            System.out.println("----------------------------------------------");
        }
    }

    public void testProcessAction() {
        loadConfigFile();
        FacesContext context = getFacesContext();

        System.out.println("Testing With Action Literal Set...");

        UICommand command = new UICommand();
        command.setAction("loginRequired");
        context.setTree(new SimpleTreeImpl(context, "/login.jsp"));

        ActionListenerImpl actionListener = new ActionListenerImpl();
        ActionEvent actionEvent = new ActionEvent(command, "login");

        actionListener.processAction(actionEvent);

        String newTreeId = context.getTree().getTreeId();
        assertTrue(newTreeId.equals("/must-login-first.jsp"));

        System.out.println("Testing With ActionRef Set...");

        command = new UICommand();
        command.setActionRef("userBean.login");

        UserBean user = new UserBean();
        LoginAction login  = new LoginAction();
        user.setLogin(login);
        context.getExternalContext().getSessionMap().put("userBean", user);
        assertTrue(user == context.getExternalContext().getSessionMap().get("userBean"));

        context.setTree(new SimpleTreeImpl(context, "/login.jsp"));

        actionEvent = new ActionEvent(command, "register");
        actionListener.processAction(actionEvent);

        newTreeId = context.getTree().getTreeId();
        // expected outcome should be tree id corresponding to "page/outcome" search..

        assertTrue(newTreeId.equals("/home.jsp"));
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

