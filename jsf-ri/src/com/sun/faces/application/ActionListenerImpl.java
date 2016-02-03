/*
 * $Id: ActionListenerImpl.java,v 1.10.34.2 2007/04/27 21:27:37 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
		throw new FacesException
                    (binding.getExpressionString() + ": " + e, e);
	    }
	    catch (EvaluationException e) {
		throw new FacesException
                    (binding.getExpressionString() + ": " + e, e);
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
