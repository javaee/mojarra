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

package javax.faces.component;

import java.io.Serializable;

/**
 * <p class="changed_added_2_0">Components that want to leverage the
 * partial state saving feature must implement this interface instead of
 * implementing {@link StateHolder}, from which this interface
 * inherits.</p>
 * @since 2.0
 */
public interface PartialStateHolder extends StateHolder {


    /**
     * <p class="changed_added_2_0">The runtime must ensure that the
     * {@link #markInitialState} method is called on each instance of
     * this interface in the view at the appropriate time to indicate
     * the component is in its initial state.  The implementor of the
     * interface must ensure that {@link #initialStateMarked} returns
     * <code>true</code> from the time <code>markInitialState()</code>
     * is called until {@link #clearInitialState} is called, after which
     * time <code>initialStateMarked()</code> must return
     * <code>false</code>.  Also, during the time that the instance
     * returns <code>true</code> from <code>initialStateMarked()</code>,
     * the implementation must return only the state that has changed in
     * its implementation of {@link StateHolder#saveState}.</p>
     * @since 2.0
     */
    void markInitialState();

    /**
     * <p class="changed_added_2_0">Return <code>true</code> if delta
     * state changes are being tracked, otherwise <code>false</code></p>
     * @since 2.0
     */
    boolean initialStateMarked();


    /**
     * <p class="changed_added_2_0">Reset the PartialStateHolder to a
     * non-delta tracking state.</p>
     * @since 2.0
     */
    void clearInitialState();

}
