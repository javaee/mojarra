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
package com.sun.faces.cdi;

import java.util.Set;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;

/**
 * A static utility class for CDI.
 */
public class CdiUtils {

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
        Converter result = null;
        CdiConverterAnnotation annotation = new CdiConverterAnnotation(value, Object.class);
        Set<Bean<?>> beanSet = beanManager.getBeans(Converter.class, annotation);
        if (!beanSet.isEmpty()) {
            Bean<?> bean = beanManager.resolve(beanSet);
            if (bean != null) {
                result = new CdiConverter(value, Object.class, (Converter) beanManager.
                        getReference(bean, Converter.class,
                                beanManager.createCreationalContext(bean)));
            }
        }
        return result;
    }

    /**
     * Create a converter using the FacesConverter forClass attribute.
     *
     * @param beanManager the bean manager.
     * @param forClass the for class.
     * @return the converter, or null if we could not match one.
     */
    public static Converter createConverter(BeanManager beanManager, Class forClass) {
        Converter result = null;
        CdiConverterAnnotation annotation = new CdiConverterAnnotation("", forClass);
        Set<Bean<?>> beanSet = beanManager.getBeans(Converter.class, annotation);
        if (!beanSet.isEmpty()) {
            Bean<?> bean = beanManager.resolve(beanSet);
            if (bean != null) {
                result = new CdiConverter("", forClass, (Converter) beanManager.
                        getReference(bean, Converter.class,
                                beanManager.createCreationalContext(bean)));
            }
        }
        return result;
    }

    /**
     * Create a validator using the FacesValidator value attribute.
     *
     * @param beanManager the bean manager.
     * @param value the value attribute.
     * @return the validator, or null if we could not match one.
     */
    public static Validator createValidator(BeanManager beanManager, String value) {
        Validator result = null;
        CdiValidatorAnnotation annotation = new CdiValidatorAnnotation(value);
        Set<Bean<?>> beanSet = beanManager.getBeans(Validator.class, annotation);
        if (!beanSet.isEmpty()) {
            Bean<?> bean = beanManager.resolve(beanSet);
            if (bean != null) {
                result = new CdiValidator(value, (Validator) beanManager.
                        getReference(bean, Validator.class,
                                beanManager.createCreationalContext(bean)));
            }
        }
        return result;
    }
}
