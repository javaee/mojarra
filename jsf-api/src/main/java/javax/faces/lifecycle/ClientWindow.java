/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_2">This class represents a client window,
 * which may be a browser tab, browser window, browser pop-up, portlet,
 * or anything else that can display a {@link
 * javax.faces.component.UIComponent} hierarchy rooted at a {@link
 * javax.faces.component.UIViewRoot}.</p>
 * 
 * <div class="changed_added_2_2">

 * <p>Lifetime</p>

 * <p>The lifetime of a <code>ClientWindow</code> starts on the first
 * request made by a particular client window (or tab, or pop-up, etc)
 * to the JSF runtime and persists as long as that window remains open.
 * A client window is always associated with exactly one
 * <code>UIViewRoot</code> instance at a time, but may display many
 * different <code>UIViewRoot</code>s during its lifetime.</p>

 * <p>The generation of <code>ClientWindow</code> is controlled by the
 * value of the <code>context-param</code> named by the value of {@link
 * #WINDOW_ID_MODE_PARAM_NAME}.  If this <code>context-param</code> is
 * not specified, or its value is "none", no <code>ClientWindow</code>
 * instances will be generated, and the entire feature is effectively
 * disabled.  For all other valid values of {@link
 * #WINDOW_ID_MODE_PARAM_NAME}, the <code>ClientWindow</code> instance
 * is associated with the incoming request during the {@link
 * Lifecycle#attachWindow} method.  If the feature is enabled, this
 * method will cause a new instance of <code>ClientWindow</code> to be
 * created, assigned an id, and passed to {@link
 * javax.faces.context.ExternalContext#setClientWindow}.</p>

 * <p>The <code>ClientWindow</code> is stored the response as specified
 * in </p>

 * </div>
 * 
 * @since 2.2
 * 
 */

public abstract class ClientWindow {
    
    /**
     * <p class="changed_added_2_2">The context-param that controls the operation
     * of the <code>ClientWindow</code> feature.  Valid values are "none" and
     * "field", without the quotes.  If not specified, "none" is assumed.</p>
     *
     * @since 2.2
     */
    public static final String WINDOW_ID_MODE_PARAM_NAME =
          "javax.faces.WINDOW_ID_MODE";
    
    /**
     * 
     * @return 
     */
    
    public abstract String getId();
    
    public abstract void decode(FacesContext context);
    
}
