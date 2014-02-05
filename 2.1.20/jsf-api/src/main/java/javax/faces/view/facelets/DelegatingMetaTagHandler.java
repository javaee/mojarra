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

package javax.faces.view.facelets;

import java.io.IOException;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;

/**
 * <p class="changed_added_2_0"><span
 * class="changed_modified_2_0_rev_a">Enable</span> the JSF
 * implementation to provide the appropriate behavior for the kind of
 * {@link MetaTagHandler} subclass for each kind of element in the view,
 * while providing a base-class from which those wanting to make a Java
 * language custom tag handler can inherit.  The JSF runtime provides
 * the implementation of {@link #getTagHandlerDelegate} for the
 * appropriate subclass.</p>
 */

public abstract class DelegatingMetaTagHandler extends MetaTagHandler {
    
    private final TagAttribute binding;

    private final TagAttribute disabled;
    
    protected TagHandlerDelegateFactory delegateFactory;
    
    public DelegatingMetaTagHandler(TagConfig config) {
        super(config);
        this.binding = this.getAttribute("binding");
        this.disabled = this.getAttribute("disabled");
        delegateFactory = (TagHandlerDelegateFactory)
                FactoryFinder.getFactory(FactoryFinder.TAG_HANDLER_DELEGATE_FACTORY);
    }
    
    protected abstract TagHandlerDelegate getTagHandlerDelegate();
    
    // Properties ----------------------------------------

    public boolean isDisabled(FaceletContext ctx) {
        return disabled != null && Boolean.TRUE.equals(disabled.getBoolean(ctx));
    }
    
    public TagAttribute getBinding() {
        return this.binding;
    }
    
    public Tag getTag() {
        return this.tag;
    }
    
    public String getTagId() {
        return this.tagId;
    }
    
    public TagAttribute getTagAttribute(String localName) {
        return super.getAttribute(localName);
    }
    
    @Override
    public void setAttributes(FaceletContext ctx, Object instance) {
        super.setAttributes(ctx, instance);
    }
    
    // Methods ----------------------------------------
    

    /**
     * <p class="changed_added_2_0">The default implementation simply
     * calls through to {@link TagHandlerDelegate#apply}.</p>
     *
     * @param ctx the <code>FaceletContext</code> for this view execution
     *
     * @param parent the parent <code>UIComponent</code> of the
     * component represented by this element instance.
     * @since 2.0
     */

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        getTagHandlerDelegate().apply(ctx, parent);
    }
    
    /**
     * <p class="changed_added_2_0_rev_a">Invoke the <code>apply()</code>
     * method on this instance's {@link TagHandler#nextHandler}.</p>
     *
     * @param ctx the <code>FaceletContext</code> for this view execution
     *
     * @param c the <code>UIComponent</code> of the
     * component represented by this element instance.
     * @since 2.0
     */

    public void applyNextHandler(FaceletContext ctx, UIComponent c) 
            throws IOException, FacesException, ELException {
        // first allow c to get populated
        this.nextHandler.apply(ctx, c);
    }
    
    /**
     * <p class="changed_added_2_0">The default implementation simply
     * calls through to {@link TagHandlerDelegate#createMetaRuleset} and
     * returns the result.</p>
     *
     * @param type the <code>Class</code> for which the
     * <code>MetaRuleset</code> must be created.
     *
     * @since 2.0
     */

    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        return getTagHandlerDelegate().createMetaRuleset(type);
    }
    
}
