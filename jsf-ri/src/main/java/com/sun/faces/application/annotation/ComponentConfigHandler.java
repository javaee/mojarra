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
 */

package com.sun.faces.application.annotation;

import com.sun.faces.application.ApplicationAssociate;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.faces.component.FacesComponent;
import javax.faces.application.Application;

/**
 * <p>
 * <code>ConfigAnnotationHandler</code> for {@link FacesComponent} annotated
 * classes.
 * </p>
 */
public class ComponentConfigHandler implements ConfigAnnotationHandler {

    private static final Collection<Class<? extends Annotation>> HANDLES;
    static {
        Collection<Class<? extends Annotation>> handles =
              new ArrayList<Class<? extends Annotation>>(1);
        handles.add(FacesComponent.class);
        HANDLES = Collections.unmodifiableCollection(handles);
    }

    // key: componentId 
    private Map<String,FacesComponentUsage> components;


    // ------------------------------------- Methods from ComponentConfigHandler


    /**
     * @see com.sun.faces.application.annotation.ConfigAnnotationHandler#getHandledAnnotations() 
     */
    public Collection<Class<? extends Annotation>> getHandledAnnotations() {

        return HANDLES;

    }


    /**
     * @see com.sun.faces.application.annotation.ConfigAnnotationHandler#collect(Class, java.lang.annotation.Annotation)
     */
    public void collect(Class<?> target, Annotation annotation) {

        if (components == null) {
            components = new HashMap<String,FacesComponentUsage>();
        }
        String value = ((FacesComponent) annotation).value();
        if (null == value || 0 == value.length()) {
            value = target.getSimpleName();
            value = Character.toLowerCase(value.charAt(0)) + value.substring(1);
        }
        components.put(value,new FacesComponentUsage(target, (FacesComponent) annotation));

    }


    /**
     * @see com.sun.faces.application.annotation.ConfigAnnotationHandler#push(javax.faces.context.FacesContext)
     */
    public void push(FacesContext ctx) {

        if (components != null) {
            Application app = ctx.getApplication();
            ApplicationAssociate appAss = ApplicationAssociate.getCurrentInstance();
            for (Map.Entry<String, FacesComponentUsage> entry : components.entrySet()) {
                if (entry.getValue().getAnnotation().createTag()) {
                    appAss.addFacesComponent(entry.getValue());
                }
                app.addComponent(entry.getKey(), entry.getValue().getTarget().getName());
            }
        }

    }

}
