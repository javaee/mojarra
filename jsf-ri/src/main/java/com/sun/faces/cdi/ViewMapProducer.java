/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewMap;

/**
 * <p class="changed_added_2_3">
 * The ViewMapProducer is the CDI producer that allows injection of the view map
 * using @Inject and allows EL resolving of #{viewScope}
 * </p>
 *
 * @since 2.3
 * @see UIViewRoot#getViewMap()
 */
public class ViewMapProducer extends CdiProducer implements Bean<Map<String, Object>> {

    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * The set of types that this producer is capable of producing, and hence
     * can be used as the type of an injection point.
     */
    private final Set<Type> types = new HashSet<>(asList(
            new ParameterizedTypeImpl(Map.class, new Type[]{String.class, Object.class}),
            Map.class,
            Object.class));

    /**
     * Inner class defining an annotation literal for @ViewMap.
     */
    public class ViewMapAnnotationLiteral
            extends AnnotationLiteral<ViewMap> {

        @Override
        public Class<? extends Annotation> annotationType() {
            return ViewMap.class;
        }

        private static final long serialVersionUID = 1L;
    }

    /**
     * Create the actual instance.
     *
     * @param creationalContext the creational context.
     * @return the Faces context.
     */
    @Override
    public Map<String, Object> create(CreationalContext<Map<String, Object>> creationalContext) {
        checkActive();
        return FacesContext.getCurrentInstance().getViewRoot().getViewMap();
    }

    /**
     * Destroy the instance.
     *
     * <p>
     * Since the FacesContext is a JSF artifact that the JSF runtime really is
     * managing the destroy method here does not need to do anything.
     * </p>
     *
     * @param instance the instance.
     * @param creationalContext the creational context.
     */
    @Override
    public void destroy(Map<String, Object> instance, CreationalContext<Map<String, Object>> creationalContext) {
    }

    /**
     * Get the bean class.
     *
     * @return the bean class.
     */
    @Override
    public Class<?> getBeanClass() {
        return Map.class;
    }

    /**
     * Get the injection points.
     *
     * @return the injection points.
     */
    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return emptySet();
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    @Override
    public String getName() {
        return "viewScope";
    }

    /**
     * Get the qualifiers.
     *
     * @return the qualifiers.
     */
    @Override
    public Set<Annotation> getQualifiers() {
        return singleton((Annotation) new ViewMapAnnotationLiteral());
    }

    /**
     * Get the scope.
     *
     * @return the scope.
     */
    @Override
    public Class<? extends Annotation> getScope() {
        return RequestScoped.class;
    }

    /**
     * Get the stereotypes.
     *
     * @return the stereotypes.
     */
    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return emptySet();
    }

    /**
     * Get the types.
     *
     * @return the types.
     */
    @Override
    public Set<Type> getTypes() {
        return types;
    }

    /**
     * Is this an alternative.
     *
     * @return false.
     */
    @Override
    public boolean isAlternative() {
        return false;
    }

    /**
     * Is this nullable.
     *
     * @return false.
     */
    @Override
    public boolean isNullable() {
        return false;
    }
}
