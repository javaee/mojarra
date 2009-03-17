/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package javax.faces.webapp.pdl;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">Encapsulate the saving and restoring of
 * the view to enable the PDL to take over the responsibility for
 * handling this feature.</p>
 *
 * @since 2.0
 */
public abstract class StateManagementStrategy {

    
    // PENDING(edburns): move into pdf document.
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
	  be taken to handle the case of components that were added
	  programmatically during this lifecycle traversal, rather than
	  by the PDL.  The {@link
	  javax.faces.event.AfterNonRestoreViewAddToViewEvent} is sent so that
	  implementations may be aware of such additions and take
	  appropriate action.</p></li>

	  <li><p>Care must be taken to handle the case of components
	  that were programmatically deleted during this lifecycle
	  traversal.  The {@link
	  javax.faces.event.PreRemoveFromViewEvent} is sent so that
	  implementations may be aware of such additions and take
	  appropriate action.</p></li>

	</ol>

     * <p>The implementation must ensure that the {@link
     * javax.faces.component.UIComponent#saveState} method is called for
     * each node in the tree.</p>

     * <p>The data structure used to save the state obtained by
     * executing the above algorithm must be <code>Serializable</code>,
     * and all of the elements within the data structure must also be
     * <code>Serializable</code>.</p>

     * </div>

     * context the <code>FacesContext</code> for this request.
     *
     * @since 2.0
     */

    public abstract Object saveView(FacesContext context);
    
    // PENDING(edburns): move into pdf document.
    /**
     * <p class="changed_added_2_0">Restore the state of the view with
     * information in the request.  The default implementation must
     * perform the following algorithm or its semantic equivalent.</p>
     *
     * <div class="changed_added_2_0">

     * 	<ol>

	  <li><p>Call {@link
	  javax.faces.application.ViewHandler#createView}.  This will
	  cause the view to be built from the PDL.  This view will not
	  contain any components programmatically added during the
	  previous lifecycle run, and it <b>will</b> contain components
	  that were programmatically deleted on the previous lifecycle
	  run.  Both of these cases must be handled.</p></li>

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

    /**
     * <p class="changed_added_2_0">Return <code>true</code> if this PDL
     * implementation takes responsibility for delivering the initial
     * state event.  Value must not change during application lifetime.
     * Safe to cache.</p>
     */
    public abstract boolean isPdlDeliversInitialStateEvent(FacesContext context);
    
}
