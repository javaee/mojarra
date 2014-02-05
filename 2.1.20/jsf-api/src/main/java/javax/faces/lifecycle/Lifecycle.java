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

package javax.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;


/**
 * <p><strong>Lifecycle</strong> manages the
 * processing of the entire lifecycle of a particular JavaServer Faces
 * request.  It is responsible for executing all of the phases that have
 * been defined by the JavaServer Faces Specification, in the specified
 * order, unless otherwise directed by activities that occurred during
 * the execution of each phase.</p>
 *
 * <p>An instance of <code>Lifecycle</code> is created by calling the
 * <code>getLifecycle()</code> method of {@link LifecycleFactory}, for
 * a specified lifecycle identifier.  Because this instance is
 * shared across multiple simultaneous requests, it must be implemented
 * in a thread-safe manner.</p>
 */

public abstract class Lifecycle {


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Register a new {@link PhaseListener} instance that is interested in
     * being notified before and after the processing for standard phases of
     * the request processing lifecycle.</p>
     *
     * @param listener The {@link PhaseListener} to be registered
     *
     * @throws NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public abstract void addPhaseListener(PhaseListener listener);


    /**
     * <p>Execute all of the phases of the request processing lifecycle,
     * up to but not including the <em>Render Response</em> phase,
     * as described in the JavaServer Faces Specification, in the specified
     * order.  The processing flow can be affected (by the application,
     * by components, or by event listeners) by calls to the
     * <code>renderResponse()</code> or <code>responseComplete()</code>
     * methods of the {@link FacesContext} instance associated with
     * the current request.</p>
     *
     * @param context FacesContext for the request to be processed
     *
     * @throws FacesException if thrown during the execution of the
     *  request processing lifecycle
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void execute(FacesContext context) throws FacesException;


    /**
     * <p>Return the set of registered {@link PhaseListener}s for this
     * {@link Lifecycle} instance.  If there are no registered listeners,
     * a zero-length array is returned.</p>
     */
    public abstract PhaseListener[] getPhaseListeners();


    /**
     * <p>Deregister an existing {@link PhaseListener} instance that is no
     * longer interested in being notified before and after the processing
     * for standard phases of the request processing lifecycle.  If no such
     * listener instance has been registered, no action is taken.</p>
     *
     * @param listener The {@link PhaseListener} to be deregistered
     * @throws NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public abstract void removePhaseListener(PhaseListener listener);


    /**
     * <p>Execute the <em>Render Response</em> phase of the request
     * processing lifecycle, unless the <code>responseComplete()</code>
     * method has been called on the {@link FacesContext} instance
     * associated with the current request.</p>
     *
     * @param context FacesContext for the request being processed
     *
     * @throws FacesException if an exception is thrown during the execution
     *  of the request processing lifecycle
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void render(FacesContext context) throws FacesException;


}
