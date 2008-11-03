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

package javax.faces.context;

import javax.faces.FacesWrapper;

/**
 * <p class="changed_added_2_0"><strong>ExceptionHandlerFactory</strong>
 * is a factory object that creates (if needed) and returns a new {@link
 * ExceptionHandler} instance.</p>
 *
 * <div class="changed_added_2_0">

 * <p>There must be one <code>ExceptionHandlerFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 *
 * <pre><code>
 *   ExceptionHandlerFactory factory = (ExceptionHandlerFactory)
 *    FactoryFinder.getFactory(FactoryFinder.EXCEPTION_HANDLER_FACTORY);
 * </code></pre>
 *

 * </div>
 */

public abstract class ExceptionHandlerFactory implements FacesWrapper<ExceptionHandlerFactory> {

    /**
     * <p class="changed_added_2_0">If this factory has been decorated, the 
     * implementation doing the decorating may override this method to provide
     * access to the implementation being wrapped.  A default implementation
     * is provided that returns <code>null</code>.</p>
     * 
     * @since 2.0
     */

    public ExceptionHandlerFactory getWrapped() {
        return null;
    }

    /**
     * <p class="changed_added_2_0">Create (if needed) and return an
     * {@link ExceptionHandler} instance for this {@link FacesContext}.
     * A new <code>ExceptionHandler</code> instance must be created for
     * each <code>FacesContext</code> instance.</p>
     */
    public abstract ExceptionHandler getExceptionHandler(FacesContext context);
    

}
