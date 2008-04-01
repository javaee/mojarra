/*
 * $Id: LifecycleFactory.java,v 1.2 2002/05/08 01:11:46 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import java.util.Iterator;
import javax.faces.FacesException;     // FIXME - subpackage?


/**
 * <p><strong>LifecycleFactory</strong> is a Factory object that creates
 * and returns new {@link Lifecycle} instances.  Implementations of
 * JavaServer Faces MUST provide at least a default implementation of
 * {@link Lifecycle}.  Advanced implementations MAY provide additional
 * instances (keyed by logical names) for performing different types of
 * request processing on a per-request basis.</p>
 *
 * <p>There shall be one <code>LifecycleFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  <strong>FIXME</strong>
 * - document the discovery process for this factory implementation.</p>
 *
 * <p>The factory instance MUST return the same {@link Lifecycle} instance
 * for all calls to the <code>createLifecycle()</code> method with no
 * parameters, or for all calls the the <code>createLifecycle()</code> method
 * with the same logical lifecycle identifier.</p>
 */

public abstract class LifecycleFactory {


    /**
     * <p>Create (if needed) and return a {@link Lifecycle} that is the
     * default processing lifecycle for all JavaServer Faces requests
     * for this web application.  All JavaServer Faces implementations
     * MUST be able to return a {@link Lifecycle} instance via this call.
     *</p>
     *
     * @exception FacesException if a {@link Lifecycle} cannot be
     *  constructed
     */
    public abstract Lifecycle createLifecycle() throws FacesException;


    /**
     * <p>Create (if needed) and return a {@link Lifecycle} instance
     * for the specified lifecycle identifier.  The set of available
     * lifecycle identifiers is available via the
     * <code>getLifecycleIds()</code> method.</p>
     *
     * @param lifecycleId Lifecycle identifier of the requested
     *  {@link Lifecycle} instance
     *
     * @exception FacesException if a {@link Lifecycle} cannot be
     *  constructed
     * @exception NullPointerException if <code>lifecycleId</code>
     *  is <code>null</code>
     */
    public abstract Lifecycle createLifecycle(String lifecycleId)
        throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of custom lifecycle
     * identifiers supported by this factory.  If no custom identifiers are
     * supported, an empty <code>Iterator</code> is returned.</p>
     */
    public abstract Iterator getLifecycleIds();


}
