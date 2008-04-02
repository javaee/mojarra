/*
 * $Id: ActionListenerImpl.java,v 1.6 2003/10/27 04:14:13 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import java.lang.reflect.InvocationTargetException;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.sun.faces.el.ValueBindingImpl;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.VariableResolverImpl;
import com.sun.faces.util.Util;

/**
 * This action listener implementation processes action events during the 
 * <em>Apply Request Values</em> or <em>Invoke Application</em>
 * phase of the request processing lifecycle (depending upon the
 * <code>immediate</code> property of the {@link ActionSource} that
 * queued this event.  It invokes the specified application action method,
 * and uses the logical outcome value to invoke the default navigation handler
 * mechanism to determine which view should be displayed next.</p>
 */
public class ActionListenerImpl implements ActionListener {


    //
    // Constructors and Initializers
    //
    public ActionListenerImpl() {
    }

    //
    // Class Methods
    //

    //
    // General Methods
    //

    public PhaseId getPhaseId() {
        return PhaseId.INVOKE_APPLICATION;
    }

    public void processAction(ActionEvent event) {

        UIComponent source = event.getComponent();
        ActionSource actionSource = (ActionSource)source;
        FacesContext context = FacesContext.getCurrentInstance();

        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
            FactoryFinder.APPLICATION_FACTORY);
        Application application = aFactory.getApplication();

        String outcome = null;

        outcome = actionSource.getAction();

        // If the action string is not set, determine the outcome
        // through the action reference.  The action reference is used
        // to retrieve an Action instance.  The invoke() method on the
        // Action instance returns the outcome.  If an action could not
        // be determined, throw an exception.
 
        String actionRef = null;
        MethodBinding binding = null;
        Object action = null;
        if (null == outcome) {
            actionRef = actionSource.getActionRef();
            if (actionRef != null) {
                binding = application.getMethodBinding(actionRef, null);
                if (binding != null) {
                    try {
                        outcome = (String) binding.invoke(context, null);
                    } catch (InvocationTargetException e) {
                        throw new FacesException(e.getTargetException());
                    }
                }
            }
        }
            

        // Retrieve the NavigationHandler instance..

        NavigationHandler navHandler = application.getNavigationHandler();

        // Invoke nav handling..

        navHandler.handleNavigation(context, actionRef, outcome); 

        // Trigger a switch to Render Response if needed
        context.renderResponse();

    }
}
