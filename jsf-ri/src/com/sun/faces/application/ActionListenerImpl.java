/*
 * $Id: ActionListenerImpl.java,v 1.9 2004/02/06 18:54:13 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    protected static Log log = LogFactory.getLog(ActionListenerImpl.class);


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
        if (log.isDebugEnabled()) {
            log.debug("processAction(" + event.getComponent().getId() + ")");
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
                throw new FacesException(e);
            } catch (EvaluationException e) {
                throw new FacesException(e);
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
