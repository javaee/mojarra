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
import javax.persistence.PersistenceUnit;

/**
 * {@link RuntimeAnnotationHandler} responsible for processing
 * {@link PersistenceUnit} annotations.
 */
class PersistenceUnitHandler extends JndiHandler implements RuntimeAnnotationHandler {

    private Method[] methods;
    private PersistenceUnit[] methodAnnotations;
    private Field[] fields;
    private PersistenceUnit[] fieldAnnotations;
    private PersistenceUnit[] classAnnotations;

    public PersistenceUnitHandler(
            PersistenceUnit[] classAnnotations,
            Method[] methods, PersistenceUnit[] methodAnnotations,
            Field[] fields, PersistenceUnit[] fieldAnnotations) {
        this.classAnnotations = classAnnotations;
        this.methods = methods;
        this.methodAnnotations = methodAnnotations;
        this.fields = fields;
        this.fieldAnnotations = fieldAnnotations;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    @Override
    public void apply(FacesContext ctx, Object... params) {
        Object object = params[0];
        for (int i = 0; i < fields.length; i++) {
            applyToField(ctx, fields[i], fieldAnnotations[i], object);
        }
        
        for (int i=0; i<methods.length; i++) {
            applyToMethod(ctx, methods[i], methodAnnotations[i], object);
        }

        for(int i=0; i<classAnnotations.length; i++) {
            applyToClass(ctx, classAnnotations[i], object);
        }
    }

    private void applyToMethod(FacesContext facesContext, Method method, PersistenceUnit unit, Object instance) {
        if (method.getName().startsWith("set")) {
            Object value = null;
            if (unit.name() != null && !"".equals(unit.name().trim())) {
                value = lookup(facesContext, JAVA_COMP_ENV + unit.name());
            }
            invokeMethod(facesContext, method, instance, value);
        }
    }

    private void applyToField(FacesContext facesContext, Field field, PersistenceUnit unit, Object instance) {
        Object value;
        if (unit.name() != null && !"".equals(unit.name().trim())) {
            value = lookup(facesContext, JAVA_COMP_ENV + unit.name());
        } else {
            value = lookup(facesContext, field.getType().getSimpleName());
        }
        setField(facesContext, field, instance, value);
    }
    
    private void applyToClass(FacesContext facesContext, PersistenceUnit unit, Object instance) {
    }
}
