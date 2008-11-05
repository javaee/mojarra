/*
 * $Id: DiscoveryHandlerFactory.java,v 1.19 2007/04/27 22:00:06 ofung Exp $
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

package javax.faces.application;

import javax.faces.context.*;
import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.lifecycle.Lifecycle;


/**
 * <p><strong class="changed_added_2_0">DiscoveryHandlerFactory</strong>
 * is a factory object that creates (if needed) and returns a {@link
 * DiscoveryHandler} instance.  Due to the fundamental nature of the
 * features provided by {@link DiscoveryHandlerFactory}, it is not
 * possible to declare it in the application configuration resources.
 * Therefore, the <code>META-INF/services/DiscoveryHandlerFactory</code>
 * mechanism specified in {@link javax.faces.FactoryFinder} must be
 * implemented by the runtime for this class.</p>

 * <div class="changed_added_2_0">
 *
 * <p>There must be one <code>DiscoveryHandlerFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 *
 * <pre><code>
 *   DiscoveryHandlerFactory factory = (DiscoveryHandlerFactory)
 *    FactoryFinder.getFactory(FactoryFinder.DISCOVERY_HANDLER_FACTORY);
 * </code></pre>
 *
 * </div>
 */

public abstract class DiscoveryHandlerFactory implements FacesWrapper<DiscoveryHandlerFactory> {

    /**
     * <p class="changed_added_2_0">If this factory has been decorated, the 
     * implementation doing the decorating may override this method to provide
     * access to the implementation being wrapped.  A default implementation
     * is provided that returns <code>null</code>.</p>
     * 
     * @since 2.0
     */

    public DiscoveryHandlerFactory getWrapped() {
        return null;
    }

    /**
     * <p>Create (if needed) and return a {@link DiscoveryHandler}
     * instance.</p>

     * @throws FacesException if a {@link DiscoveryHandler} cannot be
     *  constructed for the specified parameters
     * @throws NullPointerException if any of the parameters
     *  are <code>null</code>
     */
    public abstract DiscoveryHandler getDiscoveryHandler()
        throws FacesException;


}
