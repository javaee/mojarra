/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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
package javax.faces.context;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.faces.FacesWrapper;

/**
 * <p class="changed_added_2_2">Provides a simple implementation of
 * {@link Flash} that can be subclassed by developers wishing
 * to provide specialized behavior to an existing {@link
 * Flash} instance.  The default implementation of all methods
 * is to call through to the wrapped {@link Flash}.</p>
 *
 * <div class="changed_added_2_2">
 *
 * <p>Usage: extend this class and override {@link #getWrapped} to
 * return the instance we are wrapping.</p>
 *
 * </div>
 *
 * @since 2.2
 */
public abstract class FlashWrapper extends Flash implements FacesWrapper<Flash> {
    @Override
    public abstract Flash getWrapped();

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#doPostPhaseActions(FacesContext)} on the
     * wrapped {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public void doPostPhaseActions(FacesContext ctx) {
        getWrapped().doPostPhaseActions(ctx);
        
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#doPrePhaseActions(FacesContext)} on the
     * wrapped {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public void doPrePhaseActions(FacesContext ctx) {
        getWrapped().doPrePhaseActions(ctx);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#isKeepMessages()} on the wrapped
     * {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public boolean isKeepMessages() {
        return getWrapped().isKeepMessages();
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#isRedirect()} on the wrapped {@link Flash}
     * object.</p>
     * 
     * @since 2.2
     */
    @Override
    public boolean isRedirect() {
        return getWrapped().isRedirect();
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#keep(String)} on the wrapped {@link Flash}
     * object.</p>
     * 
     * @since 2.2
     */
    @Override
    public void keep(String key) {
        getWrapped().keep(key);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#putNow(String, Object)} on the wrapped
     * {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public void putNow(String key, Object value) {
        getWrapped().putNow(key, value);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#setKeepMessages(boolean)} on the wrapped
     * {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public void setKeepMessages(boolean newValue) {
        getWrapped().setKeepMessages(newValue);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#setRedirect(boolean)} on the wrapped
     * {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public void setRedirect(boolean newValue) {
        getWrapped().setRedirect(newValue);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#clear()} on the wrapped {@link Flash}
     * object.</p>
     * 
     * @since 2.2
     */
    @Override
    public void clear() {
        getWrapped().clear();
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#containsKey(Object)} on the wrapped
     * {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public boolean containsKey(Object key) {
        return getWrapped().containsKey(key);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#containsValue(Object)} on the wrapped
     * {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public boolean containsValue(Object value) {
        return getWrapped().containsValue(value);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#entrySet()} on the wrapped {@link Flash}
     * object.</p>
     * 
     * @since 2.2
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return getWrapped().entrySet();
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#get(Object)} on the wrapped {@link Flash}
     * object.</p>
     * 
     * @since 2.2
     */
    @Override
    public Object get(Object key) {
        return getWrapped().get(key);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#isEmpty()} on the wrapped {@link Flash}
     * object.</p>
     * 
     * @since 2.2
     */
    @Override
    public boolean isEmpty() {
        return getWrapped().isEmpty();
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#keySet()} on the wrapped {@link Flash}
     * object.</p>
     * 
     * @since 2.2
     */
    @Override
    public Set<String> keySet() {
        return getWrapped().keySet();
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#put} on the wrapped
     * {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public Object put(String key, Object value) {
        return getWrapped().put(key, value);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#putAll(Map)} on the wrapped
     * {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        getWrapped().putAll(m);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#remove(Object)} on the wrapped
     * {@link Flash} object.</p>
     * 
     * @since 2.2
     */
    @Override
    public Object remove(Object key) {
        return getWrapped().remove(key);
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#size()} on the wrapped {@link Flash}
     * object.</p>
     * 
     * @since 2.2
     */
    @Override
    public int size() {
        return getWrapped().size();
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link Flash#values()} on the wrapped {@link Flash}
     * object.</p>
     * 
     * @since 2.2
     */
    @Override
    public Collection<Object> values() {
        return getWrapped().values();
    }
    
}
