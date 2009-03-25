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

import java.beans.BeanInfo;
import java.io.IOException;
import javax.faces.application.Resource;
import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">The contract that a page declaration
 * language must implement to interact with the JSF runtime.  An
 * implementation of this class must be thread-safe.</p>
 *
 * <div class="changed_added_2_0">
 * 
 * <p>Instances of this class are application scoped and must be
 * obtained from the {@link PageDeclarationLanguageFactory}.</p>

 * </div>
 * 
 * @since 2.0
 * 
 */
public abstract class PageDeclarationLanguage {

    /**
     * <p class="changed_added_2_0">Return a reference to the component
     * metadata for the composite component represented by the argument
     * <code>componentResource</code>, or <code>null</code> if the
     * metadata cannot be found.  See section JSF.7.6.2 for the
     * specification of the default implementation. JSP implementations
     * must throw <code>UnsupportedOperationException</code>.</p>
     *
     * @param context The <code>FacesContext</code> for this request.
     * @param componentResource The <code>Resource</code> that represents the component.
     * @since 2.0
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>.
     *
     * @throws javax.faces.FacesException if there is an error in
     * obtaining the metadata
     *
     * @throws UnsupportedOperationException if this is a JSP PDL
     * implementation.
     */
    public abstract BeanInfo getComponentMetadata(FacesContext context, Resource componentResource);


    /**
     * <p class="changed_added_2_0">Return a reference to the view
     * metadata for the view represented by the argument
     * <code>viewId</code>, or <code>null</code> if the metadata cannot
     * be found.  See section JSF.7.6.2 for the specification of the
     * default implementation.  Facelets for JSF 2 implementation must
     * return non-<code>null</code>. JSP implementations must return
     * <code>null</code>.</p>
     *
     * @param context The <code>FacesContext</code> for this request.
     * @param viewId the view id from whith to extract the metadata
     * @since 2.0
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>.
     *
     * @throws javax.faces.FacesException if there is an error in
     * obtaining the metadata
     */
    public abstract ViewMetadata getViewMetadata(FacesContext context, String viewId);


    /**
     * <p class="changed_added_2_0">Take implementation specific action
     * to discover a <code>Resource</code> given the argument
     * <code>componentResource</code>.  See section JSF.7.6.2 for the
     * specification of the default implementation.  JSP implementations
     * must throw <code>UnsupportedOperationException</code>.</p>
     *
     * @param context The <code>FacesContext</code> for this request.
     * @param componentResource The <code>Resource</code> that represents the component.
     * @since 2.0
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>.
     *
     * @throws javax.faces.FacesException if there is an error in
     * obtaining the script component resource
     * @throws UnsupportedOperationException if this is a JSP PDL
     * implementation.
     */
    public abstract Resource getScriptComponentResource(FacesContext context,
                                                        Resource componentResource);
    
    
    /**
     * <p class="changed_added_2_0">Create a <code>UIViewRoot</code>
     * from the PDL contained in the artifact referenced by the argument
     * <code>viewId</code>.  See section JSF.7.6.2 for the specification of
     * the default implementation.</p>
     *
     * @param context the <code>FacesContext</code> for this request.
     * @param viewId the identifier of an artifact that contains the PDL
     * syntax that describes this view.
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>

     * @since 2.0
     */

    public abstract UIViewRoot createView(FacesContext context,
                                 String viewId);
    
    /**
     * <p class="changed_added_2_0">Restore a <code>UIViewRoot</code>
     * from a previously created view.  See section JSF.7.6.2 for the
     * specification of the default implementation.</p>
     *
     * @param context the <code>FacesContext</code> for this request.
     * @param viewId the identifier for a previously rendered view.
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>
     */
    public abstract UIViewRoot restoreView(FacesContext context, String viewId);


    /**
     * <p class="changed_added_2_0">Take any actions specific to this
     * PDL implementation to cause the argument <code>UIViewRoot</code>
     * which must have been created via a call to {@link #createView},
     * to be populated with children.</p>

     * <div class="changed_added_2_0">

     * <p>The Facelets implementation must insure that markup comprising
     * the view must be executed, with the {@link
     * javax.faces.component.UIComponent} instances in the view being
     * encountered in the same depth-first order as in other lifecycle
     * methods defined on <code>UIComponent</code>, and added to the
     * view (but not rendered) during the traversal. The runtime must
     * guarantee that the view must be fully populated before any of the
     * following happen.</p>

     * <ul>
     *
     * <li><p>The {@link javax.faces.event.PhaseListener#afterPhase}
     * method of any <code>PhaseListener</code>s attached to the
     * application is called</p></li>

     * <li><p>The {@link javax.faces.component.UIViewRoot} phase
     * listener installed via {@link
     * javax.faces.component.UIViewRoot#setAfterPhaseListener} or {@link
     * javax.faces.component.UIViewRoot#addPhaseListener} are called.</p></li>
     *
     * </ul>

     * <p>The implementation must take no action if the argument
     * <code>root</code> already has non-metadata children.  See section
     * JSF.7.6.2.3 for the view metadata specification.</p>

     * </div>

     * @param context the <code>FacesContext</code> for this request

     * @param root the <code>UIViewRoot</code> to populate with children
     * using techniques specific to this PDL implementation.
     */
    public abstract void buildView(FacesContext context, UIViewRoot root)
    throws IOException;

    
    /**
     * <p class="changed_added_2_0">Render a view rooted at
     * argument<code>view</code>. See section JSF.7.6.2 for the
     * specification of the default implementation.</p>
     *
     * @param context the <code>FacesContext</code> for this request.
     * @param view the <code>UIViewRoot</code> from an early call to
     * {@link #createView} or {@link #restoreView}.
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>
     */
    public abstract void renderView(FacesContext context,
                                    UIViewRoot view)
    throws IOException;
    
    /**
     * <p class="changed_added_2_0">For implementations that want to
     * control the implementation of state saving and restoring, the
     * {@link StateManagementStrategy} allows them to do so.  Returning
     * <code>null</code> indicates that the implementation wishes the
     * runtime to handle the state saving and restoring.
     * Implementations that provide the PDL for Facelets for JSF 2.0 and
     * later must return non-<code>null</code> from this method.</p>
     *
     *
     * @since 2.0
     */ 

    public abstract StateManagementStrategy getStateManagementStrategy(FacesContext context,
            String viewId);
    

}
