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

package javax.faces.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;

/**
 * <p class="changed_added_2_0"><span class="changed_modified_2_2">The</span>
 * presence of this annotation on a
 * class automatically registers the class with the runtime as a {@link
 * Validator}.  The value of the {@link #value} attribute is taken to be
 * the <em>validator-id</em> and the fully qualified class name of the
 * class to which this annotation is attached is taken to be the
 * <em>validator-class</em>.  The implementation must guarantee that for
 * each class annotated with <code>FacesValidator</code>, found with the
 * algorithm in section JSF.11.5,
 * {@link
 * javax.faces.application.Application#addValidator(java.lang.String,java.lang.String)}
 * is called, passing the derived <em>validator-id</em> as the first
 * argument and the derived <em>validator-class</em> as the second
 * argument.  The implementation must guarantee that all such calls to
 * <code>addValidator()</code> happen during application startup time
 * and before any requests are serviced.</p>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface FacesValidator {

    /**
     * <p class="changed_added_2_0"><span class="changed_modified_2_2">The</span> value of this annotation
     * attribute is taken to be the <em>validator-id</em> with which
     * instances of this class of component can be instantiated by
     * calling {@link
     * javax.faces.application.Application#createValidator(java.lang.String)}.
     * <span class="changed_added_2_2">If no value is specified, or the value is
     * <code>null</code>, the value is taken to be the return of calling
     * <code>getSimpleName</code> on the class to which this annotation
     * is attached and lowercasing the first character.  If more than one
     * validator with this derived name is found, the results are undefined.</span></p>
     */ 

    String value() default "";

    /**
     * <p class="changed_added_2_0">If <code>true</code>, the validator
     * id for this annotation is added to the list of default validators
     * by a call to {@link
     * javax.faces.application.Application#addDefaultValidatorId}.</p>
     */ 

    boolean isDefault() default false;


}
