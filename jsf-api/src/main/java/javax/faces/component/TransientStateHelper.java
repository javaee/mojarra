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
 * <p class="changed_added_2_1">Define a <code>Map</code>-like contract
 * that makes it easier for components to implement {@link
 * TransientStateHolder}.  Each {@link UIComponent} in the view will
 * return an implementation of this interface from its {@link
 * UIComponent#getTransientStateHelper} method.</p>
 * 
 * <div class="changed_added_2_1">
 * 
 * <p>The values retrieved or saved through {@link
 * #getTransient} or {@link
 * #putTransient} will not be
 * preserved between requests.</p>
 * 
 * </div>
 * 
 * @since 2.1
 * 
 */
public interface TransientStateHelper extends TransientStateHolder
{
    /**
     * <p class="changed_added_2_1">Return the value currently
     * associated with the specified <code>key</code> if any.</p>
     * @param key the key for which the value should be returned.
     * @since 2.1
     */
    public Object getTransient(Object key);
    
    /**
     * <p class="changed_added_2_1">Performs the same logic as {@link
     * #getTransient} } but if no value is found, this
     * will return the specified <code>defaultValue</code></p>

     * @param key the key for which the value should be returned.
     * @param defaultValue the value to return if no value is found in
     * the call to <code>get()</code>.
     * @since 2.1
     */
    public Object getTransient(Object key, Object defaultValue);

    /**
     * <p class="changed_added_2_1">Return the previously stored value
     * and store the specified key/value pair.  This is intended to
     * store data that would otherwise reside in an instance variable on
     * the component.</p>
     * 
     * @param key the key for the value
     * @param value the value
     * @since 2.1
     */
    public Object putTransient(Object key, Object value);
}
