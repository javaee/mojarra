/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.application.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.faces.context.FacesContext;
import javax.ejb.EJB;

/**
 * {@link RuntimeAnnotationHandler} responsible for processing EJB annotations.
 */
class EJBHandler extends JndiHandler implements RuntimeAnnotationHandler {

    private static final String JAVA_MODULE = "java:module/";
    private Field[] fields;
    private EJB[] fieldAnnotations;
    private Method[] methods;
    private EJB[] methodAnnotations;

    public EJBHandler(
            Field[] fields, EJB[] fieldAnnotations,
            Method[] methods, EJB[] methodAnnotations) {
        this.fields = fields;
        this.fieldAnnotations = fieldAnnotations;
        this.methods = methods;
        this.methodAnnotations = methodAnnotations;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    @Override
    public void apply(FacesContext ctx, Object... params) {
        Object object = params[0];
        for (int i = 0; i < fields.length; i++) {
            applyToField(ctx, fields[0], fieldAnnotations[0], object);
        }

        for (int i = 0; i < methods.length; i++) {
            applyToMethod(ctx, methods[i], methodAnnotations[i], object);
        }
    }

    private void applyToField(FacesContext facesContext, Field field, EJB ejb, Object instance) {
        Object value;
        if (ejb.lookup() != null && !"".equals(ejb.lookup().trim())) {
            value = lookup(facesContext, ejb.lookup());
        } else if (ejb.name() != null && !"".equals(ejb.name().trim())) {
            value = lookup(facesContext, JAVA_COMP_ENV + ejb.name());
        } else {
            value = lookup(facesContext, JAVA_MODULE + field.getType().getSimpleName());
        }
        setField(facesContext, field, instance, value);
    }

    private void applyToMethod(FacesContext facesContext, Method method, EJB ejb, Object instance) {
        if (method.getName().startsWith("set")) {
            Object value = null;
            if (ejb.lookup() != null && !"".equals(ejb.lookup().trim())) {
                value = lookup(facesContext, ejb.lookup());
            } else if (ejb.name() != null && !"".equals(ejb.name().trim())) {
                value = lookup(facesContext, JAVA_COMP_ENV + ejb.name());
            }
            invokeMethod(facesContext, method, instance, value);
        }
    }
}
