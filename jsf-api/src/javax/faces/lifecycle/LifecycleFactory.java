/*
 * $Id: LifecycleFactory.java,v 1.3 2002/05/14 00:44:05 craigmcc Exp $
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
 *    FactoryFactory.createFactory("javax.faces.lifecycle.LifecycleFactory");
 * </pre>
 *
 * <p>A <code>LifecycleFactory</code> instance MUST return the same
 * {@link Lifecycle} instance for all calls to the
 * <code>createLifecycle()</code> method with the same lifecycle identifier
 * value, from within the same web applcation.</p>
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


}
