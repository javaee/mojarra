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

import javax.faces.context.FacesContext;

/**
 *
 * <p><span class="changed_modified_2_0_rev_a">This</span> interface is
 * implemented by classes that need to save their state between
 * requests.</p>
 *
 * <p>An implementor <strong>must</strong> implement both {@link
 * #saveState} and {@link #restoreState} methods in this class, since
 * these two methods have a tightly coupled contract between themselves.
 * In other words, if there is an ineritance hierarchy, it is not
 * permissable to have the {@link #saveState} and {@link #restoreState}
 * methods reside at different levels of the hierarchy.</p>
 *
 * <p>An implementor must have a public no-args constructor.</p>
 *
 */

public interface StateHolder {

    /**
     * <p> Gets the state of the instance as a
     * <code>Serializable</code> Object.<p>
     *
     * <p>If the class that implements this interface has references to
     * instances that implement StateHolder (such as a
     * <code>UIComponent</code> with event handlers, validators, etc.)
     * this method must call the {@link #saveState} method on all those
     * instances as well.  <strong>This method must not save the state
     * of children and facets.</strong> That is done via the {@link
     * javax.faces.application.StateManager}</p>
     *
     * <p>This method must not alter the state of the implementing
     * object.  In other words, after executing this code:</p>
     *
     * <code><pre>
     * Object state = component.saveState(facesContext);
     * </pre></code>
     *
     * <p><code>component</code> should be the same as before executing
     * it.</p>
     *
     * <p>The return from this method must be <code>Serializable</code></p>
     * 
     * @throws NullPointerException if <code>context</code> is null
     */

    public Object saveState(FacesContext context);

    /**
     *
     * <p><span class="changed_modified_2_0_rev_a">Perform</state> any
     * processing required to restore the state from the entries in the
     * state Object.</p>
     *
     * <p>If the class that implements this interface has references to
     * instances that also implement StateHolder (such as a
     * <code>UIComponent</code> with event handlers, validators, etc.)
     * this method must call the {@link #restoreState} method on all those
     * instances as well. </p>

     * <p class="changed_modified_2_0_rev_a">If the <code>state</code>
     * argument is <code>null</code>, take no action and return.</p>
     * 
     * @throws NullPointerException if <code>context</code> is null.
     */

    public void restoreState(FacesContext context, Object state);

    /**
     *
     * <p>If true, the Object implementing this interface must not
     * participate in state saving or restoring.</p>
     */

    public boolean isTransient();

    /**
     * <p><span class="changed_modified_2_0_rev_a">Denotes</span>
     * whether or not the Object implementing this interface must or
     * must not participate in state saving or restoring.</p>
     * 
     * @param newTransientValue boolean pass <code>true</code> if this
     * Object <span class="changed_modified_2_0_rev_a">will not
     * participate</span> in state saving or restoring, otherwise pass
     * <code>false</code>.
     */ 
    public void setTransient(boolean newTransientValue);

}
