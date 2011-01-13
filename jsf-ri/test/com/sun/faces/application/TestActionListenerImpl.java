/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TestActionListenerImpl.java

package com.sun.faces.application;
import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.event.ActionEvent;
import java.util.Locale;

/**
 *
 *  <B>TestActionListenerImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
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
        UIViewRoot page = Util.getViewHandler(context).createView(context, null);
        page.setViewId("/login.jsp");
        page.setLocale(Locale.US);
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

        page = Util.getViewHandler(context).createView(context, null);
        page.setViewId("/login.jsp");
        page.setLocale(Locale.US);
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
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(context, null);
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

