/*
 * $Id: VariableResolverImpl.java,v 1.20.28.3 2007/04/27 21:27:40 ofung Exp $
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

package com.sun.faces.el;

import com.sun.faces.RIConstants;

import com.sun.faces.application.ApplicationAssociate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;


/**
 * <p>Concrete implementation of <code>VariableResolver</code>.</p>
 */

public class VariableResolverImpl extends VariableResolver {

    private static final Log log = LogFactory.getLog(
        VariableResolverImpl.class);

    //
    // Relationship Instance Variables
    // 

    // Specified by javax.faces.el.VariableResolver.resolveVariable()
    public Object resolveVariable(FacesContext context, String name)
        throws EvaluationException {

        ExternalContext ec = context.getExternalContext();

        if (RIConstants.APPLICATION_SCOPE.equals(name)) {
            return (ec.getApplicationMap());
        } else if (RIConstants.COOKIE_IMPLICIT_OBJ.equals(name)) {
            return (ec.getRequestCookieMap());
        } else if (RIConstants.FACES_CONTEXT_IMPLICIT_OBJ.equals(name)){
            return (context);
        } else if (RIConstants.HEADER_IMPLICIT_OBJ.equals(name)) {
            return (ec.getRequestHeaderMap());
        } else if (RIConstants.HEADER_VALUES_IMPLICIT_OBJ.equals(name)){
            return (ec.getRequestHeaderValuesMap());
        } else if (RIConstants.INIT_PARAM_IMPLICIT_OBJ.equals(name)) {
            return (ec.getInitParameterMap());
        } else if (RIConstants.PARAM_IMPLICIT_OBJ.equals(name)) {
            return (ec.getRequestParameterMap());
        } else if (RIConstants.PARAM_VALUES_IMPLICIT_OBJ.equals(name)) {
            return (ec.getRequestParameterValuesMap());
        } else if (RIConstants.REQUEST_SCOPE.equals(name)) {
            return (ec.getRequestMap());
        } else if (RIConstants.SESSION_SCOPE.equals(name)) {
            return (ec.getSessionMap());
        } else if (RIConstants.VIEW_IMPLICIT_OBJ.equals(name)) {
            return (context.getViewRoot());
        } else {
            // do the scoped lookup thing
            Object value = null;

            if (null == (value = ec.getRequestMap().get(name))) {
                if (null == (value = ec.getSessionMap().get(name))) {
                    if (null == (value = ec.getApplicationMap().get(name))) {
// if it's a managed bean try and create it
			ApplicationAssociate associate = 
			    ApplicationAssociate.getInstance(context.getExternalContext());
			
                        if (null != associate) {
                            value =
                            associate.createAndMaybeStoreManagedBeans(context, 
								      name);
                        }
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("resolveVariable: Resolved variable:" + value);
            }
            return (value);
        }

    }
}
