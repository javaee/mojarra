/*
 * $Id: ActionListenerImpl.java,v 1.11 2005/04/05 20:25:13 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.faces.RIConstants;
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

    // Log instance for this class
    private static Logger logger;
    static {
        logger = Util.getLogger(Util.FACES_LOGGER);
    }

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

    public void processAction(ActionEvent event) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("processAction(" + event.getComponent().getId() + ")");
        }
        UIComponent source = event.getComponent();
        ActionSource actionSource = (ActionSource) source;
        FacesContext context = FacesContext.getCurrentInstance();

        Application application = context.getApplication();

        String outcome = null;
        MethodBinding binding = null;

	binding = actionSource.getAction();
	if (binding != null) {
	    try {
		outcome = (String) binding.invoke(context, null);
	    } catch (MethodNotFoundException e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
		throw new FacesException
                    (binding.getExpressionString() + ": " + e.getMessage(), e);
	    }
	    catch (EvaluationException e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
		throw new FacesException
                    (binding.getExpressionString() + ": " + e.getMessage(), e);
	    }
	}

        // Retrieve the NavigationHandler instance..

        NavigationHandler navHandler = application.getNavigationHandler();

        // Invoke nav handling..

        navHandler.handleNavigation(context,
                                    (null != binding) ?
                                    binding.getExpressionString() : null,
                                    outcome);

        // Trigger a switch to Render Response if needed
        context.renderResponse();

    }
}
