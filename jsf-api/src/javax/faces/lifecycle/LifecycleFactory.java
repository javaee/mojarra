/*
 * $Id: LifecycleFactory.java,v 1.6 2002/05/25 22:35:38 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import java.util.Iterator;
import javax.faces.FacesException;     // FIXME - subpackage?


/**
 * <p><strong>LifecycleFactory</strong> is a factory object that creates
 * and returns {@link Lifecycle} instances.  Implementations of
 * JavaServer Faces MUST provide at least a default implementation of
 * {@link Lifecycle}.  Advanced implementations MAY provide additional
 * instances (keyed by lifecycle identifiers) for performing different types of
 * request processing on a per-request basis.</p>
 *
 * <p>There shall be one <code>LifecycleFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   LifecycleFactory factory = (LifecycleFactory)
 *    FactoryFinder.createFactory(FactoryFinder.LIFECYCLE_FACTORY);
 * </pre>
 *
 * <p>A <code>LifecycleFactory</code> instance MUST return the same
 * {@link Lifecycle} instance for all calls to the
 * <code>createLifecycle()</code> method with the same lifecycle identifier
 * value, from within the same web application.</p>
 *
 * <p>It is possible for an application to customize the set of {@link Phase}s
 * that will be configured in a {@link Lifecycle} instance by calling the
 * <code>registerBefore()</code> or <code>registerAfter()</code> method,
 * passing the lifecycle identifier to be customized and the {@link Phase}
 * instance to be inserted.  However, these calls MUST be completed before
 * <code>createLifecycle()</code> is called the first time for that lifecycle
 * identifier.  Subsequent attempts to customize the configured {@link Phase}s
 * will throw <code>IllegalStateException</code>.</p>
 */

public abstract class LifecycleFactory {


    /**
     * <p>The lifecycle identifier for the default {@link Lifecycle} instance
     * for this JavaServer Faces implementation.</p>
     */
    public static final String DEFAULT_LIFECYCLE = "DEFAULT";


    /**
     * <p>Create (if needed) and return a {@link Lifecycle} instance
     * for the specified lifecycle identifier.  The set of available
     * lifecycle identifiers is available via the
     * <code>getLifecycleIds()</code> method.</p>
     *
     * @param lifecycleId Lifecycle identifier of the requested
     *  {@link Lifecycle} instance
     *
     * @exception FacesException if a {@link Lifecycle} instance cannot be
     *  constructed
     * @exception NullPointerException if <code>lifecycleId</code>
     *  is <code>null</code>
     */
    public abstract Lifecycle createLifecycle(String lifecycleId)
        throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of lifecycle
     * identifiers supported by this factory.  This set MUST include
     * the value specified by <code>LifecycleFactory.DEFAULT_LIFECYCLE</code>.
     * </p>
     */
    public abstract Iterator getLifecycleIds();


    /**
     * <p>Register a custom {@link Phase} instance that will be invoked
     * <strong>after</strong> the Faces-implementation-provided standard
     * instance, and after any phases previously registered via
     * <code>registerAfter()</code>, for the specified phase identifier.
     *
     * @param lifecycleId Lifecycle identifier of the {@link Lifecycle}
     *  implementation instance to be configured
     * @param phaseId Phase identifier of the Faces lifecycle phase to
     *  which this new {@link Phase} instance is being appended
     * @param phase Phase instance to be appended
     *
     * @exception IllegalArgumentException if <code>phaseId</code> does
     *  not contain one of the standard phase identifiers defined in
     *  {@link Lifecycle}
     * @exception IllegalStateException if an attempt is made to register
     *  a new {@link Phase} after <code>createLifecycle()</code> has been
     *  successfully called for this lifecycle identifier
     * @exception NullPointerException if <code>lifecycleId</code>
     *  or <code>phase</code> are null
     */
    public abstract void registerAfter(String lifecycleId,
                                       int phaseId, Phase phase);


    /**
     * <p>Register a custom {@link Phase} instance that will be invoked
     * <strong>before</strong> the Faces-implementation-provided standard
     * instance, and before any phases previously registered via
     * <code>registerBefore()</code>, for the specified phase identifier.
     *
     * @param lifecycleId Lifecycle identifier of the {@link Lifecycle}
     *  implementation instance to be configured
     * @param phaseId Phase identifier of the Faces lifecycle phase to
     *  which this new {@link Phase} instance is being prefixed
     * @param phase Phase instance to be prefixed
     *
     * @exception IllegalArgumentException if <code>phaseId</code> does
     *  not contain one of the standard phase identifiers defined in
     *  {@link Lifecycle}
     * @exception IllegalStateException if an attempt is made to register
     *  a new {@link Phase} after <code>createLifecycle()</code> has been
     *  successfully called for this lifecycle identifier
     * @exception NullPointerException if <code>lifecycleId</code>
     *  or <code>phase</code> are null
     */
    public abstract void registerBefore(String lifecycleId,
                                        int phaseId, Phase phase);


}
