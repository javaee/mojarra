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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.faces.view.facelets;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * <p class="changed_added_2_0">Information used with {@link MetaRule}
 * for determining how and what {@link Metadata} should be wired.</p>
 *
 * @since 2.0
 */
public abstract class MetadataTarget {

    /**
     * <p class="changed_added_2_0">Return a beans
     * <code>PropertyDescriptor</code> for the property with name given
     * by argument <code>name</code>.</p>
     * @param name the name of the property for which the
     * <code>PropertyDescriptor</code> must be returned.
     */
    public abstract PropertyDescriptor getProperty(String name);

    /**
     * <p class="changed_added_2_0">Return true if the target for this
     * metadata element is an instance of the argument
     * <code>type</code>.</p>
     * @param type the <code>Class</code> to test for the instance of.
     */
    public abstract boolean isTargetInstanceOf(Class type);

    /**
     * <p class="changed_added_2_0">Return the <code>Class</code> of the
     * metadata target.</p>
     */
    public abstract Class getTargetClass();

    /**
     * <p class="changed_added_2_0">Return a 
     * <code>Class</code> for the property with name given
     * by argument <code>name</code>.</p>
     * @param name the name of the property for which the
     * <code>Class</code> must be returned.
     */
    public abstract Class getPropertyType(String name);

    /**
     * <p class="changed_added_2_0">Return a <code>Method</code> for the
     * setter of the property with name given by argument
     * <code>name</code>.</p>
     * @param name the name of the property for which the
     * <code>Method</code> must be returned.
     */
    public abstract Method getWriteMethod(String name);

    /**
     * <p class="changed_added_2_0">Return a <code>Method</code> for the
     * getter of the property with name given by argument
     * <code>name</code>.</p>
     * @param name the name of the property for which the
     * <code>Method</code> must be returned.
     */
    public abstract Method getReadMethod(String name);

}
