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

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.component.behavior.Behavior;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;

/**
 * A static utility class for CDI.
 */
public final class CdiUtils {

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
    public static Converter createConverter(BeanManager beanManager, String value) {
        Converter delegatingConverter = null;
        
        Converter managedConverter = getBeanReference(
            beanManager,
            Converter.class,
            new FacesConverterAnnotationLiteral(value, Object.class)
        );
        
        if (managedConverter != null) {
            delegatingConverter = new CdiConverter(value, Object.class, managedConverter);
        }
       
        return delegatingConverter;
    }

    /**
     * Create a converter using the FacesConverter forClass attribute.
     *
     * @param beanManager the bean manager.
     * @param forClass the for class.
     * @return the converter, or null if we could not match one.
     */
    public static Converter createConverter(BeanManager beanManager, Class forClass) {
        Converter delegatingConverter = null;
        
        Converter managedConverter = getBeanReference(
            beanManager,
            Converter.class,
            new FacesConverterAnnotationLiteral("", forClass)
        );
        
        if (managedConverter != null) {
            delegatingConverter = new CdiConverter("", forClass, managedConverter);
        }
       
        return delegatingConverter;
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
    
    /**
     * 
     * @param beanManager the bean manager
     * @param type the required bean type the reference must have
     * @param qualifier the required qualifiers the reference must have
     * @return a bean reference adhering to the required type and qualifiers
     */
    public static <T> T getBeanReference(BeanManager beanManager, Class<T> type, Annotation qualifier) {
        
        Object beanReference = null;
              
        Bean<?> bean = beanManager.resolve(beanManager.getBeans(type, qualifier));
        if (bean != null) {
            beanReference = beanManager.getReference(
                bean, type, beanManager.createCreationalContext(bean)
            );
        }
                
        return type.cast(beanReference);
    }
}
