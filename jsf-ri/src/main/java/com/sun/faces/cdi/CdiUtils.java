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

import static java.util.Optional.empty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.TypeLiteral;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.DataModel;
import javax.faces.validator.Validator;

import com.sun.faces.util.Util;

/**
 * A static utility class for CDI.
 */
public final class CdiUtils {
    
    private final static Type CONVERTER_TYPE = 
        new TypeLiteral<Converter<?>>() { private static final long serialVersionUID = 1L;}.getType();

    /**
     * Constructor.
     */
    private CdiUtils() {
    }

    /**
     * Create a converter using the FacesConverter value attribute.
     *
     * @param beanManager the bean manager.
     * @param value the value attribute.
     * @return the converter, or null if we could not match one.
     */
    public static Converter<?> createConverter(BeanManager beanManager, String value) {
        Converter<?> managedConverter = createConverter(beanManager, new FacesConverterAnnotationLiteral(value, Object.class));
        
        if (managedConverter != null) {
            return new CdiConverter(value, Object.class, managedConverter);
        }
       
        return null;
    }

    /**
     * Create a converter using the FacesConverter forClass attribute.
     *
     * @param beanManager the bean manager.
     * @param forClass the for class.
     * @return the converter, or null if we could not match one.
     */
    public static Converter<?> createConverter(BeanManager beanManager, Class<?> forClass) {
        Converter<?> managedConverter = createConverter(beanManager, new FacesConverterAnnotationLiteral("", forClass));
        
        if (managedConverter != null) {
            return new CdiConverter("", forClass, managedConverter);
        }
       
        return null;
    }
    
    private static Converter<?> createConverter(BeanManager beanManager, Annotation qualifier) {
        
        // Try to find parameterized converter first     
        Converter<?> managedConverter = (Converter<?>) getBeanReferenceByType(
            beanManager,
            CONVERTER_TYPE,
            qualifier);
        
        if (managedConverter == null) {
            // No parameterized converter, try raw converter            
            managedConverter = getBeanReference(
                beanManager,
                Converter.class,
                qualifier);
        }
        
        return managedConverter;
    }

    /**
     * Create a behavior using the FacesBehavior value attribute.
     * 
     * @param beanManager the bean manager.
     * @param value the value attribute.
     * @return the behavior, or null if we could not match one.
     */
    public static Behavior createBehavior(BeanManager beanManager, String value) {
        Behavior delegatingBehavior = null;
        
        Behavior managedBehavior = getBeanReference(
            beanManager,
            Behavior.class,
            new FacesBehaviorAnnotationLiteral(value)
        );
        
        if (managedBehavior != null) {
            delegatingBehavior = new CdiBehavior(value, managedBehavior);
        }
        
        return delegatingBehavior;
    }
    
    /**
     * Create a validator using the FacesValidator value attribute.
     *
     * @param beanManager the bean manager.
     * @param value the value attribute.
     * @return the validator, or null if we could not match one.
     */
    public static Validator createValidator(BeanManager beanManager, String value) {
        
        Validator delegatingValidator = null;
        
        Validator managedValidator = getBeanReference(
            beanManager,
            Validator.class,
            new FacesValidatorAnnotationLiteral(value)
        );
        
        if (managedValidator != null) {
            delegatingValidator = new CdiValidator(value, managedValidator);
        }
    
        return delegatingValidator;
    }

    public static <T> T getBeanReference(Class<T> type, Annotation... qualifiers) {
        return type.cast(getBeanReferenceByType(Util.getCdiBeanManager(FacesContext.getCurrentInstance()), type, qualifiers));
    }

    /**
     * @param beanManager the bean manager
     * @param type the required bean type the reference must have
     * @param qualifier the required qualifiers the reference must have
     * @return a bean reference adhering to the required type and qualifiers
     */
    public static <T> T getBeanReference(BeanManager beanManager, Class<T> type, Annotation... qualifiers) {
        return type.cast(getBeanReferenceByType(beanManager, type, qualifiers));
    }

    public static Object getBeanReferenceByType(BeanManager beanManager, Type type, Annotation... qualifiers) {

        Object beanReference = null;

        Bean<?> bean = beanManager.resolve(beanManager.getBeans(type, qualifiers));
        if (bean != null) {
            beanReference = beanManager.getReference(bean, type, beanManager.createCreationalContext(bean));
        }

        return beanReference;
    }

    /**
     * Returns concrete (non-proxied) bean instance of given class in current context.
     * 
     * @param type the required bean type the instance must have
     * @param create whether to auto-create bean if not exist
     * @return a bean instance adhering to the required type
     */
    public static <T> T getBeanInstance(Class<T> type, boolean create) {
        BeanManager beanManager = Util.getCdiBeanManager(FacesContext.getCurrentInstance());
        Bean<T> bean = (Bean<T>) beanManager.resolve(beanManager.getBeans(type));

        if (bean != null) {
            Context context = beanManager.getContext(bean.getScope());

            if (create) {
                return context.get(bean, beanManager.createCreationalContext(bean));
            }
            else {
                return context.get(bean);
            }
        }
        else {
            return null;
        }
    }

