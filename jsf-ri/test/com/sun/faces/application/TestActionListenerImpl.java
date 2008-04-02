/*
 * $Id: TestActionListenerImpl.java,v 1.1 2003/04/04 18:42:55 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestActionListenerImpl.java

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
 *  <B>TestActionListenerImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestActionListenerImpl.java,v 1.1 2003/04/04 18:42:55 rkitain Exp $
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

// NOTE: These tests work in conjunction with web/test/WEB-INF/NavigationConfig.xml file..

    public void testProcessAction() {
        FacesContext context = getFacesContext();


        System.out.println("Testing With Action Literal Set...");

        UICommand command = new UICommand();
        command.setAction("failure");
        context.setTree(new SimpleTreeImpl(context, "/login.jsp"));

        ActionListenerImpl actionListener = new ActionListenerImpl();
        ActionEvent actionEvent = new ActionEvent(command, "login");

        actionListener.processAction(actionEvent);

        String newTreeId = context.getTree().getTreeId();
        assertTrue(newTreeId.equals("/login-retry.jsp"));


        System.out.println("Testing With Action *and* ActionRef Set...");

        command = new UICommand();
        command.setAction("failure");
        command.setActionRef("UserBean");

        actionListener.processAction(actionEvent);

        newTreeId = context.getTree().getTreeId();

        // expected outcome should be tree id corresponding to "page/outcome" search..

        assertTrue(newTreeId.equals("/login-retry.jsp"));
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

} // end of class TestActionListenerImpl
