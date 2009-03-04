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

import java.io.IOException;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;


/**
 * <p class="changed_added_2_0">Public base class for markup element
 * instances that map to {@link UIComponent} instances in the view.</p>
 *
 * @since 2.0
 */
public class ComponentHandler extends MetaTagHandler {
    
    private TagHandlerHelper helper = null;
    private ComponentConfig componentConfig = null;

    /**
     * <p class="changed_added_2_0">Leverage the {@link
     * TagHandlerHelperFactory} provided by the implementation to create
     * an instance of {@link TagHandlerHelper} designed for use with
     * <code>ComponentHandler</code>.</p>
     *
     * @since 2.0
     */ 
    public ComponentHandler(ComponentConfig config) {
        super(config);
        this.componentConfig = config;

        TagHandlerHelperFactory helperFactory = (TagHandlerHelperFactory)
                FactoryFinder.getFactory(FactoryFinder.TAG_HANDLER_HELPER_FACTORY);
        helper = helperFactory.createComponentHandlerHelper(this);
    }
    
    public Tag getTag() {
        return this.tag;
    }
    
    public ComponentConfig getComponentConfig() {
        return this.componentConfig;
    }
    
    public TagAttribute getTagAttribute(String localName) { 
        return super.getAttribute(localName);
    }
    
    public String getTagId() {
        return this.tagId;
    }
    
    @Override
    public void setAttributes(FaceletContext ctx, Object instance) {
        super.setAttributes(ctx, instance);
    }

    /**
     * <p class="changed_added_2_0">The default implementation simply
     * calls through to {@link TagHandlerHelper#createMetaRuleset} and
     * returns the result.</p>
     *
     * @param type the <code>Class</code> for which the
     * <code>MetaRuleset</code> must be created.
     *
     * @since 2.0
     */

    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        return helper.createMetaRuleset(type);
    }

    /**
     * <p class="changed_added_2_0">The default implementation simply
     * calls through to {@link TagHandlerHelper#apply}.</p>
     *
     * @param ctx the <code>FaceletContext</code> for this view execution
     *
     * @param parent the parent <code>UIComponent</code> of the
     * component represented by this element instance.
     * @since 2.0
     */

    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, FaceletException, ELException {
        helper.apply(ctx, parent);
    }
    
    public void applyNextHandler(FaceletContext ctx, UIComponent c) 
            throws IOException, FacesException, ELException {
        // first allow c to get populated
        this.nextHandler.apply(ctx, c);
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
    

}
