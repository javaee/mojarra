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

package javax.faces.render;


import java.util.Iterator;
import javax.faces.FacesWrapper;
import javax.faces.context.FacesContext;


/**
 * <p><strong class="changed_modified_2_0">RenderKitFactory</strong> is a 
 * factory object that registers
 * and returns {@link RenderKit} instances.  Implementations of
 * JavaServer Faces must provide at least a default implementation of
 * {@link RenderKit}.  Advanced implementations (or external third party
 * libraries) may provide additional {@link RenderKit} implementations
 * (keyed by render kit identifiers) for performing different types of
 * rendering for the same components.</p>
 *
 * <p>There must be one {@link RenderKitFactory} instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   RenderKitFactory factory = (RenderKitFactory)
 *    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
 * </pre>
 */

public abstract class RenderKitFactory implements FacesWrapper<RenderKitFactory> {
    
    /**
     * <p class="changed_added_2_0">If this factory has been decorated, the 
     * implementation doing the decorating may override this method to provide
     * access to the implementation being wrapped.  A default implementation
     * is provided that returns <code>null</code>.</p>
     * 
     * @since 2.0
     */

    public RenderKitFactory getWrapped() {
        return null;
    }
    
    /**
     * <p>The render kit identifier of the default {@link RenderKit} instance
     * for this JavaServer Faces implementation.</p>
     */
    public static final String HTML_BASIC_RENDER_KIT = "HTML_BASIC";


    /**
     * <p>Register the specified {@link RenderKit} instance, associated with
     * the specified <code>renderKitId</code>, to be supported by this
     * {@link RenderKitFactory}, replacing any previously registered
     * {@link RenderKit} for this identifier.</p>
     *
     * @param renderKitId Identifier of the {@link RenderKit} to register
     * @param renderKit {@link RenderKit} instance that we are registering
     *
     * @throws NullPointerException if <code>renderKitId</code> or
     *  <code>renderKit</code> is <code>null</code>
     */
    public abstract void addRenderKit(String renderKitId,
                                      RenderKit renderKit);


    /**
     * <p>Return a {@link RenderKit} instance for the specified render
     * kit identifier, possibly customized based on dynamic
     * characteristics of the specified {@link FacesContext}, if
     * non-<code>null</code>.  If there is no registered {@link
     * RenderKit} for the specified identifier, return
     * <code>null</code>.  The set of available render kit identifiers
     * is available via the <code>getRenderKitIds()</code> method.</p>
     *
     * @param context FacesContext for the request currently being
     * processed, or <code>null</code> if none is available.
     * @param renderKitId Render kit identifier of the requested
     *  {@link RenderKit} instance
     *
     * @throws IllegalArgumentException if no {@link RenderKit} instance
     *  can be returned for the specified identifier
     * @throws NullPointerException if <code>renderKitId</code> is
     * <code>null</code>
     */
    public abstract RenderKit getRenderKit(FacesContext context, 
					   String renderKitId);


    /**
     * <p>Return an <code>Iterator</code> over the set of render kit
     * identifiers registered with this factory.  This set must include
     * the value specified by <code>RenderKitFactory.HTML_BASIC_RENDER_KIT</code>.
     * </p>
     */
    public abstract Iterator<String> getRenderKitIds();


}
