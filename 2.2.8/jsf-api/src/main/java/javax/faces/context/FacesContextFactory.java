/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package javax.faces.context;

import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.lifecycle.Lifecycle;


/**
 * <p><strong class="changed_modified_2_0 changed_modified_2_2">FacesContextFactory</strong> 
 * is a factory object that creates
 * (if needed) and returns new {@link FacesContext} instances, initialized
 * for the processing of the specified request and response objects.
 * Implementations may take advantage of the calls to the
 * <code>release()</code> method of the allocated {@link FacesContext}
 * instances to pool and recycle them, rather than creating a new instance
 * every time.</p>
 *
 * <p>There must be one <code>FacesContextFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   FacesContextFactory factory = (FacesContextFactory)
 *    FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
 * </pre>
 *
 */

public abstract class FacesContextFactory implements FacesWrapper<FacesContextFactory> {

    public FacesContextFactory() {
    }

    /**
     * <p class="changed_added_2_0">If this factory has been decorated, the 
     * implementation doing the decorating may override this method to provide
     * access to the implementation being wrapped.  A default implementation
     * is provided that returns <code>null</code>.</p>
     * 
     * @since 2.0
     */

    public FacesContextFactory getWrapped() {
        return null;
    }

    /**
     * <p><span class="changed_modified_2_0 changed_modified_2_2">Create</span> (if needed)
     * and return a {@link FacesContext} instance that is initialized
     * for the processing of the specified request and response objects,
     * utilizing the specified {@link Lifecycle} instance, for this web
     * application.</p>
     *
     * <p>The implementation of this method must ensure that calls to the
     * <code>getCurrentInstance()</code> method of {@link FacesContext},
     * from the same thread that called this method, will return the same
     * {@link FacesContext} instance until the <code>release()</code>
     * method is called on that instance.</p>

     * <p class="changed_added_2_0">The implementation must call
     * {@link ExternalContextFactory#getExternalContext} to produce the
     * {@link ExternalContext} for the {@link FacesContext} instance.</p>

     * <p class="changed_added_2_0">The default implementation must call
     * {@link ExceptionHandlerFactory#getExceptionHandler} and make it
     * so the return from that method is what gets returned from a call
     * to {@link FacesContext#getExceptionHandler} on the returned
     * <code>FacesContext</code> instance.</p>
     *
     * <p class="changed_added_2_2">The default implementation must call
     * {@link javax.faces.lifecycle.ClientWindowFactory#getClientWindow} and make it
     * so the return from that method is what gets returned from a call
     * to {@link ExternalContext#getClientWindow()} on the returned
     * <code>ExternalContext</code> instance.</p>
     *
     * @param context In servlet environments, the
     * <code>ServletContext</code> that is associated with this web
     * application
     * @param request In servlet environments, the
     * <code>ServletRequest</code> that is to be processed
     * @param response In servlet environments, the
     * <code>ServletResponse</code> that is to be processed
     * @param lifecycle The {@link Lifecycle} instance being used
     *  to process this request
     *
     * @throws FacesException if a {@link FacesContext} cannot be
     *  constructed for the specified parameters
     * @throws NullPointerException if any of the parameters
     *  are <code>null</code>
     */
    public abstract FacesContext getFacesContext
        (Object context, Object request,
         Object response, Lifecycle lifecycle)
        throws FacesException;


}
