/*
 * $Id: LifecycleFactory.java,v 1.14 2005/08/15 15:59:18 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import java.util.Iterator;
import javax.faces.FacesException;     // PENDING - subpackage?


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
     * @exception IllegalArgumentException if a {@link Lifecycle} with the
     *  specified <code>lifecycleId</code> has already been registered
     * @exception NullPointerException if <code>lifecycleId</code>
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
     * @exception IllegalArgumentException if no {@link Lifecycle} instance
     *  can be returned for the specified identifier
     * @exception NullPointerException if <code>lifecycleId</code>
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
