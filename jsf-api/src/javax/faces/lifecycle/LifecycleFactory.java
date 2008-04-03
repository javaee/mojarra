/*
 * $Id: LifecycleFactory.java,v 1.18 2007/04/27 22:00:09 ofung Exp $
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

package javax.faces.lifecycle;

import java.util.Iterator;


/**
 * <p><strong>LifecycleFactory</strong> is a factory object that creates
 * (if needed) and returns {@link Lifecycle} instances.  Implementations of
 * JavaServer Faces must provide at least a default implementation of
 * {@link Lifecycle}.  Advanced implementations (or external third party
 * libraries) MAY provide additional {@link Lifecycle} implementations
 * (keyed by lifecycle identifiers) for performing different types of
 * request processing on a per-request basis.</p>
 *
 * <p>There must be one <code>LifecycleFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   LifecycleFactory factory = (LifecycleFactory)
 *    FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
 * </pre>
 */

public abstract class LifecycleFactory {


    /**
     * <p>The lifecycle identifier for the default {@link Lifecycle} instance
     * for this JavaServer Faces implementation.</p>
     */
    public static final String DEFAULT_LIFECYCLE = "DEFAULT";


    /**
     * <p>Register a new {@link Lifecycle} instance, associated with
     * the specified <code>lifecycleId</code>, to be supported by this
     * <code>LifecycleFactory</code>.  This method may be called at
     * any time, and makes the corresponding {@link Lifecycle} instance
     * available throughout the remaining lifetime of this web application.
     * </p>
     *
     * @param lifecycleId Identifier of the new {@link Lifecycle}
     * @param lifecycle {@link Lifecycle} instance that we are registering
     *
     * @throws IllegalArgumentException if a {@link Lifecycle} with the
     *  specified <code>lifecycleId</code> has already been registered
     * @throws NullPointerException if <code>lifecycleId</code>
     *  or <code>lifecycle</code> is <code>null</code>
     */
    public abstract void addLifecycle(String lifecycleId,
                                      Lifecycle lifecycle);


    /**
     * <p>Create (if needed) and return a {@link Lifecycle} instance
     * for the specified lifecycle identifier.  The set of available
     * lifecycle identifiers is available via the
     * <code>getLifecycleIds()</code> method.</p>
     *
     * <p>Each call to <code>getLifecycle()</code> for the same
     * <code>lifecycleId</code>, from within the same web application,
     * must return the same {@link Lifecycle} instance.</p>
     *
     * @param lifecycleId Lifecycle identifier of the requested
     *  {@link Lifecycle} instance
     *
     * @throws IllegalArgumentException if no {@link Lifecycle} instance
     *  can be returned for the specified identifier
     * @throws NullPointerException if <code>lifecycleId</code>
     *  is <code>null</code>
     */
    public abstract Lifecycle getLifecycle(String lifecycleId);


    /**
     * <p>Return an <code>Iterator</code> over the set of lifecycle
     * identifiers supported by this factory.  This set must include
     * the value specified by <code>LifecycleFactory.DEFAULT_LIFECYCLE</code>.
     * </p>
     */
    public abstract Iterator<String> getLifecycleIds();


}
