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

/**
 * <p><strong class="changed_modified_2_0">ExternalContextFactory</strong> 
 * is a factory object that creates
 * (if needed) and returns new {@link ExternalContext} instances, initialized
 * for the processing of the specified request and response objects.</p>
 *
 * <p>There must be one <code>ExternalContextFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   ExternalContextFactory factory = (ExternalContextFactory)
 *    FactoryFinder.getFactory(FactoryFinder.EXTERNAL_CONTEXT_FACTORY);
 * </pre>
 *
 */

public abstract class ExternalContextFactory implements FacesWrapper<ExternalContextFactory> {

    public ExternalContextFactory() {
    }

    /**
     * <p class="changed_added_2_0">If this factory has been decorated, the 
     * implementation doing the decorating may override this method to provide
     * access to the implementation being wrapped.  A default implementation
     * is provided that returns <code>null</code>.</p>
     * 
     * @since 2.0
     */

    public ExternalContextFactory getWrapped() {
        return null;
    }

    /**
     * <p><span class="changed_added_2_0">Create</span> (if needed)
     * and return an {@link ExternalContext} instance that is initialized
     * for the processing of the specified request and response objects,
     * for this web application.</p>
     *
     * @param context In servlet environments, the
     * <code>ServletContext</code> that is associated with this web
     * application
     * @param request In servlet environments, the
     * <code>ServletRequest</code> that is to be processed
     * @param response In servlet environments, the
     * <code>ServletResponse</code> that is to be processed
     *
     * @throws FacesException if a {@link ExternalContext} cannot be
     *  constructed for the specified parameters
     * @throws NullPointerException if any of the parameters
     *  are <code>null</code>
     */
    public abstract ExternalContext getExternalContext
        (Object context, Object request,
         Object response) throws FacesException;


}
