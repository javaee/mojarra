/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDLGPL_1_1.html
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

import static com.sun.faces.util.CollectionsUtils.asSet;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Function;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
import javax.faces.context.FacesContext;

import com.sun.faces.config.WebConfiguration;

/**
 * An abstract base class used by the CDI producers for some common
 * functionality.
 * 
 * @since 2.3
 */
abstract class CdiProducer<T> implements Bean<T>, PassivationCapable, Serializable {
    
    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Stores the active flag.
     */
    private Boolean active;
    
    private String name;
    private Class<?> beanClass = Object.class;
    private Set<Type> types = singleton(Object.class);
    private Set<Annotation> qualifiers = singleton(new DefaultAnnotationLiteral());
    private Class<? extends Annotation> scope = Dependent.class;
    private Function<CreationalContext<T>, T> create;
    
    /**
     * Get the ID of this particular instantiation of the producer.
     * <p>
     * This is an implementation detail of CDI, where it wants to relocate
     * a particular producer in order to re-inject a value. This is typically
     * used in combination with passivation. Note that this is NOT about
     * the value we're producing, but about the producer itself.
     * 
     * @return the ID of this particular instantiation of the producer
     */
    @Override
    public String getId() {
        // In general just the class name is enough to fully identify
        // a particular producer. If functionally different instances of
        // the same producer class exists (e.g. when using a constructor
        // with arguments) a subclass needs a return a more specific
        // value here.
        return this.getClass().getName();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Class<?> getBeanClass() {
       return beanClass;
    }
    
    @Override
    public Set<Type> getTypes() {
        return types;
    }
    
    /**
     * Get the default qualifier.
     *
     * @return the qualifiers, which in the default case only contains the Default
     */
    @Override
    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }
    
    @Override
    public Class<? extends Annotation> getScope() {
        return scope;
    }
    
    @Override
    public T create(CreationalContext<T> creationalContext) {
        checkActive();
        return create.apply(creationalContext);
    }
    
    /**
     * Destroy the instance.
     *
     * <p>
     * Since most artifact that the sub classes are producing 
     * are artifacts that the JSF runtime really is
     * managing the destroy method here does not need to do anything.
     * </p>
     *
     * @param instance the instance.
     * @param creationalContext the creational context.
     */
    @Override
    public void destroy(T instance, CreationalContext<T> creationalContext) {
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
     * Get the stereotypes.
     *
     * @return the stereotypes.
     */
    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return emptySet();
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

    /**
     * Check if we are active.
     */
    protected void checkActive() {
        if (active == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            WebConfiguration webConfig = WebConfiguration.getInstance(facesContext.getExternalContext());
            active = webConfig.isOptionEnabled(WebConfiguration.BooleanWebContextInitParameter.EnableCdiResolverChain);
        }

        if (!active) {
            throw new IllegalStateException("Cannot use @Inject without setting context-param \"javax.faces.ENABLE_CDI_RESOLVER_CHAIN\" to \"true\"");
        }
    }
    
    protected CdiProducer<T> active(boolean active) {
        this.active = active;
        return this;
    }
    
    protected CdiProducer<T> name(String name) {
        this.name = name;
        return this;
    }
    
    protected CdiProducer<T> create(Function<CreationalContext<T>, T> create) {
        this.create = create;
        return this;
    }
    
    protected CdiProducer<T> beanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
        return this;
    }
    
    protected CdiProducer<T> types(Type... types) {
        this.types = asSet(types);
        return this;
    }
    
    protected CdiProducer<T> beanClassAndType(Class<?> beanClass) {
        beanClass(beanClass);
        types(beanClass);
        return this;
    }
    
    protected CdiProducer<T> qualifiers(Annotation... qualifiers) {
        this.qualifiers = asSet(qualifiers);
        return this;
    }
    
    
    protected CdiProducer<T> scope(Class<? extends Annotation> scope) {
        this.scope = scope;
        return this;
    }
    
}