    /**
     * Finds an annotation in an Annotated, taking stereo types into account
     * 
     * @param beanManager the current bean manager
     * @param annotated the Annotated in which to search
     * @param annotationType the type of the annotation to search for
     * @return An Optional that contains an instance of annotation type for which was searched if the annotated contained this. 
     */
    public static <A extends Annotation> Optional<A> getAnnotation(BeanManager beanManager, Annotated annotated, Class<A> annotationType) {

        annotated.getAnnotation(annotationType);

        if (annotated.getAnnotations().isEmpty()) {
            return empty();
        }

        if (annotated.isAnnotationPresent(annotationType)) {
            return Optional.of(annotated.getAnnotation(annotationType));
        }

        Queue<Annotation> annotations = new LinkedList<>(annotated.getAnnotations());

        while (!annotations.isEmpty()) {
            Annotation annotation = annotations.remove();

            if (annotation.annotationType().equals(annotationType)) {
                return Optional.of(annotationType.cast(annotation));
            }

            if (beanManager.isStereotype(annotation.annotationType())) {
                annotations.addAll(
                    beanManager.getStereotypeDefinition(
                        annotation.annotationType()
                    )
                );
            }
        }

        return empty();
    }
    
    public static DataModel<?> createDataModel(final Class<?> forClass) {
        
        List<DataModel<?>> dataModel = new ArrayList<DataModel<?>>(1);
        CDI<Object> cdi = CDI.current();
        
        // Scan the map in order, the first class that is a super class or equal to the class for which
        // we're looking for a DataModel is the closest match, since the Map is sorted on inheritance relation
        getDataModelClassesMap(cdi).entrySet().stream()
            .filter(e -> e.getKey().isAssignableFrom(forClass))
            .findFirst()
            .ifPresent(
                    
                 // Get the bean from CDI which is of the class type that we found during annotation scanning
                 // and has the @FacesDataModel annotation, with the "forClass" attribute set to the closest
                 // super class of our target class.
                    
                e -> dataModel.add(
                    cdi.select(
                        e.getValue(),
                        new FacesDataModelAnnotationLiteral(e.getKey())
                    ).get())
            );
        
        return dataModel.isEmpty()? null : dataModel.get(0);
    }
    
    @SuppressWarnings("unchecked")
    public static Map<Class<?>, Class<? extends DataModel<?>>> getDataModelClassesMap(CDI<Object> cdi) {
        BeanManager beanManager = cdi.getBeanManager();

        // Get the Map with classes for which a custom DataModel implementation is available from CDI
        Bean<?> bean = beanManager.resolve(beanManager.getBeans("comSunFacesDataModelClassesMap"));
        Object beanReference = beanManager.getReference(bean, Map.class, beanManager.createCreationalContext(bean));
        
        return (Map<Class<?>, Class<? extends DataModel<?>>>) beanReference;
    }

    /** 
     * Returns the current injection point.
     */
    public static InjectionPoint getCurrentInjectionPoint(BeanManager beanManager, CreationalContext<?> creationalContext) {
        Bean<? extends Object> bean = beanManager.resolve(beanManager.getBeans(InjectionPoint.class));
        InjectionPoint injectionPoint = (InjectionPoint) beanManager.getReference(bean, InjectionPoint.class, creationalContext);

        if (injectionPoint == null) { // It's broken in some Weld versions. Below is a work around. 
            bean = beanManager.resolve(beanManager.getBeans(InjectionPointGenerator.class));
            injectionPoint = (InjectionPoint) beanManager.getInjectableReference(bean.getInjectionPoints().iterator().next(), creationalContext);
        }

        return injectionPoint;
    }

    /**
     * Returns the qualifier annotation of the given qualifier class from the given injection point.
     */
    public static <A extends Annotation> A getQualifier(InjectionPoint injectionPoint, Class<A> qualifierClass) {
        for (Annotation annotation : injectionPoint.getQualifiers()) {
            if (qualifierClass.isAssignableFrom(annotation.getClass())) {
                return qualifierClass.cast(annotation);
            }
        }

        return null;
    }
    
    /**
     * Returns true if given scope is active in current context.
     */
    public static <S extends Annotation> boolean isScopeActive(Class<S> scope) {
        BeanManager beanManager = Util.getCdiBeanManager(FacesContext.getCurrentInstance());

        try {
            Context context = beanManager.getContext(scope);
            return context.isActive();
        } catch (ContextNotActiveException ignore) {
            return false;
        }
    }

}
