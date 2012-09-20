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

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.facelets.el.LegacyValueBinding;
import com.sun.faces.util.FacesLogger;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
final class ComponentRule extends MetaRule {

    final static class LiteralAttributeMetadata extends Metadata {

        private final String name;
        private final String value;
        
        public LiteralAttributeMetadata(String name, String value) {
            this.value = value;
            this.name = name;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((UIComponent) instance).getAttributes().put(this.name, this.value);
        }
    }
    
    final static class ValueExpressionMetadata extends Metadata {

        private final String name;

        private final TagAttribute attr;

        private final Class type;

        public ValueExpressionMetadata(String name, Class type,
                TagAttribute attr) {
            this.name = name;
            this.attr = attr;
            this.type = type;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((UIComponent) instance).setValueExpression(this.name, this.attr
                    .getValueExpression(ctx, this.type));
        }

    }

    final static class ValueBindingMetadata extends Metadata {

        private final String name;

        private final TagAttribute attr;

        private final Class type;

        public ValueBindingMetadata(String name, Class type, TagAttribute attr) {
            this.name = name;
            this.attr = attr;
            this.type = type;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((UIComponent) instance).setValueBinding(this.name,
                    new LegacyValueBinding(this.attr.getValueExpression(ctx,
                            this.type)));
        }

    }

    private final static Logger log = FacesLogger.FACELETS_COMPONENT.getLogger();

    public final static ComponentRule Instance = new ComponentRule();

    public ComponentRule() {
        super();
    }

    public Metadata applyRule(String name, TagAttribute attribute,
            MetadataTarget meta) {
        if (meta.isTargetInstanceOf(UIComponent.class)) {

            // if component and dynamic, then must set expression
            if (!attribute.isLiteral()) {
                Class type = meta.getPropertyType(name);
                if (type == null) {
                    type = Object.class;
                }
                return new ValueExpressionMetadata(name, type, attribute);
            } else if (meta.getWriteMethod(name) == null) {

                // this was an attribute literal, but not property
                warnAttr(attribute, meta.getTargetClass(), name);

                return new LiteralAttributeMetadata(name, attribute.getValue());
            }
        }
        return null;
    }

    private static void warnAttr(TagAttribute attr, Class type, String n) {
        if (log.isLoggable(Level.FINER)) {
            log.finer(attr + " Property '" + n + "' is not on type: "
                    + type.getName());
        }
    }

}
