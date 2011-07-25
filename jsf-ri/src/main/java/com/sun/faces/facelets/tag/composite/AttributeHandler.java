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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
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

import com.sun.faces.facelets.tag.TagHandlerImpl;
import com.sun.faces.facelets.util.ReflectionUtil;
import com.sun.faces.util.FacesLogger;
import java.lang.reflect.Method;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.faces.context.FacesContext;


public class AttributeHandler extends TagHandlerImpl {
    
    private final Logger LOGGER = FacesLogger.TAGLIB.getLogger();    

    private static final String[] COMPOSITE_ATTRIBUTE_ATTRIBUTES = {
          "required",
          "targets",
          "targetAttributeName",
          "default",
          "displayName",
          "preferred",
          "hidden",
          "expert",
          "shortDescription",
          "method-signature",
          "type",
          
    };

    private static final PropertyHandlerManager ATTRIBUTE_MANAGER =
          PropertyHandlerManager.getInstance(COMPOSITE_ATTRIBUTE_ATTRIBUTES);


    private TagAttribute name;


    public AttributeHandler(TagConfig config) {
        super(config);
        this.name = this.getRequiredAttribute("name");
    }
    
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        // only process if it's been created
        if (null == parent || 
            (null == (parent = parent.getParent())) ||
            !(ComponentHandler.isNew(parent))) {
            return;
        }
        

        Map<String, Object> attrs = parent.getAttributes();
        
        CompositeComponentBeanInfo componentBeanInfo =
              (CompositeComponentBeanInfo) attrs.get(UIComponent.BEANINFO_KEY);
        assert(null != componentBeanInfo);
        List<PropertyDescriptor> declaredAttributes = 
                componentBeanInfo.getPropertyDescriptorsList();

        // Get the value of required the name propertyDescriptor
        ValueExpression ve = name.getValueExpression(ctx, String.class);
        String strValue = (String) ve.getValue(ctx);

        // Search the propertyDescriptors for one for this attribute
        for (PropertyDescriptor cur: declaredAttributes) {
            if (strValue.endsWith(cur.getName())) {
                // If we have a match, no need to waste time
                // duplicating and replacing it.
                return;
            }
        }

        PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor = new CCAttributePropertyDescriptor(strValue, null, null);
            declaredAttributes.add(propertyDescriptor);
        } catch (IntrospectionException ex) {
            throw new  TagException(tag, "Unable to create property descriptor for property " + strValue, ex);
        }

        TagAttribute defaultTagAttribute = null;
        PropertyHandler defaultHandler = null;
        for (TagAttribute tagAttribute : this.tag.getAttributes().getAll()) {
            String attributeName = tagAttribute.getLocalName();
            if("default".equals(attributeName)) {
                // store the TagAttribute and the PropertyHandler for later
                // execution, as the handler for the default-attribute requires,
                // that the PropertyHandler for 'type' - if it exists - has been
                // applied first.
                defaultTagAttribute = tagAttribute;
                defaultHandler = ATTRIBUTE_MANAGER.getHandler(ctx, "default");
            } else {
                PropertyHandler handler =
                        ATTRIBUTE_MANAGER.getHandler(ctx, attributeName);
                if (handler != null) {
                    handler.apply(ctx, attributeName, propertyDescriptor,
                            tagAttribute);
                }
            }
        }
        if(defaultHandler!=null) {
            // If the 'default'-attribute of cc:attribute was set, apply the
            // previously stored PropertyHandler (see above) now, as now it is
            // guaranteed that if a 'type'-attribute existed, that its handler
            // was already applied
            try {
                defaultHandler.apply(ctx, "default", propertyDescriptor,
                        defaultTagAttribute);
            } catch (IllegalArgumentException ex) {
                // If the type (according to the type-attribute) can not be
                // found, the DefaultPropertyHandler will wrapp the
                // ClassNotFoundException into an IllegalArgumentException,
                // which is unwrapped into a TagException here.
                throw new TagException(tag, 
                        "'type' could not be resolved: " + ex.getCause(),
                        ex.getCause());
            }
        }
        
        this.nextHandler.apply(ctx, parent);
        
    }
    
    private class CCAttributePropertyDescriptor extends PropertyDescriptor {

        public CCAttributePropertyDescriptor(String propertyName, Method readMethod, Method writeMethod) throws IntrospectionException {
            super(propertyName, readMethod, writeMethod);
        }

        @Override
        public Object getValue(String attributeName) {
            Object result = super.getValue(attributeName);
            if ("type".equals(attributeName)) {
                if ((null != result) && !(result instanceof Class)) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    ELContext elContext = context.getELContext();
                    String classStr = (String) ((ValueExpression)result).getValue(elContext);
                    if (null != classStr) {
                        try {
                            result = ReflectionUtil.forName(classStr);

                            this.setValue(attributeName, result);
                        } catch (ClassNotFoundException ex) {
                            classStr = "java.lang." + classStr;
                            boolean throwException = false;
                            try {
                                result = ReflectionUtil.forName(classStr);
                                
                                this.setValue(attributeName, result);
                            } catch (ClassNotFoundException ex2) {
                                throwException = true;
                            }
                            if (throwException) {
                                String message = "Unable to obtain class for " + classStr;
                                if (LOGGER.isLoggable(Level.INFO)) {
                                    LOGGER.log(Level.INFO, message, ex);
                                }
                                throw new TagAttributeException(tag, name, message, ex);
                            }
                        }
                    }
                    
                }
            }
            return result;
        }
        
        
    }

}
