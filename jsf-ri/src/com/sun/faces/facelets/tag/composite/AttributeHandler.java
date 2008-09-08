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

package com.sun.faces.facelets.tag.composite;

import com.sun.faces.facelets.FaceletContext;
import com.sun.faces.facelets.FaceletException;
import com.sun.faces.facelets.tag.TagAttribute;
import com.sun.faces.facelets.tag.TagConfig;
import com.sun.faces.facelets.tag.TagException;
import com.sun.faces.facelets.tag.TagHandler;
import com.sun.faces.facelets.tag.jsf.ComponentSupport;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Map;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;


public class AttributeHandler extends TagHandler {
    
    private TagAttribute name = null;
    private TagAttribute required = null;


    public AttributeHandler(TagConfig config) {
        super(config);
        this.name = this.getRequiredAttribute("name");
        this.required = this.getAttribute("required");
    }
    
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
        // only process if it's been created
        if (null == parent || 
            (null == (parent = parent.getParent())) ||
            !(ComponentSupport.isNew(parent))) {
            return;
        }
        
        Map<String, Object> componentAttrs = parent.getAttributes();
        boolean booleanValue = false;

        CompositeComponentBeanInfo componentBeanInfo = (CompositeComponentBeanInfo)
                componentAttrs.get(UIComponent.BEANINFO_KEY);

        PropertyDescriptor propertyDescriptor = null;
        TagAttribute attr = null;

        // Get the value of required the name propertyDescriptor
        ValueExpression ve = name.getValueExpression(ctx, String.class);
        String strValue = (String) ve.getValue(ctx);
        try {
            propertyDescriptor = new PropertyDescriptor(strValue, null, null);
        } catch (IntrospectionException ex) {
            throw new  TagException(tag, "Unable to create property descriptor for property " + strValue, ex);
        }
        
        if (null != required) {
            ve = required.getValueExpression(ctx, Boolean.class);
            propertyDescriptor.setValue("required", ve);
        }
        
        if (null != (attr = this.getAttribute("displayName"))) {
            ve = attr.getValueExpression(ctx, String.class);
            strValue = (String) ve.getValue(ctx);
            if (null != strValue) {
                propertyDescriptor.setDisplayName(strValue);
            }
        }
        if (null != (attr = this.getAttribute("expert"))) {
            ve = attr.getValueExpression(ctx, Boolean.class);
            booleanValue = ((Boolean) ve.getValue(ctx)).booleanValue();
            propertyDescriptor.setExpert(booleanValue);
        }
        if (null != (attr = this.getAttribute("hidden"))) {
            ve = attr.getValueExpression(ctx, Boolean.class);
            booleanValue = ((Boolean) ve.getValue(ctx)).booleanValue();
            propertyDescriptor.setHidden(booleanValue);
        }
        if (null != (attr = this.getAttribute("preferred"))) {
            ve = attr.getValueExpression(ctx, Boolean.class);
            booleanValue = ((Boolean) ve.getValue(ctx)).booleanValue();
            propertyDescriptor.setPreferred(booleanValue);
        }
        if (null != (attr = this.getAttribute("shortDescription"))) {
            ve = attr.getValueExpression(ctx, String.class);
            strValue = (String) ve.getValue(ctx);
            if (null != strValue) {
                propertyDescriptor.setShortDescription(strValue);
            }
        }
        if (null != (attr = this.getAttribute("default"))) {
            ve = attr.getValueExpression(ctx, String.class);
            propertyDescriptor.setValue("default", ve);
        }
        
    }

}
