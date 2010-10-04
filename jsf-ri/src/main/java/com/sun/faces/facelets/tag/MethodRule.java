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

package com.sun.faces.facelets.tag;

import com.sun.faces.facelets.el.LegacyMethodBinding;

import javax.el.MethodExpression;
import javax.faces.el.MethodBinding;
import javax.faces.view.facelets.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Optional Rule for binding Method[Binding|Expression] properties
 * 
 * @author Mike Kienenberger
 * @author Jacob Hookom
 */
public final class MethodRule extends MetaRule {

    private final String methodName;

    private final Class returnTypeClass;

    private final Class[] params;

    public MethodRule(String methodName, Class returnTypeClass, Class[] params) {
        this.methodName = methodName;
        this.returnTypeClass = returnTypeClass;
        this.params = params;
    }

    public Metadata applyRule(String name, TagAttribute attribute,
            MetadataTarget meta) {
        if (!name.equals(this.methodName))
            return null;

        if (MethodBinding.class.equals(meta.getPropertyType(name))) {
            Method method = meta.getWriteMethod(name);
            if (method != null) {
                return new MethodBindingMetadata(method, attribute,
                        this.returnTypeClass, this.params);
            }
        } else if (MethodExpression.class.equals(meta.getPropertyType(name))) {
            Method method = meta.getWriteMethod(name);
            if (method != null) {
                return new MethodExpressionMetadata(method, attribute,
                        this.returnTypeClass, this.params);
            }
        }

        return null;
    }

    private static class MethodBindingMetadata extends Metadata {
        private final Method _method;

        private final TagAttribute _attribute;

        private Class[] _paramList;

        private Class _returnType;

        public MethodBindingMetadata(Method method, TagAttribute attribute,
                Class returnType, Class[] paramList) {
            _method = method;
            _attribute = attribute;
            _paramList = paramList;
            _returnType = returnType;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            MethodExpression expr = _attribute.getMethodExpression(ctx,
                    _returnType, _paramList);

            try {
                _method.invoke(instance, new LegacyMethodBinding(expr) );
            } catch (InvocationTargetException e) {
                throw new TagAttributeException(_attribute, e.getCause());
            } catch (Exception e) {
                throw new TagAttributeException(_attribute, e);
            }
        }
    }

    private static class MethodExpressionMetadata extends Metadata {
        private final Method _method;

        private final TagAttribute _attribute;

        private Class[] _paramList;

        private Class _returnType;

        public MethodExpressionMetadata(Method method, TagAttribute attribute,
                Class returnType, Class[] paramList) {
            _method = method;
            _attribute = attribute;
            _paramList = paramList;
            _returnType = returnType;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            MethodExpression expr = _attribute.getMethodExpression(ctx,
                    _returnType, _paramList);

            try {
                _method.invoke(instance, expr );
            } catch (InvocationTargetException e) {
                throw new TagAttributeException(_attribute, e.getCause());
            } catch (Exception e) {
                throw new TagAttributeException(_attribute, e);
            }
        }
    }
}
