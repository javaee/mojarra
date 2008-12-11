/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jsf;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import com.sun.faces.facelets.tag.MetaTagHandler;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import com.sun.faces.facelets.tag.TagConfig;
import com.sun.faces.facelets.tag.TagException;
import com.sun.faces.facelets.tag.MetaRuleset;
import javax.faces.application.Resource;
import javax.faces.webapp.pdl.ValueHolderAttachedObjectHandler;

/**
 * Handles setting a Converter instance on a ValueHolder. Will wire all
 * attributes set to the Converter instance created/fetched. Uses the "binding"
 * attribute for grabbing instances to apply attributes to. <p/> Will only
 * set/create Converter is the passed UIComponent's parent is null, signifying
 * that it wasn't restored from an existing tree.
 * 
 * @see javax.faces.webapp.ConverterELTag
 * @see javax.faces.convert.Converter
 * @see javax.faces.component.ValueHolder
 * @author Jacob Hookom
 * @version $Id$
 */
public class ConvertHandler extends MetaTagHandler implements ValueHolderAttachedObjectHandler {

    private final TagAttribute binding;
    
    private String converterId;

    /**
     * @param config
     * @deprecated
     */
    public ConvertHandler(TagConfig config) {
        super(config);
        this.binding = this.getAttribute("binding");
        this.converterId = null;
    }
    
    public ConvertHandler(ConverterConfig config) {
        this((TagConfig) config);
        this.converterId = config.getConverterId();
    }

    /**
     * Set Converter instance on parent ValueHolder if it's not being restored.
     * <ol>
     * <li>Cast to ValueHolder</li>
     * <li>If "binding" attribute was specified, fetch/create and re-bind to
     * expression.</li>
     * <li>Otherwise, call
     * {@link #createConverter(FaceletContext) createConverter}.</li>
     * <li>Call
     * {@link com.sun.faces.facelets.tag.MetaTagHandler#setAttributes(FaceletContext, Object) setAttributes}
     * on Converter instance.</li>
     * <li>Set the Converter on the ValueHolder</li>
     * <li>If the ValueHolder has a localValue, convert it and set the value</li>
     * </ol>
     * 
     * @see ValueHolder
     * @see Converter
     * @see #createConverter(FaceletContext)
     * @see com.sun.faces.facelets.FaceletHandler#apply(com.sun.faces.facelets.FaceletContext, javax.faces.component.UIComponent)
     */
    public final void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, FaceletException, ELException {
        // only process if it's been created
        if (parent == null || !(parent.getParent() == null)) {
            return;
        }
        if (parent instanceof ValueHolder) {
            applyAttachedObject(ctx.getFacesContext(), parent);
        } else if (parent.getAttributes().containsKey(Resource.COMPONENT_RESOURCE_KEY)) {
            if (null == getFor()) {
                // PENDING(): I18N
                throw new TagException(this.tag,
                        "converter tags nested within composite components must have a non-null \"for\" attribute");
            }
            // Allow the composite component to know about the target
            // component.
            CompositeComponentTagHandler.getAttachedObjectHandlers(parent).add(this);
        } else {
            throw new TagException(this.tag,
                    "Parent not an instance of ValueHolder: " + parent);
        }
    }

    /**
     * Create a Converter instance
     * 
     * @param ctx
     *            FaceletContext to use
     * @return Converter instance, cannot be null
     */
    protected Converter createConverter(FaceletContext ctx) {
        if (this.converterId == null) {
            throw new TagException(
                    this.tag,
                    "Default behavior invoked of requiring a converter-id passed in the constructor, must override ConvertHandler(ConverterConfig)");
        }
        return ctx.getFacesContext().getApplication().createConverter(this.converterId);
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        return super.createMetaRuleset(type).ignore("binding");
    }

    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        // cast to a ValueHolder
        ValueHolder vh = (ValueHolder) parent;
        ValueExpression ve = null;
        Converter c = null;
        if (this.binding != null) {
            ve = this.binding.getValueExpression(ctx, Converter.class);
            c = (Converter) ve.getValue(ctx);
        }
        if (c == null) {
            c = this.createConverter(ctx);
            if (ve != null) {
                ve.setValue(ctx, c);
            }
        }
        if (c == null) {
            throw new TagException(this.tag, "No Converter was created");
        }
        this.setAttributes(ctx, c);
        vh.setConverter(c);
        Object lv = vh.getLocalValue();
        FacesContext faces = ctx.getFacesContext();
        if (lv instanceof String) {
            vh.setValue(c.getAsObject(faces, parent, (String) lv));
        }
    }
    
    public String getFor() {
        String result = null;
        TagAttribute attr = this.getAttribute("for");

        if (null != attr) {
            result = attr.getValue();
        }
        return result;

    }
    
    
    
}
