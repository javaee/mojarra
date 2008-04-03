/*
 * $Id: FacesContextFactoryImpl.java,v 1.21 2007/12/17 21:46:09 rlubke Exp $
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

package com.sun.faces.context;

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.RequestStateManager;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FacesContextFactoryImpl extends FacesContextFactory {

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

    public FacesContextFactoryImpl() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from FacesContextFactory
    //
    public FacesContext getFacesContext(Object sc,
                                        Object request,
                                        Object response,
                                        Lifecycle lifecycle)
        throws FacesException {

        try {
            Util.parameterNonNull(sc);
            Util.parameterNonNull(request);
            Util.parameterNonNull(response);
            Util.parameterNonNull(lifecycle);
        } catch (Exception e) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }
        FacesContext ctx = new FacesContextImpl(new ExternalContextImpl((ServletContext) sc,
                                                                        (ServletRequest) request,
                                                                        (ServletResponse) response),
                                                lifecycle);
        // store the default FacesContext and ExternalContext implementations
        // in the request so that the API can delegate if the happen to be
        // decorated by 1.1 implementations
        RequestStateManager.set(ctx,
                                RequestStateManager.FACESCONTEXT_IMPL_ATTR_NAME,
                                ctx);
        RequestStateManager.set(ctx,
                                RequestStateManager.EXTERNALCONTEXT_IMPL_ATTR_NAME,
                                ctx.getExternalContext());
        return ctx;

    }


// The testcase for this class is TestSerlvetFacesContextFactory.java 


} // end of class FacesContextFactoryImpl
