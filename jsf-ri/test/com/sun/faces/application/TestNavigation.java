/*
 * $Id: TestNavigation.java,v 1.4 2003/05/05 15:24:14 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestNavigation.java

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.config.ConfigBase;
import com.sun.faces.config.ConfigListener;
import com.sun.faces.config.ConfigNavigationCase;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.tree.SimpleTreeImpl;

import java.util.List;

import javax.faces.application.Action;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import org.apache.cactus.server.ServletContextWrapper;
import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;


import com.sun.faces.ServletFacesTestCase;

/**
 *
 *  <B>TestNavigation</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestNavigation.java,v 1.4 2003/05/05 15:24:14 rkitain Exp $
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
    private void loadConfigFile() {
        config.getServletContext().removeAttribute(RIConstants.CONFIG_ATTR);
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

    public void testNavigationHandler() {
        loadConfigFile();
        FacesContext context = getFacesContext();
        NavigationHandlerImpl navHandler = new NavigationHandlerImpl();
        ConfigBase cbase = (ConfigBase)config.getServletContext().getAttribute(RIConstants.CONFIG_ATTR);
        navHandler.initialize(cbase);
        
        System.out.println("Testing page==/login.jsp actionref==UserBean.login outcome==success search...");
        context.setTree(new SimpleTreeImpl(context, "/login.jsp"));
        navHandler.handleNavigation(context, "UserBean.login", "success");
        String newTreeId = context.getTree().getTreeId();
        System.out.println("NEWTREEID:"+newTreeId);
        assertTrue(newTreeId.equals("/home.jsp"));

        System.out.println("Testing page==/login.jsp actionref==null outcome==failure search...");
        context.setTree(new SimpleTreeImpl(context, "/login.jsp"));
        navHandler.handleNavigation(context, null, "failure");
        newTreeId = context.getTree().getTreeId();
        System.out.println("NEWTREEID:"+newTreeId);
        assertTrue(newTreeId.equals("/login-failure.jsp"));

        System.out.println("Testing page==/login.jsp actionref==UserBean.register outcome==null search...");
        context.setTree(new SimpleTreeImpl(context, "/login.jsp"));
        navHandler.handleNavigation(context, "UserBean.register", null);
        newTreeId = context.getTree().getTreeId();
        System.out.println("NEWTREEID:"+newTreeId);
        assertTrue(newTreeId.equals("/get-user-info.jsp"));

        System.out.println("Testing page==/foobarbaz actionref==SearchForm.go outcome==success search...");
        context.setTree(new SimpleTreeImpl(context, "/foobarbaz"));
        navHandler.handleNavigation(context, "SearchForm.go", "success");
        newTreeId = context.getTree().getTreeId();
        System.out.println("NEWTREEID:"+newTreeId);
        assertTrue(newTreeId.equals("/bar.jsp"));

        System.out.println("Testing page==/foobaz actionref==SearchForm.go outcome==success search...");
        context.setTree(new SimpleTreeImpl(context, "/foobaz"));
        navHandler.handleNavigation(context, "SearchForm.go", "success");
        newTreeId = context.getTree().getTreeId();
        System.out.println("NEWTREEID:"+newTreeId);
        assertTrue(newTreeId.equals("/beta.jsp"));

        System.out.println("Testing page==/movies/yes actionref==SearchForm.go outcome==success search...");
        context.setTree(new SimpleTreeImpl(context, "/movies/yes"));
        navHandler.handleNavigation(context, "SearchForm.go", "success");
        newTreeId = context.getTree().getTreeId();
        System.out.println("NEWTREEID:"+newTreeId);
        assertTrue(newTreeId.equals("/movies/movie-search-results.jsp"));

        System.out.println("Testing page==/anypage actionref==UserBean.newUser outcome==success search...");
        context.setTree(new SimpleTreeImpl(context, "/anypage"));
        navHandler.handleNavigation(context, "UserBean.newUser", "success");
        newTreeId = context.getTree().getTreeId();
        System.out.println("NEWTREEID:"+newTreeId);
        assertTrue(newTreeId.equals("/newUser.jsp"));
    }

    // This tests that the same <from-tree-id> element value existing in a seperate
    // navigation rule, gets combined with the other rules with the same <from-tree-id>.
    // Specifically, it will to make sure that after loading, there are the correct number of
    // cases with the common <from-tree-id>;
 
    public void testSeperateRule() {
        int cnt = 0;
        ConfigBase cbase = (ConfigBase)config.getServletContext().getAttribute(RIConstants.CONFIG_ATTR);
        List navs = cbase.getNavigationCases();
        for (int i=0; i<navs.size(); i++) {
            ConfigNavigationCase nc = (ConfigNavigationCase)navs.get(i);
            if (nc.getFromTreeId().equals("/login.jsp")) {
                cnt++;
            }
        }
        assertTrue(cnt == 4);
    }

    public void testFromActionListener() {
        loadConfigFile();
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

