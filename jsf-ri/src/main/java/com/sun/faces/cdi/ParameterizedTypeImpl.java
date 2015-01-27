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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p class="changed_added_2_3">
 * ParameterizedTypeImpl is a basic implementation of the ParameterizedType
 * interface. It is used by the dynamic CDI producers that produce generic
 * types.
 * </p>
 *
 * @since 2.3
 */
public class ParameterizedTypeImpl implements ParameterizedType {

    /**
     * Stores the owner type.
     */
    private final Type ownerType;

    /**
     * Stores the raw type.
     */
    private final Class<?> rawType;

    /**
     * Stores the actual type arguments.
     */
    private final Type[] actualTypeArguments;

    /**
     * Constructs an instance of ParameterizedType without an owner type
     *
     * @param rawType Type representing the class or interface that declares
     * this type.
     * @param actualTypeArguments Array of Types representing the actual type
     * arguments for this type
     */
    public ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments) {
        this(null, rawType, actualTypeArguments);
    }

    /**
     * Constructs an instance of ParameterizedType
     *
     * @param ownerType the Type representing the type that this type is
     * embedded in, if any. It can be null.
     * @param rawType the Type representing the class or interface that declares
     * this type.
     * @param actualTypeArguments Array of Types representing the actual type
     * arguments for this type
     */
    public ParameterizedTypeImpl(Type ownerType, Class<?> rawType,
            Type[] actualTypeArguments) {

        this.ownerType = ownerType;
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
    }

    /**
     * Get the owner type.
     *
     * @return the owner type.
     */
    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    /**
     * Get the raw type.
     *
     * @return the raw type.
     */
    @Override
    public Type getRawType() {
        return rawType;
    }

    /**
     * Get the actual type arguments.
     *
     * @return the actual type arguments.
     */
    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    /**
     * Equals method.
     *
     * @param other the object to compare against.
     * @return true if it is equals, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof ParameterizedType
                ? equals((ParameterizedType) other) : false;
    }

    /**
     *
     * Tests if an other instance of ParameterizedType is "equal to" this
     * instance.
     *
     * @param other the other instance of ParameterizedType
     * @return true if instances equal, false otherwise.
     */
    public boolean equals(ParameterizedType other) {
        return this == other ? true
                : Objects.equals(getOwnerType(), other.getOwnerType())
                && Objects.equals(getRawType(), other.getRawType())
                && Arrays.equals(getActualTypeArguments(),
                        other.getActualTypeArguments());
    }

    /**
     * Hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getOwnerType())
                ^ Objects.hashCode(getRawType())
                ^ Arrays.hashCode(getActualTypeArguments());
    }
}
