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
package com.sun.faces.mock;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * <p>
 * Mock implementation of {@link PropertyResolver} that supports a limited
 * subset of expression evaluation functionality:</p>
 * <ul>
 * <li>Supports <code>getValue()</code> and <code>setValue()</code> methods that
 * take a String second argument.</li>
 * <li>Supports property getting and setting as provided by
 * <code>PropertyUtils.getSimpleProperty()</code> and
 * <code>PropertyUtils.setSimpleProperty()</code>.</li>
 * </ul>
 */
public class MockPropertyResolver extends PropertyResolver {

    // ------------------------------------------------------------ Constructors
    // ------------------------------------------------ PropertyResolver Methods
    public Object getValue(Object base, Object property)
            throws EvaluationException, PropertyNotFoundException {

        if (base == null) {
            throw new NullPointerException();
        }
        String name = property.toString();
        try {
            if (base instanceof Map) {
                Map map = (Map) base;
                if (map.containsKey(name)) {
                    return (map.get(name));
                } else {
                    throw new PropertyNotFoundException(name);
                }
            } else {
                return (PropertyUtils.getSimpleProperty(base, name));
            }
        } catch (IllegalAccessException e) {
            throw new EvaluationException(e);
        } catch (InvocationTargetException e) {
            throw new EvaluationException(e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new PropertyNotFoundException(name);
        }
    }

    public Object getValue(Object base, int index)
            throws PropertyNotFoundException {
        throw new UnsupportedOperationException();
    }

    public void setValue(Object base, Object property, Object value)
            throws PropertyNotFoundException {

        if (base == null) {
            throw new NullPointerException();
        }
        String name = property.toString();
        try {
        if (base instanceof Map) {
            ((Map) base).put(name, value);
        } else {
                PropertyUtils.setSimpleProperty(base, name, value);
        }
        } catch (IllegalAccessException e) {
            throw new EvaluationException(e);
        } catch (InvocationTargetException e) {
            throw new EvaluationException(e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new PropertyNotFoundException(name);
        }
    }

    public void setValue(Object base, int index, Object value)
            throws PropertyNotFoundException {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly(Object base, Object property)
            throws PropertyNotFoundException {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly(Object base, int index)
            throws PropertyNotFoundException {
        throw new UnsupportedOperationException();
    }

    public Class getType(Object base, Object property)
            throws PropertyNotFoundException {
        throw new UnsupportedOperationException();
    }

    public Class getType(Object base, int index)
            throws PropertyNotFoundException {
        throw new UnsupportedOperationException();
    }
}
