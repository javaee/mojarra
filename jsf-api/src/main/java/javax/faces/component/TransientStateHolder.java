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

/**
 * <p class="changed_added_2_1">This interface is implemented by classes 
 * that need to save state that is expected to be available only within the scope
 * of the current request.</p>
 * 
 * <div class="changed_added_2_1">
 * 
 * <p>An implementor <strong>must</strong> implement both {@link
 * #saveTransientState} and {@link #restoreTransientState} methods in this class, since
 * these two methods have a tightly coupled contract between themselves.
 * In other words, if there is an inheritance hierarchy, it is not
 * permissible to have the {@link #saveTransientState} and {@link #restoreTransientState}
 * methods reside at different levels of the hierarchy.</p>
 *
 * <p>An example of transient state is the "submitted" property on forms.</p>
 * 
 * </div>
 * 
 * @since 2.1
 */
public interface TransientStateHolder
{
    
    /**
     * <p class="changed_added_2_1">Return the object containing related "transient states".
     * that could be used later to restore the "transient state".<p>
     * 
     * @param context
     * @return object containing transient values
     * @since 2.1
     */
    public java.lang.Object saveTransientState(javax.faces.context.FacesContext context);

    /**
     * <p class="changed_added_2_1">Restore the "transient state" using the object passed as
     * state.</p>
     * 
     * <p class="changed_added_2_1">If the <code>state</code>
     * argument is <code>null</code> clear any previous transient
     * state if any and return.</p>
     * 
     * @param context
     * @param state the object containing transient values
     * @since 2.1
     */
    public void restoreTransientState(javax.faces.context.FacesContext context,
                             java.lang.Object state);
}
