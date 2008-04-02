/*
 * $Id: FacesContextFactory.java,v 1.17 2005/08/22 22:07:59 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.context;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;


/**
 * <p><strong>FacesContextFactory</strong> is a factory object that creates
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
 */

public abstract class FacesContextFactory {


    /**
     * <p>Create (if needed) and return a {@link FacesContext} instance
     * that is initialized for the processing of the specified request
     * and response objects, utilizing the specified {@link Lifecycle}
     * instance, for this web application.</p>
     *
     * <p>The implementation of this method must ensure that calls to the
     * <code>getCurrentInstance()</code> method of {@link FacesContext},
     * from the same thread that called this method, will return the same
     * {@link FacesContext} instance until the <code>release()</code>
     * method is called on that instance.</p>
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
     * @exception FacesException if a {@link FacesContext} cannot be
     *  constructed for the specified parameters
     * @exception NullPointerException if any of the parameters
     *  are <code>null</code>
     */
    public abstract FacesContext getFacesContext
        (Object context, Object request,
         Object response, Lifecycle lifecycle)
        throws FacesException;


}
