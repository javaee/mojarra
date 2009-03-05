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

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.facelets.tag.MetaRulesetImpl;
import com.sun.faces.util.Util;
import java.io.IOException;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.pdl.AttachedObjectHandler;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import javax.faces.webapp.pdl.facelets.tag.ConverterHandler;
import javax.faces.webapp.pdl.facelets.tag.MetaRuleset;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagException;
import javax.faces.webapp.pdl.facelets.tag.TagHandlerDelegate;


/**
 *
 * @author edburns
 */
public class ConverterTagHandlerDelegateImpl extends TagHandlerDelegate implements AttachedObjectHandler {

    private ConverterHandler owner;
    
    public ConverterTagHandlerDelegateImpl(ConverterHandler owner) {
        this.owner = owner;
    }
    
    @Override
    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, FaceletException, ELException {
        // only process if it's been created
        if (parent == null || !(parent.getParent() == null)) {
            return;
        }
        if (parent instanceof ValueHolder) {
            owner.applyAttachedObject(ctx.getFacesContext(), parent);
        } else if (parent.getAttributes().containsKey(Resource.COMPONENT_RESOURCE_KEY)) {
            if (null == owner.getFor()) {
                // PENDING(): I18N
                throw new TagException(owner.getTag(),
                        "converter tags nested within composite components must have a non-null \"for\" attribute");
            }
            // Allow the composite component to know about the target
            // component.
            CompositeComponentTagHandler.getAttachedObjectHandlers(parent).add(this);
        } else {
            throw new TagException(owner.getTag(),
                    "Parent not an instance of ValueHolder: " + parent);
        }
    }
    
    public MetaRuleset createMetaRuleset(Class type) {
        Util.notNull("type", type);
        MetaRuleset m = new MetaRulesetImpl(owner.getTag(), type);

        return m.ignore("binding");
    }
    
    public String getFor() {
        String result = null;
        TagAttribute attr = owner.getTagAttribute("for");
        
        if (null != attr) {
            result = attr.getValue();
        }
        return result;
        
    }
    
    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        // cast to a ValueHolder
        ValueHolder vh = (ValueHolder) parent;
        ValueExpression ve = null;
        Converter c = null;
        if (owner.getBinding() != null) {
            ve = owner.getBinding().getValueExpression(ctx, Converter.class);
            c = (Converter) ve.getValue(ctx);
        }
        if (c == null) {
            c = this.createConverter(ctx);
            if (ve != null) {
                ve.setValue(ctx, c);
            }
        }
        if (c == null) {
            throw new TagException(owner.getTag(), "No Converter was created");
        }
        owner.setAttributes(ctx, c);
        vh.setConverter(c);
        Object lv = vh.getLocalValue();
        FacesContext faces = ctx.getFacesContext();
        if (lv instanceof String) {
            vh.setValue(c.getAsObject(faces, parent, (String) lv));
        }
    }

    /**
     * Create a Converter instance
     * 
     * @param ctx
     *            FaceletContext to use
     * @return Converter instance, cannot be null
     */
    private Converter createConverter(FaceletContext ctx) {
        if (owner.getConverterId() == null) {
            throw new TagException(
                    owner.getTag(),
                    "Default behavior invoked of requiring a converter-id passed in the constructor, must override ConvertHandler(ConverterConfig)");
        }
        return ctx.getFacesContext().getApplication().createConverter(owner.getConverterId());
    }

    

}
