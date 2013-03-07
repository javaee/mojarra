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
import javax.xml.ws.WebServiceRef;

/**
 * {@link RuntimeAnnotationHandler} responsible for processing {@link WebServiceRef}
 * annotations.
 */
class WebServiceRefHandler extends JndiHandler implements RuntimeAnnotationHandler {

    private Field[] fields;
    private WebServiceRef[] fieldAnnotations;
    private Method[] methods;
    private WebServiceRef[] methodAnnotations;

    public WebServiceRefHandler(
            Field[] fields, WebServiceRef[] fieldAnnotations,
            Method[] methods, WebServiceRef[] methodAnnotations) {
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

        for (int i=0; i<methods.length; i++) {
            applyToMethod(ctx, methods[i], methodAnnotations[i], object);
        }
    }

    private void applyToField(FacesContext facesContext, Field field, WebServiceRef ref, Object instance) {
        Object value = null;
        /*
        if (ref.lookup() != null && !"".equals(ref.lookup().trim())) {
            value = lookup(facesContext, ref.lookup());
        } else 
        */
        if (ref.name() != null && !"".equals(ref.name().trim())) {
            value = lookup(facesContext, JAVA_COMP_ENV + ref.name());
        } else {
            value = lookup(facesContext, field.getName());
        }
        setField(facesContext, field, instance, value);
    }

    private void applyToMethod(FacesContext facesContext, Method method, WebServiceRef ref, Object instance) {
        if (method.getName().startsWith("set")) {
            Object value = null;
            /*
            if (ref.lookup() != null && !"".equals(ref.lookup().trim())) {
                value = lookup(facesContext, ref.lookup());
            } else 
            */
            if (ref.name() != null && !"".equals(ref.name().trim())) {
                value = lookup(facesContext, JAVA_COMP_ENV + ref.name());
            }
            invokeMethod(facesContext, method, instance, value);
        }
    }
}
