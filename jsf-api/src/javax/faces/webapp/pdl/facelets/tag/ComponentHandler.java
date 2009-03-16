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

package javax.faces.webapp.pdl.facelets.tag;

import javax.faces.component.UIComponent;
import javax.faces.webapp.pdl.facelets.FaceletContext;


/**
 * <p class="changed_added_2_0">Public base class for markup element
 * instances that map to {@link UIComponent} instances in the view.</p>
 *
 * <div class="changed_added_2_0">
 *
 * <p>The implementation must guarantee that the <code>apply()</code>
 * method is overridden and takes the following actions, in this order.
 * These actions must only happen the first time this facelet is applied
 * for each user.  Subsequent applications must take no action.</p>

 * <ol>

 * 	  <li><p>The <code>UIComponent</code> represented by this
 * 	  element is created with the appropriate
 * 	  <code>Application.createComponent()</code> method.  </p></li>

	  <li><p>The rendererType property of the component is set properly.
	  </p></li>

	  <li><p>{@link #onComponentCreated} is called.
	  </p></li>

	  <li><p>{@link UIComponent#pushComponentToEL} is called on the
	  newly created component.</p></li>

	  <li><p>The next handler in the facelet chain is applied.  This
	  will cause the component to be populated with children.
	  </p></li>

	  <li><p>{@link UIComponent#processEvent} is called, passing an
	  instance of {@link javax.faces.event.InitialStateEvent}.
	  </p></li>

	  <li><p>The component is added to its parent in the view.
	  </p></li>

	  <li><p>{@link UIComponent#popComponentFromEL} is called on the
	  newly created component.</p></li>


 * </ol>
 *
 * </div>
 *
 * @since 2.0
 */
public class ComponentHandler extends DelegatingMetaTagHandler {
    
    private TagHandlerDelegate helper = null;
    private ComponentConfig componentConfig = null;

    /**
     * <p class="changed_added_2_0">Leverage the {@link
     * TagHandlerDelegateFactory} provided by the implementation to create
     * an instance of {@link TagHandlerDelegate} designed for use with
     * <code>ComponentHandler</code>.</p>
     *
     * @since 2.0
     */ 
    public ComponentHandler(ComponentConfig config) {
        super(config);
        this.componentConfig = config;
    }
    
    @Override
    protected TagHandlerDelegate getTagHandlerHelper() {
        if (null == helper) {
            helper = helperFactory.createComponentHandlerDelegate(this);
        }
        return helper;
    }
    
    public ComponentConfig getComponentConfig() {
        return this.componentConfig;
    }
    
    
    /**
     * <p class="changed_added_2_0">This method is guaranteed to be
     * called after the component has been created but before it has
     * been populated with children.</p>
     *
     * @param ctx the <code>FaceletContext</code> for this view execution

     * @param c the <code>UIComponent</code> that has just been created.
     *
     * @param parent the parent <code>UIComponent</code> of the
     * component represented by this element instance.
     *
     * @since 2.0
     */
    public void onComponentCreated(FaceletContext ctx, UIComponent c, 
            UIComponent parent) {
        
    }
    
    /**
     * <p class="changed_added_2_0">This method is guaranteed to be
     * called after the component has been populated with children.</p>
     *
     * @param ctx the <code>FaceletContext</code> for this view execution

     * @param c the <code>UIComponent</code> that has just been
     * populated with children.
     *
     * @param parent the parent <code>UIComponent</code> of the
     * component represented by this element instance.
     *
     * @since 2.0
     */
    public void onComponentPopulated(FaceletContext ctx, UIComponent c, 
            UIComponent parent) {
        
    }
    
    /**
     * <p class="changed_added_2_0">Determine if the passed component is 
     * not null and if it's new to the tree.  This operation can be used for determining if attributes
     * should be wired to the component.</p>
     * 
     * @param component the component you wish to modify
     * @since 2.0
     */
    public final static boolean isNew(UIComponent component) {
        return component != null && component.getParent() == null;
    }

    
    

}
