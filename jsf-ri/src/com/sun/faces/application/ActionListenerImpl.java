/*
 * $Id: ActionListenerImpl.java,v 1.2 2003/04/03 18:27:49 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import javax.faces.FactoryFinder;
import javax.faces.application.Action;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ValueBinding;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.sun.faces.el.ValueBindingImpl;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.VariableResolverImpl;
import com.sun.faces.util.Util;

/**
 * This action listener implementation processes action events during the 
 * <strong>Invoke Application</strong> of the request processing lifecycle. 
 * Specifically, it determines the logical outcome of the current action,
 * and invokes the default navigation handling mechanism.
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
        UICommand command = (UICommand)source;
        FacesContext context = FacesContext.getCurrentInstance();

        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
            FactoryFinder.APPLICATION_FACTORY);
        Application application = aFactory.getApplication();

        String outcome = null;

        outcome = command.getAction();

        // If the action string is not set, determine the outcome through the action reference.  
        // The action reference is used to retrieve an Action instance.  The invoke() method on 
        // the Action instance returns the outcome.  If an action could not be determined,
        // throw an exception.
 
        String actionRef = null;
        ValueBinding binding = null;
        Object action = null;
        if (null == outcome) {
            actionRef = command.getActionRef();
            if (actionRef != null) {
                binding = application.getValueBinding(actionRef);
                if (binding != null) {
                    try {
                        action = binding.getValue(context);
                    } catch (PropertyNotFoundException e) {
                    }
                }
            }
            if (null == action || !(action instanceof Action)) {
                Object[] obj = new Object[1];
                obj[0] = actionRef;
                throw new IllegalArgumentException(Util.getExceptionMessage(
                    Util.NO_ACTION_FROM_ACTIONREF_ERROR_MESSAGE_ID, obj));
            }
 
            outcome = ((Action)action).invoke();
        }
            

        // Retrieve the NavigationHandler instance..

        NavigationHandler navHandler = application.getNavigationHandler();

        // Invoke nav handling..

        navHandler.handleNavigation(context, actionRef, outcome); 
    }
}
