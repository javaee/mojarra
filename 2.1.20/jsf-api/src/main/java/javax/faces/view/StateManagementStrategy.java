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

package javax.faces.view;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">Encapsulate the saving and restoring of
 * the view to enable the VDL to take over the responsibility for
 * handling this feature. Because {@link
 * ViewDeclarationLanguage#getStateManagementStrategy} is required to
 * return <code>null</code> for JSP views and non-<code>null</code> for
 * views authored in Facelets for JSF 2, this specification only applys
 * to Facelets for JSF 2.</p>
 *
 * @since 2.0
 */
public abstract class StateManagementStrategy {

    
    /**
     * <p class="changed_added_2_0">Return the state of the current view
     * in an <code>Object</code> that implements
     * <code>Serializable</code>.  The default implementation must
     * perform the following algorithm or its semantic equivalent.</p>
     *
     * <div class="changed_added_2_0">

     * 	<ol>

	  <li><p>If the <code>UIViewRoot</code> of the current view is
	  marked <code>transient</code>, return <code>null</code>
	  immediately.  </p></li>

	<li><p>Traverse the view and verify that each of the client ids
	are unique.  Throw <code>IllegalStateException</code> if more
	than one client id are the same.</p></li>

	  <li><p>Visit the tree using {@link
	  javax.faces.component.UIComponent#visitTree}.  For each node,
	  call {@link javax.faces.component.UIComponent#saveState},
	  saving the returned <code>Object</code> in a way such that it
	  can be restored given only its client id.  Special care must
	  be taken to handle the case of components that were added or
	  deleted programmatically during this lifecycle traversal,
	  rather than by the VDL.  </p></li>

	</ol>

     * <p>The implementation must ensure that the {@link
     * javax.faces.component.UIComponent#saveState} method is called for
     * each node in the tree.</p>

     * <p>The data structure used to save the state obtained by
     * executing the above algorithm must be <code>Serializable</code>,
     * and all of the elements within the data structure must also be
     * <code>Serializable</code>.</p>

     * </div>

     * @param context the <code>FacesContext</code> for this request.
     *
     * @since 2.0
     */

    public abstract Object saveView(FacesContext context);
    
    /**
     * <p class="changed_added_2_0">Restore the state of the view with
     * information in the request.  The default implementation must
     * perform the following algorithm or its semantic equivalent.</p>
     *
     * <div class="changed_added_2_0">

     * 	<ol>

	  <li><p>Build the view from the markup.  For all components in
	  the view that do not have an explicitly assigned id in the
	  markup, the values of those ids must be the same as on an
	  initial request for this view.  This view will not contain
	  any components programmatically added during the previous
	  lifecycle run, and it <b>will</b> contain components that were
	  programmatically deleted on the previous lifecycle run.  Both
	  of these cases must be handled.</p></li>

	  <li><p>Call {@link
	  javax.faces.render.ResponseStateManager#getState} to obtain
	  the data structure returned from the previous call to {@link
	  #saveView}.</p></li>

	  <li><p>Visit the tree using {@link
	  javax.faces.component.UIComponent#visitTree}.  For each node,
	  call {@link javax.faces.component.UIComponent#restoreState},
	  passing the state saved corresponding to the current client
	  id.</p></li>

	  <li><p>Ensure that any programmatically deleted components are
	  removed.</p></li>

	  <li><p>Ensure any programmatically added components are added.
	  </p></li>

	</ol>

     * <p>The implementation must ensure that the {@link
     * javax.faces.component.UIComponent#restoreState} method is called
     * for each node in the tree, except for those that were
     * programmatically deleted on the previous run through the
     * lifecycle.</p>

     * </div>
     *
     * @param context the <code>FacesContext</code> for this request

     * @param viewId the view identifier for which the state should be restored

     * @param renderKitId the render kit id for this state.

     * @since 2.0
     */

    public abstract UIViewRoot restoreView(FacesContext context, String viewId,
                                           String renderKitId);

    
}
