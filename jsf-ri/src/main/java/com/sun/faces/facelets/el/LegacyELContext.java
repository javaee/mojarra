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

package com.sun.faces.facelets.el;

import javax.el.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 
 * 
 * @author Jacob Hookom
 * @version $Id$
 * @deprecated
 */
public final class LegacyELContext extends ELContext {

    private static final String[] IMPLICIT_OBJECTS = new String[] {
            "application", "applicationScope", "cookie", "facesContext",
            "header", "headerValues", "initParam", "param", "paramValues",
            "request", "requestScope", "session", "sessionScope", "view" };

    private final static FunctionMapper functions = new EmptyFunctionMapper();

    private final FacesContext faces;

    private final ELResolver resolver;

    private final VariableMapper variables;

    public LegacyELContext(FacesContext faces) {
        this.faces = faces;
        this.resolver = new LegacyELResolver();
        this.variables = new DefaultVariableMapper();
    }

    public ELResolver getELResolver() {
        return this.resolver;
    }

    public FunctionMapper getFunctionMapper() {
        return functions;
    }

    public VariableMapper getVariableMapper() {
        return this.variables;
    }
    
    public FacesContext getFacesContext() {
        return this.faces;
    }

    private final class LegacyELResolver extends ELResolver {

        public Class getCommonPropertyType(ELContext context, Object base) {
            return Object.class;
        }

        public Iterator getFeatureDescriptors(ELContext context, Object base) {
            return Collections.EMPTY_LIST.iterator();
        }

        private VariableResolver getVariableResolver() {
            return faces.getApplication().getVariableResolver();
        }

        private PropertyResolver getPropertyResolver() {
            return faces.getApplication().getPropertyResolver();
        }

        public Class getType(ELContext context, Object base, Object property) {
            if (property == null) {
                return null;
            }
            try {
                context.setPropertyResolved(true);
                if (base == null) {
                    Object obj = this.getVariableResolver().resolveVariable(
                            faces, property.toString());
                    return (obj != null) ? obj.getClass() : null;
                } else {
                    if (base instanceof List || base.getClass().isArray()) {
                        return this.getPropertyResolver().getType(base,
                                Integer.parseInt(property.toString()));
                    } else {
                        return this.getPropertyResolver().getType(base,
                                property);
                    }
                }
            } catch (PropertyNotFoundException e) {
                throw new javax.el.PropertyNotFoundException(e.getMessage(), e
                        .getCause());
            } catch (EvaluationException e) {
                throw new ELException(e.getMessage(), e.getCause());
            }
        }

        public Object getValue(ELContext context, Object base, Object property) {
            if (property == null) {
                return null;
            }
            try {
                context.setPropertyResolved(true);
                if (base == null) {
                    return this.getVariableResolver().resolveVariable(faces,
                            property.toString());
                } else {
                    if (base instanceof List || base.getClass().isArray()) {
                        return this.getPropertyResolver().getValue(base,
                                Integer.parseInt(property.toString()));
                    } else {
                        return this.getPropertyResolver().getValue(base,
                                property);
                    }
                }
            } catch (PropertyNotFoundException e) {
                throw new javax.el.PropertyNotFoundException(e.getMessage(), e
                        .getCause());
            } catch (EvaluationException e) {
                throw new ELException(e.getMessage(), e.getCause());
            }
        }

        public boolean isReadOnly(ELContext context, Object base,
                Object property) {
            if (property == null) {
                return true;
            }
            try {
                context.setPropertyResolved(true);
                if (base == null) {
                    return false; // what can I do?
                } else {
                    if (base instanceof List || base.getClass().isArray()) {
                        return this.getPropertyResolver().isReadOnly(base,
                                Integer.parseInt(property.toString()));
                    } else {
                        return this.getPropertyResolver().isReadOnly(base,
                                property);
                    }
                }
            } catch (PropertyNotFoundException e) {
                throw new javax.el.PropertyNotFoundException(e.getMessage(), e
                        .getCause());
            } catch (EvaluationException e) {
                throw new ELException(e.getMessage(), e.getCause());
            }
        }

        public void setValue(ELContext context, Object base, Object property,
                Object value) {
            if (property == null) {
                throw new PropertyNotWritableException("Null Property");
            }
            try {
                context.setPropertyResolved(true);
                if (base == null) {
                    if (Arrays.binarySearch(IMPLICIT_OBJECTS, property
                            .toString()) >= 0) {
                        throw new PropertyNotWritableException(
                                "Implicit Variable Not Setable: " + property);
                    } else {
                        Map scope = this.resolveScope(property.toString());
                        this.getPropertyResolver().setValue(scope, property,
                                value);
                    }
                } else {
                    if (base instanceof List || base.getClass().isArray()) {
                        this.getPropertyResolver().setValue(base,
                                Integer.parseInt(property.toString()), value);
                    } else {
                        this.getPropertyResolver().setValue(base, property,
                                value);
                    }
                }
            } catch (PropertyNotFoundException e) {
                throw new javax.el.PropertyNotFoundException(e.getMessage(), e
                        .getCause());
            } catch (EvaluationException e) {
                throw new ELException(e.getMessage(), e.getCause());
            }

        }

        private Map resolveScope(String var) {
            ExternalContext ext = faces.getExternalContext();

            // cycle through the scopes to find a match, if no
            // match is found, then return the requestScope
            Map map = ext.getRequestMap();
            if (!map.containsKey(var)) {
                map = ext.getSessionMap();
                if (!map.containsKey(var)) {
                    map = ext.getApplicationMap();
                    if (!map.containsKey(var)) {
                        map = ext.getRequestMap();
                    }
                }
            }
            return map;
        }
    }

    private final static class EmptyFunctionMapper extends FunctionMapper {

        public Method resolveFunction(String prefix, String localName) {
            return null;
        }

    }

}
